package de.levin.antiend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.levin.antiend.viewmodel.FlyingTextViewModel;
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
    private volatile HashSet<FlyingTextViewModel> entityUUIDs = new HashSet<>();
    private volatile boolean endStatus;
}
