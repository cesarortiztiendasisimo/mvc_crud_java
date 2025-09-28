package com.mvc;

import com.mvc.view.UserView;
import com.mvc.controller.UserController;

import javax.swing.*;

/**
 * Simple Main application class for testing MVC pattern
 * This version works without database connection using mock data
 */
public class MainTest {
    
    /**
     * Main method - Application entry point for testing
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
        
        System.out.println("Iniciando aplicación MVC de prueba...");
        
        // Initialize application on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initializeApplication();
            }
        });
    }
    
    /**
     * Initialize the MVC application with mock data
     */
    private static void initializeApplication() {
        try {
            // Create View
            UserView view = new UserView();
            
            // Create Controller and connect with View (using mock data)
            @SuppressWarnings("unused")
            UserController controller = new UserController(view);
            
            System.out.println("Aplicación MVC iniciada correctamente con datos de prueba");
            System.out.println("Funcionalidades disponibles:");
            System.out.println("- Crear usuarios");
            System.out.println("- Consultar usuarios (ya hay datos de ejemplo)");
            System.out.println("- Actualizar usuarios");
            System.out.println("- Eliminar usuarios");
            System.out.println("- Todas las validaciones están activas");
            
        } catch (Exception e) {
            System.err.println("Error al inicializar la aplicación: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al inicializar la aplicación: " + e.getMessage(), 
                "Error de Inicialización", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
