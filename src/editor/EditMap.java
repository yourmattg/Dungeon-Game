package editor;

import map.Item;
import map.Tile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * display component for the map editor
 * @author David
 *
 */
public class EditMap extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public List<Tile> tiles = new ArrayList<Tile>();
	public Point topLeft = new Point(0, 0);
	public Point bottomRight = new Point();
	public Image cursorImage = null;
	public int mouseX = 0;
	public int mouseY = 0;
	public int offX = 0;
	public int offY = 0;

	/**
	 * does the drawing
	 */
	public void paintComponent(Graphics g) {
		//paint it
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//use blue for grid
		g2.setColor(Color.blue);
		//get the size
		Dimension size = getSize();
		//get number of squares that will fit
		int sizeX = size.width/64;
		int sizeY = size.height/64;
		//get coordinates of bottom right tile
		bottomRight.x = topLeft.x+sizeX;
		bottomRight.y = topLeft.y+sizeY;
		//get the offset (empty space) size
		offX = (size.width-(sizeX*64))/2;
		offY = (size.height-(sizeY*64))/2;
		//draw the grid
		for(int i = offX; i < sizeX*64; i+=64) {
			for(int j = offY; j < sizeY*64; j+=64) {
				g2.drawLine(i, j, i+63, j);
				g2.drawLine(i, j, i, j+63);
				g2.drawLine(i+63, j, i+63, j+63);
				g2.drawLine(i, j+63, i+63, j+63);
			}
		}
		//number of tiles
		for(Tile tile: tiles) {
			//make sure tile is in view
			if(tile.coord.x >= topLeft.x && tile.coord.x < bottomRight.x &&
					tile.coord.y >= topLeft.y && tile.coord.y < bottomRight.y) {
				//get coordinates of corner of tile
				int dx1 = offX+(64*(tile.coord.x - topLeft.x));
				int dy1 = offY+(64*(tile.coord.y - topLeft.y));
				//get the image and draw it
				try {
					Image image = ImageIO.read(new File(tile.path));
					g2.drawImage(image, dx1, dy1, 64, 64, null);
					for(int i = 0; i < tile.stack.size(); i++) {
						Item top = tile.stack.get(i);
						image = ImageIO.read(new File(top.path));
						g2.drawImage(image, dx1, dy1, 64, 64, null);
					}
					if(tile.creature != null) {
						image = ImageIO.read(new File(tile.creature.path));
						g2.drawImage(image, dx1, dy1, 64, 64, null);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
		//draw hover image
		if(cursorImage != null) {
			g2.drawImage(cursorImage, mouseX-32, mouseY-64, 64, 64, null);
		}
	}
}