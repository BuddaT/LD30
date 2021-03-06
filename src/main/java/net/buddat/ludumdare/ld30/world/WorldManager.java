package net.buddat.ludumdare.ld30.world;

import java.util.ArrayList;
import java.util.HashMap;

import net.buddat.ludumdare.ld30.Constants;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class WorldManager {

	private final HashMap<String, World> allWorlds;

	private String currentMap;

	private boolean needsReset = false;

	public WorldManager() throws SlickException {
		allWorlds = new HashMap<String, World>();
		allWorlds.put("IntroMap", new World("tilesets/introMap.tmx"));
		allWorlds.put("IntroMap2", new World("tilesets/introMap2.tmx"));
		allWorlds.put("IntroMap3", new World("tilesets/introMap3.tmx"));
		allWorlds.put("IntroMap4", new World("tilesets/introMap4.tmx"));
		allWorlds.put("FirstMap", new World("tilesets/testMap.tmx"));
		allWorlds.put("WinMap", new World("tilesets/winMap.tmx"));
		currentMap = "IntroMap";
	}

	public void renderMapBelow(Graphics g, float playerX, float playerY) {
		WorldMap world = getCurrentWorld().getWorldMap();
		world.renderBelow(-Constants.TILE_WIDTH - (int) (playerX % 1 * Constants.TILE_WIDTH)
				+ Constants.TILE_WIDTH / 2, -Constants.TILE_HEIGHT
				- (int) (playerY % 1 * Constants.TILE_HEIGHT) + Constants.TILE_HEIGHT / 2,
				(int) (Math.floor(playerX) - (Constants.TILES_DRAWN_WIDTH / 2)),
				(int) (Math.floor(playerY) - (Constants.TILES_DRAWN_HEIGHT / 2)),
				Constants.TILES_DRAWN_WIDTH, Constants.TILES_DRAWN_HEIGHT);

	}

	public void renderMapAbove(Graphics g, float playerX, float playerY) {
		WorldMap world = getCurrentWorld().getWorldMap();
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

	public void renderExitLayers(Graphics g, float playerX, float playerY) {
		WorldMap world = getCurrentWorld().getWorldMap();
		world.renderExitLayers(-Constants.TILE_WIDTH - (int) (playerX % 1 * Constants.TILE_WIDTH)
				+ Constants.TILE_WIDTH / 2, -Constants.TILE_HEIGHT
				- (int) (playerY % 1 * Constants.TILE_HEIGHT) + Constants.TILE_HEIGHT / 2,
				(int) (Math.floor(playerX) - (Constants.TILES_DRAWN_WIDTH / 2)),
				(int) (Math.floor(playerY) - (Constants.TILES_DRAWN_HEIGHT / 2)),
				Constants.TILES_DRAWN_WIDTH, Constants.TILES_DRAWN_HEIGHT);
	}

	public void renderObjectsAbove(Graphics g, float playerX, float playerY) {
		World world = getCurrentWorld();

		for (WorldObject obj : world.getObjectList(WorldConstants.OBJGROUP_TRIGGER)) {
			if (obj.isRemoved())
				continue;

			int rX = Constants.GAME_WIDTH / 2 - (int) (playerX * Constants.TILE_WIDTH);
			int rY = Constants.GAME_HEIGHT / 2 - (int) (playerY * Constants.TILE_HEIGHT);
			rX += (int) (obj.getX() * Constants.TILE_WIDTH);
			rY += (int) (obj.getY() * Constants.TILE_HEIGHT);

			obj.getObjImage().draw(rX, rY);
		}

		for (WorldObject obj : world.getObjectList(WorldConstants.OBJGROUP_INTERACTIBLE)) {
			if (obj.isRemoved())
				continue;

			int rX = Constants.GAME_WIDTH / 2 - (int) (playerX * Constants.TILE_WIDTH);
			int rY = Constants.GAME_HEIGHT / 2 - (int) (playerY * Constants.TILE_HEIGHT);
			rX += (int) (obj.getX() * Constants.TILE_WIDTH);
			rY += (int) (obj.getY() * Constants.TILE_HEIGHT);

			obj.getObjImage().draw(rX, rY);
		}
	}

	public boolean renderObjectHighlight(Graphics g, float playerX, float playerY,
			Rectangle playerBounds) {
		if (WorldObject.highlightImage == null)
			return false;

		boolean toReturn = false;
		for (WorldObject obj : getInteractibleObjects()) {
			if (obj.isRemoved())
				continue;

			if (playerBounds.intersects(obj.getBounds())) {
				float size = (obj.getWidth() > obj.getHeight() ? obj.getHeight() : obj.getWidth());
				float x = (obj.getWidth() > obj.getHeight() ? obj.getX() + obj.getWidth() / 2
						- size / 2 : obj.getX());
				float y = (obj.getHeight() > obj.getWidth() ? obj.getY() + obj.getHeight() / 2
						- size / 2 : obj.getY());

				int rX = Constants.GAME_WIDTH / 2 - (int) (playerX * Constants.TILE_WIDTH);
				int rY = Constants.GAME_HEIGHT / 2 - (int) (playerY * Constants.TILE_HEIGHT);
				rX += (int) (x * Constants.TILE_WIDTH);
				rY += (int) (y * Constants.TILE_HEIGHT);
				WorldObject.highlightImage.draw(rX, rY, size * Constants.TILE_WIDTH, size
						* Constants.TILE_HEIGHT);

				if (!obj.wasHighlighted()) {
					toReturn = true;
					obj.setHighlighted(true);
				}
			} else {
				obj.setHighlighted(false);
			}
		}

		return toReturn;
	}

	public World getCurrentWorld() {
		return allWorlds.get(currentMap);
	}

	public void changeMap(String newMap) {
		currentMap = newMap;
	}

	public ArrayList<WorldObject> getInteractibleObjects() {
		return getCurrentWorld().getObjectList(WorldConstants.OBJGROUP_INTERACTIBLE);
	}

	public String getCurrentMap() {
		return currentMap;
	}

	public boolean needsReset() {
		return needsReset;
	}

	public void setNeedsReset(boolean needsReset) {
		this.needsReset = needsReset;
	}

	public void reset() {
		getCurrentWorld().reset();
		needsReset = false;
	}
}
