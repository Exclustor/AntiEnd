package de.levin.antiend.other;

import com.google.inject.Inject;
import de.levin.antiend.data.repository.ConfigurationRepository;
import de.levin.antiend.data.repository.MessagesRepository;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessagesHelper {

    private final ConfigurationRepository config;
    private final MessagesRepository messages;

    private final Map<UUID, Long> TIMED_TELL_CACHE = new HashMap<>();

    @Inject
    public MessagesHelper(ConfigurationRepository config, MessagesRepository messages) {
        this.config = config;
        this.messages = messages;
    }

    public void sendPreventMessage(Player player) {
        if (config.isSendPreventMessage() == false) {
            return;
        }

        String replacedMessage = ChatColor.translateAlternateColorCodes('&',
                messages.getPreventMessage().replace("%p%", messages.getPrefix()));
        if (config.isSendMessagesAsActionbar()) {
            sendActionText(player, replacedMessage);
        } else {
            tellTimed(2, player, replacedMessage);
        }

    }

    public void tellTimed(int delaySeconds, Player player, String message) {
        var currentTime = System.currentTimeMillis() / 1000L;
        var uuid = player.getUniqueId();
        if (!TIMED_TELL_CACHE.containsKey(uuid)
                || currentTime - TIMED_TELL_CACHE.get(uuid) > delaySeconds) {
            player.sendMessage(message);
            TIMED_TELL_CACHE.put(uuid, currentTime);
        }
    }

    public static void sendActionText(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
