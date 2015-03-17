//Jim

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class ViolationRecord extends ApplicationProgram {

	@Override
	public void run() {
		
		try {
			String violatorSin;
			
			violatorSin = promptUserForViolatorSin();
			
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[Database Error] Violation Record App terminated, sending user back to main menu.");
			return;
		}
	}
	
	
	
	/* 
	 * Description: prompt user for a valid violator sin number. 
	 * Input:none	Output:	a valid violator sin number
	 */
	private String promptUserForViolatorSin() throws SQLException{
		while(true){
			Scanner scanner = new Scanner (System.in);	
			System.out.println("Enter a VALID violator social insurance number (SIN):");
			String violatorSin = scanner.nextLine();
			if (sinExists(violatorSin)){
				return violatorSin;	//got a valid sin exist
			}else{
				System.out.println("SIN \""+violatorSin+"\" does NOT exist");
			}
		}				
	}

	/*
	 * Description: prompt user for a valid violator sin number. 
	 * Input:none	Output:	a valid violator sin number
	 */
	private String promptUserForVehicleSerialNo() throws SQLException{
		while(true){
			Scanner scanner = new Scanner (System.in);	
			System.out.println("Enter a VALID violator social insurance number (SIN):");
			String violatorSin = scanner.nextLine();
			if (sinExists(violatorSin)){
				return violatorSin;	//got a valid sin exist
			}else{
				System.out.println("SIN \""+violatorSin+"\" does NOT exist");
			}
		}				
	}
	
	/*
	 * Description: Helper function check if a SIN exists inside people table
	 * Input: String sin, 
	 * Output: Boolean
	 */
	private boolean sinExists(String sin) throws SQLException{
		ResultSet resultSet = runQuery	("SELECT COUNT(*) AS count FROM people WHERE sin='"+sin+"'");
		resultSet.next();
		return resultSet.getInt("count") > 1;
	}
	
	/*
	 * Description: Helper function check if a vehicle serial number exists inside vehicle table
	 * Input: String sin, 
	 * Output: Boolean
	 */
	private boolean vehicleSerialNoExists(String serialNo) throws SQLException{
		ResultSet resultSet = runQuery	("SELECT COUNT(*) AS count FROM vehicle WHERE serial_no='"+serialNo+"'");
		resultSet.next();
		return resultSet.getInt("count") > 1;
	}	
	
	
	/*
	 * Description: Helper function that runs a given query.
	 * Input: String query, 
	 * Output: ResultSet resultSet.
	 */
	private ResultSet runQuery(String query) throws SQLException{
		Connection connection = DatabaseConnection.getConnection();
		ResultSet resultSet = connection.createStatement().executeQuery(query);
		return resultSet;		
	}
	
	


}
