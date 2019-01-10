package factory.shared;

import java.awt.Graphics;
import java.util.List;
import java.util.function.Consumer;

import factory.shared.enums.SubsystemStatus;
import factory.shared.interfaces.Placeable;
import factory.shared.interfaces.Stoppable;
import factory.subsystems.AbstractSubsystem;
import factory.subsystems.monitoring.MonitoringSystem;
import factory.subsystems.monitoring.interfaces.MonitoringInterface;

public class FactoryApplication implements Stoppable {

	private MonitoringInterface monitor;

	public FactoryApplication() {
		this.monitor = new MonitoringSystem();

		AbstractSubsystem testSubsystem1 = createTestSubsystem("TEST 1", 100);
		AbstractSubsystem testSubsystem2 = createTestSubsystem("TEST 2", 200);
		this.monitor.addToSubsystemList(testSubsystem1);
		this.monitor.setCurrentSubsystemToShow(testSubsystem1);
		this.monitor.addToSubsystemList(testSubsystem2);
	}

	// TODO remove
	private AbstractSubsystem createTestSubsystem(String name, int y) {
		return new AbstractSubsystem(this.monitor, name) {
			private boolean run = false;
			private int x = 0;

			@Override
			public Position getPosition() {
				update();
				return new Position(x, y);
			}

			private void update() {
				if (run)
					x++;
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
					g.drawString(getName(), 10, 20);
				};
			}

			@Override
			public void start() {
				run = true;
			}

			@Override
			public void stop() {
				run = false;
			}

			@Override
			public List<Placeable> getPlaceables() {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}

	public void start() {
		this.monitor.start();
	}

	public void stop() {
		this.monitor.start();
	}

	public MonitoringInterface getMonitor() {
		return monitor;
	}

}
