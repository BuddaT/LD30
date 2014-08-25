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

	private EntityRenderer lastRenderedBelow;

	public EntityManager(WorldManager worldManager, Player player) {
		this.worldManager = worldManager;
		this.map = worldManager.getCurrentWorld().getWorldMap();
		this.player = player;
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
		assignMovement(entityRenderer.getEntity(), objectBoundsCoords);
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

	public void updateEntities() {
		objectBoundsCoords = buildBoundsCoords();
		for (EntityRenderer renderer : entityRenderers) {
			Entity entity = renderer.getEntity();
			assignMovement(entity, objectBoundsCoords);
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

	private void assignMovement(Entity entity, Map<TileNode, List<WorldObject>> boundsCoords) {
		int entityTileX = entity.getTileX();
		int entityTileY = entity.getTileY();
		boolean isExitActive = worldManager.getCurrentWorld().isExitActive();
		EntityAttractor closestAttractor = getClosestAttractor(entity, boundsCoords);

		if (closestAttractor == null) {
			if (!isExitActive) {
				entity.setSpeed(0);
				return;
			}
			// Attract to the player
			closestAttractor = player;
		}
		List<List<TileNode>> paths = pathfinder.calculateLeastCostPath(
				entityTileX, entityTileY, (int) closestAttractor.getX(), (int) closestAttractor.getY());
		if (paths.isEmpty()) {
			entity.setSpeed(0);
		} else {
			List<TileNode> path = paths.get((int) (Math.random() * paths.size()));
			Movement proposedMovement;
			float maxSpeed = isExitActive ? entity.getMaxExitSpeed() : entity.getMaxNormalSpeed();
			if (path.isEmpty()) {
				System.err.println("Empty path returned from path finder for (" + entityTileX + ","
						+ entityTileY + ") to (" + player.getTileX() + "," + player.getTileY() + ")");
				entity.setSpeed(0);
				return;
			} else if (path.size() == 1) {
				// Path only contains origin node
				proposedMovement = Movement.movementTo(
						maxSpeed, entity.getX(), entity.getY(), player.getX(), player.getY());
			} else {
				TileNode nextNode = path.get(1);
				proposedMovement = Movement.movementTo(maxSpeed, entity.getX(), entity.getY(), nextNode);
			}
			entity.setMovement(proposedMovement);
		}
	}

	private EntityAttractor getClosestAttractor(Entity entity, Map<TileNode, List<WorldObject>> boundsCoords) {
		// TODO: Some tiles checked that don't need to be.
		float senseRadius = entity.getSenseRadius();
		Vector2f entityPosn = new Vector2f(entity.getX(), entity.getY());
		float closestDistance = entityPosn.distance(new Vector2f(player.getX(), player.getY()));
		EntityAttractor closestAttractor = closestDistance <= senseRadius ? player : null;
		for (int tileX = (int) (entity.getX() - senseRadius); tileX <= (int) (entity.getX() + senseRadius); tileX++) {
			for (int tileY = (int) (entity.getY() - senseRadius); tileY <= (int) entity.getY() + senseRadius; tileY++) {
				List<WorldObject> objects = boundsCoords.get(new TileNode(tileX, tileY));
				if (objects == null) {
					continue;
				}
				for (WorldObject object : objects) {
					if (object.isAttractor()) {
						float distance = entityPosn.distance(new Vector2f(object.getX(), object.getY()));
						if (distance <= senseRadius && distance < closestDistance) {
							closestAttractor = object;
						}
					}
				}
			}
		}
		return closestAttractor;
	}
}
