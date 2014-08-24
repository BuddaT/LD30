package net.buddat.ludumdare.ld30.world;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.Layer;
import org.newdawn.slick.tiled.TiledMap;

public class WorldMap extends TiledMap {
	private static final String COLLISION_PROPERTY = "collide";

	private boolean renderCollisionLayer = false;

	private int collisionLayerId = 0;

	private final ArrayList<ArrayList<WorldObject>> objectList;

	public WorldMap(String ref) throws SlickException {
		super(ref);

		for (int i = 0; i < layers.size(); i++) {
			Layer l = (Layer) layers.get(i);
			if (l.name.equals(WorldConstants.COLLISION_LAYER_NAME))
				collisionLayerId = i;
		}

		objectList = new ArrayList<ArrayList<WorldObject>>();

		for (int i = 0; i < getObjectGroupCount(); i++) {
			objectList.add(new ArrayList<WorldObject>());

			if (i == WorldConstants.OBJGROUP_TEXT) {
				for (int j = 0; j < getObjectCount(i); j++) {
					objectList.get(i).add(new TextObject(this, i, j));
				}
			} else if (i == WorldConstants.OBJGROUP_TRIGGER) {
				for (int j = 0; j < getObjectCount(i); j++) {
					objectList.get(i).add(new TriggerObject(this, i, j));
				}
			} else {
				for (int j = 0; j < getObjectCount(i); j++) {
					objectList.get(i).add(new WorldObject(this, i, j));
				}
			}
		}
	}

	public void renderBelow(int x, int y, int startX, int startY, int width, int height) {
		for (int i = 0; i < WorldConstants.LAYERS_BELOW; i++) {
			if (i == collisionLayerId && !renderCollisionLayer)
				continue;

			renderLayer(i, x, y, startX, startY, width, height);
		}
	}

	public void renderAbove(int x, int y, int startX, int startY, int width, int height) {
		for (int i = WorldConstants.LAYERS_BELOW; i < layers.size(); i++) {
			if (i == collisionLayerId && !renderCollisionLayer)
				continue;

			renderLayer(i, x, y, startX, startY, width, height);
		}
	}

	public void renderLayer(int layer, int x, int y, int startX, int startY, int width, int height) {
		Layer l = (Layer) layers.get(layer);

		for (int i = 0; i < height; i++)
			l.render(x, y, startX, startY, width, i, false, tileWidth, tileHeight);
	}

	public boolean doRenderCollisionLayer() {
		return renderCollisionLayer;
	}

	public void setRenderCollisionLayer(boolean doRender) {
		renderCollisionLayer = doRender;
	}

	public int getCollisionLayerId() {
		return collisionLayerId;
	}

	public ArrayList<WorldObject> getObjectList(int objectLayer) {
		return objectList.get(objectLayer);
	}

	public WorldObject getObjectByName(String name) {
		for (ArrayList<WorldObject> objList : objectList) {
			for (WorldObject obj : objList) {
				if (obj.getObjName().equals(name))
					return obj;
			}
		}

		return null;
	}

	public boolean getTilePropertyAsBoolean(int tileId, String propertyName, boolean defaultValue) {
		String value = super.getTileProperty(tileId, propertyName, defaultValue ? "true" : "false");
		if ("true".equals(value)) {
			return true;
		} else if ("false".equals(value)) {
			return false;
		} else if (value != null) {
			System.err.println("Non-boolean value found attempting to extract boolean property "
					+ propertyName + ": " + value);
		}
		return defaultValue;
	}

	public boolean isCollideable(int x, int y) {
		int tileId = getTileId(x, y, collisionLayerId);
		return getTilePropertyAsBoolean(tileId, COLLISION_PROPERTY, false);
	}
}
