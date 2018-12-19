package factory.shared.enums;

import factory.shared.Task;

/**
 * The kind of a FactoryEvent.<br>
 * EventKinds can portray simple Notifications (e.g. a Task being completed), but can also be errors.
 */
public enum EventKind {
	//------------------------------- RobotArms Notifications -------------------------------
	
	//---------------------------------- RobotArms Errors -----------------------------------
	
	
	//------------------------------- Conveyors Notifications -------------------------------
	CONVEYORS_LACK_OF_OIL			(),
	
	//---------------------------------- Conveyors Errors -----------------------------------
	
	
	
	//------------------------------- Warehouse Notifications -------------------------------
	WAREHOUSE_TASK_COMPLETED		(Task.class),
	
	//---------------------------------- Warehouse Errors -----------------------------------
	
	
	
	//---------------------------------- AGV Notifications ----------------------------------
	AGV_CONTAINER_DELIVERED 		(Task.class),
	
	//------------------------------------- AGV Errors --------------------------------------
	AGV_FORKLIFT_DAMAGED 			(),
	AGV_FORKLIFT_COLLISION 			(),
	
	
	//------------------------------- Monitoring Notifications ------------------------------
	
	//---------------------------------- Monitoring Errors ----------------------------------
	
	;
	
	public Class<?>[] attachmentTypes;
	
	private EventKind(Class<?>... attachmentTypes) {
		this.attachmentTypes = attachmentTypes;
	}
	
	@Override
	public String toString() {
		StringBuilder attachmentsSb = new StringBuilder();
		for (int i = 0; i < attachmentTypes.length; i++) {
			attachmentsSb.append(attachmentTypes[i].getSimpleName());
			if (i != attachmentTypes.length - 1)
				attachmentsSb.append(", ");
		}
		return String.format("Event: \"%s\", Attachments: %d -- [%s]", super.toString(), attachmentTypes.length, attachmentsSb.toString());
	}
}
