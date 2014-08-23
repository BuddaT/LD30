package net.buddat.ludumdare.ld30.world.player;

/**
 * Direction in which the player is facing.
 */
public enum CardinalDirection {
	UP(false),
	DOWN(false),
	LEFT(true),
	RIGHT(true);

	private final boolean isHorizontal;
	private CardinalDirection(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}

	public boolean isHorizontal() {
		return isHorizontal;
	}

	public boolean isVertical() {
		return !isHorizontal;
	}
}
