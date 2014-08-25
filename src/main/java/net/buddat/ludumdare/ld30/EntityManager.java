package net.buddat.ludumdare.ld30;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.buddat.ludumdare.ld30.ai.MapNodeBuilder;
import net.buddat.ludumdare.ld30.ai.Pathfinder;
import net.buddat.ludumdare.ld30.ai.TileNode;
import net.buddat.ludumdare.ld30.world.EntityAttractor;
import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.WorldMap;
import net.buddat.ludumdare.ld30.world.WorldObject;
import net.buddat.ludumdare.ld30.world.entity.Entity;
import net.buddat.ludumdare.ld30.world.entity.EntityRenderer;
import net.buddat.ludumdare.ld30.world.entity.Movement;
import net.buddat.ludumdare.ld30.world.player.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * Manages NPC entities
 */
public class EntityManager {
	private final TreeSet<EntityRenderer> entityRenderers;
	private final WorldMap map;
	private final Player player;
	private final Pathfinder pathfinder;
	private final WorldManager worldManager;
	private Map<TileNode, List<WorldObject>> objectBoundsCoords;
	private Circle repathArea;

	private EntityRenderer lastRenderedBelow;

	public EntityManager(WorldManager worldManager, Player player) {
		this.worldManager = worldManager;
		this.map = worldManager.getCurrentWorld().getWorldMap();
		this.player = player;
		repathArea = new Circle(player.getX(), player.getY(), REPATH_RADIUS);
		pathfinder = new Pathfinder(new MapNodeBuilder(map));
		objectBoundsCoords = buildBoundsCoords();

		// Entity renders are ordered by Y position, then X position, so that they can be rendered in order
		entityRenderers = new TreeSet<>(new Comparator<EntityRenderer>() {
			@Override
			public int compare(EntityRenderer o1, EntityRenderer o2) {
				Entity e1 = o1.getEntity();
				Entity e2 = o2.getEntity();
				if (e1.getY() < e2.getY()) {
					return -1;
				} else if (e1.getY() == e2.getY()) {
					if (e1.getX() < e2.getX()) {
						return -1;
					} else if (e1.getX() == e2.getX()) {
						return 0;
					} else {
						return 1;
					}
				} else {
					return 1;
				}
			}
		});
	}

	public void renderEntitiesBelow(GameContainer gc, float x, float y) {
		for (EntityRenderer renderer : entityRenderers) {
			if (renderer.getEntity().getY() < y) {
				renderer.render(gc, x, y);
				lastRenderedBelow = renderer;
			} else {
				break;
			}
		}
	}

	public void renderEntitiesAbove(GameContainer gc, float x, float y) {
		SortedSet<EntityRenderer> aboveSet;
		if (lastRenderedBelow == null) {
			aboveSet = entityRenderers;
		} else {
			EntityRenderer higher = entityRenderers.higher(lastRenderedBelow);
			if (higher == null) { // No more to render
				lastRenderedBelow = null;
				return;
			}
			aboveSet = entityRenderers.tailSet(higher);
		}
		for (EntityRenderer renderer : aboveSet) {
			renderer.render(gc, x, y);
		}
		lastRenderedBelow = null;
	}

	public void addEntity(EntityRenderer entityRenderer) {
		entityRenderers.add(entityRenderer);
		assignMovement(entityRenderer.getEntity(), objectBoundsCoords, 0);
	}

	/**
	 * Builds a map of tile positions to objects based on object bounds.
	 * @return Map of tile positions to a list of objects whose bounds fall into those positions.
	 */
	private Map<TileNode, List<WorldObject>> buildBoundsCoords() {
		List<WorldObject> objects = worldManager.getInteractibleObjects();
		HashMap<TileNode, List<WorldObject>> objectCoords = new HashMap<>();
		for (WorldObject object : objects) {
			Rectangle bounds = object.getBounds();
			float x = bounds.getX();
			float y = bounds.getY();
			for (int tileX = (int) x; tileX <= (x + bounds.getWidth()); tileX++) {
				for (int tileY = (int) y; tileY <= (y + bounds.getHeight()); tileY++) {
					TileNode coords = new TileNode(tileX, tileY);
					List<WorldObject> objectList;
					if (objectCoords.containsKey(coords)) {
						objectList = objectCoords.get(coords);
					} else {
						objectList = new ArrayList<>();
						objectCoords.put(coords, objectList);
					}
					objectList.add(object);
				}
			}
		}
		return objectCoords;
	}

	public void updateEntities(int delta) {
		objectBoundsCoords = buildBoundsCoords();
		for (EntityRenderer renderer : entityRenderers) {
			Entity entity = renderer.getEntity();
			assignMovement(entity, objectBoundsCoords, delta);
			float oldX = entity.getX();
			float oldY = entity.getY();
			entity.move();

			WorldObject intersectObject = findIntersectObject(entity, objectBoundsCoords);
			if (intersectObject != null) {
				entity.setX(oldX);
				entity.setY(oldY);

				if (entity.canDestroy() && worldManager.getCurrentWorld().isExitActive()) {
					if (Math.random() * 100 + 1 > Constants.DESTRUCTION_PERCENTAGE)
						intersectObject.setRemoved(true);
				}
			}
		}
	}

