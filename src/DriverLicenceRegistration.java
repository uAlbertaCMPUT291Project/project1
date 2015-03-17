import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.sql.*;

//Leah
public class DriverLicenceRegistration extends ApplicationProgram {

	@Override
	void run() {
		Scanner user_input = new Scanner( System.in );
		//make sure licence  number does not exists
		//make sure sin does exist. If not add person (?)
		System.out.println("Enter licence number:");
		String licence = user_input.nextLine();
		System.out.println("Enter sin:");
		String sin = user_input.nextLine();
		System.out.println("Enter class:");
		String cLass = user_input.nextLine();
		System.out.println("Enter photo file path:");
		String filePath = user_input.nextLine();
		System.out.println("Enter issuing date:");
		String issueDate = user_input.nextLine();
		System.out.println("Enter expiring date:");
		String expireDate = user_input.nextLine();
		
		try{
			PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(
					"insert into drive_licence values ('"+licence+"', '"+sin+"', '"
					+cLass+"', ?, TO_DATE('"+issueDate+"', 'YYYY-MM-DD', TO_DATE('"
					+expireDate+", 'YYYY-MM-DD')");
			File file = new File(filePath);
			stmt.setBinaryStream(1, new FileInputStream(file), (int)file.length());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
	}

}
