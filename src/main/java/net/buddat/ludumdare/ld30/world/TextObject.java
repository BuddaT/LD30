package net.buddat.ludumdare.ld30.world;

import org.newdawn.slick.tiled.TiledMap;

public class TextObject extends WorldObject {

	private boolean isActivated = false;
	private boolean isShowing = false;

	public TextObject(TiledMap parentMap, int groupId, int objectId) {
		super(parentMap, groupId, objectId);
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	public boolean isShowing() {
		return isShowing;
	}

	public void setShowing(boolean isShowing) {
		this.isShowing = isShowing;
	}

}
