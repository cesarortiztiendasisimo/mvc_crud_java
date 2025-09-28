package com.supermercado.view;

import com.supermercado.model.Empleado;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.List;

/**
 * Vista Principal del Sistema de Empleados
 * Arquitectura 4+1 - Vista de Procesos
 * Interfaz gráfica para consultar empleados por cargo
 */
public class EmpleadoView extends JFrame {
    
    // Componentes de búsqueda
    private JComboBox<String> cargosComboBox;
    private JTextField busquedaTextField;
    private JButton buscarButton;
    private JButton mostrarTodosButton;
    private JButton limpiarButton;
    
    // Componentes de formulario
    private JTextField idTextField;
    private JTextField nombreTextField;
    private JTextField cargoTextField;
    private JTextField salarioTextField;
    private JComboBox<String> departamentoComboBox;
    
    // Botones CRUD
    private JButton crearButton;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JButton nuevoButton;
    
    // Tabla de resultados
    private JTable empleadosTable;
    private DefaultTableModel tableModel;
    
    // Componentes informativos
    private JLabel contadorLabel;
    private JLabel estadisticasLabel;
    private JLabel usuarioLabel;
    private JButton logoutButton;
    
    public EmpleadoView() {
        initializeComponents();
        setupLayout();
        configureTable();
        configureWindow();
    }
    
