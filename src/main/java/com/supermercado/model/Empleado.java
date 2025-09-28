package com.supermercado.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Clase Empleado - Entidad del dominio
 * Representa un empleado del supermercado con sus atributos principales
 * Seguimiento arquitectura 4+1 - Vista Lógica
 */
public class Empleado {
    private int id;
    private String nombre;
    private String cargo;
    private BigDecimal salario;
    private LocalDate fechaIngreso;
    private String departamento;
    private String telefono;
    private String email;
    private boolean activo;
    
    // Constructor por defecto
    public Empleado() {
        this.activo = true;
        this.fechaIngreso = LocalDate.now();
    }
    
    // Constructor completo
    public Empleado(int id, String nombre, String cargo, BigDecimal salario, String departamento) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
        this.departamento = departamento;
        this.fechaIngreso = LocalDate.now();
        this.activo = true;
    }
    
    // Constructor para sistema web
    public Empleado(String nombre, String cargo, BigDecimal salario, String telefono, String email) {
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
        this.telefono = telefono;
        this.email = email;
        this.departamento = "General"; // Default
        this.fechaIngreso = LocalDate.now();
        this.activo = true;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    // Métodos utilitarios
    public String getSalarioFormateado() {
        return String.format("$%,.2f", salario.doubleValue());
    }
    
    @Override
    public String toString() {
        return String.format("Empleado{id=%d, nombre='%s', cargo='%s', salario=%s, departamento='%s'}", 
                           id, nombre, cargo, getSalarioFormateado(), departamento);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Empleado empleado = (Empleado) obj;
        return id == empleado.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}