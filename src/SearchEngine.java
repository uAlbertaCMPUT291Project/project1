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
			String query;
			ResultSet searchResultSet;
			if (userMenuInput==1){
				System.out.println("Enter a driver licence number");
				String licenceNo = scanner.nextLine();
				query = "SELECT " +
							"p.name, l.licence_no, p.addr, p.birthday, l.class, r.r_id, l.expiring_date " +
						"FROM " +
							"drive_licence l, people p, restriction r " +
						"WHERE " +
							"p.sin=l.sin AND l.licence_no=r.licence_no AND l.licence_no ='"+licenceNo+"'";
				searchResultSet = runQuery(query);
				while(searchResultSet.next()){
					System.out.println(
							searchResultSet.getString("name")+"  |  "+
							searchResultSet.getString("licence_no")+"  |  "+
							searchResultSet.getString("addr")+"  |  "+
							searchResultSet.getString("birthday")+"  |  "+
							searchResultSet.getString("class")+"  |  "+
							searchResultSet.getString("r_id")+"  |  "+
							searchResultSet.getString("expiring_date")
					);
				}
			}
			
			if (userMenuInput==2){
				System.out.println("Enter a name");
				String name = scanner.nextLine();
				query = "SELECT " +
							"p.name, l.licence_no, p.addr, p.birthday, l.class, r.r_id, l.expiring_date " +
						"FROM " +
							"drive_licence l, people p, restriction r " +
						"WHERE " +
							"p.sin=l.sin AND l.licence_no=r.licence_no AND p.name ='"+name+"'";
				searchResultSet = runQuery(query);
				while(searchResultSet.next()){
					System.out.println(
							searchResultSet.getString("name")+"  |  "+
							searchResultSet.getString("licence_no")+"  |  "+
							searchResultSet.getString("addr")+"  |  "+
							searchResultSet.getString("birthday")+"  |  "+
							searchResultSet.getString("class")+"  |  "+
							searchResultSet.getString("r_id")+"  |  "+
							searchResultSet.getString("expiring_date")
					);
				}			
				
			}
			
			if (userMenuInput==3){
				System.out.println("Enter a driver licence number");
				String licenceNo = scanner.nextLine();
				query = "SELECT " +
						"t.ticket_no, t.violator_no, t.vehicle_id, t.office_no, t.vtype, t.vdate, t.place, t.descriptions " +
					"FROM " +
						"drive_licence l, people p, ticket t " +
					"WHERE " +
						"p.sin=l.sin AND p.sin=t.violator_no AND l.licence_no ='"+licenceNo+"'";
				searchResultSet = runQuery(query);
				while(searchResultSet.next()){
					System.out.println(
							searchResultSet.getString("ticket_no")+"  |  "+
							searchResultSet.getString("violator_no")+"  |  "+
							searchResultSet.getString("vehicle_id")+"  |  "+
							searchResultSet.getString("office_no")+"  |  "+
							searchResultSet.getString("vtype")+"  |  "+
							searchResultSet.getString("vdate")+"  |  "+
							searchResultSet.getString("place")+"  |  "+
							searchResultSet.getString("descriptions")
					);
				}	
			}

			if (userMenuInput==4){
				System.out.println("Enter a sin");
				String sin = scanner.nextLine();
				query = "SELECT " +
						"t.ticket_no, t.violator_no, t.vehicle_id, t.office_no, t.vtype, t.vdate, t.place, t.descriptions " +
					"FROM " +
						"drive_licence l, people p, ticket t " +
					"WHERE " +
						"p.sin=l.sin AND p.sin=t.violator_no AND p.sin ='"+sin+"'";
				searchResultSet = runQuery(query);
				while(searchResultSet.next()){
					System.out.println(
							searchResultSet.getString("ticket_no")+"  |  "+
							searchResultSet.getString("violator_no")+"  |  "+
							searchResultSet.getString("vehicle_id")+"  |  "+
							searchResultSet.getString("office_no")+"  |  "+
							searchResultSet.getString("vtype")+"  |  "+
							searchResultSet.getString("vdate")+"  |  "+
							searchResultSet.getString("place")+"  |  "+
							searchResultSet.getString("descriptions")
					);
				}				
			}	
			

			if (userMenuInput==5){
				System.out.println("Enter a vehicle serial number");
				String serialNo = scanner.nextLine();
				query = "SELECT " +
						"COUNT(DISTINCT s.transaction_id) AS numberOfSale, AVG(s.price) AS averagePrice, COUNT(DISTINCT t.ticket_no) AS numberOfTicket " +
					"FROM " +
						"auto_sale s, vehicle v, ticket t " +
					"WHERE " +
						"s.vehicle_id=v.serial_no AND t.vehicle_id=v.serial_no AND v.serial_no ='"+serialNo+"'";
				searchResultSet = runQuery(query);
				while(searchResultSet.next()){
					System.out.println(
							"Number of sale: "+searchResultSet.getString("numberOfSale")+"\n"+
							"Average Price: "+searchResultSet.getString("averagePrice")+"\n"+
							"Number of violations involved: "+searchResultSet.getString("numberOfTicket")
					);
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
