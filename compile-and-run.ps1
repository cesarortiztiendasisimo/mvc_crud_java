# Script para compilar y ejecutar el sistema completo
Write-Host "=== COMPILANDO SISTEMA COMPLETO ===" -ForegroundColor Green

# Compilar todos los componentes MVC de usuarios
Write-Host "Compilando sistema de usuarios..." -ForegroundColor Yellow
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/controller/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/dao/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/model/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/service/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/view/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/config/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/MainWithFallback.java

# Compilar sistema de empleados
Write-Host "Compilando sistema de empleados..." -ForegroundColor Yellow
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/model/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/dao/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/controller/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/view/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/util/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/api/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/launcher/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/SupermercadoApp.java

Write-Host "Compilación completada!" -ForegroundColor Green
Write-Host ""
Write-Host "=== INICIANDO SISTEMA UNIFICADO ===" -ForegroundColor Cyan
Write-Host "Sistema de autenticación con selección de plataforma disponible." -ForegroundColor White
Write-Host "- Sistema de Empleados: Gestión integrada con interface Material Design" -ForegroundColor Gray
Write-Host "- Sistema de Usuarios: Proceso independiente con datos mock" -ForegroundColor Gray
Write-Host "- Sistema Web de Empleados: Interfaz web en http://localhost:8081" -ForegroundColor Gray
Write-Host ""

# Ejecutar el sistema principal
java -cp "target/classes" com.supermercado.SupermercadoApp