package com.mvc;

import com.mvc.view.UserView;
import com.mvc.controller.UserController;
import com.mvc.config.DatabaseConfig;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Main application class with fallback to Mock DAO
 * Entry point for the User Management System MVC application
 */
public class MainWithFallback {
    
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
        boolean useMockDAO = false;
        try {
            DatabaseConfig config = DatabaseConfig.getInstance();
            System.out.println("OK Conexion a base de datos establecida exitosamente");
            config.closeConnection();
            
        } catch (SQLException e) {
            System.err.println("ADVERTENCIA: No se pudo conectar a MySQL: " + e.getMessage());
            System.out.println("Cambiando a modo Mock (datos en memoria)...");
            useMockDAO = true;
            
            // Mostrar notificación al usuario
            JOptionPane.showMessageDialog(null, 
                "No se pudo conectar a MySQL.\n" +
                "La aplicación funcionará con datos simulados en memoria.\n\n" +
                "Los datos no se guardarán al cerrar la aplicación.", 
                "Modo Demo - Sin Base de Datos", 
                JOptionPane.WARNING_MESSAGE);
        }
        
        // Initialize application on Event Dispatch Thread
        final boolean finalUseMockDAO = useMockDAO;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initializeApplication(finalUseMockDAO);
            }
        });
    }
    
    /**
     * Initialize the MVC application
     * @param useMockDAO true to use mock DAO, false to use real database DAO
     */
    private static void initializeApplication(boolean useMockDAO) {
        try {
            // Create View
            UserView view = new UserView();
            
            // Create Controller with appropriate DAO
            @SuppressWarnings("unused") // Controller initializes event handlers automatically
            UserController controller;
            if (useMockDAO) {
                controller = new UserController(view, true); // Use mock DAO
                view.setTitle("Sistema de Gestión de Usuarios - MODO DEMO (Sin BD)");
            } else {
                controller = new UserController(view, false); // Use real DAO
                view.setTitle("Sistema de Gestión de Usuarios - MYSQL");
            }
            
            System.out.println("OK Aplicacion MVC iniciada correctamente" + 
                             (useMockDAO ? " (Modo Demo)" : " (MySQL)"));
            
        } catch (Exception e) {
            System.err.println("ERROR: Error al inicializar la aplicacion: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar la aplicación: " + e.getMessage(), 
                "Error de Inicialización", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
