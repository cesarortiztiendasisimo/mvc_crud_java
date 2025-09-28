# Script para compilar y ejecutar el Sistema de Empleados de Supermercado
# Arquitectura MVC + Modelo 4+1 de Kruchten

Write-Host "=== SISTEMA DE EMPLEADOS DE SUPERMERCADO ===" -ForegroundColor Green
Write-Host "Patron MVC + Arquitectura 4+1" -ForegroundColor Yellow

# Cambiar al directorio del proyecto
Set-Location -Path $PSScriptRoot

# Crear directorios si no existen
Write-Host "Creando estructura de directorios..." -ForegroundColor Blue
New-Item -ItemType Directory -Force -Path "target/classes" | Out-Null

# Compilar todos los archivos Java
Write-Host "Compilando aplicacion..." -ForegroundColor Blue
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" `
    "src/main/java/com/mvc/model/User.java" `
    "src/main/java/com/mvc/dao/UserDAO.java" `
    "src/main/java/com/mvc/dao/UserDAOMock.java" `
    "src/main/java/com/mvc/service/AuthenticationService.java" `
    "src/main/java/com/supermercado/model/Empleado.java" `
    "src/main/java/com/supermercado/dao/EmpleadoDAO.java" `
    "src/main/java/com/supermercado/dao/EmpleadoDAOImpl.java" `
    "src/main/java/com/supermercado/view/LoginView.java" `
    "src/main/java/com/supermercado/view/EmpleadoView.java" `
    "src/main/java/com/supermercado/controller/LoginController.java" `
    "src/main/java/com/supermercado/controller/EmpleadoController.java" `
    "src/main/java/com/supermercado/SupermercadoApp.java"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilacion exitosa!" -ForegroundColor Green
    
    Write-Host "`n=== CARACTERISTICAS DE LA APLICACION ===" -ForegroundColor Cyan
    Write-Host "Sistema de Gestion de Empleados de Supermercado" -ForegroundColor White
    Write-Host "Arquitectura: MVC + Modelo 4+1 de Kruchten" -ForegroundColor White
    Write-Host "AUTENTICACION INTEGRADA: Requiere usuario del sistema principal" -ForegroundColor Yellow
    Write-Host "10 empleados precargados para pruebas" -ForegroundColor White
    Write-Host "Funcion principal: Consultar empleados por cargo" -ForegroundColor White
    Write-Host "CRUD completo implementado" -ForegroundColor White
    
    Write-Host "`n=== USUARIOS DE PRUEBA DISPONIBLES ===" -ForegroundColor Magenta
    Write-Host "Email: juan.perez@email.com | Nombre: Juan Perez" -ForegroundColor White
    Write-Host "Email: maria.garcia@email.com | Nombre: Maria Garcia" -ForegroundColor White  
    Write-Host "Email: carlos.lopez@email.com | Nombre: Carlos Lopez" -ForegroundColor White
    
    Write-Host "`nIniciando aplicacion con login..." -ForegroundColor Cyan
    
    # Ejecutar la aplicación
    java -cp "target/classes" com.supermercado.SupermercadoApp
    
} else {
    Write-Host "Error en la compilacion. Revisa los errores anteriores." -ForegroundColor Red
    Read-Host "Presiona Enter para continuar"
}