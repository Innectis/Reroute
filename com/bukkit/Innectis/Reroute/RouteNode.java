package com.bukkit.Innectis.Reroute;

import org.bukkit.block.Block;

/**
 * Reroute route node
 * Potentially temp - SM's testing.
 * @author Innectis
 */

public class RouteNode {

	public enum NodeType { JUNCTION, TERMINAL };

	// World and coordinates of this block
	private Block Block;

	// Type of this node
	private NodeType nodeType = NodeType.JUNCTION;

	// Nodes reachable in given directions
	private RouteNode[] connected = new RouteNode[4];

	// Either a terminal name or a test name
	private String nodeName;

	public RouteNode(Block center, NodeType type, String name)
	{
		Block = center;
		nodeType = type;
		nodeName = name;
		Helper.log("Node constructed: " + name + " at " + center);
	}

	public Block getBlock() { return Block; }

	public NodeType getNodeType() { return nodeType; }

	public RouteNode getConnected(int i) { return connected[i]; }
	public void setConnected(int i, RouteNode node) { connected[i] = node; }

	public String getName() { return nodeName; }

}
