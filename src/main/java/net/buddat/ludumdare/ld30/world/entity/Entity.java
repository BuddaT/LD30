package net.buddat.ludumdare.ld30.world.entity;

import net.buddat.ludumdare.ld30.Collidable;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;
import org.newdawn.slick.geom.Rectangle;

/**
 * NPC mob
 */
public abstract class Entity implements Collidable {
	private float x;
	private float y;
	private CardinalDirection facingUpDown;
	private CardinalDirection facingLeftRight;
	private float speed;
	private float lateralSpeed;
	private float direction;

	private final Rectangle collisionBounds;

	public Entity(float x, float y, boolean isFacingDown, boolean isFacingLeft, float speed, Rectangle collisionBounds,
				  Movement movement) {
		this.x = x;
		this.y = y;
		this.facingUpDown = isFacingDown ? CardinalDirection.DOWN : CardinalDirection.UP;
		this.facingLeftRight = isFacingLeft ? CardinalDirection.LEFT : CardinalDirection.RIGHT;
		this.collisionBounds = collisionBounds;
		setSpeed(speed);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
		lateralSpeed = Movement.calculateLateralSpeed(speed);
	}

	public void setDirection(float direction) {
		this.direction = direction;
	}
}
