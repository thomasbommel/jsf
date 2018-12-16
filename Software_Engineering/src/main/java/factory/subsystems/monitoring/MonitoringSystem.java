package factory.subsystems.monitoring;

import app.gui.GUIHandler;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class MonitoringSystem implements MonitoringInterface {

	private GUIHandler handler;

	public MonitoringSystem() {
		this.handler = new GUIHandler(this);
	}

	public void start() {
		this.handler.start();
	}
	
	

}
