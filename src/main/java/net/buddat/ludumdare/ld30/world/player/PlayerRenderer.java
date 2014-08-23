package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.Constants;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Controls the drawing of a player
 */
public class PlayerRenderer {
	private final Player player;

	private final Image playerIconLeft;
	private final Image playerIconRight;
	private final Image shadowIconLeft;
	private final Image shadowIconRight;

	public PlayerRenderer(Player player) throws SlickException {
		this.player = player;
		try {
			playerIconLeft = new Image("sprites/player.png");
			playerIconRight = playerIconLeft.getFlippedCopy(true, false);
			shadowIconLeft = new Image("sprites/player_shadow.png");
			shadowIconRight = shadowIconLeft;
		} catch (SlickException e) {
			String errMsg = "Error loading player images";
			System.out.println(errMsg);
			throw e;
		}
	}

	public void render() {
		final Image shadowIcon;
		final Image playerIcon;

		if (Direction.LEFT.equals(player.getDirection())) {
			shadowIcon = shadowIconRight;
			playerIcon = playerIconRight;
		} else {

			shadowIcon = shadowIconRight;
			playerIcon = playerIconRight;
		}
		shadowIcon.draw(Constants.PLR_DRAWN_X, Constants.PLR_DRAWN_Y);
		playerIcon.draw(Constants.PLR_DRAWN_X, Constants.PLR_DRAWN_Y);
	}
}
