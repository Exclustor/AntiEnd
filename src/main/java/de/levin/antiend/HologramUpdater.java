package de.levin.antiend;

import de.levin.antiend.data.IDatabase;
import de.levin.antiend.data.repository.ConfigurationRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;

public class HologramUpdater {

    private final ConfigurationRepository config;
    private final IDatabase database;
    private final AntiEnd plugin;

    public HologramUpdater(ConfigurationRepository config, IDatabase database, AntiEnd plugin) {
        this.config = config;
        this.database = database;
        this.plugin = plugin;
    }


}
