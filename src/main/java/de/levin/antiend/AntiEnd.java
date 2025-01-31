package de.levin.antiend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.levin.antiend.command.CommandAntiEnd;
import de.levin.antiend.listener.EventPlayerInteract;
import de.levin.antiend.listener.EventPlayerTeleport;
import de.levin.antiend.other.Translation;
import lombok.Getter;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiEnd extends JavaPlugin {

    @Getter
    private static AntiEnd instance;
    private static final String ANTIEND_COMMAND = "antiend";

    @Override
    public void onEnable() {
        instance = this;
        ReloadableStart(false);

        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "AntiEnd " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "Enabled");
    }

    public void ReloadableStart(boolean isReload){
        Translation.saveMessages(this);
        saveDefaultConfig();

        if(isReload)
            reloadConfig();

        var injector = Guice.createInjector(new DependencyManager());

        registerEventHandlers(injector);
        registerCommands(injector);
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
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "AntiEnd " + ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + "Disabled");
    }
}
