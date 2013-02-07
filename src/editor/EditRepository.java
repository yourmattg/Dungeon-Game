package editor;

import game.Creature;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import map.Item;
import map.Map;
import map.Tile;

import database.DatabaseAccessor;

public class EditRepository extends DatabaseAccessor {
	
	/**
	 * Returns true if the given map exists in the database
	 * @param map
	 * @return
	 */
	public static boolean exists(Map map){
		// Query the database to see if this map exists
		/*try {
			Statement stmt = conn.createStatement();
			String query = "SELECT name FROM maps WHERE name = '" + map.name + "' AND author = '" + map.author + "'";
			ResultSet res = stmt.executeQuery(query);
			// If a row exists, that means we found a map with this name
			if(res.next()){
				return true;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}*/
		return false;
	}
	
	/**
	 * Saves the map to the database
	 * @param map
	 * @return success
	 */
	public static boolean saveMap(Map map){
		// Serialize the shit out of the tile list
		String tileData = serializeTileList(map.tiles);
		
		// Send a query to store this map in the database
		return true;
		/*try {
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO maps (author, name, tiles) VALUES('";
			query += map.author + "', '" + map.name + "', '" + tileData + "')";
			System.out.println(query);
			stmt.executeUpdate(query);
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}*/		
	}
	
	/**
	 * Loads the map from the database and returns it as a Map object
	 * @param name
	 * @return
	 */
	public static Map loadMap(String name, String author){
		// Query the database for this particular map
		/*try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM maps WHERE name = '" + name + "' AND author = '" + author + "'";
			System.out.println(query);
			ResultSet res = stmt.executeQuery(query);
			// If a row exists, that means we found a map with this name/author
			if(res.next()){
				Map map = new Map(res.getString("name"), res.getString("author"));
				map.tiles = parseTileList(res.getString("tiles"));
				return map;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}*/
		return null;
	}
	
	/**
	 * Returns all items in the database
	 * @return
	 */
	public static ArrayList<Item> getAllItems(){
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(new Item(0, "shovel", true, true, true));
		// Get all items from the database
		/*try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM items";
			ResultSet res = stmt.executeQuery(query);
			
			// Create item for each row in database
			while(res.next()){
				// get all attributes
				int id = res.getInt("id");
				String name = res.getString("name");
				String imageName = res.getString("image");
				boolean walkable = res.getBoolean("walkable");
				boolean movable = res.getBoolean("movable");
				boolean obtainable = res.getBoolean("obtainable");
				
				// add new item to the list
				Item item = new Item(id, name, walkable, movable, obtainable);
				items.add(item);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}*/
		
		return items;
	}
	
	/**
	 * Returns a list of all tiles in the database
	 * @return
	 */
	public static ArrayList<Tile> getAllTiles(){
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(new Tile(0, "wall", false));
		tiles.add(new Tile(1, "floor", true));
		
		// Get all items from the database
		/*try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM tiles";
			ResultSet res = stmt.executeQuery(query);
			
			// Create tile for each row in database
			while(res.next()){
				Tile tile = new Tile();
				// get all attributes
				tile.id = res.getInt("id");
				tile.name = res.getString("name");
				tile.imageName = res.getString("image");
				tile.walkable = res.getBoolean("walkable");
				
				// add tile to list
				tiles.add(tile);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}*/
		
		return tiles;		
	}
	
	public static ArrayList<Creature> getAllCreatures(){
		ArrayList<Creature> creatures = new ArrayList<Creature>();
		creatures.add(new Creature("Troll"));
		return creatures;
	}
	
	
	/**
	 * Serialization functions (Object -> Database)
	 */
	
	
	/**
	 * Converts a Tile list to a JSON formatted string
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String serializeTileList(List<Tile> list){
		JSONArray array = new JSONArray();
		for(Tile tile : list){
			JSONObject jsonTile = serializeTile(tile);
			array.add(jsonTile);
		}
		return array.toString();
	}
	
	/**
	 * Convert a Tile to a JSON object
	 * @param tile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject serializeTile(Tile tile){
		JSONObject jsonTile = new JSONObject();
		
		// add id and coordinates
		jsonTile.put("x", tile.coord.x);
		jsonTile.put("y", tile.coord.y);
		jsonTile.put("id", tile.id);
		
		// serialize item stack
		jsonTile.put("stack", serializeItemStack(tile.stack));
		
		return jsonTile;
	}
	
	/**
	 * Takes a Item stack and returns a JSON string of item id's
	 * @param stack
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String serializeItemStack(Stack<Item> stack){
		JSONArray array = new JSONArray();
		for(Item item: stack){
			array.add(item.id);
		}
		return array.toString();
	}
	
	
	
	/**
	 * Parsing functions (Database -> Object)
	 */
	
	
	/**
	 * Parses a JSON array of Tiles and returns an ArrayList of Tiles
	 * @param json
	 * @return
	 */
	private static ArrayList<Tile> parseTileList(String json){
		ArrayList<Tile> tileList = new ArrayList<Tile>();

		// Get JSON text as array object
		JSONArray array = null;
		// Add a Tile object for each tile json object
		for(int i=0; i < array.size(); i++){
			JSONObject object = (JSONObject) array.get(i);
			Tile tile = parseTileJSON(object);
			tileList.add(tile);
		}
		
		return tileList;
	}
	
	/**
	 * Converts a JSON object into a Tile
	 * @param object
	 * @return
	 */
	private static Tile parseTileJSON(JSONObject object){
		Tile tile = null;
		// Get id, x, and y attributes
		int id = (int) object.get("id");
		int x = (int) object.get("x");
		int y = (int) object.get("y");
		String name = (String) object.get("name");
		
		// Recreate item stack
		Stack<Item> stack = new Stack<Item>();
		JSONArray stackArray = (JSONArray) object.get("stack");
		// push items in reverse order
		for(int i=stackArray.size()-1; i >= 0; i--){
			int itemId = (int) stackArray.get(i);
			Item item = itemForId(itemId);
			stack.push(item);
		}
		
		tile = new Tile(id, name, x, y, stack);

		return tile;
	}
	
	/**
	 * Recreates the item from an id, using information from the items table
	 * @param id
	 * @return
	 */
	private static Item itemForId(final int id){
		// Create item to return
		Item item = null;
		
		// Send query for this id in the items table
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM items WHERE id = " + id;
			ResultSet res = stmt.executeQuery(query);
			// If a row exists, that means we found a matching id
			if(res.next()){
				// copy attributes from table
				String name = res.getString("name");
				String imageName = res.getString("image");
				boolean walkable = res.getBoolean("walkable");
				boolean obtainable = res.getBoolean("obtainable");
				boolean movable = res.getBoolean("movable");
				
				item = new Item(id, name, walkable, movable, obtainable);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return item;
	}
	
}