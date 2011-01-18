/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bukkit.Innectis.Reroute;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;

/**
 * Listens for right clicking on signs, which creates a station if it has the
 * first line of "station".
 * @author 31
 */
public class RRBlockListener extends BlockListener
{
	private final Reroute plugin;

	public RRBlockListener(Reroute instance)
	{
		plugin = instance;
	}

	@Override
	public void onBlockRightClick(BlockRightClickEvent event)
	{
		//Check to see if the clicker is me! If not, dont' do anything.
		// It might be possible to make an infinite rail loop! Inspection
		// should be done for this, just in case.
		//Also check to see if it's a sign post.
		if(event.getBlock().getType().equals(Material.SIGN_POST))
		{
			//If it's a sign, get the sign.
			Sign s = (Sign)event.getBlock().getState();
			String[] lines = s.getLines();
			if(lines[0].equals("station")) //First line of the sign must be "station"
			{
				String stationName = lines[1];
				
				//Check for a rail under the player. This will be used as the outgoing rail.
				Location playerloc = event.getPlayer().getLocation();
				if(event.getBlock().getWorld().getBlockAt(playerloc.getBlockX(), playerloc.getBlockY(), playerloc.getBlockZ())
					.getType().equals(Material.RAILS))
				{
					System.out.println("New station: "+stationName);
					RailNode newNode = new RailNode(event.getBlock().getWorld(), stationName);
					newNode.setLocation(playerloc.getBlockX(), playerloc.getBlockY(), playerloc.getBlockZ());
					newNode.explore(plugin.getRails());
				}
			}
		}
	}
}
