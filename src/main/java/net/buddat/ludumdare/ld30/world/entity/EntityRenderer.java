package net.buddat.ludumdare.ld30.world.entity;

import net.buddat.ludumdare.ld30.Constants;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Renders an entity.
 */
public class EntityRenderer {

	private final Entity entity;
	private final Image entityIcon;
	private final Image shadowIcon;
	private final int baseImageXOffset;
	private final int baseImageYOffset;

	private long lastAnimationRenderTime;

	public EntityRenderer(GameContainer gc, Entity entity, String iconPath, String shadowPath, int baseImageXOffset, int baseImageYOffset)
			throws SlickException {
		this.entity = entity;
		entityIcon = loadImage(iconPath);
		shadowIcon = loadImage(shadowPath);

		lastAnimationRenderTime = gc.getTime();
		this.baseImageXOffset = baseImageXOffset;
		this.baseImageYOffset = baseImageYOffset;
	}

	private Image loadImage(String path) throws SlickException {
		try {
			return new Image(path);
		} catch (SlickException e) {
			System.out.println("Error loading entity image " + path);
			throw e;
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public void render(GameContainer gc, float playerX, float playerY) {
		float xOffset = (entity.getX() - playerX) * Constants.TILE_WIDTH;
		float yOffset = (entity.getY() - playerY) * Constants.TILE_HEIGHT;
		int imageXOffset = calcImageXOffset(gc.getTime());
		int imageYOffset = calcImageYOffset();
		drawImage(shadowIcon, xOffset, yOffset, imageXOffset, imageYOffset);
		drawImage(entityIcon, xOffset, yOffset, imageXOffset, imageYOffset);
	}

	protected int calcImageXOffset(long newTime) {
		return baseImageXOffset;
	}

	protected int calcImageYOffset() {
		return baseImageYOffset;
	}

	protected void drawImage(Image image, float xOffset, float yOffset, int imageXOffset, int imageYOffset) {
		image.draw(Constants.PLR_DRAWN_X + xOffset, Constants.PLR_DRAWN_Y + yOffset,
				Constants.PLR_DRAWN_X + xOffset + entity.getWidth(), Constants.PLR_DRAWN_Y + yOffset + entity.getHeight(),
				imageXOffset, imageYOffset, imageXOffset + entity.getWidth(), imageYOffset + entity.getHeight());
	}
}
