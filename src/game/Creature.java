package game;

/**
 * A Creature is simply a creature in a dungeon
 * @author mattgraf
 *
 */
public class Creature extends Mob{
	public Creature(String name, double row, double col){
		this.name = name;
		this.row = row;
		this.col = col;
		
		imageName = "images/" + name + ".png";
	}

	/**
	 * Kill the creature. Right now all this does is change the image.
	 */
	@Override
	public void die() {
		imageName = "images/dead_" + name + ".png";
		// TODO: remove from activeCreatures
	}
}
