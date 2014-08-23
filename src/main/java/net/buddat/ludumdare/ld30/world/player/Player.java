package net.buddat.ludumdare.ld30.world.player;

/**
 * Represents the player, movement and animation.
 */
public class Player {
	private Direction direction;
	private float x;
	private float y;
	private float speed;
	private Direction facingUpDown = Direction.DOWN;
	private Direction facingLeftRight = Direction.LEFT;

	public Player(float x, float y, Direction facingUpDown, Direction facingLeftRight, float speed) {
		this.x = x;
		this.y = y;
		this.facingUpDown = facingUpDown;
		this.facingLeftRight = facingLeftRight;
		direction = facingLeftRight;
		this.speed = speed;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void move(Direction direction) {
		setDirection(direction);
		switch (direction) {
			case LEFT:
				x -= speed;
				break;
			case RIGHT:
				x += speed;
				break;
			case UP:
				y -= speed;
				break;
			case DOWN:
				y += speed;
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
		x += xOffset;
		y += yOffset;
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
}
