package de.levin.antiend.listener;

import com.google.inject.Inject;
import de.levin.antiend.data.repository.ConfigurationRepository;
import de.levin.antiend.other.MessagesHelper;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EventPlayerTeleport implements Listener {

    private final ConfigurationRepository config;
    private final MessagesHelper messagesHelper;

    @Inject
    public EventPlayerTeleport(ConfigurationRepository config, MessagesHelper messagesHelper) {
        this.config = config;
        this.messagesHelper = messagesHelper;
    }

    @EventHandler
    public void playerJumpIntoVoid(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL) == false)
            return;

        if (config.isDisableEnd() == false || config.isDisableJumpIntoPortal() == false)
            return;

        var player = event.getPlayer();

        config.getSendCommandsOnPortalEnter().forEach(cmd ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                        .replace("%player%", player.getName())
                        .replace("%world%", player.getWorld().getName())
                        .replace("/", "")));
        messagesHelper.sendPreventMessage(event.getPlayer());
        event.setCancelled(true);

    }
}
