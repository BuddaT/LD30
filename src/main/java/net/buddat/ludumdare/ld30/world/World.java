package net.buddat.ludumdare.ld30.world;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class World {

	private TiledMap worldMap;

	public World(String mapLocation) {
		try {
			worldMap = new TiledMap(mapLocation);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public TiledMap getWorldMap() {
		return worldMap;
	}

	public void setWorldMap(TiledMap newMap) {
		worldMap = newMap;
	}

}
