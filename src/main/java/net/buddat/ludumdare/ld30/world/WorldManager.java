package net.buddat.ludumdare.ld30.world;

import java.util.HashMap;

import net.buddat.ludumdare.ld30.Constants;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class WorldManager {

	private final HashMap<String, World> allWorlds;

	public WorldManager() throws SlickException {
		allWorlds = new HashMap<String, World>();
		allWorlds.put("TestMap", new World("maps/testMap.tmx"));
	}

	public void renderMap(Graphics g, float playerX, float playerY) {
		WorldMap world = allWorlds.get("TestMap").getWorldMap();
		world.renderAll(-world.getTileWidth() - (int) (playerX % 1 * Constants.TILE_WIDTH)
				+ Constants.TILE_WIDTH / 2, -world.getTileHeight()
				- (int) (playerY % 1 * Constants.TILE_HEIGHT) + Constants.TILE_HEIGHT / 2,
				(int) (Math.floor(playerX) - (Constants.TILES_DRAWN_WIDTH / 2)),
				(int) (Math.floor(playerY) - (Constants.TILES_DRAWN_HEIGHT / 2)),
				Constants.TILES_DRAWN_WIDTH, Constants.TILES_DRAWN_HEIGHT);

		g.setColor(Color.black);
		if (Constants.DEV_DRAW_GRID) {
			for (int i = -1; i < Constants.GAME_WIDTH / world.getTileWidth() + 2; i++) {
				for (int j = -1; j < Constants.GAME_HEIGHT / world.getTileHeight() + 2; j++) {
					g.drawRect(-world.getTileWidth() - (int) (playerX % 1 * Constants.TILE_WIDTH)
							+ Constants.TILE_WIDTH / 2 + i * world.getTileWidth(),
							-world.getTileHeight() - (int) (playerY % 1 * Constants.TILE_HEIGHT)
									+ Constants.TILE_HEIGHT / 2 + j * world.getTileHeight(),
							world.getTileWidth(), world.getTileHeight());
				}
			}
		}
	}

	public WorldMap getCurrentWorld() {
		return allWorlds.get("TestMap").getWorldMap();
	}
}
