package net.buddat.ludumdare.ld30;

import org.newdawn.slick.geom.Rectangle;

public interface Collidable {

	public boolean intersects(Collidable other);

	public Rectangle getBounds();

}
