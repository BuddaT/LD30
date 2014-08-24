package net.buddat.ludumdare.ld30.world.entity;

import net.buddat.ludumdare.ld30.world.player.CardinalDirection;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the {@link Movement} class
 */
public class MovementTest {
	private static final double ALLOWED_DELTA = 0.0001;
	private float DOWN_LEFT = 135;
	private float DOWN_RIGHT = 45;
	private float UP_RIGHT = 315;
	private float UP_LEFT = 225;

	@Test
	public void testMoveDown() {
		Movement movement = new Movement(1f, CardinalDirection.DOWN);
		Vector2d vector2d = movement.calculateNewPosition(100, 100);
		assertStep("Moving down should give correct offset", vector2d, 100, 101);

		movement = new Movement(1f, CardinalDirection.DOWN.getAngle());
		vector2d = movement.calculateNewPosition(100, 100);
		assertStep("Moving down should give correct offset", vector2d, 100, 101);
	}

	@Test
	public void testConvertPolarToCartesian() {
		Vector2d cartesian = Movement.calculateOffset(1, 90);
		assertStep("Converting 90 degrees polar to cartesian", cartesian, 0, 1);
		cartesian = Movement.calculateOffset(1, 180);
		assertStep("Converting 180 degrees polar to cartesian", cartesian, -1, 0);
		cartesian = Movement.calculateOffset(1, 270);
		assertStep("Converting 180 degrees polar to cartesian", cartesian, 0, -1);
		cartesian = Movement.calculateOffset(1, 0);
		assertStep("Converting 180 degrees polar to cartesian", cartesian, 1, 0);
	}

	@Test
	public void testCombineCardinals() {
		float angle = Movement.combinedAngle(CardinalDirection.UP, CardinalDirection.RIGHT);
		assertEquals("Up and right", UP_RIGHT, angle, ALLOWED_DELTA);
		angle = Movement.combinedAngle(CardinalDirection.UP, CardinalDirection.LEFT);
		assertEquals("Up and left", UP_LEFT, angle, ALLOWED_DELTA);
		angle = Movement.combinedAngle(CardinalDirection.DOWN, CardinalDirection.LEFT);
		assertEquals("Down and left", DOWN_LEFT, angle, ALLOWED_DELTA);
		angle = Movement.combinedAngle(CardinalDirection.DOWN, CardinalDirection.RIGHT);
		assertEquals("Down and right", DOWN_RIGHT, angle, ALLOWED_DELTA);
	}

	@Test
	public void testMoveDiagonally() {
		Vector2d offset = Movement.calculateOffset(1,
				Movement.combinedAngle(CardinalDirection.UP, CardinalDirection.RIGHT));
		assertEquals("Up and right diagonal x offset", Math.sqrt(0.5), offset.getX(), ALLOWED_DELTA);
		assertEquals("Up and right diagonal y offset", -Math.sqrt(0.5), offset.getY(), ALLOWED_DELTA);
	}

	private void assertStep(String message, Vector2d vector2d, float expectedXOffset, float expectedYOffset) {
		assertNotNull("Vector2d returned should never be null", vector2d);
		assertEquals(message, expectedXOffset, vector2d.getX(), ALLOWED_DELTA);
		assertEquals(message, expectedYOffset, vector2d.getY(), ALLOWED_DELTA);
	}
}
