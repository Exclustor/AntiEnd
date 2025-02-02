package de.levin.antiend;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import de.levin.antiend.data.IDatabase;
import de.levin.antiend.data.JsonDatabase;
import de.levin.antiend.data.repository.ConfigurationRepository;
import de.levin.antiend.data.repository.MessagesRepository;
import de.levin.antiend.data.repository.RepositoryFactory;
import de.levin.antiend.flyingtext.Hologram;
import de.levin.antiend.flyingtext.IFlyingText;
import de.levin.antiend.listener.EventPlayerInteract;
import de.levin.antiend.listener.EventPlayerTeleport;
import de.levin.antiend.other.FileManager;
import de.levin.antiend.other.Logger;
import de.levin.antiend.other.MessagesHelper;

import java.io.File;

public class DependencyManager extends AbstractModule {

    private static final String CONFIG_FILE = "config.yml";

    @Override
    protected void configure() {
        bind(Logger.class).in(Scopes.SINGLETON);
        bind(FileManager.class).in(Scopes.SINGLETON);
        bind(EventPlayerTeleport.class).in(Scopes.SINGLETON);
        bind(EventPlayerInteract.class).in(Scopes.SINGLETON);
        bind(MessagesHelper.class).in(Scopes.SINGLETON);

        bind(IDatabase.class).to(JsonDatabase.class);
        bind(IFlyingText.class).to(Hologram.class);
    }

    @Singleton
    @Provides
    public ConfigurationRepository provideConfiguration(FileManager fileManager) {
        return RepositoryFactory.load(ConfigurationRepository.class, CONFIG_FILE);
    }

    @Singleton
    @Provides
    public MessagesRepository providePlugin(ConfigurationRepository config) {
        return RepositoryFactory.load(MessagesRepository.class, FileManager.MESSAGES_FOLDER + File.separator + config.getLanguage() + ".yml");
    }

    @Singleton
    @Provides
    public AntiEnd providePlugin() {
        return AntiEnd.getInstance();
    }
}