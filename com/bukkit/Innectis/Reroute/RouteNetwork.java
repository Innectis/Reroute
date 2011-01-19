package com.bukkit.Innectis.Reroute;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.Material;

/**
 * Reroute route node
 * Potentially temp - SM's testing.
 * @author Innectis
 */

public class RouteNetwork {

	public List<RouteNode> nodes = new ArrayList<RouteNode>();

	public RouteNetwork(Block start)
	{
		RouteNode home = new RouteNode(start, RouteNode.NodeType.TERMINAL, "Home");
		nodes.add(home);
		System.out.println("Crawling from " + start);
		for (int i = 0; i < 4; ++i) {
			if (Material.WALL_SIGN.equals(Helper.offset(start, i).getType())) {
				crawl(home, i);
				break;
			}
		}
	}

	private void crawl(RouteNode from, int facing)
	{
		System.out.println("  Crawling " + facing + " " + from.getBlock());
		int origFacing = facing;
		Block loc = from.getBlock();
		// Move two blocks away - gets us onto standard track.
		loc = Helper.offset(Helper.offset(loc, facing), facing);

		while (true) {
			if (Material.WALL_SIGN.equals(loc.getType())) {
				// We probably hit a station
				Sign sign = (Sign) loc.getState();
				if (sign.getLine(0).equals("[Reroute]") && sign.getLine(1).equals("Station")) {
					String name = sign.getLine(2);
					// Assuming Junctions aren't asploded, this is always new.
					// Centerpoint of a station is the wall sign's location.
					RouteNode node = getNodeByName(name);
					if (node == null) {
						node = new RouteNode(loc, RouteNode.NodeType.TERMINAL, name);
						from.setConnected(origFacing, node);
						node.setConnected((facing + 2) % 4, from);
						// Since stations are dead-ends, no need to branch.
					}
					break;
				}
			} else if (Material.IRON_BLOCK.equals(loc.getType())) {
				// We probably hit a junction
				Block newLoc = Helper.offset(loc, facing);
				RouteNode node = getNodeByBlock(newLoc);
				if (node == null) {
					node = new RouteNode(newLoc, RouteNode.NodeType.JUNCTION, "Junction");
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
				// straightahead, see below
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
			// Debugging.
			loc.setType(Material.GLASS);
			loc = Helper.offset(loc, facing);
		}
	}

	private boolean trackExists(Block loc)
	{
		Block loc2 = loc.getWorld().getBlockAt(loc.getX(), loc.getY() + 1, loc.getZ());
		return Material.COBBLESTONE.equals(loc.getType()) &&
				Material.RAILS.equals(loc2.getType());
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

	public RouteNode getNodeByBlock(Block loc)
	{
		for (int i = 0; i < nodes.size(); ++i) {
			if (nodes.get(i).getBlock().equals(loc)) {
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
