import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//Jim
public class SearchEngine extends ApplicationProgram {

	@Override
	void run() {
		try {
			int userMenuInput = getMenuInput();		
			Scanner scanner = new Scanner(System.in);
			

			if (userMenuInput==1){
				System.out.println("Enter adriver licence number");
				String licenceNo = scanner.nextLine();
				String query = "SELECT name,licence_no FROM drive_licence l, people p WHERE p.sin=l.sin AND l.licence_no ="+licenceNo;
				ResultSet searchResultSet = runQuery(query);
				while(searchResultSet.next()){
					System.out.println(searchResultSet.getString("name")+" "+searchResultSet.getString("licence_no"));
				}
			}
			

			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[Database Error] Searching Engine terminated, sending user back to main menu.");
			return;
		}
	}
	
	/*private ResultSet getDriveLicenceByLicenceNo(String licenceNo){
		return runQuery("select ");
	}*/
	
	
	
	
	
	private int getMenuInput() {

		Scanner scanner = new Scanner(System.in);
		int userInput = -1;

		while (userInput < 1 || userInput > 6) {
			System.out.println("Search Engine");
			System.out.println("Please choose from the following options:");
			System.out.println("1: Search drive licence by driver licence number");
			System.out.println("2: Search drive licence by given name");
			System.out.println("3: Search violation record by driver licence number");
			System.out.println("4: Search violation record by SIN");
			System.out.println("5: Search vehicle history by vehicle serial number ");
			System.out.println("6: Back to main menu");

			if (!scanner.hasNextInt()) {
				System.out.print("ERROR: ");
				System.out.println("Not an integer. Please enter an integer");
				System.out.println();
				scanner.next();
				continue;
			}

			userInput = scanner.nextInt();
			if (userInput > 6 || userInput < 1) {
				System.out.print("ERROR: ");
				System.out.println("Not a valid menu option. Please try again");
				System.out.println();
			}
		}

		return userInput;
	}
	
	
	/*
	 * Description: Helper function that runs a given query.
	 * Input: String query, 
	 * Output: ResultSet resultSet.
	 */
	private ResultSet runQuery(String query) throws SQLException{
		Connection connection = DatabaseConnection.getConnection();
		ResultSet resultSet = connection.createStatement().executeQuery(query);
		return resultSet;		
	}
}
