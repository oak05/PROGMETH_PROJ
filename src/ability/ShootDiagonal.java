package ability;

import java.util.ArrayList;

import entity.base.Bullet;
import entity.base.Entity;
import entity.base.Piece;
import entity.player.Player;

public class ShootDiagonal extends Shoot {

	// Shoot bullets in all diagonal direction simultaneously

	public ShootDiagonal(int bulletDamage, double bulletSpeed) {
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
		newBullets.add(new Bullet(shooter.getGridX(), Y, getBulletDamage(), getBulletSpeed(), 5, isPlayer));
		newBullets.add(new Bullet(shooter.getGridX(), Y, getBulletDamage(), getBulletSpeed(), 6, isPlayer));
		newBullets.add(new Bullet(shooter.getGridX(), Y, getBulletDamage(), getBulletSpeed(), 7, isPlayer));
		newBullets.add(new Bullet(shooter.getGridX(), Y, getBulletDamage(), getBulletSpeed(), 8, isPlayer));
		return newBullets;
	}

}