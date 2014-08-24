package net.buddat.ludumdare.ld30;

import net.buddat.ludumdare.ld30.world.entity.Entity;
import net.buddat.ludumdare.ld30.world.entity.EntityRenderer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.*;

/**
 * Manages NPC entities
 */
public class EntityManager {
	private final SortedSet<EntityRenderer> entityRenderers;
	public EntityManager() {
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

	public void renderEntities(GameContainer gc) {
		for (EntityRenderer renderer : entityRenderers) {
			renderer.render(gc);
		}
	}

	public void addEntity(EntityRenderer entityRenderer) {
		entityRenderers.add(entityRenderer);
	}

}
