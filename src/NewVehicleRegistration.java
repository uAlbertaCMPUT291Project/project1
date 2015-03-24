import java.sql.*;
import java.util.Scanner;

//Leah
//This component is used to register a new vehicle by an auto registration officer.
//By a new vehicle, we mean a vehicle that has not been registered in the database. 
//The component shall allow an officer to enter the detailed information 
//about the vehicle and personal information 
//about its new owners, if it is not in the database. 
//You may assume that all the information about vehicle types has been 
//loaded in the initial database.

//check data types
public class NewVehicleRegistration extends ApplicationProgram {
	boolean existance;
	String is_primary_owner;
	Scanner user_input = new Scanner( System.in );
	@Override
	void run() {		
		//involved tables: vehicle, owner, people
		//owner sin and vehicle id shared in owner table
		
		//get vehicle info from user	
		System.out.println("Enter vehicle serial number:");
		String serial_no = user_input.nextLine();
		
		//check if vehicle already exists
		existance = checkVehicleExistance(serial_no);
		
		if (existance==false){
			System.out.println("Enter vehicle maker:");
			String maker = user_input.nextLine();
			System.out.println("Enter vehicle model:");
			String model = user_input.nextLine();
			System.out.println("Enter vehicle year:");
			String year = user_input.nextLine();
			System.out.println("Enter vehicle color:");
			String color = user_input.nextLine();
			//idea - display vehicle types and their id's
			System.out.println("Enter vehicle type id:");
			String type_id = user_input.nextLine();

			//add vehicle to vehicle table, if it doesn't exists
			try{
				Statement statement = DatabaseConnection.getConnection().createStatement();
				String vehicleStmt = "insert into vehicle values ('"+serial_no+"', '"+maker+"', '"
						+model+"', "+year+", '"+color+"', "+type_id+")";
				statement.executeUpdate(vehicleStmt);
				System.out.println("Vehicle added");
			} catch (SQLException e){
				//e.printStackTrace();
				System.out.print(e.getMessage());
			}
		} else {
			System.out.print("vehicle already exists\n");
			serial_no = null;
			//ask if quit or try again - loop
		}
		
		//get person sin
		System.out.println("Enter owner's sin:");
		String sin = user_input.nextLine();
		
		addPerson(sin);
		addOwner(serial_no, sin);
		
		System.out.println("Add another owner? (y/n)");
		String ans = user_input.nextLine();
		if (ans == "y"){
			//get person sin
			System.out.println("Enter owner's sin:");
			sin = user_input.nextLine();
			addPerson(sin);
			addOwner(serial_no,sin);
		}
		
	}
	
	//returns false if person doesn't exist, true if person does exist.
	private boolean checkPersonExistance(String sin) {

		try {
			Statement stmt = DatabaseConnection.getConnection()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = "select * from people where sin = '"
					+ sin + "'";
			ResultSet rs = stmt.executeQuery(query);

			rs.last();
			if (rs.getRow() > 0) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	//returns false if vehicle doesn't exist, true if vehicle does exist.
	private boolean checkVehicleExistance(String serial_no) {

		try {
			Statement stmt = DatabaseConnection.getConnection()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = "select * from vehicle where lower(serial_no) = lower('"
					+ serial_no+ "')";
			ResultSet rs = stmt.executeQuery(query);

			rs.last();
			if (rs.getRow() > 0) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	private void addPerson(String sin){
		//check if person exists
		existance = checkPersonExistance(sin);
		//add the person if they do not exist
		if (existance==false){
			System.out.println("Enter owner's name:");
			String name = user_input.nextLine();
			System.out.println("Enter owner's height:");
			String height = user_input.nextLine();
			System.out.println("Enter owner's weight:");
			String weight = user_input.nextLine();
			System.out.println("Enter owner's eye color:");
			String eyecolor = user_input.nextLine();
			System.out.println("Enter owner's hair color:");
			String haircolor = user_input.nextLine();
			System.out.println("Enter owner's address:");
			String address = user_input.nextLine();
			System.out.println("Enter owner's gender (m/f):");
			String gender = user_input.nextLine();
			System.out.println("Enter owner's birthday (YYYY-MM-DD):");
			String birthday = user_input.nextLine();

			try{
				Statement statement = DatabaseConnection.getConnection().createStatement();
				String peopleStmt = "insert into people values ('"+sin+"', '"+name+"', "
								+height+", "+weight+", '"+eyecolor+"', '"+haircolor
								+"', '"+address+"', '"+gender
								+"', TO_DATE('"+birthday+"', 'YYYY-MM-DD'))";
				statement.executeUpdate(peopleStmt);
				System.out.println("Person added");
			} catch (SQLException e){
				e.printStackTrace();
				System.out.print(e.getMessage());
			}
		}
	}
	
	private void addOwner(String serial_no, String sin){
		//are they primary owner - check if already have a primary owner?
		System.out.println("Are they the primary owner? (y/n):");
		is_primary_owner = user_input.nextLine();
		
		//add row to owner
		try{
			Statement statement = DatabaseConnection.getConnection().createStatement();
			String ownerStmt = "insert into owner values ('"+sin+"', '"+serial_no+"', '"
									+is_primary_owner+"')";
			statement.executeUpdate(ownerStmt);
			System.out.println("Vehicle successfully registered");
		} catch (SQLException e){
			//e.printStackTrace();
			System.out.print(e.getMessage());
		}
	}
	
	
}
