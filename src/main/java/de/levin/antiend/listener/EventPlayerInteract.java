package de.levin.antiend.listener;

import com.google.inject.Inject;
import de.levin.antiend.Data.ConfigurationRepository;
import de.levin.antiend.other.MessagesHelper;
import lombok.var;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.stream.Collectors;

public class EventPlayerInteract implements Listener {

    private final ConfigurationRepository config;

    public List<String> disabledWorlds;
    private final MessagesHelper messagesHelper;

    @Inject
    public EventPlayerInteract(ConfigurationRepository config, MessagesHelper messagesHelper) {
        this.config = config;
        this.messagesHelper = messagesHelper;
        this.disabledWorlds = getDisabledWorlds(config);
    }

    private static List<String> getDisabledWorlds(ConfigurationRepository config){
        return config.getEntryDisabledInWorlds().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getItem() == null)
            return;

        if (event.getItem().getType() != Material.ENDER_EYE)
            return;

        if (config.isDisableEnd() == false)
            return;

        var playerWorld = player.getWorld().getName().toLowerCase();
        if (disabledWorlds.contains(playerWorld) == false)
            return;

        var bypass = config.getBypass();
        if (bypass.isAllowBypass())
            if (player.hasPermission(bypass.getPermission()) || player.hasPermission("antiend.*") || player.isOp())
                return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {
            if (!disabledWorlds.contains(player.getWorld().getName().toLowerCase()))
                return;
        }

        event.setCancelled(true);
        messagesHelper.sendPreventMessage(player);
    }
}
