# 👥 MANUAL DE USUARIO
## Sistema MVC de Gestión de Empleados

---

## 📋 **INFORMACIÓN GENERAL**

| **Campo** | **Valor** |
|-----------|-----------|
| **Sistema** | Gestión de Empleados MVC |
| **Versión** | 1.0.0 |
| **Fecha** | Septiembre 27, 2025 |
| **Interfaces** | Web + Desktop |
| **URL Web** | http://localhost:8084 |

---

## 🚀 **INICIO RÁPIDO**

### **🌐 Acceso Web (Recomendado)**

1. **Abrir navegador** (Chrome, Firefox, Edge, Safari)
2. **Ir a:** `http://localhost:8084`
3. **Hacer clic en "Iniciar Sesión"**
4. **Ingresar credenciales:**
   - **📧 Email:** `admin@sistema.com`
   - **👤 Nombre:** `admin`
5. **¡Listo!** Ya puedes gestionar empleados

### **🖥️ Acceso Desktop**
- **✅ Se abre automáticamente** al iniciar el sistema
- **🔍 Buscar ventana:** "Sistema de Gestión de Empleados"
- **☕ Icono Java** en la barra de tareas

---

## 🔐 **SISTEMA DE AUTENTICACIÓN**

### **Usuarios Disponibles**

| **Usuario** | **Email** | **Contraseña** | **Rol** |
|-------------|-----------|----------------|---------|
| **admin** | admin@sistema.com | admin123 | Administrador |
| **user** | user@sistema.com | password | Usuario |
| **test** | test@sistema.com | test123 | Pruebas |

### **Proceso de Login (Web)**

1. **📝 Completar formulario:**
   ```
   📧 Email: [tu-email@sistema.com]
   👤 Nombre: [tu-nombre]
   ```

2. **🔘 Hacer clic en "Iniciar Sesión"**

3. **✅ Confirmación:**
   - Verde: "Inicio de sesión exitoso"
   - Rojo: "Credenciales incorrectas"

4. **🔄 Redirección automática** a la gestión de empleados

### **Cerrar Sesión**
- **Web:** Botón "Cerrar Sesión" en la esquina superior derecha
- **Desktop:** Cerrar ventana con X

---

## 👥 **GESTIÓN DE EMPLEADOS**

### **📊 Vista Principal**

#### **🌐 Interfaz Web**
```
┌─────────────────────────────────────────────────┐
│ 🏢 Sistema de Gestión - Empleados               │
├─────────────────────────────────────────────────┤
│ ➕ Nuevo Empleado    🔍 Buscar    📊 Filtrar    │
├─────────────────────────────────────────────────┤
│ ID │ Nombre      │ Email        │ Puesto   │ 💰 │
│ 1  │ Ana García  │ ana@...      │ Gerente  │... │
│ 2  │ Luis Martín │ luis@...     │ Vendedor │... │
│ └──┴─────────────┴──────────────┴──────────┴────┘
│ ✏️ Editar  🗑️ Eliminar  👁️ Ver Detalles         │
└─────────────────────────────────────────────────┘
```

#### **🖥️ Interfaz Desktop**
```
┌─────────────────────────────────────────────────┐
│ ☕ Sistema de Gestión de Empleados               │
├─────────────────────────────────────────────────┤
│ [➕ Crear] [✏️ Editar] [🗑️ Eliminar] [🔍 Buscar] │
├─────────────────────────────────────────────────┤
│           📊 TABLA DE EMPLEADOS                 │
│ ┌───┬──────────────┬───────────────┬───────────┐ │
│ │ ID│ Nombre       │ Email         │ Salario   │ │
│ │ 1 │ Ana García   │ ana@email.com │ $65,000   │ │
│ │ 2 │ Luis Martínez│ luis@email.com│ $45,000   │ │
│ └───┴──────────────┴───────────────┴───────────┘ │
│                                                 │
│ 📝 Formulario aparece abajo al seleccionar      │
└─────────────────────────────────────────────────┘
```

---

## ➕ **CREAR NUEVO EMPLEADO**

