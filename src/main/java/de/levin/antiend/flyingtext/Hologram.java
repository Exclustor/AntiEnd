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

import java.util.ArrayList;
import java.util.UUID;

public class Hologram implements IFlyingText {

    private final MessagesRepository messages;
    private final ConfigurationRepository config;
    private final IDatabase database;
    private final Logger logger;

    @Inject
    public Hologram(MessagesRepository messages, ConfigurationRepository config, IDatabase database, Logger logger) {
        this.messages = messages;
        this.config = config;
        this.database = database;
        this.logger = logger;
    }

    @Override
    public Result create(Location location) {
        var entityUUIDs = new ArrayList<UUID>();
        try {
            if (database.exists() == false)
                return Result.Failed;

            var height = location.getY();
            for (var line : messages.getHologramText()) {
                if (line.isEmpty()) {
                    height -= config.getHologram().getSpaceBetweenEachLine();
                    continue;
                }

                var calculatedLocation = new Location(location.getWorld(), location.getX(), height, location.getZ());
                var entity = Bukkit.getWorld(location.getWorld().getUID()).spawnEntity(calculatedLocation, EntityType.ARMOR_STAND);
                configureArmorStand((ArmorStand) entity, line);
                entityUUIDs.add(entity.getUniqueId());

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
                .replace("%status%", getReplacedStatus())
                .replace("%duration%", String.valueOf(database.getDurationEndDisabled())));
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
            database.getAll().forEach(uuid -> Bukkit.getEntity(uuid).remove());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.Failed;
        }
        return database.delete();
    }

    @Override
    public Result teleport(Location location) {
        var uuids = database.getAll();
        if (database.getAll().isEmpty())
            return Result.Failed;

        var height = location.getY();
        try {
            for (var uuid : uuids) {
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
