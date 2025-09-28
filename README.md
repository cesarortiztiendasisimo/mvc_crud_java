# Sistema de Gestión Unificado - Patrón MVC Completo

Este proyecto implementa un **sistema integral de gestión** con **múltiples interfaces** utilizando el patrón arquitectónico MVC (Model-View-Controller) en Java. Incluye interfaces **desktop (Swing)** e **interfaz web moderna** con REST API.

## 🏗️ Arquitecturas Implementadas

### 1. Sistema de Empleados (Desktop + Web)
- **Desktop**: Interfaz Swing con Material Design
- **Web**: Interfaz HTML5 moderna + REST API
- **Datos**: Mock data con 10 empleados precargados
- **Funcionalidad**: CRUD completo + búsquedas avanzadas

### 2. Sistema de Usuarios (Desktop)
- **Interfaz**: Swing con Nimbus Look & Feel
- **Datos**: Mock data simulado (sin base de datos)
- **Proceso**: Independiente del sistema principal

### 3. Sistema de Autenticación Unificado
- **Login único**: Acceso a todos los sistemas
- **Selección múltiple**: Empleados Desktop, Usuarios o Web
- **Usuarios de prueba**: Cargados dinámicamente

## 📁 Estructura Completa del Proyecto

```
mvc_crud_java/
├── src/main/java/
│   ├── com/mvc/                    # Sistema de Usuarios
│   │   ├── controller/
│   │   ├── dao/
│   │   ├── model/
│   │   ├── service/
│   │   ├── view/
│   │   ├── config/
│   │   └── MainWithFallback.java
│   └── com/supermercado/           # Sistema de Empleados
│       ├── controller/             # Controllers Desktop
│       ├── dao/                    # Data Access Objects
│       ├── model/                  # Entidades (Empleado)
│       ├── service/                # Servicios de negocio
│       ├── view/                   # Vistas Desktop Swing
│       ├── api/                    # 🌐 REST API Controllers
│       ├── util/                   # 🛠️ Utilidades JSON
│       ├── launcher/               # 🚀 Web Server Launchers
│       └── SupermercadoApp.java    # Aplicación principal
├── src/main/resources/web/         # 🌐 Recursos Web
│   ├── empleados.html              # Interfaz HTML5
│   ├── empleados-styles.css        # Estilos CSS3 modernos
│   └── empleados-script.js         # JavaScript aplicación
├── database/
│   └── create_database.sql
├── docs/
│   └── importancia-mvc.md
├── compile-and-run.ps1             # 🔧 Script compilación completa
├── run-web.ps1                     # 🌐 Script solo sistema web
├── README-SISTEMA.md               # 📖 Documentación completa
├── API-EXAMPLES.md                 # 🔗 Ejemplos API REST
└── README.md
```

## 🚀 Ejecución Rápida

### Método Recomendado (Sistema Completo)
```powershell
# Compilar y ejecutar todo el sistema unificado
powershell -ExecutionPolicy Bypass -File "compile-and-run.ps1"
```

### Solo Sistema Web de Empleados
```powershell
# Solo la interfaz web moderna
powershell -ExecutionPolicy Bypass -File "run-web.ps1"
```
**Luego abra:** http://localhost:8081

### Ejecución Individual
```powershell
# Solo sistema web de empleados (servidor HTTP)
java -cp "target/classes" com.supermercado.launcher.EmpleadoWebLauncher

# Sistema unificado con login (incluye opción web)
java -cp "target/classes" com.supermercado.SupermercadoApp
```

## 🌐 Sistema Web de Empleados - Características

### REST API Completa
- **Puerto**: 8081
- **Formato**: JSON
- **CORS**: Habilitado
- **Endpoints**: CRUD completo + búsquedas

```bash
GET    /api/empleados           # Listar empleados
POST   /api/empleados           # Crear empleado
PUT    /api/empleados           # Actualizar empleado
DELETE /api/empleados/{id}      # Eliminar empleado
GET    /api/empleados/cargos    # Listar cargos únicos
```

### Interfaz Web Moderna
- **HTML5** con diseño responsive
- **CSS3** con Material Design
- **JavaScript ES6+** con validaciones en tiempo real
- **Modales** de confirmación y mensajes
- **Búsqueda y filtros** instantáneos
- **Formateo automático** de números y monedas

### Funcionalidades Web
- ✅ **CRUD Completo**: Crear, leer, actualizar, eliminar
- ✅ **Validación en Tiempo Real**: Feedback inmediato
- ✅ **Búsqueda Instantánea**: Por nombre de empleado
- ✅ **Filtros Dinámicos**: Por cargo con dropdown
- ✅ **Diseño Responsive**: Móviles, tablets y desktop
- ✅ **Confirmaciones de Seguridad**: Para acciones críticas
- ✅ **Formateo Inteligente**: Salarios con separadores de miles

