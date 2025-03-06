package gui;

import entity.base.*;
import entity.player.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
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
import javafx.stage.Stage;
import logic.GameLogic;
import soundeffect.BackgroundMusic;

public class GameGUI extends Application {
	private static boolean isSpacebarPressed = false;
	private static long lastShootTime = 0;
	private static final long SHOOT_COOLDOWN = 500;
	private static final int BOARD_SIZE = 17;
	private static final int TILE_SIZE = 40;
	private static final int SIZE = BOARD_SIZE * TILE_SIZE;
	private Label healthLabel;
	private Label waveLabel;

	private Canvas canvas;
	private static StackPane root;
	private GraphicsContext gc;

	private static boolean upPressed = false;
	private static boolean downPressed = false;
	private static boolean leftPressed = false;
	private static boolean rightPressed = false;

	@Override
	public void start(Stage primaryStage) {
		// Main Menu Setup
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
		Scene scene = new Scene(layout, SIZE, SIZE);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Show the wave selection screen after clicking "Start Game"
	private void showLevelSelectionScreen(Stage primaryStage) {
		VBox levelSelectionLayout = new VBox(20);
		levelSelectionLayout.setAlignment(Pos.CENTER);

		// Wave selection options (ComboBox for simplicity)
		ComboBox<String> levelSelectionComboBox = new ComboBox<>();
		levelSelectionComboBox.getItems().addAll("Level 1", "Level 2", "Level 3", "Level 4 ", "Level 5");
		levelSelectionComboBox.setValue("Level 1");

		// Start Button (to start the game with selected wave)
		Button startGameButton = new Button("Start Game");
		// In showWaveSelectionScreen method of GameGUI.java
		startGameButton.setOnAction(e -> {
			String selectedLevel = levelSelectionComboBox.getValue();
			int level = levelSelectionComboBox.getSelectionModel().getSelectedIndex() + 1;
			System.out.println(level);

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
		Scene waveSelectionScene = new Scene(levelSelectionLayout, SIZE, SIZE);
		primaryStage.setScene(waveSelectionScene);
		primaryStage.show();
	}

	public void gameStart(Stage primaryStage) {
		canvas = new Canvas(SIZE, SIZE);
		gc = canvas.getGraphicsContext2D();

		Player player = GameLogic.getInstance().getPlayer();
		GameLogic.getInstance().playerUpgrade();
		player.getPlayerImageView().setTranslateX(player.getGridX() * TILE_SIZE);
		player.getPlayerImageView().setTranslateY(player.getGridY() * TILE_SIZE);

		// Initialize labels
		healthLabel = new Label("Health: " + player.getHealth());
		healthLabel.setTextFill(Color.WHITE);
		healthLabel.setFont(Font.font(16));

		waveLabel = new Label("Wave: " + GameLogic.getInstance().getWave());
		waveLabel.setTextFill(Color.WHITE);
		waveLabel.setFont(Font.font(16));

		HBox infoBox = new HBox(20);
		infoBox.getChildren().addAll(healthLabel, waveLabel);
		infoBox.setAlignment(Pos.TOP_LEFT);
		infoBox.setPadding(new Insets(10));
		infoBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

		root = new StackPane(canvas);
		root.getChildren().addAll(infoBox, player.getPlayerImageView());
		BackgroundMusic.playBGM();

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("What the Hell is This Chess?");
		primaryStage.show();

		// Handle player input
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.W) {
				player.moveUp();
			} else if (event.getCode() == KeyCode.S) {
				player.moveDown();
			} else if (event.getCode() == KeyCode.A) {
				player.moveLeft();
			} else if (event.getCode() == KeyCode.D) {
				player.moveRight();
			} else if (event.getCode() == KeyCode.UP) {
				player.setDirection(1);
			} else if (event.getCode() == KeyCode.DOWN) {
				player.setDirection(2);
			} else if (event.getCode() == KeyCode.LEFT) {
				player.setDirection(3);
			} else if (event.getCode() == KeyCode.RIGHT) {
				player.setDirection(4);
			} else if (event.getCode() == KeyCode.UP && event.getCode() == KeyCode.LEFT) {
				player.setDirection(5);
			} else if (event.getCode() == KeyCode.UP && event.getCode() == KeyCode.RIGHT) {
				player.setDirection(6);
			} else if (event.getCode() == KeyCode.DOWN && event.getCode() == KeyCode.LEFT) {
				player.setDirection(7);
			} else if (event.getCode() == KeyCode.DOWN && event.getCode() == KeyCode.RIGHT) {
				player.setDirection(8);
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
		GameLogic.getInstance().spawnEnemies();
		GameLogic.getInstance().startGameLoop();
		render(primaryStage);
	}

	private void render(Stage primaryStage) {
		AnimationTimer gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (GameLogic.getInstance().isGameOver()) {
					stop(); // Stop the game loop
					showGameOverScreen(primaryStage); // Show the game over screen
					return;
				} else if (GameLogic.getInstance().isGameWon()) {
					stop();
					showGameWonScreen(primaryStage);
					return;
				}

				// Update labels every frame
				updateHealthLabel(GameLogic.getInstance().getPlayer().getHealth());
				updateWaveLabel(GameLogic.getInstance().getWave());

				gc.clearRect(0, 0, SIZE, SIZE);
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

	// Draw enemies, bullets
	private void drawEntities() {

		for (Bullet bullet : GameLogic.getInstance().getBullets()) {
			bullet.updateBulletPosition(); // Move the bullet
		}
	}

	// GameOver screen
	private void showGameOverScreen(Stage primaryStage) {
		Label gameOverLabel = new Label("Game Over");
		gameOverLabel.setFont(new Font("Arial", 50));
		gameOverLabel.setTextFill(Color.RED);
		BackgroundMusic.stopBGM();

		Button restart = new Button("Restart");
		restart.setOnAction(e -> {
			resetGameState();
			// Use the same primary stage instead of creating a new one
			showLevelSelectionScreen(primaryStage);
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

		Scene gameOverScene = new Scene(layout, SIZE, SIZE);
		primaryStage.setScene(gameOverScene);
		primaryStage.setTitle("Game Over");
		primaryStage.show();
	}

	// GameOver screen
	private void showGameWonScreen(Stage primaryStage) {
		Label gameOverLabel = new Label("Game Won");
		gameOverLabel.setFont(new Font("Arial", 50));
		gameOverLabel.setTextFill(Color.YELLOW);

		Button restart = new Button("Restart");
		restart.setOnAction(e -> {
			resetGameState();
			// Use the same primary stage instead of creating a new one
			showLevelSelectionScreen(primaryStage);
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

		Scene gameOverScene = new Scene(layout, SIZE, SIZE);
		primaryStage.setScene(gameOverScene);
		primaryStage.setTitle("Game Over");
		primaryStage.show();
	}

	private void resetGameState() {
		// Properly reset the game state
		GameLogic.getInstance().reset();

		// Reset player position and properties
		Player player = GameLogic.getInstance().getPlayer();
		player.setGridX(8.0); // Put player back to starting position
		player.setGridY(15.0); // Adjust to your starting position
		player.setHealth(10); // You need to implement this method in Player class

		// Clear game collections
		GameLogic.getInstance().getEnemies().clear();
		GameLogic.getInstance().getBullets().clear();

		setLastShootTime(0);
		setSpacebarPressed(false);
	}

	private void handleShooting(long now) {
		if (isSpacebarPressed && now - lastShootTime >= SHOOT_COOLDOWN * 1_000_000) { // Convert ms to ns
			GameLogic.getInstance().getPlayer().shootBullet(); // Player shoots
			lastShootTime = now; // Update cooldown time
		}
	}

	// In GameGUI.java
	public void updateHealthLabel(int health) {
		Platform.runLater(() -> healthLabel.setText("Health: " + health));
	}

	public void updateWaveLabel(int wave) {
		Platform.runLater(() -> waveLabel.setText("Wave: " + wave));
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

	public static int getSize() {
		return SIZE;
	}

	public static StackPane getRoot() {
		return root;
	}

}