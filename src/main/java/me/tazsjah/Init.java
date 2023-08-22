package me.tazsjah;

import me.tazsjah.Commands.AddVote;
import me.tazsjah.Commands.ListVotes;
import me.tazsjah.Commands.RemoveVote;
import me.tazsjah.Commands.Vote;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Init extends JavaPlugin {

    public void onEnable() {

        File c = new File(getServer().getPluginManager().getPlugin("MiniVote").getDataFolder(), "config.yml");
        if(!c.exists()){
            this.saveResource("config.yml", false);
        }

        Config config = new Config();
        VoteGUI vgui = new VoteGUI(config);

        Bukkit.getPluginCommand("addvote").setExecutor(new AddVote());
        Bukkit.getPluginCommand("remvote").setExecutor(new RemoveVote());
        Bukkit.getPluginCommand("vote").setExecutor(new Vote(vgui));
        Bukkit.getPluginCommand("listvotes").setExecutor(new ListVotes());

        Bukkit.getPluginManager().registerEvents(vgui, this);

        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "Loading the MiniVote addon");
    }

    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED  + "Disabling the MiniVote addon");
    }

}
