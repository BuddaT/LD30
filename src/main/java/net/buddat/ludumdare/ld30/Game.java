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
		worldManager.renderMap();
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
			gameContainer.setDisplayMode(800, 600, false);
			gameContainer.setShowFPS(true);
			gameContainer.setTargetFrameRate(60);
			gameContainer.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
