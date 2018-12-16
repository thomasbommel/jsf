package factory.shared;

import factory.shared.interfaces.Stoppable;
import factory.subsystems.agv.interfaces.AgvMonitorInterface;
import factory.subsystems.monitoring.MonitoringSystem;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class FactoryApplication implements Stoppable{

	private MonitoringInterface monitor;
	private AgvMonitorInterface agv;
	
	public FactoryApplication() {
		this.monitor = new MonitoringSystem();
		
		
		
	}

	public void start() {
		this.monitor.start();
	}

	public void stop() {
		this.monitor.start();
	}
	
	
}
