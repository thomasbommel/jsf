package factory.subsystems.agv.interfaces;

import factory.shared.Task;
import factory.shared.interfaces.Monitorable;

public interface AgvMonitorInterface extends Monitorable{

	void submitTask(Task task);
	
	Task getCurrentTask();
	
	
	
}
