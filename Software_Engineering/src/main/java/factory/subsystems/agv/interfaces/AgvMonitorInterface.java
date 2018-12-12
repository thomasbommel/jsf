package factory.subsystems.agv.interfaces;

import factory.shared.Task;
import factory.shared.FactoryEvent;
import factory.shared.Position;

public interface AgvMonitorInterface{

	void submitTask(Task task);
	
	Task getCurrentTask();
	
	Position getPosition();
	
	/**
	 * notifies the monitoring system about the given event 
	 * @param task
	 * @param event
	 */
	void notifyMonitoringSystem(Task task, FactoryEvent event);
}
