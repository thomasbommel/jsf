package app.gui;

import java.awt.Color;
import java.awt.Graphics;

public class MenuPanel extends GUIPanel {

	public MenuPanel(int fps) {
		super(fps);
		this.setBackground(Color.LIGHT_GRAY);
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
