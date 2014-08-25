package net.buddat.ludumdare.ld30.world.entity;

import net.buddat.ludumdare.ld30.Collidable;
import net.buddat.ludumdare.ld30.Constants;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;

import org.newdawn.slick.geom.Rectangle;

/**
 * NPC mob
 */
public abstract class Entity implements Collidable {
	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private float x;
	private float y;
	private final CardinalDirection facingUpDown;
	private final CardinalDirection facingLeftRight;

	private final Rectangle collisionBounds;
	private Movement movement;

	private long lastDestructionAttempt;

	public Entity(float x, float y, Movement movement, Rectangle collisionBounds) {
		this.x = x;
		this.y = y;
		this.facingUpDown = CardinalDirection.getVerticalBias(movement.getDirection(), CardinalDirection.DOWN);
		this.facingLeftRight = CardinalDirection.getHorizontalBias(movement.getDirection(), CardinalDirection.RIGHT);
		this.collisionBounds = collisionBounds;
		setMovement(movement);

		lastDestructionAttempt = System.currentTimeMillis();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getTileX() {
		return (int) x;
	}

	public int getTileY() {
		return (int) y;
	}

	public void setX(float newX) {
		Rectangle bounds = getBounds();
		bounds.setX(bounds.getX() + (newX - x));
		x = newX;
	}

	public void setY(float newY) {
		Rectangle bounds = getBounds();
		bounds.setY(bounds.getY() + (newY - y));
		y = newY;
	}

	public float getSpeed() {
		return movement.getSpeed();
	}

	public void setSpeed(float speed) {
		this.movement = new Movement(speed, movement.getDirection());
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
	}

	public void setDirection(float direction) {
		this.movement = new Movement(movement.getSpeed(), direction);
	}

	public CardinalDirection getFacingUpDown() {
		return facingUpDown;
	}

	public CardinalDirection getFacingLeftRight() {
		return facingLeftRight;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public void move() {
		Vector2d newPosition = movement.calculateNewPosition(x, y);
		setX(newPosition.getX());
		setY(newPosition.getY());
	}

	public boolean canDestroy() {
		long timeSinceLast = System.currentTimeMillis() - lastDestructionAttempt;
		if (timeSinceLast > Constants.ENTITY_DESTRUCTION_DELAY) {
			lastDestructionAttempt = System.currentTimeMillis();
			return true;
		}

		return false;
	}

	@Override
	public boolean intersects(Collidable other) {
		return getBounds().intersects(other.getBounds());
	}

	@Override
	public Rectangle getBounds() {
		return collisionBounds;
	}
}
