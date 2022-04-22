package de.levin.core;

import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	public static void setConfigFile(FileConfiguration config) {

		// A B C D E F G H I J K L M N O P Q R S T U V W X Y Z

		config.set("BypassPermission", false);

		config.set("DisableEnd", true);

		config.set("PreventMessage", true);

		config.set("SlientStart", false);

	}

	public static void setMessagesFile(FileConfiguration config) {
		config.set("Prefix", "&3AntiEnd &8�&7");
		config.set("NoPerms", "%p% You don't have the permission for doing that.");
		config.set("Reload", "%p% Plugin reloaded.");
		config.set("PreventMessage", "%p% The end is disabled on this server.");
//		config.set("PreventionToggle", "%p% You toggled the end prevention to %s%&7.");
		config.set("Help", "%p% Use /antiend help for help.");
		config.set("HelpList", Arrays.asList("%p% &8&m============&f Version: %v% &8&m============",
				"%p% &8- &7/antiend help &8| &fShow this text", "%p% &8- &7/antiend reload &8| &freload the plugin",
				"%p% &8&m==================================="));

	}

}