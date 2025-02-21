package gui;

import entity.base.*;
import entity.player.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.GameLogic;

public class GameGUI extends Application {
	private static boolean isSpacebarPressed = false;
	private static long lastShootTime = 0;
	private static final long SHOOT_COOLDOWN = 1000;

	private static final int BOARD_SIZE = 17;
	private static final int TILE_SIZE = 40;
	private static final int WIDTH = BOARD_SIZE * TILE_SIZE;
	private static final int HEIGHT = BOARD_SIZE * TILE_SIZE;

	private GameLogic game;
	private Canvas canvas;
	private StackPane root;
	private GraphicsContext gc;

	@Override
	public void start(Stage primaryStage) {
		game = GameLogic.getInstance();
		canvas = new Canvas(WIDTH, HEIGHT);
		gc = canvas.getGraphicsContext2D();

		ImageView playerImageView = GameLogic.getInstance().getPlayer().getPlayerImageView();
		root = new StackPane(canvas);
		root.getChildren().add(playerImageView);

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.setTitle("What the Hell is This Chess?");
		primaryStage.show();

		// Handle player input
		scene.setOnKeyPressed(event -> {
			Player player = game.getPlayer();
			if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
				player.moveUp();
			} else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
				player.moveDown();
			} else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
				player.moveLeft();
			} else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
				player.moveRight();
			}
			if (event.getCode() == KeyCode.SPACE) {
				isSpacebarPressed = true;
			}
		});
		scene.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.SPACE) {
				isSpacebarPressed = false;
			}
		});

		// Start game loop
		game.startGameLoop();
		render();

	}

	private void render() {
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				gc.clearRect(0, 0, WIDTH, HEIGHT);
				drawChessBoard();
				drawEntities();
				handleShooting(now); // Handle shooting with cooldown
				root.layout();
			}
		}.start();
	}

	// ♟️ Draw a proper 16x16 chess board
	private void drawChessBoard() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if ((row + col) % 2 == 0) {
					gc.setFill(Color.LIGHTGRAY);
				} else {
					gc.setFill(Color.BLACK);
				}
				gc.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
	}

	// 🔫 Draw player, enemies, bullets
	private void drawEntities() {

		gc.setFill(Color.RED);
		for (Piece enemy : game.getEnemies()) {
			gc.fillRect(enemy.getGridX() * TILE_SIZE, enemy.getGridY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		}

		gc.setFill(Color.YELLOW);
		for (Bullet bullet : game.getBullets()) {
			double bulletSize = TILE_SIZE / 2.5;
			double bulletY;
			if (bullet.isPlayerBullet()) {
				bulletY = (bullet.getGridY() * TILE_SIZE + TILE_SIZE / 2 - bulletSize / 2) + 1 * TILE_SIZE;
			} else {
				bulletY = (bullet.getGridY() * TILE_SIZE + TILE_SIZE / 2 - bulletSize / 2);
			}
			gc.fillOval(bullet.getGridX() * TILE_SIZE + TILE_SIZE / 2 - bulletSize / 2, bulletY, bulletSize,
					bulletSize);
		}
	}

	private void handleShooting(long now) {
		if (isSpacebarPressed && now - lastShootTime >= SHOOT_COOLDOWN * 1_000_000) { // Convert ms to ns
			game.getPlayer().shootBullet(); // Player shoots
			lastShootTime = now; // Update cooldown time
		}
	}

	public static boolean isSpacebarPressed() {
		return isSpacebarPressed;
	}

	public static void setSpacebarPressed(boolean isSpacebarPressed) {
		GameGUI.isSpacebarPressed = isSpacebarPressed;
	}

	public static long getLastShootTime() {
		return lastShootTime;
	}

	public static void setLastShootTime(long lastShootTime) {
		GameGUI.lastShootTime = lastShootTime < 0 ? 0 : lastShootTime;
	}

	public static long getShootCooldown() {
		return SHOOT_COOLDOWN;
	}

	public GameLogic getGame() {
		return game;
	}

	public void setGame(GameLogic game) {
		this.game = game;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public GraphicsContext getGc() {
		return gc;
	}

	public void setGc(GraphicsContext gc) {
		this.gc = gc;
	}

	public static int getBoardSize() {
		return BOARD_SIZE;
	}

	public static int getTileSize() {
		return TILE_SIZE;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}
}