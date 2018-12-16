package factory.subsystems.monitoring.interfaces;

import factory.shared.FactoryEvent;
import factory.shared.interfaces.Stoppable;
import factory.subsystems.AbstractSubsystem;

public interface MonitoringInterface extends Stoppable{

	//TODO create methods like addRobotSystem, etc.
	
	public void handleEvent(FactoryEvent event);

	public void addToSubsystemList(AbstractSubsystem subsystem);
	
	
}
