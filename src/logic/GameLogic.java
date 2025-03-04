package logic;

import entity.base.*;
import entity.enemy.Enemy;
import entity.piece.*;
import entity.player.Player;
import ability.Ability;
import ability.ShootCardinal;
import ability.ShootDiagonal;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameLogic {
	// Static instance (Singleton-like)
	private static GameLogic instance;

	private Player player;
	private ArrayList<Piece> enemies;
	private ArrayList<Bullet> bullets;
	private boolean isRunning;

	private int wave; // Track the current wave
	private int maxEnemies; // Max enemies per wave
	private int[][] enemypositions = {{},{},{},{}};

	public GameLogic() {
		player = new Player(8.0, 15.0, 1000); // Start position
		enemies = new ArrayList<>();
		bullets = new ArrayList<>();
		isRunning = true;
		wave = 1;
		maxEnemies = 3; // Initial number of enemies in wave 1
		spawnEnemies(); // Spawn enemies for the first wave
	}

	// Static method to get instance
	public static GameLogic getInstance() {
		if (instance == null) {
			instance = new GameLogic();
		}
		return instance;
	}

	private void spawnEnemies() {
		int enemiesToSpawn = wave * maxEnemies; // Scale enemies by wave number
		for (int i = 0; i < enemiesToSpawn; i++) {
			// Spawn a new enemy at a random position (for simplicity, we use Pawn here)
			Enemy enemy = new Enemy(Math.floor(Math.random() * 17), Math.floor(Math.random() * 17), 20);
			ArrayList<Ability> abilities = new ArrayList<>();
			abilities.add(new ShootCardinal(10, 0.075));
			enemy.setAbility(abilities);
			enemies.add(enemy); // Add enemy to the list
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
			((Enemy) enemy).count();
			if (((Enemy) enemy).getCount() >= 150) {
				enemy.shootBullet();
				((Enemy) enemy).resetCount();
			}
		}

		// Move bullets and check if they need to be removed
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = iterator.next();
			bullet.move();
			if (bullet.getGridY() < -1) {
				iterator.remove();
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

		// Bullet vs Bullet Collision (unchanged)
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet1 = bullets.get(i);
			double tolerance = bullet1.getDirection() < 5 ? 0.125 : 0.25;
			for (int j = i + 1; j < bullets.size(); j++) {
				Bullet bullet2 = bullets.get(j);
				if (bullet1.isPlayerBullet() != bullet2.isPlayerBullet()
						&& Math.abs(bullet1.getGridX() - bullet2.getGridX()) < tolerance
						&& Math.abs(bullet1.getGridY() - bullet2.getGridY()) < tolerance) {
					bulletsToRemove.add(bullet1);
					bulletsToRemove.add(bullet2);
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
					double dy = bullet.getGridY() - enemy.getGridY() + 1;
					double distance = Math.sqrt(dx * dx + dy * dy);

					if (distance < tolerance) {
						enemy.takeDamage(bullet.getDamage());
						bullet.decreaseDurability();
						if (bullet.isDestroyed()) {
							bulletsToRemove.add(bullet);
						}
						if (enemy.isDead()) {
							deadEnemies.add(enemy);
						}
					}
				}
			} else {
				double dx = bullet.getGridX() - player.getGridX();
				double dy = bullet.getGridY() - player.getGridY();
				double distance = Math.sqrt(dx * dx + dy * dy);
				if (distance < tolerance) {
					player.takeDamage(bullet.getDamage());
					bulletsToRemove.add(bullet);
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
//	    isRunning = true;   // make sure the game loop will run again
		instance = new GameLogic();
	}

	// Transition to the next waves
	private void nextWave() {
		wave++; // Increase wave number
		spawnEnemies(); // Spawn enemies for the next wave
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
}
