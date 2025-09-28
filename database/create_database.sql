-- Script para crear la base de datos y tabla de usuarios
-- Sistema de Gestión de Usuarios - MVC Pattern

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS user_management_db;
USE user_management_db;

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo
INSERT INTO users (name, email, phone, address) VALUES 
('Juan Pérez', 'juan.perez@email.com', '+57 300 123 4567', 'Calle 123 #45-67, Bogotá'),
('María García', 'maria.garcia@email.com', '+57 301 234 5678', 'Carrera 89 #12-34, Medellín'),
('Carlos López', 'carlos.lopez@email.com', '+57 302 345 6789', 'Avenida 56 #78-90, Cali'),
('Ana Rodríguez', 'ana.rodriguez@email.com', '+57 303 456 7890', 'Diagonal 12 #34-56, Barranquilla'),
('Luis Martínez', 'luis.martinez@email.com', '+57 304 567 8901', 'Transversal 78 #90-12, Cartagena');

-- Verificar la creación de la tabla y datos
SELECT * FROM users;