## 🏗️ Arquitectura MVC Expandida

### Model (Modelo)
- **Empleado.java**: Entidad empleado con atributos completos
- **User.java**: Entidad usuario para autenticación
- **EmpleadoDAO.java/Impl**: Operaciones CRUD para empleados
- **UserDAO.java/Mock**: Operaciones con datos simulados
- **AuthenticationService**: Servicio de autenticación unificado (Singleton)

### View (Vista)
#### Desktop (Swing)
- **EmpleadoView.java**: Interfaz Material Design para empleados
- **LoginView.java**: Login unificado con selección de sistema
- **UserView.java**: Interfaz Nimbus para usuarios

#### Web (HTML5 + CSS3 + JS)
- **empleados.html**: Página principal con formulario CRUD
- **empleados-styles.css**: Estilos modernos responsive
- **empleados-script.js**: Lógica de negocio frontend

### Controller (Controlador)
#### Desktop Controllers
- **EmpleadoController.java**: Lógica de negocio empleados
- **LoginController.java**: Control de autenticación
- **UserController.java**: Control de usuarios

#### Web Controllers (REST API)
- **EmpleadoRestController.java**: API REST con HttpServer
- **JsonUtil.java**: Utilidades JSON sin dependencias externas

## 🔧 Tecnologías Implementadas

### Backend
- **Java 11+** con HttpServer nativo
- **Swing** con Material Design personalizado
- **Patrón Singleton** para servicios globales
- **DAO Pattern** para abstracción de datos
- **Mock Data** para demostración sin base de datos

### Frontend Web
- **HTML5** semántico y accesible
- **CSS3** con Flexbox y Grid
- **JavaScript ES6+** modular
- **Responsive Design** mobile-first
- **Material Design** Components

### Comunicación
- **REST API** con JSON nativo
- **CORS** habilitado para desarrollo
- **ProcessBuilder** para sistemas independientes
- **Singleton** para servicios compartidos

## Configuración de la Base de Datos

### 1. Instalar MySQL
- Descargar e instalar MySQL Server
- Asegurarse de que el servicio esté ejecutándose

### 2. Crear la Base de Datos
```sql
-- Ejecutar el script ubicado en database/create_database.sql
mysql -u root -p < database/create_database.sql
```

### 3. Configurar Credenciales
Editar el archivo `src/main/java/com/mvc/config/DatabaseConfig.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/user_management_db";
private static final String USER = "tu_usuario_mysql";
private static final String PASSWORD = "tu_contraseña_mysql";
```

## Instalación y Ejecución

### 1. Clonar/Descargar el Proyecto
```bash
# Si usas Git
git clone <url-del-repositorio>
cd mvc_crud_java

# O descargar y extraer el ZIP
```

### 2. Compilar el Proyecto
```bash
# Compilar con Maven
mvn clean compile

# Verificar dependencias
mvn dependency:resolve
```

### 3. Ejecutar la Aplicación
```bash
# Ejecutar desde Maven
mvn exec:java -Dexec.mainClass="com.mvc.Main"

# O desde IDE
# Ejecutar la clase Main.java
```

## Funcionalidades CRUD

### Crear Usuario
1. Completar todos los campos del formulario:
   - Nombre (solo letras y espacios)
   - Email (formato válido)
   - Teléfono (formato colombiano: +57 XXX XXX XXXX)
   - Dirección
2. Hacer clic en "Crear Usuario"
3. El usuario se agrega a la base de datos y aparece en la tabla

### Consultar Usuarios
- La tabla muestra todos los usuarios automáticamente al iniciar
- Usar "Refrescar" para actualizar la lista
- Seleccionar una fila para ver detalles en el formulario

### Actualizar Usuario
1. Seleccionar un usuario en la tabla (carga datos en el formulario)
2. Modificar los campos necesarios
3. Hacer clic en "Actualizar"
4. Los cambios se guardan en la base de datos

### Eliminar Usuario
1. Seleccionar un usuario en la tabla
2. Hacer clic en "Eliminar"
3. Confirmar la eliminación en el diálogo
4. El usuario se elimina permanentemente

### Funciones Adicionales
- **Limpiar**: Borra todos los campos del formulario
- **Validaciones**: El sistema valida formatos y campos obligatorios
- **Mensajes**: Muestra confirmaciones y errores al usuario

## Características Técnicas

### Patrones de Diseño Implementados
- **MVC**: Separación clara de responsabilidades
- **DAO**: Abstracción del acceso a datos
- **Singleton**: Para la conexión a base de datos

