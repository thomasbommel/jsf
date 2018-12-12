package factory.subsystems.robotarms.interfaces;

import java.util.List;

import factory.shared.Container;

public interface RobotInterface {

	/**
	 * @return the list of Containers the robot currently has in his storage
	 */
	List<Container> getContainers();
	
	/**
	 * adds a box to the robot
	 * @param box
	 */
	void addBox(Container box);
	
	/**
	 * does the robot-specific work
	 */
	void doWork();
	
}
