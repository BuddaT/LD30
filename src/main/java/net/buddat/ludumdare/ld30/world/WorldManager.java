package net.buddat.ludumdare.ld30.world;

import java.util.HashMap;

import net.buddat.ludumdare.ld30.world.player.Direction;
import net.buddat.ludumdare.ld30.world.player.Player;
import net.buddat.ludumdare.ld30.world.player.PlayerRenderer;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class WorldManager {

	private final HashMap<String, World> allWorlds;
	private final PlayerRenderer playerRenderer;

	public WorldManager() throws SlickException {
		allWorlds = new HashMap<String, World>();
		allWorlds.put("TestMap", new World("maps/testMap.tmx"));
		playerRenderer = new PlayerRenderer(
				new Player(allWorlds.get("TestMap"), 300, 300, Direction.RIGHT));
	}

	public void renderMap() throws SlickException {
		TiledMap world = allWorlds.get("TestMap").getWorldMap();
		world.render(0, 0);

		playerRenderer.render();
	}
}