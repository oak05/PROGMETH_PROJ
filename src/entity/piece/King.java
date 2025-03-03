package entity.piece;

import ability.ShootCardinal;
import ability.ShootDiagonal;
import entity.base.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class King extends Piece {

	public King(Double x, Double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		this.ability.add(new ShootDiagonal(20, 0.05));
		this.ability.add(new ShootCardinal(20, 0.05));
		
		// Piece Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/w_king_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);
		this.imageView.setX(getGridX() * tileSize);
		this.imageView.setY(getGridY() * tileSize);
	}

}