package net.buddat.ludumdare.ld30;

import java.util.*;

import net.buddat.ludumdare.ld30.ai.MapNodeBuilder;
import net.buddat.ludumdare.ld30.ai.Pathfinder;
import net.buddat.ludumdare.ld30.ai.TileNode;
import net.buddat.ludumdare.ld30.world.*;
import net.buddat.ludumdare.ld30.world.entity.*;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;
import net.buddat.ludumdare.ld30.world.player.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * Manages NPC entities
 */
public class EntityManager {
	private final Player player;
	private final WorldManager worldManager;
	private WorldState worldState;
	private Map<TileNode, List<WorldObject>> boundsCoords;

	private EntityRenderer nextRenderer;
	private Iterator<EntityRenderer> entityRendererIterator;

	public EntityManager(long renderTime, WorldManager worldManager, Player player) throws SlickException {
		this.worldManager = worldManager;
		this.player = player;
		worldState = new WorldState(worldManager);
		boundsCoords = buildBoundsCoords();
		createEntities(renderTime);
	}

	public void renderEntitiesBelow(GameContainer gc, float x, float y) {
		entityRendererIterator = worldState.entityRenderers.iterator();
		while (entityRendererIterator.hasNext()) {
			nextRenderer = entityRendererIterator.next();
			if (nextRenderer.getEntity().getY() < y) {
				nextRenderer.render(gc, x, y);
				nextRenderer = null;
			} else {
				break;
			}
		}
	}

	public void renderEntitiesAbove(GameContainer gc, float x, float y) {
		if (nextRenderer != null) {
			nextRenderer.render(gc, x, y);
		}
		while (entityRendererIterator.hasNext()) {
			EntityRenderer renderer = entityRendererIterator.next();
			renderer.render(gc, x, y);
		}
		nextRenderer = null;
	}

	public void addEntity(EntityRenderer entityRenderer) {
		worldState.entityRenderers.add(entityRenderer);
		Entity entity = entityRenderer.getEntity();
		worldState.entities.add(entity);
		worldState.entitiesByDistance.add(entity);
		assignMovement(entity, boundsCoords, 0);
	}

	public SortedSet<Entity> getEntitiesByDistance() {
		return worldState.entitiesByDistance;
	}

