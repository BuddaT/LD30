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
import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.WorldMap;
import net.buddat.ludumdare.ld30.world.WorldObject;
import net.buddat.ludumdare.ld30.world.entity.Entity;
import net.buddat.ludumdare.ld30.world.entity.EntityRenderer;
import net.buddat.ludumdare.ld30.world.entity.Movement;
import net.buddat.ludumdare.ld30.world.player.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

/**
 * Manages NPC entities
 */
public class EntityManager {
	private final TreeSet<EntityRenderer> entityRenderers;
	private final WorldMap map;
	private final Player player;
	private final Pathfinder pathfinder;
	private final WorldManager worldManager;

	private EntityRenderer lastRenderedBelow;

	public EntityManager(WorldManager worldManager, Player player) {
		this.worldManager = worldManager;
		this.map = worldManager.getCurrentWorld().getWorldMap();
		this.player = player;
		pathfinder = new Pathfinder(new MapNodeBuilder(map));

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
		assignMovement(entityRenderer.getEntity());
	}

	private Map<TileNode, List<WorldObject>> buildBoundsMap() {
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
		Map<TileNode, List<WorldObject>> boundsCoords = buildBoundsMap();
		for (EntityRenderer renderer : entityRenderers) {
			Entity entity = renderer.getEntity();
			assignMovement(entity);
			float oldX = entity.getX();
			float oldY = entity.getY();
			entity.move();

			WorldObject intersectObject = findIntersectObject(boundsCoords, entity);
			if (intersectObject != null) {
				entity.setX(oldX);
				entity.setY(oldY);

				if (entity.canDestroy()) {
					if (Math.random() * 100 + 1 > Constants.DESTRUCTION_PERCENTAGE)
						intersectObject.setRemoved(true);
				}
			}
		}
	}

	private WorldObject findIntersectObject(Map<TileNode, List<WorldObject>> boundsCoords,
											Entity entity) {
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

	private void assignMovement(Entity entity) {
		int entityTileX = entity.getTileX();
		int entityTileY = entity.getTileY();
		List<List<TileNode>> paths = pathfinder.calculateLeastCostPath(
				entityTileX, entityTileY, player.getTileX(), player.getTileY());
		if (paths.isEmpty()) {
			entity.setSpeed(0);
		} else {
			List<TileNode> path = paths.get((int) (Math.random() * paths.size()));
			Movement proposedMovement;
			if (path.isEmpty()) {
				System.err.println("Empty path returned from path finder for (" + entityTileX + ","
						+ entityTileY + ") to (" + player.getTileX() + "," + player.getTileY() + ")");
				entity.setSpeed(0);
				return;
			} else if (path.size() == 1) {
				// Path only contains origin node
				proposedMovement = Movement.movementTo(
						entity.getSpeed(), entity.getX(), entity.getY(), player.getX(), player.getY());
			} else {
				TileNode nextNode = path.get(1);
				proposedMovement = Movement.movementTo(entity.getSpeed(), entity.getX(), entity.getY(), nextNode);
			}
			entity.setMovement(proposedMovement);
		}
	}
}
