package spells;

import java.util.Hashtable;

import static spells.SpellAction.SpellType.*;

/**
 * SpellReference contains information about all spells
 * @author mattgraf
 *
 */
public class SpellReference {
	private static Hashtable<String, SpellAction> spellTable;
	
	/**
	 * Adds spells to the spell reference
	 */
	public static void init(){
		spellTable = new Hashtable<String, SpellAction>();
		
		// Incinerate: 24 damage over 4 seconds
		SpellAction action = new SpellAction("Incinerate", DOT);
		action.delay = 500;
		action.healthChange = -3;
		action.iterations = 8;
		action.isClickAction = true;
		spellTable.put(action.name, action);
		
		// Heal: heals 10 health over 1.6 seconds
		action = new SpellAction("Heal", DOT);
		action.delay = 333;
		action.healthChange = 2;
		action.iterations = 5;
		action.isClickAction = true;
		spellTable.put(action.name, action);
	}
	
	/**
	 * Returns a SpellAction for the given name, null if no spell was found
	 * @param name
	 * @return
	 */
	public static SpellAction spellActionForName(String name){
		SpellAction action = spellTable.get(name);
		if(action != null){
			return action;
		}
		
		return null;
	}
}
