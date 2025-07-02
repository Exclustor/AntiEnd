package de.levin.antiend.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.levin.antiend.AntiEnd;
import de.levin.antiend.other.Logger;
import de.levin.antiend.viewmodel.FlyingTextViewModel;
import jakarta.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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
    public Result save(HashSet<FlyingTextViewModel> flyingTextViewModel) {
        jsonObjects.getEntityUUIDs().addAll(flyingTextViewModel);
        return save();
    }

    @Override
    public Result delete() {
        if (jsonObjects.getEntityUUIDs().isEmpty())
            return Result.Failed;

        jsonObjects.setEntityUUIDs(new HashSet<>());
        return save();
    }

    @Override
    public boolean getEndStatus() {
        return jsonObjects.isEndStatus();
    }

    @Override
    public void setEndStatus(boolean endStatus) {
        jsonObjects.setEndStatus(endStatus);
    }

    @Override
    public boolean exists() {
        return jsonObjects.getEntityUUIDs().isEmpty();
    }

    @Override
    public HashSet<FlyingTextViewModel> getAll() {
        return jsonObjects.getEntityUUIDs();
    }

    @Override
    public int getDurationEndDisabled() {
        return jsonObjects.getDuration();
    }

    @Override
    public void setDurationEndDisabled(int duration) {
        jsonObjects.setDuration(duration);
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

