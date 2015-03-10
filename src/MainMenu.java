import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//Author: Nelson
public class MainMenu {
	//
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//main connection to database
		DatabaseConnection connection = new DatabaseConnection();
		int menu_choice = -1;

		ApplicationProgram auto_program = new AutoTransaction();
		ApplicationProgram driver_program = new DriverLicenceRegistration();
		ApplicationProgram vehicle_program = new NewVehicleRegistration();
		ApplicationProgram violation_program = new ViolationRecord();
		ApplicationProgram search_program = new SearchEngine();

		Map<Integer, ApplicationProgram> dictionary = new HashMap();
		dictionary.put(0, auto_program);
		dictionary.put(1, driver_program);
		dictionary.put(2, vehicle_program);
		dictionary.put(3, violation_program);
		dictionary.put(4, search_program);

		setup_connection(connection);
		while (true){
			menu_choice = get_menu_input();
			dictionary.get(menu_choice).run();
		}

	}
	
	public static int get_menu_input(){
		
		Scanner user_input = new Scanner(System.in);
		int user_option = -1;
		
		System.out.println("Choose from the following options:");
		System.out.println("0: Auto Transaction");
		System.out.println("1: Driver License Registration");
		System.out.println("2: New Vehicle Registration");
		System.out.println("3: Violation Record");
		System.out.println("4: Search Engine");
		System.out.println("5: Exit");
		
		if(user_input.hasNextInt()){
			user_option = user_input.nextInt();
		}
		
		return user_option;
	}

	public static void setup_connection(DatabaseConnection connection) {

		Scanner user_input = new Scanner(System.in);
		String oracle_username, oracle_password;
		String oracle_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
		
		boolean connection_successful = false;

		System.out.println("Welcome to CMPUT291 Project1 Database Application");
		System.out.println();

		while (!connection_successful) {

			System.out.print("Please enter your Oracle username:");
			oracle_username = user_input.nextLine();
			System.out.print("Please enter your Oracle password:");
			oracle_password = user_input.nextLine();

			try {
				connection.createConncetion(oracle_username, oracle_password, oracle_url);
				connection_successful = true;
				System.out.println("Successful Connection to Oracle");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.print(e.getMessage());
				System.out.println("Please enter credentials again");
				System.out.println();
			}
		}
	}

}
