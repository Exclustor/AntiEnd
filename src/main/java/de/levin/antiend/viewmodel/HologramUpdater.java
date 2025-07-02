package de.levin.antiend.viewmodel;

import de.levin.antiend.AntiEnd;
import de.levin.antiend.data.IDatabase;
import de.levin.antiend.data.repository.ConfigurationRepository;

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
