package com.bukkit.Innectis.Reroute;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Helper stuff for Reroute
 * @author Innectis
 */

public class Helper {
	
	private final static Logger logger = Logger.getLogger("Minecraft");

	public static void log(String text)
	{
		logger.log(Level.INFO, text);
	}

	public static void logWarning(String text)
	{
		logger.log(Level.WARNING, text);
	}

	public static Location offset(Location loc, int direction)
	{
		World w = loc.getWorld();
		int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
		direction %= 4;
		if (direction < 0) direction += 4;
		switch (direction) {
			case 0: // North
				return new Location(w, x-1, y, z);
			case 1: // East
				return new Location(w, z, y, z-1);
			case 2: // South
				return new Location(w, x+1, y, z);
			case 3: // West
				return new Location(w, x, y, z+1);
			default: // Should never get here.
				return new Location(w, x, y, z);
		}
	}

	public static Block getBlock(Location loc)
	{
		return loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}

}
