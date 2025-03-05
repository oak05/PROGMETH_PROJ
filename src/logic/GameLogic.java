package logic;

import entity.base.*;
import entity.piece.*;
import entity.player.Player;
import gui.GameGUI;
import ability.Ability;
import ability.ShootCardinal;
import ability.ShootDiagonal;
import ability.ShootStraight;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import soundeffect.SoundManager;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameLogic {
	// Static instance (Singleton-like)
	private static GameLogic instance;

	private Player player;
	private ArrayList<Piece> enemies;
	private ArrayList<Bullet> bullets;
	private boolean isRunning;

	private int wave; // Track the current wave
	private int maxEnemies; // Max enemies per wave
	private double[][] enemypositions = { { 3, 2 }, { 1, 8 }, { 3, 14 }, { 13, 11 }, { 13, 5 }, { 9, 1 }, { 9, 15 },
			{ 15, 15 }, { 15, 1 }, { 1, 5 }, { 1, 11 }, { 15, 8 } };
	private int[][] reflect = { { 1, 2 }, { 3, 4 }, { 5, 8 }, { 6, 7 } };

	public GameLogic() {
		player = new Player(8.0, 15.0, 10); // Start position
		enemies = new ArrayList<>();
		bullets = new ArrayList<>();
		isRunning = true;
		wave = 1;
		maxEnemies = 1; // Initial number of enemies in wave 1
	}

	// Static method to get instance
	public static GameLogic getInstance() {
		if (instance == null) {
			instance = new GameLogic();
		}
		return instance;
	}

	public static void setInstance(GameLogic instance) {
		GameLogic.instance = instance;
	}

	public void spawnEnemies() {
		int enemiesToSpawn = wave * maxEnemies; // Scale enemies by wave number
		Random random = new Random();

		// Create a list to track used positions
		Set<Integer> usedPositions = new HashSet<>();

		for (int i = 0; i < enemiesToSpawn; i++) {
			int attempts = 0;
			int number;

			// Find a unique position
			do {
				number = random.nextInt(12);
				attempts++;

				// Prevent infinite loop
				if (attempts > 50) {
					System.out.println("Warning: Could not find unique position after 50 attempts");
					break;
				}
			} while (usedPositions.contains(number));

			// Mark position as used
			usedPositions.add(number);

			// Debug print
			System.out.println("Spawning enemy at: " + enemypositions[number][1] + " " + enemypositions[number][0]);

			// Create enemy based on wave
			Piece enemy;
			switch (wave) {
			case 1:
				enemy = new Pawn(enemypositions[number][1], enemypositions[number][0], 3);
				break;
			case 2:
				enemy = new Rook(enemypositions[number][1], enemypositions[number][0], 5);
				break;
			case 3:
				enemy = new Queen(enemypositions[number][1], enemypositions[number][0], 7);
				break;
			case 4:
				enemy = new King(enemypositions[number][1], enemypositions[number][0], 10);
				break;
			default:
				// Fallback to Pawn for unexpected wave numbers
				enemy = new Pawn(enemypositions[number][1], enemypositions[number][0], 3);
			}

			// Add to scene
			Platform.runLater(() -> GameGUI.getRoot().getChildren().add(enemy.getImageView()));
			enemies.add(enemy);
		}
	}

	public void playerShoot() {
		player.shootBullet();
	}

	public void startGameLoop() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				updateGame();
			}
		}.start();
	}

	private void updateGame() {
		if (!isRunning || isGameOver())
			return;
		// Spawn bullets for enemies every 50 ticks (adjust based on game speed)
		for (Piece enemy : enemies) {
			enemy.count();
			double disX = enemy.getGridX() - player.getGridX();
			double disY = enemy.getGridY() - player.getGridY();
			if (disX == 0 && disY != 0) {
				if (disY > 0)
					enemy.setDirection(1);// Move Up
				else if (disY < 0)
					enemy.setDirection(2);// Move Down
			} else if (disY == 0 && disX != 0) {
				if (disX > 0)
					enemy.setDirection(3);// Move Left
				else if (disX < 0)
					enemy.setDirection(4);// Move Right
			} else if (disY != 0 && disX != 0) {
				if (disX > 0 && disY > 0)
					enemy.setDirection(5);// Move Up Left
				else if (disX < 0 && disY > 0)
					enemy.setDirection(6);// Move Up Right

				else if (disX > 0 && disY < 0)
					enemy.setDirection(7);// Move Down Left
				else if (disX < 0 && disY < 0)
					enemy.setDirection(8);// Move Down Right
			}
			if (enemy.getCount() >= 150) {
//				enemy.shootBullet();
				enemy.resetCount();
			}
		}

		// Move bullets and check if they need to be removed
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.move();
			if (bullet.getGridY() < -1) {
				iterator.remove();
				Platform.runLater(() -> GameGUI.getRoot().getChildren().remove(bullet.getImageView()));
			}
		}

		// Check for collisions
		checkCollisions();

		// Check if all enemies are defeated to transition to the next wave
		if (enemies.isEmpty()) {
			if (wave == 4) {
				this.isRunning = false;
				return;// End game
			}
			nextWave();
		}
	}

	private void checkCollisions() {
		ArrayList<Piece> deadEnemies = new ArrayList<>();
		ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

		// Bullet vs Bullet Collision
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet1 = bullets.get(i);
			double tolerance = bullet1.getDirection() < 5 ? 0.125 : 0.25;
			for (int j = i + 1; j < bullets.size(); j++) {
				Bullet bullet2 = bullets.get(j);
				if (bullet1.isPlayerBullet() != bullet2.isPlayerBullet()
						&& Math.abs(bullet1.getGridX() - bullet2.getGridX()) < tolerance
						&& Math.abs(bullet1.getGridY() - bullet2.getGridY()) < tolerance) {
					SoundManager.playHit();
					bulletsToRemove.add(bullet1);
					bulletsToRemove.add(bullet2);
					Platform.runLater(() -> GameGUI.getRoot().getChildren().remove(bullet1.getImageView()));
					Platform.runLater(() -> GameGUI.getRoot().getChildren().remove(bullet2.getImageView()));
					break;
				}
			}
		}

		// Bullet vs Enemy Collision
		for (Bullet bullet : bullets) {
			double tolerance = bullet.getDirection() < 5 ? 0.125 : 0.25;
			if (bullet.isPlayerBullet()) {
				for (Piece enemy : enemies) {
					double dx = bullet.getGridX() - enemy.getGridX();
					double dy = bullet.getGridY() - enemy.getGridY();
					double distance = Math.sqrt(dx * dx + dy * dy);

					if (distance < tolerance) {
						SoundManager.playHit();

						// Reflection logic for King and Queen
						if ((enemy instanceof King || enemy instanceof Queen)
								&& ((enemy instanceof King && ((King) enemy).getReflectLeft() > 0)
										|| (enemy instanceof Queen && ((Queen) enemy).getReflectLeft() > 0))) {

							// Find new direction based on reflection pairs
							for (int[] pair : reflect) {
								if (enemy.getDirection() == pair[0]) {
									bullet.setDirection(pair[1]);
									break;
								} else if (enemy.getDirection() == pair[1]) {
									bullet.setDirection(pair[0]);
									break;
								}
							}

							// Change bullet ownership and mark reflection
							bullet.setPlayerBullet(false);
							bullet.rotate();
							System.out.println(enemy.getDirection());
							// Decrement reflection count
							if (enemy instanceof King) {
								((King) enemy).reflected();
							} else {
								((Queen) enemy).reflected();
							}

							// Continue to next iteration to prevent further processing
							continue;
						}

						enemy.takeDamage(bullet.getDamage());
						bullet.decreaseDurability();
						if (bullet.isDestroyed()) {
							bulletsToRemove.add(bullet);
							Platform.runLater(() -> GameGUI.getRoot().getChildren().remove(bullet.getImageView()));
						}
						if (enemy.isDead()) {
							deadEnemies.add(enemy);
							Platform.runLater(() -> GameGUI.getRoot().getChildren().remove(enemy.getImageView()));
						}
					}
				}
			} else {
				double dx = bullet.getGridX() - player.getGridX();
				double dy = bullet.getGridY() - player.getGridY();
				double distance = Math.sqrt(dx * dx + dy * dy);
				if (distance < tolerance) {
					SoundManager.playHit();
					player.takeDamage(bullet.getDamage());
					bulletsToRemove.add(bullet);
					Platform.runLater(() -> GameGUI.getRoot().getChildren().remove(bullet.getImageView()));
					if (player.isDead()) {
						isRunning = false; // Game over
					}
				}
			}
		}

		// Remove destroyed enemies and bullets
		enemies.removeAll(deadEnemies);
		bullets.removeAll(bulletsToRemove);
	}

	public void reset() {
		instance = new GameLogic();
	}

	// Transition to the next waves
	private void nextWave() {
		Random random = new Random();
		int number = random.nextInt(3);
		ArrayList<Ability> playerAbility = new ArrayList<Ability>();
		if (number == 0) {
			playerAbility.add(new ShootStraight(1, 0.075));
		} else if (number == 1) {
			playerAbility.add(new ShootCardinal(1, 0.075));
		} else if (number == 2) {
			playerAbility.add(new ShootDiagonal(1, 0.075));
			playerAbility.add(new ShootCardinal(1, 0.075));
		}
		player.setAbility(playerAbility);
		wave++; // Increase wave number
		spawnEnemies(); // Spawn enemies for the next wave
		System.out.println("Wave : " + wave);
	}

	// Check if the game is over
	public boolean isGameOver() {
		return player.isDead(); // Return true if the player is dead
	}

	public boolean isGameWon() {
		return enemies.isEmpty() && wave == 4; // All enemies defeated
	}

	// Getters and Setters
	public Player getPlayer() {
		return player;
	}

	public ArrayList<Piece> getEnemies() {
		return enemies;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public int getWave() {
		return this.wave;
	}
}
