@echo off
REM Script para lanzar API y aplicación de escritorio simultáneamente
REM Autor: Sistema MVC User Management

echo === Lanzador MVC User Management ===
echo Iniciando API y aplicación de escritorio...

REM Cambiar al directorio del script
cd /d "%~dp0"

REM Compilar proyecto
echo Compilando proyecto...
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" ^
    "src/main/java/com/mvc/model/User.java" ^
    "src/main/java/com/mvc/dao/UserDAO.java" ^
    "src/main/java/com/mvc/dao/UserDAOImpl.java" ^
    "src/main/java/com/mvc/dao/UserDAOMock.java" ^
    "src/main/java/com/mvc/config/DatabaseConfig.java" ^
    "src/main/java/com/mvc/view/UserView.java" ^
    "src/main/java/com/mvc/controller/UserController.java" ^
    "src/main/java/com/mvc/Main.java" ^
    "src/main/java/com/mvc/api/FullApiServer.java" ^
    "src/main/java/com/mvc/api/swagger/SwaggerHandler.java"

if %errorlevel% equ 0 (
    echo Compilación exitosa!
    
    REM Lanzar API en segundo plano
    echo Iniciando API Server en puerto 9090...
    start "API Server" /min java -cp "target/classes" com.mvc.api.FullApiServer
    
    REM Esperar para que la API se inicie
    timeout /t 3 /nobreak >nul
    
    REM Lanzar aplicación de escritorio
    echo Iniciando aplicación de escritorio...
    java -cp "target/classes" com.mvc.Main
    
    echo.
    echo === URLs de la API ===
    echo Swagger UI: http://localhost:9090/swagger
    echo API Docs: http://localhost:9090/swagger/api-docs
    echo Health Check: http://localhost:9090/api/health
    
) else (
    echo Error en la compilación. Revisa los errores anteriores.
    pause
)
