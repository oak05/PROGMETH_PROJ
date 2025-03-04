package entity.piece;

import entity.base.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Knight extends Piece {

	public Knight(double x, double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		
		// Piece Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/b_knight_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);
		this.imageView.setX(getGridX() * tileSize);
		this.imageView.setY(getGridY() * tileSize);
	}

}
