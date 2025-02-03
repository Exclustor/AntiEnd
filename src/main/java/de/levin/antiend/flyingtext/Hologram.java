package de.levin.antiend.flyingtext;

import com.google.inject.Inject;
import de.levin.antiend.data.IDatabase;
import de.levin.antiend.data.Result;
import de.levin.antiend.data.repository.ConfigurationRepository;
import de.levin.antiend.data.repository.MessagesRepository;
import de.levin.antiend.other.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.UUID;

public class Hologram implements IFlyingText {

    private final MessagesRepository messages;
    private final ConfigurationRepository config;
    private final IDatabase database;
    private final Logger logger;

    private static final String STATUS_PLACEHOLDER = "%status%";
    private static final String STATUS_DURATION = "%duration%";

    @Inject
    public Hologram(MessagesRepository messages, ConfigurationRepository config, IDatabase database, Logger logger) {
        this.messages = messages;
        this.config = config;
        this.database = database;
        this.logger = logger;
    }

    @Override
    public Result create(Location location) {
        var entityUUIDs = new HashMap<UUID, Boolean>();
        try {
            if (database.exists() == false)
                return Result.Failed;

            var height = location.getY();
            for (var line : messages.getHologramText()) {
                if (line.isEmpty()) {
                    height -= config.getHologram().getSpaceBetweenEachLine();
                    continue;
                }

                var isUpdatable = line.contains(STATUS_PLACEHOLDER) || line.contains(STATUS_DURATION);
                var calculatedLocation = new Location(location.getWorld(), location.getX(), height, location.getZ());
                var entity = Bukkit.getWorld(location.getWorld().getUID()).spawnEntity(calculatedLocation, EntityType.ARMOR_STAND);
                configureArmorStand((ArmorStand) entity, line);
                entityUUIDs.put(entity.getUniqueId(), isUpdatable);

                height -= config.getHologram().getSpaceBetweenEachLine();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.Error;
        }

        return database.save(entityUUIDs);
    }

    private void configureArmorStand(ArmorStand armorStand, String name) {
        armorStand.setMarker(true);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(replaceCustomName(name));
    }

    private String replaceCustomName(String name) {
        return ChatColor.translateAlternateColorCodes('&', name
                .replace(STATUS_PLACEHOLDER, getReplacedStatus())
                .replace(STATUS_DURATION, String.valueOf(database.getDurationEndDisabled())));
    }

    private String getReplacedStatus() {
        if (database.getEndStatus())
            return messages.getHologramCreatedStatusEnabled();
        else
            return messages.getHologramCreatedStatusDisabled();
    }

    @Override
    public Result delete() {
        try {
            database.getAll().keySet().forEach(uuid -> Bukkit.getEntity(uuid).remove());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.Failed;
        }
        return database.delete();
    }

    @Override
    public Result teleport(Location location) {
        var uuidsWithBoolean = database.getAll();
        if (database.getAll().isEmpty())
            return Result.Failed;

        var height = location.getY();
        try {
            for (var uuid : uuidsWithBoolean.keySet()) {
                var armorStand = Bukkit.getEntity(uuid);
                var calculatedLoc = new Location(location.getWorld(), location.getX(), height, location.getZ());

                armorStand.teleport(calculatedLoc);
                height -= config.getHologram().getSpaceBetweenEachLine();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.Failed;
        }

        return Result.Success;
    }

    @Override
    public void updateDuration(int number) {

    }

    @Override
    public void updateStatus(boolean status) {

    }
}
