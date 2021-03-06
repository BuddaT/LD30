package net.buddat.ludumdare.ld30.world;

import net.buddat.ludumdare.ld30.Collidable;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

public class WorldObject implements Collidable, EntityAttractor {

	public static Image highlightImage;

	private TiledMap parentMap;
	private final int groupId, objectId;

	private float xPos;
	private float yPos;
	private float width;
	private float height;

	private final Rectangle objectBounds;

	private final String objName;
	private final String objType;

	private Image objImage;
	private Sound objSound;

	private boolean removedFromMap = false;
	private boolean removedSoundPlayed = false;

	private boolean isAttractor = false;

	private boolean highlighted = false;

	public WorldObject(TiledMap parentMap, int groupId, int objectId) {
		this.parentMap = parentMap;
		this.groupId = groupId;
		this.objectId = objectId;

		this.xPos = parentMap.getObjectX(groupId, objectId) / (float) parentMap.getTileWidth();
		this.yPos = parentMap.getObjectY(groupId, objectId) / (float) parentMap.getTileHeight();
		this.width = parentMap.getObjectWidth(groupId, objectId) / (float) parentMap.getTileWidth();
		this.height = parentMap.getObjectHeight(groupId, objectId)
				/ (float) parentMap.getTileHeight();

		this.objName = parentMap.getObjectName(groupId, objectId);
		this.objType = parentMap.getObjectType(groupId, objectId);

		this.objectBounds = new Rectangle(xPos, yPos, width, height);

		try {
			String objImageString = getProperty(WorldConstants.OBJPROP_IMAGE, null);
			objImage = (objImageString != null ? new Image(objImageString) : null);

			String objSoundString = getProperty(WorldConstants.OBJPROP_SOUND, null);
			objSound = (objSoundString != null ? new Sound(objSoundString) : null);

			if (highlightImage == null)
				highlightImage = new Image(WorldConstants.HIGHLIGHT_IMAGE);
		} catch (SlickException e) {
			e.printStackTrace();
		}

		String objAttractor = getProperty(WorldConstants.OBJPROP_ATTRACTOR, "false");
		if ("true".equals(objAttractor))
			isAttractor = true;

	}

	public TiledMap getParentMap() {
		return parentMap;
	}

	public void setParentMap(TiledMap parentMap) {
		this.parentMap = parentMap;
	}

	public int getGroupId() {
		return groupId;
	}

	public int getObjectId() {
		return objectId;
	}

	public Image getObjImage() {
		return objImage;
	}

	public void setObjImage(Image objImage) {
		this.objImage = objImage;
	}

	@Override
	public float getX() {
		return xPos;
	}

	public void setX(float newPos) {
		xPos = newPos;
		objectBounds.setX(newPos);
	}

	@Override
	public float getY() {
		return yPos;
	}

	@Override
	public Vector2f getPosn() {
		return new Vector2f(xPos, yPos);
	}

	public void setY(float newPos) {
		yPos = newPos;
		objectBounds.setY(newPos);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float newWidth) {
		width = newWidth;
		objectBounds.setWidth(newWidth);
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float newHeight) {
		height = newHeight;
		objectBounds.setHeight(newHeight);
	}

	public String getObjName() {
		return objName;
	}

	public String getObjType() {
		return objType;
	}

	public void setRemoved(boolean removed) {
		removedFromMap = removed;
	}

	public boolean isRemoved() {
		return removedFromMap;
	}

	public Sound getSound() {
		return objSound;
	}

	public boolean hasSoundPlayed() {
		return removedSoundPlayed;
	}

	public void setRemovedSoundPlayed(boolean hasPlayed) {
		removedSoundPlayed = hasPlayed;
	}

	public String getProperty(String property, String def) {
		return parentMap.getObjectProperty(groupId, objectId, property, def);
	}

	@Override
	public boolean intersects(Collidable other) {
		/*if (other.getBounds().intersects(this.getBounds())) {
			System.out.println("Intersect: a:" + this.getBounds().getX() + ","
					+ this.getBounds().getY() + ";" + this.getBounds().getWidth() + ","
					+ this.getBounds().getHeight() + " b:" + other.getBounds().getX() + ","
					+ other.getBounds().getY() + ";" + other.getBounds().getWidth() + ","
					+ other.getBounds().getHeight());
		}*/
		return other.getBounds().intersects(this.getBounds());
	}

	@Override
	public Rectangle getBounds() {
		return objectBounds;
	}

	public void setIsAttractor(boolean isAttractor) {
		this.isAttractor = isAttractor;
	}

	public boolean isAttractor() {
		return isAttractor;
	}

	public boolean wasHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean isHighlighted) {
		highlighted = isHighlighted;
	}
}
