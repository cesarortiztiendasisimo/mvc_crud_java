# Script para compilar y ejecutar solo el sistema web de empleados
Write-Host "=== COMPILANDO SISTEMA WEB DE EMPLEADOS ===" -ForegroundColor Green

# Crear directorio target si no existe
if (-not (Test-Path "target/classes")) {
    New-Item -ItemType Directory -Path "target/classes" -Force | Out-Null
}

# Compilar todos los componentes necesarios
Write-Host "Compilando componentes base..." -ForegroundColor Yellow
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/model/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/dao/*.java

Write-Host "Compilando utilidades y API..." -ForegroundColor Yellow
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/util/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/api/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/launcher/*.java

Write-Host "Compilación completada!" -ForegroundColor Green
Write-Host ""

# Copiar recursos web si es necesario
if (Test-Path "src/main/resources/web") {
    Write-Host "Copiando recursos web..." -ForegroundColor Yellow
    if (-not (Test-Path "target/classes/web")) {
        New-Item -ItemType Directory -Path "target/classes/web" -Force | Out-Null
    }
    Copy-Item "src/main/resources/web/*" "target/classes/web/" -Recurse -Force
    Write-Host "Recursos web copiados." -ForegroundColor Green
}

Write-Host "=== INICIANDO SISTEMA WEB DE EMPLEADOS ===" -ForegroundColor Cyan
Write-Host "🌐 Servidor web iniciándose en puerto 8081..." -ForegroundColor White
Write-Host "📋 Interfaz web disponible en: http://localhost:8081" -ForegroundColor Gray
Write-Host "🔗 Se abrirá automáticamente en su navegador predeterminado" -ForegroundColor Gray
Write-Host ""
Write-Host "Presione Ctrl+C para detener el servidor" -ForegroundColor Yellow
Write-Host ""

# Ejecutar el sistema web
java -cp "target/classes" com.supermercado.launcher.EmpleadoWebLauncher