	private WorldObject findIntersectObject(Entity entity, Map<TileNode, List<WorldObject>> boundsCoords) {
		int entityTileX = entity.getTileX();
		int entityTileY = entity.getTileY();
		for (int tileX = entityTileX - 1; tileX <= entityTileX + 1; tileX++) {
			for (int tileY = entityTileY - 1; tileY <= entityTileY + 1; tileY++) {
				List<WorldObject> objectsInTile = boundsCoords.get(new TileNode(tileX, tileY));
				if (objectsInTile != null) {
					for (WorldObject object : objectsInTile) {
						if (object.isRemoved())
							continue;

						if (entity.getBounds().intersects(object.getBounds())) {
							return object;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Radius around the player in which entities' paths are to be regularly recalculated.
	 */
	private static final float REPATH_RADIUS = 15f;
	private static final int MIN_PATH_UPDATE_DELTA = 5000;

	private void assignMovement(Entity entity, Map<TileNode, List<WorldObject>> boundsCoords, int delta) {
		int entityTileX = entity.getTileX();
		int entityTileY = entity.getTileY();
		boolean isExitActive = worldManager.getCurrentWorld().isExitActive();
		float playerDistance = entity.getPosn().distance(player.getPosn());
		if (playerDistance > REPATH_RADIUS) {
			int pathUpdateDelta = entity.incrementPathUpdateDelta(delta);
			if (pathUpdateDelta < MIN_PATH_UPDATE_DELTA) {
				return;
			}
		}
		SortedSet<EntityAttractor> attractors = getAttractors(entity, boundsCoords);

		if (attractors.size() == 0) {
			if (!isExitActive) {
				entity.setSpeed(0);
				return;
			}
			// Attract to the player
			attractors.add(player);
		}
		for (EntityAttractor attractor : attractors) {
			Movement proposedMovement;
			List<List<TileNode>> paths;
			float maxSpeed = isExitActive ? entity.getMaxExitSpeed() : entity.getMaxNormalSpeed();
			if (entityTileX == (int) attractor.getX() && entityTileY == (int) attractor.getY()) {
				paths = buildOriginPath(entityTileX, entityTileY);
			} else {
				paths = pathfinder.calculateLeastCostPath(
					entityTileX, entityTileY, (int) attractor.getX(), (int) attractor.getY());
			}
			if (!paths.isEmpty()) {
				List<TileNode> path = paths.get(0);
				if (path.isEmpty()) {
					System.err.println("Empty path returned from path finder for (" + entityTileX + ","
							+ entityTileY + ") to (" + attractor.getX() + "," + attractor.getY() + ")");
					continue;
				} else if (path.size() == 1) {
					// Path only contains origin node
					proposedMovement = Movement.movementTo(
							maxSpeed, entity.getX(), entity.getY(), attractor.getX(), attractor.getY());
				} else {
					TileNode nextNode = path.get(1);
					proposedMovement = Movement.movementTo(maxSpeed, entity.getX(), entity.getY(), nextNode);
				}
				entity.setPath(path);
				entity.setMovement(proposedMovement);
				return;
			}
		}
		// No paths found to any entities
		entity.setSpeed(0);
	}

	private List<List<TileNode>> buildOriginPath(int tileX, int tileY) {
		List<TileNode> path = new ArrayList<>(1);
		path.add(new TileNode(tileX, tileY));
		List<List<TileNode>> paths = new ArrayList<>(1);
		paths.add(path);
		return paths;
	}

	private SortedSet<EntityAttractor> getAttractors(Entity entity, Map<TileNode, List<WorldObject>> boundsCoords) {
		// TODO: Some tiles checked that don't need to be.
		float senseRadius = entity.getSenseRadius();
		Vector2f entityPosn = entity.getPosn();
		SortedSet<EntityAttractor> attractors = new TreeSet<>(new DistanceComparator(entity.getX(), entity.getY()));
		float playerDistance = entityPosn.distance(player.getPosn());
		if (playerDistance <= senseRadius) {
			attractors.add(player);
		}
		for (int tileX = (int) (entity.getX() - senseRadius); tileX <= (int) (entity.getX() + senseRadius); tileX++) {
			for (int tileY = (int) (entity.getY() - senseRadius); tileY <= (int) entity.getY() + senseRadius; tileY++) {
				List<WorldObject> objects = boundsCoords.get(new TileNode(tileX, tileY));
				if (objects == null) {
					continue;
				}
				for (WorldObject object : objects) {
					if (object.isAttractor()) {
						float distance = entityPosn.distance(object.getPosn());
						if (distance <= senseRadius) {
							attractors.add(object);
						}
					}
				}
			}
		}
		return attractors;
	}

	private class DistanceComparator implements Comparator<EntityAttractor> {

		private final Vector2f posn;

		public DistanceComparator(float fromX, float fromY) {
			this.posn = new Vector2f(fromX, fromY);
		}

		@Override
		public int compare(EntityAttractor o1, EntityAttractor o2) {
			float distance1 = posn.distance(o1.getPosn());
			float distance2 = posn.distance(o2.getPosn());
			return Float.compare(distance1, distance2);
		}
	}

	public void reset() {

	}
}
