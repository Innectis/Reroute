/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.Innectis.Reroute;

import org.bukkit.Material;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleListener;

/**
 * @author Innectis
 */
public class RRVehicleMoveListener extends VehicleListener
{
	private final Reroute plugin;

	public RRVehicleMoveListener(Reroute instance)
	{
		plugin = instance;
	}

	@Override
	public void onVehicleMove(VehicleMoveEvent event)
    {
		Vehicle cart = event.getVehicle();
		int x = event.getFrom().getBlockX();
		int y = event.getFrom().getBlockY();
		int z = event.getFrom().getBlockZ();
		Material m = plugin.getServer().getWorlds()[0].getBlockAt(x, y-1, z).getType();
		if(Material.OBSIDIAN.equals(m)) {
			if(Math.abs(cart.getVelocity().lengthSquared()) > 0.0001) {
				cart.setVelocity(event.getVehicle().getVelocity().normalize().multiply(1000000));
			}
		}
	}
}
