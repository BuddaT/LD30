package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.Constants;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Controls the drawing of a player
 */
public class PlayerRenderer {
	private final Player player;
	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private static final int LEFT_X_OFFSET = 0;
	private static final int DOWN_Y_OFFSET = 0;
	private static final int MOVING_X_OFFSET = WIDTH;
	private static final int RIGHT_X_OFFSET = WIDTH * 2;
	private static final int UP_Y_OFFSET = HEIGHT;
	private static final int NUM_MOVING_ANIMS = 2;
	private static final float ANIM_RATE = 5f;

	private final Image playerIcon;
	private final Image shadowIcon;

	private int lastMovingAnim = 0;
	private long lastAnimationRenderTime;

	public PlayerRenderer(GameContainer gc, Player player) throws SlickException {
		this.player = player;
		try {
			playerIcon = new Image("sprites/player.png");
			shadowIcon = new Image("sprites/player_shadow.png");
		} catch (SlickException e) {
			String errMsg = "Error loading player images";
			System.out.println(errMsg);
			throw e;
		}
		lastAnimationRenderTime = gc.getTime();
	}

	public void render(GameContainer gc) {
		long newAnimationRenderTime = gc.getTime();
		int xOffset = calcImageXOffset(newAnimationRenderTime);
		final int yOffset;
		if (CardinalDirection.UP.equals(player.getFacingUpDown())) {
			yOffset = UP_Y_OFFSET;
		} else {
			yOffset = DOWN_Y_OFFSET;
		}
		drawPlayerImage(shadowIcon, xOffset, yOffset);
		drawPlayerImage(playerIcon, xOffset, yOffset);
	}

	private void drawPlayerImage(Image image, float xOffset, float yOffset) {
		image.draw(Constants.PLR_DRAWN_X, Constants.PLR_DRAWN_Y,
				Constants.PLR_DRAWN_X + WIDTH, Constants.PLR_DRAWN_Y + WIDTH,
				xOffset, yOffset, xOffset + WIDTH, yOffset + HEIGHT);
	}

	/**
	 * Images for animation and direction are tiled within in a single image
	 * file. Calculate the X offset of the relevant tile from which to display.
	 * @return xOffset
	 */
	private int calcImageXOffset(long newAnimationRenderTime) {
		int xOffset;
		if (CardinalDirection.LEFT.equals(player.getFacingLeftRight())) {
			xOffset = LEFT_X_OFFSET;
		} else {
			xOffset = RIGHT_X_OFFSET;
		}
		if (player.isMoving()) {
			if (newAnimationRenderTime - lastAnimationRenderTime >= (1000 / ANIM_RATE)) {
				lastMovingAnim = (lastMovingAnim + 1) % NUM_MOVING_ANIMS;
				lastAnimationRenderTime = newAnimationRenderTime;
			}
			xOffset += MOVING_X_OFFSET * lastMovingAnim;
		} else {
			lastMovingAnim = 0;
		}
		return xOffset;
	}
}
