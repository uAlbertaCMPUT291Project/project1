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
	// the transaction date
	String transactionDate;
	// the price
	String price;
	// transaction ID
	int transactionID;

	@Override
	void run() {

		System.out
				.println("Welcome to the Auto Transaction Section of the program\n");
		String mainloop = "not exit";
		outerloop:
		while (!mainloop.equalsIgnoreCase("exit")) {

			sellerSIN = null;
			buyerSIN = null;
			vehicleSerial = null;
			transactionDate = null;
			price = null;
			transactionID = 0;

			// get vehicle serial number
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

			vehicleSerial = vehicleSerial.toUpperCase();

			// get the seller sin
			sellerSIN = getSellerfromUser(vehicleSerial);
			if (sellerSIN == null) {
				System.out.println("You have requested to exit");
				return;
			}

			sellerSIN = sellerSIN.toUpperCase();

			// get buyer SIN
			while (buyerSIN == null) {
				String input = getStringfromUser("Please enter the buyer name. Enter 'exit' to quit");
				String nameEntered = input;
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
						buyerSIN = addPerson(nameEntered);
						if (buyerSIN == null) {
							System.out.println("Error creating new buyer");
						}
						else if (buyerSIN.equalsIgnoreCase("exit")) {
							System.out.println("You have requested to exit");
							return;
						}
						 
					}
				} else if (buyerSIN.equalsIgnoreCase("exit")) {
					System.out.println("You have requested to exit");
					return;
				}
			}

			buyerSIN = buyerSIN.toUpperCase();

			// get date from user
			while (transactionDate == null) {
				String input = getStringfromUser("Please enter the transaction date in the form yyyy-mm-dd (eg. enter 2014-08-30 for August 30, 2014). Enter 'exit' to quit");
				if (input.equalsIgnoreCase("exit")) {
					System.out.println("You have requested to exit");
					return;
				}

				transactionDate = input;
			}

			// get the price from the user
			while (price == null) {
				String input = getStringfromUser("Please enter the price of the transaction (eg. 15469.47). Enter 'exit' to quit");
				if (input.equalsIgnoreCase("exit")) {
					System.out.println("You have requested to exit");
					return;
				}
				price = input;
			}
			// get the next transaction ID
			transactionID = getNextTransactionID();

			boolean result = createNewTransaction();
			if (!result) {
				System.out
						.println("The auto transaction was not succesfull. No data was changed, now exiting.");
			}

			result = addNewOwnerAndRemoveOthers();
			if (!result) {
				System.out
						.println("Auto Transaction completed successfully but could not add new owner or remove previous owners.");
			}

			System.out.println("Transaction has been entered and updated successfully");

			mainloop = getStringfromUser("Please enter 'exit' to go back to the main menu or any key to enter another transaction");

		}

	}

	private boolean addNewOwnerAndRemoveOthers() {
		Statement stmt;

		try {
			stmt = DatabaseConnection.getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			// add new owner
			String query = "INSERT into owner values ('" + buyerSIN + "', '"
					+ vehicleSerial + "', 'y')";
			stmt.executeUpdate(query);

			// delete other owners
			String delete_command = "DELETE from owner WHERE vehicle_id = '"
					+ vehicleSerial + "' AND owner_id <> '" + buyerSIN + "'";
			stmt.executeUpdate(delete_command);

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	private boolean createNewTransaction() {
		Statement stmt;

		try {
			stmt = DatabaseConnection.getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			String update = "INSERT into auto_sale values (" + transactionID
					+ ", '" + sellerSIN + "', '" + buyerSIN + "', '"
					+ vehicleSerial + "', date '" + transactionDate + "', "
					+ price + ")";

			stmt.executeUpdate(update);

		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return false;
		}
		return true;

	}

	// returns 1 + the current max transaction ID
	private int getNextTransactionID() {

		Statement stmt;
		int next_transactionID = 0;

		try {
			stmt = DatabaseConnection.getConnection().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			String query = "SELECT MAX(transaction_id) FROM auto_sale";
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			next_transactionID = rs.getInt("MAX(TRANSACTION_ID)");
		} catch (SQLException e) {

			System.out.println(e.getMessage());
			return -1;
		}
		return next_transactionID + 1;
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
			System.out.println(i + 1 + ":\tSelect " + (i + 1)
					+ " to exit and cancel the transaction");
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
			return false;
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
	 * if no such person exists, null is returned if multiple people have the
	 * same name the user is prompted to select which person they want
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
			System.out.println(i + 1 + ":\tSelect " + (i + 1)
					+ " to exit and cancel the transaction");
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
			return null;
		}
	}

	// adds a person if they do not exist
	private String addPerson(String name) {
		
		// check if person exists
		// add the person if they do not exist
		String sin = getStringfromUser("Enter " + name + "'s sin");
		
		while(checkPersonExistance(sin)) {
			sin = getStringfromUser("That sin already exisits. Enter a new SIN or 'exit' to quit");
			if (sin.equalsIgnoreCase("exit")) {
				return "exit";
			}
		}
		
		String height = getStringfromUser("Enter " + name + "'s height");
		String weight = getStringfromUser("Enter " + name + "'s weight");
		String eyecolor = getStringfromUser("Enter " + name + "'s eye color");
		String haircolor = getStringfromUser("Enter " + name + "'s hair color");
		String address = getStringfromUser("Enter " + name + "'s address");
		String gender = getStringfromUser("Enter " + name + "'s gender (m/f)");
		String birthday = getStringfromUser("Enter " + name + "'s birthday (YYYY-MM-DD)");

		try {
			Statement statement = DatabaseConnection.getConnection()
					.createStatement();
			String peopleStmt = "insert into people values ('" + sin + "', '"
					+ name + "', " + height + ", " + weight + ", '" + eyecolor
					+ "', '" + haircolor + "', '" + address + "', '" + gender
					+ "', TO_DATE('" + birthday + "', 'YYYY-MM-DD'))";
			statement.executeUpdate(peopleStmt);
			System.out.println(name + " has been added to database and will be used as the new buyer");
			
			return sin;
		} catch (SQLException e) {
			System.out.print(e.getMessage());
			return null;
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

}
