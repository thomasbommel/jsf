package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import factory.subsystems.warehouse.StorageSite;

public class Database {

	private static final String URL = "jdbc:derby:database";
	
	public static final Database INSTANCE = new Database();			//singleton
	
	private final List<DatabaseTable> TABLES = new ArrayList<>();
	
	private final Connection connection;
	
	{ 
		//TODO: rather than initializing the list here statically, add a table for each StorageSite when the layout.xml is being processed
		TABLES.add(new StorageSiteTable(new StorageSite(null, 1)));
		TABLES.add(new TransactionsTable());
	}
	
	//TODO: only for current testing, remove this method later
	public List<DatabaseTable> getTables() {
		return TABLES;
	}
	
	private Database() {
		try {
			Connection c = null;
			try {
				//get connection to existing database
				c = DriverManager.getConnection(URL);
			} catch (SQLException databaseNotFound) {
				//create new database
				c = DriverManager.getConnection(URL + ";create=true");
				Statement creationStatement = c.createStatement();
				for (DatabaseTable table : TABLES) {
					System.out.println(table.getCreationString());
					creationStatement.addBatch(table.getCreationString());
				}
				creationStatement.executeBatch();
			}
			connection = c;
			
			if (connection.isReadOnly()) {
				JOptionPane.showMessageDialog(null, 
						"Establishing Database Connection failed.\nPlease restart the program to try again."
						, "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			
			for (DatabaseTable table : TABLES) {
				table.prepareStatements(connection);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Establishing Database Connection failed.");
		}
	}
	
	/**
	 * Utility method for testing purposes
	 */
	public void printToConsole(){
		for (DatabaseTable table : TABLES) {
			table.printToConsole();
		}
	}
}




