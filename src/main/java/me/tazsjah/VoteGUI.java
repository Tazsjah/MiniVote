package me.tazsjah;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class VoteGUI implements Listener {

    HashMap<String, Integer> votes = new HashMap<>();

    Config config;

    public VoteGUI(Config config) {
        this.config = config;
    }

    int size = 0;

    Inventory ginv = null;

    public Inventory openVotes() {
        File vote = new File(Bukkit.getPluginManager().getPlugin("MiniVote").getDataFolder() + "/Votes/");
        File[] f = vote.listFiles();

        if(f.length > 8) {
            size = 18;
        } else {
            size = 9;
        }

        Inventory inv = Bukkit.createInventory(null, size, (String) config.get("gui-name"));

        for(File vf : f) {
            FileConfiguration item = YamlConfiguration.loadConfiguration(vf);

            ItemStack wool = new ItemStack(Material.getMaterial(item.getString("color").toUpperCase() + "_WOOL"), 1);
            ItemMeta meta = wool.getItemMeta();
//            wool.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.getString("display")));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add((String) config.get("item-lore"));

            meta.setLore(lore);
            wool.setItemMeta(meta);
            inv.addItem(wool);
            String name = vf.getName().replace(".yml", "");
        }
        return inv;

    }

    public void giveCompass(Inventory inventory) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            ItemStack item = new ItemStack(Material.getMaterial(config.get("voting-item").toString()), 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName((String) config.get("voting-item-name"));
            item.setItemMeta(meta);

            p.getInventory().addItem(item);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.getMaterial(config.get("voting-item").toString())){
            if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase((String) config.get("voting-item-name"))){
                event.getPlayer().openInventory(openVotes());

                double v = config.getInt("voting-item-sound-volume");
                double p = config.getInt("voting-item-sound-pitch");

                if(!openinv.contains(event.getPlayer().getUniqueId())) {
                    openinv.add(event.getPlayer().getUniqueId());
                }


                event.getPlayer().playSound(event.getPlayer().getLocation(),
                        Sound.valueOf((String) config.get("voting-item-sound")), (float) v, (float) p);
            }
        }
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        if(event.getView().getItem(event.getSlot()) == null) { return; }
        if(event.getWhoClicked() instanceof Player player) {
            if(event.getView().getTitle().equalsIgnoreCase((String) config.get("gui-name"))){
                event.setCancelled(true);

                double v = config.getInt("open-sound-volume");
                double p = config.getInt("open-sound-pitch");

                player.playSound(player.getLocation(), Sound.valueOf(config.get("open-sound").toString()), (float) v, (float) p);
                player.closeInventory();

                if(openinv.contains(player.getUniqueId())) {
                    openinv.add(player.getUniqueId());
                }

                String vote = event.getView().getItem(event.getSlot()).getItemMeta().getDisplayName();
                if(votes.get(vote) == null) {
                    votes.put(vote, 1);
                } else {
                    votes.replace(vote, votes.get(vote) + 1);
                }

                for(ItemStack i : player.getInventory().getContents()) {
                    if(i.getItemMeta().getDisplayName().equalsIgnoreCase((String) config.get("voting-item-name"))){
                        i.setAmount(0);
                        break;
                    }
                }

                player.sendMessage(ChatColor.GRAY + "You have voted for " + vote);
            }
        }
    }

    public List<UUID> openinv = new ArrayList<>();

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        openinv.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        openinv.remove(event.getPlayer().getUniqueId());
    }

    public void removeItems() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            for(ItemStack i : player.getInventory().getContents()) {
                if(i != null) {
                    if (i.getItemMeta().getDisplayName().equalsIgnoreCase((String) config.get("voting-item-name"))) {
                        i.setAmount(0);
                        break;
                    }
                }
            }
            if(openinv.contains(player.getUniqueId())) {
                player.closeInventory();
                openinv.remove(player.getUniqueId());
            }
        }
    }

    public Boolean isVote = false;

    int x = 0;

    public void setTime(int i) {
        x = i;
    }

    public void startVote() {
        new BukkitRunnable(){
            @Override
            public void run() {
                if(isVote) {
                    updateActionbar();
                    if(x != 0) {
                        x--;
                    } else {
                        cancel();
                        removeItems();
                        String z = getSelected();
                        String s = (String) config.get("vote-finished").toString().replace("$top-team", z);
                        Bukkit.broadcastMessage(s);
                        isVote = false;
                        votes.clear();
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Bukkit.getPluginManager().getPlugin("MiniVote"), 0,20);
    }

    public void updateActionbar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(config.get("vote-actionbar").toString().replace("$time", x + "")));
        }
    }

    public String getSelected() {
        if(votes.isEmpty()) {
            File vote = new File(Bukkit.getPluginManager().getPlugin("MiniVote").getDataFolder() + "/Votes/");
            File[] f = vote.listFiles();
            int chosen = (int) Math.floor(Math.random() * f.length);
            FileConfiguration vf = YamlConfiguration.loadConfiguration(f[chosen]);
            return ChatColor.translateAlternateColorCodes('&', vf.getString("display"));
        } else {
            File vote = new File(Bukkit.getPluginManager().getPlugin("MiniVote").getDataFolder() + "/Votes/", Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getKey().toString().toLowerCase());
            FileConfiguration vf = YamlConfiguration.loadConfiguration(vote);
            return ChatColor.translateAlternateColorCodes('&', Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getKey());
        }
    }


}
