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
import java.nio.file.Files;
import java.util.regex.Pattern;

/**
 * Servidor completo con API REST, Swagger y vista web integrados
 * Sirve tanto la API como los archivos estáticos de la interfaz web
 */
public class FullApiServerWithWeb {
    private HttpServer server;
    private SwaggerHandler swaggerHandler;
    private UserDAO userDAO;
    private static final int PORT = 9090;
    private static final String WEB_ROOT = "web";
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    public FullApiServerWithWeb() throws IOException {
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
        
        // Rutas de Swagger
        server.createContext("/swagger", new SwaggerUIHandler());
        server.createContext("/swagger/api-docs", new SwaggerSpecHandler());
        
        // Ruta raíz que redirige a la vista web
        server.createContext("/", new WebHandler());
        
        // Archivos estáticos de la vista web
        server.createContext("/web", new StaticFileHandler());
    }
    
    // Handler para la raíz que redirige a la vista web
    class WebHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            if ("/".equals(path)) {
                // Redirigir a la vista web
                exchange.getResponseHeaders().set("Location", "/web/");
                exchange.sendResponseHeaders(302, -1);
                return;
            }
            
            // Para otras rutas, servir archivos estáticos
            serveStaticFile(exchange, path);
        }
    }
    
    // Handler para archivos estáticos
    class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            serveStaticFile(exchange, path);
        }
    }
    
    private void serveStaticFile(HttpExchange exchange, String path) throws IOException {
        // Remover /web del path si existe
        if (path.startsWith("/web")) {
            path = path.substring(4);
        }
        
        // Si es la raíz, servir index.html
        if (path.equals("/") || path.isEmpty()) {
            path = "/index.html";
        }
        
        String filePath = WEB_ROOT + path;
        File file = new File(filePath);
        
        if (!file.exists() || !file.isFile()) {
            // Archivo no encontrado
            String response = createNotFoundPage();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(404, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            os.close();
            return;
        }
        
        // Determinar tipo de contenido
        String contentType = getContentType(path);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        
        // Agregar headers CORS para desarrollo
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        
        // Leer y enviar el archivo
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            exchange.sendResponseHeaders(200, fileBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(fileBytes);
            os.close();
        } catch (IOException e) {
            String response = "Error interno del servidor";
            exchange.sendResponseHeaders(500, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=UTF-8";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".ico")) return "image/x-icon";
        return "text/plain";
    }
    
    private String createNotFoundPage() {
        return "<!DOCTYPE html>" +
               "<html><head><title>404 - No Encontrado</title>" +
               "<style>body{font-family:Arial,sans-serif;text-align:center;margin-top:50px;}" +
               "h1{color:#e74c3c;}a{color:#3498db;text-decoration:none;}</style></head>" +
               "<body><h1>404 - Página No Encontrada</h1>" +
               "<p>La página que buscas no existe.</p>" +
               "<a href='/web/'>← Volver al inicio</a></body></html>";
    }
    
    // Handler para usuarios (CRUD completo)
    class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Configurar headers CORS
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            
            String method = exchange.getRequestMethod();
            
            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            try {
                switch (method) {
                    case "GET":
                        handleGetUsers(exchange);
                        break;
                    case "POST":
                        handleCreateUser(exchange);
                        break;
                    case "PUT":
                        handleUpdateUser(exchange);
                        break;
                    case "DELETE":
                        handleDeleteUser(exchange);
                        break;
                    default:
                        sendResponse(exchange, 405, "Método no permitido");
                }
            } catch (Exception e) {
                System.err.println("Error en UsersHandler: " + e.getMessage());
                e.printStackTrace();
                sendResponse(exchange, 500, "Error interno del servidor: " + e.getMessage());
            }
        }
        
        private void handleGetUsers(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            if (path.matches("/api/users/\\d+")) {
                // GET /api/users/{id} - Obtener usuario por ID
                String[] parts = path.split("/");
                int userId = Integer.parseInt(parts[parts.length - 1]);
                
                User user = userDAO.getUserById(userId);
                if (user != null) {
                    String jsonResponse = userToJson(user);
                    sendJsonResponse(exchange, 200, jsonResponse);
                } else {
                    sendResponse(exchange, 404, "Usuario no encontrado");
                }
            } else {
                // GET /api/users - Obtener todos los usuarios
                List<User> users = userDAO.getAllUsers();
                String jsonResponse = usersToJson(users);
                sendJsonResponse(exchange, 200, jsonResponse);
            }
        }
        
        private void handleCreateUser(HttpExchange exchange) throws IOException {
            String requestBody = readRequestBody(exchange);
            
            try {
                User user = parseUserFromJson(requestBody);
                
                // Validar datos
                validateUser(user);
                
                boolean created = userDAO.createUser(user);
                if (created) {
                    String jsonResponse = userToJson(user);
                    sendJsonResponse(exchange, 201, jsonResponse);
                } else {
                    sendResponse(exchange, 500, "Error al crear el usuario");
                }
                
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, 400, "Datos inválidos: " + e.getMessage());
            }
        }
        
        private void handleUpdateUser(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            
            if (parts.length < 4) {
                sendResponse(exchange, 400, "ID de usuario requerido");
                return;
            }
            
            int userId = Integer.parseInt(parts[parts.length - 1]);
            String requestBody = readRequestBody(exchange);
            
            try {
                User user = parseUserFromJson(requestBody);
                user.setId(userId);
                
                // Validar datos
                validateUser(user);
                
                boolean updated = userDAO.updateUser(user);
                if (updated) {
                    String jsonResponse = userToJson(user);
                    sendJsonResponse(exchange, 200, jsonResponse);
                } else {
                    sendResponse(exchange, 404, "Usuario no encontrado");
                }
                
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, 400, "Datos inválidos: " + e.getMessage());
            }
        }
        
        private void handleDeleteUser(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            
            if (parts.length < 4) {
                sendResponse(exchange, 400, "ID de usuario requerido");
                return;
            }
            
            int userId = Integer.parseInt(parts[parts.length - 1]);
            
            boolean deleted = userDAO.deleteUser(userId);
            if (deleted) {
                sendResponse(exchange, 200, "Usuario eliminado exitosamente");
            } else {
                sendResponse(exchange, 404, "Usuario no encontrado");
            }
        }
    }
    
    // Handler para health check
    class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            
            String response = "{\"status\":\"UP\",\"timestamp\":\"" + 
                            java.time.Instant.now().toString() + "\"}";
            sendJsonResponse(exchange, 200, response);
        }
    }
    
    // Handler para Swagger UI
    class SwaggerUIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String swaggerUI = swaggerHandler.generateSwaggerUI();
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, swaggerUI.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(swaggerUI.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
    
    // Handler para especificación OpenAPI
    class SwaggerSpecHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String openApiSpec = swaggerHandler.generateOpenApiSpec();
            sendJsonResponse(exchange, 200, openApiSpec);
        }
    }
    
    // Métodos de utilidad (mismo código que antes)
    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es requerido");
        }
    }
    
    private User parseUserFromJson(String json) {
        // Parser JSON simple para User
        User user = new User();
        
        // Remover llaves y espacios
        json = json.trim().substring(1, json.length() - 1);
        
        // Separar por comas
        String[] pairs = json.split(",");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("\"", "");
                String value = keyValue[1].trim().replaceAll("\"", "");
                
                // Convertir "null" string a null
                if ("null".equals(value)) {
                    value = null;
                }
                
                switch (key) {
                    case "name":
                        user.setName(value);
                        break;
                    case "email":
                        user.setEmail(value);
                        break;
                    case "phone":
                        user.setPhone(value);
                        break;
                    case "address":
                        user.setAddress(value);
                        break;
                }
            }
        }
        
        return user;
    }
    
    private String userToJson(User user) {
        return String.format(
            "{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"address\":%s}",
            user.getId(),
            escapeJson(user.getName()),
            escapeJson(user.getEmail()),
            escapeJson(user.getPhone()),
            user.getAddress() != null ? "\"" + escapeJson(user.getAddress()) + "\"" : "null"
        );
    }
    
    private String usersToJson(List<User> users) {
        return "[" + users.stream()
                .map(this::userToJson)
                .collect(Collectors.joining(",")) + "]";
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        StringBuilder body = new StringBuilder();
        char[] buffer = new char[1024];
        int length;
        while ((length = reader.read(buffer)) != -1) {
            body.append(buffer, 0, length);
        }
        return body.toString();
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
    
    public void start() {
        server.start();
        System.out.println("=== MVC User Management - Servidor Completo ===");
        System.out.println("Servidor iniciado exitosamente en puerto " + PORT);
        System.out.println();
        System.out.println("URLs disponibles:");
        System.out.println("   Vista Web:              http://localhost:" + PORT + "/");
        System.out.println("   Vista Web (directa):    http://localhost:" + PORT + "/web/");
        System.out.println("   Documentacion Swagger:  http://localhost:" + PORT + "/swagger");
        System.out.println("   API Specification:      http://localhost:" + PORT + "/swagger/api-docs");
        System.out.println("   Health Check:           http://localhost:" + PORT + "/api/health");
        System.out.println();
        System.out.println("API Endpoints:");
        System.out.println("   GET    /api/users          - Obtener todos los usuarios");
        System.out.println("   POST   /api/users          - Crear un usuario");
        System.out.println("   GET    /api/users/{id}     - Obtener usuario por ID");
        System.out.println("   PUT    /api/users/{id}     - Actualizar usuario");
        System.out.println("   DELETE /api/users/{id}     - Eliminar usuario");
        System.out.println();
        System.out.println("Presiona Ctrl+C para detener el servidor");
    }
    
    public void stop() {
        server.stop(0);
    }
    
    public static void main(String[] args) {
        try {
            FullApiServerWithWeb server = new FullApiServerWithWeb();
            server.start();
            
            // Manejar cierre elegante
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nDeteniendo servidor...");
                server.stop();
                System.out.println("Servidor detenido");
            }));
            
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
