//Author: Nelson

import java.sql.*;

public class DatabaseConnection {
	static public Connection connection;

	public DatabaseConnection() {

	}

	public void createConncetion(String username, String password, String url)
			throws SQLException {

		if (connection == null) {
			DatabaseConnection.connection = DriverManager.getConnection(url,
					username, password);
		}

	}

	public static Connection getConnection() {
		return DatabaseConnection.connection;
	}

	public static void closeConnection() {
		try {
			DatabaseConnection.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out
					.println("error occured while trying to close the database connection");
		}
	}
}
