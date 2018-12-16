package app.gui;

import factory.shared.interfaces.Stoppable;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class GUIHandler implements Stoppable{

	private static final int FPS = 30;
	
	private MonitoringInterface monitoringSystem;
	private UserInterface ui;

	public GUIHandler(MonitoringInterface monitoringSystem) {
		super();
		this.monitoringSystem = monitoringSystem;
		this.ui = new UserInterface(FPS);
	}

	public void start() {
		this.ui.start();
	}

	public void stop() {
		this.ui.stop();
	}

}
