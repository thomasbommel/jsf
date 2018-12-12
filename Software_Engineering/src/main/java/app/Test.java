package app;

import factory.shared.FactoryEvent;
import factory.shared.enums.EventKind;
import factory.shared.enums.SubsystemStatus;
import factory.shared.interfaces.Monitorable;
import factory.subsystems.agv.AgvTask;

public class Test implements Monitorable {

	public static void main(String[] args) {
		Test thiz = new Test();
		
		System.out.println(EventKind.AGV_ContainerDelivered);
		System.out.println(EventKind.AGV_ForkliftCollision);
		
		System.out.println("-- Valid Attachment --");
		new FactoryEvent(thiz, EventKind.AGV_ContainerDelivered, new AgvTask());
		
		System.out.println("-- Zero (correct) Attachments --");
		new FactoryEvent(thiz, EventKind.AGV_ForkliftCollision);
		
		try {
			System.out.println("-- Invalid Attachment --");
			new FactoryEvent(thiz, EventKind.AGV_ContainerDelivered, new Object());
		} catch (IllegalArgumentException e) {
			System.out.println("Exception caught.");
		}
		
		try {
			System.out.println("-- Too many Attachments --");
			new FactoryEvent(thiz, EventKind.AGV_ForkliftCollision, new AgvTask());
		} catch (IllegalArgumentException e) {
			System.out.println("Exception caught.");
		}
	}

	public void notify(FactoryEvent event) {
		return;
	}

	public SubsystemStatus getStatus() {
		return null;
	}

	public boolean isReady() {
		return false;
	}
	
}