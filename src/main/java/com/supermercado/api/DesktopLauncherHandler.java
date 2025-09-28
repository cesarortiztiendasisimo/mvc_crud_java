package com.supermercado.api;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.jar.*;
import java.util.zip.ZipEntry;

/**
 * Handler para descargar la aplicación de escritorio
 */
public class DesktopLauncherHandler implements HttpHandler {
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        System.out.println("DESKTOP: Solicitud de descarga desktop: " + method + " " + path);
        
        // Habilitar CORS
        Headers headers = exchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET,OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        
        if ("OPTIONS".equals(method)) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }
        
        if ("GET".equals(method)) {
            if (path.equals("/api/desktop/download")) {
                handleDownloadJar(exchange);
            } else if (path.equals("/api/desktop/launcher")) {
                handleLauncherScript(exchange);
            } else {
                sendError(exchange, 404, "Ruta no encontrada");
            }
        } else {
            sendError(exchange, 405, "Método no soportado");
        }
    }
    
    private void handleDownloadJar(HttpExchange exchange) throws IOException {
        try {
            // Crear JAR ejecutable en memoria
            ByteArrayOutputStream jarBytes = createExecutableJar();
            
            // Configurar headers para descarga
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "application/java-archive");
            headers.add("Content-Disposition", "attachment; filename=\"SupermercadoMVC-Desktop.jar\"");
            headers.add("Content-Length", String.valueOf(jarBytes.size()));
            
            // Enviar JAR
            exchange.sendResponseHeaders(200, jarBytes.size());
            
            try (OutputStream os = exchange.getResponseBody()) {
                jarBytes.writeTo(os);
            }
            
            System.out.println("OK JAR ejecutable enviado (" + jarBytes.size() + " bytes)");
            
        } catch (Exception e) {
            System.err.println("ERROR: Error creando JAR: " + e.getMessage());
            e.printStackTrace();
            sendError(exchange, 500, "Error creando JAR ejecutable");
        }
    }
    
    private void handleLauncherScript(HttpExchange exchange) throws IOException {
        try {
            String script = createLauncherScript();
            
            // Configurar headers
            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "application/octet-stream");
            headers.add("Content-Disposition", "attachment; filename=\"launcher.bat\"");
            
            // Enviar script
            byte[] scriptBytes = script.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, scriptBytes.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(scriptBytes);
            }
            
            System.out.println("OK Script launcher enviado");
            
        } catch (Exception e) {
            System.err.println("ERROR: Error creando script: " + e.getMessage());
            sendError(exchange, 500, "Error creando script launcher");
        }
    }
    
    private ByteArrayOutputStream createExecutableJar() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (JarOutputStream jos = new JarOutputStream(baos)) {
            // Crear manifest
            Manifest manifest = new Manifest();
            Attributes attributes = manifest.getMainAttributes();
            attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            attributes.put(Attributes.Name.MAIN_CLASS, "com.supermercado.SupermercadoApp");
            
            // Agregar manifest
            ZipEntry manifestEntry = new ZipEntry("META-INF/MANIFEST.MF");
            jos.putNextEntry(manifestEntry);
            manifest.write(jos);
            jos.closeEntry();
            
            // Agregar todas las clases compiladas
            addClassesToJar(jos, new File("target/classes"));
            
            System.out.println("JAR: JAR ejecutable creado exitosamente");
        }
        
        return baos;
    }
    
    private void addClassesToJar(JarOutputStream jos, File dir) throws IOException {
        addClassesToJar(jos, dir, "");
    }
    
    private void addClassesToJar(JarOutputStream jos, File dir, String basePath) throws IOException {
        if (!dir.exists()) return;
        
        File[] files = dir.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            String entryName = basePath.isEmpty() ? file.getName() : basePath + "/" + file.getName();
            
            if (file.isDirectory()) {
                addClassesToJar(jos, file, entryName);
            } else if (file.getName().endsWith(".class")) {
                ZipEntry entry = new ZipEntry(entryName);
                jos.putNextEntry(entry);
                
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        jos.write(buffer, 0, length);
                    }
                }
                
                jos.closeEntry();
            }
        }
    }
    
    private String createLauncherScript() {
        StringBuilder script = new StringBuilder();
        script.append("@echo off\n");
        script.append("echo ================================\n");
        script.append("echo  Sistema Supermercado MVC\n");
        script.append("echo  Lanzador de Aplicacion Desktop\n");
        script.append("echo ================================\n");
        script.append("echo.\n");
        script.append("\n");
        script.append("REM Verificar Java\n");
        script.append("java -version >nul 2>&1\n");
        script.append("if %ERRORLEVEL% NEQ 0 (\n");
        script.append("    echo ERROR: Java no esta instalado o no esta en el PATH\n");
        script.append("    echo Por favor instale Java 11 o superior\n");
        script.append("    pause\n");
        script.append("    exit /b 1\n");
        script.append(")\n");
        script.append("\n");
        script.append("REM Descargar JAR si no existe\n");
        script.append("if not exist \"SupermercadoMVC-Desktop.jar\" (\n");
        script.append("    echo Descargando aplicacion...\n");
        script.append("    curl -L -o \"SupermercadoMVC-Desktop.jar\" \"http://localhost:8084/api/desktop/download\"\n");
        script.append("    if %ERRORLEVEL% NEQ 0 (\n");
        script.append("        echo ERROR: No se pudo descargar la aplicacion\n");
        script.append("        pause\n");
        script.append("        exit /b 1\n");
        script.append("    )\n");
        script.append("    echo Descarga completada.\n");
        script.append(")\n");
        script.append("\n");
        script.append("echo Iniciando aplicacion de escritorio...\n");
        script.append("java -jar \"SupermercadoMVC-Desktop.jar\"\n");
        script.append("\n");
        script.append("if %ERRORLEVEL% NEQ 0 (\n");
        script.append("    echo ERROR: No se pudo iniciar la aplicacion\n");
        script.append("    pause\n");
        script.append(")\n");
        
        return script.toString();
    }
    
    private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String errorJson = "{\"error\":\"" + message + "\"}";
        byte[] errorBytes = errorJson.getBytes(StandardCharsets.UTF_8);
        
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, errorBytes.length);
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(errorBytes);
        }
        
        System.err.println("ERROR: Error " + statusCode + ": " + message);
    }
}