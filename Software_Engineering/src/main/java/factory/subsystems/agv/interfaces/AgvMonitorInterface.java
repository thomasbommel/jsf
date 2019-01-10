package factory.subsystems.agv.interfaces;

import factory.shared.Task;
import factory.shared.interfaces.Monitorable;
import factory.subsystems.agv.Forklift;

import java.util.List;

import factory.shared.FactoryEvent;

public interface AgvMonitorInterface extends Monitorable{

	void submitTask(Task task);
	
	Task getCurrentTask();
	
	
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
