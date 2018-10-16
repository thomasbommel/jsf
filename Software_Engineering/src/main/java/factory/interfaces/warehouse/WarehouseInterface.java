package factory.interfaces.warehouse;

import java.util.List;

import factory.model.Material;
import factory.model.Task;

public interface WarehouseInterface{

	int getAmount(Material material);
	
	/**
	 * @param material
	 * @return the created Task
	 */
	public Task prepareMaterial(Material material, int availablePreparationTime);
	
	/**
	 * notifies the monitoring system about the given event
	 * @param task
	 * @param event
	 */
	public void notifyMonitoringSystem(Task task, WarehouseEvent event);
	
	//TODO change type
	public List<String> getTransactions();
}
