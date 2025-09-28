package com.mvc.view;

import com.mvc.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * User Management View - GUI Interface
 * Handles user interface components for CRUD operations
 * Follows MVC pattern - only handles presentation logic
 */
public class UserView extends JFrame {
    
    // Form components
    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    
    // Action buttons
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton clearButton;
    
    // Data display components
    private JTable userTable;
    private DefaultTableModel tableModel;
    
    /**
     * Constructor - Initialize the user interface
     */
    public UserView() {
        initializeComponents();
        setupLayout();
        configureTable();
        setVisible(true);
    }
    
    /**
     * Initialize all GUI components
     */
    private void initializeComponents() {
        // Window configuration
        setTitle("Sistema de Gestión de Usuarios - MVC Pattern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Initialize text fields
        idField = new JTextField(10);
        idField.setEditable(false);
        idField.setBackground(Color.LIGHT_GRAY);
        
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(15);
        addressField = new JTextField(25);
        
        // Initialize buttons with icons and tooltips
        createButton = new JButton("Crear Usuario");
        createButton.setToolTipText("Crear un nuevo usuario");
        createButton.setBackground(new Color(46, 125, 50));
        createButton.setForeground(Color.WHITE);
        
        updateButton = new JButton("Actualizar");
        updateButton.setToolTipText("Actualizar usuario seleccionado");
        updateButton.setBackground(new Color(25, 118, 210));
        updateButton.setForeground(Color.WHITE);
        
        deleteButton = new JButton("Eliminar");
        deleteButton.setToolTipText("Eliminar usuario seleccionado");
        deleteButton.setBackground(new Color(211, 47, 47));
        deleteButton.setForeground(Color.WHITE);
        
        refreshButton = new JButton("Refrescar");
        refreshButton.setToolTipText("Actualizar lista de usuarios");
        refreshButton.setBackground(new Color(158, 158, 158));
        refreshButton.setForeground(Color.WHITE);
        
        clearButton = new JButton("Limpiar");
        clearButton.setToolTipText("Limpiar formulario");
        clearButton.setBackground(new Color(255, 152, 0));
        clearButton.setForeground(Color.WHITE);
        
        // Initialize table
        String[] columnNames = {"ID", "Nombre", "Email", "Teléfono", "Dirección"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(25);
        userTable.getTableHeader().setReorderingAllowed(false);
        
        // Add selection listener to populate form when row is selected
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });
    }
    
    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Create main panels
        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel tablePanel = createTablePanel();
        
        // Add panels to main window
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the form panel for user data input
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Datos del Usuario", 
            0, 0, 
            new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        // Email field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        // Phone field
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        // Address field
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);
        
        return panel;
    }
    
    /**
     * Create the button panel for actions
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Acciones", 
            0, 0, 
            new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);
        
        // Add buttons to panel
        panel.add(createButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    /**
     * Create the table panel for displaying users
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Lista de Usuarios", 
            0, 0, 
            new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(850, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Configure table appearance and behavior
     */
    private void configureTable() {
        // Set column widths
        userTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        userTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        userTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Email
        userTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Phone
        userTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Address
        
        // Set table appearance
        userTable.setGridColor(Color.LIGHT_GRAY);
        userTable.setShowGrid(true);
        userTable.getTableHeader().setBackground(new Color(63, 81, 181));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    /**
     * Populate form fields from selected table row
     */
    private void populateFormFromSelectedRow() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            phoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            addressField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }
    
    /**
     * Update the user table with new data
     * @param users List of users to display
     */
    public void updateUserTable(List<User> users) {
        tableModel.setRowCount(0); // Clear existing data
        
        for (User user : users) {
            Object[] rowData = {
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress()
            };
            tableModel.addRow(rowData);
        }
    }
    
    /**
     * Clear all form fields
     */
    public void clearForm() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        userTable.clearSelection();
    }
    
    /**
     * Show success message to user
     * @param message Success message to display
     */
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show error message to user
     * @param message Error message to display
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show confirmation dialog
     * @param message Confirmation message
     * @return true if user confirms, false otherwise
     */
    public boolean showConfirmDialog(String message) {
        int result = JOptionPane.showConfirmDialog(
            this, 
            message, 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    // Getter methods for controller access
    public JTextField getIdField() { return idField; }
    public JTextField getNameField() { return nameField; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getPhoneField() { return phoneField; }
    public JTextField getAddressField() { return addressField; }
    
    public JButton getCreateButton() { return createButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getClearButton() { return clearButton; }
    
    public JTable getUserTable() { return userTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
}
