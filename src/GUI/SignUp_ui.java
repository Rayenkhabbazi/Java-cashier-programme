package GUI;

import dao.AuthSignUp;
import dao.SingletonConnection;
import modele.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_ui extends JFrame {

	private Connection connection;

	private JTextField emailField;
	private JTextField phoneNumberField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;

	public SignUp_ui(Connection connection) {
		this.connection = connection;
		setTitle("Sign Up Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel emailLabel = new JLabel("Email:");
		JLabel phoneNumberLabel = new JLabel("Phone Number:");
		JLabel passwordLabel = new JLabel("Password:");
		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");

		emailField = new JTextField();
		phoneNumberField = new JTextField();
		passwordField = new JPasswordField();
		confirmPasswordField = new JPasswordField();

		JButton signUpButton = new JButton("Sign Up");
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = emailField.getText();
				String phoneNumber = phoneNumberField.getText();
				String password = new String(passwordField.getPassword());
				String confirmPassword = new String(confirmPasswordField.getPassword());

				if (email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
					JOptionPane.showMessageDialog(SignUp_ui.this, "Please fill in all fields.");
					return;
				}

				if (!isValidEmail(email)) {
					JOptionPane.showMessageDialog(SignUp_ui.this, "Invalid email format or ");
					return;
				}

				if (!isValidEmail(email)) {
					JOptionPane.showMessageDialog(SignUp_ui.this, "Invalid email format or ");
					return;
				}


				if (!isValidPassword(password)) {
					JOptionPane.showMessageDialog(SignUp_ui.this, "Password week ");
					return;
				}
				
				
				if (!isValidPhoneNumber(phoneNumber)) {
					JOptionPane.showMessageDialog(SignUp_ui.this, "Invalid phone number format");
					return;
				}


				// Validate input

				
				if (!password.equals(confirmPassword)) {
					JOptionPane.showMessageDialog(SignUp_ui.this, "Passwords do not match.");
					return;
				}

				// Create a client object
				Client client = new Client(email, phoneNumber, password);

				// Call AuthSignUp to sign up the client
				AuthSignUp authSignUp = new AuthSignUp(connection);
				if (authSignUp.signUp(client)) {
					JOptionPane.showMessageDialog(SignUp_ui.this, "Sign up successful.");
					dispose();
					new LoginUI(connection).setVisible(true);

					// Clear fields after successful sign-up
					emailField.setText("");
					phoneNumberField.setText("");
					passwordField.setText("");
					confirmPasswordField.setText("");
				} else {
					JOptionPane.showMessageDialog(SignUp_ui.this, "Failed to sign up. Please try again.");
				}
			}
		});

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginUI(connection).setVisible(true);
			}
		});

		panel.add(emailLabel);
		panel.add(emailField);
		panel.add(phoneNumberLabel);
		panel.add(phoneNumberField);
		panel.add(passwordLabel);
		panel.add(passwordField);
		panel.add(confirmPasswordLabel);
		panel.add(confirmPasswordField);
		panel.add(loginButton);
		panel.add(signUpButton);

		setContentPane(panel);
		setVisible(true);
	}

	private boolean isValidEmail(String email) {
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean isValidPhoneNumber(String phoneNumber) {
		// Accepts phone numbers in format xxx-xxx-xxxx or (xxx) xxx-xxxx
		String regex = "^\\+\\d{3}\\s\\d{8,15}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNumber);
		return matcher.matches();
	}

	private boolean isValidPassword(String password) {
		// "String regex =
		//P@ssw0rd 
		// "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";/
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Connection connection = SingletonConnection.getInstance();
				new SignUp_ui(connection);
			}
		});
	}
}
