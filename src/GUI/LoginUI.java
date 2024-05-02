package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import dao.AuthLogin2;
import dao.MenuItemDAO;
import modele.Client;
import modele.MenuItemList;
import modele.Specialty;

public class LoginUI extends JFrame {
	private Connection connection;
	public static int id ;

	private JTextField emailField;
	private JPasswordField passwordField;

	public LoginUI(Connection connection) {
		this.connection = connection;

		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 200);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel emailLabel = new JLabel("Email:");
		JLabel passwordLabel = new JLabel("Password:");

		emailField = new JTextField();
		passwordField = new JPasswordField();

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				String password = new String(passwordField.getPassword());

				if (email.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(LoginUI.this, "Please enter both email and password");
					return;
				}

				AuthLogin2 authLogin = new AuthLogin2();
				int id = authLogin.login(email, password);
				
				if (id>-1) {
					JOptionPane.showMessageDialog(LoginUI.this, "Login successful ");
					dispose();
					MenuItemDAO menuItemDAO = new MenuItemDAO();
					
					MenuItemList menuItemList = menuItemDAO.selectMenuItemsByClientId(id);
					
					
					new Caissa_UI(connection,menuItemList,id).setVisible(true);

					// Add code here to open the main application window or perform other actions
				} else {
					JOptionPane.showMessageDialog(LoginUI.this, "Invalid email or password");
				}
			}
		});

		JButton signUpButton = new JButton("Sign Up");
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				SignUp_ui signUp_ui = new SignUp_ui(null);
				signUp_ui.setVisible(true);
				// Add code here to open the sign-up window
			}
		});

		panel.add(emailLabel);
		panel.add(emailField);
		panel.add(passwordLabel);
		panel.add(passwordField);
		panel.add(loginButton);
		panel.add(signUpButton);

		setContentPane(panel);
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Create a connection to your database
				// Connection connection = YourDatabaseConnection.getConnection();
				Connection connection = null; // Initialize your connection here
				LoginUI login = new LoginUI(connection);
			}
		});
	}
}
