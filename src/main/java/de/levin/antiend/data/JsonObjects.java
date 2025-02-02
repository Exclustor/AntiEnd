package de.levin.antiend.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonObjects {
    public volatile int duration;
    public volatile List<UUID> entityUUIDs = new ArrayList<>();
    public volatile boolean endStatus;
}
