@echo off
echo =============================================
echo     SISTEMA WEB CON DESCARGA DESKTOP
echo =============================================
echo.

echo 🔧 Compilando sistema...
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/model/*.java src/main/java/com/supermercado/dao/*.java src/main/java/com/supermercado/util/*.java src/main/java/com/supermercado/api/*.java src/main/java/com/supermercado/launcher/*.java src/main/java/com/mvc/model/*.java src/main/java/com/mvc/dao/*.java

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error en compilación
    pause
    exit /b 1
)

echo ✅ Compilación exitosa

echo 📁 Copiando recursos web...
copy "src\main\resources\web\*.html" "target\classes\web\" >nul
copy "src\main\resources\web\*.css" "target\classes\web\" >nul
copy "src\main\resources\web\*.js" "target\classes\web\" >nul

echo ✅ Recursos copiados

echo.
echo 🌐 URLs DISPONIBLES:
echo    Login:     http://localhost:8084/login.html
echo    Empleados: http://localhost:8084/empleados.html
echo    Descargas: http://localhost:8084/api/desktop/download
echo.

echo 🚀 Iniciando servidor...
echo    Presione Ctrl+C para detener
echo.

java -cp "target/classes" com.supermercado.launcher.EmpleadoWebLauncher

echo.
echo 🔴 Servidor detenido
pause