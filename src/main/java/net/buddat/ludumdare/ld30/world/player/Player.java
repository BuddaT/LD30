package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.world.World;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Represents the player, movement and animation.
 */
public class Player {
	private Direction direction;
	private float x;
	private float y;

	public Player(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void move(float xOffset, float yOffset) {
		if (x < 0) {
			direction = Direction.LEFT;
		} else if (x > 0) {
			direction = Direction.RIGHT;
		}
		x += xOffset;
		y += yOffset;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
