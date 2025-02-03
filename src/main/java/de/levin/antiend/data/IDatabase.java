package de.levin.antiend.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IDatabase {

    /**
     * UUID    -> Bukkit Entity <br>
     * Boolean -> Is updatable
     * */
    Result save(Map<UUID, Boolean> uuidBooleanMap);
    Result delete();
    boolean getEndStatus();
    boolean exists();
    Map<UUID, Boolean> getAll();
    int getDurationEndDisabled();
}

