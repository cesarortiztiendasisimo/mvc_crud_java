package com.mvc.launcher;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Lanzador que ejecuta simultáneamente la API y la aplicación de escritorio
 * 
 * @author Sistema MVC User Management
 * @version 1.0
 */
public class ApplicationLauncher {
    
    private static final String API_CLASS = "com.mvc.api.FullApiServer";
    private static final String DESKTOP_CLASS = "com.mvc.MainWithFallback";
    private static final String CLASSPATH = "target/classes";
    
    public static void main(String[] args) {
        System.out.println("=== Lanzador MVC User Management ===");
        System.out.println("Iniciando API y aplicación de escritorio...");
        
        try {
            // Lanzar API en un proceso separado
            Process apiProcess = launchApiServer();
            System.out.println("✅ API Server iniciado en proceso separado");
            
            // Esperar un momento para que la API se inicie
            Thread.sleep(3000);
            
            // Lanzar aplicación de escritorio en un hilo separado
            CompletableFuture<Void> desktopApp = CompletableFuture.runAsync(() -> {
                try {
                    Process desktopProcess = launchDesktopApp();
                    System.out.println("✅ Aplicación de escritorio iniciada");
                    
                    // Esperar a que termine la aplicación de escritorio
                    int exitCode = desktopProcess.waitFor();
                    System.out.println("🔴 Aplicación de escritorio cerrada (código: " + exitCode + ")");
                    
                    // Cuando se cierre la app de escritorio, también cerrar la API
                    if (apiProcess.isAlive()) {
                        apiProcess.destroyForcibly();
                        System.out.println("🔴 API Server detenido");
                    }
                    
                } catch (Exception e) {
                    System.err.println("❌ Error al ejecutar aplicación de escritorio: " + e.getMessage());
                }
            });
            
            // Mostrar información de la API
            showApiInfo();
            
            // Manejar señales de terminación
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n🔄 Cerrando aplicaciones...");
                if (apiProcess.isAlive()) {
                    apiProcess.destroyForcibly();
                    System.out.println("🔴 API Server detenido");
                }
            }));
            
            // Esperar a que termine la aplicación de escritorio
            desktopApp.join();
            
        } catch (Exception e) {
            System.err.println("❌ Error al lanzar aplicaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lanza el servidor API en un proceso separado
     */
    private static Process launchApiServer() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
            "java", "-cp", CLASSPATH, API_CLASS
        );
        pb.redirectErrorStream(true);
        return pb.start();
    }
    
    /**
     * Lanza la aplicación de escritorio en un proceso separado
     */
    private static Process launchDesktopApp() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
            "java", "-cp", CLASSPATH, DESKTOP_CLASS
        );
        pb.redirectErrorStream(true);
        return pb.start();
    }
    
    /**
     * Muestra información sobre la API disponible
     */
    private static void showApiInfo() {
        System.out.println("\n=== 🌐 API Disponible ===");
        System.out.println("📚 Swagger UI:    http://localhost:9090/swagger");
        System.out.println("📄 API Docs:      http://localhost:9090/swagger/api-docs");
        System.out.println("💚 Health Check:  http://localhost:9090/api/health");
        System.out.println("👥 Users API:     http://localhost:9090/api/users");
        System.out.println("\n💡 Tip: Abre Swagger UI en tu navegador para probar la API");
        System.out.println("⚠️  Al cerrar la aplicación de escritorio se detendrá también la API\n");
    }
}
