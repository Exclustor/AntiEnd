package de.levin.antiend.other;

import com.google.inject.Inject;
import de.levin.antiend.AntiEnd;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {
    private final AntiEnd plugin;

    @Inject
    public Logger(AntiEnd plugin) {
        this.plugin = plugin;
    }

    public void info(String message) {
        Bukkit.getConsoleSender().sendMessage(AntiEnd.PREFIX + message);
    }

    public void debug(String message) {
        Bukkit.getConsoleSender().sendMessage(AntiEnd.PREFIX + message);
    }

    public void error(String message) {
        plugin.getLogger().severe(AntiEnd.PREFIX + message);
    }

}
