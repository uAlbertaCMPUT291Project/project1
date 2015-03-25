import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.sql.*;

//Leah
public class DriverLicenceRegistration extends ApplicationProgram {
	boolean existance = true;
	boolean licenceExist;
	boolean alreadyExists;
	String sin = null;
	Scanner user_input = new Scanner( System.in );
	@Override
	void run() {
		System.out.println("Welcome to Driver Licence Registration!\n");

		System.out.println("Please enter licence number. Enter 'exit' to quit.");
		String licence = user_input.nextLine();
		if (licence.equalsIgnoreCase("exit")) {
			System.out.println("You have requested to exit");
			return;
		}
		
		//check if licence number already exists
		alreadyExists = licenceExists(licence);
		if (alreadyExists==true){
			System.out.println("That licence number already exists");
			System.out.println("Request failed");
			return;
		}
		
		
		System.out.println("Please enter sin. Enter 'exit' to quit.");
		sin = user_input.nextLine();
		if (sin.equalsIgnoreCase("exit")) {
			System.out.println("You have requested to exit");
			return;
		}
		//check if person already has a licence
		licenceExist = alreadyHasLicence(sin);
		if (licenceExist == true){
			//person already has a licence
			System.out.println("That person already has a licence.");
			System.out.println("Request failed");
			//System.out.println("You will now return to the main menu.");
			return;
		}
		//check if person exists, if not add them.
		addPerson(sin);
		
		System.out.println("Please enter class. Enter 'exit' to quit ");
		String cLass = user_input.nextLine();
		if (cLass.equalsIgnoreCase("exit")) {
			System.out.println("You have requested to exit");
			return;
		}
		
		System.out.println("Please enter photo file path. Enter 'exit' to quit.");
		String filePath = user_input.nextLine();
		if (filePath.equalsIgnoreCase("exit")) {
			System.out.println("You have requested to exit");
			return;
		}
		
		System.out.println("Please enter issuing date (YYYY-MM-DD): ");
		String issueDate = user_input.nextLine();
		if (issueDate.equalsIgnoreCase("exit")) {
			System.out.println("You have requested to exit");
			return;
		}
		
		System.out.println("Please enter expiring date (YYYY-MM-DD): ");
		String expireDate = user_input.nextLine();
		if (expireDate.equalsIgnoreCase("exit")) {
			System.out.println("You have requested to exit");
			return;
		}
		try{
			PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(
					"insert into drive_licence values ('"+licence+"', '"+sin+"', '"
					+cLass+"', ?, TO_DATE('"+issueDate+"', 'YYYY-MM-DD'), TO_DATE('"
					+expireDate+"', 'YYYY-MM-DD'))");
			File file = new File(filePath);
			stmt.setBinaryStream(1, new FileInputStream(file), (int)file.length());
			stmt.executeUpdate();
			System.out.println("Licence added");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
	}

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
					//e.printStackTrace();
					System.out.print(e.getMessage());
				}
			}
		}
	}
	
	private boolean alreadyHasLicence(String sin){
		try {
			Statement stmt = DatabaseConnection.getConnection()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = "select * from drive_licence where lower(sin) = lower('"
					+ sin+ "')";
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
	
	private boolean licenceExists(String licence_no){
		try {
			Statement stmt = DatabaseConnection.getConnection()
					.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			String query = "select * from drive_licence where lower(licence_no) = lower('"
					+ licence_no+ "')";
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
