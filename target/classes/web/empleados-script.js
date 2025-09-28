/**
 * JavaScript para Sistema de Gestión de Empleados - Web
 * Maneja la interacción con la API REST y la interfaz de usuario
 */

// Configuración
const API_BASE_URL = '/api/empleados';
let currentEditId = null;
let empleados = [];
let filteredEmpleados = [];

// Estado de la aplicación
const AppState = {
    isLoading: false,
    editMode: false,
    currentEmployee: null
};

// Inicialización cuando se carga la página
document.addEventListener('DOMContentLoaded', function() {
    // Verificar autenticación antes de inicializar
    if (!checkAuthentication()) {
        return;
    }
    
    initializeApp();
    setupEventListeners();
    loadEmpleados();
    loadCargosFilter();
});

/**
 * Inicializar la aplicación
 */
function initializeApp() {
    console.log('🚀 Iniciando Sistema de Gestión de Empleados Web');
    showMessage('🌐 Sistema iniciado correctamente', 'success');
}

/**
 * Configurar event listeners
 */
function setupEventListeners() {
    // Formulario principal
    document.getElementById('empleadoForm').addEventListener('submit', handleFormSubmit);
    document.getElementById('clearBtn').addEventListener('click', clearForm);
    document.getElementById('cancelEditBtn').addEventListener('click', cancelEdit);
    
    // Filtros y búsqueda
    document.getElementById('searchInput').addEventListener('input', handleSearch);
    document.getElementById('cargoFilter').addEventListener('change', handleCargoFilter);
    document.getElementById('refreshBtn').addEventListener('click', () => {
        loadEmpleados();
        showMessage('🔄 Lista actualizada', 'info');
    });
    
    // Modales
    document.getElementById('confirmYes').addEventListener('click', handleConfirmYes);
    document.getElementById('confirmNo').addEventListener('click', hideConfirmModal);
    document.getElementById('messageOk').addEventListener('click', hideMessageModal);
    
    // Validaciones en tiempo real
    setupRealTimeValidation();
}

/**
 * Configurar validaciones en tiempo real
 */
function setupRealTimeValidation() {
    const nombreInput = document.getElementById('nombre');
    const salarioInput = document.getElementById('salario');
    const telefonoInput = document.getElementById('telefono');
    const emailInput = document.getElementById('email');
    
    nombreInput.addEventListener('input', () => validateField('nombre'));
    salarioInput.addEventListener('input', () => validateField('salario'));
    telefonoInput.addEventListener('input', () => validateField('telefono'));
    emailInput.addEventListener('input', () => validateField('email'));
}

/**
 * Cargar lista de empleados desde la API
 */
async function loadEmpleados() {
    try {
        setLoading(true);
        showTableMessage('📋 Cargando empleados...');
        
        const response = await fetch(API_BASE_URL);
        if (!response.ok) {
            throw new Error(`Error HTTP: ${response.status}`);
        }
        
        empleados = await response.json();
        filteredEmpleados = [...empleados];
        renderEmpleadosTable();
        
        console.log(`✅ Cargados ${empleados.length} empleados`);
        
    } catch (error) {
        console.error('❌ Error al cargar empleados:', error);
        showTableMessage('❌ Error al cargar empleados. Verifique la conexión.');
        showMessage('Error al cargar la lista de empleados', 'error');
    } finally {
        setLoading(false);
    }
}

/**
 * Cargar cargos para el filtro
 */
