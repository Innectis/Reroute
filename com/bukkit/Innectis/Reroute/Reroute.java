package com.bukkit.Innectis.Reroute;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;

import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Innectis Reroute plugin class
 *
 * Features:
 *   Accelerators: place rails on Obsidian.
 *
 * @author Innectis
 */
public class Reroute extends JavaPlugin
{
	private final RRVehicleMoveListener vehicleListener = new RRVehicleMoveListener(this);
	private final RRBlockListener blockListener = new RRBlockListener(this);

	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

	private final RailGraph rails = new RailGraph();

	public Reroute(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader)
	{
		super(pluginLoader, instance, desc, folder, plugin, cLoader);
	}

	public void onEnable()
	{
		// Register events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.VEHICLE_MOVE, vehicleListener , Priority.Highest, this);
		pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener , Priority.High, this);

		// Say hi
		PluginDescriptionFile pdfFile = this.getDescription();
		Helper.log(pdfFile.getName() + " v" + pdfFile.getVersion() + " enabled");
	}

	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
		Helper.log(pdfFile.getName() + " v" + pdfFile.getVersion() + " disabled");
	}

	public RailGraph getRails()
	{
		return rails;
	}

	public boolean isDebugging(final Player player)
	{
		if (debugees.containsKey(player)) {
			return debugees.get(player);
		} else {
			return false;
		}
	}

	public void setDebugging(final Player player, final boolean value)
	{
		debugees.put(player, value);
	}
}
