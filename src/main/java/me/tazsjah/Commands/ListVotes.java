package me.tazsjah.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListVotes implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        File f = new File(Bukkit.getPluginManager().getPlugin("MiniVote").getDataFolder() + "/Votes/");

        if (!f.exists()) {
            sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "No votes found");
            return true;
        }

        if (sender.hasPermission("vote.admin") || sender.hasPermission("vote.list")) {
            List<String> votes = new ArrayList<>();

            for (File vf : f.listFiles()) {
                String s = vf.getName().replace(".yml", "");
                votes.add(s);
            }

            sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "Current Votes: " + ChatColor.YELLOW + votes.toString().replace("[", "").replace("]", ""));
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You have no permission");
        }

        return false;
    }
}