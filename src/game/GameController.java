package game;

import hud.HudList;
import hud.HudObject;

import java.util.ArrayList;
import java.util.LinkedList;

import map.Map;
import map.Tile;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import actions.Action;
import actions.AttackAction;
import actions.MoveAction;
import actions.Action.ActionType;

import spells.SpellReference;

public class GameController extends BasicGame{
	public enum Direction{
		NORTH, SOUTH, EAST, WEST, NONE
	}
	
	static AppGameContainer app;
	final int DRAW_DIST = 4;
	Map map;
	Position center = new Position(5, 6);
	Direction moveDir;
	Player player;
	
	ActiveCreatures activeCreatures;
	HudList hudList;
	LinkedList<Action> actionQueue;
	
	Action clickAction;
	int actionDelay;
	final int ACTION_DELAY = 200;
	
	int[][] testmap = {{1,1,1,1,1,1,1,1,1,1},
					 {1,0,1,0,0,0,0,0,0,1},
					 {1,0,1,0,0,0,0,0,0,1},
					 {1,0,1,0,0,1,0,1,1,1},
					 {1,0,1,0,0,1,0,0,0,1},
					 {1,0,1,0,0,1,0,0,0,1},
					 {1,0,1,0,0,0,0,0,0,1},
					 {1,0,1,1,1,1,0,0,0,1},
					 {1,0,0,0,0,0,0,0,0,1},
					 {1,1,1,1,1,1,1,1,1,1}};
	
	public GameController(String title) {
		super(title);
	}

	/**
	 * @param args
	 * @throws SlickException 
	 */
	public static void main(String[] args) throws SlickException {
        app = new AppGameContainer(new GameController("Dungeon"));

        app.setDisplayMode(768, 704, false);
        app.start();
	}

	@Override
	/**
	 * Draw ALL the things
	 */
	public void render(GameContainer container, Graphics graphics) throws SlickException {
		drawMap();
		drawCreatures();
		drawPlayer();
		drawPanels();
		hudList.drawObjects();
	}

	@Override
	/**
	 * Initializes game container
	 */
	public void init(GameContainer container) throws SlickException {
		createMap();
		actionDelay = 0;
		player = new Player();
		player.row = center.row;
		player.col = center.col;
		actionQueue = new LinkedList<Action>();
		SpellReference.init();
		hudList = new HudList();
		initCreatures();
	}

	/**
	 * Update game logic
	 */
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		
		// If the user is able to make a new action,
		// check for attack/spell actions and reset actionDelay
		if(actionDelay <= 0){
			if(checkUserActions(input, delta)){
				actionDelay = ACTION_DELAY;
			}
		}
		else{
			actionDelay -= delta;
		}
		 
		// If the player isn't moving, check for WASD input
		if(player.dx + player.dy == 0){
			checkDirection(input);
		}
		
