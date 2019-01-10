package factory.subsystems.agv.interfaces;

import factory.shared.Task;
import factory.subsystems.agv.Forklift;

import java.util.List;

import factory.shared.FactoryEvent;

public interface AgvMonitorInterface{

	void submitTask(Task task);
	
	/**
	 * notifies the monitoring system about the given event 
	 * @param task
	 * @param event
	 */
	void notifyMonitoringSystem(Task task, FactoryEvent event);
	
	/**
	 * returns a list of all forklifts
	 * @return forklifts
	 */
	List<Forklift> getForklifts();
}
