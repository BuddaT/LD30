package net.buddat.ludumdare.ld30;

public class Constants {

	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 608;
	public static final boolean FULLSCREEN = false;
	public static final int TARGET_FPS = 60;

	public static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;

	public static final int TILES_DRAWN_WIDTH = GAME_WIDTH / TILE_WIDTH + 2;
	public static final int TILES_DRAWN_HEIGHT = GAME_HEIGHT / TILE_HEIGHT + 2;

	public static final int PLR_DRAWN_X = GAME_WIDTH / 2 - TILE_WIDTH / 2;
	public static final int PLR_DRAWN_Y = GAME_HEIGHT / 2 - TILE_WIDTH / 2;

	public static final boolean DEV_DRAW_GRID = true;
	public static final boolean DEV_SHOW_FPS = true;

}
