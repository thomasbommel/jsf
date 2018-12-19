package app.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import factory.shared.interfaces.Placeable;

public class FactoryPanel extends GUIPanel {

	private List<Placeable> objectsToDraw;

	public FactoryPanel(int fps) {
		super(fps);
		this.objectsToDraw = new ArrayList<>();
		this.setBackground(Color.WHITE);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		this.objectsToDraw.forEach(o ->{
			drawPlaceableOnGraphics(g, o);
		});
	}

	private void drawPlaceableOnGraphics(Graphics g, Placeable placeable) {
		int posX = placeable.getPosition().getPositionX();
		int posY = placeable.getPosition().getPositionY();
		
		g.translate(posX, posY);
		placeable.drawPlaceable().accept(g);
		g.translate(-posX,-posY);
	}

	public List<Placeable> getObjectsToDraw() {
		return objectsToDraw;
	}

}
