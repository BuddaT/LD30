package net.buddat.ludumdare.ld30;

import net.buddat.ludumdare.ld30.controls.Controller;
import net.buddat.ludumdare.ld30.world.TextObject;
import net.buddat.ludumdare.ld30.world.WorldConstants;
import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.WorldObject;
import net.buddat.ludumdare.ld30.world.player.Player;
import net.buddat.ludumdare.ld30.world.player.PlayerRenderer;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;

public class Game extends BasicGame {

	private static AppGameContainer gameContainer;
	private static float DEFAULT_SPEED = 0.15f;
	private static float PLAYER_X = 50.0f;
	private static float PLAYER_Y = 30.0f;

	private WorldManager worldManager;
	private Player player;
	private PlayerRenderer playerRenderer;
	private Controller controller;

	private UnicodeFont textFont;
	private Image textBackground;

	public Game(String title) {
		super(title);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		worldManager.renderMapBelow(g, player.getX(), player.getY());

		playerRenderer.render(gc);

		worldManager.renderObjectsAbove(g, player.getX(), player.getY());
		worldManager.renderMapAbove(g, player.getX(), player.getY());

		if (Constants.DEV_DRAW_BOUNDS) {
			g.setColor(Color.red);
			int rX = Constants.GAME_WIDTH / 2 - (int) (player.getX() * Constants.TILE_WIDTH);
			int rY = Constants.GAME_HEIGHT / 2 - (int) (player.getY() * Constants.TILE_HEIGHT);
			Rectangle plrBounds = new Rectangle(rX + player.getBounds().getX()
					* Constants.TILE_WIDTH, rY + player.getBounds().getY() * Constants.TILE_HEIGHT,
					player.getBounds().getWidth() * Constants.TILE_WIDTH, player.getBounds()
							.getHeight()
							* Constants.TILE_HEIGHT);
			g.draw(plrBounds);

			g.setColor(Color.blue);
			Rectangle pickingBounds = new Rectangle(rX + player.getPickingBounds().getX()
					* Constants.TILE_WIDTH, rY + player.getPickingBounds().getY()
					* Constants.TILE_HEIGHT, player.getPickingBounds().getWidth()
					* Constants.TILE_WIDTH, player.getPickingBounds().getHeight()
					* Constants.TILE_HEIGHT);
			g.draw(pickingBounds);

			g.setColor(Color.green);
			for (WorldObject obj : worldManager.getInteractibleObjects()) {
				Rectangle objBounds = new Rectangle(rX + obj.getBounds().getX()
						* Constants.TILE_WIDTH,
						rY + obj.getBounds().getY() * Constants.TILE_HEIGHT, obj.getBounds()
								.getWidth() * Constants.TILE_WIDTH, obj.getBounds().getHeight()
								* Constants.TILE_HEIGHT);
				g.draw(objBounds);
			}
		}
		
		for (WorldObject obj : worldManager.getCurrentWorld().getObjectList(
				WorldConstants.OBJGROUP_TEXT)) {
			TextObject text = (TextObject) obj;
			if (text.isShowing()) {
				textBackground.draw(25, 440);
				Utilities.renderText(
						textFont,
						text.getParentMap().getObjectProperty(text.getGroupId(),
								text.getObjectId(), "text", ""), 50, 455, Utilities.ALIGN_LEFT,
						Color.white, true, 700);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc) throws SlickException {
		worldManager = new WorldManager();
		player = new Player(PLAYER_X, PLAYER_Y, true, false, DEFAULT_SPEED);
		playerRenderer = new PlayerRenderer(gc, player);
		controller = new Controller(worldManager, player);

		try {
			textFont = new UnicodeFont("CRYSRG__.TTF", 24, true, false);
			textFont.addAsciiGlyphs();
			textFont.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
			//textFont.getEffects().add(new OutlineEffect(1, java.awt.Color.BLACK));
			textFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		textBackground = new Image("textBg.png");
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		controller.handleInput(gc.getInput());
	}

	public static void main(String[] args) {
		try {
			gameContainer = new AppGameContainer(new Game("testing"));
			gameContainer.setDisplayMode(Constants.GAME_WIDTH, Constants.GAME_HEIGHT,
					Constants.FULLSCREEN);
			gameContainer.setShowFPS(Constants.DEV_SHOW_FPS);
			gameContainer.setTargetFrameRate(Constants.TARGET_FPS);
			gameContainer.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
