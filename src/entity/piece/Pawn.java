package entity.piece;

import ability.ShootStraight;
import entity.base.Piece;

public class Pawn extends Piece {

	public Pawn(double x, double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		this.ability.add(new ShootStraight(1, 0.075));
	}

}
