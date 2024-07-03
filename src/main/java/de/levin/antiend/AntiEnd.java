package de.levin.antiend;

import de.levin.antiend.command.CommandAntiEnd;
import de.levin.antiend.listener.PlayerInteract;

import de.levin.antiend.other.Translation;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;

public final class AntiEnd extends JavaPlugin {

    @Getter
    private static AntiEnd instance;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Translation.saveFilesToMessagesFolder(this);

        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);

        getCommand("antiend").setExecutor(new CommandAntiEnd());
        getCommand("antiend").setTabCompleter(new CommandAntiEnd());

        if (!(this.getConfig().getBoolean("slientStart", false))) {
            Bukkit.getConsoleSender().sendMessage("§3AntiEnd §8» §7" + "Enabled");
        }
    }

    @Override
    public void onDisable() {
        if (!(this.getConfig().getBoolean("slientStart", false))) {
            Bukkit.getConsoleSender().sendMessage("§3AntiEnd §8» §7" + "Disabled");
        }
    }

    public static void reloadPlugin() {
        AntiEnd instance = AntiEnd.getInstance();
        instance.reloadConfig();
        Translation.reload();
    }
}
