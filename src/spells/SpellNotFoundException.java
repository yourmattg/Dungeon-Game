package spells;

/**
 * SpellNotFoundException: for when a spell couldn't be found in the spell reference
 * @author mattgraf
 *
 */
public class SpellNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	//Constructor that accepts a message
    public SpellNotFoundException(String spellName){
       super("\"" + spellName + "\" was not be found in the spell reference table");
    }

}
