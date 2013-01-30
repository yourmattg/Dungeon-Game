package actions;

import game.Mob;

/**
 * A basic Attack Action. It immediately deals a single damage amount to the target mob.
 * @author mattgraf
 *
 */
public class AttackAction extends Action{
	public int damage;
	
	/**
	 * Constructor with target and damage specified
	 * @param target
	 * @param damage
	 */
	public AttackAction(Mob target, int damage){
		this.damage = damage;
		this.target = target;
	}

	/**
	 * Simply deal the damage and check the target's health. Return false since this doesn't repeat
	 */
	@Override
	public boolean perform(int delta) {
		target.health -= damage;
		if(target.deathCheck()){
			target.die();
		}
		return false;
	}
	
	
}
