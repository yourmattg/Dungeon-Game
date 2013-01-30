package map;

import java.util.ArrayList;
import java.util.List;

public class Map {

	public String name = null;
	public String author = null;
	public List<Tile> tiles = new ArrayList<Tile>();
	public boolean isSaved = false;
	public Tile[][] tileArray;
	
	public Map(String name, String author) {
		this.name = name;
		this.author = author;
	}

	public Map() {}
}
