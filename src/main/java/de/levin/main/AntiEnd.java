package de.levin.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.levin.core.Config;
import de.levin.core.Core;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class AntiEnd extends JavaPlugin implements CommandExecutor, Listener, TabCompleter {

	private static AntiEnd plugin;

	File configyml = new File(getDataFolder() + File.separator + "config.yml");
	public static File messagesyml = new File("plugins/AntiEnd/messages.yml");
	public static FileConfiguration messagescfg = YamlConfiguration.loadConfiguration(messagesyml);

	public static void sendActionText(Player player, String message) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
//		getCommand("antiend").setTabCompleter(new AntiEnd());
		// config.yml

		if (!configyml.exists()) {
			saveDefaultConfig();
			// Config.setConfigFile(this.getConfig());
			// saveconfig()
		} else {
			reloadConfig();
		}
		// messages.yml

		if (!messagesyml.exists()) {
			try {
				messagesyml.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Config.setMessagesFile(messagescfg);
			try {
				messagescfg.save(messagesyml);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				messagescfg.save(messagesyml);
			} catch (IOException e) {
				e.printStackTrace();
			}
			messagescfg = YamlConfiguration.loadConfiguration(messagesyml);
		}

		plugin = this;

		if (!(this.getConfig().getBoolean("SlientStart", false))) {
			Bukkit.getConsoleSender().sendMessage("§3AntiEnd §8» §7" + "Enabled");
		}

	}

	@Override
	public void onDisable() {
		if (!(this.getConfig().getBoolean("SlientStart", false))) {
			Bukkit.getConsoleSender().sendMessage("§3AntiEnd §8» §7" + "Disabled");
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getPlayer().getItemInHand().getType() != null && e.getAction() != null
				&& e.getPlayer().getItemInHand().getType() == Material.ENDER_EYE) {

			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (e.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {

					if (getConfig().getBoolean("DisableEnd") == true) {
						
					if (getConfig().getBoolean("BypassPermission", true)
							&& e.getPlayer().hasPermission("antiend.bypass")
									| e.getPlayer().hasPermission("antiend.*")) {
						return;
					} else {
						if (Core.hasEye(e.getClickedBlock().getX(), e.getClickedBlock().getY(),
								e.getClickedBlock().getZ()) == false) {
							e.setCancelled(true);
							sendActionText(e.getPlayer(), messagescfg.getString("PreventMessage").replace("%p%",
									messagescfg.getString("Prefix").replaceAll("&", "§")));
						}
					}
					}

				}
			}

			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null
					&& e.getClickedBlock().getType() != Material.END_PORTAL_FRAME) {

				if (getConfig().getBoolean("DisableEnd") == true) {

					if (getConfig().getBoolean("BypassPermission") == true
							&& e.getPlayer().hasPermission("antiend.bypass")
									| e.getPlayer().hasPermission("antiend.*")) {
						return;

					} else if (getConfig().getBoolean("DisableThrowEnderEye") == true) {
						e.setCancelled(true);
						sendActionText(e.getPlayer(), messagescfg.getString("PreventMessage").replace("%p%",
								messagescfg.getString("Prefix").replaceAll("&", "§")));
					}
				}

			} else if (e.getAction() == Action.RIGHT_CLICK_AIR) if (getConfig().getBoolean("DisableEnd") == true) {

				if (getConfig().getBoolean("BypassPermission") == true
						&& e.getPlayer().hasPermission("antiend.bypass")
						| e.getPlayer().hasPermission("antiend.*")) {
					return;

				} else if (getConfig().getBoolean("DisableThrowEnderEye") == true) {
					e.setCancelled(true);
					sendActionText(e.getPlayer(), messagescfg.getString("PreventMessage").replace("%p%",
							messagescfg.getString("Prefix").replaceAll("&", "§")));
				}
			}
			
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("antiend.use") | !sender.hasPermission("antiend.*")) {
			sender.sendMessage(messagescfg.getString("NoPerms").replace("%p%",
					messagescfg.getString("Prefix").replaceAll("&", "§")));
		} else {
			if (args.length != 1) {
				sender.sendMessage(messagescfg.getString("Help").replace("%p%", messagescfg.getString("Prefix"))
						.replaceAll("&", "§"));

			} else {
				if (args[0].equalsIgnoreCase("help")) {
					List<String> HelpList = messagescfg.getStringList("HelpList");
					for (String l : HelpList) {
						sender.sendMessage(
								l.replace("&", "§").replace("%p%", messagescfg.getString("Prefix")).replace("&", "§")
										.replace("%v%", this.getDescription().getVersion().replace("&", "§")));
					}

				} else {
					if (args[0].equalsIgnoreCase("reload")) {
						reloadConfig();
						messagescfg = YamlConfiguration.loadConfiguration(messagesyml);
						sender.sendMessage(messagescfg.getString("Reload").replace("%p%",
								messagescfg.getString("Prefix").replaceAll("&", "§")));

					} else {
						if (sender.hasPermission("antiend.use") | sender.hasPermission("antiend.*")) {
							sender.sendMessage(messagescfg.getString("Help").replace("%p%",
									messagescfg.getString("Prefix").replaceAll("&", "§")));

						}
					}
				}
			}

		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> complete = new ArrayList<String>();
		if (args.length == 1 && sender.hasPermission("antiend.use") | sender.hasPermission("antiend.*")) {
			complete.add("reload");
			complete.add("help");
			return complete;
		}
		return null;

	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}

}