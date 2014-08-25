package net.buddat.ludumdare.ld30.ai;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.*;

/**
 * Tests that the path finder calculates the correct paths
 */
public class PathfinderTest {

	@Test
	public void testPathFinder() {
		TileNode[][] nodes = new TileNode[2][2];
		createTileNodeAt(nodes, 0, 0);
		createTileNodeAt(nodes, 1, 1);
		TestNodeBuilder builder = new TestNodeBuilder(nodes, 2, 2);
		Pathfinder pathfinder = new Pathfinder(builder);
		List<List<TileNode>> paths = pathfinder.calculateLeastCostPath(0, 0, 1, 1);
		assertNotNull("Pathfinder paths should never be null", paths);
		assertEquals("Should be exactly one shortest path for single-tile route", 1, paths.size());
	}

	private void createTileNodeAt(TileNode[][] nodes, int x, int y) {
		nodes[x][y] = new TileNode(x, y);
	}

	private class TestNodeBuilder implements NodeBuilder {
		private final TileNode[][] nodes;
		private final int width;
		private final int height;

		public TestNodeBuilder(TileNode[][] nodes, int width, int height) {
			this.nodes = nodes;
			this.width = width;
			this.height = height;
		}

		@Override
		public TileNode[][] getNodes() {
			return nodes;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}
	}
}
