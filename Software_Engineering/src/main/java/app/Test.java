package app;

import app.gui.SubsystemMenu;
import factory.shared.FactoryEvent;
import factory.shared.Position;
import factory.shared.enums.EventKind;
import factory.shared.enums.SubsystemStatus;
import factory.shared.interfaces.Monitorable;
import factory.subsystems.agv.AgvTask;

class Test implements Monitorable {

	public static void main(String[] args) {
		Test thiz = new Test();
		
		System.out.println(EventKind.AGV_CONTAINER_DELIVERED);
		System.out.println(EventKind.AGV_FORKLIFT_COLLISION);
		
		System.out.println("-- Valid Attachment --");
		new FactoryEvent(thiz, EventKind.AGV_CONTAINER_DELIVERED, new AgvTask());
		
		System.out.println("-- Zero (correct) Attachments --");
		new FactoryEvent(thiz, EventKind.AGV_FORKLIFT_COLLISION);
		
		try {
			System.out.println("-- Invalid Attachment --");
			new FactoryEvent(thiz, EventKind.AGV_CONTAINER_DELIVERED, new Object());
		} catch (IllegalArgumentException e) {
			System.out.println("Exception caught.");
		}
		
		try {
			System.out.println("-- Too many Attachments --");
			new FactoryEvent(thiz, EventKind.AGV_FORKLIFT_COLLISION, new AgvTask());
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

	@Override
	public Position getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubsystemMenu getCurrentSubsystemMenu() {
		// TODO Auto-generated method stub
		return null;
	}


}