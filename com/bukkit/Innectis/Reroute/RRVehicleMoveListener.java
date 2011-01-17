/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.Innectis.Reroute;

import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleListener;

/**
 * @author 31
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
		if(event.getVehicle() instanceof Minecart)
		{
			Minecart minecart = ((Minecart)event.getVehicle());
			int x = event.getFrom().getBlockX();
			int y = event.getFrom().getBlockY();
			int z = event.getFrom().getBlockZ();
			Material m = plugin.getServer().getWorlds()[0].getBlockAt(x, y-1, z).getType();
			if(Material.OBSIDIAN.equals(m))
			{
				if(event.getVehicle().getVelocity().lengthSquared() < -0.0001
				 ||event.getVehicle().getVelocity().lengthSquared() >  0.0001)
					event.getVehicle().setVelocity(event.getVehicle().getVelocity().normalize().multiply(1000000));
			}
			else if(Material.CHEST.equals(m))
			{
				//Ejects the rider from the vehicle.
				event.getVehicle().eject();
				//Should destroy the cart, but doesn't. At least it lets you
				// destroy it with one click after you get out though.
				minecart.setDamage(1000000);
			}
		}
	}

}
