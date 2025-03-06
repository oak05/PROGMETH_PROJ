package entity.player;

import java.util.ArrayList;
import java.util.Random;
import ability.Ability;
import ability.ShootCardinal;
import ability.ShootDiagonal;
import ability.ShootStraight;
import entity.base.Piece;
import gui.GameGUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends Piece {

	public Player(Double x, Double y, int hp) {
		// TODO Auto-generated constructor stub
		// Player Logic
		super(x, y, hp);
		ArrayList<Ability> playerAbility = new ArrayList<Ability>();
		playerAbility.add(new ShootStraight(2, 0.075));
		setAbility(playerAbility);
		// Player Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/w_king_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);

		setBulletImageView("/Bullet/65.png");
	}

	// Getters & Setters

	public ImageView getPlayerImageView() {
		return this.imageView;
	}

	public void setPlayerImageView(ImageView playerImageView) {
		this.imageView = playerImageView;
	}

}