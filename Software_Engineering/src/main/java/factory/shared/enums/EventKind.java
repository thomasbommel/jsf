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
	CONVEYORS_LackOfOil			(),
	
	//---------------------------------- Conveyors Errors -----------------------------------
	
	
	
	//------------------------------- Warehouse Notifications -------------------------------
	
	//---------------------------------- Warehouse Errors -----------------------------------
	
	
	
	//---------------------------------- AGV Notifications ----------------------------------
	AGV_ContainerDelivered 		(Task.class),
	
	//------------------------------------- AGV Errors --------------------------------------
	AGV_ForkliftDamaged 		(),
	AGV_ForkliftCollision 		(),
	
	
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
