package com.supermercado.dao;

import com.supermercado.model.Empleado;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del DAO de Empleados
 * Vista de Desarrollo - Implementación concreta del acceso a datos
 */
public class EmpleadoDAOImpl implements EmpleadoDAO {
    
    private List<Empleado> empleados;
    private int siguienteId;
    
    public EmpleadoDAOImpl() {
        this.empleados = new ArrayList<>();
        this.siguienteId = 1;
        cargarDatosPrueba();
    }
    
    /**
     * Cargar datos de prueba según especificación
     * Al menos 5 empleados precargados
     */
    private void cargarDatosPrueba() {
        // Cajeros
        Empleado emp1 = new Empleado("Ana María López", "Cajero", new BigDecimal("1200000"), 
                                   "+57 300 123 4567", "ana.lopez@supermercado.com");
        emp1.setId(siguienteId++);
        emp1.setDepartamento("Ventas");
        empleados.add(emp1);
        
        Empleado emp2 = new Empleado("Carlos Mendoza", "Cajero", new BigDecimal("1250000"), 
                                   "+57 301 234 5678", "carlos.mendoza@supermercado.com");
        emp2.setId(siguienteId++);
        emp2.setDepartamento("Ventas");
        empleados.add(emp2);
        
        // Supervisores
        Empleado emp3 = new Empleado("María Fernanda García", "Supervisor", new BigDecimal("1800000"), 
                                   "+57 302 345 6789", "maria.garcia@supermercado.com");
        emp3.setId(siguienteId++);
        emp3.setDepartamento("Ventas");
        empleados.add(emp3);
        
        Empleado emp4 = new Empleado("Jorge Luis Ramírez", "Supervisor", new BigDecimal("1750000"), 
                                   "+57 303 456 7890", "jorge.ramirez@supermercado.com");
        emp4.setId(siguienteId++);
        emp4.setDepartamento("Logística");
        empleados.add(emp4);
        
        // Gerentes
        Empleado emp5 = new Empleado("Roberto Martínez", "Gerente", new BigDecimal("3500000"), 
                                   "+57 304 567 8901", "roberto.martinez@supermercado.com");
        emp5.setId(siguienteId++);
        emp5.setDepartamento("Administración");
        empleados.add(emp5);
        
        // Personal de bodega
        Empleado emp6 = new Empleado("Pedro Sánchez", "Almacenista", new BigDecimal("1100000"), 
                                   "+57 305 678 9012", "pedro.sanchez@supermercado.com");
        emp6.setId(siguienteId++);
        emp6.setDepartamento("Logística");
        empleados.add(emp6);
        
        Empleado emp7 = new Empleado("Carmen Torres", "Almacenista", new BigDecimal("1150000"), 
                                   "+57 306 789 0123", "carmen.torres@supermercado.com");
        emp7.setId(siguienteId++);
        emp7.setDepartamento("Logística");
        empleados.add(emp7);
        
        // Personal de limpieza
        Empleado emp8 = new Empleado("Luis Morales", "Limpieza", new BigDecimal("950000"), 
                                   "+57 307 890 1234", "luis.morales@supermercado.com");
        emp8.setId(siguienteId++);
        emp8.setDepartamento("Mantenimiento");
        empleados.add(emp8);
        
        // Personal de seguridad
        Empleado emp9 = new Empleado("Andrea Díaz", "Seguridad", new BigDecimal("1300000"), 
                                   "+57 308 901 2345", "andrea.diaz@supermercado.com");
        emp9.setId(siguienteId++);
        emp9.setDepartamento("Seguridad");
        empleados.add(emp9);
        
        // Vendedores
        Empleado emp10 = new Empleado("Miguel Hernández", "Vendedor", new BigDecimal("1400000"), 
                                    "+57 309 012 3456", "miguel.hernandez@supermercado.com");
        emp10.setId(siguienteId++);
        emp10.setDepartamento("Ventas");
        empleados.add(emp10);
        
        System.out.println("✅ Cargados " + empleados.size() + " empleados de prueba");
    }
    
    @Override
    public boolean crear(Empleado empleado) {
        if (empleado == null) return false;
        
        // Validar que no exista el ID
        if (empleado.getId() == 0) {
            empleado.setId(siguienteId++);
        } else if (obtenerPorId(empleado.getId()) != null) {
            return false; // ID ya existe
        }
        
        return empleados.add(empleado);
    }
    
    @Override
    public Empleado obtenerPorId(int id) {
        return empleados.stream()
                .filter(e -> e.getId() == id && e.isActivo())
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Empleado> obtenerTodos() {
        return empleados.stream()
                .filter(Empleado::isActivo)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Empleado> consultarPorCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return empleados.stream()
                .filter(e -> e.isActivo())
                .filter(e -> e.getCargo().toLowerCase().contains(cargo.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Empleado> consultarPorDepartamento(String departamento) {
        if (departamento == null || departamento.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return empleados.stream()
                .filter(e -> e.isActivo())
                .filter(e -> e.getDepartamento().toLowerCase().contains(departamento.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean actualizar(Empleado empleado) {
        if (empleado == null) return false;
        
        Empleado existente = obtenerPorId(empleado.getId());
        if (existente == null) return false;
        
        // Actualizar datos
        existente.setNombre(empleado.getNombre());
        existente.setCargo(empleado.getCargo());
        existente.setSalario(empleado.getSalario());
        existente.setDepartamento(empleado.getDepartamento());
        existente.setFechaIngreso(empleado.getFechaIngreso());
        
        return true;
    }
    
    @Override
    public boolean eliminar(int id) {
        Empleado empleado = obtenerPorId(id);
        if (empleado == null) return false;
        
        empleado.setActivo(false);
        return true;
    }
    
    @Override
    public List<String> obtenerCargosUnicos() {
        return empleados.stream()
                .filter(Empleado::isActivo)
                .map(Empleado::getCargo)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> obtenerDepartamentosUnicos() {
        return empleados.stream()
                .filter(Empleado::isActivo)
                .map(Empleado::getDepartamento)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener estadísticas de empleados por cargo
     * @return Mapa con cargo y cantidad de empleados
     */
    public Map<String, Long> obtenerEstadisticasPorCargo() {
        return empleados.stream()
                .filter(Empleado::isActivo)
                .collect(Collectors.groupingBy(
                    Empleado::getCargo,
                    Collectors.counting()
                ));
    }
}