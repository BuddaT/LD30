package net.buddat.ludumdare.ld30;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Controls the drawing of a player
 */
public class PlayerRenderer {
	private final Player player;

	public PlayerRenderer(Player player) {
		this.player = player;
		try {
			Image playerIcon = new Image("sprites/player.png");
			Image shadowIcon = new Image("sprites/player_shadow.png");
			shadowIcon.draw(player.getX(), player.getY());
			playerIcon.draw(player.getX(), player.getY());
		} catch (SlickException e) {
			System.out.println("Error loading player images");
		}

	}
}
