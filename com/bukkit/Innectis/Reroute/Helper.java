package com.bukkit.Innectis.Reroute;

import java.util.logging.Logger;
import java.util.logging.Level;

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

	public static Block offset(Block loc, int direction)
	{
		World w = loc.getWorld();
		int x = loc.getX(), y = loc.getY(), z = loc.getZ();
		direction %= 4;
		if (direction < 0) direction += 4;
		switch (direction) {
			case 0: // North
				return w.getBlockAt(x-1, y, z);
			case 1: // East
				return w.getBlockAt(z, y, z-1);
			case 2: // South
				return w.getBlockAt(x+1, y, z);
			case 3: // West
				return w.getBlockAt(x, y, z+1);
			default: // Should never get here.
				return w.getBlockAt(x, y, z);
		}
	}

	public static Block getBlock(Block loc)
	{
		return loc.getWorld().getBlockAt(loc.getX(), loc.getY(), loc.getZ());
	}

}
