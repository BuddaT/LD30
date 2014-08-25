package net.buddat.ludumdare.ld30;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundManager {

	private Sound highlightSound = null;
	private Sound playerMovingSound = null;

	public SoundManager() {
		try {
			highlightSound = new Sound("sounds/highlight.ogg");
			playerMovingSound = new Sound("sounds/footsteps.ogg");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void playHighlight() {
		if (!isHighlightPlaying())
			highlightSound.play(1.0f, 0.4f);
	}

	public boolean isHighlightPlaying() {
		return highlightSound.playing();
	}

	public void playerMoving(boolean isMoving) {
		if (isMoving) {
			if (playerMovingSound.playing())
				return;
			else
				playerMovingSound.loop(1.0f, 0.2f);
		} else {
			if (playerMovingSound.playing())
				playerMovingSound.stop();
		}
	}
}
