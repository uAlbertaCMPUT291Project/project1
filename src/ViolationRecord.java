//Jim
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ViolationRecord extends ApplicationProgram {

	@Override
	public void run() {
		
		try {
			
			String ticketNo = new Integer(getMaxTicketNo()+1).toString();
			String violatorSin;
			String vehicleSerialNo;
			String officerSin; 
			String vtype;
			String vdate;
			String place;
			String descriptions;
			
			
			violatorSin = promptUserForViolatorSin();
			vehicleSerialNo = promptUserForVehicleSerialNo();
			officerSin = promptUserForOfficerSin();
			vtype = promptUserForVtype();
			vdate = promptUserForVdate();
			place = promptUserForPlace();
			descriptions = promptUserForDescriptions();
			
			runQuery("insert into ticket values('"+ticketNo+"','"+violatorSin+"','"+vehicleSerialNo+"','"
												  +officerSin+"','"+vtype+"',TO_DATE('"+vdate+"','yyyy/mm/dd'),'"+place+"','"+descriptions+"')");
			
			System.out.println("Ticket Recorded");
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
	 * Description: prompt user for a valid vehicle number. 
	 * Input:none	Output:	a valid vehicle number
	 */
	private String promptUserForVehicleSerialNo() throws SQLException{
		while(true){
			Scanner scanner = new Scanner (System.in);	
			System.out.println("Enter a VALID vehicle serial number:");
			String vehicleSerialNumber = scanner.nextLine();
			if (vehicleSerialNoExists(vehicleSerialNumber)){
				return vehicleSerialNumber;	//got a valid sin exist
			}else{
				System.out.println("Vehicle serial number \""+vehicleSerialNumber+"\" does NOT exist");
			}
		}				
	}
	
	private String promptUserForOfficerSin()throws SQLException{
		while(true){
			Scanner scanner = new Scanner (System.in);	
			System.out.println("Enter a VALID officer social insucrance number (SIN):");
			String officerSin = scanner.nextLine();
			if (sinExists(officerSin)){
				return officerSin;	//got a valid sin exist
			}else{
				System.out.println("SIN \""+officerSin+"\" does NOT exist");
			}
		}		
	}
	
	private String promptUserForVtype()throws SQLException{
		while(true){
			Scanner scanner = new Scanner (System.in);	
			System.out.println("Enter a VALID violation type:");
			String violationType = scanner.nextLine();
			if (vtypeExists(violationType)){
				return violationType;	//got a valid sin exist
			}else{
				System.out.println("Violation type \""+violationType+"\" does NOT exist");
			}
		}		
	}
	
	
	private String promptUserForVdate(){
		Scanner scanner = new Scanner (System.in);	
		String vdate;
		while(true){
			System.out.println("Enter the violation date \"yyyy/mm/dd\":");
			vdate = scanner.nextLine();
			if (validDate(vdate)){
				break;
			}else{
				System.out.println("Date entered doesn't match \"yyyy/mm/dd\"");
			}
		}
		return vdate;		
	}
	
	private String promptUserForPlace(){
		Scanner scanner = new Scanner (System.in);	
		System.out.println("Enter the violation location:");
		String place = scanner.nextLine();

		return place;		
	}
	
	private String promptUserForDescriptions(){
		Scanner scanner = new Scanner (System.in);	
		System.out.println("Enter descriptions:");
		String descriptions = scanner.nextLine();

		return descriptions;		
	}
	
	
	/*
	 * Description: Helper function check if a SIN exists inside people table
	 * Input: String sin, 
	 * Output: Boolean
	 */
	private boolean sinExists(String sin) throws SQLException{
		ResultSet resultSet = runQuery	("SELECT COUNT(*) AS count FROM people WHERE sin='"+sin+"'");
		resultSet.next();
		return resultSet.getInt("count") > 0;
	}
		
	/*
	 * Description: Helper function check if a vehicle serial number exists inside vehicle table
	 * Input: String serialNo, 
	 * Output: Boolean
	 */
	private boolean vehicleSerialNoExists(String serialNo) throws SQLException{
		ResultSet resultSet = runQuery	("SELECT COUNT(*) AS count FROM vehicle WHERE serial_no='"+serialNo+"'");
		resultSet.next();
		return resultSet.getInt("count") > 0;
	}	
	
	
	private boolean vtypeExists(String vtype) throws SQLException{
		ResultSet resultSet = runQuery	("SELECT COUNT(*) AS count FROM ticket_type WHERE vtype='"+vtype+"'");
		resultSet.next();
		return resultSet.getInt("count") > 0;
	}
	
	
	private int getMaxTicketNo() throws SQLException{
		ResultSet resultSet = runQuery	("SELECT MAX(ticket_no) AS maxTicketNo FROM ticket");
		resultSet.next();
		return resultSet.getInt("maxTicketNo");
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
	
	private boolean validDate(String dateString){
		try {
			new SimpleDateFormat("yyyy/MM/dd").parse( dateString);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	


}
