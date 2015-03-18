import java.sql.*;
import java.util.Scanner;

//Author: Nelson
/*
 * This component is used to complete an auto transaction. 
 * Your program shall allow the officer to enter all necessary information 
 * to complete this task, including, but not limiting to, the details about 
 * the seller, the buyer, the date, and the price. 
 * The component shall also remove the relevant information of the previous ownership.
 */

public class AutoTransaction extends ApplicationProgram {

	// the sellers SIN
	String sellerSIN;
	// the buyers SIN
	String buyerSIN;
	// the vehicles serial number
	String vehicleSerial;

	@Override
	void run() {

		sellerSIN = null;
		buyerSIN = null;
		vehicleSerial = null;

		System.out
				.println("Welcome to the Auto Transaction Section of the program\n");

		while (vehicleSerial == null) {
			String input = getStringfromUser("Please enter the vehicle serial number that is being sold. Enter 'exit' to quit");
			if (input.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			}
			if (!checkVehicleSerial(input)) {
				System.out
						.println("That vehicle serial number does not match any records.");
			} else {
				vehicleSerial = input;
			}
		}

		sellerSIN = getSellerfromUser(vehicleSerial);
		if (sellerSIN == null) {
			System.out.println("You have requested to exit");
			return;
		}

		while (buyerSIN == null) {
			String input = getStringfromUser("Please enter the buyer name. Enter 'exit' to quit");
			if (input.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			}
			buyerSIN = getSINfromName(input);
			if (buyerSIN == null) {
				System.out.print("No such buyer exists. ");

				input = getStringfromUser("Enter 'new' to add buyer to database, 'exit' to quit, or anything else to enter a different buyer.");
				if (input.equalsIgnoreCase("exit")) {
					System.out.println("You have requested to exit");
					return;
				} else if (input.equalsIgnoreCase("new")) {
					System.out.println("implement add buyer yet");
				}
			}
			else if (buyerSIN.equalsIgnoreCase("exit")) {
				System.out.println("You have requested to exit");
				return;
			}
		}

		System.out.println("Seller: " + sellerSIN + "Buyer: " + buyerSIN);

	}

	/*
	 * returns the serial number of the user chosen seller for a given vehicle
	 * serial number;
	 * 
	 * the vehicle has to exist and then the vehicle automatically has at least
	 * one owner returns null if the user wants to exit
	 */
	private String getSellerfromUser(String vehicleSerial) {
		Scanner user_input = new Scanner(System.in);
		int user_option = -1;

		try {
			Statement stmt = DatabaseConnection.getConnection()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = "SELECT p.sin, p.name, o.is_primary_owner "
					+ "FROM people p, vehicle v, owner o "
					+ "WHERE p.sin = o.owner_id AND v.serial_no = o.vehicle_id "
					+ "AND lower(v.serial_no) = lower('" + vehicleSerial + "')";

			ResultSet rs = stmt.executeQuery(query);

			rs.last();
			int returned_rows = rs.getRow();
			rs.first();

			// print out all owners of a vehicle
			System.out
					.println("Please choose the seller from the list of current owners");
			int i = 1;
			System.out.println("  \tSerial Number  Primary Owner\tName");
			System.out.println(i + ":\t" + rs.getString("sin")
					+ rs.getString("is_primary_owner") + "\t\t"
					+ rs.getString("name"));
			while (rs.next()) {
				i++;
				System.out.println(i + ":\t" + rs.getString("sin")
						+ rs.getString("is_primary_owner") + "\t\t"
						+ rs.getString("name"));
			}
			System.out.println(i + 1 + ":\tSelect me to exit and cancel the transaction");
			// get user to select from current owners
			while (user_option < 1 || user_option > returned_rows + 1) {
				if (!user_input.hasNextInt()) {
					user_input.next();
					continue;
				}
				user_option = user_input.nextInt();
			}

			if (user_option == returned_rows + 1) {
				return null;
			}

			rs.absolute(user_option);

			return rs.getString("sin");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/*
	 * checks if a given serial number for a vehicle exists in the database
	 * 
	 * returns true or false depending on the result
	 */
	private boolean checkVehicleSerial(String vehicleSerialNUmber) {

		try {
			Statement stmt = DatabaseConnection.getConnection()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = "select * from vehicle where lower(serial_no) = lower('"
					+ vehicleSerialNUmber + "')";
			ResultSet rs = stmt.executeQuery(query);

			rs.last();
			if (rs.getRow() != 1) {
				return false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	/*
	 * prompts the user to enter a string the message prompt is promptString
	 * 
	 * no checking is done on the input
	 */
	private String getStringfromUser(String promptString) {

		Scanner input = new Scanner(System.in);
		System.out.println(promptString);
		return input.nextLine();
	}

	/*
	 * trys to find the SIN from the inputed name
	 * 
	 * if no such person exists, null is returned 
	 * if multiple people have the same name the user is prompted to select which person they want
	 */
	private String getSINfromName(String name) {

		Scanner user_input = new Scanner(System.in);
		int user_option = -1;

		try {
			Statement stmt;
			int returned_rows = 0;

			stmt = DatabaseConnection.getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			String query = "select sin, name, gender, birthday from people where lower(name) = lower('"
					+ name + "')";
			ResultSet rs = stmt.executeQuery(query);

			rs.last();
			returned_rows = rs.getRow();
			if (rs.getRow() < 1) {
				return null;
			}
			rs.first();

			// print out all sins with given name
			System.out
					.println("Please confirm the buyer from the following list");
			int i = 1;
			System.out.println("  \tSerial Number  Gender\tBirthday\tName");
			System.out.println(i + ":\t" + rs.getString("sin")
					+ rs.getString("gender") + "\t" + rs.getDate("birthday")
					+ "\t" + rs.getString("name"));
			while (rs.next()) {
				i++;
				System.out.println(i + ":\t" + rs.getString("sin")
						+ rs.getString("gender") + "\t"
						+ rs.getDate("birthday") + "\t" + rs.getString("name"));
			}
			System.out.println(i + 1 + ":\tSelect me to exit and cancel the transaction");
			// get user to select from current owners
			while (user_option < 1 || user_option > returned_rows + 1) {
				if (!user_input.hasNextInt()) {
					user_input.next();
					continue;
				}
				user_option = user_input.nextInt();
			}

			if (user_option == returned_rows + 1) {
				return "exit";
			}

			rs.absolute(user_option);

			return rs.getString("sin");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return null;
	}

	/*
	 * String query = "select * from movie"; ResultSet rs =
	 * stmt.executeQuery(query);
	 * 
	 * while (rs.next()) { System.out.println(rs.getString("title") + ", " +
	 * rs.getInt("movie_number")); }
	 */

}
