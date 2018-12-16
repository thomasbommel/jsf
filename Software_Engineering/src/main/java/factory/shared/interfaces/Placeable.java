package factory.shared.interfaces;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import factory.shared.Position;

public interface Placeable {

	Position getPosition();
	
	default JPanel getDrawable() {//TODO refactor
		JPanel panel = new JPanel();
		panel.setBackground(Color.cyan);
		panel.add(new JLabel("default"));
		return panel;
	};		
	
}
