package com.mvc.dao;

import com.mvc.model.User;
import java.util.List;

/**
 * User Data Access Object interface
 * Defines CRUD operations for User entity
 */
public interface UserDAO {
    
    /**
     * Create a new user
     * @param user User object to create
     * @return true if user was created successfully, false otherwise
     */
    boolean createUser(User user);
    
    /**
     * Retrieve a user by ID
     * @param id User ID
     * @return User object if found, null otherwise
     */
    User getUserById(int id);
    
    /**
     * Retrieve all users
     * @return List of all users
     */
    List<User> getAllUsers();
    
    /**
     * Update an existing user
     * @param user User object with updated information
     * @return true if user was updated successfully, false otherwise
     */
    boolean updateUser(User user);
    
    /**
     * Delete a user by ID
     * @param id User ID to delete
     * @return true if user was deleted successfully, false otherwise
     */
    boolean deleteUser(int id);
    
    /**
     * Search users by name
     * @param name Name to search for
     * @return List of users matching the name
     */
    List<User> searchUsersByName(String name);
}
