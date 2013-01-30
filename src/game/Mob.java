package game;

import game.GameController.Direction;

/**
 * MOB = M(obile)OB(ject). In this case it represents a living entity of some sort.
 * @author mattgraf
 *
 */
public abstract class Mob {
	public String name;
	public String imageName;
	public boolean isPlayer = false;
	public double moveTime = 400; // ms per square
	public double dx = 0;
	public double dy = 0;
	
	public double row;
	public double col;
	
	public Direction dir;
	
	public int health = 20;
	public int maxHealth = 20;
	public int mana = 10;
	public int maxMana = 10;
	
	/**
	 * Returns true if this mob should die or not
	 * @return
	 */
	public boolean deathCheck(){
		if(health <= 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Does appropriate death things
	 */
	public abstract void die();
	
	
}
