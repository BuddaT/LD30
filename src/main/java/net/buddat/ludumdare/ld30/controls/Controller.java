package net.buddat.ludumdare.ld30.controls;

import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.player.Direction;
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
		if (input.isKeyDown(Input.KEY_LEFT)) {
			player.move(worldManager.getCurrentWorld(), Direction.LEFT);
		} else if (input.isKeyDown(Input.KEY_RIGHT)) {
			player.move(worldManager.getCurrentWorld(), Direction.RIGHT);
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			player.move(worldManager.getCurrentWorld(), Direction.DOWN);
		} else if (input.isKeyDown(Input.KEY_UP)) {
			player.move(worldManager.getCurrentWorld(), Direction.UP);
		}
	}
}
