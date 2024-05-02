package modele;

public class Client {
	private int id;

	private String email;
	private String phoneNumber;
	private String password;

	public Client(String email, String phoneNumber, String password) {
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public Client(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Client(int id) {
		this.id = id;

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;

	}

	/*
	 * public void setId(int id) { this.id = id; }
	 */
}
