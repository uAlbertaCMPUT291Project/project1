import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

//Author: Nelson
public class MainMenu {

	private static int quit_code = 6;
	private static int smallest_value = 1;
	private static int largest_value = 6;

	//
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// main connection to database
		DatabaseConnection connection = new DatabaseConnection();
		Scanner user_input = new Scanner(System.in);
		int menu_choice = -1;

		ApplicationProgram auto_program = new AutoTransaction();
		ApplicationProgram driver_program = new DriverLicenceRegistration();
		ApplicationProgram vehicle_program = new NewVehicleRegistration();
		ApplicationProgram violation_program = new ViolationRecord();
		ApplicationProgram search_program = new SearchEngine();

		Map<Integer, ApplicationProgram> dictionary = new HashMap<Integer, ApplicationProgram>();
		dictionary.put(1, auto_program);
		dictionary.put(2, driver_program);
		dictionary.put(3, vehicle_program);
		dictionary.put(4, violation_program);
		dictionary.put(5, search_program);

		setup_connection(connection);

		while (menu_choice != quit_code) {
			menu_choice = get_menu_input();
			if (menu_choice != quit_code) {
				dictionary.get(menu_choice).run();
			}

			if (menu_choice != quit_code) {
				System.out
						.println("Please press ENTER to go back to the main menu.");
				try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}

		}
		System.out.println("Closing connection and Exiting. Bye!");

	}

	public static int get_menu_input() {

		Scanner user_input = new Scanner(System.in);
		int user_option = -1;

		while (user_option < 1 || user_option > 6) {
			System.out.println("MAIN MENU");
			System.out.println("Please choose from the following options:");
			System.out.println("1: Auto Transaction");
			System.out.println("2: Driver License Registration");
			System.out.println("3: New Vehicle Registration");
			System.out.println("4: Violation Record");
			System.out.println("5: Search Engine");
			System.out.println("6: Exit");

			if (!user_input.hasNextInt()) {
				System.out.print("ERROR: ");
				System.out.println("Not an integer. Please enter an integer");
				System.out.println();
				user_input.next();
				continue;
			}

			user_option = user_input.nextInt();
			if (user_option > largest_value || user_option < smallest_value) {
				System.out.print("ERROR: ");
				System.out.println("Not a valid menu option. Please try again");
				System.out.println();
			}
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
				connection.createConncetion(oracle_username, oracle_password,
						oracle_url);
				connection_successful = true;
				System.out.println("Successful Connection to Oracle");
				System.out.println();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.print("ERROR: ");
				System.out.print(e.getMessage());
				System.out.println("Please enter credentials again");
				System.out.println();
			}
		}
	}

}
