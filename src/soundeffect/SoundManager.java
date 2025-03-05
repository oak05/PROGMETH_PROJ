package soundeffect;

import javafx.scene.media.AudioClip;

public class SoundManager {
	private static final AudioClip shootSound = new AudioClip(
			SoundManager.class.getResource("/sounds/shoots/shoot1.wav").toString());
	private static final AudioClip hitSound = new AudioClip(
			SoundManager.class.getResource("/sounds/impacts/hit1.wav").toString());

	public static void playShoot() {
		try {
			shootSound.play();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error loading sound: " + e.getMessage());
		}
	}

	public static void playHit() {
		try {
			hitSound.play();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error loading sound : " + e.getMessage());
		}
	}
}