    /**
     * Inicializar todos los componentes de la interfaz
     */
    private void initializeComponents() {
        // Componentes de búsqueda con estilo moderno
        cargosComboBox = new JComboBox<>();
        cargosComboBox.addItem("-- Seleccionar Cargo --");
        cargosComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        cargosComboBox.setPreferredSize(new Dimension(180, 28));
        
        busquedaTextField = new JTextField(20);
        busquedaTextField.setToolTipText("Buscar por cargo o nombre");
        busquedaTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        busquedaTextField.setPreferredSize(new Dimension(200, 28));
        busquedaTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        
        buscarButton = new JButton("Buscar");
        buscarButton.setToolTipText("Buscar empleados por cargo");
        buscarButton.setFont(new Font("Arial", Font.BOLD, 12));
        buscarButton.setPreferredSize(new Dimension(80, 28));
        buscarButton.setBackground(new Color(33, 150, 243));
        buscarButton.setForeground(Color.WHITE);
        buscarButton.setFocusPainted(false);
        buscarButton.setBorderPainted(false);
        
        mostrarTodosButton = new JButton("Mostrar Todos");
        mostrarTodosButton.setToolTipText("Mostrar todos los empleados");
        mostrarTodosButton.setFont(new Font("Arial", Font.BOLD, 12));
        mostrarTodosButton.setPreferredSize(new Dimension(110, 28));
        mostrarTodosButton.setBackground(new Color(156, 39, 176));
        mostrarTodosButton.setForeground(Color.WHITE);
        mostrarTodosButton.setFocusPainted(false);
        mostrarTodosButton.setBorderPainted(false);
        
        limpiarButton = new JButton("Limpiar");
        limpiarButton.setToolTipText("Limpiar formulario");
        limpiarButton.setFont(new Font("Arial", Font.BOLD, 12));
        limpiarButton.setPreferredSize(new Dimension(80, 28));
        limpiarButton.setBackground(new Color(96, 125, 139));
        limpiarButton.setForeground(Color.WHITE);
        limpiarButton.setFocusPainted(false);
        limpiarButton.setBorderPainted(false);
        
        // Componentes de formulario mejorados
        idTextField = new JTextField(10);
        idTextField.setEditable(false);
        idTextField.setBackground(new Color(245, 245, 245));
        idTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        idTextField.setPreferredSize(new Dimension(200, 28));
        idTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        
        nombreTextField = new JTextField(25);
        nombreTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        nombreTextField.setPreferredSize(new Dimension(200, 28));
        nombreTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        
        cargoTextField = new JTextField(20);
        cargoTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        cargoTextField.setPreferredSize(new Dimension(200, 28));
        cargoTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        
        salarioTextField = new JTextField(15);
        salarioTextField.setFont(new Font("Arial", Font.PLAIN, 12));
        salarioTextField.setPreferredSize(new Dimension(200, 28));
        salarioTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        
        departamentoComboBox = new JComboBox<>(new String[]{
            "Ventas", "Logistica", "Administracion", 
            "Servicios Generales", "Seguridad", "Recursos Humanos"
        });
        departamentoComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        departamentoComboBox.setPreferredSize(new Dimension(200, 28));
        
        // Botones CRUD con colores Material Design
        crearButton = new JButton("Crear");
        crearButton.setBackground(new Color(76, 175, 80));
        crearButton.setForeground(Color.WHITE);
        crearButton.setFont(new Font("Arial", Font.BOLD, 12));
        crearButton.setPreferredSize(new Dimension(90, 32));
        crearButton.setFocusPainted(false);
        crearButton.setBorderPainted(false);
        
        actualizarButton = new JButton("Actualizar");
        actualizarButton.setBackground(new Color(33, 150, 243));
        actualizarButton.setForeground(Color.WHITE);
        actualizarButton.setFont(new Font("Arial", Font.BOLD, 12));
        actualizarButton.setPreferredSize(new Dimension(90, 32));
        actualizarButton.setFocusPainted(false);
        actualizarButton.setBorderPainted(false);
        
        eliminarButton = new JButton("Eliminar");
        eliminarButton.setBackground(new Color(244, 67, 54));
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.setFont(new Font("Arial", Font.BOLD, 12));
        eliminarButton.setPreferredSize(new Dimension(90, 32));
        eliminarButton.setFocusPainted(false);
        eliminarButton.setBorderPainted(false);
        
        nuevoButton = new JButton("Nuevo");
        nuevoButton.setBackground(new Color(156, 39, 176));
        nuevoButton.setForeground(Color.WHITE);
        nuevoButton.setFont(new Font("Arial", Font.BOLD, 12));
        nuevoButton.setPreferredSize(new Dimension(90, 32));
        nuevoButton.setFocusPainted(false);
        nuevoButton.setBorderPainted(false);
        
        // Tabla de empleados con estilo mejorado
        String[] columnNames = {"ID", "Nombre", "Cargo", "Salario", "Departamento", "Fecha Ingreso"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        empleadosTable = new JTable(tableModel);
        empleadosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        empleadosTable.setRowHeight(28);
        empleadosTable.setFont(new Font("Arial", Font.PLAIN, 12));
        empleadosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        empleadosTable.getTableHeader().setBackground(new Color(238, 238, 238));
        empleadosTable.getTableHeader().setForeground(new Color(60, 60, 60));
        empleadosTable.setGridColor(new Color(220, 220, 220));
        empleadosTable.setShowGrid(true);
        empleadosTable.setIntercellSpacing(new Dimension(1, 1));
        
        // Labels informativos con estilo mejorado
        contadorLabel = new JLabel("Total empleados: 0");
        contadorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        contadorLabel.setForeground(new Color(60, 60, 60));
        
        estadisticasLabel = new JLabel("Selecciona un cargo para ver estadisticas");
        estadisticasLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        estadisticasLabel.setForeground(new Color(120, 120, 120));
        
        usuarioLabel = new JLabel("Usuario: No autenticado");
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usuarioLabel.setForeground(new Color(63, 81, 181));
        
        logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setBackground(new Color(255, 152, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 11));
        logoutButton.setPreferredSize(new Dimension(110, 28));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        
        // Listener para selección de tabla
        empleadosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarEmpleadoSeleccionado();
            }
        });
    }
    
    /**
     * Configurar el diseño de la interfaz
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior - Búsqueda
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        // Panel central - Tabla
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Panel derecho - Formulario CRUD
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.EAST);
        
        // Panel inferior - Estadísticas
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Crear panel de búsqueda
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                "Búsqueda de Empleados",
                0, 0, new Font("Arial", Font.BOLD, 13), new Color(60, 60, 60)
            ),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.setBackground(new Color(248, 250, 252));
        
        JLabel cargoLabel = new JLabel("Cargo:");
        cargoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cargoLabel.setForeground(new Color(60, 60, 60));
        panel.add(cargoLabel);
        panel.add(cargosComboBox);
        
        panel.add(Box.createHorizontalStrut(10));
        
        JLabel busquedaLabel = new JLabel("Búsqueda libre:");
        busquedaLabel.setFont(new Font("Arial", Font.BOLD, 12));
        busquedaLabel.setForeground(new Color(60, 60, 60));
        panel.add(busquedaLabel);
        panel.add(busquedaTextField);
        
        panel.add(Box.createHorizontalStrut(10));
        
        panel.add(buscarButton);
        panel.add(mostrarTodosButton);
        panel.add(limpiarButton);
        
        return panel;
    }
    
    /**
     * Crear panel de tabla
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Empleados"));
        
        JScrollPane scrollPane = new JScrollPane(empleadosTable);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crear panel de formulario CRUD
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                "Gestión de Empleados",
                0, 0, new Font("Arial", Font.BOLD, 13), new Color(60, 60, 60)
            ),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setBackground(new Color(248, 249, 250));
        panel.setPreferredSize(new Dimension(350, 0));
        
        // Panel de campos
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(new Color(248, 249, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));
        idLabel.setForeground(new Color(60, 60, 60));
        fieldsPanel.add(idLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        idTextField.setPreferredSize(new Dimension(200, 28));
        fieldsPanel.add(idTextField, gbc);
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nombreLabel.setForeground(new Color(60, 60, 60));
        fieldsPanel.add(nombreLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        nombreTextField.setPreferredSize(new Dimension(200, 28));
        fieldsPanel.add(nombreTextField, gbc);
        
        // Cargo
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel cargoLabel = new JLabel("Cargo:");
        cargoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cargoLabel.setForeground(new Color(60, 60, 60));
        fieldsPanel.add(cargoLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        cargoTextField.setPreferredSize(new Dimension(200, 28));
        fieldsPanel.add(cargoTextField, gbc);
        
        // Salario
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel salarioLabel = new JLabel("Salario:");
        salarioLabel.setFont(new Font("Arial", Font.BOLD, 12));
        salarioLabel.setForeground(new Color(60, 60, 60));
        fieldsPanel.add(salarioLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        salarioTextField.setPreferredSize(new Dimension(200, 28));
        fieldsPanel.add(salarioTextField, gbc);
        
        // Departamento
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;
        JLabel deptLabel = new JLabel("Departamento:");
        deptLabel.setFont(new Font("Arial", Font.BOLD, 12));
        deptLabel.setForeground(new Color(60, 60, 60));
        fieldsPanel.add(deptLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        departamentoComboBox.setPreferredSize(new Dimension(200, 28));
        fieldsPanel.add(departamentoComboBox, gbc);
        
        // Panel de botones mejorado
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        buttonPanel.add(nuevoButton);
        buttonPanel.add(crearButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        
        // Agregar componentes al panel principal
        panel.add(fieldsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Crear panel de estadísticas
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        panel.setBackground(new Color(248, 249, 250));
        
        // Panel izquierdo - estadísticas
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(new Color(248, 249, 250));
        
        leftPanel.add(contadorLabel);
        
        JLabel separador = new JLabel(" | ");
        separador.setFont(new Font("Arial", Font.PLAIN, 12));
        separador.setForeground(new Color(150, 150, 150));
        leftPanel.add(separador);
        
        leftPanel.add(estadisticasLabel);
        
        // Panel derecho - usuario y logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(248, 249, 250));
        
        rightPanel.add(usuarioLabel);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(logoutButton);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Configurar tabla de empleados
     */
    private void configureTable() {
        // Configurar anchos de columnas
        empleadosTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        empleadosTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nombre
        empleadosTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Cargo
        empleadosTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Salario
        empleadosTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // Departamento
        empleadosTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Fecha
        
        // Estilo de tabla
        empleadosTable.setGridColor(Color.LIGHT_GRAY);
        empleadosTable.setShowGrid(true);
        empleadosTable.getTableHeader().setBackground(new Color(63, 81, 181));
        empleadosTable.getTableHeader().setForeground(Color.WHITE);
        empleadosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    /**
     * Configurar ventana principal
     */
    private void configureWindow() {
        setTitle("Sistema de Gestión de Empleados - Supermercado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 750);
        setMinimumSize(new Dimension(1200, 650));
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Color de fondo general
        getContentPane().setBackground(new Color(250, 250, 250));
        
        // Icono de la aplicación (opcional)
        try {
            setIconImage(createDefaultIcon());
        } catch (Exception e) {
            // Ignorar si no se puede crear el ícono
        }
    }
    
    /**
     * Crear ícono por defecto para la aplicación
     */
    private Image createDefaultIcon() {
        // Crear un ícono simple de 32x32
        Image img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setColor(new Color(63, 81, 181));
        g2d.fillRect(0, 0, 32, 32);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("SM", 8, 20);
        g2d.dispose();
        return img;
    }
    
    /**
     * Cargar empleado seleccionado en el formulario
     */
    private void cargarEmpleadoSeleccionado() {
        int selectedRow = empleadosTable.getSelectedRow();
        if (selectedRow >= 0) {
            idTextField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            nombreTextField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            cargoTextField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            
            // Limpiar formato de salario para edición
            String salarioStr = tableModel.getValueAt(selectedRow, 3).toString();
            salarioStr = salarioStr.replaceAll("[^0-9.]", ""); // Remover formato
            salarioTextField.setText(salarioStr);
            
            String departamento = tableModel.getValueAt(selectedRow, 4).toString();
            departamentoComboBox.setSelectedItem(departamento);
        }
    }
    
    // === MÉTODOS PÚBLICOS PARA EL CONTROLADOR ===
    
    /**
     * Actualizar lista de empleados en la tabla
     */
    public void actualizarTablaEmpleados(List<Empleado> empleados) {
        tableModel.setRowCount(0);
        
        for (Empleado empleado : empleados) {
            Object[] row = {
                empleado.getId(),
                empleado.getNombre(),
                empleado.getCargo(),
                empleado.getSalarioFormateado(),
                empleado.getDepartamento(),
                empleado.getFechaIngreso().toString()
            };
            tableModel.addRow(row);
        }
        
        contadorLabel.setText("Total empleados: " + empleados.size());
    }
    
    /**
     * Actualizar ComboBox de cargos
     */
    public void actualizarCargos(List<String> cargos) {
        cargosComboBox.removeAllItems();
        cargosComboBox.addItem("-- Seleccionar Cargo --");
        for (String cargo : cargos) {
            cargosComboBox.addItem(cargo);
        }
    }
    
    /**
     * Limpiar formulario
     */
    public void limpiarFormulario() {
        idTextField.setText("");
        nombreTextField.setText("");
        cargoTextField.setText("");
        salarioTextField.setText("");
        departamentoComboBox.setSelectedIndex(0);
        empleadosTable.clearSelection();
    }
    
    /**
     * Obtener empleado del formulario
     */
    public Empleado obtenerEmpleadoFormulario() throws NumberFormatException {
        Empleado empleado = new Empleado();
        
        if (!idTextField.getText().trim().isEmpty()) {
            empleado.setId(Integer.parseInt(idTextField.getText().trim()));
        }
        
        empleado.setNombre(nombreTextField.getText().trim());
        empleado.setCargo(cargoTextField.getText().trim());
        empleado.setSalario(new BigDecimal(salarioTextField.getText().trim()));
        empleado.setDepartamento(departamentoComboBox.getSelectedItem().toString());
        
        return empleado;
    }
    
    /**
     * Mostrar mensaje de éxito
     */
    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Mostrar mensaje de error
     */
    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Mostrar diálogo de confirmación
     */
    public boolean mostrarConfirmacion(String mensaje) {
        int result = JOptionPane.showConfirmDialog(
            this, mensaje, "Confirmacion", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Actualizar estadísticas
     */
    public void actualizarEstadisticas(String estadisticas) {
        estadisticasLabel.setText(estadisticas);
    }
    
    /**
     * Actualizar información del usuario autenticado
     */
    public void actualizarUsuarioAutenticado(String infoUsuario) {
        usuarioLabel.setText("Usuario: " + infoUsuario);
    }
    
    // === GETTERS PARA EVENTOS DEL CONTROLADOR ===
    
    public JButton getBuscarButton() { return buscarButton; }
    public JButton getMostrarTodosButton() { return mostrarTodosButton; }
    public JButton getLimpiarButton() { return limpiarButton; }
    public JButton getCrearButton() { return crearButton; }
    public JButton getActualizarButton() { return actualizarButton; }
    public JButton getEliminarButton() { return eliminarButton; }
    public JButton getNuevoButton() { return nuevoButton; }
    public JButton getLogoutButton() { return logoutButton; }
    
    public JComboBox<String> getCargosComboBox() { return cargosComboBox; }
    public JTextField getBusquedaTextField() { return busquedaTextField; }
    public JTable getEmpleadosTable() { return empleadosTable; }
}