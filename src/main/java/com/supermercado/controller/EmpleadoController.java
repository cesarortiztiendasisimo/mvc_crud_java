package com.supermercado.controller;

import com.supermercado.dao.EmpleadoDAO;
import com.supermercado.dao.EmpleadoDAOImpl;
import com.supermercado.model.Empleado;
import com.supermercado.view.EmpleadoView;
import com.mvc.service.AuthenticationService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador Principal del Sistema de Empleados
 * Arquitectura 4+1 - Vista de Procesos
 * Coordina la interacción entre Vista y Modelo
 */
public class EmpleadoController {
    
    private EmpleadoView vista;
    private EmpleadoDAO dao;
    private AuthenticationService authService;
    
    public EmpleadoController(EmpleadoView vista) {
        this.vista = vista;
        this.dao = new EmpleadoDAOImpl();
        this.authService = AuthenticationService.getInstance();
        
        inicializarEventos();
        cargarDatosIniciales();
        actualizarInfoUsuario();
    }
    
    /**
     * Configurar todos los event listeners
     * Vista de Procesos - Flujo de eventos
     */
    private void inicializarEventos() {
        
        // Eventos de búsqueda
        vista.getBuscarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarEmpleados();
            }
        });
        
        vista.getMostrarTodosButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTodosEmpleados();
            }
        });
        
        vista.getLimpiarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarBusqueda();
            }
        });
        
        // Eventos CRUD
        vista.getCrearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearEmpleado();
            }
        });
        
        vista.getActualizarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarEmpleado();
            }
        });
        
        vista.getEliminarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEmpleado();
            }
        });
        
        vista.getNuevoButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoEmpleado();
            }
        });
        
        // Evento de selección de cargo
        vista.getCargosComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarPorCargoSeleccionado();
            }
        });
        
        // Evento de Enter en búsqueda libre
        vista.getBusquedaTextField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarEmpleados();
            }
        });
        
        // Evento de logout
        vista.getLogoutButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
    }
    
    /**
     * Cargar datos iniciales en la vista
     */
    private void cargarDatosIniciales() {
        // Cargar todos los empleados
        mostrarTodosEmpleados();
        
        // Cargar lista de cargos únicos
        actualizarListaCargos();
        
        // Mostrar estadísticas generales
        mostrarEstadisticasGenerales();
    }
    
    /**
     * Buscar empleados según criterios de búsqueda
     * Caso de uso principal: Consultar empleados por cargo
     */
    private void buscarEmpleados() {
        try {
            String textoBusqueda = vista.getBusquedaTextField().getText().trim();
            List<Empleado> resultados;
            
            if (textoBusqueda.isEmpty()) {
                vista.mostrarMensajeError("Ingrese un termino de busqueda");
                return;
            }
            
            // Buscar por cargo (función principal)
            resultados = dao.consultarPorCargo(textoBusqueda);
            
            // Si no encuentra por cargo, buscar por nombre
            if (resultados.isEmpty()) {
                resultados = dao.obtenerTodos().stream()
                    .filter(e -> e.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase()))
                    .collect(Collectors.toList());
            }
            
            vista.actualizarTablaEmpleados(resultados);
            
            if (resultados.isEmpty()) {
                vista.mostrarMensajeError("No se encontraron empleados con el criterio: " + textoBusqueda);
                vista.actualizarEstadisticas("Sin resultados para: " + textoBusqueda);
            } else {
                vista.actualizarEstadisticas(String.format("Encontrados: %d empleados para '%s'", 
                                                         resultados.size(), textoBusqueda));
            }
            
        } catch (Exception e) {
            vista.mostrarMensajeError("Error en la busqueda: " + e.getMessage());
        }
    }
    
    /**
     * Mostrar todos los empleados activos
     */
    private void mostrarTodosEmpleados() {
        try {
            List<Empleado> empleados = dao.obtenerTodos();
            vista.actualizarTablaEmpleados(empleados);
            mostrarEstadisticasGenerales();
        } catch (Exception e) {
            vista.mostrarMensajeError("Error al cargar empleados: " + e.getMessage());
        }
    }
    
    /**
     * Filtrar por cargo seleccionado en ComboBox
     */
    private void filtrarPorCargoSeleccionado() {
        try {
            String cargoSeleccionado = (String) vista.getCargosComboBox().getSelectedItem();
            
            if (cargoSeleccionado == null || cargoSeleccionado.startsWith("--")) {
                return; // No hacer nada si no hay selección válida
            }
            
            List<Empleado> empleados = dao.consultarPorCargo(cargoSeleccionado);
            vista.actualizarTablaEmpleados(empleados);
            
            vista.actualizarEstadisticas(String.format("Cargo '%s': %d empleados", 
                                                     cargoSeleccionado, empleados.size()));
            
        } catch (Exception e) {
            vista.mostrarMensajeError("Error al filtrar por cargo: " + e.getMessage());
        }
    }
    
    /**
     * Crear nuevo empleado
     */
    private void crearEmpleado() {
        try {
            // Validar campos obligatorios
            if (!validarCamposFormulario()) {
                return;
            }
            
            Empleado empleado = vista.obtenerEmpleadoFormulario();
            empleado.setId(0); // ID será asignado automáticamente
            
            // Validaciones de negocio
            if (!validarEmpleado(empleado)) {
                return;
            }
            
            boolean exito = dao.crear(empleado);
            
            if (exito) {
                vista.mostrarMensajeExito("Empleado creado exitosamente");
                vista.limpiarFormulario();
                mostrarTodosEmpleados();
                actualizarListaCargos();
            } else {
                vista.mostrarMensajeError("Error al crear el empleado");
            }
            
        } catch (NumberFormatException e) {
            vista.mostrarMensajeError("El salario debe ser un numero valido");
        } catch (Exception e) {
            vista.mostrarMensajeError("Error al crear empleado: " + e.getMessage());
        }
    }
    
    /**
     * Actualizar empleado existente
     */
    private void actualizarEmpleado() {
        try {
            String idTexto = vista.obtenerEmpleadoFormulario().getId() + "";
            if (idTexto.trim().isEmpty() || "0".equals(idTexto)) {
                vista.mostrarMensajeError("Seleccione un empleado de la tabla para actualizar");
                return;
            }
            
            if (!validarCamposFormulario()) {
                return;
            }
            
            Empleado empleado = vista.obtenerEmpleadoFormulario();
            
            if (!validarEmpleado(empleado)) {
                return;
            }
            
            boolean exito = dao.actualizar(empleado);
            
            if (exito) {
                vista.mostrarMensajeExito("Empleado actualizado exitosamente");
                mostrarTodosEmpleados();
                actualizarListaCargos();
            } else {
                vista.mostrarMensajeError("Error al actualizar el empleado");
            }
            
        } catch (NumberFormatException e) {
            vista.mostrarMensajeError("El salario debe ser un numero valido");
        } catch (Exception e) {
            vista.mostrarMensajeError("Error al actualizar empleado: " + e.getMessage());
        }
    }
    
    /**
     * Eliminar empleado (marcar como inactivo)
     */
    private void eliminarEmpleado() {
        try {
            Empleado empleado = vista.obtenerEmpleadoFormulario();
            
            if (empleado.getId() == 0) {
                vista.mostrarMensajeError("Seleccione un empleado de la tabla para eliminar");
                return;
            }
            
            boolean confirmar = vista.mostrarConfirmacion(
                String.format("Esta seguro de eliminar al empleado '%s'?\n" +
                            "Esta accion marcara al empleado como inactivo.", 
                            empleado.getNombre())
            );
            
            if (confirmar) {
                boolean exito = dao.eliminar(empleado.getId());
                
                if (exito) {
                    vista.mostrarMensajeExito("Empleado eliminado exitosamente");
                    vista.limpiarFormulario();
                    mostrarTodosEmpleados();
                    actualizarListaCargos();
                } else {
                    vista.mostrarMensajeError("Error al eliminar el empleado");
                }
            }
            
        } catch (Exception e) {
            vista.mostrarMensajeError("Error al eliminar empleado: " + e.getMessage());
        }
    }
    
    /**
     * Preparar formulario para nuevo empleado
     */
    private void nuevoEmpleado() {
        vista.limpiarFormulario();
        vista.mostrarMensajeExito("Formulario listo para nuevo empleado");
    }
    
    /**
     * Limpiar campos de búsqueda y mostrar todos
     */
    private void limpiarBusqueda() {
        vista.getBusquedaTextField().setText("");
        vista.getCargosComboBox().setSelectedIndex(0);
        mostrarTodosEmpleados();
    }
    
    /**
     * Actualizar lista de cargos únicos en ComboBox
     */
    private void actualizarListaCargos() {
        try {
            List<String> cargos = dao.obtenerCargosUnicos();
            vista.actualizarCargos(cargos);
        } catch (Exception e) {
            System.err.println("Error al actualizar lista de cargos: " + e.getMessage());
        }
    }
    
    /**
     * Mostrar estadísticas generales del sistema
     */
    private void mostrarEstadisticasGenerales() {
        try {
            if (dao instanceof EmpleadoDAOImpl) {
                EmpleadoDAOImpl daoImpl = (EmpleadoDAOImpl) dao;
                Map<String, Long> estadisticas = daoImpl.obtenerEstadisticasPorCargo();
                
                StringBuilder stats = new StringBuilder("Estadisticas: ");
                estadisticas.entrySet().stream()
                    .limit(3) // Mostrar solo los 3 primeros
                    .forEach(entry -> stats.append(String.format("%s(%d) ", entry.getKey(), entry.getValue())));
                
                vista.actualizarEstadisticas(stats.toString());
            }
        } catch (Exception e) {
            vista.actualizarEstadisticas("Estadisticas no disponibles");
        }
    }
    
    /**
     * Validar campos obligatorios del formulario
     */
    private boolean validarCamposFormulario() {
        try {
            Empleado empleado = vista.obtenerEmpleadoFormulario();
            
            if (empleado.getNombre().trim().isEmpty()) {
                vista.mostrarMensajeError("El nombre es obligatorio");
                return false;
            }
            
            if (empleado.getCargo().trim().isEmpty()) {
                vista.mostrarMensajeError("El cargo es obligatorio");
                return false;
            }
            
            if (empleado.getSalario().compareTo(BigDecimal.ZERO) <= 0) {
                vista.mostrarMensajeError("El salario debe ser mayor a 0");
                return false;
            }
            
            return true;
            
        } catch (NumberFormatException e) {
            vista.mostrarMensajeError("El salario debe ser un numero valido");
            return false;
        } catch (Exception e) {
            vista.mostrarMensajeError("Error en validacion: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Validaciones de negocio específicas
     */
    private boolean validarEmpleado(Empleado empleado) {
        // Validar longitud del nombre
        if (empleado.getNombre().length() < 3) {
            vista.mostrarMensajeError("El nombre debe tener al menos 3 caracteres");
            return false;
        }
        
        if (empleado.getNombre().length() > 100) {
            vista.mostrarMensajeError("El nombre no puede exceder 100 caracteres");
            return false;
        }
        
        // Validar formato del nombre (solo letras y espacios)
        if (!empleado.getNombre().matches("^[a-zA-Z\\s]+$")) {
            vista.mostrarMensajeError("El nombre solo puede contener letras y espacios");
            return false;
        }
        
        // Validar salario mínimo y máximo
        BigDecimal salarioMinimo = new BigDecimal("800000");  // Salario mínimo Colombia aprox
        BigDecimal salarioMaximo = new BigDecimal("10000000"); // Límite razonable
        
        if (empleado.getSalario().compareTo(salarioMinimo) < 0) {
            vista.mostrarMensajeError("El salario no puede ser menor a $800,000");
            return false;
        }
        
        if (empleado.getSalario().compareTo(salarioMaximo) > 0) {
            vista.mostrarMensajeError("El salario no puede ser mayor a $10,000,000");
            return false;
        }
        
        return true;
    }
    
    /**
     * Actualizar información del usuario autenticado en la vista
     */
    private void actualizarInfoUsuario() {
        if (authService.isAutenticado()) {
            vista.actualizarUsuarioAutenticado(authService.getInfoUsuarioAutenticado());
        } else {
            vista.actualizarUsuarioAutenticado("No autenticado");
        }
    }
    
    /**
     * Cerrar sesión y regresar al login
     */
    private void cerrarSesion() {
        boolean confirmar = vista.mostrarConfirmacion(
            "¿Está seguro de que desea cerrar la sesión?\n" +
            "Se cerrará el sistema de empleados."
        );
        
        if (confirmar) {
            authService.logout();
            vista.dispose();
            System.exit(0);
        }
    }
}