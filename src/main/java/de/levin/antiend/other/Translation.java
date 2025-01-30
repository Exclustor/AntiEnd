package de.levin.antiend.other;

import lombok.var;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Translation {

    public static final String MESSAGES_FOLDER = "messages";

    private static final List<Locale> supportedLanguages = new ArrayList<>(
            Arrays.asList(Locale.ENGLISH, Locale.GERMAN));

    public static void saveMessages(JavaPlugin pluginInstance) {
        File messagesFolder = new File(pluginInstance.getDataFolder() + File.separator + MESSAGES_FOLDER);
        if (messagesFolder.exists() == false)
            messagesFolder.mkdir();

        for (Locale language : supportedLanguages) {
            var languageFileName = language + ".yml";
            File file = new File(MESSAGES_FOLDER, languageFileName);
            if (file.exists() == false)
                pluginInstance.saveResource(MESSAGES_FOLDER + File.separator + languageFileName, false);
        }
    }
}