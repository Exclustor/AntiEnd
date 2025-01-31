package de.levin.antiend;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import de.levin.antiend.Data.ConfigurationRepository;
import de.levin.antiend.Data.MessagesRepository;
import de.levin.antiend.Data.RepositoryFactory;
import de.levin.antiend.listener.EventPlayerInteract;
import de.levin.antiend.listener.EventPlayerTeleport;
import de.levin.antiend.other.MessagesHelper;
import de.levin.antiend.other.Translation;

import java.io.File;

public class DependencyManager extends AbstractModule {

    private static final String CONFIG_FILE = "config.yml";

    @Override
    protected void configure() {
        bind(EventPlayerTeleport.class).in(Scopes.SINGLETON);
        bind(EventPlayerInteract.class).in(Scopes.SINGLETON);
        bind(MessagesHelper.class).in(Scopes.SINGLETON);
    }

    @Singleton @Provides
    public ConfigurationRepository provideConfiguration() {
        AntiEnd.getInstance().getLogger().info("loaded config!");
        return RepositoryFactory.load(ConfigurationRepository.class, CONFIG_FILE);
    }

    @Singleton @Provides
    public MessagesRepository provideMessageConfig(ConfigurationRepository config) {
        AntiEnd.getInstance().getLogger().info("loaded messages!!!");
        return RepositoryFactory.load(MessagesRepository.class,
                Translation.MESSAGES_FOLDER + File.separator + config.getLanguage().toString() + ".yml");
    }

}
