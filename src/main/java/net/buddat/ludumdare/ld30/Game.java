package net.buddat.ludumdare.ld30;

import net.buddat.ludumdare.ld30.world.WorldManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

	private static AppGameContainer gameContainer;
	private WorldManager worldManager;

	public Game(String title) {
		super(title);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		// Feed playerX and Y to worldManager for rendering map in proper
		// position.
		worldManager.renderMap(g, 5.35f, 4.12f);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		worldManager = new WorldManager();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {

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
