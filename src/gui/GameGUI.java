package gui;

import entity.base.*;
import entity.player.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

	private Stage primaryStage;
	private GameLogic game;
	private Canvas canvas;
	private StackPane root;
	private GraphicsContext gc;

	@Override
	public void start(Stage primaryStage) {
		// Main Menu Setup
		this.primaryStage = primaryStage;
		showMainMenu(primaryStage);
	}

	// Show the main menu
	private void showMainMenu(Stage primaryStage) {
		primaryStage.setTitle("Game Menu");

		// Load Background Image
		Image bgImage = new Image("image1_0.png");
		BackgroundImage backgroundImage = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(100, 100, true, true, true, false));

		// Create Layout with Background
		VBox layout = new VBox(20);
		layout.setBackground(new Background(backgroundImage));

		// Create buttons
		Button startButton = new Button("Start Game");
		Button quitButton = new Button("Quit");

		startButton.setOnAction(e -> showLevelSelectionScreen(primaryStage));

		quitButton.setOnAction(e -> System.exit(0));
		Region spacer = new Region();
		StackPane.setAlignment(spacer, Pos.TOP_CENTER);
		spacer.setPrefHeight(0.25 * 680);
		// Add buttons to layout
		layout.getChildren().addAll(spacer, startButton, quitButton);
		layout.setStyle("-fx-alignment: center; -fx-padding: 50;");

		// Set Scene with new size (680x680)
		Scene scene = new Scene(layout, 680, 680);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Show the wave selection screen after clicking "Start Game"
	private void showLevelSelectionScreen(Stage primaryStage) {
		VBox levelSelectionLayout = new VBox(20);
		levelSelectionLayout.setAlignment(Pos.CENTER);

		// Wave selection options (ComboBox for simplicity)
		ComboBox<String> levelSelectionComboBox = new ComboBox<>();
		levelSelectionComboBox.getItems().addAll("Level 1", "Level 2", "Level 3", "Level 4");
		levelSelectionComboBox.setValue("Level 1");

		// Start Button (to start the game with selected wave)
		Button startGameButton = new Button("Start Game");
		// In showWaveSelectionScreen method of GameGUI.java
		startGameButton.setOnAction(e -> {
			String selectedLevel = levelSelectionComboBox.getValue();
			int level = levelSelectionComboBox.getSelectionModel().getSelectedIndex() + 1;

			// Set the wave in GameLogic before starting the game
			GameLogic.getInstance().setWave(level);

			try {
				gameStart(primaryStage);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		// Add components to layout
		levelSelectionLayout.getChildren().addAll(new javafx.scene.control.Label("Choose Level"),
				levelSelectionComboBox, startGameButton);

		// Set Scene for Wave Selection
		Scene waveSelectionScene = new Scene(levelSelectionLayout, 680, 680);
		primaryStage.setScene(waveSelectionScene);
		primaryStage.show();
	}

	public void gameStart(Stage primaryStage) {
		game = GameLogic.getInstance();
		canvas = new Canvas(WIDTH, HEIGHT);
		gc = canvas.getGraphicsContext2D();

		Player player = game.getPlayer();
		// Position player properly on screen
		player.getPlayerImageView().setTranslateX(player.getGridX() * TILE_SIZE);
		player.getPlayerImageView().setTranslateY(player.getGridY() * TILE_SIZE);

		root = new StackPane(canvas);
		root.getChildren().add(player.getPlayerImageView());

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.setTitle("What the Hell is This Chess?");
		primaryStage.show();

		// Handle player input
		scene.setOnKeyPressed(event -> {
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
		AnimationTimer gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (game.isGameOver()) {
					primaryStage.close();
					stop(); // Stop the game loop
					showGameOverScreen(); // Show the game over screen
					return;
				}
				gc.clearRect(0, 0, WIDTH, HEIGHT);
				drawChessBoard();
				drawEntities();
				handleShooting(now); // Handle shooting with cooldown
				root.layout();
			}
		};
		gameLoop.start();
	}

	// Draw a proper 16x16 chess board
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

	// Draw player, enemies, bullets
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

	// GameOver screen
	private void showGameOverScreen() {
		Stage gameOverStage = new Stage();
		Label gameOverLabel = new Label("Game Over");
		gameOverLabel.setFont(new Font("Arial", 50));
		gameOverLabel.setTextFill(Color.RED);

		Button restart = new Button("Restart");

		restart.setOnAction(e -> {
			gameOverStage.close();
			resetGameState();

			// Create a new primary stage since the old one was closed
			Stage newStage = new Stage();
			showLevelSelectionScreen(newStage);
		});

		Button quit = new Button("Quit");
		quit.setOnAction(e -> System.exit(0));

		VBox layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);

		Image bgImage = new Image(getClass().getResourceAsStream("/gameOverBg.png"));
		BackgroundImage bg = new BackgroundImage(bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, false, true));

		layout.setBackground(new Background(bg));
		layout.getChildren().addAll(gameOverLabel, restart, quit);

		Scene gameOverScene = new Scene(layout, 680, 680);
		gameOverStage.setScene(gameOverScene);
		gameOverStage.setTitle("Game Over");
		gameOverStage.show();
	}

	private void resetGameState() {
		// Properly reset the game state
		GameLogic gameLogic = GameLogic.getInstance();

		// Reset player position and properties
		Player player = gameLogic.getPlayer();
		player.setGridX(8.0); // Put player back to starting position
		player.setGridY(15.0); // Adjust to your starting position
		player.setHealth(10); // You need to implement this method in Player class

		// Clear game collections
		gameLogic.getEnemies().clear();
		gameLogic.getBullets().clear();

		gameLogic.setWave(1);

		setLastShootTime(0);
		setSpacebarPressed(false);
	}

	private void handleShooting(long now) {
		if (isSpacebarPressed && now - lastShootTime >= SHOOT_COOLDOWN * 1_000_000) { // Convert ms to ns
			game.getPlayer().shootBullet(); // Player shoots
			lastShootTime = now; // Update cooldown time
		}
	}

	// Getter & Setter

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