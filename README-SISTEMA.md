# Sistema de Gestión Unificado - MVC Completo

## Descripción
Sistema integrado que combina **tres aplicaciones MVC** con interfaces diferentes:
- **Sistema de Empleados (Desktop)**: Gestión con interface Material Design en Swing
- **Sistema de Usuarios (Desktop)**: CRUD de usuarios con datos mock en Swing
- **Sistema Web de Empleados**: Interfaz web moderna con REST API

## Características Principales

### 🔐 Sistema de Autenticación Unificado
- Login único para acceder a los tres sistemas
- Autenticación basada en usuarios del sistema principal
- Selección de sistema desde la ventana de login
- Soporte para múltiples sistemas simultáneos

### 👥 Sistema de Empleados (Desktop)
- Interface moderna con Material Design
- CRUD completo de empleados
- 10 empleados precargados
- Búsqueda por cargo
- Ejecuta integrado en la misma JVM

### 👤 Sistema de Usuarios (Desktop)
- CRUD completo de usuarios
- Datos simulados (mock) - no requiere MySQL
- Ejecuta en proceso independiente
- Interface con Nimbus Look & Feel

### 🌐 Sistema Web de Empleados (Web)
- **REST API completa** para operaciones CRUD
- **Interfaz web moderna** con HTML5, CSS3 y JavaScript
- **Servidor HTTP integrado** (Puerto 8081)
- **Validación en tiempo real** de formularios
- **Filtros y búsqueda avanzada**
- **Diseño responsive** para móviles y tablets
- **Modales de confirmación** para acciones críticas
- **Formateo automático** de datos (salarios, números)

## Arquitectura Técnica

### Patrón MVC Expandido
- **Model**: Entidades (Empleado, User) + DAOs + Servicios
- **View**: 
  - Desktop: Swing con Material Design
  - Web: HTML5 + CSS3 + JavaScript
- **Controller**: 
  - Desktop: Controllers Swing
  - Web: REST API Controllers

### REST API Endpoints
```
GET    /api/empleados           # Listar todos los empleados
GET    /api/empleados/{id}      # Obtener empleado por ID
POST   /api/empleados           # Crear nuevo empleado
PUT    /api/empleados           # Actualizar empleado existente
DELETE /api/empleados/{id}      # Eliminar empleado
GET    /api/empleados/cargos    # Obtener lista de cargos únicos
```

### Arquitectura del Sistema Web
```
Frontend (HTML/CSS/JS)
        ↕
REST API (Java HttpServer)
        ↕
DAO Layer (EmpleadoDAOImpl)
        ↕
Mock Data (Empleados precargados)
```

## Ejecución

### Método Recomendado (Sistema Completo)
```powershell
powershell -ExecutionPolicy Bypass -File "compile-and-run.ps1"
```

### Solo Sistema Web de Empleados
```powershell
powershell -ExecutionPolicy Bypass -File "run-web.ps1"
```
Luego abra: http://localhost:8081

### Compilación Manual Completa
```powershell
# Compilar sistema de usuarios
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/controller/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/dao/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/model/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/service/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/view/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/config/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/mvc/MainWithFallback.java

# Compilar sistema de empleados (Desktop y Web)
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/model/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/dao/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/controller/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/view/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/util/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/api/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/launcher/*.java
javac -encoding UTF-8 -cp "src/main/java" -d "target/classes" src/main/java/com/supermercado/SupermercadoApp.java

# Ejecutar sistema unificado
java -cp "target/classes" com.supermercado.SupermercadoApp

# O ejecutar solo el sistema web
java -cp "target/classes" com.supermercado.launcher.EmpleadoWebLauncher
```

## Funcionalidades del Sistema Web

### 📝 Gestión de Empleados
- **Crear**: Formulario completo con validación en tiempo real
- **Leer**: Tabla responsive con datos formateados
- **Actualizar**: Edición inline con carga automática de datos
- **Eliminar**: Confirmación de seguridad antes de eliminar

### 🔍 Búsqueda y Filtros
- **Búsqueda por nombre**: Filtrado instantáneo
- **Filtro por cargo**: Dropdown dinámico con cargos disponibles
- **Actualización automática**: Datos en tiempo real

### ✅ Validaciones Avanzadas
- **Nombres**: Solo letras y espacios, mínimo 2 caracteres
- **Salario**: Rango de $1.000.000 a $10.000.000
- **Teléfono**: Formato flexible con validación
- **Email**: Validación RFC completa
- **Feedback visual**: Estados de éxito/error en tiempo real

### 🎨 Interfaz Moderna
- **Diseño responsive**: Adaptable a móviles y tablets
- **Material Design**: Colores y animaciones modernas
- **Modales de confirmación**: UX mejorada para acciones críticas
- **Mensajes informativos**: Feedback claro al usuario
- **Formateo automático**: Números con separadores de miles

## Usuarios de Prueba
El sistema incluye usuarios precargados para testing. Se muestran en la ventana de login con formato:
- **Nombre Usuario** (email@ejemplo.com)

## Arquitectura

### Patrón MVC Expandido
- **Model**: Entidades de datos (User, Empleado) + DAOs + Servicios
- **View**: 
  - Desktop: Interfaces Swing con Material Design
  - Web: HTML5 + CSS3 + JavaScript moderno
- **Controller**: 
  - Desktop: Lógica de negocio Swing
  - Web: REST API Controllers con HttpServer

### Arquitectura 4+1 de Kruchten Completa
- **Vista Lógica**: Separación MVC clara en todos los sistemas
- **Vista de Proceso**: 
  - Proceso principal (Empleados Desktop integrado)
  - Proceso independiente (Usuarios con datos mock)
  - Proceso web (Servidor HTTP en puerto 8081)
