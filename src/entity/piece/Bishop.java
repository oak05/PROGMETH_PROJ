package entity.piece;

import ability.ShootDiagonal;
import entity.base.Piece;
import gui.GameGUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bishop extends Piece {

	public Bishop(Double x, Double y, int hp) {
		// TODO Auto-generated constructor stub
		super(x, y, hp);
		this.ability.add(new ShootDiagonal(1, 0.075));

		// Piece Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/b_bishop_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);
		this.imageView.setX(getGridX() * tileSize);
		this.imageView.setY(getGridY() * tileSize);

		setBulletImageView("/Bullet/04.png");
	}
}