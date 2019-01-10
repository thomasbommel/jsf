package factory.subsystems.agv;

import java.util.LinkedList;
import java.util.List;

import factory.shared.FactoryEvent;
import factory.shared.Task;
import factory.subsystems.agv.interfaces.AgvMonitorInterface;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class AgvCoordinator implements AgvMonitorInterface{
	
	private final List<Forklift> forklifts = new LinkedList<>();
	private final MonitoringInterface monitoring;
	
	public AgvCoordinator(MonitoringInterface mon)
	{
		monitoring = mon;
	}
	
	public void addForklift(Forklift forklift)
	{
		forklifts.add(forklift);
	}

	@Override
	public void submitTask(Task task) {
		// TODO: Choose best free Forklift and call forklift.assignTask
	}

	@Override
	public void notifyMonitoringSystem(Task task, FactoryEvent event) {
		// TODO: Clarify these Interfaces
		monitoring.handleEvent(event);
	}

	@Override
	public List<Forklift> getForklifts() {
		return forklifts;
	}
}
