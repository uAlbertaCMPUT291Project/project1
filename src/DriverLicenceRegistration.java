import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.sql.*;

//Leah
public class DriverLicenceRegistration extends ApplicationProgram {
	boolean existance = false;
	String sin = null;
	int licence = 0;
	@Override
	void run() {
		Scanner user_input = new Scanner( System.in );
		//make sure licence  number does not exists - generate licence number
		//make sure sin does exist. If not add person
		//System.out.println("Enter licence number:");
		//String licence = user_input.nextLine();
		
		while (existance){
			System.out.println("Enter sin:");
			sin = user_input.nextLine();
			existance = checkPersonExistance(sin);
		}
		System.out.println("Enter class: ");
		String cLass = user_input.nextLine();
		System.out.println("Enter photo file path: ");
		String filePath = user_input.nextLine();
		System.out.println("Enter issuing date (YYYY-MM-DD): ");
		String issueDate = user_input.nextLine();
		System.out.println("Enter expiring date (YYYY-MM-DD): ");
		String expireDate = user_input.nextLine();
		
		System.out.println("insert into drive_licence values ('"+licence+"', '"+sin+"', '"
				+cLass+"', ?, TO_DATE('"+issueDate+"', 'YYYY-MM-DD'), TO_DATE('"
				+expireDate+"', 'YYYY-MM-DD'))");
		
		try{
			PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(
					"insert into drive_licence values ('"+licence+"', '"+sin+"', '"
					+cLass+"', ?, TO_DATE('"+issueDate+"', 'YYYY-MM-DD'), TO_DATE('"
					+expireDate+"', 'YYYY-MM-DD'))");
			File file = new File(filePath);
			stmt.setBinaryStream(1, new FileInputStream(file), (int)file.length());
			stmt.executeUpdate();
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
	
}
