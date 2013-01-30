package spells;

import actions.Action;
import game.Mob;

/**
 * SpellActions can either happen instantly, or have an effect over time.
 *  For healing spells, the healthChange variable should be a positive value, whereas
 *   offensive spells should have a negative value.
 * @author mattgraf
 *
 */
public class SpellAction extends Action {
	
	public enum SpellType{
		INSTANT, DOT
	}

	public String name;
	public SpellType spellType;
	public int healthChange;
	
	// used for DOT spells
	public int delay;
	public int tempDelay;
	public int iterations;
	
	public boolean shouldRepeat;
	
	/**
	 * Constructs a spell action with a name and type. Automatically sets
	 *  shouldRepeat based on spell type.
	 * @param name
	 * @param type
	 */
	public SpellAction(String name, SpellType type){
		this.name = name;
		this.spellType = type;
		if(type == SpellType.DOT){
			shouldRepeat = true;
		}
		else if(type == SpellType.INSTANT){
			shouldRepeat = false;
		}
	}
	
	/**
	 * Constructor for an instant damage/healing spell
	 * @param target
	 * @param healthChange
	 */
	public SpellAction(Mob target, int healthChange){
		this.spellType = SpellType.INSTANT;
		this.target = target;
		this.healthChange = healthChange;
		shouldRepeat = false;
	}
	
	/**
	 * Constructor for a DOT type spell
	 * @param target
	 * @param healthChange
	 * @param delay
	 * @param iterations
	 */
	public SpellAction(Mob target, int healthChange, int delay, int iterations){
		this.spellType = SpellType.DOT;
		this.target = target;
		this.healthChange = healthChange;
		this.delay = delay;
		tempDelay = 0;
		this.iterations = iterations;
		shouldRepeat = true;
	}
	
	/**
	 * Performs the action for this spell
	 */
	@Override
	public boolean perform(int delta) {
		boolean healthChanged = false;
		
		// If this is an instant, just do the damage and be done with it
		if(spellType == SpellType.INSTANT){
			target.health += healthChange;
			healthChanged = true;
		}
		// If its damage-over-time, do that
		else if(spellType == SpellType.DOT){
			if(damageOverTime(delta)){
				healthChanged = true;
			}
		}
		
		// If we made a change to the target's health, do a death check
		if(healthChanged){
			if(target.deathCheck()){
				target.die();
			}
		}
		
		return shouldRepeat;
	}
	
	/**
	 * Decrements the delay and makes changes to target health if necessary
	 * @param delta
	 * @return true if target's health was changed
	 */
	private boolean damageOverTime(int delta){
		tempDelay -= delta;
		// if our delay is up, do the damage/healing
		if(tempDelay <= 0){
			iterations--;
			target.health += healthChange;
			System.out.println("Health " + healthChange);
			// If this effect is over, don't repeat the action
			if(iterations == 0){
				shouldRepeat = false;
			}
			// Reset delay so the damage/healing can happen again
			else{
				tempDelay = delay;
			}
			return true;
		}
		
		return false;
	}

}
