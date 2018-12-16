package app.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import factory.shared.interfaces.Stoppable;

public class UserInterface implements Stoppable {

	private int fps;

	private JFrame frame;
	private JPanel contentPane;

	private FactoryPanel factoryPanel;
	private MenuPanel menuPanel;

	private JPanel menuBar;

	public UserInterface(int fps) {
		super();
		this.fps = fps;
		initUI();
	}

	public void start() {
		this.frame.setVisible(true);
	}

	public void stop() {
		// TODO Auto-generated method stub
	}

	private void initUI() {
		this.frame = new JFrame("Toy Car Factory v.0.0.0.0.0.1 ;)");
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.contentPane = (JPanel) frame.getContentPane();
		this.contentPane.setBackground(Color.GREEN);
		this.contentPane.setLayout(null);
		this.frame.setSize(1200, 800);

		initFactoryPanel();
		initDefaultMenuPanel();

		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
	}

	private void initMenuBar() {
		this.menuBar = new JPanel();
		this.menuBar.setBounds(0, 0, 800, 50);
		this.contentPane.add(this.menuBar);
	}
	
	private void initFactoryPanel() {
		this.factoryPanel = new FactoryPanel(this.fps);
		this.factoryPanel.setBounds(0, 50, 800, 750);
		this.contentPane.add(this.factoryPanel);
	}

	private void initDefaultMenuPanel() {
		this.menuPanel = new MenuPanel(this.fps);
		this.menuPanel.setBackground(Color.ORANGE);
		this.menuPanel.setBounds(800, 0, 400, 800);
		this.contentPane.add(menuPanel);
	}

	public FactoryPanel getFactoryPanel() {
		return factoryPanel;
	}

	public void setFactoryPanel(FactoryPanel factoryPanel) {
		this.factoryPanel = factoryPanel;
	}

	public MenuPanel getMenuPanel() {
		return menuPanel;
	}

	public void setMenuPanel(MenuPanel menuPanel) {
		this.menuPanel = menuPanel;
	}

}