### Validaciones Incluidas
- **Nombres**: Solo letras, espacios y caracteres especiales españoles
- **Email**: Formato RFC válido
- **Teléfono**: Formato colombiano con/sin código de país
- **Campos obligatorios**: Validación de campos vacíos
- **Longitud**: Límites en caracteres para cada campo

### Manejo de Errores
- Conexión a base de datos
- Validaciones de entrada
- Errores SQL específicos
- Mensajes informativos al usuario

## Solución de Problemas

### Error de Conexión a Base de Datos
1. Verificar que MySQL esté ejecutándose
2. Comprobar credenciales en `DatabaseConfig.java`
3. Asegurarse de que la base de datos existe
4. Verificar que el puerto 3306 esté disponible

### Error de Compilación
1. Verificar versión de Java (JDK 11+)
2. Ejecutar `mvn clean install`
3. Revisar dependencias en `pom.xml`

### Problemas de Interfaz
1. Verificar que Swing esté disponible
2. Comprobar resolución de pantalla
3. Reiniciar la aplicación

## Documentación Adicional

- **Importancia del MVC**: Ver `docs/importancia-mvc.md`
- **Script SQL**: Ver `database/create_database.sql`
- **Configuración del Proyecto**: Ver `.github/copilot-instructions.md`

## 📋 Objetivos Académicos Cumplidos

Este proyecto demuestra de manera **integral**:

### Patrones Arquitectónicos
1. **MVC Completo**: Implementado en desktop y web
2. **REST API**: Arquitectura orientada a servicios
3. **Singleton**: Para servicios compartidos
4. **DAO**: Abstracción de acceso a datos
5. **Observer**: En interfaces reactivas

### Tecnologías Modernas
6. **Desarrollo Web Full-Stack**: Backend Java + Frontend moderno
7. **APIs REST**: Comunicación HTTP estándar
8. **Interfaz Responsive**: Adaptable a dispositivos
9. **Validación Avanzada**: Cliente y servidor
10. **Manejo de Procesos**: Sistemas concurrentes

### Ingeniería de Software
11. **Separación de Responsabilidades**: Arquitectura limpia
12. **Reutilización de Código**: Componentes modulares
13. **Manejo de Errores**: Robusto y user-friendly
14. **Documentación Completa**: Código y uso
15. **Testing Ready**: Estructura preparada para pruebas

## 🔗 Documentación Adicional

- **[README-SISTEMA.md](README-SISTEMA.md)**: Documentación técnica completa
- **[API-EXAMPLES.md](API-EXAMPLES.md)**: Ejemplos de uso de la REST API
- **[docs/importancia-mvc.md](docs/importancia-mvc.md)**: Fundamentos del patrón MVC
- **[database/create_database.sql](database/create_database.sql)**: Script de base de datos MySQL

## 🚨 Solución Rápida de Problemas

### Sistema no compila
```powershell
# Usar script automático
powershell -ExecutionPolicy Bypass -File "compile-and-run.ps1"
```

### Sistema web no abre
```powershell
# Solo sistema web
powershell -ExecutionPolicy Bypass -File "run-web.ps1"
# Luego ir a: http://localhost:8081
```

### Login no funciona
- Usar usuarios de la lista mostrada en el login
- Nombre y email deben coincidir exactamente
- Seleccionar el sistema deseado en el dropdown

## 🌟 Características Destacadas

### Sistema Triple Integrado
- **Desktop Empleados**: Interface nativa Swing
- **Desktop Usuarios**: Proceso independiente
- **Web Empleados**: Interfaz moderna + API REST

### Experiencia de Usuario
- **Login Unificado**: Un solo punto de acceso
- **Selección Múltiple**: Tres sistemas desde un login
- **Ejecución Simultánea**: Sistemas independientes
- **Interfaces Modernas**: Material Design + Web responsive

### Arquitectura Profesional
- **APIs RESTful**: Estándares de la industria
- **JSON Nativo**: Sin dependencias externas pesadas
- **Mock Data**: Demostración sin configuración
- **Procesos Concurrentes**: Arquitectura escalable

## 📞 Soporte y Contribuciones

Este sistema está completamente funcional y documentado. Para dudas específicas:

1. **Revisar documentación**: README-SISTEMA.md y API-EXAMPLES.md
2. **Usar scripts automáticos**: compile-and-run.ps1 o run-web.ps1
3. **Verificar logs**: Los sistemas muestran información detallada en consola

## 🎯 Estado del Proyecto

✅ **Totalmente Funcional**  
✅ **Tres Sistemas Integrados**  
✅ **API REST Completa**  
✅ **Interfaz Web Moderna**  
✅ **Documentación Completa**  
✅ **Scripts de Automatización**  

**Listo para demostración y uso académico/profesional.**