### **🌐 En la Web**

1. **🔘 Clic en "➕ Nuevo Empleado"**
2. **📝 Completar formulario:**
   ```
   👤 Nombre:     [Juan]
   📧 Apellido:   [Pérez]
   ✉️ Email:      [juan.perez@email.com]
   📞 Teléfono:   [555-1234]
   💰 Salario:    [45000]
   💼 Puesto:     [Desarrollador]
   ```
3. **✅ Clic en "Guardar Empleado"**
4. **🎉 Confirmación:** "Empleado creado exitosamente"

### **🖥️ En Desktop**

1. **🔘 Clic en botón "Crear"**
2. **📝 Llenar campos en formulario inferior:**
   - Todos los campos son **obligatorios**
   - **💰 Salario:** Solo números decimales
   - **📧 Email:** Formato válido requerido
3. **✅ Clic en "Guardar"**
4. **🔄 La tabla se actualiza** automáticamente

### **✅ Validaciones**
- ✅ **Nombre y Apellido:** Mínimo 2 caracteres
- ✅ **Email:** Formato válido (@)
- ✅ **Teléfono:** Solo números y guiones
- ✅ **Salario:** Solo números positivos
- ✅ **Puesto:** Mínimo 3 caracteres

---

## ✏️ **EDITAR EMPLEADO**

### **🌐 En la Web**

1. **👁️ Localizar empleado** en la tabla
2. **🔘 Clic en "✏️ Editar"** en la fila
3. **📝 Modificar datos** en el formulario
4. **✅ Clic en "Actualizar"**
5. **🎉 Confirmación:** "Empleado actualizado"

### **🖥️ En Desktop**

1. **🖱️ Seleccionar fila** en la tabla
2. **🔘 Clic en "Editar"**
3. **📝 Los datos aparecen** en el formulario inferior
4. **✏️ Modificar campos** necesarios
5. **✅ Clic en "Actualizar"**
6. **🔄 Tabla se actualiza** inmediatamente

### **💡 Consejos**
- **📧 Email único:** No puede repetirse con otro empleado
- **💰 Salario:** Usar punto decimal (ejemplo: 45000.50)
- **📞 Teléfono:** Formato: 555-1234 o 555-123-4567

---

## 🗑️ **ELIMINAR EMPLEADO**

### **🌐 En la Web**

1. **🎯 Localizar empleado** a eliminar
2. **🔘 Clic en "🗑️ Eliminar"**
3. **⚠️ Confirmar eliminación** en el diálogo
4. **✅ Empleado eliminado** de la lista

### **🖥️ En Desktop**

1. **🖱️ Seleccionar empleado** en la tabla
2. **🔘 Clic en "Eliminar"**
3. **⚠️ Diálogo de confirmación:**
   ```
   ⚠️ ¿Está seguro de eliminar este empleado?
   
   👤 Nombre: Juan Pérez
   📧 Email: juan.perez@email.com
   
   [❌ No]  [✅ Sí, Eliminar]
   ```
4. **✅ Confirmar eliminación**

### **⚠️ Importante**
- **🚫 La eliminación es permanente**
- **💾 No hay papelera de reciclaje**
- **🔄 Actualizar tabla** si no se ve el cambio

---

## 🔍 **BUSCAR EMPLEADOS**

### **🌐 En la Web**

1. **📝 Usar barra de búsqueda** superior
2. **🔍 Buscar por:**
   - **👤 Nombre o apellido**
   - **📧 Email**
   - **💼 Puesto**
3. **⚡ Resultados en tiempo real**

### **🖥️ En Desktop**

1. **🔘 Clic en "Buscar"**
2. **📝 Ingresar término de búsqueda**
3. **🔍 Seleccionar criterio:**
   ```
   🔘 Por Nombre
   🔘 Por Email  
   🔘 Por Puesto
   🔘 Por Salario (rango)
   ```
4. **✅ Aplicar filtro**

### **💡 Consejos de Búsqueda**
- **🔤 No distingue mayúsculas/minúsculas**
- **🔍 Búsqueda parcial disponible** (ej: "gar" encuentra "García")
- **🔄 Limpiar filtros** para ver todos los empleados

