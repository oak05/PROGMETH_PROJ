package entity.piece;

import ability.ShootDiagonal;
import entity.base.Piece;

public class Bishop extends Piece {

	public Bishop(Double x, Double y, int hp) {
		// TODO Auto-generated constructor stub
		super(x, y, hp);;
		this.ability.add(new ShootDiagonal(2, 0.075));
	}

}