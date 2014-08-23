package net.buddat.ludumdare.ld30;

import net.buddat.ludumdare.ld30.controls.Controller;
import net.buddat.ludumdare.ld30.world.WorldManager;
import net.buddat.ludumdare.ld30.world.player.Direction;
import net.buddat.ludumdare.ld30.world.player.Player;
import net.buddat.ludumdare.ld30.world.player.PlayerRenderer;

import org.newdawn.slick.*;

public class Game extends BasicGame {

	private static AppGameContainer gameContainer;
	private static float DEFAULT_SPEED = 0.5f;
	private static float PLAYER_X = 5.0f;
	private static float PLAYER_Y = 1.0f;

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
		worldManager.renderMap(g, player.getX(), player.getY());
		playerRenderer.render();
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		worldManager = new WorldManager();
		player = new Player(PLAYER_X, PLAYER_Y, Direction.DOWN, Direction.LEFT, Direction.LEFT, DEFAULT_SPEED);
		playerRenderer = new PlayerRenderer(player);
		controller = new Controller(player);
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
