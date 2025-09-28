# Script para ejecutar el Sistema Web de Empleados con Login
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host "    SISTEMA WEB DE EMPLEADOS CON LOGIN" -ForegroundColor Yellow
Write-Host "=============================================" -ForegroundColor Cyan
Write-Host ""

# Compilar el sistema
Write-Host "📦 Compilando sistema..." -ForegroundColor Green
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/model/*.java src/main/java/com/supermercado/dao/*.java src/main/java/com/supermercado/util/*.java src/main/java/com/supermercado/api/*.java src/main/java/com/supermercado/launcher/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Compilación exitosa" -ForegroundColor Green
} else {
    Write-Host "❌ Error en compilación" -ForegroundColor Red
    exit 1
}

# Copiar archivos web
Write-Host "📁 Copiando recursos web..." -ForegroundColor Green
$null = New-Item -ItemType Directory -Force -Path "target/classes/web"
Copy-Item "src/main/resources/web/*" "target/classes/web/" -Force

Write-Host "✅ Recursos copiados" -ForegroundColor Green
Write-Host ""

# Mostrar información del sistema
Write-Host "🌐 URLS DEL SISTEMA:" -ForegroundColor Yellow
Write-Host "   Login:     http://localhost:8084/login.html" -ForegroundColor White
Write-Host "   Empleados: http://localhost:8084/empleados.html" -ForegroundColor White
Write-Host "   API REST:  http://localhost:8084/api/empleados" -ForegroundColor White
Write-Host ""

Write-Host "👥 CREDENCIALES DE PRUEBA:" -ForegroundColor Yellow
Write-Host "   Usuario: admin    | Contraseña: admin123" -ForegroundColor White
Write-Host "   Usuario: empleado | Contraseña: emp123" -ForegroundColor White
Write-Host "   Usuario: cesar    | Contraseña: 123" -ForegroundColor White
Write-Host ""

Write-Host "🚀 Iniciando servidor web..." -ForegroundColor Green
Write-Host "   Presione Ctrl+C para detener el servidor" -ForegroundColor Gray
Write-Host ""

# Ejecutar el servidor
java -cp "target/classes" com.supermercado.launcher.EmpleadoWebLauncher