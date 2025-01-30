package de.levin.antiend.listener;

import com.google.inject.Inject;
import de.levin.antiend.Data.ConfigurationRepository;
import de.levin.antiend.other.MessagesHelper;
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
        if (event.getCause().equals(org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.END_PORTAL) == false)
            return;

        if (config.isDisableEnd() == false)
            return;

        messagesHelper.sendPreventMessage(event.getPlayer());
        event.setCancelled(true);

    }
}
