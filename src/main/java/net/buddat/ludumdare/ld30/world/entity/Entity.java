package net.buddat.ludumdare.ld30.world.entity;

import net.buddat.ludumdare.ld30.Collidable;
import net.buddat.ludumdare.ld30.Constants;
import net.buddat.ludumdare.ld30.ai.TileNode;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import java.util.List;

/**
 * NPC mob
 */
public abstract class Entity implements Collidable {
	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;
	private final float maxExitSpeed;
	private final float maxNormalSpeed;
	private float x;
	private float y;
	private CardinalDirection facingUpDown;
	private CardinalDirection facingLeftRight;

	private final Rectangle collisionBounds;
	private Movement movement;

	private long lastDestructionAttempt;

	private float senseRadius;
	private int pathUpdateDelta = 0;
	private List<TileNode> path;
	private TileNode nextPathStep;

	/**
	 * Creates an entity with the given characteristics.
	 *
	 * @param x X position
	 * @param y Y position
	 * @param movement Starting movement
	 * @param exitSpeed Speed to use one the exit is activated
	 * @param collisionBounds Bounding box to check for collisions with other objects
	 * @param senseRadius Radius in which the entity senses objects/player
	 */
	public Entity(float x, float y, Movement movement, float exitSpeed, Rectangle collisionBounds, float senseRadius) {
		this.x = x;
		this.y = y;
		this.maxExitSpeed = exitSpeed;
		this.maxNormalSpeed = movement.getSpeed();
		this.senseRadius = senseRadius;
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

	public Vector2f getPosn() {
		return new Vector2f(x, y);
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

	public float getMaxExitSpeed() {
		return maxExitSpeed;
	}

	public float getMaxNormalSpeed() {
		return maxNormalSpeed;
	}

	public void setMovement(Movement movement) {
		this.movement = movement;
		facingUpDown = CardinalDirection.getVerticalBias(movement.getDirection(), facingUpDown);
		facingLeftRight = CardinalDirection.getHorizontalBias(movement.getDirection(), facingLeftRight);
	}

	public void setPath(List<TileNode> path) {
		pathUpdateDelta = 0;
		if (path == null) {
			this.path = null;
			this.nextPathStep = null;
		} else {
			this.path = path;
			if (path.size() > 0) {
				if (path.size() > 1) {
					nextPathStep = path.get(1);
				} else {
					nextPathStep = path.get(0);
				}
				movement = Movement.movementTo(movement.getSpeed(), getX(), getY(), nextPathStep);
			}
		}
	}

	public int incrementPathUpdateDelta(int delta) {
		pathUpdateDelta += delta;
		return pathUpdateDelta;
	}

	public void setDirection(float direction) {
		this.movement = new Movement(movement.getSpeed(), direction);
		facingUpDown = CardinalDirection.getVerticalBias(direction, facingUpDown);
		facingLeftRight = CardinalDirection.getHorizontalBias(direction, facingLeftRight);
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
		Vector2d newPosition;
		if (path == null) {
			newPosition = movement.calculateNewPosition(getX(), getY());
		} else {
			// Need to move along a path
			if (nextPathStep == null) {
				nextPathStep = path.get(0);
				movement = Movement.movementTo(movement.getSpeed(), getX(), getY(), nextPathStep);
			}
			newPosition = movement.calculateNewPosition(getX(), getY());
			if ((int) newPosition.getX() != nextPathStep.getX() || (int) newPosition.getY() != nextPathStep.getY()) {
				// If we cross tiles, go to the next path
				if (path.size() > 1) {
					nextPathStep = path.get(1);
					movement = Movement.movementTo(movement.getSpeed(), getX(), getY(), nextPathStep);
				} else {
					// reached the end of the path
					path = null;
					nextPathStep = null;
					setSpeed(0);
				}
			}
		}
		setX(newPosition.getX());
		setY(newPosition.getY());
	}

	public float getSenseRadius() {
		return senseRadius;
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
