package factory.subsystems.agv;

import factory.shared.Position;
import factory.shared.ResourceBox;
import factory.shared.Task;

public class AgvTask extends Task {

	private final ResourceBox box;
	private final Position pickup, dropoff;
	
	public AgvTask(int id, ResourceBox box, Position pickup, Position dropoff) {
		super();
		this.box = box;
		this.pickup = pickup;
		this.dropoff = dropoff;
	}

	public ResourceBox getBox() {
		return box;
	}

	public Position getPickup() {
		return pickup;
	}

	public Position getDropoff() {
		return dropoff;
	}
	
}
