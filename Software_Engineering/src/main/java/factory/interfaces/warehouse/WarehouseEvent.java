package factory.interfaces.warehouse;

import factory.interfaces.monitoring.FactoryEvent;

public abstract class WarehouseEvent extends FactoryEvent{

	private final WarehouseInterface warehouse;

	public WarehouseEvent(WarehouseInterface warehouse) {
		super();
		this.warehouse = warehouse;
	}

	public WarehouseInterface getWarehouse() {
		return warehouse;
	}
	
}
