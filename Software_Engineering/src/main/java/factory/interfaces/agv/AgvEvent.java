package factory.interfaces.agv;

import factory.interfaces.monitoring.FactoryEvent;

public abstract class AgvEvent extends FactoryEvent{

	private final AgvInterface agv;

	public AgvEvent(AgvInterface agv) {
		super();
		this.agv = agv;
	}

	public AgvInterface getAgv() {
		return agv;
	};
	
	
	
}
