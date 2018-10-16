package factory.interfaces.robotarms;

import factory.interfaces.monitoring.FactoryEvent;

public abstract  class RobotEvent extends FactoryEvent{
	
	private final RobotInterface robot;

	public RobotEvent(RobotInterface robotInterface) {
		super();
		this.robot = robotInterface;
	}

	public RobotInterface getRobot() {
		return robot;
	}
	
}
