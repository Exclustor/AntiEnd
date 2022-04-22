package de.levin.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.EndPortalFrame;

public interface Core extends Directional {

	public static boolean hasEye(int x, int y, int z) {
		Location loc = new Location(Bukkit.getWorld("World"), x, y, z);
		EndPortalFrame frame = (EndPortalFrame) loc.getBlock().getBlockData();
		return frame.hasEye();

	}

}
