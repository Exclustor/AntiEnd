package de.levin.antiend.data;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.UUID;

public interface IDatabase {

    Result save(List<UUID> flyingText);
    Result delete();
    boolean getEndStatus();
    boolean exists();
    List<UUID> getAll();
    int getDurationEndDisabled();
}

