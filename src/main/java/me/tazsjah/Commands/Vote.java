package me.tazsjah.Commands;

import me.tazsjah.VoteGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Vote implements CommandExecutor {

    VoteGUI vgui;

    public Vote(VoteGUI vgui) {
        this.vgui = vgui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        File f = new File(Bukkit.getPluginManager().getPlugin("MiniVote").getDataFolder() + "/Votes/");

        if(!f.exists()) {
            sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "No votes found"); return true;
        }

        if(sender.hasPermission("vote.admin") || sender.hasPermission("vote.start")) {
            if(args.length > 0) {
                if(Integer.parseInt(args[0]) < 5) {
                    sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "Time must be higher than 5");
                    return true;
                }

                if (!vgui.isVote) {
                    vgui.giveCompass(vgui.openVotes());
                    vgui.isVote = true;
                    vgui.startVote();
                    vgui.setTime(Integer.parseInt(args[0]));
                } else {
                    sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "Voting has already started");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You must specify the amount of time in seconds /vote (seconds)");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[ ! ] " + ChatColor.GRAY + "You have no permission");
        }
        return false;
    }




}
