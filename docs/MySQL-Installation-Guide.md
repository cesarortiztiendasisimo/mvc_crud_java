# Guía de Instalación de MySQL para el Proyecto MVC

## 📋 **Requisitos Previos**
- Windows 10/11
- Java 11 o superior
- Proyecto MVC User Management configurado

## 🚀 **Opción 1: MySQL Community Server (Completo)**

### 1. Descargar MySQL
1. Visita: https://dev.mysql.com/downloads/mysql/
2. Descarga "MySQL Community Server" para Windows
3. Elige la versión "Windows (x86, 64-bit), MSI Installer"

### 2. Instalar MySQL
1. Ejecuta el instalador como administrador
2. Selecciona "Developer Default" o "Custom"
3. En configuración:
   - **Root Password**: Deja vacío o usa "root"
   - **Puerto**: 3306 (default)
   - **Authentication**: Use Strong Password Encryption

### 3. Configurar Base de Datos
```sql
-- Abrir MySQL Command Line Client o MySQL Workbench
CREATE DATABASE IF NOT EXISTS user_management_db;
USE user_management_db;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertar datos de prueba
INSERT INTO users (name, email, phone, address) VALUES 
('Juan Pérez', 'juan.perez@email.com', '3001234567', 'Calle 123 #45-67, Bogotá'),
('María García', 'maria.garcia@email.com', '3007654321', 'Carrera 45 #23-12, Medellín'),
('Carlos López', 'carlos.lopez@email.com', '3009876543', 'Avenida 68 #34-56, Cali');
```

## 🚀 **Opción 2: XAMPP (Más Fácil)**

### 1. Descargar XAMPP
1. Visita: https://www.apachefriends.org/download.html
2. Descarga XAMPP para Windows
3. Instala seleccionando Apache, MySQL y phpMyAdmin

### 2. Iniciar Servicios
1. Abre XAMPP Control Panel
2. Start "Apache" y "MySQL"
3. Click en "Admin" junto a MySQL para abrir phpMyAdmin

### 3. Crear Base de Datos en phpMyAdmin
1. Abre http://localhost/phpmyadmin
2. Click "Nueva" base de datos
3. Nombre: `user_management_db`
4. Collation: `utf8_general_ci`
5. Ejecuta el script SQL del paso anterior

## 📦 **Descargar Driver MySQL**

### Opción Manual
1. Descarga desde: https://dev.mysql.com/downloads/connector/j/
2. Extrae `mysql-connector-java-8.x.x.jar`
3. Copia a: `C:\Proyectos\mvc_crud_java\libs\`

### Agregar Driver al Classpath
Modifica los scripts de compilación:
```powershell
javac -encoding UTF-8 -cp "src/main/java;libs/*" -d "target/classes" ...
java -cp "target/classes;libs/*" com.mvc.Main
```

## ✅ **Verificar Instalación**

### Opción 1: Desde Command Line
```cmd
mysql -u root -p
SHOW DATABASES;
USE user_management_db;
SHOW TABLES;
```

### Opción 2: Usando la Aplicación
1. Ejecuta: `java -cp "target/classes;libs/*" com.mvc.Main`
2. Si conecta exitosamente, verás: "Conexión a base de datos establecida"

## 🔧 **Configuración en DatabaseConfig.java**

Si usas credenciales diferentes:
```java
private static final String URL = "jdbc:mysql://localhost:3306/user_management_db";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_password";
```

## 🚨 **Troubleshooting**

### Error: "Access denied for user 'root'@'localhost'"
```sql
-- Resetear password de root
ALTER USER 'root'@'localhost' IDENTIFIED BY '';
FLUSH PRIVILEGES;
```

### Error: "Public Key Retrieval is not allowed"
Cambiar URL a:
```java
private static final String URL = "jdbc:mysql://localhost:3306/user_management_db?allowPublicKeyRetrieval=true&useSSL=false";
```

### Error: "Connection refused"
1. Verificar que MySQL esté ejecutándose
2. Verificar puerto 3306 disponible: `netstat -an | findstr 3306`

## 💡 **Notas Importantes**

- **Sin MySQL**: La aplicación funciona perfectamente con datos en memoria
- **Con MySQL**: Los datos se guardan permanentemente
- **Modo Híbrido**: Puedes cambiar entre modos fácilmente

## 🎯 **Recomendación**

Para desarrollo y pruebas, el **modo Mock** es suficiente. 
Para producción o datos persistentes, instala MySQL siguiendo esta guía.
