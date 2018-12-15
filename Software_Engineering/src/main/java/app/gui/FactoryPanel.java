package app.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class FactoryPanel extends JPanel {

	public FactoryPanel() {
		this.setBackground(Color.YELLOW);
		this.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawRect(10, 10, 60, 50);
		g.fillRect(100, 10, 60, 50);
		g.drawRoundRect(190, 10, 60, 50, 15, 15);
		g.fillRoundRect(70, 90, 140, 100, 30, 40);
	}

}
