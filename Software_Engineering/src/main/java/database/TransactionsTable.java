package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import factory.shared.enums.Material;

public class TransactionsTable extends DatabaseTable {

	private PreparedStatement insertTransaction;
	
	private final String tableName = "Transactions";
	
	public TransactionsTable() {
		super();
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	protected void initializeMap(Map<String, String> columnNameToDatatypeMap) {
		columnNameToDatatypeMap.put("Company", "VARCHAR(30)");
		columnNameToDatatypeMap.put("Material", "VARCHAR(20)");
		columnNameToDatatypeMap.put("ContainerAmount", "INT");
		columnNameToDatatypeMap.put("Cost_Euro", "INT");
		columnNameToDatatypeMap.put("Date", "VARCHAR(8)");
		columnNameToDatatypeMap.put("StorageSite_ID", "INT");
	}

	@Override
	public void prepareStatements(Connection databaseConnection) {
		super.prepareStatements(databaseConnection);
		try {
			insertTransaction = databaseConnection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertTransaction(String company, Material material, int containerAmount, int cost, String date, int storageSiteID) throws SQLException {
		insertTransaction.setString(1, company);
		insertTransaction.setString(2, material.toString());
		insertTransaction.setInt(3, containerAmount);
		insertTransaction.setInt(4, cost);
		insertTransaction.setString(5, date);
		insertTransaction.setInt(6, storageSiteID);
		insertTransaction.execute();
	}

	/**
	 * Utility method for testing purposes
	 */
	@Override
	public void printToConsole() {
		try {
			System.out.printf("------------ TABLE: %s ------------%n", tableName);
			System.out.printf("%-31s %-21s %-16s %-10s %-9s %s%n", "Company", "Material", "ContainerAmount", "Cost_Euro", "Date", "StorageSite_ID");
			
			ResultSet allData = selectEverything.executeQuery();
			while (allData.next()) {
				System.out.printf("%-31s %-21s %-16d %-10d %-9s %d%n", 
						allData.getString(1), 
						allData.getString(2),
						allData.getInt(3),
						allData.getInt(4),
						allData.getString(5),
						allData.getInt(6));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
