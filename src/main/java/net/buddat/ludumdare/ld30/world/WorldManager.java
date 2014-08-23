package net.buddat.ludumdare.ld30.world;

import java.util.HashMap;

public class WorldManager {

	private final HashMap<String, World> allWorlds;

	public WorldManager() {
		allWorlds = new HashMap<String, World>();
		allWorlds.put("TestMap", new World("/data/maps/testMap.tmx"));

		System.out.println(allWorlds.get("TestMap").getWorldMap()
				.getLayerCount());
	}

	public void renderMap() {
		allWorlds.get("TestMap").getWorldMap().render(0, 0);
	}

}
