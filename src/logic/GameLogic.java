package logic;

import entity.base.*;
import entity.piece.*;
import entity.player.Player;
import gui.GameGUI;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ability.Ability;
import ability.ShootCardinal;
import ability.ShootStraight;

public class GameLogic {
    // ✅ Static instance (Singleton-like)
    private static GameLogic instance;

    private Player player;
    private ArrayList<Piece> enemies;
    private ArrayList<Bullet> bullets;
    private boolean isRunning;

    private static long count = 0;

    private GameLogic() {
        player = new Player(8.0, 15.0, 100); // Start position
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        isRunning = true;
        spawnEnemies();
    }

    // ✅ Static method to get instance
    public static GameLogic getInstance() {
        if (instance == null) {
            instance = new GameLogic();
        }
        return instance;
    }

    private void spawnEnemies() {
        Pawn enemy = new Pawn(8.0, 0.0, 20);
        ArrayList<Ability> abilities = new ArrayList<>();
        abilities.add(new ShootCardinal(10, 0.075));
        enemy.setAbility(abilities);
        enemies.add(enemy); // Example enemy
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
        if (!isRunning)
            return;
        for (Piece enemy : enemies) {
            count++;
            if (count >= 50) { // Adjust based on game speed
                enemy.shootBullet();
                count = 0;
            }
        }
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.move();
            if (bullet.getGridY() < -1) {
                iterator.remove();
            }
        }
        checkCollisions();
    }

    private void checkCollisions() {
        ArrayList<Piece> deadEnemies = new ArrayList<>();
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.isPlayerBullet()) {
                for (Piece enemy : enemies) {
                    if (Math.abs(bullet.getGridX() - enemy.getGridX()) <= 0.125
                            && Math.abs(bullet.getGridY() - enemy.getGridY()) <= 0.125) {
                        enemy.takeDamage(bullet.getDamage());
                        bulletsToRemove.add(bullet);
                        if (enemy.isDead()) {
                            System.out.println(bullet.isPlayerBullet());
                            deadEnemies.add(enemy);
                        }
                    }
                }
            } else {
                if (Math.abs(bullet.getGridX() - player.getGridX()) <= 0.125
                        && Math.abs(bullet.getGridY() - player.getGridY()) <= 0.5) {
                    player.takeDamage(bullet.getDamage());
                    bulletsToRemove.add(bullet);
                    if (player.isDead()) {
                        // Stop game
                        isRunning = false;
                    }
                }
            }
        }
        enemies.removeAll(deadEnemies);
        bullets.removeAll(bulletsToRemove);
    }

    // **New Method**: Check if the game is over
    public boolean isGameOver() {
        return player.isDead(); // Return true if the player is dead
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
}