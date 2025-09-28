package com.mvc.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * API Client example for testing the User Management API
 * Demonstrates how to consume the REST API from Java
 */
public class ApiClient {
    
    private static final String BASE_URL = "http://localhost:9090/api";
    
    public static void main(String[] args) {
        ApiClient client = new ApiClient();
        
        System.out.println("=== MVC User Management API Client ===");
        System.out.println("Testing REST API endpoints...\n");
        
        try {
            // Test health check
            System.out.println("1. Health Check:");
            String health = client.get("/health");
            System.out.println("Response: " + health + "\n");
            
            // Test get all users
            System.out.println("2. Get All Users:");
            String users = client.get("/users");
            System.out.println("Response: " + users + "\n");
            
            // Test create user
            System.out.println("3. Create New User:");
            String newUser = "{\"name\":\"API Test User\",\"email\":\"apitest@email.com\",\"phone\":\"+57 305 555 0123\",\"address\":\"API Test Address 123\"}";
            String createdUser = client.post("/users", newUser);
            System.out.println("Response: " + createdUser + "\n");
            
            // Test get user by ID
            System.out.println("4. Get User by ID (4):");
            String userById = client.get("/users/4");
            System.out.println("Response: " + userById + "\n");
            
            // Test update user
            System.out.println("5. Update User (ID 4):");
            String updatedUser = "{\"name\":\"Updated API User\",\"email\":\"updated@email.com\",\"phone\":\"+57 306 555 0123\",\"address\":\"Updated Address 456\"}";
            String updateResponse = client.put("/users/4", updatedUser);
            System.out.println("Response: " + updateResponse + "\n");
            
            // Test get all users again to see changes
            System.out.println("6. Get All Users (after update):");
            String usersAfterUpdate = client.get("/users");
            System.out.println("Response: " + usersAfterUpdate + "\n");
            
            // Test delete user
            System.out.println("7. Delete User (ID 4):");
            String deleteResponse = client.delete("/users/4");
            System.out.println("Response: " + deleteResponse + "\n");
            
            // Test get all users again to see deletion
            System.out.println("8. Get All Users (after delete):");
            String usersAfterDelete = client.get("/users");
            System.out.println("Response: " + usersAfterDelete + "\n");
            
        } catch (Exception e) {
            System.err.println("Error testing API: " + e.getMessage());
            System.err.println("Make sure the API server is running (ApiServer.java)");
        }
    }
    
    public String get(String endpoint) throws IOException {
        return makeRequest("GET", endpoint, null);
    }
    
    public String post(String endpoint, String jsonBody) throws IOException {
        return makeRequest("POST", endpoint, jsonBody);
    }
    
    public String put(String endpoint, String jsonBody) throws IOException {
        return makeRequest("PUT", endpoint, jsonBody);
    }
    
    public String delete(String endpoint) throws IOException {
        return makeRequest("DELETE", endpoint, null);
    }
    
    private String makeRequest(String method, String endpoint, String jsonBody) throws IOException {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        
        if (jsonBody != null) {
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        
        return String.format("Status: %d, Body: %s", responseCode, response.toString());
    }
}
