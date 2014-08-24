package net.buddat.ludumdare.ld30.world.player;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Verifies functionality of cardinal directions and angle calculations. Zero must be left and angles increase
 * counterclockwise, so that conversions match x/y drawing.
 */
public class CardinalDirectionTest {

	private float DOWN_LEFT = 135;
	private float DOWN_RIGHT = 45;
	private float UP_RIGHT = 315;
	private float UP_LEFT = 225;

	/**
	 * Vertical biases of an angle that exactly corresponds to a cardinal direction
	 */
	@Test
	public void testVerticalBiasDirect() {
		assertNull("Vertical bias of direct RIGHT should be null",
				CardinalDirection.getVerticalBias(CardinalDirection.RIGHT.getAngle()));
		assertNull("Vertical bias of direct LEFT should be null",
				CardinalDirection.getVerticalBias(CardinalDirection.LEFT.getAngle()));
	}

	/**
	 * Vertical biases of an angle that does not exactly correspond to a cardinal direction
	 */
	@Test
	public void testVerticalBiasAngle() {
		CardinalDirection bias = CardinalDirection.getVerticalBias(DOWN_LEFT);
		assertEquals("Vertical downwards bias of known angle down/left", CardinalDirection.DOWN, bias);
		bias = CardinalDirection.getVerticalBias(DOWN_RIGHT);
		assertEquals("Vertical downwards bias of known angle down/right", CardinalDirection.DOWN, bias);
		bias = CardinalDirection.getVerticalBias(UP_RIGHT);
		assertEquals("Vertical upwards bias of known angle up/right", CardinalDirection.UP, bias);
		bias = CardinalDirection.getVerticalBias(UP_LEFT);
		assertEquals("Vertical upwards bias of known angle up/left", CardinalDirection.UP, bias);
	}

	/**
	 * Horizontal biases of an angle that exactly corresponds to a cardinal direction
	 */
	@Test
	public void testHorizontalBiasDirect() {
		assertNull("Horizontal bias of direct UP should be null",
				CardinalDirection.getHorizontalBias(CardinalDirection.UP.getAngle()));
		assertNull("Horizontal bias of direct DOWN should be null",
				CardinalDirection.getHorizontalBias(CardinalDirection.DOWN.getAngle()));
	}

	/**
	 * Horizontal biases of an angle that does not exactly correspond to a cardinal direction
	 */
	@Test
	public void testHorizontalBiasAngle() {
		CardinalDirection bias = CardinalDirection.getHorizontalBias(DOWN_LEFT);
		assertEquals("Horizontal left bias of known angle down/left", CardinalDirection.LEFT, bias);
		bias = CardinalDirection.getHorizontalBias(DOWN_RIGHT);
		assertEquals("Horizontal right bias of known angle down/right", CardinalDirection.RIGHT, bias);
		bias = CardinalDirection.getHorizontalBias(UP_RIGHT);
		assertEquals("Horizontal right bias of known angle up/right", CardinalDirection.RIGHT, bias);
		bias = CardinalDirection.getHorizontalBias(UP_LEFT);
		assertEquals("Horizontal left bias of known angle up/left", CardinalDirection.LEFT, bias);
	}
}
