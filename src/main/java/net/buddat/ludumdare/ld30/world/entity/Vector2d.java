package net.buddat.ludumdare.ld30.world.entity;

/**
 * Represents a two dimensional vector, for example as used in an absolute or relative position.
 * This class is immutable and therefore thread-safe.
 */
public final class Vector2d {
	private final float x;
	private final float y;

	public Vector2d(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public static Vector2d fromPolarDegrees(float radius, float degrees) {
		return new Vector2d(radius * (float) Math.cos(toRadians(degrees)),
				radius * (float) Math.sin(toRadians(degrees)));
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public Vector2d add(Vector2d that) {
		return new Vector2d(x + that.x, y + that.y);
	}

	public static double toRadians(double degrees) {
		return degrees * Math.PI / 180d;
	}

	public static double toDegrees(double radians) {
		return radians * 180d / Math.PI;
	}
}