package dao;

import modele.MenuItemList;
import modele.MenuItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuItemDAO {
    private Connection cnx;

    // Method to select menu items from the database based on id_client and store
    // them in MenuItemList
    public MenuItemList selectMenuItemsByClientId(int id) {
        cnx = SingletonConnection.getInstance();
        MenuItemList menuItemList = new MenuItemList();
        String query = "SELECT * FROM menuitems WHERE id_client = ?";

        try {
            PreparedStatement statement = cnx.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int clientId = resultSet.getInt("id_client");

                // Create MenuItem object and add it to MenuItemList
                MenuItem menuItem = new MenuItem(itemId, name, category, price, clientId);
                menuItemList.addMenuItem(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Return the populated MenuItemList
        return menuItemList;
    }
}
