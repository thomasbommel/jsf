package factory.subsystems.warehouse.interfaces;

import java.util.List;

import factory.shared.enums.Material;
import factory.shared.Task;

public interface WarehouseMonitorInterface{

	int getAmount(Material material);
	
	/**
	 * @param material
	 * @return the created Task
	 */
	public Task prepareMaterial(Material material, int availablePreparationTime);
	
	//TODO change type
	public List<String> getTransactions();
}
