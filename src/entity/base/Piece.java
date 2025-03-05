package entity.base;

import java.util.ArrayList;

import ability.Ability;
import ability.Shoot;
import gui.GameGUI;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import logic.GameLogic;
import soundeffect.SoundManager;

public abstract class Piece extends Entity implements Relocatable {

	public static final int tileSize = GameGUI.getTileSize();
	private static final int GRID_BOUNDARY = GameGUI.getBoardSize() - 1;
	private int health;
	private boolean isDead;
	private int direction = 1;
	protected ImageView imageView;
	protected ArrayList<Ability> ability;
	private int count = 0;
	private String bulletImage;

	public Piece(Double x, Double y, int hp) {
		super(x, y);
		setHealth(hp);
		ability = new ArrayList<Ability>();
	}

	public void takeDamage(int damage) {
		this.setHealth(getHealth() - damage);
	}

	public void shootBullet() {
		ArrayList<Ability> shootAbilities = getAbility();
		for (Ability ability : shootAbilities) {
			if (ability instanceof Shoot) {
				ArrayList<Bullet> newBullets = ((Shoot) ability).createBullet(this, this.direction);
				GameLogic.getInstance().getBullets().addAll(newBullets);
			}
		}
		SoundManager.playShoot();
	}

	public void updatePlayerPosition() {
		Platform.runLater(() -> {
			double targetX = getGridX() * GameGUI.getTileSize();
			double targetY = getGridY() * GameGUI.getTileSize();

			// Smooth effect
			TranslateTransition moveAnimation = new TranslateTransition(Duration.millis(25), imageView);
			moveAnimation.setToX(targetX);
			moveAnimation.setToY(targetY);
			moveAnimation.setInterpolator(Interpolator.EASE_OUT);

			// bounce effect
			ScaleTransition bounce = new ScaleTransition(Duration.millis(50), imageView);
			bounce.setFromX(1.0);
			bounce.setFromY(1.0);
			bounce.setToX(1.1);
			bounce.setToY(0.9);
			bounce.setAutoReverse(true);
			bounce.setCycleCount(2);

			ParallelTransition animation = new ParallelTransition(moveAnimation, bounce);
			animation.play();
		});
	}

	@Override
	public void moveUp() {
		boolean moveable = true;
		for (Piece enemy : GameLogic.getInstance().getEnemies()) {
			if (gridY - 1 == enemy.getGridY() && gridX == enemy.getGridX()) {
				moveable = false;
			}
		}
		if (gridY > 0 && moveable)
			gridY--;
		updatePlayerPosition();
	}

	@Override
	public void moveDown() {
		boolean moveable = true;
		for (Piece enemy : GameLogic.getInstance().getEnemies()) {
			if (gridY + 1 == enemy.getGridY() && gridX == enemy.getGridX()) {
				moveable = false;
			}
		}
		if (gridY < GRID_BOUNDARY && moveable)
			gridY++;
		updatePlayerPosition();
	}

	@Override
	public void moveLeft() {
		boolean moveable = true;
		for (Piece enemy : GameLogic.getInstance().getEnemies()) {
			if (gridY == enemy.getGridY() && gridX - 1 == enemy.getGridX()) {
				moveable = false;
			}
		}
		if (gridX > 0 && moveable)
			gridX--;
		updatePlayerPosition();
	}

	@Override
	public void moveRight() {
		boolean moveable = true;
		for (Piece enemy : GameLogic.getInstance().getEnemies()) {
			if (gridY == enemy.getGridY() && gridX + 1 == enemy.getGridX()) {
				moveable = false;
			}
		}
		if (gridX < GRID_BOUNDARY && moveable)
			gridX++;
		updatePlayerPosition();
	}

	public void count() {
		count++;
	}

	public int getCount() {
		return count;
	}

	public void resetCount() {
		this.count = 0;
	}

	// Getters & Setters

	public int getHealth() {
		return health;
	}

	public void setHealth(int hp) {
		this.health = hp < 0 ? 0 : hp;
	}

	public ArrayList<Ability> getAbility() {
		return ability;
	}

	public void setAbility(ArrayList<Ability> ability) {
		this.ability = ability;
	}

	public boolean isDead() {
		return this.health <= 0;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getBulletImage() {
		return bulletImage;
	}

	public void setBulletImageView(String bulletImage) {
		this.bulletImage = bulletImage;
	}

}