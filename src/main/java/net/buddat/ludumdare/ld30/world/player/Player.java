package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.Collidable;
import net.buddat.ludumdare.ld30.world.World;
import net.buddat.ludumdare.ld30.world.WorldConstants;
import net.buddat.ludumdare.ld30.world.WorldObject;

import org.newdawn.slick.geom.Rectangle;

/**
 * Represents the player, movement and animation.
 */
public class Player implements Collidable {

	private CardinalDirection direction;
	private float x;
	private float y;
	private float speed;
	private boolean isMoving = false;

	/**
	 * Lateral (x/y) speed when moving diagonally
	 */
	private final float lateralSpeed;
	private CardinalDirection facingUpDown = CardinalDirection.DOWN;
	private CardinalDirection facingLeftRight = CardinalDirection.LEFT;

	private final Rectangle playerBounds, pickingBounds;

	private WorldObject heldObject = null;

	public Player(float x, float y, boolean isFacingDown, boolean isFacingLeft, float speed) {
		this.x = x;
		this.y = y;
		this.facingUpDown = isFacingDown ? CardinalDirection.DOWN : CardinalDirection.UP;
		this.facingLeftRight = isFacingLeft ? CardinalDirection.LEFT : CardinalDirection.RIGHT;
		direction = facingLeftRight;
		this.speed = speed;
		this.lateralSpeed = calculateLateralSpeed(speed);

		playerBounds = new Rectangle(x - 0.2f, y - 1, 0.4f, 1f);
		pickingBounds = new Rectangle(x - (facingLeftRight == CardinalDirection.LEFT ? 0.4f : 0),
				y - 0.75f, 0.5f, 0.5f);
	}

	public float getX() {
		return x;
	}

	public void setX(float newX) {
		x = newX;
		playerBounds.setX(newX - 0.2f);
		pickingBounds.setX(newX - (facingLeftRight == CardinalDirection.LEFT ? 0.4f : 0));

		if (heldObject != null) {
			heldObject.setxPos(newX - (facingLeftRight == CardinalDirection.LEFT ? 0.8f : -0.35f));
		}
	}

	public float getY() {
		return y;
	}

	public void setY(float newY) {
		y = newY;
		playerBounds.setY(newY - 1);
		pickingBounds.setY(newY - 0.75f);

		if (heldObject != null)
			heldObject.setyPos(newY - 0.7f);
	}


	private void attemptSetXY(World world, float x, float y) {
		float oldX = getX(), oldY = getY();
		setX(x);
		setY(y);

		if (isCollision(world, x, y)) {
			setX(oldX);
			setY(oldY);
		}
	}

	private boolean isCollision(World world, float x, float y) {
		if (world.getWorldMap().isCollideable((int) Math.floor(x), (int) Math.floor(y))) {
			return true;
		}

		for (WorldObject obj : world.getObjectList(WorldConstants.OBJGROUP_INTERACTIBLE)) {
			if (obj == heldObject)
				continue;

			if (obj.intersects(this))
				return true;
		}

		return false;
	}

	public void moveDiagonal(World world, CardinalDirection upDown, CardinalDirection leftRight) {
		setDirection(upDown);
		setDirection(leftRight);
		float newX = x + (CardinalDirection.RIGHT.equals(leftRight) ? lateralSpeed : -lateralSpeed);
		float newY = y + (CardinalDirection.DOWN.equals(upDown) ? lateralSpeed : -lateralSpeed);
		attemptSetXY(world, newX, newY);
		isMoving = true;
	}

	public void move(World world, CardinalDirection direction) {
		setDirection(direction);
		switch (direction) {
			case LEFT:
			attemptSetXY(world, x - speed, y);
				break;
			case RIGHT:
			attemptSetXY(world, x + speed, y);
				break;
			case UP:
			attemptSetXY(world, x, y - speed);
				break;
			case DOWN:
			attemptSetXY(world, x, y + speed);
				break;
			default:
				System.out.println("Unknown direction: " + direction);
		}
		isMoving = true;
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

	private float calculateLateralSpeed(float diagonalSpeed) {
		return (float) Math.sqrt(Math.pow(diagonalSpeed, 2) / 2);
	}

	public void setIsMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public boolean intersects(Collidable other) {
		return getBounds().intersects(other.getBounds());
	}

	public Rectangle getBounds() {
		return playerBounds;
	}

	public Rectangle getPickingBounds() {
		return pickingBounds;
	}

	public WorldObject getHeldObject() {
		return heldObject;
	}

	public void setHeldObject(WorldObject newObject) {
		heldObject = newObject;

		if (heldObject != null) {
			heldObject.setxPos(x - (facingLeftRight == CardinalDirection.LEFT ? 0.8f : -0.35f));
			heldObject.setyPos(y - 0.7f);
		}
	}
}
