package net.buddat.ludumdare.ld30.world;

import org.newdawn.slick.SlickException;

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

}
