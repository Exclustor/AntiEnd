package de.levin.antiend.listener;

import de.levin.antiend.AntiEnd;
import de.levin.antiend.other.Translation;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerInteract implements Listener {

    private FileConfiguration messages;
    private FileConfiguration config;
    private static final Map<String, Long> TIMED_TELL_CACHE = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        config = AntiEnd.getInstance().getConfig();
        messages = Translation.getLanguageConfiguration();
        Player player = event.getPlayer();

        if (player.getItemInHand().getType() == Material.ENDER_EYE || player.getInventory().getItemInOffHand().getType() == Material.ENDER_EYE) {
            if (config.getBoolean("disableEnd")) {

                if (config.getBoolean("bypass.allowBypass")) {
                    if(player.hasPermission(config.getString("bypass.permission")) || player.hasPermission("antiend.*") || player.isOp()) {
                        return;
                    }
                }

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    List<String> worldsFromConfig = config.getStringList("entryDisabledInWorlds").stream().map(String::toLowerCase).collect(Collectors.toList());

                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {
                        if (!worldsFromConfig.contains(player.getWorld().getName().toLowerCase())) {
                            return;
                        }
                    }

                    if(checkCancelSend(event.getPlayer(), worldsFromConfig)){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerJumpIntoVoid(PlayerTeleportEvent event){
        if(event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL) && config.getBoolean("disableEnd")) {
            List<String> worldsFromConfig = config.getStringList("entryDisabledInWorlds").stream().map(String::toLowerCase).collect(Collectors.toList());
            if(checkCancelSend(event.getPlayer(), worldsFromConfig)){
                event.setCancelled(true);
            }
        }
    }

    private boolean checkCancelSend(Player player, List<String> worldsFromConfig){
        if (worldsFromConfig.contains(player.getWorld().getName().toLowerCase())) {
            if (config.getBoolean("sendPreventMessage")) {
                String message = ChatColor.translateAlternateColorCodes('&', messages.getString("preventMessage").replace("%p%", messages.getString("prefix")));
                if (config.getBoolean("sendMessagesAsActionbar", true)) {
                    sendActionText(player, message);
                } else {
                    tellTimed(2, player, message);
                }
            }
            return true;
        }
        return false;
    }

    public static void tellTimed(int delaySeconds, Player player, String message) {
        long currentTime = System.currentTimeMillis() / 1000L;
        if (!TIMED_TELL_CACHE.containsKey(message) || currentTime - TIMED_TELL_CACHE.get(message) > delaySeconds) {
            player.sendMessage(message);
            TIMED_TELL_CACHE.put(message, currentTime);
        }
    }

    public static void sendActionText(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
