package factory.subsystems.conveyors.interfaces;

public interface ConveyorMonitorInterface {

	int getOilStatus();
	
	void addOil(int amount);
	
	/**
	 * moves the conveyor belt forward
	 */
	void advance();
	
	//TODO type of speed?
	void setSpeed(double speed);
	double getSpeed();
	
}
