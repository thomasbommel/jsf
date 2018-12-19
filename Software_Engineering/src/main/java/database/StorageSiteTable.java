package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import factory.shared.enums.Material;
import factory.subsystems.warehouse.StorageSite;

public class StorageSiteTable extends DatabaseTable {
	
	private PreparedStatement selectMaterial, insertMaterial;
	
	private final String tableName;
	
	public StorageSiteTable(StorageSite storageSite) {
		super();
		this.tableName = "StorageSite_" + storageSite.getId();
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	protected void initializeMap(Map<String, String> columnNameToDatatypeMap) {
		columnNameToDatatypeMap.put("Material", "VARCHAR(20)");
		columnNameToDatatypeMap.put("ContainersStored", "INT");
	}

	@Override
	public void prepareStatements(Connection databaseConnection) {
		super.prepareStatements(databaseConnection);
		try {
			selectEverything = databaseConnection.prepareStatement("SELECT * FROM " + tableName);
			selectMaterial = databaseConnection.prepareStatement("SELECT * FROM " + tableName + " WHERE Material = ?");
			insertMaterial = databaseConnection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertMaterial(Material material, int containerAmount) throws SQLException {
		insertMaterial.setString(1, material.toString());
		insertMaterial.setInt(2, containerAmount);
		insertMaterial.execute();
	}
	
	/** Retrieves the amount of containers in this StorageSite of a specific material. */
	public int getContainerAmount(Material material) throws SQLException {
		selectMaterial.setString(1, material.toString());
		ResultSet result = selectMaterial.executeQuery();
		if (result.next()) {
			return result.getInt(2);
		}
		else {
			throw new SQLException(String.format("Material \"%s\" not present in Table \"%s\"!", material.toString(), tableName));
		}
	}

	/**
	 * Utility method for testing purposes
	 */
	@Override
	public void printToConsole() {
		try {
			System.out.printf("------------ TABLE: %s ------------%n", tableName);
			System.out.printf("%-21s %s%n", "Material", "ContainersStored");
			
			ResultSet allData = selectEverything.executeQuery();
			while (allData.next()) {
				System.out.printf("%-21s %d%n", allData.getString(1), allData.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
