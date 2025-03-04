package ability;

import java.util.ArrayList;

import entity.base.Bullet;
import entity.base.Entity;
import entity.base.Piece;
import entity.player.Player;

public class ShootStraight extends Shoot {

	public ShootStraight(int bulletDamage, double bulletSpeed) {
		super(bulletDamage, bulletSpeed);
	}

	@Override
	public ArrayList<Bullet> createBullet(Piece shooter) {
		// TODO Auto-generated method stub
		ArrayList<Bullet> newBullets = new ArrayList<Bullet>();
		if (shooter instanceof Player) {
			isPlayer = true;
			Y = shooter.getGridY() - 1;
		} else {
			Y = shooter.getGridY();
		}
		newBullets.add(new Bullet(shooter.getGridX(), Y, getBulletDamage(), getBulletSpeed(), 1, isPlayer));
		System.out.println("Bullet Created at " + "( " + shooter.getGridX() + ", " + shooter.getGridY() + " )");
		return newBullets;
	}

}