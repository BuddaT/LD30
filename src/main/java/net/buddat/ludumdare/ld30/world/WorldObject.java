package net.buddat.ludumdare.ld30.world;

import net.buddat.ludumdare.ld30.Collidable;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

public class WorldObject implements Collidable {

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
			objImage = new Image(parentMap.getObjectProperty(groupId, objectId,
				WorldConstants.OBJPROP_IMAGE, null));
		} catch (SlickException e) {
			e.printStackTrace();
		}
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

	public float getxPos() {
		return xPos;
	}

	public void setxPos(float newPos) {
		xPos = newPos;
		objectBounds.setX(newPos);
	}

	public float getyPos() {
		return yPos;
	}

	public void setyPos(float newPos) {
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

	public Rectangle getBounds() {
		return objectBounds;
	}

}
