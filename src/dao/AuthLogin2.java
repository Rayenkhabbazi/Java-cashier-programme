// AuthSignUp.java
package dao;

import modele.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.SingletonConnection;

public class AuthLogin2 {
	private Connection cnx;

	/*
	 * private Connection connection;
	 * 
	 * private Connection connection;
	 * 
	 * public AuthLogin2(Connection connection) { this.connection = connection; }
	 */public AuthLogin2() {

		cnx = SingletonConnection.getInstance();
	}

	 public int login(String email, String password) {
			String query = "SELECT id FROM clients WHERE email = ? AND password = ?";
			try {
				PreparedStatement statement = cnx.prepareStatement(query);
				statement.setString(1, email);
				statement.setString(2, password);
				ResultSet resultSet = statement.executeQuery();

				if (resultSet.next()) {
					// If a matching record is found, create a Client object with the retrieved data
					int id = resultSet.getInt("id");


					// Return true to indicate successful login
					return id;
				} else {
					// If no matching record is found, return false
					return -1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				// Return false in case of any exception
				return -1;
			}
		}

	/*
	 * public boolean login(String email, String password) { String query =
	 * "SELECT * FROM clients WHERE email = ? AND password = ?"; try {
	 * PreparedStatement Statement = cnx.prepareStatement(query);
	 * Statement.setString(1, email); Statement.setString(2, password); ResultSet
	 * resultSet = Statement.executeQuery();
	 * 
	 * return resultSet.next();
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); return false; } }
	 */
}
