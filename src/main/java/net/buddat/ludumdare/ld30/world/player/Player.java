package net.buddat.ludumdare.ld30.world.player;

import net.buddat.ludumdare.ld30.Collidable;
import net.buddat.ludumdare.ld30.world.EntityAttractor;
import net.buddat.ludumdare.ld30.world.TextObject;
import net.buddat.ludumdare.ld30.world.World;
import net.buddat.ludumdare.ld30.world.WorldConstants;
import net.buddat.ludumdare.ld30.world.WorldObject;
import net.buddat.ludumdare.ld30.world.entity.Movement;
import net.buddat.ludumdare.ld30.world.entity.Vector2d;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * Represents the player, movement and animation.
 */
public class Player implements Collidable, EntityAttractor {
	private static final float BOUNDS_X_OFFSET = -0.2f;
	private static final float BOUNDS_Y_OFFSET = -0.5f;
	private static final float BOUNDS_WIDTH = 0.4f;
	private static final float BOUNDS_HEIGHT = 0.5f;

	private CardinalDirection direction;
	private float x;
	private float y;
	private Movement movement;
	private boolean isMoving = false;

	/**
	 * Lateral (x/y) speed when moving diagonally
	 */
	private CardinalDirection facingUpDown = CardinalDirection.DOWN;
	private CardinalDirection facingLeftRight = CardinalDirection.LEFT;

	private final Rectangle playerBounds, pickingBounds;

	private WorldObject heldObject = null;

	public Player(float x, float y, boolean isFacingDown, boolean isFacingLeft, Movement movement) {
		this.x = x;
		this.y = y;
		this.facingUpDown = isFacingDown ? CardinalDirection.DOWN : CardinalDirection.UP;
		this.facingLeftRight = isFacingLeft ? CardinalDirection.LEFT : CardinalDirection.RIGHT;
		direction = facingLeftRight;
		this.movement = movement;

		playerBounds = new Rectangle(x + BOUNDS_X_OFFSET, y + BOUNDS_Y_OFFSET, BOUNDS_WIDTH, BOUNDS_HEIGHT);
		pickingBounds = new Rectangle(x - (facingLeftRight == CardinalDirection.LEFT ? 0.4f : 0),
				y - 0.75f, 0.5f, 0.5f);
	}

	@Override
	public float getX() {
		return x;
	}

	public int getTileX() {
		return (int) x;
	}

	public void setX(float newX) {
		x = newX;
		playerBounds.setX(newX + BOUNDS_X_OFFSET);
		pickingBounds.setX(newX - (facingLeftRight == CardinalDirection.LEFT ? 0.4f : 0));

		if (heldObject != null) {
			heldObject.setX(newX
					- (facingLeftRight == CardinalDirection.LEFT ? heldObject.getWidth() + 0.3f
					: -0.35f));
		}
	}

	@Override
	public float getY() {
		return y;
	}

	public int getTileY() {
		return (int) y;
	}

	@Override
	public Vector2f getPosn() {
		return new Vector2f(x, y);
	}

	public void setY(float newY) {
		y = newY;
		playerBounds.setY(newY + BOUNDS_Y_OFFSET);
		pickingBounds.setY(newY - 0.75f);

		if (heldObject != null)
			heldObject.setY(newY - 0.7f);
	}


	private void attemptSetXY(World world, Vector2d newPosition, CardinalDirection... movingDir) {
		float oldX = getX(), oldY = getY();
		setX(newPosition.getX());
		setY(newPosition.getY());

		if (isCollision(world, newPosition.getX(), newPosition.getY(), movingDir)) {
			setX(oldX);
			setY(oldY);
		} else {
			for (WorldObject obj : world.getObjectList(WorldConstants.OBJGROUP_TEXT)) {
				TextObject text = (TextObject) obj;
				if (text.isActivated())
					continue;
				if (text.intersects(this)) {
					text.setActivated(true);
					text.setShowing(true);
					break;
				}
			}
		}
	}

	private boolean isCollision(World world, float x, float y, CardinalDirection... movingDir) {
		for (CardinalDirection d : movingDir) {
			switch (d) {
				case UP:
					if (world.getWorldMap().isCollideable((int) Math.floor(x),
							(int) Math.floor(y + BOUNDS_Y_OFFSET))) {
						return true;
					}
					break;
				case DOWN:
					if (world.getWorldMap().isCollideable((int) Math.floor(x), (int) Math.floor(y))) {
						return true;
					}
					break;
				case LEFT:
					if (world.getWorldMap().isCollideable((int) Math.floor(x + BOUNDS_X_OFFSET / 2.0f), 
							(int) Math.floor(y))) {
						return true;
					}
					break;
				case RIGHT:
					if (world.getWorldMap().isCollideable((int) Math.floor(x - BOUNDS_X_OFFSET / 2.0f), 
							(int) Math.floor(y))) {
						return true;
					}
					break;
				default:
					
				break;
			}
		}

		for (WorldObject obj : world.getObjectList(WorldConstants.OBJGROUP_INTERACTIBLE)) {
			if (obj == heldObject)
				continue;
			if (obj.isRemoved())
				continue;

			if (obj.intersects(this))
				return true;
		}

		for (WorldObject obj : world.getObjectList(WorldConstants.OBJGROUP_TRIGGER)) {
			if (obj.isRemoved())
				continue;
			if (obj.intersects(this))
				return true;
		}

		return false;
	}

	public void move(World world, float angularDirection) {
		setDirection(angularDirection);
		movement = new Movement(movement.getSpeed(), angularDirection);
		attemptSetXY(world, movement.calculateNewPosition(x, y), facingLeftRight, facingUpDown);
		isMoving = true;
	}

	public void move(World world, CardinalDirection direction) {
		setDirection(direction);
		movement = new Movement(movement.getSpeed(), direction);
		attemptSetXY(world, movement.calculateNewPosition(x, y), direction);
		isMoving = true;
	}

	public void setDirection(float angularDirection) {
		facingLeftRight = CardinalDirection.getHorizontalBias(angularDirection, facingLeftRight);
		facingUpDown = CardinalDirection.getVerticalBias(angularDirection, facingUpDown);
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
		this.movement = movement.changeSpeed(speed);
	}

	public CardinalDirection getFacingLeftRight() {
		return facingLeftRight;
	}

	public CardinalDirection getFacingUpDown() {
		return facingUpDown;
	}

	public void setIsMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public boolean isMoving() {
		return isMoving;
	}

	@Override
	public boolean intersects(Collidable other) {
		return getBounds().intersects(other.getBounds());
	}

	@Override
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
			heldObject.setX(x
					- (facingLeftRight == CardinalDirection.LEFT ? heldObject.getWidth() + 0.3f
					: -0.35f));
			heldObject.setY(y - 0.7f);
		}
	}

	public void reset(float x, float y, boolean isFacingDown, boolean isFacingLeft) {
		this.x = x;
		this.y = y;
		this.facingUpDown = isFacingDown ? CardinalDirection.DOWN : CardinalDirection.UP;
		this.facingLeftRight = isFacingLeft ? CardinalDirection.LEFT : CardinalDirection.RIGHT;
		direction = facingLeftRight;

		playerBounds.setBounds(x + BOUNDS_X_OFFSET, y + BOUNDS_Y_OFFSET, BOUNDS_WIDTH,
				BOUNDS_HEIGHT);
		pickingBounds.setBounds(x - (facingLeftRight == CardinalDirection.LEFT ? 0.4f : 0),
				y - 0.75f, 0.5f, 0.5f);

		heldObject = null;
	}
}
