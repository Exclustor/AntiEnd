package de.levin.antiend.other;

import com.google.inject.Inject;
import de.levin.antiend.AntiEnd;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FileManager {

    public static final String MESSAGES_FOLDER = "messages";
    public static final String CONFIG_FILE = "config.yml";

    private static final List<Locale> supportedLanguages = new ArrayList<>(
            Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
    private final AntiEnd plugin;
    private final Logger logger;

    @Inject
    public FileManager(AntiEnd plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
        setupFiles();
    }

    public void setupFiles() {
        UpdateSaveMessageFiles();
        updateSaveFile(CONFIG_FILE);
    }

    public void UpdateSaveMessageFiles() {
        var messagesFolder = new File(plugin.getDataFolder() + File.separator + MESSAGES_FOLDER);
        if (messagesFolder.exists() == false)
            messagesFolder.mkdir();

        for (var language : supportedLanguages) {
            var languageFileName = language + ".yml";
            updateSaveFile(MESSAGES_FOLDER + File.separator + languageFileName);
        }
    }

    public void updateSaveFile(String resourcePath) {
        var file = new File(plugin.getDataFolder(), resourcePath);
        if (file.exists() == false) {
            plugin.saveResource(resourcePath, false);
            return;
        }

        try {
            var defaultConfigStream = plugin.getResource(resourcePath.replace('\\', '/'));
            if (defaultConfigStream == null) {
                logger.error("Standard Config Ressource not found! Please contact the owner of AntiEnd of this issue: " + resourcePath);
                return;
            }

            var defaultLines = readAllLines(defaultConfigStream);
            var currentLines = readAllLines(new FileInputStream(file));

            var updatedLines = mergeLines(defaultLines, currentLines);
            if (updatedLines != null) {
                try (var writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
                    for (var line : updatedLines)
                        writer.write(line + System.lineSeparator());
                }

                logger.info("Updates successfully files");
            }
        } catch (IOException e) {
            logger.error("Error while updating config.yml: " + e.getMessage());
        }
    }

    private static List<String> readAllLines(InputStream inputStream) throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            var lines = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null)
                lines.add(line);

            return lines;
        }
    }

    private static List<String> mergeLines(List<String> defaultLines, List<String> currentLines) {
        var updatedLines = new ArrayList<>(currentLines);
        var updated = false;

        int indentLevel = 0;
        String lastKey = null;

        for (var defaultLine : defaultLines) {
            var trimmedDefaultLine = defaultLine.trim();

            if (trimmedDefaultLine.isEmpty() || trimmedDefaultLine.startsWith("#")) {
                if (updatedLines.contains(defaultLine) == false) {
                    updatedLines.add(defaultLine);
                    updated = true;
                }
                continue;
            }

            if (trimmedDefaultLine.startsWith("-") == false) {
                lastKey = getKey(trimmedDefaultLine);
                indentLevel = getIndentation(defaultLine);

                if (lineExistsInLines(lastKey, currentLines) == false) {
                    updatedLines.add(defaultLine);
                    updated = true;
                }

                continue;
            }

            if (lastKey != null && listItemExistsInContext(trimmedDefaultLine, updatedLines, indentLevel) == false) {
                updatedLines.add(defaultLine);
                updated = true;
            }
        }

        return updated ? updatedLines : null;
    }

    private static boolean lineExistsInLines(String key, List<String> lines) {
        if (key == null)
            return false;

        for (var existingLine : lines)
            if (existingLine.trim().startsWith(key + ":"))
                return true;

        return false;
    }

    private static boolean listItemExistsInContext(String listItem, List<String> lines, int indentLevel) {
        var indent = new String(new char[indentLevel]).replace('\0', ' ');
        var indentedItem = indent + listItem;

        for (var line : lines)
            if (line.trim().equals(indentedItem))
                return true;

        return false;
    }

    private static String getKey(String line) {
        var keyValue = line.split(":", 2);
        return keyValue.length > 0 ? keyValue[0].trim() : null;
    }

    private static int getIndentation(String line) {
        var count = 0;
        for (char c : line.toCharArray())
            if (c == ' ')
                count++;
            else
                break;

        return count;
    }
}