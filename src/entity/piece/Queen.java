package entity.piece;

import ability.ShootCardinal;
import ability.ShootDiagonal;
import entity.base.Piece;
import gui.GameGUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Queen extends Piece {
	private int reflectLeft;

	public Queen(double x, double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		this.ability.add(new ShootDiagonal(1, 0.15));
		this.ability.add(new ShootCardinal(1, 0.15));
		this.setReflectLeft(3);

		// Piece Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/b_queen_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);
		this.imageView.setX(getGridX() * tileSize);
		this.imageView.setY(getGridY() * tileSize);

		setBulletImageView("/Bullet/56.png");
	}

	public int getReflectLeft() {
		return reflectLeft;
	}
	
	public void setReflectLeft(int reflectLeft) {
		this.reflectLeft = reflectLeft;
	}

	public void reflected() {
		this.reflectLeft--;
	}

}
