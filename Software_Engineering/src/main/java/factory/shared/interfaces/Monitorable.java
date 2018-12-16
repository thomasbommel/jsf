package factory.shared.interfaces;

import factory.shared.FactoryEvent;
import factory.shared.enums.SubsystemStatus;

public interface Monitorable extends Placeable{

	void notify(FactoryEvent event);

	SubsystemStatus getStatus();

	boolean isReady();

}
