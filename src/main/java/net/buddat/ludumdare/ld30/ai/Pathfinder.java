package net.buddat.ludumdare.ld30.ai;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import net.buddat.ludumdare.ld30.world.WorldMap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public final class Pathfinder {
	private final WorldMap map;
	private final int width;
	private final int height;
	private final TileNode[][] nodes;

	public Pathfinder(WorldMap map) {
		this.map = map;
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

	/**
	 * Calculates the least-cost path, starting from (xOrigin, yOrigin) and ending at
	 * (xGoal, yGoal).
	 *
	 * @param xOrigin X coordinate of the origin tile
	 * @param yOrigin Y coordinate of the origin tile
	 * @param xGoal X coordinate of the goal tile
	 * @param yGoal Y coordinate of the goal tile
	 */
	public List<List<TileNode>> calculateLeastCostPath(int xOrigin, int yOrigin, int xGoal, int yGoal) {
		checkBounds("Origin", xOrigin, yOrigin, width, height);
		checkBounds("Goal", xOrigin, yOrigin, width, height);

		TileNode origin = nodes[xOrigin][yOrigin];
		TileNode goal = nodes[xGoal][yGoal];
		SearchProblem<Void, TileNode, WeightedNode<Void, TileNode, Double>> p =
				ProblemBuilder.create()
				.initialState(origin)
				.defineProblemWithoutActions()
				.useTransitionFunction(new StateTransitionFunction<TileNode>() {
					@Override
					public Iterable<TileNode> successorsOf(TileNode tileNode) {
						return validLocationsFrom(tileNode);
					}
				})
				.useCostFunction(new CostFunction<Void, TileNode, Double>() {
					@Override
					public Double evaluate(Transition<Void, TileNode> voidTileNodeTransition) {
						TileNode from = voidTileNodeTransition.getFromState();
						TileNode to = voidTileNodeTransition.getState();
						return from.distanceTo(to);
					}
				})
				.build();
		return Hipster.createAStar(p).search(goal).getOptimalPaths();
	}

	/**
	 * Verifies that the given coordinates are within valid bounds, ie. the following must
	 * hold:
	 * 0 < x < width && 0 < y < height
	 *
	 * If not, an IllegalArgumentException is raised.
	 *
	 * @param coordName Name of the coordinate, displayed in the error message
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Width of the map
	 * @param height Height of the map
	 */
	private void checkBounds(String coordName, int x, int y, int width, int height) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			throw new IllegalArgumentException(coordName + " coordinates " + x + ", " + y +
					" out of bounds: " + width + ", " + height);
		}
	}

	/**
	 * Checks and returns all neighbours from a given tile node, including diagonals.
	 * @param node Node for which neighbours are returned.
	 * @return All neighbours for the given node.
	 */
	private Iterable<TileNode> validLocationsFrom(TileNode node) {
		Set<TileNode> neighbours = new HashSet<>();
		final int x = node.getX();
		final int y = node.getY();
		if (x > 0) {
			// Left
			addIfNotNull(neighbours, x - 1, y);
			if (y > 0) {
				// Diagonal up/left
				addIfNotNull(neighbours, x - 1, y - 1);
			}
			if (y < height - 1) {
				// Diagonal down/left
				addIfNotNull(neighbours, x - 1, y + 1);
			}
		}
		if (x < width - 1) {
			// Right
			addIfNotNull(neighbours, x + 1, y);
			if (y > 0) {
				// Diagonal up/right
				addIfNotNull(neighbours, x + 1, y - 1);
			}
			if (y < height - 1) {
				// Diagonal down/right
				addIfNotNull(neighbours, x + 1, y + 1);
			}
		}
		if (y > 0) {
			// Up
			addIfNotNull(neighbours, x, y - 1);
		}
		if (y < height - 1) {
			addIfNotNull(neighbours, x, y + 1);
		}
		return neighbours;
	}

	private void addIfNotNull(Set<TileNode> addNodes, int x, int y) {
		if (nodes[x][y] != null) {
			addNodes.add(nodes[x][y]);
		}
	}
}
