package me.tazsjah.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class RemoveVote implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if(sender.hasPermission("vote.admin") || sender.hasPermission("vote.remvote")) {
            if(args.length > 0) {
                File v = new File(Bukkit.getPluginManager().getPlugin("MiniVote").getDataFolder() + "/Votes/", args[0].toLowerCase() + ".yml");
                if(v.exists()) {
                    v.delete();
                    sender.sendMessage(ChatColor.GRAY + "Removed the vote!");
                } else {
                    sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "This vote does not exist");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You must specify a NAME for the vote. /remvote (name)");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You have no permission");
        }
        return false;
    }
}
