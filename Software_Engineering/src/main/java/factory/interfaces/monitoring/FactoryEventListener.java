package factory.interfaces.monitoring;

import factory.interfaces.agv.AgvEvent;
import factory.interfaces.conveyors.ConveyorEvent;
import factory.interfaces.robotarms.RobotEvent;
import factory.interfaces.warehouse.WarehouseEvent;
import factory.model.Task;

public interface FactoryEventListener {

	void notify(Task task, WarehouseEvent warehouseEvent);
	
	void notify(Task task, AgvEvent agvEvent);
	
	void notify(Task task, ConveyorEvent conveyorEvent);
	
	void notify(Task task, RobotEvent robotEvent);
	
}
