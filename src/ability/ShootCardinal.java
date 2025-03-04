package ability;

import java.util.ArrayList;

import entity.base.Bullet;
import entity.base.Piece;
import entity.player.Player;
import gui.GameGUI;

public class ShootCardinal extends Shoot {

	// Shoot bullets in Left , Right , Up , Down direction simultaneously

	public ShootCardinal(int bulletDamage, double bulletSpeed) {
		super(bulletDamage, bulletSpeed);
	}

	@Override
	public ArrayList<Bullet> createBullet(Piece shooter) {
		// TODO Auto-generated method stub
		ArrayList<Bullet> newBullets = new ArrayList<Bullet>();
		if (shooter instanceof Player) isPlayer = true;
		
		Bullet b1 = new Bullet(shooter.getGridX(), shooter.getGridY(), getBulletDamage(), getBulletSpeed(), 1, isPlayer);
		Bullet b2 = new Bullet(shooter.getGridX(), shooter.getGridY(), getBulletDamage(), getBulletSpeed(), 2, isPlayer);
		Bullet b3 = new Bullet(shooter.getGridX(), shooter.getGridY(), getBulletDamage(), getBulletSpeed(), 3, isPlayer);
		Bullet b4 = new Bullet(shooter.getGridX(), shooter.getGridY(), getBulletDamage(), getBulletSpeed(), 4, isPlayer);
		
		newBullets.add(b1);
		newBullets.add(b2);
		newBullets.add(b3);
		newBullets.add(b4);
	
		GameGUI.getRoot().getChildren().add(b1.getImageView());
		GameGUI.getRoot().getChildren().add(b2.getImageView());
		GameGUI.getRoot().getChildren().add(b3.getImageView());
		GameGUI.getRoot().getChildren().add(b4.getImageView());
		
		return newBullets;
	}

}