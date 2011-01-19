package com.bukkit.Innectis.Reroute;

import org.bukkit.block.Block;
import org.bukkit.Material;

/**
 * Reroute route node
 * Potentially temp - SM's testing.
 * @author Innectis
 */

public class RouteNode {

	public enum NodeType { JUNCTION, TERMINAL };

	// World and coordinates of this block
	private Block location;

	// Type of this node
	private NodeType nodeType = NodeType.JUNCTION;

	// Nodes reachable in given directions
	private RouteNode[] connected = new RouteNode[4];

	// Either a terminal name or a test name
	private String nodeName;

	public RouteNode(Block center, NodeType type, String name)
	{
		location = center;
		nodeType = type;
		nodeName = name;
		center.getWorld().getBlockAt(center.getX(), center.getY() + 3, center.getZ()).setType(Material.GLASS);
		System.out.println("Node " + name + " at " + center);
	}

	public Block getBlock() { return location; }

	public NodeType getNodeType() { return nodeType; }

	public RouteNode getConnected(int i) { return connected[i]; }
	public void setConnected(int i, RouteNode node) { connected[i] = node; }

	public String getName() { return nodeName; }

}