	/**
	 * Builds a map of tile positions to objects based on object bounds.
	 * @return Map of tile positions to a list of objects whose bounds fall into those positions.
	 */
	private Map<TileNode, List<WorldObject>> buildBoundsCoords() {
		List<WorldObject> objects = new ArrayList<>();
		objects.addAll(worldManager.getInteractibleObjects());
		objects.addAll(worldManager.getCurrentWorld().getObjectList(WorldConstants.OBJGROUP_TRIGGER));
		HashMap<TileNode, List<WorldObject>> objectCoords = new HashMap<>();
		for (WorldObject object : objects) {
			if (object.isRemoved()) {
				continue;
			}
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
		boundsCoords = buildBoundsCoords();
		for (EntityRenderer renderer : worldState.entityRenderers) {
			Entity entity = renderer.getEntity();
			assignMovement(entity, boundsCoords, delta);
			float oldX = entity.getX();
			float oldY = entity.getY();
			entity.move();

			WorldObject intersectObject = findIntersectObject(entity, boundsCoords);
			if (intersectObject != null) {
				entity.setX(oldX);
				entity.setY(oldY);

				if (entity.canDestroy() && worldManager.getCurrentWorld().isExitActive()) {
					if (Math.random() * 100 + 1 > Constants.DESTRUCTION_PERCENTAGE)
						intersectObject.setRemoved(true);
				}
			}
		}
		worldState.entitiesByDistance.clear();
		worldState.entitiesByDistance.addAll(worldState.entities);
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
			List<List<TileNode>> paths;
			float maxSpeed = isExitActive ? entity.getMaxExitSpeed() : entity.getMaxNormalSpeed();
			if (entityTileX == (int) attractor.getX() && entityTileY == (int) attractor.getY()) {
				paths = buildOriginPath(entityTileX, entityTileY);
			} else {
				paths = worldState.pathfinder.calculateLeastCostPath(
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
					entity.setPath(path);
					entity.setMovement(Movement.movementTo(
							maxSpeed, entity.getX(), entity.getY(), attractor.getX(), attractor.getY()));
				} else {
					entity.setSpeed(maxSpeed);
					entity.setPath(path);
				}
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

	private void createEntities(long renderTime)
			throws SlickException {
		ArrayList<WorldObject> mobList = worldManager.getCurrentWorld()
				.getObjectList(WorldConstants.OBJGROUP_MOB);
		for (WorldObject mob : mobList) {
			int mobId = Integer.parseInt(mob.getProperty(WorldConstants.MOBPROP_ID, "0"));
			EntityType type = EntityType.forTypeId(mobId);
			if (type == null) {
				System.err.println("Unknown mob type: " + mobId);
				continue;
			}
			Movement mobMovement;
			EntityRenderer entityRenderer;
			switch(type) {
				case HORN_DEMON:
					mobMovement = new Movement(HornDemon.DEFAULT_SPEED, CardinalDirection.LEFT);
					entityRenderer = HornDemon.buildRenderer(renderTime, new HornDemon(mob.getX(), mob.getY(), mobMovement));
					break;
				case SKULL_FACE:
					mobMovement = new Movement(SkullFace.DEFAULT_SPEED, CardinalDirection.LEFT);
					entityRenderer = SkullFace.buildRenderer(renderTime, new SkullFace(mob.getX(), mob.getY(), mobMovement));
					break;
				case FIRE_FACE:
					mobMovement = new Movement(FireFace.DEFAULT_SPEED, CardinalDirection.LEFT);
					entityRenderer = FireFace.buildRenderer(renderTime, new FireFace(mob.getX(), mob.getY(), mobMovement));
					break;
				case CLAWED_BITER:
					mobMovement = new Movement(ClawedBiter.DEFAULT_SPEED, CardinalDirection.LEFT);
					entityRenderer = ClawedBiter.buildRenderer(renderTime,
							new ClawedBiter(mob.getX(), mob.getY(), mobMovement));
					addEntity(entityRenderer);
					break;
				default:
					System.err.println("No mob creation specified for mob " + type);
					continue;
			}
			addEntity(entityRenderer);
		}
	}

	public void reset(long renderTime) throws SlickException {
		worldState = new WorldState(this.worldManager);
		createEntities(renderTime);
	}

	/**
	 * Stores the state of each world, which is discarded upon loading the next.
	 */
	private final class WorldState {
		private final TreeSet<EntityRenderer> entityRenderers;
		private final Set<Entity> entities;
		private final TreeSet<Entity> entitiesByDistance;
		private final WorldMap map;
		private final Pathfinder pathfinder;
		public WorldState(WorldManager worldManager1) {
			this.map = worldManager1.getCurrentWorld().getWorldMap();
			pathfinder = new Pathfinder(new MapNodeBuilder(map));

			// Entity renders are ordered by Y position, then X position, so that they can be rendered in order
			entityRenderers = new TreeSet<>(new Comparator<EntityRenderer>() {
				@Override
				public int compare(EntityRenderer o1, EntityRenderer o2) {
					Entity e1 = o1.getEntity();
					Entity e2 = o2.getEntity();
					int cmp = Float.compare(e1.getY(), e2.getY());
					return (cmp == 0) ? Float.compare(e1.getX(), e2.getX()) : cmp;
				}
			});
			entities = new HashSet<>();
			entitiesByDistance = new TreeSet<>(new Comparator<Entity>() {
				@Override
				public int compare(Entity o1, Entity o2) {
					float distance1 = o1.getPosn().distance(EntityManager.this.player.getPosn());
					float distance2 = o2.getPosn().distance(EntityManager.this.player.getPosn());
					return Float.compare(distance1, distance2);
				}
			});
		}
	}
}