async function loadCargosFilter() {
    try {
        const response = await fetch(`${API_BASE_URL}/cargos`);
        if (response.ok) {
            const cargos = await response.json();
            const select = document.getElementById('cargoFilter');
            
            // Limpiar opciones existentes excepto la primera
            select.innerHTML = '<option value="">-- Todos los cargos --</option>';
            
            // Agregar opciones de cargos
            cargos.forEach(cargo => {
                const option = document.createElement('option');
                option.value = cargo;
                option.textContent = cargo;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error al cargar cargos:', error);
    }
}

/**
 * Renderizar tabla de empleados
 */
function renderEmpleadosTable() {
    const tableBody = document.getElementById('empleadosTableBody');
    const messageDiv = document.getElementById('tableMessage');
    
    if (filteredEmpleados.length === 0) {
        tableBody.innerHTML = '';
        showTableMessage('📭 No se encontraron empleados');
        return;
    }
    
    messageDiv.style.display = 'none';
    
    const rows = filteredEmpleados.map(empleado => `
        <tr class="fade-in" data-id="${empleado.id}">
            <td>${empleado.id}</td>
            <td><strong>${empleado.nombre}</strong></td>
            <td>
                <span class="cargo-badge">${empleado.cargo}</span>
            </td>
            <td class="salary-cell">$${formatNumber(empleado.salario)}</td>
            <td>${empleado.telefono}</td>
            <td>${empleado.email}</td>
            <td>
                <div class="table-actions">
                    <button class="btn btn-edit" onclick="editEmpleado(${empleado.id})">
                        ✏️ Editar
                    </button>
                    <button class="btn btn-delete" onclick="confirmDeleteEmpleado(${empleado.id})">
                        🗑️ Eliminar
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
    
    tableBody.innerHTML = rows;
}

/**
 * Manejar envío del formulario
 */
async function handleFormSubmit(event) {
    event.preventDefault();
    
    if (!validateForm()) {
        showMessage('❌ Por favor corrija los errores en el formulario', 'error');
        return;
    }
    
    const formData = new FormData(event.target);
    const empleadoData = {
        nombre: formData.get('nombre').trim(),
        cargo: formData.get('cargo'),
        salario: parseInt(formData.get('salario')),
        telefono: formData.get('telefono').trim(),
        email: formData.get('email').trim().toLowerCase()
    };
    
    const isEdit = currentEditId !== null;
    if (isEdit) {
        empleadoData.id = currentEditId;
    }
    
    try {
        setLoading(true);
        
        const url = isEdit ? API_BASE_URL : API_BASE_URL;
        const method = isEdit ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(empleadoData)
        });
        
        if (response.ok) {
            const result = await response.json();
            showMessage(
                isEdit ? '✅ Empleado actualizado correctamente' : '✅ Empleado creado correctamente',
                'success'
            );
            
            clearForm();
            loadEmpleados();
        } else {
            const error = await response.json();
            throw new Error(error.error || 'Error en la operación');
        }
        
    } catch (error) {
        console.error('❌ Error en la operación:', error);
        showMessage(`❌ Error: ${error.message}`, 'error');
    } finally {
        setLoading(false);
    }
}

/**
 * Editar empleado
 */
function editEmpleado(id) {
    const empleado = empleados.find(e => e.id === id);
    if (!empleado) {
        showMessage('❌ Empleado no encontrado', 'error');
        return;
    }
    
    // Llenar el formulario con los datos
    document.getElementById('empleadoId').value = empleado.id;
    document.getElementById('nombre').value = empleado.nombre;
    document.getElementById('cargo').value = empleado.cargo;
    document.getElementById('salario').value = empleado.salario;
    document.getElementById('telefono').value = empleado.telefono;
    document.getElementById('email').value = empleado.email;
    
    // Cambiar a modo edición
    currentEditId = id;
    AppState.editMode = true;
    AppState.currentEmployee = empleado;
    
    // Actualizar interfaz
    document.getElementById('submitText').textContent = '💾 Actualizar Empleado';
    document.getElementById('cancelEditBtn').style.display = 'inline-block';
    
    // Scroll al formulario
    document.querySelector('.form-container').scrollIntoView({ behavior: 'smooth' });
    
    showMessage(`✏️ Editando: ${empleado.nombre}`, 'info');
}

/**
 * Confirmar eliminación
 */
function confirmDeleteEmpleado(id) {
    const empleado = empleados.find(e => e.id === id);
    if (!empleado) {
        showMessage('❌ Empleado no encontrado', 'error');
        return;
    }
    
    showConfirmModal(
        `¿Está seguro que desea eliminar al empleado "${empleado.nombre}"?`,
        () => deleteEmpleado(id)
    );
}

/**
 * Eliminar empleado
 */
async function deleteEmpleado(id) {
    try {
        setLoading(true);
        
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showMessage('✅ Empleado eliminado correctamente', 'success');
            loadEmpleados();
            
            // Si estamos editando este empleado, cancelar la edición
            if (currentEditId === id) {
                clearForm();
            }
        } else {
            const error = await response.json();
            throw new Error(error.error || 'Error al eliminar empleado');
        }
        
    } catch (error) {
        console.error('❌ Error al eliminar empleado:', error);
        showMessage(`❌ Error: ${error.message}`, 'error');
    } finally {
        setLoading(false);
    }
}

/**
 * Limpiar formulario
 */
function clearForm() {
    document.getElementById('empleadoForm').reset();
    currentEditId = null;
    AppState.editMode = false;
    AppState.currentEmployee = null;
    
    // Restaurar interfaz
    document.getElementById('submitText').textContent = '💾 Crear Empleado';
    document.getElementById('cancelEditBtn').style.display = 'none';
    
    // Limpiar validaciones
    clearValidationStates();
    
    showMessage('📝 Formulario limpiado', 'info');
}

/**
 * Cancelar edición
 */
function cancelEdit() {
    clearForm();
    showMessage('❌ Edición cancelada', 'info');
}

/**
 * Manejar búsqueda
 */
function handleSearch() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase().trim();
    applyFilters();
}

/**
 * Manejar filtro por cargo
 */
function handleCargoFilter() {
    applyFilters();
}

/**
 * Aplicar filtros
 */
function applyFilters() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase().trim();
    const cargoFilter = document.getElementById('cargoFilter').value;
    
    filteredEmpleados = empleados.filter(empleado => {
        const matchesSearch = !searchTerm || 
            empleado.nombre.toLowerCase().includes(searchTerm) ||
            empleado.email.toLowerCase().includes(searchTerm);
        
        const matchesCargo = !cargoFilter || empleado.cargo === cargoFilter;
        
        return matchesSearch && matchesCargo;
    });
    
    renderEmpleadosTable();
    
    // Mostrar mensaje si no hay resultados
    if (filteredEmpleados.length === 0 && (searchTerm || cargoFilter)) {
        showTableMessage('🔍 No se encontraron empleados que coincidan con los filtros');
    }
}

/**
 * Validar formulario completo
 */
function validateForm() {
    const fields = ['nombre', 'cargo', 'salario', 'telefono', 'email'];
    let isValid = true;
    
    fields.forEach(fieldName => {
        if (!validateField(fieldName)) {
            isValid = false;
        }
    });
    
    return isValid;
}

/**
 * Validar campo individual
 */
function validateField(fieldName) {
    const field = document.getElementById(fieldName);
    const value = field.value.trim();
    const group = field.closest('.form-group');
    
    // Limpiar estado anterior
    group.classList.remove('error', 'success');
    
    let isValid = true;
    let errorMessage = '';
    
    switch (fieldName) {
        case 'nombre':
            if (!value) {
                errorMessage = 'El nombre es obligatorio';
                isValid = false;
            } else if (value.length < 2) {
                errorMessage = 'El nombre debe tener al menos 2 caracteres';
                isValid = false;
            } else if (!/^[a-zA-ZÀ-ÿ\s]+$/.test(value)) {
                errorMessage = 'El nombre solo puede contener letras y espacios';
                isValid = false;
            }
            break;
            
        case 'cargo':
            if (!value) {
                errorMessage = 'Debe seleccionar un cargo';
                isValid = false;
            }
            break;
            
        case 'salario':
            if (!value) {
                errorMessage = 'El salario es obligatorio';
                isValid = false;
            } else if (parseInt(value) < 1000000) {
                errorMessage = 'El salario debe ser al menos $1.000.000';
                isValid = false;
            } else if (parseInt(value) > 10000000) {
                errorMessage = 'El salario no puede exceder $10.000.000';
                isValid = false;
            }
            break;
            
        case 'telefono':
            if (!value) {
                errorMessage = 'El teléfono es obligatorio';
                isValid = false;
            } else if (!/^[\+]?[\s\-\(\)]?[\d\s\-\(\)]+$/.test(value)) {
                errorMessage = 'Formato de teléfono inválido';
                isValid = false;
            }
            break;
            
        case 'email':
            if (!value) {
                errorMessage = 'El email es obligatorio';
                isValid = false;
            } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
                errorMessage = 'Formato de email inválido';
                isValid = false;
            }
            break;
    }
    
    // Aplicar estado visual
    if (isValid) {
        group.classList.add('success');
    } else {
        group.classList.add('error');
        
        // Mostrar mensaje de error
        let errorElement = group.querySelector('.error-message');
        if (!errorElement) {
            errorElement = document.createElement('span');
            errorElement.className = 'error-message';
            group.appendChild(errorElement);
        }
        errorElement.textContent = errorMessage;
    }
    
    return isValid;
}

/**
 * Limpiar estados de validación
 */
function clearValidationStates() {
    const groups = document.querySelectorAll('.form-group');
    groups.forEach(group => {
        group.classList.remove('error', 'success');
        const errorMsg = group.querySelector('.error-message');
        if (errorMsg) {
            errorMsg.remove();
        }
    });
}

/**
 * Mostrar modal de confirmación
 */
function showConfirmModal(message, onConfirm) {
    document.getElementById('confirmMessage').textContent = message;
    document.getElementById('confirmModal').style.display = 'flex';
    
    // Configurar callback
    window.currentConfirmCallback = onConfirm;
}

/**
 * Ocultar modal de confirmación
 */
function hideConfirmModal() {
    document.getElementById('confirmModal').style.display = 'none';
    window.currentConfirmCallback = null;
}

/**
 * Manejar confirmación
 */
function handleConfirmYes() {
    if (window.currentConfirmCallback) {
        window.currentConfirmCallback();
    }
    hideConfirmModal();
}

/**
 * Mostrar modal de mensaje
 */
function showMessage(message, type = 'info') {
    const modal = document.getElementById('messageModal');
    const title = document.getElementById('messageTitle');
    const text = document.getElementById('messageText');
    
    // Configurar título según el tipo
    switch (type) {
        case 'success':
            title.textContent = '✅ Éxito';
            title.style.color = '#4CAF50';
            break;
        case 'error':
            title.textContent = '❌ Error';
            title.style.color = '#f44336';
            break;
        case 'warning':
            title.textContent = '⚠️ Advertencia';
            title.style.color = '#FF9800';
            break;
        default:
            title.textContent = '💡 Información';
            title.style.color = '#2196F3';
    }
    
    text.textContent = message;
    modal.style.display = 'flex';
    
    // Auto-ocultar después de 3 segundos para mensajes de éxito e info
    if (type === 'success' || type === 'info') {
        setTimeout(() => {
            hideMessageModal();
        }, 3000);
    }
}

/**
 * Ocultar modal de mensaje
 */
function hideMessageModal() {
    document.getElementById('messageModal').style.display = 'none';
}

/**
 * Mostrar mensaje en la tabla
 */
function showTableMessage(message) {
    const messageDiv = document.getElementById('tableMessage');
    messageDiv.querySelector('p').textContent = message;
    messageDiv.style.display = 'block';
}

/**
 * Configurar estado de carga
 */
function setLoading(isLoading) {
    AppState.isLoading = isLoading;
    const container = document.querySelector('.container');
    
    if (isLoading) {
        container.classList.add('loading');
    } else {
        container.classList.remove('loading');
    }
}

/**
 * Formatear número con separadores de miles
 */
function formatNumber(num) {
    return new Intl.NumberFormat('es-CO').format(num);
}

/**
 * Utilidad para debugging
 */
function logAppState() {
    console.log('📊 Estado de la aplicación:', {
        ...AppState,
        totalEmpleados: empleados.length,
        filteredEmpleados: filteredEmpleados.length,
        currentEditId
    });
}

// ================================
// SISTEMA DE AUTENTICACIÓN
// ================================

/**
 * Verificar autenticación del usuario
 */
function checkAuthentication() {
    try {
        const sessionData = localStorage.getItem('employeeSystemSession');
        const currentUser = sessionStorage.getItem('currentUser');
        
        if (!sessionData || !currentUser) {
            redirectToLogin();
            return false;
        }
        
        const session = JSON.parse(sessionData);
        const now = new Date();
        const expiry = new Date(session.expiryTime);
        
        if (now >= expiry) {
            logout();
            return false;
        }
        
        displayUserInfo(JSON.parse(currentUser));
        return true;
        
    } catch (error) {
        console.error('Error verificando autenticación:', error);
        redirectToLogin();
        return false;
    }
}

/**
 * Mostrar información del usuario en el header
 */
function displayUserInfo(user) {
    const userNameElement = document.getElementById('userName');
    const userRoleElement = document.getElementById('userRole');
    
    if (userNameElement && userRoleElement) {
        userNameElement.textContent = user.name || user.username;
        userRoleElement.textContent = user.role || 'Usuario';
    }
}

/**
 * Redirigir al login
 */
function redirectToLogin() {
    window.location.href = 'login.html';
}

/**
 * Cerrar sesión
 */
function logout() {
    if (confirm('¿Está seguro que desea cerrar sesión?')) {
        localStorage.removeItem('employeeSystemSession');
        sessionStorage.removeItem('currentUser');
        redirectToLogin();
    }
}