package com.mvc.api;

import com.mvc.api.swagger.SwaggerHandler;
import com.mvc.model.User;
import com.mvc.dao.UserDAO;
import com.mvc.dao.UserDAOMock;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Servidor completo con API REST y documentación Swagger integrados
 * Combina las funcionalidades de UserApiController y SwaggerHandler
 */
public class FullApiServer {
    private HttpServer server;
    private SwaggerHandler swaggerHandler;
    private UserDAO userDAO;
    private static final int PORT = 9090;
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public FullApiServer() throws IOException {
        // Configurar el servidor en el puerto 9090
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Inicializar componentes
        swaggerHandler = new SwaggerHandler();
        userDAO = new UserDAOMock(); // Usar mock para demo
        
        // Configurar todas las rutas
        setupRoutes();
        
        // Configurar el ejecutor por defecto
        server.setExecutor(null);
    }
    
    private void setupRoutes() {
        // Rutas de la API
        server.createContext("/api/users", new UsersHandler());
        server.createContext("/api/health", new HealthHandler());
        
        // Documentación Swagger
        server.createContext("/swagger", swaggerHandler);
        
        // Manejador por defecto
        server.createContext("/", new DefaultHandler());
    }
    
    public void start() {
        server.start();
        System.out.println("=== MVC User Management API + Swagger ===");
        System.out.println("Servidor iniciado exitosamente en puerto " + PORT);
        System.out.println("");
        System.out.println("URLs disponibles:");
        System.out.println("   Documentacion Swagger:  http://localhost:" + PORT + "/swagger");
        System.out.println("   API Specification:      http://localhost:" + PORT + "/swagger/api-docs");
        System.out.println("   Health Check:           http://localhost:" + PORT + "/api/health");
        System.out.println("");
        System.out.println("API Endpoints:");
        System.out.println("   GET    /api/users          - Obtener todos los usuarios");
        System.out.println("   POST   /api/users          - Crear un usuario");
        System.out.println("   GET    /api/users/{id}     - Obtener usuario por ID");
        System.out.println("   PUT    /api/users/{id}     - Actualizar usuario");
        System.out.println("   DELETE /api/users/{id}     - Eliminar usuario");
        System.out.println("");
        System.out.println("Presiona Ctrl+C para detener el servidor");
    }
    
    public void stop() {
        server.stop(0);
        System.out.println("Servidor detenido");
    }
    
