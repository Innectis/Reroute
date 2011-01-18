package com.bukkit.Innectis.Reroute;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Innectis
 */

public class Route {

	public List<RouteNode> route = null;

	public Route(RouteNode from, RouteNode to)
	{
		List<RouteNode> list = new ArrayList<RouteNode>();
		list.add(from);
		route = subcalcRoute(list, to);
	}

	private List<RouteNode> subcalcRoute(List<RouteNode> from, RouteNode to)
	{
		// This is actually a depth-first search, not breadth, so...
		RouteNode node = from.get(from.size() - 1);
		for (int i = 0; i < 4; ++i) {
			if (node.getConnected(i) == to) {
				List<RouteNode> newlist = new ArrayList<RouteNode>(from);
				newlist.add(to);
				return newlist;
			} else if (node.getConnected(i).getNodeType() == RouteNode.NodeType.JUNCTION) {
				if (!from.contains(node.getConnected(i))) {
					List<RouteNode> newlist = new ArrayList<RouteNode>(from);
					newlist.add(node.getConnected(i));
					newlist = subcalcRoute(newlist, to);
					if (newlist != null) {
						return newlist;
					}
				}
			}
		}
		return null;
	}

}