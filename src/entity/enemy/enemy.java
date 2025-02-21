package entity.enemy;

import java.util.ArrayList;

import ability.Ability;
import ability.Shoot;
import ability.ShootStraight;
import entity.base.Bullet;
import entity.base.Piece;
import entity.piece.Pawn;
import logic.GameLogic;

public class enemy extends Piece {

	public enemy(Double x, Double y, int hp) {
		// TODO Auto-generated constructor stub
		super(x, y, hp);
	}

	public void handleShoot() {
//		if (now - lastShootTime >= SHOOT_COOLDOWN * 1_000_000) { // Convert ms to ns
//			enemy.shootBullet();
//			lastShootTime = now; // Update cooldown time
	}

//	private void spawnEnemies() {
//		Pawn enemy = new Pawn(8.0, 0.0, 20);
//		ArrayList<Ability> abilities = new ArrayList<>();
//		abilities.add(new ShootStraight(10, 0.075));
//		enemy.setAbility(abilities);
//		enemies.add(enemy); // Example enemy
//
//	}
	
//	@Override
//	public void shootBullet() {
//		ArrayList<Ability> shootAbilities = getAbility();
//		for (Ability ability : shootAbilities) {
//			if (ability instanceof Shoot) {
//				ArrayList<Bullet> newBullets = ((Shoot) ability).createBullet(this);
//				GameLogic.getInstance().getBullets().addAll(newBullets);
//			}
//		}
//	}
}
