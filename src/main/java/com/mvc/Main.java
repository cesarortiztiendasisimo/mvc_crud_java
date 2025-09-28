package com.mvc;

import com.mvc.view.UserView;
import com.mvc.controller.UserController;
import com.mvc.config.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Main application class
 * Entry point for the User Management System MVC application
 */
public class Main {
    
    /**
     * Main method - Application entry point
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Configure Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error setting Look and Feel: " + e.getMessage());
        }
        
        // Test database connection
        try {
            DatabaseConfig config = DatabaseConfig.getInstance();
            System.out.println("Conexión a base de datos establecida exitosamente");
            
            // Close test connection
            config.closeConnection();
            
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error al conectar con la base de datos.\n" +
                "Verifique que MySQL esté ejecutándose y que la base de datos 'user_management_db' exista.\n" +
                "Error: " + e.getMessage(), 
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
            
            // Show instructions for database setup
            showDatabaseSetupInstructions();
            return;
        }
        
        // Initialize application on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initializeApplication();
            }
        });
    }
    
    /**
     * Initialize the MVC application
     */
    private static void initializeApplication() {
        try {
            // Create View
            UserView view = new UserView();
            
            // Create Controller and connect with View
            @SuppressWarnings("unused")
            UserController controller = new UserController(view);
            
            System.out.println("Aplicación MVC iniciada correctamente");
            
        } catch (Exception e) {
            System.err.println("Error al inicializar la aplicación: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar la aplicación: " + e.getMessage(), 
                "Error de Inicialización", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Show database setup instructions
     */
    private static void showDatabaseSetupInstructions() {
        String instructions = 
            "Para configurar la base de datos, siga estos pasos:\n\n" +
            "1. Asegúrese de que MySQL esté instalado y ejecutándose\n" +
            "2. Ejecute el siguiente script SQL:\n\n" +
            "CREATE DATABASE IF NOT EXISTS user_management_db;\n" +
            "USE user_management_db;\n\n" +
            "CREATE TABLE IF NOT EXISTS users (\n" +
            "    id INT AUTO_INCREMENT PRIMARY KEY,\n" +
            "    name VARCHAR(100) NOT NULL,\n" +
            "    email VARCHAR(100) NOT NULL UNIQUE,\n" +
            "    phone VARCHAR(20) NOT NULL,\n" +
            "    address VARCHAR(200),\n" +
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
            "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n" +
            ");\n\n" +
            "3. Verifique las credenciales en DatabaseConfig.java\n" +
            "4. Reinicie la aplicación";
        
        JTextArea textArea = new JTextArea(instructions);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(null, scrollPane, 
            "Instrucciones de Configuración de Base de Datos", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
