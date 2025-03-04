package entity.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ability.Ability;
import ability.ShootCardinal;
import ability.ShootDiagonal;
import ability.ShootStraight;
import entity.base.Entity;
import entity.base.Piece;
import entity.base.Relocatable;
import entity.piece.Pawn;
import gui.GameGUI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import logic.GameLogic;

public class Player extends Piece {

	private double speed;

	public Player(Double x, Double y, int hp) {
		// TODO Auto-generated constructor stub

		// Player Logic
		super(x, y, hp);
//		this.ability.add(new ShootStraight(10, 0.075));
		this.ability.add(new ShootDiagonal(100, 0.075));
		this.ability.add(new ShootCardinal(100, 0.075));

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