//Author: Nelson

import java.sql.*;
public class DatabaseConnection {
	static private Connection connection;
	
	public DatabaseConnection(String username, String password, String url){
		
		if (connection==null){
			try{ 
				DatabaseConnection.connection = DriverManager.getConnection(url,username,password);
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public static Connection getConnection(){
		return DatabaseConnection.connection;
	}
	
	
}
