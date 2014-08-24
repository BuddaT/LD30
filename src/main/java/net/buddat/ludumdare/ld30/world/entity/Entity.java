package net.buddat.ludumdare.ld30.world.entity;

import net.buddat.ludumdare.ld30.Collidable;
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
	private CardinalDirection facingUpDown;
	private CardinalDirection facingLeftRight;
	private float speed;
	private float lateralSpeed;
	private float direction;

	private final Rectangle collisionBounds;

	public Entity(float x, float y, Movement movement, Rectangle collisionBounds) {
		this.x = x;
		this.y = y;
		this.facingUpDown = CardinalDirection.getVerticalBias(movement.getDirection(), CardinalDirection.DOWN);
		this.facingLeftRight = CardinalDirection.getHorizontalBias(movement.getDirection(), CardinalDirection.RIGHT);
		this.collisionBounds = collisionBounds;
		setSpeed(speed);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float newX) {
		getBounds().setX(newX - x);
		x = newX;
	}

	public void setY(float newY) {
		getBounds().setY(newY - y);
		y = newY;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
		lateralSpeed = Movement.calculateLateralSpeed(speed);
	}

	public void setDirection(float direction) {
		this.direction = direction;
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

	@Override
	public boolean intersects(Collidable other) {
		return getBounds().intersects(other.getBounds());
	}

	@Override
	public Rectangle getBounds() {
		return collisionBounds;
	}
}
