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
		try {
			entityIcon = new Image("sprites/player.png");
			shadowIcon = new Image("sprites/player_shadow.png");
		} catch (SlickException e) {
			String errMsg = "Error loading player images";
			System.out.println(errMsg);
			throw e;
		}
		lastAnimationRenderTime = gc.getTime();
		this.baseImageXOffset = baseImageYOffset;
		this.baseImageYOffset = baseImageYOffset;
	}

	public Entity getEntity() {
		return entity;
	}

	public void render(GameContainer gc) {
		int xOffset = calcImageXOffset(gc.getTime());
		int yOffset = calcImageYOffset();
		drawImage(shadowIcon, xOffset, yOffset);
		drawImage(entityIcon, xOffset, yOffset);
	}

	protected int calcImageXOffset(long newTime) {
		return baseImageXOffset;
	}

	protected int calcImageYOffset() {
		return baseImageYOffset;
	}

	protected void drawImage(Image image, int xOffset, int yOffset) {
		image.draw(entity.getX(), entity.getY(),
				entity.getX() + entity.getWidth(), entity.getY() + entity.getHeight(),
				xOffset, yOffset, xOffset + entity.getWidth(), yOffset + entity.getHeight());
	}
}
