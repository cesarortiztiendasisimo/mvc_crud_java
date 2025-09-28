package com.mvc.dao;

import com.mvc.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock implementation of UserDAO for testing without database
 * This allows testing the MVC pattern without MySQL setup
 */
public class UserDAOMock implements UserDAO {
    
    private List<User> users;
    private int nextId;
    
    public UserDAOMock() {
        this.users = new ArrayList<>();
        this.nextId = 1;
        
        // Add some sample data
        users.add(new User(nextId++, "Cesar Ortiz", "brccesar@gmail.com", "+57 3057515403", "Calle 123 #45-67, Bogotá"));
        users.add(new User(nextId++, "María García", "maria.garcia@email.com", "+57 301 234 5678", "Carrera 89 #12-34, Medellín"));
        users.add(new User(nextId++, "Carlos López", "carlos.lopez@email.com", "+57 302 345 6789", "Avenida 56 #78-90, Cali"));
    }
    
    @Override
    public boolean createUser(User user) {
        try {
            user.setId(nextId++);
            users.add(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public User getUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    @Override
    public boolean updateUser(User user) {
        try {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == user.getId()) {
                    users.set(i, user);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean deleteUser(int id) {
        try {
            return users.removeIf(user -> user.getId() == id);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<User> searchUsersByName(String name) {
        return users.stream()
                .filter(user -> user.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(ArrayList::new, (list, user) -> list.add(user), (list1, list2) -> list1.addAll(list2));
    }
}
