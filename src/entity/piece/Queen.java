package entity.piece;

import ability.ShootCardinal;
import ability.ShootDiagonal;
import entity.base.Piece;

public class Queen extends Piece {

	public Queen(double x, double y, int hp) {
		super(x, y, hp);
		// TODO Auto-generated constructor stub
		this.ability.add(new ShootDiagonal(10, 0.075)); 
		this.ability.add(new ShootCardinal(10, 0.075)); 
	}

}
