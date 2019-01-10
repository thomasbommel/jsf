package factory.subsystems.monitoring;

import factory.shared.FactoryEvent;
import factory.shared.enums.EventKind;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class MonitoringSystem implements MonitoringInterface {

	@Override
	public void handleEvent(FactoryEvent event) {
		// TODO Auto-generated method stub
		if (event.getKind() == EventKind.WAREHOUSE_TASK_COMPLETED) {
			System.out.println("Task done!");
		}
	}

}
