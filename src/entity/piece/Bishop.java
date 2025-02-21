package entity.piece;

import ability.ShootDiagonal;
import entity.base.Piece;

public class Bishop extends Piece {

	public Bishop(Double x, Double y, int hp) {
		// TODO Auto-generated constructor stub
		super(x, y, hp);
		ShootDiagonal sd_ability = new ShootDiagonal(5, 5);
		this.ability.add(sd_ability);
	}

}