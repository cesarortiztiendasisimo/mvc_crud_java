# Script para lanzar API y aplicación de escritorio simultáneamente
# Autor: Sistema MVC User Management
# Fecha: $(Get-Date)

Write-Host "=== Lanzador MVC User Management ===" -ForegroundColor Green
Write-Host "Iniciando API y aplicación de escritorio..." -ForegroundColor Yellow

# Cambiar al directorio del proyecto
Set-Location -Path $PSScriptRoot

# Compilar si es necesario
Write-Host "Compilando proyecto..." -ForegroundColor Blue
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" `
    "src/main/java/com/mvc/model/User.java" `
    "src/main/java/com/mvc/dao/UserDAO.java" `
    "src/main/java/com/mvc/dao/UserDAOImpl.java" `
    "src/main/java/com/mvc/dao/UserDAOMock.java" `
    "src/main/java/com/mvc/config/DatabaseConfig.java" `
    "src/main/java/com/mvc/service/AuthenticationService.java" `
    "src/main/java/com/mvc/view/UserView.java" `
    "src/main/java/com/mvc/controller/UserController.java" `
    "src/main/java/com/mvc/Main.java" `
    "src/main/java/com/mvc/MainWithFallback.java" `
    "src/main/java/com/mvc/api/FullApiServer.java" `
    "src/main/java/com/mvc/api/swagger/SwaggerHandler.java" `
    "src/main/java/com/supermercado/model/Empleado.java" `
    "src/main/java/com/supermercado/dao/EmpleadoDAO.java" `
    "src/main/java/com/supermercado/dao/EmpleadoDAOImpl.java" `
    "src/main/java/com/supermercado/view/LoginView.java" `
    "src/main/java/com/supermercado/view/EmpleadoView.java" `
    "src/main/java/com/supermercado/controller/LoginController.java" `
    "src/main/java/com/supermercado/controller/EmpleadoController.java" `
    "src/main/java/com/supermercado/SupermercadoApp.java"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilación exitosa!" -ForegroundColor Green
    
    Write-Host "`n=== SELECCIONA QUE APLICACION EJECUTAR ===" -ForegroundColor Magenta
    Write-Host "1. Sistema de Usuarios (Original MVC + API)" -ForegroundColor White
    Write-Host "2. Sistema de Empleados de Supermercado (Nuevo MVC)" -ForegroundColor White
    Write-Host "3. Ambas aplicaciones" -ForegroundColor White
    
    $opcion = Read-Host "Selecciona una opción (1-3)"
    
    switch ($opcion) {
        "1" {
            Write-Host "`nIniciando Sistema de Usuarios..." -ForegroundColor Cyan
            # Lanzar API en proceso en segundo plano
            Start-Process -FilePath "java" -ArgumentList "-cp", "target/classes", "com.mvc.api.FullApiServer" -WindowStyle Minimized
            Start-Sleep -Seconds 2
            # Lanzar aplicación de escritorio
            java -cp "target/classes" com.mvc.MainWithFallback
        }
        "2" {
            Write-Host "`nIniciando Sistema de Empleados de Supermercado..." -ForegroundColor Cyan
            java -cp "target/classes" com.supermercado.SupermercadoApp
        }
        "3" {
            Write-Host "`nIniciando ambas aplicaciones..." -ForegroundColor Cyan
            # Lanzar API
            Start-Process -FilePath "java" -ArgumentList "-cp", "target/classes", "com.mvc.api.FullApiServer" -WindowStyle Minimized
            Start-Sleep -Seconds 1
            # Lanzar Sistema de Usuarios
            Start-Process -FilePath "java" -ArgumentList "-cp", "target/classes", "com.mvc.MainWithFallback"
            Start-Sleep -Seconds 1
            # Lanzar Sistema de Supermercado
            java -cp "target/classes" com.supermercado.SupermercadoApp
        }
        default {
            Write-Host "Opción no válida. Ejecutando Sistema de Supermercado por defecto..." -ForegroundColor Yellow
            java -cp "target/classes" com.supermercado.SupermercadoApp
        }
    }
    
    Write-Host "`n=== URLs DISPONIBLES ===" -ForegroundColor Yellow
    Write-Host "API Swagger UI: http://localhost:9090/swagger" -ForegroundColor White
    Write-Host "API Health Check: http://localhost:9090/api/health" -ForegroundColor White
    Write-Host "API Users: http://localhost:9090/api/users" -ForegroundColor White
    
} else {
    Write-Host "Error en la compilación. Revisa los errores anteriores." -ForegroundColor Red
    Read-Host "Presiona Enter para continuar"
}
