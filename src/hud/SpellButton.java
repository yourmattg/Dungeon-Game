package hud;

/**
 * SpellButtons behave in two different ways. Either they perform an action upon clicking, 
 *  or they prepare to receive a target for their action.
 */
public class SpellButton extends HudObject {
	
	/**
	 * Constructor: specifies name and coordinates. 
	 *  Automatically determines image file based on name.
	 * @param name
	 * @param x
	 * @param y
	 */
	public SpellButton(String name, int x, int y){
		super(name, x, y);
		this.imageName = "images/spells/icons/" + name + ".png";
		height = 64;
		width = 64;
	}
}
