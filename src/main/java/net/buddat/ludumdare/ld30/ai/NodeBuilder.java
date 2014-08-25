package net.buddat.ludumdare.ld30.ai;

import net.buddat.ludumdare.ld30.world.WorldMap;

/**
 * Builds nodes in a width x height array and provides access for pathfinding.
 */
public interface NodeBuilder {
	public TileNode[][] getNodes();

	public int getWidth();

	public int getHeight();
}
