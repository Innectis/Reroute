package com.bukkit.Innectis.Reroute;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Sign;
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
		nodes.add(home);
		for (int i = 0; i < 4; ++i) {
			if (Material.WALL_SIGN.equals(Helper.getBlock(Helper.offset(start, i)).getType())) {
				crawl(home, i);
				break;
			}
		}
	}

	private void crawl(RouteNode from, int facing)
	{
		int origFacing = facing;
		Location loc = from.getLocation();
		// Move two blocks away - gets us onto standard track.
		loc = Helper.offset(Helper.offset(loc, facing), facing);

		while (true) {
			if (Material.WALL_SIGN.equals(Helper.getBlock(loc).getType())) {
				// We probably hit a station
				Sign sign = (Sign) Helper.getBlock(loc).getState();
				if (sign.getLine(0).equals("[Reroute]") && sign.getLine(1).equals("Station")) {
					String name = sign.getLine(2);
					Location newLoc = Helper.offset(loc, facing);
					// Assuming Junctions aren't asploded, this is new.
					RouteNode node = getNodeByName(name);
					if (node == null) {
						node = new RouteNode(newLoc, RouteNode.NodeType.TERMINAL, name);
						from.setConnected(origFacing, node);
						node.setConnected((facing + 2) % 4, from);
						// Since stations are dead-ends, no need to branch.
					}
					break;
				}
			} else if (Material.IRON_BLOCK.equals(Helper.getBlock(loc).getType())) {
				// We probably hit a junction
				Location newLoc = Helper.offset(loc, facing);
				RouteNode node = getNodeByLocation(newLoc);
				if (node == null) {
					node = new RouteNode(newLoc, RouteNode.NodeType.JUNCTION, "j");
					from.setConnected(origFacing, node);
					node.setConnected((facing + 2) % 4, from);
					// Branch to the other three directions.
					for (int i = -1; i <= 1; ++i) {
						int nf = (facing + i) % 4;
						if (nf < 0) nf += 4;
						crawl(node, nf);
					}
				}
				break;
			} else if (trackExists(Helper.offset(loc, facing + 1)) &&
					trackExists(Helper.offset(loc, facing - 1))) {
				// straightahead
				loc = Helper.offset(loc, facing);
			} else if (trackExists(Helper.offset(loc, facing + 1)) &&
					trackExists(Helper.offset(loc, facing)) &&
					!trackExists(Helper.offset(loc, facing - 1))) {
				// left turn
				facing = (facing - 1) % 4;
				if (facing < 0) facing += 4;
			} else if (trackExists(Helper.offset(loc, facing - 1)) &&
					trackExists(Helper.offset(loc, facing)) &&
					!trackExists(Helper.offset(loc, facing + 1))) {
				// right turn
				facing = (facing + 1) % 4;
			} else {
				// Dead end!
				break;
			}
			loc = Helper.offset(loc, facing);
		}
	}

	private boolean trackExists(Location loc)
	{
		Location loc2 = new Location(loc.getWorld(),
				loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
		return Material.COBBLESTONE.equals(Helper.getBlock(loc).getType()) &&
				Material.RAILS.equals(Helper.getBlock(loc2).getType());
	}

	public RouteNode getNodeByName(String name)
	{
		for (int i = 0; i < nodes.size(); ++i) {
			if (nodes.get(i).getName().equals(name)) {
				return nodes.get(i);
			}
		}
		return null;
	}

	public RouteNode getNodeByLocation(Location loc)
	{
		for (int i = 0; i < nodes.size(); ++i) {
			if (nodes.get(i).getLocation().equals(loc)) {
				return nodes.get(i);
			}
		}
		return null;
	}

	public Route getRoute(RouteNode from, RouteNode to)
	{
		Route r = new Route(from, to);
		if(r.route == null) {
			return null;
		}
		return r;
	}

}
