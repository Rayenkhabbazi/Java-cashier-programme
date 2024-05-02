package dao;

import modele.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.SingletonConnection;

public class Readitmes {
	private Connection cnx;

	/*
	 * private Connection connection;
	 * 
	 * private Connection connection;
	 * 
	 * public AuthLogin2(Connection connection) { this.connection = connection; }
	 */public Readitmes() {

		cnx = SingletonConnection.getInstance();
	}

	public boolean Readitmes(String email, String password) {
		String query = "SELECT * FROM clients WHERE email = ? AND password = ?";
		try {
			PreparedStatement Statement = cnx.prepareStatement(query);
			Statement.setString(1, email);
			Statement.setString(2, password);
			ResultSet resultSet = Statement.executeQuery();

			return resultSet.next();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}