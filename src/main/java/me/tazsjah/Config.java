package me.tazsjah;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    File f = new File(Bukkit.getPluginManager().getPlugin("MiniVote").getDataFolder(), "config.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(f);

    public Object get(String s) {
        if(config.get(s) instanceof String) {
            return ChatColor.translateAlternateColorCodes('&', config.getString(s));
        }
        return config.get(s);
    }

    public int getInt(String s) {
        return config.getInt(s);
    }

}
