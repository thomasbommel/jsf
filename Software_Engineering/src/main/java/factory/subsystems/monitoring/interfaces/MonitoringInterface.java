package factory.subsystems.monitoring.interfaces;

import java.util.List;

import factory.shared.FactoryEvent;
import factory.shared.enums.SubsystemStatus;
import factory.shared.interfaces.Stoppable;
import factory.subsystems.AbstractSubsystem;

public interface MonitoringInterface extends Stoppable {

	// TODO create methods like addRobotSystem, etc.

	public void handleEvent(FactoryEvent event);

	public void addToSubsystemList(AbstractSubsystem subsystem);

	public SubsystemStatus getStatus();
	
	
	public void setStatus(SubsystemStatus status);//TODO remove

	public void setCurrentSubsystemToShow(AbstractSubsystem subsystem);
	
	public List<AbstractSubsystem> getTestSubSystemList();

}
