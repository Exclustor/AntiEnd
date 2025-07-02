package de.levin.antiend.flyingtext;

import com.google.inject.Inject;
import de.levin.antiend.AntiEnd;
import de.levin.antiend.data.IDatabase;
import de.levin.antiend.data.Result;
import de.levin.antiend.data.repository.ConfigurationRepository;
import de.levin.antiend.data.repository.MessagesRepository;
import de.levin.antiend.other.Logger;
import de.levin.antiend.viewmodel.FlyingTextViewModel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.HashSet;

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
        var flyingTextViewModels = new HashSet<FlyingTextViewModel>();
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
                flyingTextViewModels.add(new FlyingTextViewModel(entity.getUniqueId(), line));

                height -= config.getHologram().getSpaceBetweenEachLine();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.Error;
        }

        StartUpdatingDuration();
        return database.save(flyingTextViewModels);
    }

    private void StartUpdatingDuration() {
        Bukkit.getScheduler().runTaskTimer(AntiEnd.getInstance(), () -> {
                    var timer = database.getDurationEndDisabled();
                    if (timer == 0) {
                        database.setEndStatus(!database.getEndStatus());
                        database.setDurationEndDisabled(config.getHologram().getDynamicOpening().getDuration());
                        updateText(STATUS_PLACEHOLDER, getEndStatusText());
                        updateText(STATUS_DURATION, getDurationText());
                        return;
                    }

                    database.setDurationEndDisabled(--timer);
                    updateText(STATUS_DURATION, getDurationText());
                },
                0L, 20L
        );
    }

    private void configureArmorStand(ArmorStand armorStand, String name) {
        armorStand.setMarker(true);
        armorStand.setVisible(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(replaceCustomName(name));
    }

    private String replaceCustomName(String name) {
        return ChatColor.translateAlternateColorCodes('&', name
                .replace(STATUS_PLACEHOLDER, getEndStatusText())
                .replace(STATUS_DURATION, getDurationText()));
    }

    private String getEndStatusText() {
        return database.getEndStatus() ? messages.getHologramCreatedStatusEnabled() : messages.getHologramCreatedStatusDisabled();
    }

    private String getDurationText() {
        return String.valueOf(database.getDurationEndDisabled());
    }

    @Override
    public Result delete() {
        try {
            database.getAll().forEach(model -> Bukkit.getEntity(model.getUuid()).remove());
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
            for (var model : uuidsWithBoolean) {
                var armorStand = Bukkit.getEntity(model.getUuid());
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
    public void updateText(String placeholder, String value) {
        database.getAll().forEach((model) -> {
            var text = model.getOriginalText();
            if (text.contains(placeholder) == false)
                return;

            var entity = Bukkit.getEntity(model.getUuid());
            if (entity == null)
                return;

            text = ChatColor.translateAlternateColorCodes('&', text.replace(placeholder, value));

            var armorStand = (ArmorStand) entity;
            armorStand.setCustomName(text);
        });
    }

//    public void Test(){
//        if(database.getEndStatus() == false){
//            var runnable = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
//                database.getAll().forEach((uuid, isUpdatable) -> {
//                    var entity = Bukkit.getEntity(uuid);
//                    if (entity == null)
//                        return;
//
//                    var armorStand = (ArmorStand) entity;
//                    armorStand.setCustomName(config.getHologram().getHologramText().get(0));
//                });
//            }, 0, 20L);
//        }
//    }

}
