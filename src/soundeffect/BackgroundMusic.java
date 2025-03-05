package soundeffect;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BackgroundMusic {
	private static MediaPlayer mediaPlayer;

	public static void playBGM() {
		try {
			Media bgm = new Media(BackgroundMusic.class.getResource("/sounds/BGM/BGM1.mp3").toString());
			mediaPlayer = new MediaPlayer(bgm);
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.play();
		} catch (Exception e) {
			System.err.println("Error loading background music: " + e.getMessage());
		}
	}

	public static void stopBGM() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
}
