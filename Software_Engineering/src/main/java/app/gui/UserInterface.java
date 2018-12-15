package app.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class UserInterface {

	private JFrame frame;
	private JPanel contentPane;

	public UserInterface() {
		super();
		initUI();
	}

	private void initUI() {
		this.frame = new JFrame("Toy Car Factory v.0.0.0.0.0.1 ;)");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.contentPane = (JPanel) frame.getContentPane();
		contentPane.setBackground(Color.GREEN);
		this.frame.setSize(1200, 800);

		initFactoryPanel();
		initMenuPanel();

		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}

	private void initFactoryPanel() {
		FactoryPanel factoryPanel = new FactoryPanel();
		factoryPanel.setBounds(0, 0, 800, 800);
		contentPane.add(factoryPanel);
	}
	
	private void initMenuPanel() {
		FactoryPanel factoryPanel = new FactoryPanel();
		factoryPanel.setBackground(Color.ORANGE);
		factoryPanel.setBounds(800, 0, 200, 800);
		
		contentPane.add(factoryPanel);
	}
	

}
