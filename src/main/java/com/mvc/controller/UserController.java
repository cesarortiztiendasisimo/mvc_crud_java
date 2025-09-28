package com.mvc.controller;

import com.mvc.model.User;
import com.mvc.dao.UserDAO;
import com.mvc.dao.UserDAOMock;
import com.mvc.view.UserView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User Controller - MVC Pattern Implementation
 * Handles business logic and coordinates between Model and View
 * Manages CRUD operations and user interactions
 */
public class UserController {
    
    private UserDAO userDAO;
    private UserView userView;
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    // Phone validation pattern (Colombian format)
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?57\\s?[0-9]{3}\\s?[0-9]{3}\\s?[0-9]{4}$|^[0-9]{10}$");
    
    /**
     * Constructor - Initialize controller with view and DAO
     * @param view UserView instance
     */
    public UserController(UserView view) {
        this.userView = view;
        // Using Mock DAO for testing without database
        this.userDAO = new UserDAOMock();
        initializeEventHandlers();
        loadUsersOnStartup();
    }
    
    /**
     * Constructor - Initialize controller with view and specified DAO type
     * @param view UserView instance
     * @param useMockDAO true to use mock DAO, false to use real database DAO
     */
    public UserController(UserView view, boolean useMockDAO) {
        this.userView = view;
        if (useMockDAO) {
            this.userDAO = new UserDAOMock();
        } else {
            try {
                // Try to use real DAO implementation
                this.userDAO = new com.mvc.dao.UserDAOImpl();
            } catch (Exception e) {
                System.err.println("⚠️ Error al crear DAO real, usando Mock: " + e.getMessage());
                this.userDAO = new UserDAOMock();
            }
        }
        initializeEventHandlers();
        loadUsersOnStartup();
    }
    
