package factory.subsystems.monitoring;

import java.util.ArrayList;
import java.util.List;

import app.gui.GUIHandler;
import factory.shared.FactoryEvent;
import factory.subsystems.AbstractSubsystem;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class MonitoringSystem implements MonitoringInterface {

	private List<AbstractSubsystem> testList = new ArrayList<>(); //TODO replace by one list for each type of subsystem
	
	
	
	
	private GUIHandler handler;

	public MonitoringSystem() {
		this.handler = new GUIHandler(this);
	}

	@Override
	public void addToSubsystemList(AbstractSubsystem subsystem) {
		this.testList.add(subsystem);
		this.handler.addToFactoryPanel(subsystem);
	}
	
	public void start() {
		this.handler.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEvent(FactoryEvent event) {
		// TODO Auto-generated method stub
		
	}

	
	
	

}
