package net.buddat.ludumdare.ld30.world.player;

/**
 * Direction in which the player is facing.
 */
public enum CardinalDirection {
	UP(false, 270),
	DOWN(false, 90),
	LEFT(true, 180),
	RIGHT(true, 0);

	private final boolean isHorizontal;
	private final float angle;
	private CardinalDirection(boolean isHorizontal, float angle) {
		this.isHorizontal = isHorizontal;
		this.angle = angle;
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public boolean isVertical() {
		return !isHorizontal;
	}

	public float getAngle() {
		return angle;
	}

	/**
	 * Returns the horizontal bias of an angular direction, or null if the angle is directly up or down.
	 * @param direction Direction for which horizontal bias is determined.
	 * @return {@link #LEFT} if the angle is towards the left, {@link #RIGHT}
	 * if the angle is towards the right, otherwise null.
	 */
	public static CardinalDirection getHorizontalBias(float direction) {
		return getHorizontalBias(direction, null);
	}

	/**
	 * Returns the horizontal bias of an angular direction, or defaultDirection if the angle is directly up or down.
	 * @param direction Direction for which horizontal bias is determined.
	 * @param defaultDirection Default to return if there is no horizontal bias, ie. if the angle is directly up or down.
	 * @return {@link #LEFT} if the angle is towards the left, {@link #RIGHT}
	 * if the angle is towards the right, otherwise null.
	 */
	public static CardinalDirection getHorizontalBias(float direction, CardinalDirection defaultDirection) {
		float dir = direction % 360;
		if (dir == UP.angle || dir == DOWN.angle) {
			return defaultDirection;
		}
		return (dir < UP.angle && dir > DOWN.angle) ? LEFT : RIGHT;
	}

	/**
	 * Returns the vertical bias of an angular direction, or null if the angle is directly left or right.
	 * @param direction Direction for which vertical bias is determined.
	 * @return {@link #UP} if the angle is towards up, {@link #DOWN}
	 * if the angle is towards down, otherwise null.
	 */
	public static CardinalDirection getVerticalBias(float direction) {
		return getVerticalBias(direction, null);
	}

	/**
	 * Returns the vertical bias of an angular direction, or defaultDirection if the angle is directly left or right.
	 * @param direction Direction for which vertical bias is determined.
	 * @param defaultDirection Direction to return if there is no vertical bias, ie. if the angle is directly left or
	 *                            right.
	 * @return {@link #UP} if the angle is towards up, {@link #DOWN}
	 * if the angle is towards down, otherwise null.
	 */
	public static CardinalDirection getVerticalBias(float direction, CardinalDirection defaultDirection) {
		float dir = direction % 360;
		if (dir == LEFT.angle || dir == RIGHT.angle) {
			return defaultDirection;
		}
		return (dir < RIGHT.angle || dir > LEFT.angle) ? UP : DOWN;
	}
}
