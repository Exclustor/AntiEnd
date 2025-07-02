package de.levin.antiend.flyingtext;

import de.levin.antiend.AntiEnd;
import de.levin.antiend.data.Result;
import jakarta.inject.Inject;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PacketArmorStand implements IFlyingText {

    private static final String METADATA_KEY = "antiend_hologram";
    private final AntiEnd plugin;
    private final List<ArmorStand> armorStands = new ArrayList<>();

    @Inject
    public PacketArmorStand(AntiEnd plugin) {
        this.plugin = plugin;
    }

    @Override
    public Result create(Location location) {
        try {
            return Result.Success;
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Erstellen des ArmorStands: " + e.getMessage());
            e.printStackTrace();
            return Result.Error;
        }
    }

    @Override
    public Result delete() {
        try {
            // Alle ArmorStands entfernen
            for (ArmorStand stand : new ArrayList<>(armorStands)) {
                if (stand != null && !stand.isDead()) {
                    stand.remove();
                }
            }
            armorStands.clear();

            // Zusätzlich alle ArmorStands mit unserer Metadata finden und entfernen (Sicherheit)
            for (Entity entity : plugin.getServer().getWorlds().stream()
                    .flatMap(world -> world.getEntitiesByClass(ArmorStand.class).stream())
                    .filter(as -> as.hasMetadata(METADATA_KEY))
                    .collect(Collectors.toList())) {
                entity.remove();
            }

            return Result.Success;
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Löschen der ArmorStands: " + e.getMessage());
            return Result.Error;
        }
    }

    @Override
    public Result teleport(Location location) {
        try {
            for (ArmorStand stand : new ArrayList<>(armorStands)) {
                if (stand != null && !stand.isDead()) {
                    stand.teleport(location);
                }
            }
            return Result.Success;
        } catch (Exception e) {
            plugin.getLogger().severe("Fehler beim Teleportieren der ArmorStands: " + e.getMessage());
            return Result.Error;
        }
    }
}