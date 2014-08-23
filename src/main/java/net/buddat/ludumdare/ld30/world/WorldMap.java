package net.buddat.ludumdare.ld30.world;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.Layer;
import org.newdawn.slick.tiled.TiledMap;

public class WorldMap extends TiledMap {

	private boolean renderCollisionLayer = false;

	private int collisionLayerId = 0;

	public WorldMap(String ref) throws SlickException {
		super(ref);

		for (int i = 0; i < layers.size(); i++) {
			Layer l = (Layer) layers.get(i);
			if (l.name.equals(WorldConstants.COLLISION_LAYER_NAME))
				collisionLayerId = i;
		}
	}

	public void renderAll(int x, int y, int startX, int startY, int width, int height) {
		for (int i = 0; i < layers.size(); i++) {
			if (i == collisionLayerId && !renderCollisionLayer)
				continue;

			renderLayer(i, x, y, startX, startY, width, height);
		}
	}

	public void renderLayer(int layer, int x, int y, int startX, int startY, int width, int height) {
		Layer l = (Layer) layers.get(layer);

		for (int i = 0; i < height; i++)
			l.render(x, y, startX, startY, width, i, false, tileWidth, tileHeight);
	}

	public boolean doRenderCollisionLayer() {
		return renderCollisionLayer;
	}

	public void setRenderCollisionLayer(boolean doRender) {
		renderCollisionLayer = doRender;
	}

}
