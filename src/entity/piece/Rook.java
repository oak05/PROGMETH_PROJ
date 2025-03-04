package entity.piece;

import ability.ShootCardinal;
import entity.base.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Rook extends Piece {

	public Rook(double x, double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		this.ability.add(new ShootCardinal(10, 0.075)); 
		
		// Piece Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/b_rook_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);
		this.imageView.setX(getGridX() * tileSize);
		this.imageView.setY(getGridY() * tileSize);
	}

}
