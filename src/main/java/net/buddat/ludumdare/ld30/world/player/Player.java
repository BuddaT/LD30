package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.Collidable;
import net.buddat.ludumdare.ld30.Constants;
import net.buddat.ludumdare.ld30.world.WorldMap;

import org.newdawn.slick.geom.Rectangle;

/**
 * Represents the player, movement and animation.
 */
public class Player implements Collidable {

	private CardinalDirection direction;
	private float x;
	private float y;
	private float speed;
	/**
	 * Lateral (x/y) speed when moving diagonally
	 */
	private float lateralSpeed;
	private CardinalDirection facingUpDown = CardinalDirection.DOWN;
	private CardinalDirection facingLeftRight = CardinalDirection.LEFT;

	private final Rectangle playerBounds;

	public Player(float x, float y, boolean isFacingDown, boolean isFacingLeft, float speed) {
		this.x = x;
		this.y = y;
		this.facingUpDown = isFacingDown ? CardinalDirection.DOWN : CardinalDirection.UP;
		this.facingLeftRight = isFacingLeft ? CardinalDirection.LEFT : CardinalDirection.RIGHT;
		direction = facingLeftRight;
		this.speed = speed;
		this.lateralSpeed = calculateLateralSpeed(speed);

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


	private void attemptSetXY(WorldMap worldMap, float x, float y) {
		if (!isCollision(worldMap, x, y)) {
			setX(x);
			setY(y);
		}
	}

	private boolean isCollision(WorldMap worldMap, float x, float y) {
		return worldMap.getTileProperty(worldMap.getTileId((int) Math.floor(x),
				(int) Math.floor(y), worldMap.getCollisionLayerId()), "collide", "false").equals("true");
	}

	public void moveDiagonal(WorldMap worldMap, CardinalDirection upDown, CardinalDirection leftRight) {
		setDirection(upDown);
		setDirection(leftRight);
		float newX = x + (CardinalDirection.RIGHT.equals(leftRight) ? lateralSpeed : -lateralSpeed);
		float newY = y + (CardinalDirection.DOWN.equals(upDown) ? lateralSpeed : -lateralSpeed);
		attemptSetXY(worldMap, newX, newY);
	}

	public void move(WorldMap worldMap, CardinalDirection direction) {
		setDirection(direction);
		switch (direction) {
			case LEFT:
				attemptSetXY(worldMap, x - speed, y);
				break;
			case RIGHT:
				attemptSetXY(worldMap, x + speed, y);
				break;
			case UP:
				attemptSetXY(worldMap, x, y - speed);
				break;
			case DOWN:
				attemptSetXY(worldMap, x, y + speed);
				break;
			default:
				System.out.println("Unknown direction: " + direction);
		}
	}

	public void setDirection(CardinalDirection direction) {
		if (this.direction.equals(direction)) {
			return;
		}
		if (direction.isHorizontal()) {
			facingLeftRight = direction;
		} else {
			facingUpDown = direction;
		}
		this.direction = direction;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public CardinalDirection getFacingLeftRight() {
		return facingLeftRight;
	}

	public CardinalDirection getFacingUpDown() {
		return facingUpDown;
	}

	public boolean intersects(Collidable other) {
		return getBounds().intersects(other.getBounds());
	}

	public Rectangle getBounds() {
		return playerBounds;
	}

	private float calculateLateralSpeed(float diagonalSpeed) {
		return (float) Math.sqrt(Math.pow(diagonalSpeed, 2) / 2);
	}
}
