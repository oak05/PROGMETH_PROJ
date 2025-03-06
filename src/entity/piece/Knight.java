package entity.piece;

import ability.ShootDiagonal;
import entity.base.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Knight extends Piece {
	private int reflectLeft = 1;

	public Knight(double x, double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		this.ability.add(new ShootDiagonal(1, 0.125));
		
		// Piece Display
		this.imageView = new ImageView(
				new Image(getClass().getResourceAsStream("/PNGs/With Shadow/1024px/b_knight_png_shadow_1024px.png")));
		this.imageView.setManaged(false);
		this.imageView.setFitWidth(tileSize);
		this.imageView.setFitHeight(tileSize);
		this.imageView.setX(getGridX() * tileSize);
		this.imageView.setY(getGridY() * tileSize);
		
		setBulletImageView("/Bullet/01.png");
	}
	
	public int getReflectLeft() {
		return reflectLeft;
	}

	public void reflected() {
		this.reflectLeft--;
	}

}
