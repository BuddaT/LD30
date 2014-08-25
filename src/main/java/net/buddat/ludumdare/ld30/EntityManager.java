package net.buddat.ludumdare.ld30;

import net.buddat.ludumdare.ld30.ai.MapNodeBuilder;
import net.buddat.ludumdare.ld30.ai.NodeBuilder;
import net.buddat.ludumdare.ld30.ai.Pathfinder;
import net.buddat.ludumdare.ld30.ai.TileNode;
import net.buddat.ludumdare.ld30.world.WorldMap;
import net.buddat.ludumdare.ld30.world.entity.Entity;
import net.buddat.ludumdare.ld30.world.entity.EntityRenderer;
import net.buddat.ludumdare.ld30.world.entity.Movement;
import net.buddat.ludumdare.ld30.world.player.Player;
import org.newdawn.slick.GameContainer;

import java.util.*;

/**
 * Manages NPC entities
 */
public class EntityManager {
	private final TreeSet<EntityRenderer> entityRenderers;
	private final WorldMap map;
	private final Player player;
	private final Pathfinder pathfinder;

	private EntityRenderer lastRenderedBelow;

	public EntityManager(WorldMap map, Player player) {
		this.map = map;
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

	public void updateEntities() {
		for (EntityRenderer renderer : entityRenderers) {
			Entity entity = renderer.getEntity();
			assignMovement(entity);
			entity.move();
		}
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
			TileNode nextNode;
			if (path.isEmpty()) {
				System.err.println("Empty path returned from path finder for (" + entityTileX + ","
						+ entityTileY + ") to (" + player.getTileX() + "," + player.getTileY() + ")");
				entity.setSpeed(0);
				return;
			} else if (path.size() == 1) {
				// Path only contains origin node?
				nextNode = path.get(0);
			} else {
				nextNode = path.get(1);
			}
			entity.setMovement(Movement.movementTo(entity.getSpeed(), entity.getX(), entity.getY(), nextNode));
		}
	}
}
