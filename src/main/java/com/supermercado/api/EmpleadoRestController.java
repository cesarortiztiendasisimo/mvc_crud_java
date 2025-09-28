package com.supermercado.api;

import com.supermercado.model.Empleado;
import com.supermercado.dao.EmpleadoDAO;
import com.supermercado.dao.EmpleadoDAOImpl;
import com.supermercado.util.JsonUtil;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

/**
 * REST API Controller para Empleados
 * Proporciona endpoints HTTP para operaciones CRUD
 */
public class EmpleadoRestController {
    
    private EmpleadoDAO empleadoDAO;
    private int port;
    private HttpServer server;
    
    public EmpleadoRestController() {
        this(8084); // Puerto por defecto - actualizado para coincidir con web login
    }
    
    public EmpleadoRestController(int port) {
        this.empleadoDAO = new EmpleadoDAOImpl();
        this.port = port;
    }
    
    /**
     * Iniciar el servidor HTTP
     */
    public void startServer() {
        try {
            // Matar cualquier proceso que esté usando el puerto
            killProcessUsingPort(port);
            
            // Esperar un momento para que se libere el puerto
            Thread.sleep(2000);
            
            server = HttpServer.create(new InetSocketAddress(port), 0);
            
            // Configurar rutas
            server.createContext("/api/empleados", new EmpleadoHandler());
            server.createContext("/api/auth", new AuthHandler());
            server.createContext("/api/desktop", new DesktopLauncherHandler());
            server.createContext("/", new StaticFileHandler());
            
            server.setExecutor(null);
            server.start();
            
            System.out.println("** Servidor de Empleados iniciado en http://localhost:" + port);
            System.out.println("API disponible en: http://localhost:" + port + "/api/empleados");
            System.out.println("Login disponible en: http://localhost:" + port + "/login.html");
            System.out.println("WEB: Interfaz web en: http://localhost:" + port + "/empleados.html");
            System.out.println("WEB: O simplemente: http://localhost:" + port);
            
        } catch (Exception e) {
            System.err.println("ERROR: Error al iniciar servidor en puerto " + port + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("No se pudo iniciar el servidor en el puerto fijo " + port, e);
        }
    }
    
    /**
     * Mata cualquier proceso que esté usando el puerto especificado
     */
    private void killProcessUsingPort(int port) {
        try {
            // Comando para encontrar el proceso usando el puerto
            ProcessBuilder pb = new ProcessBuilder("netstat", "-ano");
            Process process = pb.start();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(":" + port + " ") && line.contains("LISTENING")) {
                        // Extraer el PID de la línea
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length > 4) {
                            String pid = parts[parts.length - 1];
                            try {
                                // Matar el proceso
                                ProcessBuilder killProcess = new ProcessBuilder("taskkill", "/PID", pid, "/F");
                                killProcess.start().waitFor();
                                System.out.println("Proceso " + pid + " terminado (liberando puerto " + port + ")");
                            } catch (Exception ignored) {
                                // Ignorar errores al matar proceso
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignorar errores en la limpieza de puertos
            System.out.println("AVISO: No se pudo verificar/limpiar el puerto " + port);
        }
    }
    
    /**
     * Obtener el puerto en el que está corriendo el servidor
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Verificar si el servidor está corriendo
     */
    public boolean isRunning() {
        return server != null;
    }
    
    /**
     * Detener el servidor
     */
    public void stopServer() {
        if (server != null) {
            server.stop(0);
            server = null;
            System.out.println("🛑 Servidor detenido");
        }
    }
    
    /**
     * Manejador para las operaciones de empleados
     */
    private class EmpleadoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Configurar CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            try {
                switch (method) {
                    case "OPTIONS":
                        handleCors(exchange);
                        break;
                    case "GET":
                        if (path.equals("/api/empleados")) {
                            handleGetAllEmpleados(exchange);
                        } else if (path.matches("/api/empleados/\\d+")) {
                            handleGetEmpleado(exchange);
                        } else if (path.equals("/api/empleados/cargos")) {
                            handleGetCargos(exchange);
                        }
                        break;
                    case "POST":
                        handleCreateEmpleado(exchange);
                        break;
                    case "PUT":
                        handleUpdateEmpleado(exchange);
                        break;
                    case "DELETE":
                        handleDeleteEmpleado(exchange);
                        break;
                    default:
                        sendErrorResponse(exchange, 405, "Método no permitido");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Error interno del servidor: " + e.getMessage());
            }
        }
        
        private void handleCors(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, -1);
        }
        
        private void handleGetAllEmpleados(HttpExchange exchange) throws IOException {
            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            String json = JsonUtil.toJson(empleados);
            sendJsonResponse(exchange, 200, json);
        }
        
        private void handleGetEmpleado(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            
            Empleado empleado = empleadoDAO.obtenerPorId(id);
            if (empleado != null) {
                String json = JsonUtil.toJson(empleado);
                sendJsonResponse(exchange, 200, json);
            } else {
                sendErrorResponse(exchange, 404, "Empleado no encontrado");
            }
        }
        
        private void handleGetCargos(HttpExchange exchange) throws IOException {
            List<String> cargos = empleadoDAO.obtenerCargosUnicos();
            String json = JsonUtil.toJsonStringArray(cargos);
            sendJsonResponse(exchange, 200, json);
        }
        
        private void handleCreateEmpleado(HttpExchange exchange) throws IOException {
            String body = getRequestBody(exchange);
            Empleado empleado = JsonUtil.fromJson(body);
            
            boolean success = empleadoDAO.crear(empleado);
            if (success) {
                sendJsonResponse(exchange, 201, JsonUtil.createMessage("Empleado creado exitosamente"));
            } else {
                sendErrorResponse(exchange, 400, "Error al crear empleado");
            }
        }
        
        private void handleUpdateEmpleado(HttpExchange exchange) throws IOException {
            String body = getRequestBody(exchange);
            Empleado empleado = JsonUtil.fromJson(body);
            
            boolean success = empleadoDAO.actualizar(empleado);
            if (success) {
                sendJsonResponse(exchange, 200, JsonUtil.createMessage("Empleado actualizado exitosamente"));
            } else {
                sendErrorResponse(exchange, 400, "Error al actualizar empleado");
            }
        }
        
        private void handleDeleteEmpleado(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
            
            boolean success = empleadoDAO.eliminar(id);
            if (success) {
                sendJsonResponse(exchange, 200, JsonUtil.createMessage("Empleado eliminado exitosamente"));
            } else {
                sendErrorResponse(exchange, 400, "Error al eliminar empleado");
            }
        }
        
        private String getRequestBody(HttpExchange exchange) throws IOException {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }
    
    /**
     * Manejador para archivos estáticos
     */
    private class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            System.out.println("FILE: Solicitando archivo: " + path);
            
            if (path.equals("/") || path.equals("/empleados")) {
                path = "/empleados.html";
            } else if (path.equals("/login")) {
                path = "/login.html";
            }
            
            // Intentar servir desde el classpath primero
            String resourcePath = "/web" + path;
            InputStream resource = getClass().getResourceAsStream(resourcePath);
            
            if (resource == null) {
                // Intentar desde el filesystem si no está en classpath
                String filePath = "target/classes/web" + path;
                try {
                    resource = new java.io.FileInputStream(filePath);
                    System.out.println("📂 Cargando desde filesystem: " + filePath);
                } catch (Exception e) {
                    System.out.println("404: No encontrado: " + path);
                }
            } else {
                System.out.println("📦 Cargando desde classpath: " + resourcePath);
            }
            
            if (resource != null) {
                String contentType = getContentType(path);
                exchange.getResponseHeaders().add("Content-Type", contentType);
                
                byte[] bytes = resource.readAllBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
                resource.close();
                
                System.out.println("✅ Servido: " + path + " (" + bytes.length + " bytes)");
            } else {
                System.out.println("404: Archivo no encontrado: " + path);
                String errorHtml = "<html><body><h1>Error 404</h1><p>Archivo no encontrado: " + path + "</p></body></html>";
                byte[] errorBytes = errorHtml.getBytes();
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(404, errorBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(errorBytes);
                }
            }
        }
        
        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html; charset=utf-8";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".json")) return "application/json";
            return "text/plain";
        }
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
    
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        String json = JsonUtil.createError(message);
        sendJsonResponse(exchange, statusCode, json);
    }
    
    /**
     * Método main para ejecutar el servidor
     */
    public static void main(String[] args) {
        EmpleadoRestController controller = new EmpleadoRestController();
        controller.startServer();
        
        // Mantener el servidor corriendo
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("🛑 Deteniendo servidor...");
            controller.stopServer();
        }));
        
        System.out.println("Presiona Ctrl+C para detener el servidor");
        
        // Mantener el hilo principal vivo
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}