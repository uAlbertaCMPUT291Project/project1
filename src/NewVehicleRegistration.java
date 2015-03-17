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

public class NewVehicleRegistration extends ApplicationProgram {

	@Override
	void run() {
		
		Scanner user_input = new Scanner( System.in );
		
		//involved tables: vehicle, owner, people
		//owner sin and vehicle id shared in owner table
				
		
		//get vehicle id/info from user	
		System.out.println("Enter vehicle serial number:");
		String serial_no = user_input.nextLine();
		//check if vehicle already exists
		
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

		
		//check that the vehicle does not exist already
		//if exists - tell user & start over - will sql error if this happens?
		
		
		//add vehicle to vehicle table, if it doesn't exists
		try{
			Statement statement = DatabaseConnection.getConnection().createStatement();
			String vehicleStmt = "insert into vehicle values ('"+serial_no+"', '"+maker+"', '"
										+model+"', "+year+", '"+color+"', "+type_id+")";
			statement.executeUpdate(vehicleStmt);
			System.out.println("Vehicle added");
		} catch (SQLException e){
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		
		// get people (check existance - add if neccessary), add owner
		
		//person
		System.out.println("Enter owner's sin:");
		String sin = user_input.nextLine();
		
		//check if person exists
		//check if person is already in people table
		//if not get and add all their info
		try{
			Statement statement = DatabaseConnection.getConnection().createStatement();
			String checkPeopleStmt = "select sin " +
					"from people " +
					"where people.sin="+sin;
			statement.executeQuery(checkPeopleStmt);
		} catch (SQLException e){
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		//if (statement is not empty){ person already exists}
		//person - if neccessary (sin does not exist in table)
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

		
		//add person - not complete
		//date yyyy-mm-dd
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
		
		
		//is primary owner??
		System.out.println("Are they the primary owner? (y/n):");
		String is_primary_owner = user_input.nextLine();
		
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
