package net.buddat.ludumdare.ld30;

import java.util.ArrayList;

import net.buddat.ludumdare.ld30.controls.Controller;
import net.buddat.ludumdare.ld30.world.*;
import net.buddat.ludumdare.ld30.world.entity.*;
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

		entityManager = new EntityManager(worldManager, player);
		createEntities(gc, worldManager.getCurrentWorld()
				.getObjectList(WorldConstants.OBJGROUP_MOB));

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
			resetWorld();
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
		entityManager.updateEntities();

		if (worldManager.getCurrentWorld().isExitActive()) {
			soundManager.playExitMusic();

			if (player.intersects(worldManager.getCurrentWorld().getExitObject())) {
				worldManager.changeMap(worldManager.getCurrentWorld().getExitObject()
						.getProperty(WorldConstants.TELEPORT_EXIT, "FirstMap"));
				resetWorld();
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

	private void resetWorld() {
		exitOverride = false;

		worldManager.reset();
		soundManager.reset();
		entityManager.reset();

		Point starting = worldManager.getCurrentWorld().getStartingPosition();
		player.reset(starting.getX(), starting.getY(), true, false);
	}

	private void addTestEntities(GameContainer gc) throws SlickException {
		Movement mobMovement = new Movement(ClawedBiter.DEFAULT_SPEED, CardinalDirection.LEFT);
		EntityRenderer entityRenderer = ClawedBiter.buildRenderer(gc, new ClawedBiter(40, 40, mobMovement));
		entityManager.addEntity(entityRenderer);
	}

	private void createEntities(GameContainer gc, ArrayList<WorldObject> mobList)
			throws SlickException {
		for (WorldObject mob : mobList) {
			int mobId = Integer.parseInt(mob.getProperty(WorldConstants.MOBPROP_ID, "0"));
			EntityType type = EntityType.forTypeId(mobId);
			if (type == null) {
				System.err.println("Unknown mob type: " + mobId);
				continue;
			}
			Movement mobMovement;
			EntityRenderer entityRenderer;
			switch(type) {
				case HORN_DEMON:
					mobMovement = new Movement(HornDemon.DEFAULT_SPEED, CardinalDirection.LEFT);
					entityRenderer = HornDemon.buildRenderer(gc, new HornDemon(mob.getX(), mob.getY(), mobMovement));
					break;
				case SKULL_FACE:
					mobMovement = new Movement(SkullFace.DEFAULT_SPEED, CardinalDirection.LEFT);
					entityRenderer = SkullFace.buildRenderer(gc, new SkullFace(mob.getX(), mob.getY(), mobMovement));
					break;
				case FIRE_FACE:
					mobMovement = new Movement(FireFace.DEFAULT_SPEED, CardinalDirection.LEFT);
					entityRenderer = FireFace.buildRenderer(gc, new FireFace(mob.getX(), mob.getY(), mobMovement));
					break;
				case CLAWED_BITER:
					mobMovement = new Movement(ClawedBiter.DEFAULT_SPEED, CardinalDirection.LEFT);
					entityRenderer = ClawedBiter.buildRenderer(gc,
							new ClawedBiter(mob.getX(), mob.getY(), mobMovement));
					entityManager.addEntity(entityRenderer);
					break;
				default:
					System.err.println("No mob creation specified for mob " + type);
					continue;
			}
			entityManager.addEntity(entityRenderer);
		}
	}
}
