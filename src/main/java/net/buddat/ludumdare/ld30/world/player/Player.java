package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.world.World;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Represents the player, movement and animation.
 */
public class Player {
	private final World world;
	private final TiledMap tiledMap;
	private Direction direction;
	private int x;
	private int y;

	public Player(World world, int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.world = world;
		this.tiledMap = world.getWorldMap();
		this.direction = direction;
	}

	public int getTileX() {
		return x / tiledMap.getTileWidth();
	}

	public int getTileY() {
		return y / tiledMap.getTileHeight();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void move(int xOffset, int yOffset) {
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
