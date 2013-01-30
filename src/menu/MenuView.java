package menu;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class MenuView extends JFrame {
	
	private JButton playButton;
	private JButton createButton;
	private JButton logoutButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuView frame = new MenuView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MenuView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);
		setMinimumSize(new Dimension(300, 300));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
	
		initLabels();
		initButtons();
	}
	
	/**
	 * Initializes label and adds some spacing
	 */
	public void initLabels(){
		Component verticalGlue = Box.createVerticalGlue();
		getContentPane().add(verticalGlue);
		
		JLabel welcomeLabel = new JLabel("Welcome, Matt!");
		welcomeLabel.setAlignmentX(0.5f);
		getContentPane().add(welcomeLabel);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		getContentPane().add(verticalStrut);		
	}
	
	/**
	 * Creates Play, Create, and Logout buttons, with spacing between them
	 */
	public void initButtons(){
		playButton = new JButton("Play");
		playButton.setAlignmentX(0.5f);
		playButton.setPreferredSize(new Dimension(200, 40));
		playButton.setMaximumSize(new Dimension(200, 40));
		getContentPane().add(playButton);
		
		Component verticalStrut1 = Box.createVerticalStrut(20);
		getContentPane().add(verticalStrut1);
		
		createButton = new JButton("Create");
		createButton.setAlignmentX(0.5f);
		createButton.setPreferredSize(new Dimension(200, 40));
		createButton.setMaximumSize(new Dimension(200, 40));
		getContentPane().add(createButton);
		
		Component verticalStrut2 = Box.createVerticalStrut(20);
		getContentPane().add(verticalStrut2);
		
		logoutButton = new JButton("Logout");
		logoutButton.setAlignmentX(0.5f);
		logoutButton.setPreferredSize(new Dimension(200, 40));
		logoutButton.setMaximumSize(new Dimension(200, 40));
		getContentPane().add(logoutButton);
		
		Component verticalGlue1 = Box.createVerticalGlue();
		getContentPane().add(verticalGlue1);
	}
}
