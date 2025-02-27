package entity.enemy;

import java.util.ArrayList;

import ability.Ability;
import ability.Shoot;
import ability.ShootStraight;
import entity.base.Bullet;
import entity.base.Piece;
import entity.piece.Pawn;
import logic.GameLogic;

public class Enemy extends Piece {
	private int count = 0;

	public Enemy(Double x, Double y, int hp) {
		// TODO Auto-generated constructor stub
		super(x, y, hp);
	}

	public void count() {
		count++;
	}

	public int getCount() {
		return count;
	}

	public void resetCount() {
		this.count = 0;
	}
}