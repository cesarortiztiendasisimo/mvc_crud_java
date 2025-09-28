package com.supermercado.launcher;

import com.supermercado.api.EmpleadoRestController;
import java.net.ServerSocket;
import java.io.IOException;

/**
 * Launcher para el servidor web de empleados
 * Inicia la API REST y servidor de archivos estáticos
 */
public class EmpleadoWebLauncher {
    private static final int PUERTO_PREFERIDO = 8084; // Actualizado para coincidir con sistema web
    private static final int PUERTO_MAX = 8090;
    
    public static void main(String[] args) {
        try {
            System.out.println("=== SISTEMA WEB DE GESTIÓN DE EMPLEADOS ===");
            System.out.println("Arquitectura: REST API + Frontend Web");
            
            // Encontrar un puerto disponible
            int puertoDisponible = encontrarPuertoDisponible();
            
            if (puertoDisponible == -1) {
                System.err.println("❌ No se encontró un puerto disponible entre " + PUERTO_PREFERIDO + " y " + PUERTO_MAX);
                return;
            }
            
            System.out.println("🚀 Iniciando servidor en puerto: " + puertoDisponible);
            
            // Crear y iniciar el controlador con el puerto disponible
            EmpleadoRestController controller = new EmpleadoRestController(puertoDisponible);
            controller.startServer();
            
            // Obtener el puerto real donde se inició el servidor
            int puertoReal = controller.getPort();
            
            System.out.println("");
            System.out.println("✅ Sistema web iniciado exitosamente");
            System.out.println("🌐 Abra su navegador en: http://localhost:" + puertoReal);
            System.out.println("📋 Para ver la interfaz web de empleados");
            System.out.println("");
            System.out.println("Presione Ctrl+C para detener el servidor");
            
            // Agregar shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n🛑 Cerrando servidor...");
                controller.stopServer();
            }));
            
            // Mantener el servidor corriendo
            try {
                // Usar un bucle infinito en lugar de join para mantener el servidor activo
                while (controller.isRunning()) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("🛑 Servidor interrumpido");
                controller.stopServer();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar el servidor web: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Encuentra un puerto disponible empezando por el preferido
     */
    private static int encontrarPuertoDisponible() {
        for (int puerto = PUERTO_PREFERIDO; puerto <= PUERTO_MAX; puerto++) {
            if (isPuertoDisponible(puerto)) {
                return puerto;
            }
        }
        return -1; // No hay puertos disponibles
    }
    
    /**
     * Verifica si un puerto está disponible
     */
    private static boolean isPuertoDisponible(int puerto) {
        try (ServerSocket socket = new ServerSocket(puerto)) {
            socket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}