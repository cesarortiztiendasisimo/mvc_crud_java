package com.supermercado.dao;

import com.supermercado.model.Empleado;
import java.util.List;

/**
 * Interface EmpleadoDAO
 * Define las operaciones CRUD para la gestión de empleados
 * Vista de Desarrollo - Separación de responsabilidades
 */
public interface EmpleadoDAO {
    
    /**
     * Crear un nuevo empleado
     * @param empleado Empleado a crear
     * @return true si se creó exitosamente
     */
    boolean crear(Empleado empleado);
    
    /**
     * Obtener empleado por ID
     * @param id ID del empleado
     * @return Empleado encontrado o null
     */
    Empleado obtenerPorId(int id);
    
    /**
     * Obtener todos los empleados
     * @return Lista de todos los empleados
     */
    List<Empleado> obtenerTodos();
    
    /**
     * Consultar empleados por cargo
     * @param cargo Cargo a buscar
     * @return Lista de empleados con el cargo especificado
     */
    List<Empleado> consultarPorCargo(String cargo);
    
    /**
     * Consultar empleados por departamento
     * @param departamento Departamento a buscar
     * @return Lista de empleados del departamento
     */
    List<Empleado> consultarPorDepartamento(String departamento);
    
    /**
     * Actualizar empleado existente
     * @param empleado Empleado con datos actualizados
     * @return true si se actualizó exitosamente
     */
    boolean actualizar(Empleado empleado);
    
    /**
     * Eliminar empleado (marca como inactivo)
     * @param id ID del empleado a eliminar
     * @return true si se eliminó exitosamente
     */
    boolean eliminar(int id);
    
    /**
     * Obtener lista de cargos únicos
     * @return Lista de cargos disponibles
     */
    List<String> obtenerCargosUnicos();
    
    /**
     * Obtener lista de departamentos únicos
     * @return Lista de departamentos disponibles
     */
    List<String> obtenerDepartamentosUnicos();
}