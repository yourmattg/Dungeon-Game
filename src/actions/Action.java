package actions;

import game.Mob;

/**
 * The Action class, in a general sense, represents a "verb"
 * An Action can be a single action, or something that should be repeated each turn
 * @author mattgraf
 *
 */
public abstract class Action {
	
	public enum ActionType{
		MOVE, ATTACK, SPELL
	}
	
	// Performs the action, and returns true if this action should be requeued
	public abstract boolean perform(int delta);
	
	public Mob target;
	public boolean isClickAction;
	public boolean canCollide;
	ActionType type;
}


//import test
