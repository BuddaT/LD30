package net.buddat.ludumdare.ld30;

import net.buddat.ludumdare.ld30.controls.Controller;
import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.player.CardinalDirection;
import net.buddat.ludumdare.ld30.world.player.Player;
import net.buddat.ludumdare.ld30.world.player.PlayerRenderer;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

	private static AppGameContainer gameContainer;
	private static float DEFAULT_SPEED = 0.2f;
	private static float PLAYER_X = 50.0f;
	private static float PLAYER_Y = 30.0f;

	private WorldManager worldManager;
	private Player player;
	private PlayerRenderer playerRenderer;
	private Controller controller;

	public Game(String title) {
		super(title);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		// Feed playerX and Y to worldManager for rendering map in proper
		// position.
		worldManager.renderMapBelow(g, player.getX(), player.getY());
		playerRenderer.render();
		worldManager.renderMapAbove(g, player.getX(), player.getY());
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		worldManager = new WorldManager();
		player = new Player(PLAYER_X, PLAYER_Y, true, false, DEFAULT_SPEED);
		playerRenderer = new PlayerRenderer(player);
		controller = new Controller(worldManager, player);
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
