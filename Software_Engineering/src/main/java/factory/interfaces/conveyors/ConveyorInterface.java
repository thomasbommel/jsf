package factory.interfaces.conveyors;

import factory.model.Task;

public interface ConveyorInterface {

	OilStatusInterface getOilStatus();
	
	void addOil(double amount);
	
	/**
	 * moves the conveyor belt forward
	 */
	void advance();
	
	
	/**
	 * @return true if all the robots are ready, false otherwise
	 */
	boolean isReady();
	
	//TODO type of speed?
	void setSpeed(double speed);
	double getSpeed();
	
	
	/**
	 * notifies the monitoring system about the given event 
	 * @param task
	 * @param event
	 */
	void notifyMonitoringSystem(Task task, ConveyorEvent event);
	
}
