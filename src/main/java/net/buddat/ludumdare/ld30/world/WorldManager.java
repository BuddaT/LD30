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

	public void renderMapBelow(Graphics g, float playerX, float playerY) {
		WorldMap world = allWorlds.get("TestMap").getWorldMap();
		world.renderBelow(-Constants.TILE_WIDTH - (int) (playerX % 1 * Constants.TILE_WIDTH)
				+ Constants.TILE_WIDTH / 2, -Constants.TILE_HEIGHT
				- (int) (playerY % 1 * Constants.TILE_HEIGHT) + Constants.TILE_HEIGHT / 2,
				(int) (Math.floor(playerX) - (Constants.TILES_DRAWN_WIDTH / 2)),
				(int) (Math.floor(playerY) - (Constants.TILES_DRAWN_HEIGHT / 2)),
				Constants.TILES_DRAWN_WIDTH, Constants.TILES_DRAWN_HEIGHT);

	}

	public void renderMapAbove(Graphics g, float playerX, float playerY) {
		WorldMap world = allWorlds.get("TestMap").getWorldMap();
		world.renderAbove(-Constants.TILE_WIDTH - (int) (playerX % 1 * Constants.TILE_WIDTH)
				+ Constants.TILE_WIDTH / 2, -Constants.TILE_HEIGHT
				- (int) (playerY % 1 * Constants.TILE_HEIGHT) + Constants.TILE_HEIGHT / 2,
				(int) (Math.floor(playerX) - (Constants.TILES_DRAWN_WIDTH / 2)),
				(int) (Math.floor(playerY) - (Constants.TILES_DRAWN_HEIGHT / 2)),
				Constants.TILES_DRAWN_WIDTH, Constants.TILES_DRAWN_HEIGHT);

		g.setColor(Color.black);
		if (Constants.DEV_DRAW_GRID) {
			for (int i = -1; i < Constants.GAME_WIDTH / Constants.TILE_WIDTH + 2; i++) {
				for (int j = -1; j < Constants.GAME_HEIGHT / Constants.TILE_HEIGHT + 2; j++) {
					g.drawRect(-Constants.TILE_WIDTH - (int) (playerX % 1 * Constants.TILE_WIDTH)
							+ Constants.TILE_WIDTH / 2 + i * Constants.TILE_WIDTH,
							-Constants.TILE_HEIGHT - (int) (playerY % 1 * Constants.TILE_HEIGHT)
									+ Constants.TILE_HEIGHT / 2 + j * Constants.TILE_HEIGHT,
							world.getTileWidth(), world.getTileHeight());
				}
			}
		}
	}

	public void renderObjectsAbove(Graphics g, float playerX, float playerY) {
		World world = allWorlds.get("TestMap");
		int rX = Constants.GAME_WIDTH / 2 - (int) (playerX * Constants.TILE_WIDTH);
		int rY = Constants.GAME_HEIGHT / 2 - (int) (playerY * Constants.TILE_HEIGHT);
		for (WorldObject obj : world.getObjectList(WorldConstants.OBJGROUP_INTERACTIBLE)) {
			rX += (int) (obj.getxPos() * Constants.TILE_WIDTH);
			rY += (int) (obj.getyPos() * Constants.TILE_HEIGHT);
			obj.getObjImage().draw(rX, rY);
		}
	}

	public WorldMap getCurrentWorld() {
		return allWorlds.get("TestMap").getWorldMap();
	}
}
