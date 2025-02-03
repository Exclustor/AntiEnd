package de.levin.antiend.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.levin.antiend.AntiEnd;
import de.levin.antiend.other.Logger;
import jakarta.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonDatabase implements IDatabase {

    private final JsonObjects jsonObjects;

    private transient final Logger logger;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DATABASE_PATH = AntiEnd.getInstance().getDataFolder() + File.separator + "database.json";

    @Inject
    public JsonDatabase(JsonObjects jsonObjects, Logger logger) {
        this.jsonObjects = jsonObjects;
        this.logger = logger;
    }

    @Override
    public Result save(Map<UUID, Boolean> uuidBooleanMap) {
        jsonObjects.getEntityUUIDs().putAll(uuidBooleanMap);
        return save();
    }

    @Override
    public Result delete() {
        if (jsonObjects.getEntityUUIDs().isEmpty())
            return Result.Failed;

        jsonObjects.setEntityUUIDs(new HashMap<>());
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
    public Map<UUID, Boolean> getAll() {
        return jsonObjects.getEntityUUIDs();
    }

    @Override
    public int getDurationEndDisabled() {
        return jsonObjects.getDuration();
    }

    private Result save() {
        try {
            objectMapper.writeValue(new File(DATABASE_PATH), jsonObjects);
        } catch (IOException e) {
            logger.error("Json Datenbank konnte nicht gespeichert werden: " + e.getMessage());
            return Result.Error;
        }
        logger.debug("Json Datenbank wurde gespeichert!");
        return Result.Success;
    }
}

