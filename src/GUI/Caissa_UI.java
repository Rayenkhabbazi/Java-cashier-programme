package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modele.MenuItemList;
import modele.Specialty;
import modele.Client;
import dao.AuthSignUp;
import dao.OrderDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import com.itextpdf.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysql.cj.xdevapi.Table;


public class Caissa_UI extends JFrame {
    private JButton startButton;
    private static Connection connection;
    private JButton closeButton;
    private JButton payment;
    private JButton deleteButton;
    private JPanel specialtyPanel;
    private JPanel itemListPanel;
    private MenuItemList menuItemList;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> percentageComboBox;
    private JLabel clockLabel;
    private Timer timer;
    private boolean x;
    private boolean isButtonClicked = false;
    private JLabel totalPriceLabel;
    private int commandCount=1;
    private JLabel commandCountLabel;
	private static int clientId;
	

    public Caissa_UI(Connection connection,MenuItemList menuItemList,int id) {
    	this.connection = connection;
    	this.clientId = id;
        this.menuItemList = menuItemList;
        x = false;
        setTitle("Cashier Interface");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel for start and close buttons in the north
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startButton = new JButton("Start");
        closeButton = new JButton("Close");
        clockLabel = new JLabel();
        northPanel.add(clockLabel);
        northPanel.add(startButton);
        northPanel.add(closeButton);
        Specialty sp = new Specialty(menuItemList);

        // Panel for specialties in the west
        specialtyPanel = new JPanel(new GridLayout(0, 1));

        itemListPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane itemListScrollPane = new JScrollPane(itemListPanel);

        JPanel eastPanel = new JPanel(new BorderLayout());
        JPanel modifyPanel = createModifyPanel();
        eastPanel.add(modifyPanel, BorderLayout.SOUTH);
        
        
        
        
        
        commandCountLabel = new JLabel("Number of Tickets: ");
        northPanel.add(commandCountLabel, BorderLayout.NORTH);



        // Initialize JTable
        
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Price");
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        eastPanel.add(tableScrollPane, BorderLayout.NORTH);
        // Add panels to the main panel
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(specialtyPanel, BorderLayout.WEST);
        mainPanel.add(itemListScrollPane, BorderLayout.CENTER);
        mainPanel.add(eastPanel, BorderLayout.EAST);

        getContentPane().add(mainPanel);

        // Delete Item button
        JPanel deleteAndDiscountPanel = new JPanel(new BorderLayout());
        JPanel deleteButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deleteButton = new JButton("Delete Item");
        deleteButtonPanel.add(deleteButton);
        deleteAndDiscountPanel.add(deleteButtonPanel, BorderLayout.NORTH);
        deleteAndDiscountPanel.add(modifyPanel, BorderLayout.CENTER);
        eastPanel.add(deleteAndDiscountPanel, BorderLayout.CENTER);

        JPanel totalPriceAndPaymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPriceLabel = new JLabel("Total Price: $0.00");
        payment = new JButton("Payment");
        totalPriceAndPaymentPanel.add(totalPriceLabel);
        totalPriceAndPaymentPanel.add(payment);
        eastPanel.add(totalPriceAndPaymentPanel, BorderLayout.SOUTH);
        
        
        
        payment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Increment command count
                updateCommandCount();
                // Get items selected
                List<String> itemsSelected = getSelectedItems();
                // Get total price
                double totalPrice = calculateTotalPrice();
                // Get ticket number
                int ticketNumber = commandCount; // Assuming commandCount is the ticket number
                // Get current date
                Date date = new Date(); // Current date and time
                // Get client ID
                int clientId = id; // Assuming clientIdField is a JTextField for entering client ID

                // Save the data to a file
                saveToFile(itemsSelected, totalPrice, ticketNumber, date, clientId);

                // Generate a PDF receipt
               /* generateReceiptPDF(ticketNumber, itemsSelected, totalPrice, date, clientId);*/
                
                // Clear the JTable
                clearTable();
            }
        });

        

        setEnabledAllComponents(false);
        specialtyPanel.setEnabled(false);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isButtonClicked = true;
                if (x == false) {
                    addSpecialties(sp.getSpecialties());
                    x = true;
                }
                startClock();
                setEnabledAllComponents(true);
                specialtyPanel.setEnabled(true);
            }
        });
        
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopClock();
                itemListPanel.removeAll();
                setEnabledAllComponents(false);
                
                // Create an instance of OrderDAO with the connection object
                OrderDAO orderDAO = new OrderDAO();

                
                // Call the readOrdersAndInsertIntoDatabase method
                orderDAO.readOrdersAndInsertIntoDatabase(FILE_PATH);
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                    updateTotalPrice(); // Update total price after removing an item
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Double-click detected, you can implement modification logic here
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // Implement modification logic here
                        // For example, displaying a dialog box to input new price
                        // Or allowing selection of percentage change
                    }
                }
            }
        });
        
        

    }
    private void updateCommandCount() {
    	commandCount++;
        commandCountLabel.setText("Number of Commands: " + commandCount);
    }
    private JPanel createModifyPanel() {
        JPanel modifyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel modifyLabel = new JLabel("Apply Discount:");
        percentageComboBox = new JComboBox<>(new String[]{"5%", "10%", "15%", "20%"});
        JButton applyDiscountButton = new JButton("Apply");
        applyDiscountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedPercentage = (String) percentageComboBox.getSelectedItem();
                    double percentage = Double.parseDouble(selectedPercentage.replace("%", "")) / 100.0;
                    double currentPrice = (double) tableModel.getValueAt(selectedRow, 1);
                    double discount = currentPrice * percentage;
                    double discountedPrice = currentPrice - discount;
                    tableModel.setValueAt(discountedPrice, selectedRow, 1);
                    updateTotalPrice(); // Update total price after applying discount
                }
            }
        });

        modifyPanel.add(modifyLabel);
        modifyPanel.add(percentageComboBox);
        modifyPanel.add(applyDiscountButton);
        return modifyPanel;
    }

    private void startClock() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date now = new Date();
                clockLabel.setText(sdf.format(now));
            }
        });
        timer.start();
    }

    private void stopClock() {
        if (timer != null) {
        	itemListPanel.removeAll();
            timer.stop();
        }
    }

    private void addSpecialties(String[] specialties) {

        for (String specialty : specialties) {
            JButton button = new JButton(specialty);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    displayItemsByCategory(specialty);
                }
            });

            specialtyPanel.add(button);
        }
    }

    private void displayItemsByCategory(String category) {
        itemListPanel.removeAll(); // Clear previous items
        List<Object[]> items = menuItemList.getItemsByCategory(category);
        for (Object[] item : items) {
            String itemName = (String) item[0];
            double itemPrice = (double) item[1];
            JButton itemButton = new JButton(itemName + " - $" + itemPrice);
            itemButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addSelectedItemToTable(itemName, itemPrice);
                }
            });
            itemListPanel.add(itemButton);
        }
        itemListPanel.revalidate();
        itemListPanel.repaint();
    }

    private void setEnabledAllComponents(boolean enabled) {
        closeButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
        specialtyPanel.setEnabled(enabled);
        payment.setEnabled(enabled);
    }

    public static void main(String[] args) {
        // Replace this with your actual MenuItemList initialization
        MenuItemList menuItemList = new MenuItemList();
		Caissa_UI cashierInterface = new Caissa_UI(connection,menuItemList,clientId);
		
		OrderDAO orderDAO = new OrderDAO();

        cashierInterface.setVisible(true);
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double price = (double) tableModel.getValueAt(i, 1);
            totalPrice += price;
        }
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }

    private void addSelectedItemToTable(String itemName, double itemPrice) {
        Object[] rowData = {itemName, itemPrice};
        tableModel.addRow(rowData);
        updateTotalPrice(); // Update total price after adding an item
    }
    
   /* private static final String FILE_NAME = "orders.txt";
   /* private static final String FILE_PATH = "Users/R_A_V_E_N/eclipse-workspace/onechance/src/order_file/orders.txt";*/

    
