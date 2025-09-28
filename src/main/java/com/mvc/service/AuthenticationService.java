package com.mvc.service;

import com.mvc.dao.UserDAO;
import com.mvc.dao.UserDAOMock;
import com.mvc.model.User;
import java.util.List;

/**
 * Servicio de Autenticación
 * Valida usuarios del sistema principal para acceder al sistema de empleados
 */
public class AuthenticationService {
    
    private static AuthenticationService instance;
    private UserDAO userDAO;
    private User usuarioAutenticado;
    
    private AuthenticationService() {
        this.userDAO = new UserDAOMock();
        this.usuarioAutenticado = null;
    }
    
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }
    
    /**
     * Autenticar usuario por email y nombre
     * @param email Email del usuario
     * @param nombre Nombre del usuario
     * @return true si la autenticación es exitosa
     */
    public boolean autenticar(String email, String nombre) {
        if (email == null || nombre == null || email.trim().isEmpty() || nombre.trim().isEmpty()) {
            return false;
        }
        
        List<User> usuarios = userDAO.getAllUsers();
        
        for (User user : usuarios) {
            if (user.getEmail().equalsIgnoreCase(email.trim()) && 
                user.getName().equalsIgnoreCase(nombre.trim())) {
                this.usuarioAutenticado = user;
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Verificar si hay un usuario autenticado
     * @return true si hay usuario autenticado
     */
    public boolean isAutenticado() {
        return usuarioAutenticado != null;
    }
    
    /**
     * Obtener el usuario autenticado
     * @return Usuario autenticado o null
     */
    public User getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    
    /**
     * Cerrar sesión
     */
    public void logout() {
        this.usuarioAutenticado = null;
    }
    
    /**
     * Obtener información del usuario autenticado para mostrar
     * @return String con información del usuario
     */
    public String getInfoUsuarioAutenticado() {
        if (usuarioAutenticado != null) {
            return String.format("%s (%s)", usuarioAutenticado.getName(), usuarioAutenticado.getEmail());
        }
        return "No autenticado";
    }
}