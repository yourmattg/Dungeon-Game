package actions;

import game.Mob;
import game.Position;
import game.GameController.Direction;

/**
 * A MoveAction moves a mob in a certain direction until they've moved 1 square.
 * @author mattgraf
 *
 */
public class MoveAction extends Action {

	double moveProgress = 0;
	Position center;
	Direction moveDir;
	Mob target;
	
	/**
	 * Constructor with target, move direction, and camera center specified.
	 *  Center should be null if the target is not the player.
	 * @param target
	 * @param moveDir
	 * @param center
	 */
	public MoveAction(Mob target, Direction moveDir, Position center){
		type = ActionType.MOVE;
		this.target = target;
		this.center = center;
		this.moveDir = moveDir;
		
		setTargetDirection();
	}
	
	/**
	 * Sets the target's direction, dx and dy
	 */
	void setTargetDirection(){
		target.dir = moveDir;
		
		if(moveDir == Direction.NORTH){
            target.dx = 0;
            target.dy = 1;
		}
		if(moveDir == Direction.WEST){
            target.dx = -1;
            target.dy = 0;
		}
		if(moveDir == Direction.SOUTH){
            target.dx = 0;
            target.dy = -1;
		}
		if(moveDir == Direction.EAST){
            target.dx = 1;
            target.dy = 0;
		}
	}
	
	/**
	 * Move the target according to how much time has passed. This should repeat until
	 *  the target has moved a full square
	 */
	@Override
	public boolean perform(int delta) {
		// figure out how much progress to make based on time passed
		double amountMoved = delta/target.moveTime;
		
		// if we are about to reach the end of a move cycle,
		// reset move progress & direction
		if(moveProgress + amountMoved >= 1){
			amountMoved = 1-moveProgress;
			moveProgress = 0;
			moveDir = Direction.NONE;
		}
		// increment move progress
		else{
			moveProgress += amountMoved;
		}
		
		// Where the actual movement happens
		target.col += target.dx*amountMoved;
		target.row -= target.dy*amountMoved;
		// If a player, move the center of the camera
		if(target.isPlayer){
			center.col += target.dx*amountMoved;
			center.row -= target.dy*amountMoved;			
		}
		
		// if we finished a move cycle, round the coordinates to the nearest whole
		if(moveProgress == 0){
			target.col = round(center.col);
			target.row = round(center.row);
			if(target.isPlayer){
				center.col = round(center.col);
				center.row = round(center.row);				
			}
			// Reset target dx and dy
			target.dx = 0;
			target.dy = 0;
			
			// We're done, so don't repeat
			return false;
		}
		
		// We're not done yet, so repeat the action
		return true;
	}
	
	/**
	 * Rounds double to nearest whole number
	 * @param d
	 * @return
	 */
	double round(double d){
		return Math.floor(d + 0.5);
	}

}
