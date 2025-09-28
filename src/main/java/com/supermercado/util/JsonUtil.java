package com.supermercado.util;

import com.supermercado.model.Empleado;
import java.util.List;

/**
 * Utilidad simple para convertir objetos a JSON sin dependencias externas
 */
public class JsonUtil {
    
    /**
     * Convierte una lista de empleados a JSON
     */
    public static String toJson(List<Empleado> empleados) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < empleados.size(); i++) {
            if (i > 0) json.append(",");
            json.append(toJson(empleados.get(i)));
        }
        
        json.append("]");
        return json.toString();
    }
    
    /**
     * Convierte un empleado a JSON
     */
    public static String toJson(Empleado empleado) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"id\":").append(empleado.getId()).append(",");
        json.append("\"nombre\":\"").append(escapeJson(empleado.getNombre())).append("\",");
        json.append("\"cargo\":\"").append(escapeJson(empleado.getCargo())).append("\",");
        json.append("\"salario\":").append(empleado.getSalario()).append(",");
        json.append("\"telefono\":\"").append(escapeJson(empleado.getTelefono())).append("\",");
        json.append("\"email\":\"").append(escapeJson(empleado.getEmail())).append("\",");
        json.append("\"departamento\":\"").append(escapeJson(empleado.getDepartamento())).append("\",");
        json.append("\"fechaIngreso\":\"").append(empleado.getFechaIngreso()).append("\",");
        json.append("\"activo\":").append(empleado.isActivo());
        json.append("}");
        return json.toString();
    }
    
    /**
     * Convierte una lista de strings a JSON
     */
    public static String toJsonStringArray(List<String> strings) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        
        for (int i = 0; i < strings.size(); i++) {
            if (i > 0) json.append(",");
            json.append("\"").append(escapeJson(strings.get(i))).append("\"");
        }
        
        json.append("]");
        return json.toString();
    }
    
    /**
     * Parsea un JSON simple de empleado
     */
    public static Empleado fromJson(String json) {
        // Implementación básica de parsing JSON
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            
            Empleado empleado = new Empleado();
            String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim();
                    
                    // Remover comillas del valor si es string
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    switch (key) {
                        case "id":
                            if (!value.equals("null")) {
                                empleado.setId(Integer.parseInt(value));
                            }
                            break;
                        case "nombre":
                            empleado.setNombre(value);
                            break;
                        case "cargo":
                            empleado.setCargo(value);
                            break;
                        case "salario":
                            empleado.setSalario(new java.math.BigDecimal(value));
                            break;
                        case "telefono":
                            empleado.setTelefono(value);
                            break;
                        case "email":
                            empleado.setEmail(value);
                            break;
                        case "departamento":
                            empleado.setDepartamento(value);
                            break;
                        case "activo":
                            empleado.setActivo(Boolean.parseBoolean(value));
                            break;
                    }
                }
            }
            
            return empleado;
        }
        
        throw new IllegalArgumentException("Invalid JSON format");
    }
    
    /**
     * Escapa caracteres especiales para JSON
     */
    private static String escapeJson(String str) {
        if (str == null) return "";
        
        return str.replace("\\", "\\\\")
                 .replace("\"", "\\\"")
                 .replace("\b", "\\b")
                 .replace("\f", "\\f")
                 .replace("\n", "\\n")
                 .replace("\r", "\\r")
                 .replace("\t", "\\t");
    }
    
    /**
     * Crea un mensaje JSON simple
     */
    public static String createMessage(String message) {
        return "{\"message\":\"" + escapeJson(message) + "\"}";
    }
    
    /**
     * Crea un error JSON simple
     */
    public static String createError(String error) {
        return "{\"error\":\"" + escapeJson(error) + "\"}";
    }
}