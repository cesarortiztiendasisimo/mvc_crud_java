package com.supermercado.controller;

import com.mvc.service.AuthenticationService;
import com.supermercado.view.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador de Login
 * Maneja la autenticación de usuarios para acceder al sistema de empleados
 */
public class LoginController {
    
    private LoginView loginView;
    private AuthenticationService authService;
    
    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.authService = AuthenticationService.getInstance();
        
        initializeEventListeners();
        loginView.focusEmail();
    }
    
    private void initializeEventListeners() {
        loginView.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarLogin();
            }
        });
    }
    
    private void procesarLogin() {
        String email = loginView.getEmail();
        String nombre = loginView.getNombre();
        
        // Validar campos
        if (email.isEmpty() || nombre.isEmpty()) {
            loginView.setStatusMessage("Por favor complete todos los campos", true);
            return;
        }
        
        // Validar formato de email básico
        if (!email.contains("@") || !email.contains(".")) {
            loginView.setStatusMessage("Formato de email inválido", true);
            return;
        }
        
        // Intentar autenticación
        loginView.setStatusMessage("Verificando credenciales...", false);
        
        // Simular pequeña demora para realismo
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        boolean autenticado = authService.autenticar(email, nombre);
        
        if (autenticado) {
            loginView.setStatusMessage("¡Autenticación exitosa!", false);
            loginView.setLoginExitoso(true);
            
            // Cerrar ventana después de un momento
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        loginView.dispose();
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
            
        } else {
            loginView.setStatusMessage("Credenciales incorrectas. Usuario no encontrado.", true);
            loginView.clearFields();
        }
    }
}