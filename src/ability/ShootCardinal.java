package ability;

import java.util.ArrayList;

import entity.base.Bullet;
import entity.base.Piece;
import entity.player.Player;

public class ShootCardinal extends Shoot {

	// Shoot bullets in Left , Right , Up , Down direction simultaneously

	public ShootCardinal(int bulletDamage, double bulletSpeed) {
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
		newBullets.add(new Bullet(shooter.getGridX(), Y, getBulletDamage(), getBulletSpeed(), 2, isPlayer));
		newBullets.add(new Bullet(shooter.getGridX(), Y, getBulletDamage(), getBulletSpeed(), 3, isPlayer));
		newBullets.add(new Bullet(shooter.getGridX(), Y, getBulletDamage(), getBulletSpeed(), 4, isPlayer));
		return newBullets;
	}

}