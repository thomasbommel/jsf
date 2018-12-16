package factory.shared.interfaces;

import java.awt.Color;
import java.awt.Graphics;
import java.util.function.Consumer;

import factory.shared.Position;

public interface Placeable {

	Position getPosition();
	
	default Consumer<Graphics> drawPlaceable() {//TODO refactor
		return g->{
			//System.out.println("Placeable.getDrawable()");
			g.setColor(Color.BLUE);
			g.drawRect(0, 0, 40, 40);
			g.drawString("getDrawable()", 0, 0);
		};
	};		
	
}
