package net.buddat.ludumdare.ld30.world;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class WorldObject {
	
	private TiledMap parentMap;
	private final int groupId, objectId;

	private float xPos;
	private float yPos;
	private float width;
	private float height;

	private final String objName;
	private final String objType;

	private Image objImage;

	public WorldObject(TiledMap parentMap, int groupId, int objectId) {
		this.parentMap = parentMap;
		this.groupId = groupId;
		this.objectId = objectId;

		this.xPos = parentMap.getObjectX(groupId, objectId)
				/ (float) parentMap.getTileWidth();
		this.yPos = parentMap.getObjectY(groupId, objectId)
				/ (float) parentMap.getTileHeight();
		this.width = parentMap.getObjectWidth(groupId, objectId)
				/ (float) parentMap.getTileWidth();
		this.height = parentMap.getObjectHeight(groupId, objectId)
				/ (float) parentMap.getTileHeight();

		this.objName = parentMap.getObjectName(groupId, objectId);
		this.objType = parentMap.getObjectType(groupId, objectId);

		try {
			this.objImage = new Image(parentMap.getObjectImage(groupId,
					objectId));
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
	}

	public float getyPos() {
		return yPos;
	}

	public void setyPos(float newPos) {
		yPos = newPos;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float newWidth) {
		width = newWidth;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float newHeight) {
		height = newHeight;
	}

	public String getObjName() {
		return objName;
	}

	public String getObjType() {
		return objType;
	}

}
