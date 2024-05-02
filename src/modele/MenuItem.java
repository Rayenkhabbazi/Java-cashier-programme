package modele;

// Model class for menu item
public class MenuItem {
	private int id;
	private String name;
	private String category;
	private double price;
	private int id_client;

	// Constructor
	public MenuItem(int id,String name,String category, double price, int id_client) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
		this.id_client = id_client;
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getId_client() {
		return id_client;
	}

	public void setId_client(int id_client) {
		this.id_client = id_client;
	}
}
