package game;

import java.util.ArrayList;

/**
 * A list of all active creatures on the map. Right now "active" just means "alive".
 * @author mattgraf
 *
 */
public class ActiveCreatures {
	public ArrayList<Creature> creatures;
	
	/**
	 * Default constructor: creature an empty creature list
	 */
	public ActiveCreatures(){
		creatures = new ArrayList<Creature>();
	}
	
	/**
	 * Adds a creature to the list
	 * @param creature
	 */
	public void add(Creature creature){
		creatures.add(creature);
	}
	
	/**
	 * Returns a list of all creatures within a given range of position
	 * @param position
	 * @param DIST
	 * @return
	 */
	public ArrayList<Creature> creaturesInRange(Position position, final int DIST){
		ArrayList<Creature> list = new ArrayList<Creature>();
		for(int i=0; i<creatures.size(); i++){
			Creature creature = creatures.get(i);
			if(Math.abs(creature.row - position.row) <= DIST &&
				Math.abs(creature.col - position.col) <= DIST){
					list.add(creature);
				}
		}
		return list;
	}
}
