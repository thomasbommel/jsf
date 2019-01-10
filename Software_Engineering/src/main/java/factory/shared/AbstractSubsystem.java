package factory.shared;

import factory.shared.interfaces.Monitorable;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public abstract class AbstractSubsystem implements Monitorable {

	private MonitoringInterface monitor;

	public AbstractSubsystem(MonitoringInterface monitor) {
		super();
		this.monitor = monitor;
	}
	
	@Override
	public void notify(FactoryEvent event) {
		monitor.handleEvent(event);
	}
	
}
