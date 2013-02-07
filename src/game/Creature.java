package game;

/**
 * A Creature is simply a creature in a dungeon
 * @author mattgraf
 *
 */
public class Creature extends Mob{
	
	public Creature(String name) {
		this.name = name;
		path = "res/img/creatures/"+name+".png";
	}
	
	public Creature(String name, double row, double col){
		this.name = name;
		path = "res/img/creatures/"+name+".png";
		this.row = row;
		this.col = col;
	}

	/**
	 * Kill the creature. Right now all this does is change the image.
	 */
	@Override
	public void die() {
		path = "images/dead_" + name + ".png";
		// TODO: remove from activeCreatures
	}
}
