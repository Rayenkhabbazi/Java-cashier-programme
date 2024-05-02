package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDAO {
    private Connection connection;

    public OrderDAO() {
        connection = SingletonConnection.getInstance();
    }

    private static final String FILE_NAME = "orders.txt";

    public void readOrdersAndInsertIntoDatabase(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("Order Details:")) {
                    int clientId = Integer.parseInt(reader.readLine().split(": ")[1]);
                    int ticketNumber = Integer.parseInt(reader.readLine().split(": ")[1]);
                    String dateString = reader.readLine().split(": ")[1];
                    // Parse date string into Date object, you can use SimpleDateFormat
                    double totalPrice = Double.parseDouble(reader.readLine().split(": ")[1].substring(1)); // Remove $
                    
                    // Read items until blank line
                    StringBuilder itemsBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        itemsBuilder.append(line).append("\n");
                    }
                    String itemsSelected = itemsBuilder.toString().trim(); // Remove trailing newline
                    
                    // Insert into database
                    insertOrderIntoDatabase(clientId, ticketNumber, dateString, totalPrice, itemsSelected);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertOrderIntoDatabase(int clientId, int ticketNumber, String dateString, double totalPrice, String itemsSelected) throws SQLException {
        // Parse dateString into the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");
        String formattedDateString = null;
        try {
            Date date = dateFormat.parse(dateString);
            // Format the date to the desired format for database insertion
            SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formattedDateString = dbDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parse exception or throw it to the caller
            throw new SQLException("Error parsing date string: " + dateString);
        }

        String query = "INSERT INTO orders (client_id, ticket_number, order_date, total_price, items_selected) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, clientId);
            statement.setInt(2, ticketNumber);
            statement.setString(3, formattedDateString); // Use the formatted date string
            statement.setDouble(4, totalPrice);
            statement.setString(5, itemsSelected);
            statement.executeUpdate();
        }
    }

}
