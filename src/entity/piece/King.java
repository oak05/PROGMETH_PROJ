package entity.piece;

import ability.ShootCardinal;
import ability.ShootDiagonal;
import entity.base.Piece;

public class King extends Piece {

	public King(Double x, Double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		this.ability.add(new ShootDiagonal(20, 0.1));
		this.ability.add(new ShootCardinal(20, 0.1));
	}

}