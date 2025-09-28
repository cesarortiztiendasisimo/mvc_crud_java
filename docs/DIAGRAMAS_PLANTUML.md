# Diagramas PlantUML - Sistema MVC de Gestión de Empleados

Este documento contiene todos los diagramas del sistema en formato PlantUML para generar diagramas profesionales.

## Índice de Diagramas

1. [Diagrama de Arquitectura MVC](#1-diagrama-de-arquitectura-mvc)
2. [Diagrama de Clases Principal](#2-diagrama-de-clases-principal)
3. [Diagrama de Secuencia - Login](#3-diagrama-de-secuencia---login)
4. [Diagrama de Secuencia - CRUD Empleados](#4-diagrama-de-secuencia---crud-empleados)
5. [Diagrama de Componentes](#5-diagrama-de-componentes)
6. [Diagrama de Despliegue](#6-diagrama-de-despliegue)
7. [Diagrama de Base de Datos](#7-diagrama-de-base-de-datos)
8. [Diagrama de Casos de Uso](#8-diagrama-de-casos-de-uso)
9. [Diagrama de Estados - Sesión Usuario](#9-diagrama-de-estados---sesión-usuario)
10. [Diagrama de Actividades - CRUD](#10-diagrama-de-actividades---crud)
11. [Diagrama de Flujo - Autenticación](#11-diagrama-de-flujo---autenticación)
12. [Diagrama de Red - Comunicación](#12-diagrama-de-red---comunicación)

---

## 1. Diagrama de Arquitectura MVC

```plantuml
@startuml mvc_architecture
!theme plain
title Arquitectura MVC - Sistema de Gestión de Empleados

package "Presentation Layer" as presentation {
  [Web Interface\n(Material Design)]
  [Desktop Interface\n(Java Swing)]
}

package "Controller Layer" as controller {
  [EmpleadoRestController\n(HTTP Server)]
  [AuthHandler\n(Authentication)]
  [EmpleadoController\n(Business Logic)]
}

package "Model Layer" as model {
  [Empleado\n(Entity)]
  [Usuario\n(Entity)]
  [EmpleadoDAO\n(Interface)]
  [UserDAO\n(Interface)]
}

package "Data Access Layer" as data {
  [EmpleadoDAOImpl\n(Mock Implementation)]
  [UserDAOMock\n(Mock Implementation)]
}

presentation --> controller : HTTP Requests
controller --> model : Business Logic
model --> data : Data Operations
data --> model : Data Results
model --> controller : Response
controller --> presentation : HTTP Response

note right of presentation
  Dual Interface:
  - Web (Material Design)
  - Desktop (Swing)
end note

note right of controller
  Port 8084
  REST API
  Static Files
end note

note right of data
  Mock Data:
  - 10 Empleados
  - 3 Usuarios
end note

@enduml
```

---

## 2. Diagrama de Clases Principal

```plantuml
@startuml class_diagram
!theme plain
title Diagrama de Clases - Sistema de Gestión de Empleados

class Empleado {
  -int id
  -String nombre
  -String apellido
  -String email
  -String telefono
  -String departamento
  -double salario
  +Empleado()
  +Empleado(id, nombre, apellido, email, telefono, departamento, salario)
  +getId() : int
  +setId(int) : void
  +getNombre() : String
  +setNombre(String) : void
  +getApellido() : String
  +setApellido(String) : void
  +getEmail() : String
  +setEmail(String) : void
  +getTelefono() : String
  +setTelefono(String) : void
  +getDepartamento() : String
  +setDepartamento(String) : void
  +getSalario() : double
  +setSalario(double) : void
  +toString() : String
}

class Usuario {
  -String username
  -String password
  -String name
  +Usuario()
  +Usuario(username, password, name)
  +getUsername() : String
  +setUsername(String) : void
  +getPassword() : String
  +setPassword(String) : void
  +getName() : String
  +setName(String) : void
  +toString() : String
}

interface EmpleadoDAO {
  +create(Empleado) : void
  +findById(int) : Empleado
  +findAll() : List<Empleado>
  +update(Empleado) : void
  +delete(int) : void
}

interface UserDAO {
  +authenticate(String, String) : Usuario
  +findByUsername(String) : Usuario
  +getAllUsers() : List<Usuario>
}

class EmpleadoDAOImpl {
  -List<Empleado> empleados
  +EmpleadoDAOImpl()
  +create(Empleado) : void
  +findById(int) : Empleado
  +findAll() : List<Empleado>
  +update(Empleado) : void
  +delete(int) : void
  -initializeData() : void
}

class UserDAOMock {
  -List<Usuario> usuarios
  +UserDAOMock()
  +authenticate(String, String) : Usuario
  +findByUsername(String) : Usuario
  +getAllUsers() : List<Usuario>
  -initializeUsers() : void
}

class EmpleadoController {
  -EmpleadoDAO empleadoDAO
  +EmpleadoController()
  +createEmpleado(Empleado) : void
  +getEmpleado(int) : Empleado
  +getAllEmpleados() : List<Empleado>
  +updateEmpleado(Empleado) : void
  +deleteEmpleado(int) : void
}

class EmpleadoRestController {
  -HttpServer server
  -EmpleadoController empleadoController
  -UserDAO userDAO
  +main(String[]) : void
  +startServer() : void
  -createRoutes() : void
  -killProcessUsingPort(int) : void
}

class AuthHandler {
  -UserDAO userDAO
  +AuthHandler(UserDAO)
  +handle(HttpExchange) : void
  -authenticate(String, String) : Usuario
}

class EmpleadoView {
  -EmpleadoController controller
  -JFrame frame
  -JTable table
  -DefaultTableModel tableModel
  +EmpleadoView()
  +initializeComponents() : void
  +refreshTable() : void
  +showCreateDialog() : void
  +showEditDialog(Empleado) : void
  -clearFields() : void
}

EmpleadoDAO <|-- EmpleadoDAOImpl
UserDAO <|-- UserDAOMock
EmpleadoController --> EmpleadoDAO
EmpleadoRestController --> EmpleadoController
EmpleadoRestController --> UserDAO
AuthHandler --> UserDAO
EmpleadoView --> EmpleadoController

@enduml
```

---

## 3. Diagrama de Secuencia - Login

```plantuml
@startuml login_sequence
!theme plain
title Secuencia de Autenticación - Login de Usuario

actor Usuario as user
participant "Web Interface" as web
participant "EmpleadoRestController" as server
participant "AuthHandler" as auth
participant "UserDAOMock" as dao

user -> web: Ingresa credenciales
web -> web: validate_form()
web -> server: POST /api/auth/login\n{"username": "admin", "password": "admin"}

server -> auth: handle(HttpExchange)
auth -> auth: Parse JSON request
auth -> dao: authenticate(username, password)

alt Credenciales válidas
    dao -> dao: Buscar usuario en lista
    dao --> auth: Usuario encontrado
    auth --> server: HTTP 200 OK\n{"success": true, "user": {...}}
    server --> web: Response con datos usuario
    web -> web: Guardar sesión
    web -> web: Redireccionar a dashboard
    web --> user: Mostrar interfaz principal
else Credenciales inválidas
    dao --> auth: null (usuario no encontrado)
    auth --> server: HTTP 401 Unauthorized\n{"success": false, "message": "..."}
    server --> web: Error de autenticación
    web -> web: Mostrar mensaje error
    web --> user: "Credenciales incorrectas"
end

note right of dao
  Usuarios de prueba:
  - admin/admin
  - user/user
  - test/test
end note

@enduml
```

---

## 4. Diagrama de Secuencia - CRUD Empleados

```plantuml
@startuml crud_sequence
!theme plain
title Secuencia CRUD - Gestión de Empleados

actor Usuario as user
participant "Interface" as ui
participant "EmpleadoController" as controller
participant "EmpleadoDAOImpl" as dao

== Crear Empleado ==
user -> ui: Llenar formulario
ui -> ui: Validar datos
ui -> controller: createEmpleado(empleado)
controller -> dao: create(empleado)
dao -> dao: Agregar a lista
dao --> controller: void
controller --> ui: Empleado creado
ui --> user: Mensaje éxito

== Leer Empleados ==
user -> ui: Solicitar lista
ui -> controller: getAllEmpleados()
controller -> dao: findAll()
dao -> dao: Retornar lista completa
dao --> controller: List<Empleado>
controller --> ui: Lista empleados
ui -> ui: Actualizar tabla/vista
ui --> user: Mostrar empleados

== Actualizar Empleado ==
user -> ui: Seleccionar empleado
ui -> controller: getEmpleado(id)
controller -> dao: findById(id)
dao --> controller: Empleado
controller --> ui: Datos empleado
ui --> user: Mostrar formulario edición
user -> ui: Modificar datos
ui -> controller: updateEmpleado(empleado)
controller -> dao: update(empleado)
dao -> dao: Actualizar en lista
dao --> controller: void
controller --> ui: Empleado actualizado
ui --> user: Mensaje éxito

== Eliminar Empleado ==
user -> ui: Confirmar eliminación
ui -> controller: deleteEmpleado(id)
controller -> dao: delete(id)
dao -> dao: Remover de lista
dao --> controller: void
controller --> ui: Empleado eliminado
ui -> ui: Actualizar vista
ui --> user: Mensaje éxito

@enduml
```

---

## 5. Diagrama de Componentes

```plantuml
@startuml components_diagram
!theme plain
title Diagrama de Componentes - Sistema MVC

package "Frontend Components" {
  [Web UI] as web
  [Desktop UI] as desktop
  [Material Design CSS] as css
  [JavaScript Auth] as js
}

package "Backend Components" {
  [HTTP Server] as server
  [REST API] as api
  [Authentication] as auth
  [Business Logic] as logic
}

package "Data Components" {
  [DAO Interface] as interface
  [Mock Data] as mock
  [Entity Models] as models
}

package "Static Resources" {
  [HTML Files] as html
  [CSS Styles] as styles
  [JavaScript Files] as scripts
  [Images] as images
}

web --> css
web --> js
js --> api : HTTP/JSON
desktop --> logic : Direct calls

server --> api
server --> auth
server --> html : Static files
api --> logic
auth --> mock
logic --> interface
interface --> mock
mock --> models

note right of server
  Puerto 8084
  Servidor HTTP embebido
  CORS habilitado
end note

note right of mock
  Datos de prueba:
  - 10 empleados
  - 3 usuarios
end note

@enduml
```

---

## 6. Diagrama de Despliegue

```plantuml
@startuml deployment_diagram
!theme plain
title Diagrama de Despliegue - Sistema MVC

node "Servidor Local" as server_node {
  artifact "mvc_crud_java.jar" as jar
  component "JVM" as jvm {
    component "HTTP Server\n:8084" as http_server
    component "Swing Desktop" as swing
  }
}

node "Cliente Web" as client_web {
  component "Navegador" as browser {
    artifact "HTML/CSS/JS" as web_files
  }
}

node "Sistema Operativo" as os {
  component "Puerto 8084" as port
  database "Memoria" as memory {
    entity "Mock Data" as data
  }
}

jar --> jvm : ejecuta
http_server --> port : bind
swing --> memory : acceso directo
http_server --> memory : acceso vía DAO
browser --> http_server : HTTP/REST
web_files --> browser : carga local

note right of jar
  Aplicación Java
  Compilada con JDK 8+
end note

note right of port
  Puerto configurable
  Validación automática
end note

note right of data
  Datos en memoria:
  - Lista empleados
  - Lista usuarios
end note

@enduml
```

---

## 7. Diagrama de Base de Datos

```plantuml
@startuml database_diagram
!theme plain
title Modelo de Datos - Sistema de Empleados (Mock)

entity "Empleado" as emp {
  * id : Integer <<PK>>
  --
  nombre : String
  apellido : String
  email : String
  telefono : String
  departamento : String
  salario : Double
}

entity "Usuario" as user {
  * username : String <<PK>>
  --
  password : String
  name : String
}

note right of emp
  Datos de prueba (10 registros):
  - Ana García (Desarrollo)
  - Carlos López (Marketing)
  - María Rodríguez (RRHH)
  - etc...
end note

note right of user
  Usuarios del sistema (3 registros):
  - admin/admin (Administrador)
  - user/user (Usuario)
  - test/test (Pruebas)
end note

note bottom
  Implementación Mock:
  - Datos almacenados en memoria
  - No persistencia en disco
  - Ideal para desarrollo/pruebas
end note

@enduml
```

---

## 8. Diagrama de Casos de Uso

```plantuml
@startuml use_cases
!theme plain
title Casos de Uso - Sistema de Gestión de Empleados

left to right direction

actor "Usuario\nAdministrador" as admin
actor "Usuario\nRegular" as user

rectangle "Sistema de Gestión de Empleados" {
  usecase "Iniciar Sesión" as login
  usecase "Crear Empleado" as create
  usecase "Consultar Empleados" as read
  usecase "Actualizar Empleado" as update
  usecase "Eliminar Empleado" as delete
  usecase "Cambiar Interfaz" as switch
  usecase "Cerrar Sesión" as logout
  
  usecase "Validar Credenciales" as validate
  usecase "Gestionar Datos" as manage
}

admin --> login
admin --> create
admin --> read
admin --> update
admin --> delete
admin --> switch
admin --> logout

user --> login
user --> read
user --> switch
user --> logout

login ..> validate : <<include>>
create ..> manage : <<include>>
update ..> manage : <<include>>
delete ..> manage : <<include>>

note right of admin
  Permisos completos:
  - CRUD completo
  - Todas las operaciones
end note

note right of user
  Permisos limitados:
  - Solo lectura
  - Consulta de datos
end note

@enduml
```

---

## 9. Diagrama de Estados - Sesión Usuario

```plantuml
@startuml user_session_states
!theme plain
title Estados de Sesión de Usuario

[*] --> NoAutenticado : Inicio aplicación

NoAutenticado --> Autenticando : Enviar credenciales
Autenticando --> Autenticado : Credenciales válidas
Autenticando --> NoAutenticado : Credenciales inválidas

state Autenticado {
  [*] --> Dashboard
  Dashboard --> ViendoEmpleados : Ver lista
  Dashboard --> CreandoEmpleado : Nuevo empleado
  Dashboard --> EditandoEmpleado : Editar empleado
  
  ViendoEmpleados --> Dashboard : Volver
  ViendoEmpleados --> EditandoEmpleado : Seleccionar empleado
  
  CreandoEmpleado --> Dashboard : Guardar/Cancelar
  EditandoEmpleado --> Dashboard : Guardar/Cancelar
  EditandoEmpleado --> EliminandoEmpleado : Eliminar
  
  EliminandoEmpleado --> Dashboard : Confirmar/Cancelar
}

Autenticado --> NoAutenticado : Cerrar sesión
Autenticado --> [*] : Cerrar aplicación

note right of Autenticando
  Validación contra:
  - UserDAOMock
  - 3 usuarios de prueba
end note

note right of Autenticado
  Operaciones disponibles:
  - CRUD empleados
  - Cambio de interfaz
  - Gestión de sesión
end note

@enduml
```

---

## 10. Diagrama de Actividades - CRUD

```plantuml
@startuml crud_activities
!theme plain
title Actividades CRUD - Gestión de Empleados

|Usuario|
start
:Seleccionar operación;

|Sistema|
if (Operación?) then (Crear)
  |Usuario|
  :Llenar formulario;
  :Enviar datos;
  |Sistema|
  :Validar datos;
  if (Datos válidos?) then (Sí)
    :Crear empleado;
    :Actualizar lista;
    :Mostrar éxito;
  else (No)
    :Mostrar error;
  endif

elseif (Leer) then
  :Obtener lista empleados;
  :Mostrar en interfaz;

elseif (Actualizar) then
  |Usuario|
  :Seleccionar empleado;
  :Modificar datos;
  :Enviar cambios;
  |Sistema|
  :Validar datos;
  if (Datos válidos?) then (Sí)
    :Actualizar empleado;
    :Mostrar éxito;
  else (No)
    :Mostrar error;
  endif

else (Eliminar)
  |Usuario|
  :Seleccionar empleado;
  :Confirmar eliminación;
  |Sistema|
  if (Confirmado?) then (Sí)
    :Eliminar empleado;
    :Actualizar lista;
    :Mostrar éxito;
  else (No)
    :Cancelar operación;
  endif
endif

|Usuario|
if (Continuar?) then (Sí)
  stop
else (No)
  :Cerrar aplicación;
  end
endif

@enduml
```

---

## 11. Diagrama de Flujo - Autenticación

```plantuml
@startuml auth_flow
!theme plain
title Flujo de Autenticación

start

:Usuario accede al sistema;
:Mostrar formulario login;

|Usuario|
:Ingresar username;
:Ingresar password;
:Hacer clic en "Login";

|Sistema|
:Recibir credenciales;
:Validar formato;

if (Formato válido?) then (No)
  :Mostrar error formato;
  stop
endif

:Buscar usuario en UserDAOMock;

if (Usuario existe?) then (No)
  :Mostrar "Usuario no encontrado";
  stop
endif

if (Password correcto?) then (No)
  :Mostrar "Contraseña incorrecta";
  stop
endif

:Crear sesión usuario;
:Guardar datos sesión;

fork
  :Mostrar interfaz web;
fork again
  :Mostrar interfaz desktop;
end fork

:Usuario autenticado;

|Usuario|
if (Cerrar sesión?) then (Sí)
  |Sistema|
  :Limpiar sesión;
  :Volver a login;
  stop
else (No)
  :Continuar usando sistema;
  end
endif

note right
  Credenciales de prueba:
  - admin / admin
  - user / user  
  - test / test
end note

@enduml
```

---

## 12. Diagrama de Red - Comunicación

```plantuml
@startuml network_diagram
!theme plain
title Comunicación de Red - Sistema MVC

node "Cliente Web" as client {
  component "Navegador" as browser
  component "JavaScript" as js
}

cloud "HTTP/REST" as http {
  interface "Puerto 8084" as port
}

node "Servidor Local" as server {
  component "HttpServer" as httpserver
  component "AuthHandler" as auth
  component "API REST" as api
  component "Static Files" as static
}

node "Aplicación Java" as app {
  component "EmpleadoController" as controller
  component "Mock Data" as data
}

browser --> port : GET/POST requests
js --> port : AJAX calls
port --> httpserver : bind

httpserver --> auth : /api/auth/*
httpserver --> api : /api/empleados/*
httpserver --> static : /*.html, *.css, *.js

auth --> data : User validation
api --> controller : Business logic
controller --> data : CRUD operations

note right of port
  Protocolo: HTTP/1.1
  Puerto: 8084
  CORS: Habilitado
  Content-Type: application/json
end note

note right of data
  Almacenamiento: Memoria
  Empleados: 10 registros
  Usuarios: 3 registros
end note

@enduml
```

---

## Instrucciones de Uso

### 1. **Instalación PlantUML**
```bash
# Opción 1: VS Code Extension
ext install plantuml

# Opción 2: Standalone
# Descargar plantuml.jar desde http://plantuml.com
```

### 2. **Generar Diagramas**
```bash
# Método 1: VS Code
# Ctrl+Shift+P -> PlantUML: Preview Current Diagram

# Método 2: Línea de comandos
java -jar plantuml.jar diagrama.puml

# Método 3: Online
# Copiar código a http://www.plantuml.com/plantuml/
```

### 3. **Formatos de Salida**
- **PNG**: Imágenes de alta calidad
- **SVG**: Gráficos vectoriales escalables
- **PDF**: Documentos profesionales
- **LaTeX**: Integración académica

### 4. **Personalización**
```plantuml
# Temas disponibles
!theme plain
!theme bluegray
!theme materia
!theme spacelab

# Colores personalizados
skinparam backgroundColor #FAFAFA
skinparam classBackgroundColor #E8F5E8
```

---

## Notas Técnicas

- **Compatibilidad**: PlantUML requiere Java 8+
- **Rendimiento**: Diagramas complejos pueden tardar en renderizar
- **Sintaxis**: Seguir guía oficial en plantuml.com/guide
- **Versionado**: Mantener diagramas sincronizados con código

---

*Documento generado el 27 de septiembre de 2025*  
*Sistema MVC de Gestión de Empleados v1.0*