		// run all actions for this turn
		runActionQueue(delta);
	}
	
	/**
	 * Checks if a HUD object was clicked, or if a creature was clicked with some sort of targeting action
	 * @param input
	 * @param delta
	 * @throws SlickException
	 */
	public boolean checkUserActions(Input input, int delta) throws SlickException{
		// Check if something on the HUD was clicked
		// that requires an action to be performed
		Action hudAction = hudList.checkForClick(input, actionQueue);
		if(hudAction != null){
			// If this action requires something to be targeted/selected, prepare for it
			if(hudAction.isClickAction){
				// Prepare to cast a spell
				clickAction = hudAction;
				Image image = new Image("images/spells/spellcursor.png");
				app.setMouseCursor(image, 10, 10);
			}
			// Otherwise, we can add this instantaneous action to the queue
			else{
				actionQueue.add(hudAction);
			}
		}
		
		// If we were waiting for something onscreen to be targeted, check all creatures
		if(clickAction != null){
			return checkActionClick(input);
		}
		// Default action is a basic melee attack
		else{
			return checkAttackClick(input);
		}		
	}
	
	/**
	 * Performs all actions currently on the event queue, and enqueues certain actions
	 * @param delta
	 */
	public void runActionQueue(int delta){
		// Only perform actions that were on the queue upon calling,
		// since we will be enqueueing certain actions again
		int count = actionQueue.size();
		while(count > 0){
			Action action = actionQueue.removeFirst();
			if(action.perform(delta)){
				actionQueue.add(action);
			}
			if(action.canCollide){
				// TODO: detect if this object hit anything
			}
			count--;
		}
	}
	
	/**
	 * Checks if an adjacent creature was targeted for a basic melee attack
	 * @param input
	 */
	public boolean checkAttackClick(Input input){
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			double row = input.getMouseY()/(double)64;
			double col = input.getMouseX()/(double)64;
			
			ArrayList<Creature> adjacentCreatures = activeCreatures.creaturesInRange(center, 1);
			for(Creature creature : adjacentCreatures){
				double screenRow = ((creature.row - center.row) + DRAW_DIST);
				double screenCol = ((creature.col - center.col) + DRAW_DIST);
				if(row >= screenRow && row <= screenRow+1 &&
					col >= screenCol && col <= screenCol+1){
					// queue attack action
					AttackAction attackAction = new AttackAction(creature, 10);
					actionQueue.add(attackAction);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if creatures in range were targeted for a spell
	 * @param input
	 */
	public boolean checkActionClick(Input input){
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			double row = input.getMouseY()/(double)64;
			double col = input.getMouseX()/(double)64;
			
			ArrayList<Creature> adjacentCreatures = activeCreatures.creaturesInRange(center, DRAW_DIST);
			for(Creature creature : adjacentCreatures){
				double screenRow = ((creature.row - center.row) + DRAW_DIST);
				double screenCol = ((creature.col - center.col) + DRAW_DIST);
				if(row >= screenRow && row <= screenRow+1 &&
					col >= screenCol && col <= screenCol+1){
					// queue spell? action
					clickAction.target = creature;
					actionQueue.add(clickAction);
					clickAction = null;
					app.setDefaultMouseCursor();
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Changes player movement based on keyboard input
	 * @param input
	 */
	public void checkDirection(Input input){
        if(input.isKeyDown(Input.KEY_W) && player.canMove(map, Direction.NORTH)){
            MoveAction moveAction = new MoveAction(player, Direction.NORTH, center);
            actionQueue.add(moveAction);
        }
 
        else if(input.isKeyDown(Input.KEY_A) && player.canMove(map, Direction.WEST)){
            MoveAction moveAction = new MoveAction(player, Direction.WEST, center);
            actionQueue.add(moveAction);
        }
 
        else if(input.isKeyDown(Input.KEY_S) && player.canMove(map, Direction.SOUTH)){
            MoveAction moveAction = new MoveAction(player, Direction.SOUTH, center);
            actionQueue.add(moveAction);
        }
        
        else if(input.isKeyDown(Input.KEY_D) && player.canMove(map, Direction.EAST)){
            MoveAction moveAction = new MoveAction(player, Direction.EAST, center);
            actionQueue.add(moveAction);
        }		
	}
	
	/**
	 * Draws the region of the map in sight
	 */
	public void drawMap(){
		for(int row=-DRAW_DIST; row<=DRAW_DIST+1; row++){
			for(int col=-DRAW_DIST; col<=DRAW_DIST+1; col++){
				// get integer coordinates for this tile
				int tileRow = (int)center.row + row;
				int tileCol = (int)center.col + col;
				
				// get the x and y pixel offset
				int rowOffset = (int)((center.row - (int)center.row)*64);
				int colOffset = (int)((center.col - (int)center.col)*64);
				
				// if this tile is out of bounds, don't attempt to draw
				if(tileRow < 0 || tileCol < 0 || tileRow >= 10 || tileCol >= 10){
					continue;
				}
				
				// get and draw tile
				Tile tile = map.tileArray[tileRow][tileCol];
				try {
					Image tileImage = new Image(tile.imageName);
					int screenRow = row + DRAW_DIST;
					int screenCol = col + DRAW_DIST;
					tileImage.draw(64*screenCol-colOffset, 64*screenRow-rowOffset);
					
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Draws all creatures within sight
	 */
	public void drawCreatures(){
		ArrayList<Creature> nearbyCreatures = activeCreatures.creaturesInRange(center, DRAW_DIST+1);	
		for(Creature creature : nearbyCreatures){
			// get the x and y pixel offset
			int rowOffset = (int)((center.row - (int)center.row)*64);
			int colOffset = (int)((center.col - (int)center.col)*64);
			
			// draw creature
			try {
				Image creatureImage = new Image(creature.imageName);
				int screenRow = (int) ((creature.row - (int)center.row) + DRAW_DIST);
				int screenCol = (int) ((creature.col - (int)center.col) + DRAW_DIST);
				creatureImage.draw(64*screenCol-colOffset, 64*screenRow-rowOffset);
				
			} catch (SlickException e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * Draws the player in the center of the screen
	 */
	public void drawPlayer(){
		Image playerImage;
		try {
			playerImage = new Image("images/person.png");
			if(player.dir == Direction.SOUTH){
				playerImage.rotate(180);
			}
			if(player.dir == Direction.WEST){
				playerImage.rotate(270);
			}
			if(player.dir == Direction.EAST){
				playerImage.rotate(90);
			}
			playerImage.draw(64*DRAW_DIST, 64*DRAW_DIST);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Draws the HUD panels
	 */
	public void drawPanels(){
		try {
			Image rightPanel = new Image("images/right_panel.png");
			rightPanel.draw(576, 0);
			
			Image bottomPanel = new Image("images/bottom_panel.png");
			bottomPanel.draw(0, 576);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the map tile array
	 */
	public void createMap(){
		map = new Map();
		map.tileArray = new Tile[10][10];
		for(int row=0; row<10; row++){
			for(int col=0; col<10; col++){
				int type = testmap[row][col];
				Tile tile = new Tile();
				if(type == 1){
					tile.walkable = false;
					tile.imageName = "images/wall.png";
				}
				if(type == 0){
					tile.walkable = true;
					tile.imageName = "images/floor.png";
				}
				map.tileArray[row][col] = tile;
			}
		}
	}
	
	/**
	 * Initializes the creatures on the map
	 */
	public void initCreatures(){
		activeCreatures = new ActiveCreatures();
		
		Creature troll = new Creature("Troll", 1, 1);
		activeCreatures.add(troll);
		
		troll = new Creature("Troll", 1, 5);
		activeCreatures.add(troll);
		
		troll = new Creature("Troll", 3, 3);
		activeCreatures.add(troll);

		troll = new Creature("Troll", 4, 7);
		activeCreatures.add(troll);
		
		troll = new Creature("Troll", 8, 8);
		activeCreatures.add(troll);
	}
	
}
