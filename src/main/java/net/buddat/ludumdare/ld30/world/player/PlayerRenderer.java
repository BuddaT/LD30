package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.Constants;

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
	private static final int RIGHT_X_OFFSET = WIDTH;
	private static final int UP_Y_OFFSET = HEIGHT;

	private final Image playerIcon;
	private final Image shadowIcon;

	public PlayerRenderer(Player player) throws SlickException {
		this.player = player;
		try {
			playerIcon = new Image("sprites/player.png");
			shadowIcon = new Image("sprites/player_shadow.png");
		} catch (SlickException e) {
			String errMsg = "Error loading player images";
			System.out.println(errMsg);
			throw e;
		}
	}

	public void render() {
		final int xOffset;
		final int yOffset;
		if (Direction.LEFT.equals(player.getFacingLeftRight())) {
			xOffset = LEFT_X_OFFSET;
		} else {
			xOffset = RIGHT_X_OFFSET;
		}
		if (Direction.UP.equals(player.getFacingUpDown())) {
			yOffset = UP_Y_OFFSET;
		} else {
			yOffset = DOWN_Y_OFFSET;
		}
		shadowIcon.draw(Constants.PLR_DRAWN_X, Constants.PLR_DRAWN_Y,
				Constants.PLR_DRAWN_X + WIDTH, Constants.PLR_DRAWN_Y + HEIGHT,
				xOffset, yOffset, xOffset + WIDTH, yOffset + HEIGHT);
		playerIcon.draw(Constants.PLR_DRAWN_X, Constants.PLR_DRAWN_Y,
				Constants.PLR_DRAWN_X + WIDTH, Constants.PLR_DRAWN_Y + HEIGHT,
				xOffset, yOffset, xOffset + WIDTH, yOffset + HEIGHT);
	}
}
