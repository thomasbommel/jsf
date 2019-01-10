package factory.subsystems.agv;

import factory.shared.Position;
import factory.shared.Task;
import factory.shared.enums.SubsystemStatus;
import factory.subsystems.AbstractSubsystem;
import factory.subsystems.agv.interfaces.AgvMonitorInterface;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class AGVSystem extends AbstractSubsystem implements AgvMonitorInterface {

	public AGVSystem(MonitoringInterface monitor) {
		super(monitor);
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

	@Override
	public void submitTask(Task task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Task getCurrentTask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
