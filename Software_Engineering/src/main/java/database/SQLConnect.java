package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TODO: rework

public class SQLConnect {
	private Connection connection = null;
	private String url;
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";

	public SQLConnect() {
		// Load the driver
		try {
			Class.forName(driver);
			System.out.println(driver + " loaded.");
		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		}
		// URL = "jdbc:derby:/database;create=true";
		url = "jdbc:derby:database;create=true";

		System.out.println("Created SQL Connect");
	}

	public void CreateConnection() {
		try {
			connection = DriverManager.getConnection(url);
			System.out.println("Successfully Connected");
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	public void CloseConnection() {
		try {
			this.connection.close();
			System.out.println("Connection successfully closed");
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}

	public static void main(String args[]) {
		SQLConnect sql = new SQLConnect();
		sql.CreateConnection();
		sql.CloseConnection();
	}
}