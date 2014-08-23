package net.buddat.ludumdare.ld30.world;

import java.util.HashMap;

import net.buddat.ludumdare.ld30.Constants;
import net.buddat.ludumdare.ld30.world.player.Direction;
import net.buddat.ludumdare.ld30.world.player.Player;
import net.buddat.ludumdare.ld30.world.player.PlayerRenderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class WorldManager {

	private final HashMap<String, World> allWorlds;
	private final PlayerRenderer playerRenderer;

	public WorldManager() throws SlickException {
		allWorlds = new HashMap<String, World>();
		allWorlds.put("TestMap", new World("maps/testMap.tmx"));
		playerRenderer = new PlayerRenderer(new Player(allWorlds.get("TestMap"), 300, 300,
				Direction.RIGHT));
	}

	public void renderMap(Graphics g, float playerX, float playerY) {
		TiledMap world = allWorlds.get("TestMap").getWorldMap();
		world.render(-world.getTileWidth() - (int) (playerX % 1 * Constants.TILE_WIDTH)
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

		playerRenderer.render();
	}
}
