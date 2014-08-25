package net.buddat.ludumdare.ld30.world;

import org.newdawn.slick.geom.Vector2f;

/**
 * Object that can attract entities.
 */
public interface EntityAttractor {
	/**
	 * X position of the attractor
	 * @return X position
	 */
	public float getX();

	/**
	 * Y position of the attractor
	 * @return Y position
	 */
	public float getY();

	/**
	 * Returns a vector containing the position of this object.
	 * @return Vector position.
	 */
	public Vector2f getPosn();
}