    /**
     * Manejador para endpoints /api/users
     */
    private class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            try {
                if (path.equals("/api/users")) {
                    if ("GET".equals(method)) {
                        handleGetAllUsers(exchange);
                    } else if ("POST".equals(method)) {
                        handleCreateUser(exchange);
                    } else {
                        sendMethodNotAllowed(exchange);
                    }
                } else if (path.startsWith("/api/users/")) {
                    String idStr = path.substring("/api/users/".length());
                    try {
                        int id = Integer.parseInt(idStr);
                        if ("GET".equals(method)) {
                            handleGetUserById(exchange, id);
                        } else if ("PUT".equals(method)) {
                            handleUpdateUser(exchange, id);
                        } else if ("DELETE".equals(method)) {
                            handleDeleteUser(exchange, id);
                        } else {
                            sendMethodNotAllowed(exchange);
                        }
                    } catch (NumberFormatException e) {
                        sendBadRequest(exchange, "Invalid user ID: " + idStr);
                    }
                } else {
                    sendNotFound(exchange);
                }
            } catch (Exception e) {
                sendInternalError(exchange, e.getMessage());
                e.printStackTrace();
            }
        }
        
        private void handleGetAllUsers(HttpExchange exchange) throws IOException {
            List<User> users = userDAO.getAllUsers();
            String jsonResponse = usersToJson(users);
            sendJsonResponse(exchange, 200, jsonResponse);
        }
        
        private void handleCreateUser(HttpExchange exchange) throws IOException {
            String requestBody = readRequestBody(exchange);
            User user = userFromJson(requestBody);
            
            if (user == null) {
                sendBadRequest(exchange, "Invalid JSON format");
                return;
            }
            
            String validationError = validateUser(user);
            if (validationError != null) {
                sendBadRequest(exchange, validationError);
                return;
            }
            
            boolean created = userDAO.createUser(user);
            if (created) {
                // Since createUser returns boolean, we need to get the created user
                // For demonstration, we'll return the user with a generated ID
                String jsonResponse = userToJson(user);
                sendJsonResponse(exchange, 201, jsonResponse);
            } else {
                sendInternalError(exchange, "Failed to create user");
            }
        }
        
        private void handleGetUserById(HttpExchange exchange, int id) throws IOException {
            User user = userDAO.getUserById(id);
            if (user == null) {
                sendNotFound(exchange, "User not found with ID: " + id);
                return;
            }
            
            String jsonResponse = userToJson(user);
            sendJsonResponse(exchange, 200, jsonResponse);
        }
        
        private void handleUpdateUser(HttpExchange exchange, int id) throws IOException {
            String requestBody = readRequestBody(exchange);
            User user = userFromJson(requestBody);
            
            if (user == null) {
                sendBadRequest(exchange, "Invalid JSON format");
                return;
            }
            
            user.setId(id); // Set the ID from URL
            
            String validationError = validateUser(user);
            if (validationError != null) {
                sendBadRequest(exchange, validationError);
                return;
            }
            
            boolean updated = userDAO.updateUser(user);
            if (updated) {
                String jsonResponse = userToJson(user);
                sendJsonResponse(exchange, 200, jsonResponse);
            } else {
                sendNotFound(exchange, "User not found with ID: " + id);
            }
        }
        
        private void handleDeleteUser(HttpExchange exchange, int id) throws IOException {
            boolean deleted = userDAO.deleteUser(id);
            if (!deleted) {
                sendNotFound(exchange, "User not found with ID: " + id);
                return;
            }
            
            sendJsonResponse(exchange, 200, "{\"message\": \"User deleted successfully\"}");
        }
    }
    
    /**
     * Manejador para health check
     */
    private class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = "{\"status\": \"OK\", \"timestamp\": \"" + 
                    java.time.Instant.now().toString() + "\"}";
                sendJsonResponse(exchange, 200, response);
            } else {
                sendMethodNotAllowed(exchange);
            }
        }
    }
    
    /**
     * Manejador por defecto para rutas no encontradas
     */
    private class DefaultHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            String response = "{\"error\": \"Endpoint not found\", " +
                "\"swagger_documentation\": \"http://localhost:" + PORT + "/swagger\", " +
                "\"available_endpoints\": [\"/api/users\", \"/api/health\", \"/swagger\"]}";
            sendJsonResponse(exchange, 404, response);
        }
    }
    
    // Métodos auxiliares
    private void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.getResponseBody().close();
    }
    
    private void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        String response = "{\"error\": \"" + message.replace("\"", "\\\"") + "\"}";
        sendJsonResponse(exchange, 400, response);
    }
    
    private void sendNotFound(HttpExchange exchange) throws IOException {
        sendNotFound(exchange, "Endpoint not found");
    }
    
    private void sendNotFound(HttpExchange exchange, String message) throws IOException {
        String response = "{\"error\": \"" + message.replace("\"", "\\\"") + "\"}";
        sendJsonResponse(exchange, 404, response);
    }
    
    private void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        String response = "{\"error\": \"Method not allowed\"}";
        sendJsonResponse(exchange, 405, response);
    }
    
    private void sendInternalError(HttpExchange exchange, String message) throws IOException {
        String response = "{\"error\": \"Internal server error: " + message.replace("\"", "\\\"") + "\"}";
        sendJsonResponse(exchange, 500, response);
    }
    
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    
    // JSON parsing methods (simple implementation)
    private User userFromJson(String json) {
        try {
            // Simple JSON parsing for User
            json = json.trim();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                return null;
            }
            
            User user = new User();
            
            // Extract name
            String name = extractJsonValue(json, "name");
            if (name != null) user.setName(name);
            
            // Extract email
            String email = extractJsonValue(json, "email");
            if (email != null) user.setEmail(email);
            
            // Extract phone
            String phone = extractJsonValue(json, "phone");
            if (phone != null) user.setPhone(phone);
            
            // Extract address
            String address = extractJsonValue(json, "address");
            if (address != null) user.setAddress(address);
            
            return user;
        } catch (Exception e) {
            return null;
        }
    }
    
    private String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]+)\"";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }
    
    private String userToJson(User user) {
        return "{" +
            "\"id\":" + user.getId() + "," +
            "\"name\":\"" + escapeJson(user.getName()) + "\"," +
            "\"email\":\"" + escapeJson(user.getEmail()) + "\"," +
            "\"phone\":\"" + escapeJson(user.getPhone()) + "\"," +
            "\"address\":\"" + escapeJson(user.getAddress()) + "\"" +
            "}";
    }
    
    private String usersToJson(List<User> users) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(userToJson(users.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    
    private String validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return "Name is required";
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "Email is required";
        }
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            return "Invalid email format";
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            return "Phone is required";
        }
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            return "Address is required";
        }
        return null; // No errors
    }
    
    public static void main(String[] args) {
        try {
            FullApiServer server = new FullApiServer();
            
            // Agregar shutdown hook para limpiar recursos
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            
            server.start();
            
            // Mantener el servidor corriendo
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
