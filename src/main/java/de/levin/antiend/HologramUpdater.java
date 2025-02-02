package de.levin.antiend;

import de.levin.antiend.data.IDatabase;
import de.levin.antiend.data.repository.ConfigurationRepository;
import org.bukkit.Bukkit;

public class HologramUpdater {

    private final ConfigurationRepository config;
    private final IDatabase database;
    private final AntiEnd plugin;

    public HologramUpdater(ConfigurationRepository config, IDatabase database, AntiEnd plugin) {
        this.config = config;
        this.database = database;
        this.plugin = plugin;
    }

    public void Test(){
        if(database.getEndStatus() == false){
            var runnable = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            }, 0, 20L);
        }
    }
}
