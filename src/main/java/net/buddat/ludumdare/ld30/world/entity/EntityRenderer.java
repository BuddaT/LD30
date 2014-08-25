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
	private final int imageLeftXOffset;
	private final int imageLeftYOffset;
	private final int imageRightXOffset;
	private final int imageRightYOffset;

	private long lastAnimationRenderTime;

	public EntityRenderer(GameContainer gc, Entity entity, String iconPath, String shadowPath, int imageLeftXOffset,
						  int imageLeftYOffset, int imageRightXOffset, int imageRightYOffset)
			throws SlickException {
		this(gc, entity, loadImage(iconPath), loadImage(shadowPath), imageLeftXOffset, imageLeftYOffset,
				imageRightXOffset, imageRightYOffset);
	}

	public EntityRenderer(GameContainer gc, Entity entity, Image entityIcon, Image shadowIcon, int imageLeftXOffset,
						  int imageLeftYOffset, int imageRightXOffset, int imageRightYOffset) throws  SlickException {
		this.entity = entity;
		this.entityIcon = entityIcon;
		this.shadowIcon = shadowIcon;
		lastAnimationRenderTime = gc.getTime();
		this.imageLeftXOffset = imageLeftXOffset;
		this.imageLeftYOffset = imageLeftYOffset;
		this.imageRightXOffset = imageRightXOffset;
		this.imageRightYOffset = imageRightYOffset;
	}

	private static Image loadImage(String path) throws SlickException {
		try {
			return new Image(path);
		} catch (SlickException e) {
			System.out.println("Error loading image " + path);
			throw e;
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public void render(GameContainer gc, float playerX, float playerY) {
		float xOffset = (entity.getX() - playerX) * Constants.TILE_WIDTH;
		float yOffset = (entity.getY() - playerY) * Constants.TILE_HEIGHT;
		int imageXOffset = calcImageXOffset(lastAnimationRenderTime, gc.getTime());
		int imageYOffset = calcImageYOffset();
		drawImage(shadowIcon, xOffset, yOffset, imageXOffset, imageYOffset);
		drawImage(entityIcon, xOffset, yOffset, imageXOffset, imageYOffset);
	}

	protected int calcImageXOffset(long lastAnimationRendered, long currentRenderTime) {
		if (CardinalDirection.LEFT.equals(getEntity().getFacingLeftRight())) {
			return getImageLeftXOffset();
		} else {
			return getImageRightXOffset();
		}
	}

	protected int calcImageYOffset() {
		if (CardinalDirection.LEFT.equals(getEntity().getFacingLeftRight())) {
			return getImageLeftYOffset();
		} else {
			return getImageRightYOffset();
		}
	}

	protected void setLastAnimationRenderTime(long time) {
		lastAnimationRenderTime = time;
	}

	protected void drawImage(Image image, float xOffset, float yOffset, int imageXOffset, int imageYOffset) {
		image.draw(Constants.PLR_DRAWN_X + xOffset, Constants.PLR_DRAWN_Y + yOffset,
				Constants.PLR_DRAWN_X + xOffset + entity.getWidth(), Constants.PLR_DRAWN_Y + yOffset + entity.getHeight(),
				imageXOffset, imageYOffset, imageXOffset + entity.getWidth(), imageYOffset + entity.getHeight());
	}

	protected int getImageLeftXOffset() {
		return imageLeftXOffset;
	}

	protected int getImageRightXOffset() {
		return imageRightXOffset;
	}

	protected int getImageLeftYOffset() {
		return imageLeftYOffset;
	}

	protected int getImageRightYOffset() {
		return imageRightYOffset;
	}
}
