package map;

import game.Creature;

import java.awt.Point;
import java.util.Stack;

public class Tile {
	
	public Point coord;
	public int id;
	public String name;
	public boolean walkable;
	public Stack<Item> stack = null;
	public String path;
	public Creature creature = null;

	public Tile() {}
	
	public Tile(int id, String name, boolean walkable) {
		this.id = id;
		this.name = name;
		path = "res/img/tiles/"+name+".png";
		this.walkable = walkable;
	}
	
	public Tile(int id, String name, int x, int y, Stack<Item> stack) {
		this.id = id;
		this.name = name;
		path = "res/img/tiles/"+name+".png";
		this.coord = new Point(x, y);
		this.stack = (stack == null) ? new Stack<Item>() : stack;
	}

}
