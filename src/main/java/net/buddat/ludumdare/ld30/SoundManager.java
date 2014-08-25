package net.buddat.ludumdare.ld30;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundManager {

	private Sound highlightSound;
	private Sound playerMovingSound;
	private Sound backgroundMusic, exitMusic;

	public SoundManager() {
		try {
			highlightSound = new Sound("sounds/highlight.ogg");
			playerMovingSound = new Sound("sounds/footsteps.ogg");
			backgroundMusic = new Sound("sounds/background.ogg");
			exitMusic = new Sound("sounds/exit.ogg");

			backgroundMusic.loop(1.0f, 0.6f);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		highlightSound.stop();
		playerMovingSound.stop();
		backgroundMusic.stop();
		exitMusic.stop();

		backgroundMusic.loop(1.0f, 0.6f);
	}

	public void playHighlight() {
		if (!isHighlightPlaying())
			highlightSound.play(1.0f, 0.6f);
	}

	public void playExitMusic() {
		if (exitMusic.playing())
			return;

		backgroundMusic.stop();
		exitMusic.loop(1.0f, 0.6f);
	}

	public boolean isHighlightPlaying() {
		return highlightSound.playing();
	}

	public void playerMoving(boolean isMoving) {
		if (isMoving) {
			if (playerMovingSound.playing())
				return;
			else
				playerMovingSound.loop(1.0f, 0.3f);
		} else {
			if (playerMovingSound.playing())
				playerMovingSound.stop();
		}
	}

	public void playSoundOnce(Sound sound) {
		sound.play(1.0f, 0.6f);
	}
}
