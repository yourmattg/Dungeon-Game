package map;

import java.awt.Point;
import java.util.Stack;

public class Tile {
	
	public Point coord;
	public int id;
	public String name;
	public boolean walkable;
	public Stack<Item> stack;
	public String imageName;

	public Tile() {}
	
	public Tile(int id, String name, int x, int y, Stack<Item> stack) {
		this.id = id;
		this.name = name;
		this.coord = new Point(x, y);
		this.stack = stack;
	}

}
