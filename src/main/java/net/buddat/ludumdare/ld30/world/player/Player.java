package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.Collidable;
import net.buddat.ludumdare.ld30.Constants;
import net.buddat.ludumdare.ld30.world.WorldMap;

import org.newdawn.slick.geom.Rectangle;

/**
 * Represents the player, movement and animation.
 */
public class Player implements Collidable {

	private Direction direction;
	private float x;
	private float y;
	private float speed;
	private Direction facingUpDown = Direction.DOWN;
	private Direction facingLeftRight = Direction.LEFT;

	private final Rectangle playerBounds;

	public Player(float x, float y, Direction facingUpDown, Direction facingLeftRight, float speed) {
		this.x = x;
		this.y = y;
		this.facingUpDown = facingUpDown;
		this.facingLeftRight = facingLeftRight;
		direction = facingLeftRight;
		this.speed = speed;

		playerBounds = new Rectangle(x, y, Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
	}

	public float getX() {
		return x;
	}

	public void setX(float newX) {
		x = newX;
		playerBounds.setX(newX);
	}

	public float getY() {
		return y;
	}

	public void setY(float newY) {
		y = newY;
		playerBounds.setY(newY);
	}

	public void move(WorldMap worldMap, Direction direction) {
		setDirection(direction);
		switch (direction) {
			case LEFT:
				setX(x - speed);
				if (worldMap.getTileProperty(worldMap.getTileId((int) Math.floor(x), 
						(int) Math.floor(y), worldMap.getCollisionLayerId()), "collide", "false").equals("true")) {
					setX(x + speed);
				}
				break;
			case RIGHT:
				setX(x + speed);
				if (worldMap.getTileProperty(worldMap.getTileId((int) Math.floor(x), 
						(int) Math.floor(y), worldMap.getCollisionLayerId()), "collide", "false").equals("true")) {
					setX(x - speed);
				}
				break;
			case UP:
				setY(y - speed);
				if (worldMap.getTileProperty(worldMap.getTileId((int) Math.floor(x), 
						(int) Math.floor(y), worldMap.getCollisionLayerId()), "collide", "false").equals("true")) {
					setY(y + speed);
				}
				break;
			case DOWN:
				setY(y + speed);
				if (worldMap.getTileProperty(worldMap.getTileId((int) Math.floor(x), 
						(int) Math.floor(y), worldMap.getCollisionLayerId()), "collide", "false").equals("true")) {
					setY(y - speed);
				}
				break;
			default:
				System.out.println("Unknown direction: " + direction);
		}
	}

	public void move(float xOffset, float yOffset) {
		if (x < 0) {
			direction = Direction.LEFT;
		} else if (x > 0) {
			direction = Direction.RIGHT;
		}
		setX(x + xOffset);
		setY(y + yOffset);
	}

	public void setDirection(Direction direction) {
		if (this.direction.equals(direction)) {
			return;
		}
		if (direction.isHorizontal()) {
			facingLeftRight = direction;
		} else {
			facingUpDown = direction;
		}
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Direction getFacingLeftRight() {
		return facingLeftRight;
	}

	public Direction getFacingUpDown() {
		return facingUpDown;
	}

	public boolean intersects(Collidable other) {
		return getBounds().intersects(other.getBounds());
	}

	public Rectangle getBounds() {
		return playerBounds;
	}
}
