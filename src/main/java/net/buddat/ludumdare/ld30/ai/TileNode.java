package net.buddat.ludumdare.ld30.ai;

/**
 * Represents the coordinates of a tile, for searching purposes.
 */
public class TileNode {
	private final int x;
	private final int y;

	public TileNode(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Double distanceTo(TileNode node) {
		int xDiff = Math.abs(node.x - x);
		int yDiff = Math.abs(node.y - y);
		if (xDiff == 0) {
			return (double) yDiff;
		} else if (yDiff == 0) {
			return (double) xDiff;
		} else {
			return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
		}
	}
}