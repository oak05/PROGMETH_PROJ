package entity.player;

import java.util.ArrayList;
import java.util.Random;
import ability.Ability;
import ability.ShootCardinal;
import ability.ShootDiagonal;
import ability.ShootStraight;
import entity.base.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends Piece {

	private double speed;

	public Player(Double x, Double y, int hp) {
		// TODO Auto-generated constructor stub
		// Player Logic
		super(x, y, hp);
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
		setAbility(playerAbility);
		// Player Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/w_king_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);
		updatePlayerPosition();
	}

	// Getters & Setters

	public ImageView getPlayerImageView() {
		return this.imageView;
	}

	public void setPlayerImageView(ImageView playerImageView) {
		this.imageView = playerImageView;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}