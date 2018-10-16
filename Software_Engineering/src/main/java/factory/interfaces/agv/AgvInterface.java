package factory.interfaces.agv;

import factory.model.Position;
import factory.model.Task;

public interface AgvInterface{

	void submitTask(Task task);
	
	Task getCurrentTask();
	
	Position getPosition();
	
	/**
	 * notifies the monitoring system about the given event 
	 * @param task
	 * @param event
	 */
	void notifyMonitoringSystem(Task task, AgvEvent event);
}
