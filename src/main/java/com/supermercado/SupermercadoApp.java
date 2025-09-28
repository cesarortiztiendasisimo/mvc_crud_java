package com.supermercado;

import com.supermercado.controller.EmpleadoController;
import com.supermercado.controller.LoginController;
import com.supermercado.view.EmpleadoView;
import com.supermercado.view.LoginView;
import com.mvc.service.AuthenticationService;

import javax.swing.*;

/**
 * Clase Principal del Sistema de Empleados de Supermercado
 * Arquitectura 4+1 - Vista Física
 * Punto de entrada de la aplicación
 */
public class SupermercadoApp {
    
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema
        configurarLookAndFeel();
        
        // Inicializar aplicación en el EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                iniciarAplicacion();
            }
        });
    }
    
    /**
     * Configurar apariencia de la aplicación
     */
    private static void configurarLookAndFeel() {
        try {
            // Usar el Look and Feel del sistema operativo
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
            // Continuar con el Look and Feel por defecto
        }
    }
    
    /**
     * Inicializar la aplicación MVC
     * Arquitectura 4+1 - Escenarios (Use Cases)
     */
    private static void iniciarAplicacion() {
        try {
            System.out.println("=== SISTEMA DE GESTION DE EMPLEADOS ===");
            System.out.println("Arquitectura: MVC + Modelo 4+1 de Kruchten");
            System.out.println("Inicializando componentes...");
            
            // Mostrar login primero
            LoginView loginView = new LoginView(null);
            new LoginController(loginView);
            loginView.setVisible(true);
            
            // Esperar a que se complete el login
            new Thread(() -> {
                while (loginView.isVisible()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                
                // Verificar si el login fue exitoso
                if (loginView.isLoginExitoso()) {
                    SwingUtilities.invokeLater(() -> {
                        if (loginView.isEmpleadosSelected()) {
                            iniciarSistemaEmpleados();
                        } else if (loginView.isUsuariosSelected()) {
                            iniciarSistemaUsuarios();
                        } else if (loginView.isEmpleadosWebSelected()) {
                            iniciarSistemaEmpleadosWeb();
                        }
                    });
                } else {
                    System.out.println("Login cancelado. Cerrando aplicacion.");
                    System.exit(0);
                }
            }).start();
            
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicacion: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(
                null,
                "Error fatal al iniciar la aplicacion:\n" + e.getMessage(),
                "Error del Sistema",
                JOptionPane.ERROR_MESSAGE
            );
            
            System.exit(1);
        }
    }
    
    /**
     * Inicializar el sistema de empleados después del login exitoso
     */
    private static void iniciarSistemaEmpleados() {
        AuthenticationService authService = AuthenticationService.getInstance();
        
        // Crear la vista
        EmpleadoView vista = new EmpleadoView();
        
        // Crear el controlador (que conecta vista y modelo)
        new EmpleadoController(vista);
        
        // Mostrar la aplicación
        vista.setVisible(true);
        
        System.out.println("Aplicacion iniciada exitosamente");
        System.out.println("Usuario autenticado: " + authService.getInfoUsuarioAutenticado());
        System.out.println("Empleados precargados: 10");
        System.out.println("Funcion principal: Consultar empleados por cargo");
        
        // Mostrar mensaje de bienvenida
        JOptionPane.showMessageDialog(
            vista,
            "Bienvenido al Sistema de Gestion de Empleados!\n\n" +
            "Usuario: " + authService.getInfoUsuarioAutenticado() + "\n\n" +
            "Funciones principales:\n" +
            "• Consultar empleados por cargo\n" +
            "• CRUD completo de empleados\n" +
            "• 10 empleados precargados para pruebas\n\n" +
            "Desarrollado con patron MVC y arquitectura 4+1",
            "Sistema de Empleados - Supermercado",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Inicializar el sistema de usuarios después del login exitoso
     */
    private static void iniciarSistemaUsuarios() {
        AuthenticationService authService = AuthenticationService.getInstance();
        
        try {
            // Usar ProcessBuilder para ejecutar el sistema de usuarios con fallback
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", "target/classes", "com.mvc.MainWithFallback");
            pb.directory(new java.io.File("."));
            pb.inheritIO(); // Para ver la salida en la consola
            
            @SuppressWarnings("unused") // Process runs independently in background
            Process process = pb.start();
            
            System.out.println("Sistema de Usuarios iniciado exitosamente");
            System.out.println("Usuario autenticado: " + authService.getInfoUsuarioAutenticado());
            
            // Mostrar mensaje informativo
            JOptionPane.showMessageDialog(
                null,
                "Sistema de Usuarios iniciado en ventana separada.\n\n" +
                "Usuario autenticado: " + authService.getInfoUsuarioAutenticado() + "\n\n" +
                "El sistema de usuarios funciona con datos simulados (Mock)\n" +
                "y se ejecuta de forma independiente.\n\n" +
                "Puede usar ambos sistemas simultáneamente.",
                "Sistema de Usuarios - Modo Demo",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // No cerramos la aplicación actual, permitimos que coexistan ambos sistemas
            
        } catch (Exception e) {
            System.err.println("Error al iniciar sistema de usuarios: " + e.getMessage());
            JOptionPane.showMessageDialog(
                null,
                "Error al iniciar el Sistema de Usuarios:\n" + e.getMessage() + 
                "\n\nPosibles soluciones:\n" +
                "• Verifique que todos los archivos estén compilados\n" +
                "• El sistema usa datos mock (no requiere MySQL)\n" +
                "• Ejecute compile-and-run.ps1 para compilar todo",
                "Error al iniciar Sistema de Usuarios",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Inicializar el sistema web de empleados después del login exitoso
     */
    private static void iniciarSistemaEmpleadosWeb() {
        AuthenticationService authService = AuthenticationService.getInstance();
        
        try {
            // Usar ProcessBuilder para ejecutar el servidor web de empleados
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", "target/classes", "com.supermercado.launcher.EmpleadoWebLauncher");
            pb.directory(new java.io.File("."));
            pb.inheritIO(); // Para ver la salida en la consola
            
            Process process = pb.start();
            
            System.out.println("Sistema Web de Empleados iniciado exitosamente (PID: " + process.pid() + ")");
            System.out.println("Usuario autenticado: " + authService.getInfoUsuarioAutenticado());
            System.out.println("Servidor web corriendo en http://localhost:8084");
            
            // Mostrar mensaje informativo
            JOptionPane.showMessageDialog(
                null,
                "Sistema Web de Empleados iniciado exitosamente.\n\n" +
                "Usuario autenticado: " + authService.getInfoUsuarioAutenticado() + "\n\n" +
                "WEB: Abra su navegador en: http://localhost:8084\n" +
                "INTERFAZ: Para acceder a la interfaz web de empleados\n\n" +
                "El servidor web funciona independientemente.\n" +
                "Puede usar ambos sistemas simultaneamente.",
                "Sistema Web de Empleados - Iniciado",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Intentar abrir el navegador automáticamente
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:8084"));
                System.out.println("Navegador abierto automáticamente");
            } catch (Exception browserException) {
                System.out.println("No se pudo abrir el navegador automáticamente");
            }
            
            // No cerramos la aplicación actual, permitimos que coexistan ambos sistemas
            
        } catch (Exception e) {
            System.err.println("Error al iniciar sistema web de empleados: " + e.getMessage());
            JOptionPane.showMessageDialog(
                null,
                "Error al iniciar el Sistema Web de Empleados:\n" + e.getMessage() + 
                "\n\nPosibles soluciones:\n" +
                "• Verifique que todos los archivos estén compilados\n" +
                "• Verifique que el puerto 8084 esté disponible\n" +
                "• Ejecute compile-and-run.ps1 para compilar todo",
                "Error al iniciar Sistema Web",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}