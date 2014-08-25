package net.buddat.ludumdare.ld30;

import java.util.ArrayList;

import net.buddat.ludumdare.ld30.controls.Controller;
import net.buddat.ludumdare.ld30.world.TextObject;
import net.buddat.ludumdare.ld30.world.TriggerObject;
import net.buddat.ludumdare.ld30.world.WorldConstants;
import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.WorldObject;
import net.buddat.ludumdare.ld30.world.entity.ClawedBiter;
import net.buddat.ludumdare.ld30.world.entity.EntityRenderer;
import net.buddat.ludumdare.ld30.world.entity.EntityType;
import net.buddat.ludumdare.ld30.world.entity.FireFace;
import net.buddat.ludumdare.ld30.world.entity.HornDemon;
import net.buddat.ludumdare.ld30.world.entity.Movement;
import net.buddat.ludumdare.ld30.world.entity.SkullFace;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;
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
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Game extends BasicGame {

	private static AppGameContainer gameContainer;
	private static float DEFAULT_SPEED = 0.15f;
	private static float PLAYER_X = 50.0f;
	private static float PLAYER_Y = 30.0f;

	public static boolean exitOverride = false;

	private WorldManager worldManager;
	private SoundManager soundManager;
	private EntityManager entityManager;
	private Player player;
	private PlayerRenderer playerRenderer;
	private Controller controller;

	private UnicodeFont textFont;
	private Image textBackground;

	public Game(String title) {
		super(title);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		worldManager.renderMapBelow(g, player.getX(), player.getY());
		entityManager.renderEntitiesBelow(gc, player.getX(), player.getY());

		playerRenderer.render(gc);
		soundManager.playerMoving(player.isMoving());

		entityManager.renderEntitiesAbove(gc, player.getX(), player.getY());
		worldManager.renderObjectsAbove(g, player.getX(), player.getY());
		if (player.getHeldObject() == null) {
			boolean justHighlighted = worldManager.renderObjectHighlight(g, player.getX(),
					player.getY(),
					player.getPickingBounds());

			if (justHighlighted)
				soundManager.playHighlight();
		}
		worldManager.renderMapAbove(g, player.getX(), player.getY());

		if (worldManager.getCurrentWorld().isExitActive())
			worldManager.renderExitLayers(g, player.getX(), player.getY());

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
				if (obj.isRemoved())
					continue;

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
				Utilities.renderText(textFont, text.getProperty("text", ""), 50, 455,
						Utilities.ALIGN_LEFT, Color.white, true, 700);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc) throws SlickException {
		worldManager = new WorldManager();
		soundManager = new SoundManager();

		Point starting = worldManager.getCurrentWorld().getStartingPosition();
		player = new Player(starting.getX(), starting.getY(), true, false, new Movement(DEFAULT_SPEED, CardinalDirection.DOWN));
		playerRenderer = new PlayerRenderer(gc, player);

		entityManager = new EntityManager(gc.getTime(), worldManager, player);

		controller = new Controller(worldManager, player);

		try {
			textFont = new UnicodeFont(/* "CRYSRG__.TTF" */"pointfree.ttf", /* 24 */18, true, false);
			textFont.addAsciiGlyphs();
			textFont.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
			textFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		textBackground = new Image("textBg.png");
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		controller.handleInput(gc.getInput());

		if (worldManager.needsReset()) {
			resetWorld(gc);
		}

		if (player.getHeldObject() != null)
			if (player.getHeldObject().isRemoved())
				player.setHeldObject(null);

		for (WorldObject obj : worldManager.getCurrentWorld().getObjectList(
				WorldConstants.OBJGROUP_TRIGGER)) {
			if (obj.isRemoved()) {
				if (!obj.hasSoundPlayed()) {
					soundManager.playSoundOnce(obj.getSound());
					obj.setRemovedSoundPlayed(true);
				}
				continue;
			}
			((TriggerObject) (obj)).update(delta);
		}
		entityManager.updateEntities(delta);

		if (worldManager.getCurrentWorld().isExitActive()) {
			soundManager.playExitMusic();

			if (player.intersects(worldManager.getCurrentWorld().getExitObject())) {
				worldManager.changeMap(worldManager.getCurrentWorld().getExitObject()
						.getProperty(WorldConstants.EXITOBJ_NEXTMAP, "IntroMap"));
				resetWorld(gc);
			}
		}
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

	private void resetWorld(GameContainer gc) throws SlickException {
		exitOverride = false;

		worldManager.reset();
		soundManager.reset();
		// TODO: Refactor out need for gc.getTime() and use deltas for animation
		entityManager.reset(gc.getTime());

		Point starting = worldManager.getCurrentWorld().getStartingPosition();
		player.reset(starting.getX(), starting.getY(), true, false);
	}

	private void addTestEntities(GameContainer gc) throws SlickException {
		Movement mobMovement = new Movement(ClawedBiter.DEFAULT_SPEED, CardinalDirection.LEFT);
		EntityRenderer entityRenderer = ClawedBiter.buildRenderer(gc.getTime(), new ClawedBiter(40, 40, mobMovement));
		entityManager.addEntity(entityRenderer);
	}
}
