package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.newdawn.slick.SlickException;

import game.Creature;
import map.Item;
import map.Map;
import map.Tile;

public class EditControl {
	/*
	 * TODO: 
	 * button implementation (insert, move, delete)
	 * asterisk in unsaved map's title
	 * load a stored map, replace Untitled if no changes made
	 * verification
	 */
	static EditView view;
	static List<Map> map = new ArrayList<Map>();
	static List<EditMap> editMap = new ArrayList<EditMap>();
	static String author = "David";
	static List<Item> items;
	static List<Tile> tiles;
	static List<Creature> creatures;
	static enum states {PLACE, MOVEMAP, MOVEITEM, DELETE};
	static int state = 0;
	
	public static void main(String[] args) throws SlickException {
		view = new EditView();
		newMap();
		setupLists();
		setupListeners();
		view.setVisible(true);
	}
	
	public static void setupLists() {
		//connect to the db
		EditRepository.connect();
		//build tiles list
		tiles = EditRepository.getAllTiles();
		view.listTiles.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 7948894173829388724L;
			public int getSize() {
				return tiles.size();
			}
			public String getElementAt(int index) {
				return tiles.get(index).name;
			}
		});
		//build items list
		items = EditRepository.getAllItems();
		view.listItems.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 3503367777071045187L;
			public int getSize() {
				return items.size();
			}
			public String getElementAt(int index) {
				return items.get(index).name;
			}
		});
		//build creatures list
		creatures = EditRepository.getAllCreatures();
		view.listCreatures.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 7300745736010421735L;
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
	}
	
	public static void newMap() {
		//add to list of maps
		map.add(new Map("Untitled", author));
		//give it a tab
		view.dungeonPane.addTab("Untitled", null);
		//add the button
		view.dungeonPane.setTabComponentAt(map.size()-1, new ButtonTabComponent(view.dungeonPane));
		//add a component
		EditMap em = new EditMap();
		editMap.add(em);
		view.dungeonPane.setComponentAt(map.size()-1, em);
	}
	
	public static void openMap() {
		String name = (String)JOptionPane.showInputDialog(
				view, "Enter your map name:", "Load...", JOptionPane.PLAIN_MESSAGE);
		Map load = EditRepository.loadMap(name, author);
		//TODO: finish loading
	}
	
	/**
	 * checks if the map already has a name, then saves
	 */
	public static void saveMap(int index) {
		//check if its been modified
		if(!map.get(index).isSaved) {
			//don't want to save it as untitled
			if(map.get(index).name == "Untitled") {
				saveMapAs(index);
			} else {
				saveResult(index);
			}
		}
	}
	
	/**
	 * lets you specify a new name to save a map as
	 */
	public static void saveMapAs(int index) {
		//request name
		String name = "";
		//don't let them choose an empty name
		while(name.equals("")) {
			//ask for new name
			name = (String)JOptionPane.showInputDialog(
					view, "Enter a map name:", "Save As...", JOptionPane.PLAIN_MESSAGE);
			//cancelled or closed
			if(name == null) {
				return;
			}
		}
		//check if it exists
		if(EditRepository.exists(map.get(index))) {
			//either overwrite or choose a new name
			int answer = JOptionPane.showConfirmDialog(
					view, "Overwrite existing dungeon?", "Dungeon already exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(answer == 1) {
				saveMapAs(index);
				return;
			}
		}
		//set map name
		map.get(index).name = name;
		//update tab label
		view.dungeonPane.setTitleAt(index, name);
		//save
		saveResult(index);
	}
	
	/**
	 * informs of a successful or unsuccessful save
	 */
	public static void saveResult(int index) {
		//saves the map, issues alert
		if(EditRepository.saveMap(map.get(index))) {
			//save success
			JOptionPane.showMessageDialog(
					view, "Save successful", "Save successful", JOptionPane.INFORMATION_MESSAGE);
			//update isSaved
			map.get(index).isSaved = true;
		} else {
			//save error
			JOptionPane.showMessageDialog(
					view, "There was an error while saving your dungeon", "Save error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * prompts save before exit, then closes
	 */
	public static void exit() {
		//check if any maps aren't saved
		for(int i = 0; i < map.size(); i++) {
			if(!map.get(i).isSaved) {
				//show user which tab is being referred to
				view.dungeonPane.setSelectedIndex(i);
				//offer to save
				int answer = JOptionPane.showConfirmDialog(
						view, "Save changes before closing?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if(answer == 0) {
					//save map
					saveMap(view.dungeonPane.getSelectedIndex());
				} else if(answer == 2) {
					//user cancelled
					return;
				}
			}
		}
		//disconnect when were done
		EditRepository.disconnect();
		//exit
		view.dispose();
	}
	
	/**
	 * called by the TabButton when its close button is clicked
	 * @param index
	 */
	public static void closeTab(int index) {
		//make sure it has been saved
		if(!map.get(index).isSaved) {
			int answer = JOptionPane.showConfirmDialog(
					view, "Save changes before closing?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(answer == 0) {
				saveMap(index);
			}
		}
		//remove map from list
		map.remove(index);
		editMap.remove(index);
	}
	
	/**
	 * allows you to search for a creature, tile, or item
	 */
	public static void search() {
		//clear selections
		view.listTiles.clearSelection();
		view.listItems.clearSelection();
		view.listCreatures.clearSelection();
		//get text to compare
		String text = view.txtSearch.getText();
		ListModel<String> model = view.listTiles.getModel();
		int size = model.getSize();
		//search through lists for the first object that matches
		for(int i = 0; i < size; i++) {
			if(model.getElementAt(i).contains(text)) {
				view.listTiles.setSelectedIndex(i);
				return;
			}
		}
		model = view.listItems.getModel();
		size = model.getSize();
		for(int i = 0; i < size; i++) {
			if(model.getElementAt(i).contains(text)) {
				view.listItems.setSelectedIndex(i);
				return;
			}
		}
		model = view.listCreatures.getModel();
		size = model.getSize();
		for(int i = 0; i < size; i++) {
			if(model.getElementAt(i).contains(text)) {
				view.listCreatures.setSelectedIndex(i);
				return;
			}
		}	
	}
	
	public static void addObject(int x, int y) {
		//get dungeon pane index
		int index = view.dungeonPane.getSelectedIndex();
		//get map
		EditMap em = editMap.get(index);
		//change is being made
		map.get(index).isSaved = false;
		//get tile position
		int tileX = em.topLeft.x+((x-em.offX)/64);
		int tileY = em.topLeft.y+((y-em.offY-30)/64);
		//get tile's name
		String name = view.listTiles.getSelectedValue();
		if(name != null) {
			//add to tile list with position
			em.tiles.add(new Tile(0, name, tileX, tileY, null));
		} else if((name = view.listItems.getSelectedValue()) != null) {
			Tile tile = getTile(tileX, tileY);
			if(tile != null)
				tile.stack.push(new Item(0, name, true, true, true));
		} else if((name = view.listCreatures.getSelectedValue()) != null) {
			Tile tile = getTile(tileX, tileY);
			if(tile != null)
				tile.creature = new Creature(name, tileX, tileY);
		}
		//draw it in
		em.repaint();
	}
	
	public static Tile getTile(int x, int y) {
		EditMap em = editMap.get(view.dungeonPane.getSelectedIndex());
		for(Tile tile:em.tiles) {
			if(x == tile.coord.x && y == tile.coord.y) {
				return tile;
			}
		}
		return null;
	}
	
	public static void displayImage(int x, int y) {
		String name;
		//get name
		if((name = view.listTiles.getSelectedValue()) != null) {
			//get map
			EditMap em = editMap.get(view.dungeonPane.getSelectedIndex());
			try {
				//open image
				em.cursorImage = ImageIO.read(new File("res/img/tiles/"+name+".png"));
				//update mouse coordinates
				em.mouseX = x;
				em.mouseY = y;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if((name = view.listItems.getSelectedValue()) != null) {
			EditMap em = editMap.get(view.dungeonPane.getSelectedIndex());
			try {
				//open image
				em.cursorImage = ImageIO.read(new File("res/img/items/"+name+".png"));
				//update mouse coordinates
				em.mouseX = x;
				em.mouseY = y;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if((name = view.listCreatures.getSelectedValue()) != null) {
			EditMap em = editMap.get(view.dungeonPane.getSelectedIndex());
			try {
				//open image
				em.cursorImage = ImageIO.read(new File("res/img/creatures/"+name+".png"));
				//update mouse coordinates
				em.mouseX = x;
				em.mouseY = y;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void removeImage() {
		//get map
		EditMap em = editMap.get(view.dungeonPane.getSelectedIndex());
		//get rid of the cursor image
		em.cursorImage = null;
		//redraw
		em.repaint();
	}
	
	public static void moveImage(int x, int y) {
		//get map
		EditMap em = editMap.get(view.dungeonPane.getSelectedIndex());
		//update mouse coordinates
		em.mouseX = x;
		em.mouseY = y;
		//redraw
		editMap.get(view.dungeonPane.getSelectedIndex()).repaint();
	}
	
	/**
	 * sets up listeners for each object
	 */
	public static void setupListeners() {
		//exit listener
		view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
		
		//menu options
		view.fileNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newMap();
			}
		});
		view.fileOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openMap();
			}
		});
		view.fileSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveMap(view.dungeonPane.getSelectedIndex());
			}
		});
		view.fileSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveMapAs(view.dungeonPane.getSelectedIndex());
			}
		});
		view.fileExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});

		//item pane
		view.txtSearch.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				view.txtSearch.setText("");
			}
			@Override
			public void focusLost(FocusEvent e) {
				view.txtSearch.setText("search...");
			}
		});
		view.txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				search();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}
		});
		view.listTiles.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				view.listItems.clearSelection();
				view.listCreatures.clearSelection();
			}
			@Override
			public void focusLost(FocusEvent e) {}
		});
		view.listItems.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				view.listTiles.clearSelection();
				view.listCreatures.clearSelection();
			}
			@Override
			public void focusLost(FocusEvent e) {}
		});
		view.listCreatures.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				view.listTiles.clearSelection();
				view.listItems.clearSelection();
			}
			@Override
			public void focusLost(FocusEvent e) {}
		});
		
		//dungeon pane
		view.dungeonPane.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addObject(e.getX(), e.getY());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				displayImage(e.getX(), e.getY());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				removeImage();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				//TODO: add icon to show what release will do
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				//TODO: take action from mouseClicked
			}
		});
		view.dungeonPane.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				moveImage(e.getX(), e.getY());
			}
		});
	}
}