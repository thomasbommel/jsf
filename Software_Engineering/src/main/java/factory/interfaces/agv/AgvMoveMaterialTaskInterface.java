package factory.interfaces.agv;

import factory.model.Position;
import factory.model.Task;
import factory.model.TaskHandler;

public interface AgvMoveMaterialTaskInterface extends Task {

	TaskHandler getTaskHandler();
	Position getFromPosition();
	Position getToPosition();
	
	
	
}
