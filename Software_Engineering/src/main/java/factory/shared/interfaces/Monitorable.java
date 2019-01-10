package factory.shared.interfaces;

import java.util.List;

import factory.shared.FactoryEvent;
import factory.shared.enums.SubsystemStatus;

public interface Monitorable {

	void notify(FactoryEvent event);

	SubsystemStatus getStatus();

	List<Placeable> getPlaceables();

}
