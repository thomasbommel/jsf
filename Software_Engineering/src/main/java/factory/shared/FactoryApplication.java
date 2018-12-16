package factory.shared;

import java.awt.Graphics;
import java.util.function.Consumer;

import factory.shared.enums.SubsystemStatus;
import factory.shared.interfaces.Stoppable;
import factory.subsystems.AbstractSubsystem;
import factory.subsystems.monitoring.MonitoringSystem;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class FactoryApplication implements Stoppable {

	private MonitoringInterface monitor;

	public FactoryApplication() {
		this.monitor = new MonitoringSystem();
		
		AbstractSubsystem testSubsystem = createTestSubsystem();
		this.monitor.addToSubsystemList(testSubsystem);
	}

	//TODO remove
	private AbstractSubsystem createTestSubsystem() {
		return new AbstractSubsystem(this.monitor) {
			int x = 0;
			@Override
			public Position getPosition() {
				return new Position(x++	,200);
			}
			
			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public SubsystemStatus getStatus() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Consumer<Graphics> drawPlaceable() {
				return g -> {
					g.drawRect(0, 0, 80, 30);
					g.drawString("it works", 10,20);
				};
			}
	
		};
	}

	public void start() {
		this.monitor.start();
	}

	public void stop() {
		this.monitor.start();
	}

}
