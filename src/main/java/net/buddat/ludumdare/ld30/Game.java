package net.buddat.ludumdare.ld30;

import net.buddat.ludumdare.kickergame.Constants;
import net.buddat.ludumdare.kickergame.client.gfx.MenuScreen;
import net.buddat.ludumdare.kickergame.client.gfx.PlayScreen;
import net.buddat.ludumdare.kickergame.client.gfx.Screen;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {
	
	private Screen currentScreen;
	private Screen menuScreen, playScreen;

	public Game(String title) {
		super(title);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		currentScreen.render(gc, g);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		menuScreen = new MenuScreen(this);
		menuScreen.init(gc);
		
		playScreen = new PlayScreen(this);
		playScreen.init(gc);
		
		currentScreen = menuScreen;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		currentScreen.update(gc, delta);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer gameContainer = new AppGameContainer(new Game(Constants.NAME));
			gameContainer.setDisplayMode(Constants.WIDTH, Constants.HEIGHT, Constants.FULLSCREEN);
			gameContainer.setShowFPS(Constants.SHOW_FPS);
			gameContainer.setTargetFrameRate(Constants.FPS);
			gameContainer.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void changeScreen(Screen newScreen) {
		currentScreen.dispose();
		currentScreen = newScreen;
	}
	
	public Screen getPlayScreen() {
		return playScreen;
	}
	
	public Screen getMenuScreen() {
		return menuScreen;
	}

}
