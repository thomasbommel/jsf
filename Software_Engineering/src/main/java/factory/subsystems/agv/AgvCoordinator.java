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
	private SubsystemStatus status = SubsystemStatus.WAITING;
	private boolean ready = false;
	
	public AgvCoordinator(MonitoringInterface mon)
	{
		super(mon);
		status = SubsystemStatus.RUNNING;
		ready = true;
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
		return status;
	}

	@Override
	public boolean isReady() {
		return ready;
	}
}
