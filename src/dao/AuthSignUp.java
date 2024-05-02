// AuthSignUp.java
package dao;

import modele.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthSignUp {

	private Connection connection;

	public AuthSignUp(Connection connection) {
		this.connection = connection;
	}

	public boolean signUp(Client client) {
		String query = "INSERT INTO clients (email, phone_number, password) VALUES (?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, client.getEmail());
			preparedStatement.setString(2, client.getPhoneNumber());
			preparedStatement.setString(3, client.getPassword());
			int rowsInserted = preparedStatement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
