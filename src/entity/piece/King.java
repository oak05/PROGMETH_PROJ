package entity.piece;

import ability.ShootCardinal;
import ability.ShootDiagonal;
import entity.base.Piece;
import gui.GameGUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class King extends Piece {
	private int reflectLeft = 5;

	public King(Double x, Double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		this.ability.add(new ShootDiagonal(4, 0.075));
		this.ability.add(new ShootCardinal(4, 0.075));

		// Piece Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/b_king_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);
		this.imageView.setX(getGridX() * tileSize);
		this.imageView.setY(getGridY() * tileSize);

		setBulletImageView("/Bullet/63.png");

	}

	public int getReflectLeft() {
		return reflectLeft;
	}

	public void reflected() {
		this.reflectLeft--;
	}
}