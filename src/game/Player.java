package game;

import map.Map;
import game.GameController.Direction;

public class Player extends Mob{
	
	/**
	 * Default constructor: specifies that this is a player, with default movement speed 300ms/square
	 */
	public Player(){
		isPlayer = true;
		moveTime = 300;
	}

	/**
	 * Kills the player.
	 */
	@Override
	public void die() {
		System.out.println("You fucking died");
		// TODO: show menu
	}
	
	/**
	 * Checks if the player can move in the given direction
	 * @param dir
	 * @return
	 */
	public boolean canMove(Map map, Direction dir){
		int tileRow = (int)row;
		int tileCol = (int)col;
		if(dir == Direction.NORTH){
			tileRow--;
		}
		if(dir == Direction.SOUTH){
			tileRow++;
		}
		if(dir == Direction.EAST){
			tileCol++;
		}
		if(dir == Direction.WEST){
			tileCol--;
		}
		return map.tileArray[tileRow][tileCol].walkable;
	}

}
