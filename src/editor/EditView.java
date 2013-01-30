package editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class EditView extends JFrame {
	private static final long serialVersionUID = 4842780960258969211L;
	
	public JMenuBar menuBar;
		public JMenu menuFile;
			public JMenuItem fileNew;
			public JMenuItem fileOpen;
			public JMenuItem fileSave;
			public JMenuItem fileSaveAs;
			public JMenuItem fileExit;
	
	public JPanel contentPane;
		public JToolBar toolBar;
			public JButton btnMove;
			public JButton btnInsert;
			public JButton btnDelete;
		public JSplitPane splitPane;
			public JPanel itemPane;
				public JTextField txtSearch;
				public JList<String> listTiles;
				public JList<String> listItems;
				public JList<String> listCreatures;
			public JTabbedPane dungeonPane;

	/**
	 * Create the frame.
	 */
	public EditView() {
		setTitle("Dungeon Editor");
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		createMenu();
		createContentPane();
		createToolBar();
		createItemPane();
		createDungeonPane();
		createSplitPane();
	}
	
	private void createMenu() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);
		
		fileNew = new JMenuItem("New");
		fileNew.setMnemonic(KeyEvent.VK_N);
		fileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		menuFile.add(fileNew);
		
		fileOpen = new JMenuItem("Open");
		fileOpen.setMnemonic(KeyEvent.VK_O);
		fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menuFile.add(fileOpen);
		
		menuFile.addSeparator();
		
		fileSave = new JMenuItem("Save");
		fileSave.setMnemonic(KeyEvent.VK_S);
		fileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuFile.add(fileSave);
		
		fileSaveAs = new JMenuItem("Save As...");
		fileSaveAs.setMnemonic(KeyEvent.VK_A);
		fileSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		menuFile.add(fileSaveAs);
		
		menuFile.addSeparator();
		
		fileExit = new JMenuItem("Exit");
		fileExit.setMnemonic(KeyEvent.VK_X);
		fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		menuFile.add(fileExit);
	}
	
	private void createContentPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	private void createToolBar() {
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		btnMove = new JButton("Move");
		toolBar.add(btnMove);
		
		btnInsert = new JButton("Insert");
		toolBar.add(btnInsert);
		
		btnDelete = new JButton("Delete");
		toolBar.add(btnDelete);
	}
	
	private void createItemPane() {
		itemPane = new JPanel();
		itemPane.setBorder(new EmptyBorder(0, 10, 0, 10));
		itemPane.setMinimumSize(new Dimension(150, 10));
		itemPane.setLayout(new BoxLayout(itemPane, BoxLayout.Y_AXIS));
		
		Component verticalGlue0 = Box.createVerticalGlue();
		verticalGlue0.setMaximumSize(new Dimension(0, 8000));
		itemPane.add(verticalGlue0);
		
		txtSearch = new JTextField();
		txtSearch.setMaximumSize(new Dimension(2147483647, 25));
		txtSearch.setAlignmentY(Component.TOP_ALIGNMENT);
		itemPane.add(txtSearch);
		txtSearch.setText("search...");
		txtSearch.setColumns(10);
		
		Component verticalGlue1 = Box.createVerticalGlue();
		verticalGlue1.setMaximumSize(new Dimension(0, 8000));
		itemPane.add(verticalGlue1);
		
		JLabel lblTiles = new JLabel("Tiles");
		lblTiles.setAlignmentY(Component.TOP_ALIGNMENT);
		lblTiles.setAlignmentX(Component.CENTER_ALIGNMENT);
		itemPane.add(lblTiles);
		
		listTiles = new JList<String>();
		listTiles.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listTiles.setFixedCellWidth(400);
		listTiles.setPreferredSize(new Dimension(150, 150));
		listTiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemPane.add(listTiles);
		
		itemPane.add(Box.createVerticalGlue());
		
		JLabel lblItems = new JLabel("Items");
		lblItems.setAlignmentY(Component.TOP_ALIGNMENT);
		lblItems.setAlignmentX(Component.CENTER_ALIGNMENT);
		itemPane.add(lblItems);
		
		listItems = new JList<String>();
		listItems.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listItems.setFixedCellWidth(400);
		listItems.setPreferredSize(new Dimension(150, 150));
		listItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemPane.add(listItems);
		
		itemPane.add(Box.createVerticalGlue());
		
		JLabel lblCreatures = new JLabel("Creatures");
		lblCreatures.setAlignmentY(Component.TOP_ALIGNMENT);
		lblCreatures.setAlignmentX(Component.CENTER_ALIGNMENT);
		itemPane.add(lblCreatures);
		
		listCreatures = new JList<String>();
		listCreatures.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listCreatures.setFixedCellWidth(400);
		listCreatures.setPreferredSize(new Dimension(150, 150));
		listCreatures.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemPane.add(listCreatures);
		
		itemPane.add(Box.createVerticalGlue());
	}
	
	private void createDungeonPane() {
		dungeonPane = new JTabbedPane(JTabbedPane.TOP);
	}
	
	private void createSplitPane() {
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, itemPane, dungeonPane);
		splitPane.setDividerLocation(250);	
		contentPane.add(splitPane, BorderLayout.CENTER);
	}
}