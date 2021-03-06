package net.buddat.ludumdare.ld30.world.entity;

import net.buddat.ludumdare.ld30.Constants;
import net.buddat.ludumdare.ld30.ai.TileNode;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;
import org.newdawn.slick.geom.Vector2f;

/**
 * Defines movement using a combination of speed and angularDirection. Direction may
 * be specified either with an angle or with a cardinal angularDirection.
 */
public class Movement {
	private final float speed;
	private final float angularDirection;
	private final float lateralSpeed;
	private final CardinalDirection cardinalDirection;

	public Movement(float speed, CardinalDirection direction) {
		this.speed = speed;
		this.lateralSpeed = calculateLateralSpeed(speed);
		this.angularDirection = direction.getAngle();
		this.cardinalDirection = direction;
	}

	public Movement(float speed, float angularDirection) {
		this.speed = speed;
		this.lateralSpeed = calculateLateralSpeed(speed);
		this.angularDirection = angularDirection;
		CardinalDirection newCardinal = null;
		for (CardinalDirection cardinal : CardinalDirection.values()) {
			if (cardinal.getAngle() == angularDirection) {
				newCardinal = cardinal;
				break;
			}
		}
		cardinalDirection = newCardinal;
	}

	public float getSpeed() {
		return speed;
	}

	public float getDirection() {
		return angularDirection;
	}

	public Movement changeSpeed(float newSpeed) {
		if (cardinalDirection == null) {
			return new Movement(newSpeed, angularDirection);
		} else {
			return new Movement(newSpeed, cardinalDirection);
		}
	}

	/**
	 * Calculates a Vector2d from a given x,y position, determining based on
	 * current speed and direction the x and y offsets required to move.
	 * @param x X coordinate from which to move
	 * @param y Y coordinate from which to move
	 * @return Vector2d containing the x and y offsets for movement.
	 */
	public Vector2d calculateNewPosition(float x, float y) {
		if (speed == 0) {
			return new Vector2d(x, y);
		} else if (cardinalDirection == null) {
			return new Vector2d(x, y).add(calculateOffset(speed, angularDirection));
		} else {
			switch(cardinalDirection) {
				case LEFT:
					return new Vector2d(x - speed, y);
				case RIGHT:
					return new Vector2d(x + speed, y);
				case UP:
					return new Vector2d(x, y - speed);
				case DOWN:
					return new Vector2d(x, y + speed);
				default:
					System.out.println("Unknown direction: " + cardinalDirection);
					return null;
			}
		}
	}

	public static float calculateLateralSpeed(float speed) {
		return (float) Math.sqrt(Math.pow(speed, 2) / 2);
	}

	public static Vector2d calculateOffset(float speed, float angularDirection) {
		return Vector2d.fromPolarDegrees(speed, angularDirection);
	}

	public static float combinedAngle(CardinalDirection direction1, CardinalDirection direction2) {
		Vector2d vec1 = Vector2d.fromPolarDegrees(1, direction1.getAngle());
		Vector2d combined = vec1.add(Vector2d.fromPolarDegrees(1, direction2.getAngle()));
		double theta = Math.atan2(combined.getY(), combined.getX());
		if (Double.isNaN(theta)) {
			throw new IllegalArgumentException(
					"Invalid cardinal directions to combine: " + direction1 + ", " + direction2);
		} else if (theta < 0) {
			theta += 2 * Math.PI;
		}
		return (float) Vector2d.toDegrees(theta);
	}

	public static Movement movementTo(float speed, float x, float y, float destX, float destY) {
		Vector2f difference = new Vector2f(destX - x, destY - y);
		return new Movement(speed, (float) difference.getTheta());
	}

	/**
	 * Calculates direct line of movement from the given position to the destination tile node.
	 * @param speed Speed of movement
	 * @param x Origin x position
	 * @param y Origin y position
	 * @param dest Destination tile
	 * @return
	 */
	public static Movement movementTo(float speed, float x, float y, TileNode dest) {
		int srcTileX = (int) x;
		int srcTileY = (int) y;
		float destX = dest.getX();
		if (dest.getX() < srcTileX) {
			destX++;
		} else if (dest.getX() == srcTileX) {
			destX += 0.5f;
		}
		float destY = dest.getY();
		if (dest.getY() < srcTileY) {
			destY++;
		} else if (dest.getY() == srcTileY) {
			destY += 0.5f;
		}
		return movementTo(speed, x, y, destX, destY);
	}
}
