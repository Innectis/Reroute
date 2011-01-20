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
		System.out.println("Crawling from " + start);
		for (int i = 0; i < 4; ++i) {
			Block b = Helper.offset(start, i);
			if (Material.WALL_SIGN.equals(b.getType())) {
				Sign s = (Sign) b.getState();
				if (s.getLine(0).equals("[Reroute]") && s.getLine(1).equals("Station")) {
					RouteNode home = new RouteNode(b, RouteNode.NodeType.TERMINAL, s.getLine(2));
					nodes.add(home);
					crawl(home, i);
					break;
				}
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
			Block fm1 = Helper.offset(loc, facing - 1);		// facing - 1 (L)
			Block fwd = Helper.offset(loc, facing);			// forward
			Block fp1 = Helper.offset(loc, facing + 1);		// facing + 1 (R)
			if (Material.WALL_SIGN.equals(loc.getType())) {
				// We probably hit a station
				Sign sign = (Sign) loc.getState();
				if (sign.getLine(0).equals("[Reroute]") && sign.getLine(1).equals("Station")) {
					String name = sign.getLine(2);
					// Assuming junctions aren't asploded, this is always new,
					// but a check is in place just to make sure. Centerpoint
					// of a terminal is the wall sign's location.
					RouteNode node = getNodeByLocation(loc);
					if (node == null) {
						node = new RouteNode(loc, RouteNode.NodeType.TERMINAL, name);
					}
					from.setConnected(origFacing, node);
					node.setConnected((facing + 2) % 4, from);
					break;
				}
			} else if (Material.IRON_BLOCK.equals(loc.getType())) {
				// We hit a junction. Don't create
				RouteNode node = getNodeByLocation(fwd);
				boolean isNew = (node == null);
				if (isNew) {
					node = new RouteNode(fwd, RouteNode.NodeType.JUNCTION, "Junction");
				}
				from.setConnected(origFacing, node);
				node.setConnected((facing + 2) % 4, from);
				if (isNew) {
					// Branch to the other three directions.
					for (int i = -1; i <= 1; ++i) {
						int nf = (facing + i) % 4;
						if (nf < 0) nf += 4;
						crawl(node, nf);
					}
				}
				break;
				// Done crawling.
			} else if (trackExists(fp1) && trackExists(fm1)) {
				// straightahead (handled below)
			} else if (trackExists(fp1) && trackExists(fwd) && !trackExists(fm1)) {
				// left turn
				facing = (facing - 1) % 4;
				if (facing < 0) facing += 4;
			} else if (trackExists(fm1) && trackExists(fwd) && !trackExists(fp1)) {
				// right turn
				facing = (facing + 1) % 4;
			} else if (trackExists(loc.getWorld().getBlockAt(fm1.getX(), loc.getY() - 1, fm1.getZ())) &&
					trackExists(loc.getWorld().getBlockAt(fp1.getX(), loc.getY() - 1, fp1.getZ()))) {
				// downwards slope
				loc = loc.getWorld().getBlockAt(loc.getX(), loc.getY() - 1, loc.getZ());
			} else if (trackExists(loc.getWorld().getBlockAt(fm1.getX(), loc.getY() + 1, fm1.getZ())) &&
					trackExists(loc.getWorld().getBlockAt(fp1.getX(), loc.getY() + 1, fp1.getZ()))) {
				// upwards slope
				loc = loc.getWorld().getBlockAt(loc.getX(), loc.getY() + 1, loc.getZ());
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

	public RouteNode getNodeByLocation(Block loc)
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
