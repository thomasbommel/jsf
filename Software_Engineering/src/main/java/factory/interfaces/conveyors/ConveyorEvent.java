package factory.interfaces.conveyors;

import factory.interfaces.monitoring.FactoryEvent;

public abstract class ConveyorEvent extends FactoryEvent {
	
	private final ConveyorInterface conveyor;

	public ConveyorEvent(ConveyorInterface conveyor) {
		super();
		this.conveyor = conveyor;
	}

	public ConveyorInterface getConveyor() {
		return conveyor;
	}
	
	//TODO replace with some kind of enum (better for db access)
	public static class ConveyorLowOilEvent extends ConveyorEvent{
		public ConveyorLowOilEvent(ConveyorInterface conveyor) {
			super(conveyor);
		}
	}
	
}


