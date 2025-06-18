package inventorymanagement.gui;

import inventorymanagement.dao.ProductDao;
import inventorymanagement.pojo.ProductPojo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryGUI extends JFrame {
    private JTextField idField, nameField, quantityField;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private JTable table;
    private DefaultTableModel model;
    private ProductDao productDAO;

    public InventoryGUI() {
        super("Inventory Management System");
        productDAO = new ProductDao();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        idField = new JTextField();
        nameField = new JTextField();
        quantityField = new JTextField();
        inputPanel.add(new JLabel("Product ID"));
        inputPanel.add(new JLabel("Name"));
        inputPanel.add(new JLabel("Quantity"));
        inputPanel.add(new JLabel());
        inputPanel.add(idField);
        inputPanel.add(nameField);
        inputPanel.add(quantityField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Quantity"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        refreshButton.addActionListener(e -> loadData());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                idField.setText(model.getValueAt(selectedRow, 0).toString());
                nameField.setText(model.getValueAt(selectedRow, 1).toString());
                quantityField.setText(model.getValueAt(selectedRow, 2).toString());
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        List<ProductPojo> list = productDAO.getAllProducts();
        for (ProductPojo p : list) {
            model.addRow(new Object[]{p.getProductId(), p.getProductName(), p.getQuantity()});
        }
    }

    private void addProduct() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String qtyText = quantityField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            ProductPojo product = new ProductPojo(id, name, qty);
            boolean result = ProductDao.addProduct(product);

            if (result) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.");
        }
    }

    private void updateProduct() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String qtyText = quantityField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            ProductPojo product = new ProductPojo(id, name, qty);
            boolean result = productDAO.updateProduct(product);

            if (result) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                loadData();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.");
        }
    }

    private void deleteProduct() {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter a product ID to delete.");
            return;
        }

        boolean result = productDAO.deleteProduct(id);
        if (result) {
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            loadData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete product.");
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        quantityField.setText("");
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}
