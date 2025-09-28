package com.supermercado.api;

import com.mvc.dao.UserDAOMock;
import com.mvc.model.User;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AuthHandler implements HttpHandler {
    
    private UserDAOMock userDAO;
    
    public AuthHandler() {
        this.userDAO = new UserDAOMock();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        // Habilitar CORS
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        
        try {
            if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            if ("/api/auth/login".equals(path) && "POST".equals(method)) {
                handleLogin(exchange);
            } else if ("/api/auth/users".equals(path) && "GET".equals(method)) {
                handleGetUsers(exchange);
            } else {
                sendErrorResponse(exchange, 405, "Metodo no permitido");
            }
            
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, "Error interno del servidor");
        }
    }
    
    private void handleLogin(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        
        try {
            String email = extractJsonValue(requestBody, "email");
            String name = extractJsonValue(requestBody, "name");
            
            if (email == null || name == null) {
                sendErrorResponse(exchange, 400, "Email y nombre son requeridos");
                return;
            }
            
            List<User> users = userDAO.getAllUsers();
            User authenticatedUser = null;
            
            for (User user : users) {
                if (email.trim().equalsIgnoreCase(user.getEmail()) && 
                    name.trim().equalsIgnoreCase(user.getName())) {
                    authenticatedUser = user;
                    break;
                }
            }
            
            if (authenticatedUser != null) {
                String jsonResponse = String.format(
                    "{\"success\":true,\"message\":\"Login exitoso\",\"user\":{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\"}}",
                    authenticatedUser.getId(), authenticatedUser.getName(), authenticatedUser.getEmail()
                );
                sendJsonResponse(exchange, 200, jsonResponse);
            } else {
                sendErrorResponse(exchange, 401, "Credenciales invalidas");
            }
            
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, "Error procesando login");
        }
    }
    
    private void handleGetUsers(HttpExchange exchange) throws IOException {
        try {
            List<User> users = userDAO.getAllUsers();
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");
            
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                if (i > 0) jsonBuilder.append(",");
                jsonBuilder.append(String.format(
                    "{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\"}",
                    user.getId(), user.getName(), user.getEmail()
                ));
            }
            
            jsonBuilder.append("]");
            sendJsonResponse(exchange, 200, jsonBuilder.toString());
            
        } catch (Exception e) {
            sendErrorResponse(exchange, 500, "Error obteniendo usuarios");
        }
    }
    
    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return null;
        
        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return null;
        
        int valueStart = json.indexOf("\"", colonIndex);
        if (valueStart == -1) return null;
        
        int valueEnd = json.indexOf("\"", valueStart + 1);
        if (valueEnd == -1) return null;
        
        return json.substring(valueStart + 1, valueEnd);
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        
        byte[] response = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, response.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
    
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        String jsonResponse = String.format("{\"success\":false,\"error\":\"%s\"}", message);
        sendJsonResponse(exchange, statusCode, jsonResponse);
    }
}