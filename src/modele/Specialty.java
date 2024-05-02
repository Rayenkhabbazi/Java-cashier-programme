package modele;

import java.util.List;

public class Specialty {
    private String[] categories;

    // Constructor
    public Specialty(MenuItemList menuItemList) {
        this.categories = menuItemList.getAllCategories().toArray(new String[0]);
    }

    // Getter for categories
    public String[] getCategories() {
        return categories;
    }

    // Method to print categories
    public void printCategories() {
        for (String category : categories) {
            System.out.println(category);
        }
    }

    // Method to get the size of the categories array
    public int getCategoriesSize() {
        return categories.length;
    }

    // Method to get specialties
    public String[] getSpecialties() {
        return categories;
    }
}
