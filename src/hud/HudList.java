package hud;

import java.util.ArrayList;
import java.util.LinkedList;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import actions.Action;

import spells.SpellReference;

/**
 * HudList keeps track of all objects on the player's HUD (Heads Up Display)
 * @author mattgraf
 *
 */
public class HudList {
	ArrayList<HudObject> objects;

	/**
	 * Default constructor: creates a new HudList with an incinerate spell
	 */
	public HudList(){
		objects = new ArrayList<HudObject>();
		SpellButton incinerate = new SpellButton("Incinerate", 0, 576);
		incinerate.clickAction = SpellReference.spellActionForName("Incinerate");
		objects.add(incinerate);
	}
	
	/**
	 * Checks if this was a mouse click, and returns an Action if there is one to be performed
	 * @param input
	 * @param actionQueue
	 * @return
	 */
	public Action checkForClick(Input input, LinkedList<Action> actionQueue){
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON) || input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
			Action action = checkObjects(input, actionQueue);
			return action;
		}
		return null;
	}
	
	/**
	 * Checks if an object was clicked, and returns the Action of the clicked object
	 * @param input
	 * @param actionQueue
	 * @return
	 */
	public Action checkObjects(Input input, LinkedList<Action> actionQueue){
		for(HudObject hudObject : objects){
			if(hudObject.wasClicked(input) != null){
				return hudObject.clickAction;
			}
		}		
		return null;
	}
	
	/**
	 * Draws each HUD object on the screen
	 */
	public void drawObjects(){
		for(HudObject object : objects){
			try {
				Image hudImage = new Image(object.imageName);
				hudImage.draw(object.screenX, object.screenY);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			
		}		
	}
}
