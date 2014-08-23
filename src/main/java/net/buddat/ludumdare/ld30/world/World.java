package net.buddat.ludumdare.ld30.world;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

public class World {

	private WorldMap worldMap;

	private ArrayList<ArrayList<WorldObject>> objectList;

	public World(String mapLocation) {
		try {
			worldMap = new WorldMap(mapLocation);

			objectList = new ArrayList<ArrayList<WorldObject>>();

			for (int i = 0; i < worldMap.getObjectGroupCount(); i++) {
				objectList.add(new ArrayList<WorldObject>());
				for (int j = 0; j < worldMap.getObjectCount(i); j++) {
					objectList.get(i).add(new WorldObject(worldMap, i, j));
				}
			}
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
		return objectList.get(objectLayer);
	}

}
