package ability;

import java.util.ArrayList;

import entity.base.Bullet;
import entity.base.Entity;
import entity.base.Piece;
import entity.player.Player;
import gui.GameGUI;

public class ShootStraight extends Shoot {

	public ShootStraight(int bulletDamage, double bulletSpeed) {
		super(bulletDamage, bulletSpeed);
	}

	@Override
	public ArrayList<Bullet> createBullet(Piece shooter) {
		// TODO Auto-generated method stub
		ArrayList<Bullet> newBullets = new ArrayList<Bullet>();
		if (shooter instanceof Player) isPlayer = true;
		
		Bullet b1 = new Bullet(shooter.getGridX(), shooter.getGridY(), getBulletDamage(), getBulletSpeed(), 1 ,isPlayer);
		
		newBullets.add(b1);
		
		GameGUI.getRoot().getChildren().add(b1.getImageView());
		
		System.out.println("Bullet Created at " + "( " + shooter.getGridX() + ", " + shooter.getGridY() + " )");
		return newBullets;
	}

}