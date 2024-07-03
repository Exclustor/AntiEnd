package de.levin.antiend.other;

import de.levin.antiend.AntiEnd;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class Translation {

    private final static String MESSAGES_FOLDER = "messages";
    @Getter
    private static final List<Locale> activeLanguages = new ArrayList<>();
    @Setter
    @Getter
    private static Locale currentLanguage;
    private static final List<Locale> supportedLanguages = new ArrayList<>(
            Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
    @Setter
    private static HashMap<Locale, YamlConfiguration> languageConfigurations = new HashMap<>();

    static {
        // Initialisiere currentLanguage nachdem die Instanz von AntiEnd gesetzt wurde
        currentLanguage = Locale.forLanguageTag(AntiEnd.getInstance().getConfig().getString("language"));
    }

    public static void addTranslationFile(Locale language) {
        activeLanguages.add(language);
    }

    public static void removeTranslationFile(Locale language) {
        activeLanguages.remove(language);
    }


    public static void reload() {
        activeLanguages.clear();
        saveFilesToMessagesFolder(AntiEnd.getInstance());
        Translation.setCurrentLanguage(Locale.forLanguageTag(AntiEnd.getInstance().getConfig().getString("language")));
    }

    public static YamlConfiguration getLanguageConfiguration() {
        YamlConfiguration config = languageConfigurations.get(currentLanguage);
        if (config == null) {
            AntiEnd.getInstance().getLogger().severe("getLanguageConfiguration/Config is null!");
        }

        return config;
    }

    public static void saveFilesToMessagesFolder(JavaPlugin pluginInstance) {
        File messagesFolder = new File(pluginInstance.getDataFolder() + File.separator + MESSAGES_FOLDER);
        if (!messagesFolder.exists()) {
            messagesFolder.mkdir();
        }

        for (Locale language : supportedLanguages) {
            File file = new File(messagesFolder, language + ".yml");
            if (!file.exists()) {
                pluginInstance.saveResource(MESSAGES_FOLDER + File.separator + language + ".yml", true);
            }
        }

        for (File file : new File(pluginInstance.getDataFolder() + File.separator + MESSAGES_FOLDER).listFiles()) {
            if (file.getName().endsWith(".yml")) {
                addTranslationFile(Locale.forLanguageTag(file.getName().replace(".yml", "")));
                Locale language = Locale.forLanguageTag(file.getName().replace(".yml", ""));
                languageConfigurations.put(language, YamlConfiguration.loadConfiguration(new File(pluginInstance.getDataFolder() +
                        File.separator + MESSAGES_FOLDER + File.separator + language + ".yml")));
            }
        }
    }
}