package me.tazsjah.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AddVote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if(sender.hasPermission("vote.admin") || sender.hasPermission("vote.addvote")) {
            if(args.length > 0) {
                if(args.length > 1) {
                    if(args.length > 2) {
                        File v = new File(Bukkit.getPluginManager().getPlugin("MiniVote").getDataFolder() + "/Votes/", args[0].toLowerCase() + ".yml");
                        if (!v.exists()) {
                            if (Material.getMaterial(args[1].toUpperCase() + "_WOOL") != null) {
                                FileConfiguration vf = YamlConfiguration.loadConfiguration(v);
                                String name = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);
                                vf.set("name", name);
                                vf.set("color", args[1]);
                                vf.set("display", args[2]);

                                try {
                                    vf.save(v);
                                    sender.sendMessage(ChatColor.GRAY + "Added the vote!");
                                } catch (IOException e) {
                                    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Could not save file: " + args[0].toLowerCase() + ".yml");
                                    throw new RuntimeException(e);
                                }

                            } else {
                                sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "Invalid wool color. Please use a minecraft related wool color");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "This vote already exists. Use /remvote (name)");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You must specify a DISPLAY NAME for the vote. /addvote (name) (wool color) (color-name)");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You must specify a COLOR for the vote. /addvote (name) (wool color) (color-name)");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You must specify a NAME for the vote. /addvote (name) (wool color) (color-name)");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You have no permission");
        }

        return false;
    }
}
