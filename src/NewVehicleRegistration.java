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

//loops
//quits
public class NewVehicleRegistration extends ApplicationProgram {
	boolean existance;
	boolean idExist;
	String is_primary_owner;
	Scanner user_input = new Scanner( System.in );
	@Override
	void run() {		
		
		//get vehicle info from user	
		System.out.println("Please enter vehicle serial number. Enter 'exit' to  quit.");
		String serial_no = user_input.nextLine();
		if (serial_no.equalsIgnoreCase("exit")) {
			System.out.println("You have requested to exit");
			return;
		}
		
		//check if vehicle already exists
		existance = checkVehicleExistance(serial_no);
		
		if (existance==false){
			System.out.println("Please enter vehicle maker. Enter 'exit' to quit.");
			String maker = user_input.nextLine();
			if (maker.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			}			
			System.out.println("Please enter vehicle model. Enter 'exit' to quit.");
			String model = user_input.nextLine();
			if (model.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			}
			System.out.println("Please enter vehicle year. Enter 'exit' to quit.");
			String year = user_input.nextLine();
			if (year.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			}
			System.out.println("Please enter vehicle color. Enter 'exit' to quit.");
			String color = user_input.nextLine();
			if (color.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			}
			System.out.println("Please enter vehicle type id. Enter 'exit' to quit.");
			String type_id = user_input.nextLine();
			if (type_id.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			}
			//check if id exists
			idExist = checkIdExistance(type_id);
			if (idExist==false){
				System.out.println("That is not a valid type_id");
				System.out.println("Request failed");
				return;
			}
			
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
			System.out.print("Vehicle already exists\n");
			serial_no = null;
			System.out.println("Request failed");
			return;
		}
		
		//get person sin
		System.out.println("Please enter owner's sin. Enter 'exit' to quit.");
		String sin = user_input.nextLine();
		if (sin.equalsIgnoreCase("exit")) {
			System.out.println("You have requested to exit");
			return;
		}
		
		addPerson(sin);
		addOwner(serial_no, sin);
		
		System.out.println("Add another owner? (y/n)");
		String ans = user_input.nextLine();
		if (ans.equals("y")){
			//get person sin
			System.out.println("Enter owner's sin:");
			sin = user_input.nextLine();
			addPerson(sin);
			addOwner(serial_no,sin);
		} else {
			return;
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
	
	private boolean checkIdExistance(String id){
		try {
			Statement stmt = DatabaseConnection.getConnection()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = "select * from vehicle where lower(type_id) = lower('"
					+ id+ "')";
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
	
	//adds a person if they do not exist
	private void addPerson(String sin){
		//check if person exists
		existance = checkPersonExistance(sin);
		//add the person if they do not exist
		if (existance==false){

			System.out.println("Person does not exist yet. Press enter to add them or " +
					"enter 'exit' to quit");
			String add = user_input.nextLine();
			if (add.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			} else{

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
	}
	//adds the ownership to owner table
	private void addOwner(String serial_no, String sin){
		//are they primary owner - check if already have a primary owner?
		System.out.println("Are they the primary owner? (y/n)");
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
