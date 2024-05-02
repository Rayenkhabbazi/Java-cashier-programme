// DAO class: AuthLogin.java
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthLogin {
	public static boolean login(String email, String password) {
		String query = "SELECT * FROM clients WHERE email = ? AND password = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next(); // Returns true if there is a matching user, false otherwise
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
