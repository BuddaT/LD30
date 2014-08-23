package net.buddat.ludumdare.ld30.controls;

import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;
import net.buddat.ludumdare.ld30.world.player.Player;

import org.newdawn.slick.Input;

/**
 *
 */
public class Controller {

	final Player player;
	final WorldManager worldManager;

	public Controller(WorldManager worldManager, Player player) {
		this.worldManager = worldManager;
		this.player = player;
	}

	public void handleInput(Input input) {
		CardinalDirection leftRight = null;
		CardinalDirection upDown = null;
		if (input.isKeyDown(Input.KEY_LEFT)) {
			leftRight = CardinalDirection.LEFT;
		} else if (input.isKeyDown(Input.KEY_RIGHT)) {
			leftRight = CardinalDirection.RIGHT;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			upDown = CardinalDirection.DOWN;
		} else if (input.isKeyDown(Input.KEY_UP)) {
			upDown = CardinalDirection.UP;
		}
		if (leftRight != null) {
			if (upDown == null) {
				player.move(worldManager.getCurrentWorld(), leftRight);
			} else {
				player.moveDiagonal(worldManager.getCurrentWorld(), upDown, leftRight);
			}
		} else if (upDown != null) {
			player.move(worldManager.getCurrentWorld(), upDown);
		}
	}
}
