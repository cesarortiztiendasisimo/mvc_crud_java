# API REST - Ejemplos de Uso

## Información del Servidor
- **URL Base**: http://localhost:8081
- **Puerto**: 8081
- **Formato**: JSON
- **CORS**: Habilitado para todos los orígenes

## Endpoints Disponibles

### 1. Listar Todos los Empleados
```bash
GET http://localhost:8081/api/empleados
```

**Respuesta de ejemplo:**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez López",
    "cargo": "Cajero",
    "salario": 1500000,
    "telefono": "+57 300 123 4567",
    "email": "juan.perez@supermercado.com"
  },
  {
    "id": 2,
    "nombre": "María García Rodríguez",
    "cargo": "Supervisor",
    "salario": 2500000,
    "telefono": "+57 300 987 6543",
    "email": "maria.garcia@supermercado.com"
  }
]
```

### 2. Obtener Empleado por ID
```bash
GET http://localhost:8081/api/empleados/1
```

**Respuesta:**
```json
{
  "id": 1,
  "nombre": "Juan Pérez López",
  "cargo": "Cajero",
  "salario": 1500000,
  "telefono": "+57 300 123 4567",
  "email": "juan.perez@supermercado.com"
}
```

### 3. Crear Nuevo Empleado
```bash
POST http://localhost:8081/api/empleados
Content-Type: application/json

{
  "nombre": "Ana Martínez Torres",
  "cargo": "Vendedor",
  "salario": 1800000,
  "telefono": "+57 301 555 0123",
  "email": "ana.martinez@supermercado.com"
}
```

**Respuesta:**
```json
{
  "message": "Empleado creado exitosamente"
}
```

### 4. Actualizar Empleado Existente
```bash
PUT http://localhost:8081/api/empleados
Content-Type: application/json

{
  "id": 1,
  "nombre": "Juan Carlos Pérez López",
  "cargo": "Supervisor",
  "salario": 2200000,
  "telefono": "+57 300 123 4567",
  "email": "juan.perez@supermercado.com"
}
```

**Respuesta:**
```json
{
  "message": "Empleado actualizado exitosamente"
}
```

### 5. Eliminar Empleado
```bash
DELETE http://localhost:8081/api/empleados/1
```

**Respuesta:**
```json
{
  "message": "Empleado eliminado exitosamente"
}
```

### 6. Obtener Lista de Cargos Únicos
```bash
GET http://localhost:8081/api/empleados/cargos
```

**Respuesta:**
```json
[
  "Administrador",
  "Almacenista",
  "Cajero",
  "Gerente",
  "Limpieza",
  "Seguridad",
  "Supervisor",
  "Vendedor"
]
```

## Códigos de Respuesta HTTP

### Respuestas Exitosas
- **200 OK**: Operación exitosa (GET, PUT, DELETE)
- **201 Created**: Recurso creado exitosamente (POST)

### Respuestas de Error
- **400 Bad Request**: Datos inválidos o error en la operación
- **404 Not Found**: Empleado no encontrado
- **405 Method Not Allowed**: Método HTTP no permitido
- **500 Internal Server Error**: Error interno del servidor

## Ejemplos con cURL

### Listar empleados
```bash
curl -X GET http://localhost:8081/api/empleados
```

### Crear empleado
```bash
curl -X POST http://localhost:8081/api/empleados \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Pedro Ramírez",
    "cargo": "Almacenista", 
    "salario": 1600000,
    "telefono": "+57 302 444 5555",
    "email": "pedro.ramirez@supermercado.com"
  }'
```

### Actualizar empleado
```bash
curl -X PUT http://localhost:8081/api/empleados \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "nombre": "Juan Pérez (Actualizado)",
    "cargo": "Gerente",
    "salario": 3000000,
    "telefono": "+57 300 123 4567",
    "email": "juan.perez@supermercado.com"
  }'
```

### Eliminar empleado
```bash
curl -X DELETE http://localhost:8081/api/empleados/1
```

## Validaciones de Datos

### Campos Obligatorios
- `nombre`: Texto, mínimo 2 caracteres, solo letras y espacios
- `cargo`: Debe ser uno de los cargos válidos
- `salario`: Número entre 1,000,000 y 10,000,000
- `telefono`: Formato flexible de teléfono
- `email`: Formato de email válido

### Ejemplo de Error de Validación
```json
{
  "error": "Error al crear empleado"
}
```

## Testing con Herramientas

### Postman
1. Importar la colección con los endpoints listados arriba
2. Configurar la base URL: `http://localhost:8081`
3. Probar cada endpoint secuencialmente

### Thunder Client (VS Code)
1. Crear una nueva colección
2. Agregar requests para cada endpoint
3. Configurar headers `Content-Type: application/json` para POST/PUT

### Insomnia
1. Crear un nuevo workspace
2. Configurar el ambiente con `base_url: http://localhost:8081`
3. Crear requests para cada operación CRUD

## Notas Importantes

- El servidor debe estar ejecutándose antes de realizar peticiones
- Los datos se almacenan en memoria (se pierden al reiniciar)
- La API soporta CORS para desarrollo web
- Los IDs se generan automáticamente para nuevos empleados
- Los errores se devuelven en formato JSON consistente