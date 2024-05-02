package modele;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuItemList {
    // List to store menu items
    public static List<MenuItem> menuItemList;

    // Constructor
    public MenuItemList() {
        this.menuItemList = new ArrayList<>();
    }

    public MenuItemList(List<MenuItem> l) {
        this.menuItemList = l;
    }

    // Method to add a menu item to the list
    public void addMenuItem(MenuItem menuItem) {
        menuItemList.add(menuItem);
    }

    // Method to remove a menu item from the list
    public void removeMenuItem(MenuItem menuItem) {
        menuItemList.remove(menuItem);
    }

    // Method to get all menu items
    public List<MenuItem> getMenuItems() {
        return menuItemList;
    }

    // Method to get the length of the menu item list
    public int length() {
        return menuItemList.size();
    }

    // Method to get a menu item by its ID
    public MenuItem getMenuItemById(int id) {
        for (MenuItem menuItem : menuItemList) {
            if (menuItem.getId() == id) {
                return menuItem;
            }
        }
        return null; // If the menu item with the specified ID is not found
    }

    // Method to update a menu item
    public void updateMenuItem(MenuItem updatedMenuItem) {
        for (int i = 0; i < menuItemList.size(); i++) {
            MenuItem menuItem = menuItemList.get(i);
            if (menuItem.getId() == updatedMenuItem.getId()) {
                menuItemList.set(i, updatedMenuItem);
                return;
            }
        }
    }

    // Method to get all categories
    public static List<String> getAllCategories() {
        Set<String> uniqueCategories = new HashSet<>();
        for (MenuItem menuItem : menuItemList) {
            uniqueCategories.add(menuItem.getCategory());
        }
        return new ArrayList<>(uniqueCategories);
    }

    // Method to print menu list in serialized form
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MenuItem menuItem : menuItemList) {
            sb.append(menuItem.getName()).append("\n");
        }
        return sb.toString();
    }

    // Method to get items by category
    public List<Object[]> getItemsByCategory(String category) {
        List<Object[]> itemsTable = new ArrayList<>();
        for (MenuItem menuItem : menuItemList) {
            if (menuItem.getCategory().equals(category)) {
                Object[] itemRow = {menuItem.getName(), menuItem.getPrice()};
                itemsTable.add(itemRow);
            }
        }
        return itemsTable;
    }
}
