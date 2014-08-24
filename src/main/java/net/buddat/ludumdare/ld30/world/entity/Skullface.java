package net.buddat.ludumdare.ld30.world.entity;

import net.buddat.ludumdare.ld30.Collidable;
import org.newdawn.slick.geom.Rectangle;

/**
 * Skullface NPC entity
 */
public class Skullface extends Entity {
	private static final float BOUNDS_X_OFFSET = -0.2f;
	private static final float BOUNDS_Y_OFFSET = -1f;
	private static final float BOUNDS_WIDTH = 0.4f;
	private static final float BOUNDS_HEIGHT = 1f;

	public Skullface(float x, float y, Movement movement) {
		super(x, y, movement, new Rectangle(x + BOUNDS_X_OFFSET, y + BOUNDS_Y_OFFSET, BOUNDS_WIDTH, BOUNDS_HEIGHT));
	}
}
