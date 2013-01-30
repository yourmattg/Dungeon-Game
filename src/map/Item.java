package map;

public class Item {
	public final int id;
	public final String name;
	public final boolean walkable;
	public final boolean moveable;
	public final boolean obtainable;
	
	public Item(int id, String name, boolean walkable, boolean moveable, boolean obtainable) {
		this.id = id;
		this.name = name;
		this.walkable = walkable;
		this.moveable = moveable;
		this.obtainable = obtainable;
	}

}