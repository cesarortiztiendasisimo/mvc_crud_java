package com.mvc.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection configuration
 * Handles MySQL database connection using Singleton pattern
 */
public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Update with your MySQL password
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static DatabaseConfig instance;
    private Connection connection;
    
    private DatabaseConfig() throws SQLException {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        } catch (SQLException e) {
            throw new SQLException("Failed to create database connection", e);
        }
    }
    
    /**
     * Get database connection instance (Singleton pattern)
     * @return Connection instance
     * @throws SQLException if connection fails
     */
    public static DatabaseConfig getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConfig();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
    
    /**
     * Get the database connection
     * @return Connection object
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
