package factory.shared.interfaces;

import app.gui.SubsystemMenu;
import factory.shared.FactoryEvent;
import factory.shared.enums.SubsystemStatus;

public interface Monitorable extends Placeable, Stoppable{

	String getName();
	
	void notify(FactoryEvent event);

	SubsystemStatus getStatus();

	boolean isReady();
	
	SubsystemMenu getCurrentSubsystemMenu();

}
