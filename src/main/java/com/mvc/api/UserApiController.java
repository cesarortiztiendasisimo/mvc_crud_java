package com.mvc.api;

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
 * REST API Controller for User Management
 * Implements RESTful endpoints using Java's built-in HTTP server
 * Follows MVC pattern - this acts as the Controller for API requests
 */
public class UserApiController {
    
    private UserDAO userDAO;
    private HttpServer server;
    private static final int PORT = 9090;
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public UserApiController() {
        // You can switch between mock and real database
        this.userDAO = new UserDAOMock(); // For testing without DB
        // this.userDAO = new UserDAOImpl(); // For real database
    }
    
    /**
     * Start the HTTP server with REST endpoints
     */
    public void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Define REST endpoints
        server.createContext("/api/users", new UsersHandler());
        server.createContext("/api/users/", new UserByIdHandler());
        server.createContext("/api/health", new HealthHandler());
        
        // CORS handler for web browser compatibility
        server.createContext("/", new CorsHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("API Server started at http://localhost:" + PORT);
        System.out.println("Available endpoints:");
        System.out.println("  GET    /api/users          - Get all users");
        System.out.println("  POST   /api/users          - Create new user");
        System.out.println("  GET    /api/users/{id}     - Get user by ID");
        System.out.println("  PUT    /api/users/{id}     - Update user");
        System.out.println("  DELETE /api/users/{id}     - Delete user");
        System.out.println("  GET    /api/health         - Health check");
    }
    
    /**
     * Stop the HTTP server
     */
    public void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("API Server stopped");
        }
    }
    
    /**
     * Handler for /api/users endpoint
     * GET: Returns all users
     * POST: Creates a new user
     */
    private class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            String method = exchange.getRequestMethod();
            
            switch (method) {
                case "GET":
                    handleGetAllUsers(exchange);
                    break;
                case "POST":
                    handleCreateUser(exchange);
                    break;
                case "OPTIONS":
                    exchange.sendResponseHeaders(200, -1);
                    break;
                default:
                    sendErrorResponse(exchange, 405, "Method not allowed");
            }
        }
        
        private void handleGetAllUsers(HttpExchange exchange) throws IOException {
            try {
                List<User> users = userDAO.getAllUsers();
                String jsonResponse = convertUsersToJson(users);
                
                sendJsonResponse(exchange, 200, jsonResponse);
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
            }
        }
        
        private void handleCreateUser(HttpExchange exchange) throws IOException {
            try {
                String requestBody = readRequestBody(exchange);
                User user = parseUserFromJson(requestBody);
                
                // Validate user data
                String validationError = validateUser(user);
                if (validationError != null) {
                    sendErrorResponse(exchange, 400, validationError);
                    return;
                }
                
                boolean success = userDAO.createUser(user);
                if (success) {
                    String jsonResponse = convertUserToJson(user);
                    sendJsonResponse(exchange, 201, jsonResponse);
                } else {
                    sendErrorResponse(exchange, 500, "Failed to create user");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 400, "Invalid request: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handler for /api/users/{id} endpoint
     * GET: Returns user by ID
     * PUT: Updates user by ID
     * DELETE: Deletes user by ID
     */
    private class UserByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            // Extract ID from path
            String[] pathParts = path.split("/");
            if (pathParts.length < 4) {
                sendErrorResponse(exchange, 400, "User ID required");
                return;
            }
            
            try {
                int userId = Integer.parseInt(pathParts[3]);
                
                switch (method) {
                    case "GET":
                        handleGetUserById(exchange, userId);
                        break;
                    case "PUT":
                        handleUpdateUser(exchange, userId);
                        break;
                    case "DELETE":
                        handleDeleteUser(exchange, userId);
                        break;
                    case "OPTIONS":
                        exchange.sendResponseHeaders(200, -1);
                        break;
                    default:
                        sendErrorResponse(exchange, 405, "Method not allowed");
                }
            } catch (NumberFormatException e) {
                sendErrorResponse(exchange, 400, "Invalid user ID");
            }
        }
        
        private void handleGetUserById(HttpExchange exchange, int userId) throws IOException {
            try {
                User user = userDAO.getUserById(userId);
                if (user != null) {
                    String jsonResponse = convertUserToJson(user);
                    sendJsonResponse(exchange, 200, jsonResponse);
                } else {
                    sendErrorResponse(exchange, 404, "User not found");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
            }
        }
        
        private void handleUpdateUser(HttpExchange exchange, int userId) throws IOException {
            try {
                String requestBody = readRequestBody(exchange);
                User user = parseUserFromJson(requestBody);
                user.setId(userId);
                
                // Validate user data
                String validationError = validateUser(user);
                if (validationError != null) {
                    sendErrorResponse(exchange, 400, validationError);
                    return;
                }
                
                boolean success = userDAO.updateUser(user);
                if (success) {
                    String jsonResponse = convertUserToJson(user);
                    sendJsonResponse(exchange, 200, jsonResponse);
                } else {
                    sendErrorResponse(exchange, 404, "User not found or update failed");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 400, "Invalid request: " + e.getMessage());
            }
        }
        
        private void handleDeleteUser(HttpExchange exchange, int userId) throws IOException {
            try {
                boolean success = userDAO.deleteUser(userId);
                if (success) {
                    sendJsonResponse(exchange, 200, "{\"message\":\"User deleted successfully\"}");
                } else {
                    sendErrorResponse(exchange, 404, "User not found");
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Handler for health check endpoint
     */
    private class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            
            if ("GET".equals(exchange.getRequestMethod())) {
                String healthResponse = "{\"status\":\"OK\",\"timestamp\":\"" + 
                    java.time.Instant.now().toString() + "\"}";
                sendJsonResponse(exchange, 200, healthResponse);
            } else {
                sendErrorResponse(exchange, 405, "Method not allowed");
            }
        }
    }
    
    /**
     * CORS handler for browser compatibility
     */
    private class CorsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            exchange.sendResponseHeaders(404, -1);
        }
    }
    
    // Utility methods
    
    private void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }
    
    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        byte[] response = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, response.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.close();
    }
    
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        String jsonError = "{\"error\":\"" + errorMessage + "\"}";
        sendJsonResponse(exchange, statusCode, jsonError);
    }
    
    // Simple JSON conversion methods (without external libraries)
    
    private String convertUsersToJson(List<User> users) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            if (i > 0) json.append(",");
            json.append(convertUserToJson(users.get(i)));
        }
        json.append("]");
        return json.toString();
    }
    
    private String convertUserToJson(User user) {
        return String.format(
            "{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"address\":\"%s\"}",
            user.getId(),
            escapeJson(user.getName()),
            escapeJson(user.getEmail()),
            escapeJson(user.getPhone()),
            escapeJson(user.getAddress())
        );
    }
    
    private User parseUserFromJson(String json) {
        // Simple JSON parsing (in production, use a proper JSON library)
        User user = new User();
        
        // Extract values using regex (simplified approach)
        String name = extractJsonValue(json, "name");
        String email = extractJsonValue(json, "email");
        String phone = extractJsonValue(json, "phone");
        String address = extractJsonValue(json, "address");
        
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        
        return user;
    }
    
    private String extractJsonValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]+)\"");
        java.util.regex.Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }
    
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
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
}
