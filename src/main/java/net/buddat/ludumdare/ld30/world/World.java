package net.buddat.ludumdare.ld30.world;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class World {

	private WorldMap worldMap;

	public World(String mapLocation) {
		try {
			worldMap = new WorldMap(mapLocation);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public WorldMap getWorldMap() {
		return worldMap;
	}

	public void setWorldMap(WorldMap newMap) {
		worldMap = newMap;
	}

	public ArrayList<WorldObject> getObjectList(int objectLayer) {
		return worldMap.getObjectList(objectLayer);
	}

	public Point getStartingPosition() {
		WorldObject obj = worldMap.getObjectByName(WorldConstants.TELEPORT_ENTRY);
		
		if (obj == null)
			return null;
					
		return new Point(obj.getxPos() + obj.getWidth() / 2, obj.getyPos()
				+ obj.getHeight() / 2);

	}

	public WorldObject getExitObject() {
		return worldMap.getObjectByName(WorldConstants.TELEPORT_EXIT);
	}

	public boolean isExitActive() {
		for (WorldObject obj : getObjectList(WorldConstants.OBJGROUP_TRIGGER)) {
			if (!((TriggerObject) (obj)).isActivated())
				return false;
		}

		return true;
	}

}
