package com.mvc.api;

import com.mvc.api.swagger.SwaggerHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Servidor HTTP para la API REST del sistema de gestión de usuarios MVC.
 * Configura endpoints para operaciones CRUD y documentación Swagger.
 */
public class ApiServer {
    private HttpServer server;
    private SwaggerHandler swaggerHandler;
    
    public ApiServer() throws IOException {
        // Configurar el servidor en el puerto 3000 para evitar conflictos
        server = HttpServer.create(new InetSocketAddress(3000), 0);
        
        // Inicializar controladores
        swaggerHandler = new SwaggerHandler();
        
        // Configurar rutas de Swagger
        setupSwaggerRoutes();
        
        // Configurar CORS para todas las rutas
        setupCorsHandler();
        
        // Configurar el ejecutor por defecto
        server.setExecutor(null);
    }
    
    private void setupSwaggerRoutes() {
        // Documentación Swagger - el handler maneja internamente las sub-rutas
        server.createContext("/swagger", swaggerHandler);
    }
    
    private void setupCorsHandler() {
        // Manejador de CORS para todas las rutas no definidas
        server.createContext("/", new CorsHandler());
    }
    
    public void start() {
        server.start();
        System.out.println("✓ Swagger Documentation Server iniciado exitosamente!");
        System.out.println("");
        System.out.println("📖 Documentacion Swagger: http://localhost:3000/swagger");
        System.out.println("📋 API Specification JSON: http://localhost:3000/swagger/api-docs");
        System.out.println("");
        System.out.println("🔗 Para usar la API completa, ejecuta en otra terminal:");
        System.out.println("   java -cp target/classes com.mvc.api.UserApiController");
        System.out.println("   (La API estara en http://localhost:9090)");
        System.out.println("");
        System.out.println("⏹️  Presiona Ctrl+C para detener el servidor");
    }
    
    public void stop() {
        server.stop(0);
        System.out.println("Servidor detenido");
    }
    
    /**
     * Manejador de CORS para permitir requests desde navegadores web
     */
    private static class CorsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Agregar headers CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            
            // Manejar preflight requests (OPTIONS)
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            // Si no es un endpoint conocido, enviar 404 con enlace a Swagger
            String response = "{\"error\": \"Endpoint not found. Visit /swagger for API documentation.\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(404, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }
    
    public static void main(String[] args) {
        try {
            // Para evitar conflictos de puertos, vamos a usar el puerto 8080 para Swagger
            // y dejar que UserApiController use el 9090
            System.out.println("===== MVC User Management API con Swagger =====");
            System.out.println("Iniciando servidor solo con documentacion Swagger...");
            System.out.println("Para la API completa, ejecuta UserApiController por separado.");
            
            ApiServer server = new ApiServer();
            server.start();
            
            // Agregar shutdown hook para limpiar recursos
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            
            // Mantener el servidor corriendo
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