    /**
     * Initialize event handlers for view components
     * Connects view events to controller methods
     */
    private void initializeEventHandlers() {
        // Create user button handler
        userView.getCreateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreateUser();
            }
        });
        
        // Update user button handler
        userView.getUpdateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdateUser();
            }
        });
        
        // Delete user button handler
        userView.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteUser();
            }
        });
        
        // Refresh button handler
        userView.getRefreshButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRefreshUsers();
            }
        });
        
        // Clear form button handler
        userView.getClearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleClearForm();
            }
        });
    }
    
    /**
     * Handle create user operation
     * Validates input and creates new user in database
     */
    private void handleCreateUser() {
        try {
            // Get data from view
            String name = userView.getNameField().getText().trim();
            String email = userView.getEmailField().getText().trim();
            String phone = userView.getPhoneField().getText().trim();
            String address = userView.getAddressField().getText().trim();
            
            // Validate input data
            if (!validateUserInput(name, email, phone, address)) {
                return;
            }
            
            // Create user object
            User user = new User(name, email, phone, address);
            
            // Save to database
            boolean success = userDAO.createUser(user);
            
            if (success) {
                // Show success message and refresh view
                userView.showSuccessMessage("Usuario creado exitosamente");
                userView.clearForm();
                loadAllUsers();
            } else {
                userView.showErrorMessage("Error al crear usuario");
            }
            
        } catch (Exception e) {
            userView.showErrorMessage("Error inesperado al crear usuario: " + e.getMessage());
        }
    }
    
    /**
     * Handle update user operation
     * Validates input and updates existing user in database
     */
    private void handleUpdateUser() {
        try {
            // Check if user is selected
            String idText = userView.getIdField().getText().trim();
            if (idText.isEmpty()) {
                userView.showErrorMessage("Seleccione un usuario de la tabla para actualizar");
                return;
            }
            
            // Get data from view
            int id = Integer.parseInt(idText);
            String name = userView.getNameField().getText().trim();
            String email = userView.getEmailField().getText().trim();
            String phone = userView.getPhoneField().getText().trim();
            String address = userView.getAddressField().getText().trim();
            
            // Validate input data
            if (!validateUserInput(name, email, phone, address)) {
                return;
            }
            
            // Create user object with updated data
            User user = new User(id, name, email, phone, address);
            
            // Update in database
            boolean success = userDAO.updateUser(user);
            
            if (success) {
                // Show success message and refresh view
                userView.showSuccessMessage("Usuario actualizado exitosamente");
                userView.clearForm();
                loadAllUsers();
            } else {
                userView.showErrorMessage("Error al actualizar usuario");
            }
            
        } catch (NumberFormatException e) {
            userView.showErrorMessage("ID debe ser un número válido");
        } catch (Exception e) {
            userView.showErrorMessage("Error inesperado al actualizar usuario: " + e.getMessage());
        }
    }
    
    /**
     * Handle delete user operation
     * Confirms deletion and removes user from database
     */
    private void handleDeleteUser() {
        try {
            // Check if user is selected
            String idText = userView.getIdField().getText().trim();
            if (idText.isEmpty()) {
                userView.showErrorMessage("Seleccione un usuario de la tabla para eliminar");
                return;
            }
            
            int id = Integer.parseInt(idText);
            String userName = userView.getNameField().getText().trim();
            
            // Confirm deletion
            boolean confirmed = userView.showConfirmDialog(
                "¿Está seguro de eliminar al usuario '" + userName + "'?\n" +
                "Esta acción no se puede deshacer."
            );
            
            if (confirmed) {
                // Delete from database
                boolean success = userDAO.deleteUser(id);
                
                if (success) {
                    // Show success message and refresh view
                    userView.showSuccessMessage("Usuario eliminado exitosamente");
                    userView.clearForm();
                    loadAllUsers();
                } else {
                    userView.showErrorMessage("Error al eliminar usuario");
                }
            }
            
        } catch (NumberFormatException e) {
            userView.showErrorMessage("ID debe ser un número válido");
        } catch (Exception e) {
            userView.showErrorMessage("Error inesperado al eliminar usuario: " + e.getMessage());
        }
    }
    
    /**
     * Handle refresh users operation
     * Reloads all users from database
     */
    private void handleRefreshUsers() {
        loadAllUsers();
    }
    
    /**
     * Handle clear form operation
     * Clears all form fields
     */
    private void handleClearForm() {
        userView.clearForm();
    }
    
    /**
     * Load all users from database and update view
     */
    private void loadAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            userView.updateUserTable(users);
        } catch (Exception e) {
            userView.showErrorMessage("Error inesperado al cargar usuarios: " + e.getMessage());
        }
    }
    
    /**
     * Load users on application startup
     */
    private void loadUsersOnStartup() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadAllUsers();
            }
        });
    }
    
    /**
     * Validate user input data
     * @param name User name
     * @param email User email
     * @param phone User phone
     * @param address User address
     * @return true if all validations pass, false otherwise
     */
    private boolean validateUserInput(String name, String email, String phone, String address) {
        // Check for empty fields
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            userView.showErrorMessage("Todos los campos son obligatorios");
            return false;
        }
        
        // Validate name length
        if (name.length() < 2 || name.length() > 100) {
            userView.showErrorMessage("El nombre debe tener entre 2 y 100 caracteres");
            return false;
        }
        
        // Validate name format (only letters and spaces)
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            userView.showErrorMessage("El nombre solo puede contener letras y espacios");
            return false;
        }
        
        // Validate email format
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            userView.showErrorMessage("Formato de email inválido");
            return false;
        }
        
        // Validate email length
        if (email.length() > 100) {
            userView.showErrorMessage("El email no puede exceder 100 caracteres");
            return false;
        }
        
        // Validate phone format
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            userView.showErrorMessage("Formato de teléfono inválido. Use: +57 XXX XXX XXXX o XXXXXXXXXX");
            return false;
        }
        
        // Validate address length
        if (address.length() > 200) {
            userView.showErrorMessage("La dirección no puede exceder 200 caracteres");
            return false;
        }
        
        return true;
    }
    
    /**
     * Get user by ID for read operations
     * @param id User ID
     * @return User object or null if not found
     */
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }
    
    /**
     * Get total number of users
     * @return Number of users in database
     */
    public int getTotalUsers() {
        return userDAO.getAllUsers().size();
    }
}