---

## 📊 **VISUALIZAR DETALLES**

### **🌐 En la Web**

**👁️ Vista de Tarjeta:**
```
┌─────────────────────────────────┐
│ 👤 Ana García Martínez          │
│ ✉️ ana.garcia@supermercado.com  │
│ 📞 555-0101                     │
│ 💼 Gerente de Ventas            │
│ 💰 $65,000.00 anuales           │
│ 📅 Contratada: 15/03/2023       │
│                                 │
│ [✏️ Editar] [🗑️ Eliminar]       │
└─────────────────────────────────┘
```

### **🖥️ En Desktop**

**📋 Panel de Información:**
```
┌─────────────────────────────────┐
│ INFORMACIÓN DEL EMPLEADO        │
├─────────────────────────────────┤
│ ID:           001               │
│ Nombre:       Ana García        │
│ Email:        ana@email.com     │
│ Teléfono:     555-0101          │
│ Puesto:       Gerente Ventas    │
│ Salario:      $65,000.00        │
│ Contratación: 15/03/2023        │
└─────────────────────────────────┘
```

---

## 🎨 **PERSONALIZACIÓN**

### **🌐 Interfaz Web**

**🎯 Temas Disponibles:**
- **🟣 Púrpura** (Por defecto)
- **🔵 Azul corporativo**
- **🟢 Verde moderno**

**📱 Responsive Design:**
- **💻 Desktop:** Vista completa con tabla
- **📱 Móvil:** Vista de tarjetas apiladas
- **📟 Tablet:** Vista híbrida

### **🖥️ Interfaz Desktop**

**🎨 Look & Feel:**
- **🖥️ Sistema nativo** (Windows/Mac/Linux)
- **🔧 Ajustable por usuario**
- **⚡ Rendimiento optimizado**

---

## 🔧 **CONFIGURACIÓN**

### **⚙️ Configuración del Sistema**

```
📊 Datos mostrados por página: 10-50 empleados
🔄 Actualización automática: Cada 30 segundos
💾 Respaldo automático: Cada 5 minutos
🔐 Timeout de sesión: 2 horas
```

### **🌐 Configuración Web**
- **📱 Responsive:** Automático
- **🔄 Auto-refresh:** Configurable
- **💾 LocalStorage:** Sesiones guardadas

### **🖥️ Configuración Desktop**
- **📏 Tamaño ventana:** Recordado
- **📊 Columnas tabla:** Redimensionables
- **⚡ Atajos teclado:** Disponibles

---

## 🆘 **SOLUCIÓN DE PROBLEMAS**

### **🌐 Problemas Web**

| **Problema** | **Solución** |
|--------------|--------------|
| **🔴 No carga la página** | Verificar que el servidor esté en puerto 8084 |
| **❌ Login falló** | Usar credenciales correctas (admin/admin123) |
| **🐌 Carga lenta** | Limpiar caché del navegador (Ctrl+F5) |
| **📱 No responsive** | Usar navegador moderno (Chrome, Firefox) |

### **🖥️ Problemas Desktop**

| **Problema** | **Solución** |
|--------------|--------------|
| **☕ No abre ventana** | Verificar Java 8+ instalado |
| **🔄 Datos no actualizan** | Cerrar y reabrir aplicación |
| **⌨️ Atajos no funcionan** | Verificar foco en la ventana |
| **📊 Tabla vacía** | Reiniciar aplicación |

### **🆘 Problemas Comunes**

1. **🔌 "Error de conexión"**
   - **✅ Solución:** Verificar servidor activo en puerto 8084

2. **🔐 "Credenciales incorrectas"**
   - **✅ Solución:** Usar admin@sistema.com / admin

3. **📊 "No se cargan empleados"**
   - **✅ Solución:** Esperar 2-3 segundos, datos se cargan automáticamente

4. **🔄 "Cambios no se guardan"**
   - **✅ Solución:** Verificar campos obligatorios completados

---

## ⚡ **ATAJOS DE TECLADO**

