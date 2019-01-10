package factory.subsystems.agv;

import java.util.LinkedList;
import java.util.List;

import factory.shared.AbstractSubsystem;
import factory.shared.Task;
import factory.shared.enums.SubsystemStatus;
import factory.subsystems.agv.interfaces.AgvMonitorInterface;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class AgvCoordinator extends AbstractSubsystem implements AgvMonitorInterface{
	
	private final List<Forklift> forklifts = new LinkedList<>();
	
	public AgvCoordinator(MonitoringInterface mon)
	{
		super(mon);
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
	public List<Forklift> getForklifts() {
		return forklifts;
	}

	@Override
	public SubsystemStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}
}
