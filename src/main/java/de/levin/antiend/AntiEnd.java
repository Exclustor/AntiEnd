package de.levin.antiend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.levin.antiend.command.CommandAntiEnd;
import de.levin.antiend.listener.EventPlayerInteract;
import de.levin.antiend.listener.EventPlayerTeleport;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiEnd extends JavaPlugin {

    @Getter
    private static AntiEnd instance;
    private static final String ANTIEND_COMMAND = "antiend";
    public static final String PREFIX = "\u001B[36mAntiEnd \u001B[30m⚫\u001B[37m\u001B[0m";

    @Override
    public void onEnable() {
        instance = this;
        ReloadableStart(false);

        Bukkit.getConsoleSender().sendMessage(PREFIX + "Enabled");
    }

    public void ReloadableStart(boolean isReload) {
        if (isReload)
            OnReload();

        var injector = Guice.createInjector(new DependencyManager());

        registerEventHandlers(injector);
        registerCommands(injector);
    }

    private void OnReload() {
        // Reloads the config.yml from Bukkit
        reloadConfig();

        // Unregister all event handlers from Bukkit
        HandlerList.unregisterAll(this);

        // Unregister commands
        var cmd = getCommand(ANTIEND_COMMAND);
        if (cmd != null) {
            cmd.setExecutor(null);
            cmd.setTabCompleter(null);
        }

        // Clears the Garbage Collector
        System.gc();
    }

    private void registerCommands(Injector injector) {
        getCommand(ANTIEND_COMMAND).setExecutor(injector.getInstance(CommandAntiEnd.class));
        getCommand(ANTIEND_COMMAND).setTabCompleter(injector.getInstance(CommandAntiEnd.class));
    }

    private void registerEventHandlers(Injector injector) {
        Bukkit.getPluginManager().registerEvents(injector.getInstance(EventPlayerInteract.class), this);
        Bukkit.getPluginManager().registerEvents(injector.getInstance(EventPlayerTeleport.class), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GRAY + "Disabled");
    }
}
