/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.the31.Reroute;

import org.bukkit.Material;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleListener;

/**
 * @author 31
 */
public class RRVehicleMoveListener extends VehicleListener
{
	private final Routecraft plugin;

	public RRVehicleMoveListener(Routecraft instance)
	{
		plugin = instance;
	}

	@Override
	public void onVehicleMove(VehicleMoveEvent event)
	{
		int x = event.getFrom().getBlockX();
		int y = event.getFrom().getBlockY();
		int z = event.getFrom().getBlockZ();
		Material m = plugin.getServer().getWorlds()[0].getBlockAt(x, y-1, z).getType();
		if(Material.OBSIDIAN.equals(m))
			if(event.getVehicle().getVelocity().lengthSquared() != 0.0)
				event.getVehicle().setVelocity(event.getVehicle().getVelocity().normalize().multiply(1000000));
	}
}
