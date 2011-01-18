package com.bukkit.Innectis.Reroute;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;

/**
 * Reroute route node
 * Potentially temp - SM's testing.
 * @author Innectis
 */

public class RouteNetwork {

	public List<RouteNode> nodes = new ArrayList<RouteNode>();

	public RouteNetwork(Location start)
	{
		RouteNode home = new RouteNode(start, RouteNode.NodeType.TERMINAL, "Home");
		// for now, assume it's a valid station
		for (int i = 0; i < 4; ++i) {
			if (Material.WALL_SIGN.equals(Helper.getBlock(Helper.offset(start, i)).getType())) {
				crawl(home, i);
				break;
			}
		}
	}

	private void crawl(RouteNode from, int direction)
	{
		// ...
	}

	public Route getRoute(RouteNode from, String to)
	{
		RouteNode node = null;
		for (int i = 0; i < nodes.size(); ++i) {
			if (nodes.get(i).getName().equals(to)) {
				node = nodes.get(i);
				break;
			}
		}
		if (node == null) return null;
		return new Route(from, node);
	}

}
