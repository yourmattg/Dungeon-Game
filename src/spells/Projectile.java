package spells;

import game.Mob;

/**
 * Projectiles are not being used yet, but they represent an in-game object such as an
 *  arrow or fireball that flies across the map.
 * @author mattgraf
 *
 */
public class Projectile {
	public String imageName;
	public int damage;
	
	/**
	 * Does damage when it hits a mob
	 * @param mob
	 */
	public void collideWithMob(Mob mob){
		mob.health -= damage;
		if(mob.deathCheck()){
			mob.die();
		}
	}
}
