package hud;


import org.newdawn.slick.Input;

import actions.Action;

/**
 * An object that shows up on the HUD
 * @author mattgraf
 *
 */
public abstract class HudObject {
	public String name;
	public String imageName;
	
	public int screenX;
	public int screenY;
	
	public int width;
	public int height;
	
	public Action clickAction;
	
	/**
	 * Constructor: specifies a name and screen coordinates
	 * @param name
	 * @param x
	 * @param y
	 */
	public HudObject(String name, int x, int y){
		this.name = name;
		screenX = x;
		screenY = y;
	}
	
	/**
	 * Returns this object's action if it was clicked
	 * @param input
	 * @return
	 */
	public Action wasClicked(Input input){
		int x = input.getMouseX();
		int y = input.getMouseY();
		if(x >= screenX && x <= screenX+width && y >= screenY && y <= screenY+height)
			return clickAction;
		
		return null;
	}
}
