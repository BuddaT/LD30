package net.buddat.ludumdare.ld30.controls;

import net.buddat.ludumdare.ld30.world.player.Direction;
import net.buddat.ludumdare.ld30.world.player.Player;
import org.newdawn.slick.Input;

/**
 *
 */
public class Controller {
	final Player player;

	public Controller(Player player) {
		this.player = player;
	}

	public void handleInput(Input input) {
		if (input.isKeyDown(Input.KEY_LEFT)) {
			player.move(Direction.LEFT);
		} else if (input.isKeyDown(Input.KEY_RIGHT)) {
			player.move(Direction.RIGHT);
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			player.move(Direction.DOWN);
		} else if (input.isKeyDown(Input.KEY_UP)) {
			player.move(Direction.UP);
		}
	}
}