// Fixed file name for all orders
    private static final String FILE_PATH = "C:\\Users\\R_A_V_E_N\\eclipse-workspace\\onechance\\src\\order_file\\orders.txt";

    private void saveToFile(List<String> itemsSelected, double totalPrice, int ticketNumber, Date date, int clientId) {
        try {
            // Create a PrintWriter to write data to the file in append mode
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true));

            // Write the order details
            writer.println("Order Details:");
            writer.println("Client ID: " + clientId);
            writer.println("Ticket Number: " + ticketNumber);
            writer.println("Date: " + date);
            writer.println("Total Price: $" + String.format("%.2f", totalPrice));
            writer.println("Items:");
            for (String item : itemsSelected) {
                writer.println(item);
            }
            writer.println(); // Add a blank line between orders for readability

            // Close the PrintWriter
            writer.close();

            // Display a message indicating successful saving
            JOptionPane.showMessageDialog(null, "Order saved to file: " + FILE_PATH, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            // Handle any IO exceptions
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving order to file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> getSelectedItems() {
        List<String> itemsSelected = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String itemName = (String) tableModel.getValueAt(i, 0);
            itemsSelected.add(itemName);
        }
        return itemsSelected;
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double price = (double) tableModel.getValueAt(i, 1);
            totalPrice += price;
        }
        return totalPrice;
    }

    private int getCommandCount() {
        return commandCount;
    }
    
    private void clearTable() {
        tableModel.setRowCount(0); // Remove all rows from the table
        updateTotalPrice(); // Update total price after clearing the table
    }
    
    
    /*
    
    private void generateReceiptPDF(int ticketNumber, List<String> itemsSelected, double totalPrice, Date date, int clientId) {
        try {
            // Generate a file name based on the current date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String fileName = "receipt_" + ticketNumber + "_" + dateFormat.format(date) + ".pdf";

            // Create a PdfDocument
            PdfWriter writer = new PdfWriter(new FileOutputStream(fileName));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            // Create a Paragraph for header information
            Paragraph header = new Paragraph()
                    .add("Receipt\n\n")
                    .add("Client ID: " + clientId + "\n")
                    .add("Ticket Number: " + ticketNumber + "\n")
                    .add("Date: " + date + "\n\n");

            // Create a Table for items and prices
            Table table = new Table(new float[]{1, 4}); // Two columns: Item and Price
            table.addCell("Item");
            table.addCell("Price");
            for (String item : itemsSelected) {
                table.addCell(item);
                table.addCell("$"); // Add individual prices here if available
            }

            // Add header and table to the document
            document.add(header);
            document.add(table);

            // Add total price to the document
            Paragraph totalPriceParagraph = new Paragraph().add("Total Price: $" + String.format("%.2f", totalPrice));
            document.add(totalPriceParagraph);

            // Close the document
            document.close();

            // Display a message indicating successful saving
            JOptionPane.showMessageDialog(null, "Receipt saved to file: " + fileName, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            // Handle any IO exceptions
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving receipt to file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
*/

}
