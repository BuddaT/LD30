package net.buddat.ludumdare.ld30.world.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/**
 * Looks a bit like a rabbit with a tail, oh well.
 */
public class HornDemon extends Entity {

	private static final float BOUNDS_X_OFFSET = -0.2f;
	private static final float BOUNDS_Y_OFFSET = -1f;
	private static final float BOUNDS_WIDTH = 0.4f;
	private static final float BOUNDS_HEIGHT = 1f;

	private static final int IMAGE_WIDTH = 32;
	private static final int NUM_ANIMS = 2;
	private static final int IMAGE_RIGHT_OFFSET = IMAGE_WIDTH * NUM_ANIMS;
	private static final int IMAGE_Y_OFFSET = 0;
	private static final float ANIMATION_RATE = 5;

	private static final float SENSE_RADIUS = 5;

	private static Image ICONS;
	private static Image SHADOWS;
	public static final float DEFAULT_SPEED = 0.01f;
	public static final float EXIT_SPEED = 0.05f;

	public HornDemon(float x, float y, Movement movement) {
		super(x, y, movement, EXIT_SPEED,
				new Rectangle(x + BOUNDS_X_OFFSET, y + BOUNDS_Y_OFFSET, BOUNDS_WIDTH, BOUNDS_HEIGHT),
				SENSE_RADIUS);
	}

	public static EntityRenderer buildRenderer(GameContainer gc, HornDemon entity) throws SlickException {
		if (ICONS == null) {
			ICONS = new Image("sprites/mobs.png");
			SHADOWS = new Image("sprites/mobs_shadow.png");
		}
		return new EntityRenderer(gc, entity, ICONS, SHADOWS, 0, IMAGE_Y_OFFSET, IMAGE_RIGHT_OFFSET, IMAGE_Y_OFFSET) {
			private int currentAnim = 0;
			@Override
			protected int calcImageXOffset(long lastAnimationRenderTime, long newTime) {
				int imageXOffset = super.calcImageXOffset(lastAnimationRenderTime, newTime);
				if (newTime - lastAnimationRenderTime > 1000 / ANIMATION_RATE) {
					currentAnim = (currentAnim + 1) % NUM_ANIMS;
					setLastAnimationRenderTime(newTime);
				}
				return imageXOffset + (currentAnim * IMAGE_WIDTH);
			}
		};
	}
}
