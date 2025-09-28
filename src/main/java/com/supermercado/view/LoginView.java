package com.supermercado.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import com.mvc.dao.UserDAOMock;
import com.mvc.model.User;
import java.util.List;

/**
 * Ventana de Login para acceder al Sistema de Empleados
 * Requiere autenticación con usuarios del sistema principal
 */
public class LoginView extends JDialog {
    
    private JTextField emailTextField;
    private JTextField nombreTextField;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    private JComboBox<String> sistemaComboBox;
    private boolean loginExitoso = false;
    private String sistemaSeleccionado = "empleados";
    
    public LoginView(Frame parent) {
        super(parent, "Acceso a los Sistemas", true);
        initializeComponents();
        setupLayout();
        configureDialog();
    }
    
    private void initializeComponents() {
        emailTextField = new JTextField(25);
        emailTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailTextField.setPreferredSize(new Dimension(280, 32));
        emailTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        
        nombreTextField = new JTextField(25);
        nombreTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        nombreTextField.setPreferredSize(new Dimension(280, 32));
        nombreTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(140, 35));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        
        cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setPreferredSize(new Dimension(140, 35));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        
        sistemaComboBox = new JComboBox<>(new String[]{
            "Sistema de Empleados", "Sistema de Usuarios", "Sistema Web de Empleados"
        });
        sistemaComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        sistemaComboBox.setPreferredSize(new Dimension(280, 32));
        sistemaComboBox.setSelectedIndex(0); // Por defecto Sistema de Empleados
        
        // Agregar listener para Enter en los campos de texto
        ActionListener enterAction = e -> loginButton.doClick();
        emailTextField.addActionListener(enterAction);
        nombreTextField.addActionListener(enterAction);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        
        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Título
        JLabel titleLabel = new JLabel("Acceso a los Sistemas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(63, 81, 181));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 20, 10);
        mainPanel.add(titleLabel, gbc);
        
        // Instrucciones
        JLabel instrLabel = new JLabel("<html><div style='text-align: center; width: 400px; line-height: 1.4;'>Ingrese sus credenciales del sistema de usuarios para acceder al sistema seleccionado. Debe usar el mismo email y nombre registrado en el sistema principal.</div></html>");
        instrLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instrLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 15, 10);
        mainPanel.add(instrLabel, gbc);
        
        // Selector de sistema
        JLabel sistemaLabel = new JLabel("Sistema:");
        sistemaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sistemaLabel.setForeground(new Color(60, 60, 60));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(sistemaLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        mainPanel.add(sistemaComboBox, gbc);
        
        // Reset insets
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(60, 60, 60));
        mainPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        mainPanel.add(emailTextField, gbc);
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nombreLabel.setForeground(new Color(60, 60, 60));
        mainPanel.add(nombreLabel, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        mainPanel.add(nombreTextField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.insets = new Insets(8, 10, 8, 10);
        mainPanel.add(statusLabel, gbc);
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel de información
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Obtener usuarios dinámicamente del UserDAOMock
        StringBuilder userListHtml = new StringBuilder("<html><div style='text-align: center;'>");
        userListHtml.append("<b style='color: #3F51B5; font-size: 12px;'>Usuarios de prueba disponibles:</b><br><br>");
        
        try {
            UserDAOMock userDAO = new UserDAOMock();
            List<User> users = userDAO.getAllUsers();
            
            for (User user : users) {
                userListHtml.append("<div style='margin: 3px 0; color: #666666;'>")
                           .append("• <b>")
                           .append(user.getName())
                           .append("</b> (")
                           .append(user.getEmail())
                           .append(")</div>");
            }
        } catch (Exception e) {
            userListHtml.append("<div style='color: #FF5722;'>• Error al cargar usuarios de prueba</div>");
        }
        
        userListHtml.append("</div></html>");
        
        JLabel infoLabel = new JLabel(userListHtml.toString());
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(infoLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private void configureDialog() {
        setSize(580, 520);
        setMinimumSize(new Dimension(550, 480));
        setLocationRelativeTo(getParent());
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Focus inicial en el campo email
        SwingUtilities.invokeLater(() -> emailTextField.requestFocus());
        
        // Cancelar por defecto cierra el diálogo
        cancelButton.addActionListener(e -> {
            loginExitoso = false;
            dispose();
        });
        
        // Listener para cambio de sistema
        sistemaComboBox.addActionListener(e -> {
            int selectedIndex = sistemaComboBox.getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    sistemaSeleccionado = "empleados";
                    break;
                case 1:
                    sistemaSeleccionado = "usuarios";
                    break;
                case 2:
                    sistemaSeleccionado = "empleados-web";
                    break;
                default:
                    sistemaSeleccionado = "empleados";
            }
        });
    }
    
    // Métodos públicos para el controlador
    
    public String getEmail() {
        return emailTextField.getText().trim();
    }
    
    public String getNombre() {
        return nombreTextField.getText().trim();
    }
    
    public void setStatusMessage(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : new Color(76, 175, 80));
    }
    
    public void clearFields() {
        emailTextField.setText("");
        nombreTextField.setText("");
        statusLabel.setText(" ");
        emailTextField.requestFocus();
    }
    
    public void setLoginExitoso(boolean exitoso) {
        this.loginExitoso = exitoso;
    }
    
    public boolean isLoginExitoso() {
        return loginExitoso;
    }
    
    public JButton getLoginButton() {
        return loginButton;
    }
    
    public void focusEmail() {
        emailTextField.requestFocus();
    }
    
    public String getSistemaSeleccionado() {
        return sistemaSeleccionado;
    }
    
    public boolean isEmpleadosSelected() {
        return "empleados".equals(sistemaSeleccionado);
    }
    
    public boolean isUsuariosSelected() {
        return "usuarios".equals(sistemaSeleccionado);
    }
    
    public boolean isEmpleadosWebSelected() {
        return "empleados-web".equals(sistemaSeleccionado);
    }
}