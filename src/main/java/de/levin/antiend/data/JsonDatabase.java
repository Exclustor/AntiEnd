package de.levin.antiend.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.annotations.Expose;
import de.levin.antiend.AntiEnd;
import de.levin.antiend.data.repository.ConfigurationRepository;
import de.levin.antiend.other.Logger;
import jakarta.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class JsonDatabase implements IDatabase {

    private final JsonObjects jsonObjects;
    private final ConfigurationRepository config;
    @Expose(serialize = false, deserialize = false)
    private transient final Logger logger;

    private static final String FILE_PATH = AntiEnd.getInstance().getDataFolder() + File.separator + "database.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public JsonDatabase(JsonObjects jsonObjects, ConfigurationRepository config, Logger logger) {
        this.jsonObjects = jsonObjects;
        this.config = config;
        this.logger = logger;
    }

    @Override
    public Result save(List<UUID> uuids) {
        jsonObjects.getEntityUUIDs().addAll(uuids);
        return save();
    }

    @Override
    public Result delete() {
        if (jsonObjects.getEntityUUIDs().isEmpty())
            return Result.Failed;

        jsonObjects.setEntityUUIDs(new ArrayList<>());
        return save();
    }

    @Override
    public boolean getEndStatus() {
        return jsonObjects.isEndStatus();
    }

    @Override
    public boolean exists() {
        return jsonObjects.getEntityUUIDs().isEmpty();
    }

    @Override
    public List<UUID> getAll() {
        return jsonObjects.getEntityUUIDs();
    }

    @Override
    public int getDurationEndDisabled() {
        return jsonObjects.getDuration();
    }

    private Result save() {
        try {
            objectMapper.writeValue(new File(FILE_PATH), jsonObjects);
        } catch (IOException e) {
            logger.error("Json Datenbank konnte nicht gespeichert werden: " + e.getMessage());
            return Result.Error;
        }
        logger.debug("Json Datenbank wurde gespeichert!");
        return Result.Success;
    }
}

