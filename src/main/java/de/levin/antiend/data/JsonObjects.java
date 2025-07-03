package de.levin.antiend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.levin.antiend.viewmodel.FlyingTextViewModel;
import lombok.Data;

import java.util.*;

@Data
public class JsonObjects {
    private int duration;
    /**
     * UUID -> Bukkit Entity <br>
     * Boolean -> Is updatable
     */
    private HashSet<FlyingTextViewModel> entityUUIDs = new HashSet<>();
    private boolean endStatus;
}
