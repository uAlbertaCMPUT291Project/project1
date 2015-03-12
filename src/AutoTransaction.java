import java.sql.*;

//Author: Nelson

public class AutoTransaction extends ApplicationProgram {

	@Override
	void run() {
		// TODO Auto-generated method stub

		try {
			Statement stmt = DatabaseConnection.getConnection().createStatement();

			String command = "drop table movie";
			stmt.executeUpdate(command);

			command = "create table movie(title char(20), movie_number integer, primary key(movie_number))";
			stmt.executeUpdate(command);

			command = "insert into movie values ('movie to see', 1)";
			stmt.executeUpdate(command);

			command = "insert into movie values ('movie2', 2)";
			stmt.executeUpdate(command);

			String query = "select * from movie";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				System.out.println(rs.getString("title") + ", "
						+ rs.getInt("movie_number"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
