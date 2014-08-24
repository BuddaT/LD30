package net.buddat.ludumdare.ld30;

import net.buddat.ludumdare.ld30.world.entity.Entity;
import net.buddat.ludumdare.ld30.world.entity.EntityRenderer;
import org.newdawn.slick.GameContainer;

import java.util.*;

/**
 * Manages NPC entities
 */
public class EntityManager {
	private final TreeSet<EntityRenderer> entityRenderers;
	private EntityRenderer lastRenderedBelow;
	public EntityManager() {
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
	}

}
