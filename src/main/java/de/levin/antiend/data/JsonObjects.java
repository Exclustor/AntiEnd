package de.levin.antiend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonObjects {
    private volatile int duration;
    /**
     * UUID -> Bukkit Entity <br>
     * Boolean -> Is updatable
     */
    private volatile Map<UUID, Boolean> entityUUIDs = new HashMap<>();
    private volatile boolean endStatus;
}
