package com.mvc.api.test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Cliente de prueba para la API REST con Swagger
 * Demuestra cómo usar todos los endpoints de la API
 */
public class ApiTestClient {
    private static final String BASE_URL = "http://localhost:9090";
    
    public static void main(String[] args) {
        System.out.println("=== API Test Client - MVC User Management ===");
        System.out.println("Probando API en: " + BASE_URL);
        System.out.println("");
        
        try {
            // 1. Health Check
            System.out.println("1. Testing Health Check...");
            testHealthCheck();
            
            // 2. Get all users (initially empty)
            System.out.println("\n2. Getting all users (should be empty initially)...");
            testGetAllUsers();
            
            // 3. Create a new user
            System.out.println("\n3. Creating a new user...");
            testCreateUser();
            
            // 4. Get all users again (should have 1 user)
            System.out.println("\n4. Getting all users again...");
            testGetAllUsers();
            
            // 5. Get user by ID
            System.out.println("\n5. Getting user by ID 1...");
            testGetUserById(1);
            
            // 6. Update user
            System.out.println("\n6. Updating user ID 1...");
            testUpdateUser(1);
            
            // 7. Get updated user
            System.out.println("\n7. Getting updated user...");
            testGetUserById(1);
            
            // 8. Delete user
            System.out.println("\n8. Deleting user ID 1...");
            testDeleteUser(1);
            
            // 9. Verify deletion
            System.out.println("\n9. Verifying deletion...");
            testGetAllUsers();
            
            System.out.println("\n=== Todas las pruebas completadas exitosamente! ===");
            System.out.println("Puedes ver la documentacion Swagger en: " + BASE_URL + "/swagger");
            
        } catch (Exception e) {
            System.err.println("Error durante las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testHealthCheck() throws Exception {
        String response = sendRequest("GET", "/api/health", null);
        System.out.println("Health Check Response: " + response);
    }
    
    private static void testGetAllUsers() throws Exception {
        String response = sendRequest("GET", "/api/users", null);
        System.out.println("Get All Users Response: " + response);
    }
    
    private static void testCreateUser() throws Exception {
        String jsonUser = "{\n" +
            "  \"name\": \"Juan Perez\",\n" +
            "  \"email\": \"juan.perez@email.com\",\n" +
            "  \"phone\": \"+57 300 123 4567\",\n" +
            "  \"address\": \"Calle 123 #45-67, Bogota\"\n" +
            "}";
        
        String response = sendRequest("POST", "/api/users", jsonUser);
        System.out.println("Create User Response: " + response);
    }
    
    private static void testGetUserById(int id) throws Exception {
        String response = sendRequest("GET", "/api/users/" + id, null);
        System.out.println("Get User By ID Response: " + response);
    }
    
    private static void testUpdateUser(int id) throws Exception {
        String jsonUser = "{\n" +
            "  \"name\": \"Juan Carlos Perez Actualizado\",\n" +
            "  \"email\": \"juan.carlos@email.com\",\n" +
            "  \"phone\": \"+57 301 999 8888\",\n" +
            "  \"address\": \"Carrera 456 #78-90, Medellin\"\n" +
            "}";
        
        String response = sendRequest("PUT", "/api/users/" + id, jsonUser);
        System.out.println("Update User Response: " + response);
    }
    
    private static void testDeleteUser(int id) throws Exception {
        String response = sendRequest("DELETE", "/api/users/" + id, null);
        System.out.println("Delete User Response: " + response);
    }
    
    private static String sendRequest(String method, String endpoint, String jsonBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Setup request
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        // Send JSON body if provided
        if (jsonBody != null && !jsonBody.trim().isEmpty()) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
        
        // Read response
        int responseCode = connection.getResponseCode();
        InputStream inputStream = responseCode >= 200 && responseCode < 300 
            ? connection.getInputStream() 
            : connection.getErrorStream();
            
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        
        String result = "HTTP " + responseCode + ": " + response.toString();
        connection.disconnect();
        
        return result;
    }
}
