package net.buddat.ludumdare.ld30.controls;

import net.buddat.ludumdare.ld30.Constants;
import net.buddat.ludumdare.ld30.Game;
import net.buddat.ludumdare.ld30.world.TextObject;
import net.buddat.ludumdare.ld30.world.WorldConstants;
import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.WorldObject;
import net.buddat.ludumdare.ld30.world.entity.Movement;
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
		TextObject textShowing = null;

		if (input.isKeyPressed(Input.KEY_ESCAPE))
			worldManager.setNeedsReset(true);

		if (Game.gameOver)
			return;

		//noinspection PointlessBooleanExpression,ConstantConditions
		if (input.isKeyDown(Input.KEY_LCONTROL) && input.isKeyDown(Input.KEY_LSHIFT)
				&& input.isKeyDown(Input.KEY_P) && Constants.ALLOW_CHEATS)
			Game.exitOverride = true;

		for (WorldObject obj : worldManager.getCurrentWorld().getObjectList(
				WorldConstants.OBJGROUP_TEXT)) {
			TextObject text = (TextObject) obj;
			if (text.isShowing())
				textShowing = text;
		}

		if (textShowing == null) {
			if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
				leftRight = CardinalDirection.LEFT;
			} else if (input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
				leftRight = CardinalDirection.RIGHT;
			}
			if (input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
				upDown = CardinalDirection.DOWN;
			} else if (input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
				upDown = CardinalDirection.UP;
			}
			if (leftRight != null) {
				if (upDown == null) {
					player.move(worldManager.getCurrentWorld(), leftRight);
				} else {
					player.move(worldManager.getCurrentWorld(), Movement.combinedAngle(upDown, leftRight));
				}
			} else if (upDown != null) {
				player.move(worldManager.getCurrentWorld(), upDown);
			} else {
				player.setIsMoving(false);
			}

			if (input.isKeyPressed(Input.KEY_SPACE)) {
				if (player.getHeldObject() == null)
					for (WorldObject obj : worldManager.getInteractibleObjects()) {
						if (obj.isRemoved())
							continue;

						if (player.getPickingBounds().intersects(obj.getBounds())) {
							player.setHeldObject(obj);
							break;
						}
					}
				else
					player.setHeldObject(null);
			}
		} else {
			player.setIsMoving(false);
			if (input.isKeyPressed(Input.KEY_SPACE)) {
				textShowing.setShowing(false);
			}
		}
	}
}
