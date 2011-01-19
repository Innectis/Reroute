package com.bukkit.Innectis.Reroute;

import java.util.LinkedList;
import java.util.Queue;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * A single node in the rail graph.
 * @author 31
 */
public class RailNode
{
	public enum NodeType {
		Station,
		Junction
	}
	private NodeType type;

	//A Block class used for exploring paths.
	private class Loc
	{
		public int x,y,z;
		/** r: -1:none, 0:+x, 1:+z, 2:-x, 3:-z */
		public byte r;
		/** r: -1:none, 0:+x, 1:+z, 2:-x, 3:-z */
		public Loc(int x, int y, int z, int r)
		{
			this.x = x; this.y = y; this.z = z;
			this.r = (byte)r;
		}
	}

	//Station name or a debug junction name.
	private String nodeName;
	private static int numJunctions = 0;

	//The other nodes that this one links to.
	private LinkedList<RailNode> links;

	//The Block specific to this node, and the world it exists in.
	//For a Station, this is a block of rail.
	//For a Junction, this is the "upper-left" corner.
	private World w;
	private int x,y,z;

	public RailNode(World w)
	{
		type = NodeType.Station;
		nodeName = "junction"+(numJunctions++);
		this.w = w;
	}
	public RailNode(World w, String name)
	{
		type = NodeType.Junction;
		nodeName = name;
		this.w = w;
	}

	public void setBlock(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Searches through this rail path, adding any nodes it finds and linking
	 * them together.
	 * Uses a breadth-first search.
	 */
	public void explore(RailGraph railg)
	{
		Queue<Loc> locs = new LinkedList<Loc>();
		locs.add(new Loc(x, y, z, -1));

		//Explore this many tiles before aborting unconditionally.
		int emergencystop = 1000000;

		while(locs.size() > 0)
		{
			if(emergencystop-- == 0)
			{
				System.out.println("Rail exploration took too long--interrupted by emergencystop.");
				break;
			}
			Loc l = locs.remove();
			Material m = w.getBlockAt(l.x, l.y, l.z).getType();
			if(m.equals(Material.RAILS))
			{
				//r represents where the next rail should *not* explore.
				if(l.r != 0)
					locs.add(new Loc(l.x+1, l.y, l.z, 2));
				if(l.r != 1)
					locs.add(new Loc(l.x, l.y, l.z+1, 3));
				if(l.r != 2)
					locs.add(new Loc(l.x-1, l.y, l.z, 0));
				if(l.r != 3)
					locs.add(new Loc(l.x, l.y, l.z-1, 1));
				//w.getBlockAt(l.x, l.y, l.z).setType(Material.SAND);
			}
			else if(m.equals(Material.AIR))
			{
				if(w.getBlockAt(l.x, l.y-1, l.z).getType().equals(Material.IRON_BLOCK))
				{
					//If at the end of the rail there is an iron block...
					int midx, midy, midz; //Middle of the junction. If it exists.
					midy = l.y-1;
					//Based on how the junction is entered, the x and z values will be different relative to the original loc.
					switch(l.r)
					{
						case 0: //Opposite +x
							midx = l.x - 1;
							midz = l.z + 1;
							break;
						case 1: //Opposite +z
							midx = l.x - 1;
							midz = l.z - 1;
							break;
						case 2: //Opposite -x
							midx = l.x + 1;
							midz = l.z - 1;
							break;
						case 3: //Opposite -z
							midx = l.x + 1;
							midz = l.z + 1;
							break;
						default:
							midx = l.x;
							midz = l.z;
					}
					//Now see if there are the proper 4 iron blocks around this junction.
					if(junctionExists(midx, midy, midz))
					{
						RailNode newNode = new RailNode(w);
						newNode.setBlock(midx, midy, midz);
					}

				}
			}
		}
	}

	/**
	 * Checks for a junction centered around (midx, midy, midz).
	 */
	public boolean junctionExists(int midx, int midy, int midz)
	{
		w.getBlockAt(midx, midy+3, midz).setType(Material.BOOKSHELF);
		return
			w.getBlockAt(midx+1, midy, midz+1).getType().equals(Material.IRON_BLOCK) &&
			w.getBlockAt(midx-1, midy, midz+1).getType().equals(Material.IRON_BLOCK) &&
			w.getBlockAt(midx+1, midy, midz-1).getType().equals(Material.IRON_BLOCK) &&
			w.getBlockAt(midx-1, midy, midz-1).getType().equals(Material.IRON_BLOCK);
	}
}
