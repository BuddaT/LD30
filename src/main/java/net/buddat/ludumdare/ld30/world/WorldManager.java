package net.buddat.ludumdare.ld30.world;

import net.buddat.ludumdare.ld30.Player;
import net.buddat.ludumdare.ld30.PlayerRenderer;
import org.newdawn.slick.tiled.TiledMap;

import java.util.HashMap;

public class WorldManager {

	private final HashMap<String, World> allWorlds;

	public WorldManager() {
		allWorlds = new HashMap<String, World>();
		allWorlds.put("TestMap", new World("maps/testMap.tmx"));

		System.out.println(allWorlds.get("TestMap").getWorldMap()
				.getLayerCount());
	}

	public void renderMap() {
		TiledMap world = allWorlds.get("TestMap").getWorldMap();
		world.render(0, 0);

		PlayerRenderer playerRenderer = new PlayerRenderer(new Player(allWorlds.get("TestMap"), 300, 300));
	}

}