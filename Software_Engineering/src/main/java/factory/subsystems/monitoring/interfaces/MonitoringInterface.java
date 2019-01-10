package factory.subsystems.monitoring.interfaces;

import factory.shared.FactoryEvent;

public interface MonitoringInterface {
	
	public void handleEvent(FactoryEvent event);
	
}
