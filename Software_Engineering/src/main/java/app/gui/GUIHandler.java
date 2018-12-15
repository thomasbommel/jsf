package app.gui;

import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class GUIHandler {

	private MonitoringInterface monitoringSystem;
	private UserInterface ui;

	public GUIHandler(MonitoringInterface monitoringSystem) {
		super();
		this.monitoringSystem = monitoringSystem;
		this.ui = new UserInterface();
	}
	
	
	
	
}