### **🌐 Web**
| **Atajo** | **Acción** |
|-----------|------------|
| **Ctrl + N** | Nuevo empleado |
| **Ctrl + S** | Guardar |
| **Ctrl + F** | Buscar |
| **Esc** | Cancelar |

### **🖥️ Desktop**
| **Atajo** | **Acción** |
|-----------|------------|
| **Ctrl + N** | Crear empleado |
| **Ctrl + E** | Editar seleccionado |
| **Delete** | Eliminar seleccionado |
| **Ctrl + F** | Buscar |
| **F5** | Actualizar datos |

---

## 📊 **ESTADÍSTICAS**

### **📈 Datos del Sistema**
- **👥 Empleados precargados:** 10
- **🔐 Usuarios disponibles:** 3
- **📱 Interfaces:** 2 (Web + Desktop)
- **⚡ Tiempo de carga:** < 2 segundos
- **💾 Almacenamiento:** Mock (En memoria)

### **📋 Capacidades**
- **➕ Crear:** ✅ Ilimitado
- **✏️ Editar:** ✅ Todos los campos
- **🗑️ Eliminar:** ✅ Con confirmación
- **🔍 Buscar:** ✅ Tiempo real
- **📊 Filtrar:** ✅ Por múltiples criterios

---

## 🎓 **MEJORES PRÁCTICAS**

### **👤 Para Usuarios**
1. **💾 Guardar frecuentemente** (Web: Ctrl+S)
2. **🔍 Usar búsqueda** para listas largas
3. **📝 Verificar datos** antes de guardar
4. **🔐 Cerrar sesión** al terminar

### **🔧 Para Administradores**
1. **📊 Revisar logs** regularmente
2. **💾 Hacer respaldos** periódicos
3. **👥 Gestionar usuarios** activamente
4. **⚡ Monitorear rendimiento**

---

## 📞 **SOPORTE TÉCNICO**

### **🆘 Obtener Ayuda**
- **📧 Email:** soporte@sistema.com
- **📞 Teléfono:** +1 (555) 123-4567
- **🌐 Web:** http://localhost:8084/ayuda
- **📚 Documentación:** /docs/

### **🐛 Reportar Problemas**
```
📝 Incluir en el reporte:
1. 🖥️ Sistema operativo
2. 🌐 Navegador (si es web)
3. 📋 Pasos para reproducir
4. 📷 Captura de pantalla
5. ⚠️ Mensaje de error completo
```

---

## 📅 **HISTORIAL DE VERSIONES**

| **Versión** | **Fecha** | **Cambios** |
|-------------|-----------|-------------|
| **1.0.0** | 2025-09-27 | ✅ Versión inicial completa |
| | | ✅ Interfaces Web + Desktop |
| | | ✅ CRUD completo |
| | | ✅ Sistema autenticación |
| | | ✅ Login web reparado |

---

## ✅ **CHECKLIST USUARIO**

### **🎯 Al Iniciar**
- [ ] ✅ Verificar servidor activo (puerto 8084)
- [ ] 🌐 Probar acceso web: http://localhost:8084
- [ ] 🖥️ Verificar ventana desktop abierta
- [ ] 🔐 Probar login con admin@sistema.com

### **📊 Para Gestión Diaria**
- [ ] ➕ Crear empleados nuevos
- [ ] ✏️ Actualizar información existente
- [ ] 🔍 Buscar empleados específicos
- [ ] 📊 Revisar lista completa
- [ ] 💾 Verificar que cambios se guarden

### **🔐 Al Finalizar**
- [ ] 💾 Guardar cambios pendientes
- [ ] 🔐 Cerrar sesión web
- [ ] 🖥️ Cerrar aplicación desktop
- [ ] 📊 Verificar datos guardados

---

**🎉 ¡Felicidades! Ya dominas el Sistema MVC de Gestión de Empleados**

**📚 Manual creado el:** Septiembre 27, 2025  
**👨‍💻 Estado del sistema:** ✅ Completamente funcional  
**🔄 Última actualización:** Login web reparado definitivamente