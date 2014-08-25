package net.buddat.ludumdare.ld30.ai;

import net.buddat.ludumdare.ld30.world.WorldMap;

/**
 * Builds nodes from a map.
 */
public class MapNodeBuilder implements NodeBuilder {
	private final int width;
	private final int height;
	private final TileNode[][] nodes;
	public MapNodeBuilder(WorldMap map) {
		width = map.getWidth();
		height = map.getHeight();
		nodes = new TileNode[width][height];

		for (int x = 0; x < nodes.length; x++) {
			for (int y = 0; y < nodes[x].length; y++) {
				if (!map.isCollideable(x, y)) {
					nodes[x][y] = new TileNode(x, y);
				}
			}
		}
	}

	@Override
	public TileNode[][] getNodes() {
		return nodes;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

}
