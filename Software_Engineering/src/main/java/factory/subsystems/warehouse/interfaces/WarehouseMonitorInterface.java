package factory.subsystems.warehouse.interfaces;

import java.util.List;

import factory.shared.Task;
import factory.shared.enums.Material;
import factory.shared.interfaces.Monitorable;

public interface WarehouseMonitorInterface extends Monitorable {

	int getAmount(Material material);
	
	/**
	 * @param material
	 * @return the created Task
	 */
	public Task prepareMaterial(Material material, int availablePreparationTime);
	
	//TODO change type
	public List<String> getTransactions();
}