- **Vista de Desarrollo**: 
  - Paquetes organizados por responsabilidad
  - APIs REST bien definidas
  - Recursos web separados
- **Vista Física**: 
  - Aplicación Java standalone multiplataforma
  - Servidor web integrado
  - Recursos estáticos embebidos
- **Escenarios**: 
  - Casos de uso CRUD completos
  - Autenticación unificada
  - Sistemas simultáneos
  - Interfaz web moderna

### Tecnologías Completas
- **Backend**: Java 11+ con HttpServer nativo
- **Frontend Web**: HTML5, CSS3, JavaScript ES6+
- **Desktop**: Swing con Look & Feel Nimbus/Material Design
- **Comunicación**: REST API con JSON nativo
- **Datos**: Mock Data Patterns + DAO Pattern
- **Arquitectura**: MVC + Singleton + Observer
- **Procesos**: ProcessBuilder para sistemas independientes

## Estructura del Proyecto Completa
```
src/main/java/
├── com/mvc/                    # Sistema de Usuarios
│   ├── controller/
│   ├── dao/
│   ├── model/
│   ├── service/
│   ├── view/
│   ├── config/
│   └── MainWithFallback.java
├── com/supermercado/           # Sistema de Empleados
│   ├── controller/             # Controllers Desktop
│   ├── dao/                    # Data Access Objects
│   ├── model/                  # Entidades de datos
│   ├── service/                # Servicios de negocio
│   ├── view/                   # Vistas Desktop Swing
│   ├── api/                    # ⭐ REST API Controllers
│   ├── util/                   # ⭐ Utilidades JSON
│   ├── launcher/               # ⭐ Web Server Launchers
│   └── SupermercadoApp.java
└── resources/web/              # ⭐ Recursos Web
    ├── empleados.html          # ⭐ Interfaz HTML
    ├── empleados-styles.css    # ⭐ Estilos CSS3
    └── empleados-script.js     # ⭐ JavaScript App
```

## Solución de Problemas

### El Sistema de Usuarios no abre
- ✅ **Solucionado**: Ahora usa `MainWithFallback` que funciona sin MySQL
- Los datos son simulados y se muestran correctamente
- Si persiste el error, use `compile-and-run.ps1`

### El Sistema Web no inicia
- ✅ **Verificar puerto**: Asegurar que el puerto 8081 esté libre
- ✅ **Compilación**: Usar `run-web.ps1` para compilación automática
- ✅ **Recursos**: Los archivos HTML/CSS/JS se embeben automáticamente
- ✅ **Navegador**: Se abre automáticamente o ir a http://localhost:8081

### Error de compilación
- Verificar que Java 11+ esté instalado
- Usar el script `compile-and-run.ps1` para compilación automática
- Verificar encoding UTF-8 en Windows
- Para solo web: usar `run-web.ps1`

### Login no funciona
- Usar cualquiera de los usuarios mostrados en la lista
- Debe coincidir exactamente el nombre y email
- El sistema distingue entre mayúsculas y minúsculas

### API REST no responde
- Verificar que el servidor web esté iniciado (puerto 8081)
- Comprobar logs en consola para errores
- Verificar CORS - la API permite todos los orígenes
- Usar herramientas como Postman para probar endpoints

### Interfaz web no carga
- Verificar que los recursos estén en `src/main/resources/web/`
- Los archivos se sirven desde `/` (ej: `/empleados.html`)
- Verificar consola del navegador para errores JavaScript
- Asegurar que el servidor esté corriendo antes de abrir el navegador

## Desarrollo

### Estructura del Proyecto
```
src/main/java/
├── com/mvc/                 # Sistema de Usuarios
│   ├── controller/
│   ├── dao/
│   ├── model/
│   ├── service/
│   ├── view/
│   ├── config/
│   └── MainWithFallback.java
└── com/supermercado/        # Sistema de Empleados
    ├── controller/
    ├── dao/
    ├── model/
    ├── service/
    ├── view/
    └── SupermercadoApp.java
```

### Extensibilidad
- Agregar nuevos sistemas siguiendo el patrón establecido
- Modificar `SupermercadoApp` para incluir nuevas opciones
- Los sistemas pueden ejecutarse independientemente

## Estado Actual
✅ **Completamente Funcional - Sistema Triple**
- ✅ Login unificado con selección de tres sistemas
- ✅ Sistema de Empleados Desktop (Material Design)
- ✅ Sistema de Usuarios Desktop (datos mock)
- ✅ **Sistema Web de Empleados (REST API + Frontend moderno)**
- ✅ Interface web responsive y moderna
- ✅ API REST completa con todos los endpoints
- ✅ Validación avanzada en tiempo real
- ✅ Compilación y ejecución automatizada
- ✅ Documentación completa actualizada

## Screenshots y Demostraciones

### Sistema de Login Actualizado
- Selector de tres sistemas: Empleados, Usuarios, Web Empleados
- Lista dinámica de usuarios de prueba
- Interface Material Design

### Sistema Web de Empleados
- **URL**: http://localhost:8081
- **Formulario CRUD** completo con validaciones
- **Tabla responsive** con búsqueda y filtros
- **Modales de confirmación** para acciones críticas
- **Diseño adaptable** para móviles y desktop

### API REST Endpoints
```bash
# Obtener todos los empleados
GET http://localhost:8081/api/empleados

# Crear nuevo empleado
POST http://localhost:8081/api/empleados
Content-Type: application/json
{
  "nombre": "Juan Pérez",
  "cargo": "Cajero",
  "salario": 1500000,
  "telefono": "+57 300 123 4567",
  "email": "juan.perez@supermercado.com"
}

# Obtener cargos disponibles
GET http://localhost:8081/api/empleados/cargos
```