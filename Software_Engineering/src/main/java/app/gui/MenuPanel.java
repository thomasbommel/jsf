package app.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.BorderFactory;

public class MenuPanel extends GUIPanel {

	public MenuPanel(int fps) {
		super(fps);
		this.setBackground(Color.YELLOW);
		this.setBorder(BorderFactory.createLineBorder(
				new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)), 1));// TODO remove
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

//		g.drawRect(10, 10, 60, 50);
//		g.fillRect(100, 10, 60, 50);
//		g.drawRoundRect(190, 10, 60, 50, 15, 15);
//		g.fillRoundRect(70, 90, 140, 100, 30, 40);
	}

}
