// API Configuration
const API_BASE_URL = 'http://localhost:9090/api';
const API_ENDPOINTS = {
    users: `${API_BASE_URL}/users`,
    health: `${API_BASE_URL}/health`
};

// Global State
let users = [];
let filteredUsers = [];
let editingUserId = null;

// DOM Elements
const loadingIndicator = document.getElementById('loadingIndicator');
const usersGrid = document.getElementById('usersGrid');
const emptyState = document.getElementById('emptyState');
const searchInput = document.getElementById('searchInput');
const userModal = document.getElementById('userModal');
const confirmModal = document.getElementById('confirmModal');
const userForm = document.getElementById('userForm');
const apiStatus = document.getElementById('apiStatus');

// Initialize Application
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();
    checkApiStatus();
    loadUsers();
});

// Event Listeners
function initializeEventListeners() {
    // Header actions
    document.getElementById('refreshBtn').addEventListener('click', refreshUsers);
    document.getElementById('addUserBtn').addEventListener('click', () => openUserModal());
    
    // Search functionality
    searchInput.addEventListener('input', handleSearch);
    document.getElementById('clearSearch').addEventListener('click', clearSearch);
    
    // Form submission
    userForm.addEventListener('submit', handleFormSubmit);
    
    // Modal close events
    document.addEventListener('click', handleModalClose);
    document.addEventListener('keydown', handleEscapeKey);
}

// API Status Check
async function checkApiStatus() {
    try {
        const response = await fetch(API_ENDPOINTS.health);
        if (response.ok) {
            updateApiStatus(true);
        } else {
            updateApiStatus(false);
        }
    } catch (error) {
        console.error('API Status check failed:', error);
        updateApiStatus(false);
    }
}

function updateApiStatus(isOnline) {
    const statusElement = apiStatus;
    statusElement.className = `status-indicator ${isOnline ? 'online' : 'offline'}`;
    statusElement.innerHTML = `
        <i class="fas fa-circle"></i>
        ${isOnline ? 'API Conectada' : 'API Desconectada'}
    `;
}

// User Management Functions
async function loadUsers() {
    showLoading(true);
    try {
        const response = await fetch(API_ENDPOINTS.users);
        if (response.ok) {
            users = await response.json();
            filteredUsers = [...users];
            renderUsers();
            updateApiStatus(true);
        } else {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
    } catch (error) {
        console.error('Error loading users:', error);
        showToast('Error', 'No se pudieron cargar los usuarios', 'error');
        updateApiStatus(false);
        showEmptyState();
    } finally {
        showLoading(false);
    }
}

async function createUser(userData) {
    try {
        const response = await fetch(API_ENDPOINTS.users, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });
        
        if (response.ok) {
            const newUser = await response.json();
            showToast('Éxito', 'Usuario creado exitosamente', 'success');
            await loadUsers(); // Refresh the list
            return newUser;
        } else {
            const errorText = await response.text();
            throw new Error(`Error ${response.status}: ${errorText}`);
        }
    } catch (error) {
        console.error('Error creating user:', error);
        showToast('Error', 'No se pudo crear el usuario', 'error');
        throw error;
    }
}

async function updateUser(id, userData) {
    try {
        const response = await fetch(`${API_ENDPOINTS.users}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });
        
        if (response.ok) {
            const updatedUser = await response.json();
            showToast('Éxito', 'Usuario actualizado exitosamente', 'success');
            await loadUsers(); // Refresh the list
            return updatedUser;
        } else {
            const errorText = await response.text();
            throw new Error(`Error ${response.status}: ${errorText}`);
        }
    } catch (error) {
        console.error('Error updating user:', error);
        showToast('Error', 'No se pudo actualizar el usuario', 'error');
        throw error;
    }
}

async function deleteUser(id) {
    try {
        const response = await fetch(`${API_ENDPOINTS.users}/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showToast('Éxito', 'Usuario eliminado exitosamente', 'success');
            await loadUsers(); // Refresh the list
        } else {
            const errorText = await response.text();
            throw new Error(`Error ${response.status}: ${errorText}`);
        }
    } catch (error) {
        console.error('Error deleting user:', error);
        showToast('Error', 'No se pudo eliminar el usuario', 'error');
        throw error;
    }
}

// UI Rendering Functions
function renderUsers() {
    if (filteredUsers.length === 0) {
        showEmptyState();
        return;
    }
    
    hideEmptyState();
    
    usersGrid.innerHTML = filteredUsers.map(user => `
        <div class="user-card" data-user-id="${user.id}">
            <div class="user-avatar">
                ${getInitials(user.name)}
            </div>
            <div class="user-info">
                <h3>${escapeHtml(user.name)}</h3>
                <div class="email">
                    <i class="fas fa-envelope"></i>
                    <span>${escapeHtml(user.email)}</span>
                </div>
                <div class="phone">
                    <i class="fas fa-phone"></i>
                    <span>${escapeHtml(user.phone)}</span>
                </div>
                ${user.address ? `
                    <div class="address">
                        <i class="fas fa-map-marker-alt"></i>
                        <span>${escapeHtml(user.address)}</span>
                    </div>
                ` : ''}
            </div>
            <div class="user-actions">
                <button class="action-btn edit" onclick="editUser(${user.id})" title="Editar usuario">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="action-btn delete" onclick="confirmDeleteUser(${user.id}, '${escapeHtml(user.name)}')" title="Eliminar usuario">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
    `).join('');
}

function showEmptyState() {
    usersGrid.style.display = 'none';
    emptyState.style.display = 'block';
}

function hideEmptyState() {
    usersGrid.style.display = 'grid';
    emptyState.style.display = 'none';
}

function showLoading(show) {
    loadingIndicator.style.display = show ? 'block' : 'none';
    if (show) {
        hideEmptyState();
        usersGrid.style.display = 'none';
    }
}

// Utility Functions
function getInitials(name) {
    return name
        .split(' ')
        .map(word => word.charAt(0).toUpperCase())
        .slice(0, 2)
        .join('');
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Search Functionality
function handleSearch(event) {
    const searchTerm = event.target.value.toLowerCase().trim();
    
    if (!searchTerm) {
        filteredUsers = [...users];
    } else {
        filteredUsers = users.filter(user => 
            user.name.toLowerCase().includes(searchTerm) ||
            user.email.toLowerCase().includes(searchTerm) ||
            user.phone.toLowerCase().includes(searchTerm) ||
            (user.address && user.address.toLowerCase().includes(searchTerm))
        );
    }
    
    renderUsers();
}

function clearSearch() {
    searchInput.value = '';
    filteredUsers = [...users];
    renderUsers();
}

function refreshUsers() {
    clearSearch();
    loadUsers();
}

// Modal Management
function openUserModal(userId = null) {
    editingUserId = userId;
    const isEditing = userId !== null;
    
    // Update modal title
    document.getElementById('modalTitle').innerHTML = `
        <i class="fas fa-user"></i>
        ${isEditing ? 'Editar Usuario' : 'Nuevo Usuario'}
    `;
    
    // Reset form
    userForm.reset();
    clearFormErrors();
    
    // If editing, populate form with user data
    if (isEditing) {
        const user = users.find(u => u.id === userId);
        if (user) {
            document.getElementById('userId').value = user.id;
            document.getElementById('userName').value = user.name;
            document.getElementById('userEmail').value = user.email;
            document.getElementById('userPhone').value = user.phone;
            document.getElementById('userAddress').value = user.address || '';
        }
    }
    
    // Show modal
    userModal.classList.add('show');
    document.body.style.overflow = 'hidden';
    
    // Focus first input
    setTimeout(() => {
        document.getElementById('userName').focus();
    }, 100);
}

function closeUserModal() {
    userModal.classList.remove('show');
    document.body.style.overflow = '';
    editingUserId = null;
    clearFormErrors();
}

function openConfirmModal(title, message, onConfirm) {
    document.getElementById('confirmTitle').innerHTML = title;
    document.getElementById('confirmMessage').textContent = message;
    
    const confirmBtn = document.getElementById('confirmAction');
    confirmBtn.onclick = () => {
        closeConfirmModal();
        onConfirm();
    };
    
    confirmModal.classList.add('show');
    document.body.style.overflow = 'hidden';
}

function closeConfirmModal() {
    confirmModal.classList.remove('show');
    document.body.style.overflow = '';
}

// User Actions
function editUser(userId) {
    openUserModal(userId);
}

function confirmDeleteUser(userId, userName) {
    openConfirmModal(
        '<i class="fas fa-exclamation-triangle"></i> Confirmar Eliminación',
        `¿Estás seguro de que deseas eliminar a "${userName}"? Esta acción no se puede deshacer.`,
        () => deleteUser(userId)
    );
}

// Form Handling
async function handleFormSubmit(event) {
    event.preventDefault();
    
    if (!validateForm()) {
        return;
    }
    
    const saveBtn = document.getElementById('saveUserBtn');
    const originalText = saveBtn.innerHTML;
    
    // Show loading state
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Guardando...';
    
    try {
        const formData = {
            name: document.getElementById('userName').value.trim(),
            email: document.getElementById('userEmail').value.trim(),
            phone: document.getElementById('userPhone').value.trim(),
            address: document.getElementById('userAddress').value.trim() || null
        };
        
        if (editingUserId) {
            await updateUser(editingUserId, formData);
        } else {
            await createUser(formData);
        }
        
        closeUserModal();
        
    } catch (error) {
        // Error is already handled in the API functions
    } finally {
        // Reset button state
        saveBtn.disabled = false;
        saveBtn.innerHTML = originalText;
    }
}

// Form Validation
function validateForm() {
    const name = document.getElementById('userName').value.trim();
    const email = document.getElementById('userEmail').value.trim();
    const phone = document.getElementById('userPhone').value.trim();
    
    let isValid = true;
    
    // Clear previous errors
    clearFormErrors();
    
    // Validate name
    if (!name) {
        showFieldError('userName', 'El nombre es requerido');
        isValid = false;
    } else if (name.length < 2) {
        showFieldError('userName', 'El nombre debe tener al menos 2 caracteres');
        isValid = false;
    }
    
    // Validate email
    if (!email) {
        showFieldError('userEmail', 'El email es requerido');
        isValid = false;
    } else if (!isValidEmail(email)) {
        showFieldError('userEmail', 'Ingrese un email válido');
        isValid = false;
    }
    
    // Validate phone
    if (!phone) {
        showFieldError('userPhone', 'El teléfono es requerido');
        isValid = false;
    } else if (!isValidPhone(phone)) {
        showFieldError('userPhone', 'Ingrese un teléfono válido (ej: 3001234567)');
        isValid = false;
    }
    
    return isValid;
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function isValidPhone(phone) {
    // Colombian phone format: 10 digits or +57 followed by 10 digits
    const phoneRegex = /^(\+?57\s?)?[0-9]{10}$|^[0-9]{10}$/;
    return phoneRegex.test(phone.replace(/\s/g, ''));
}

function showFieldError(fieldId, message) {
    const field = document.getElementById(fieldId);
    const formGroup = field.closest('.form-group');
    const errorElement = formGroup.querySelector('.error-message');
    
    formGroup.classList.add('error');
    errorElement.textContent = message;
    errorElement.classList.add('show');
}

function clearFormErrors() {
    const errorElements = document.querySelectorAll('.error-message');
    const formGroups = document.querySelectorAll('.form-group');
    
    errorElements.forEach(element => {
        element.classList.remove('show');
        element.textContent = '';
    });
    
    formGroups.forEach(group => {
        group.classList.remove('error');
    });
}

// Event Handlers
function handleModalClose(event) {
    if (event.target === userModal) {
        closeUserModal();
    }
    if (event.target === confirmModal) {
        closeConfirmModal();
    }
}

function handleEscapeKey(event) {
    if (event.key === 'Escape') {
        if (userModal.classList.contains('show')) {
            closeUserModal();
        }
        if (confirmModal.classList.contains('show')) {
            closeConfirmModal();
        }
    }
}

// Toast Notifications
function showToast(title, message, type = 'info') {
    const toastContainer = document.getElementById('toastContainer');
    const toastId = 'toast-' + Date.now();
    
    const iconMap = {
        success: 'fas fa-check-circle',
        error: 'fas fa-exclamation-circle',
        warning: 'fas fa-exclamation-triangle',
        info: 'fas fa-info-circle'
    };
    
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.id = toastId;
    toast.innerHTML = `
        <div class="toast-icon">
            <i class="${iconMap[type]}"></i>
        </div>
        <div class="toast-content">
            <div class="toast-title">${title}</div>
            <div class="toast-message">${message}</div>
        </div>
        <button class="toast-close" onclick="removeToast('${toastId}')">
            <i class="fas fa-times"></i>
        </button>
    `;
    
    toastContainer.appendChild(toast);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        removeToast(toastId);
    }, 5000);
}

function removeToast(toastId) {
    const toast = document.getElementById(toastId);
    if (toast) {
        toast.style.animation = 'slideInRight 0.3s ease reverse';
        setTimeout(() => {
            toast.remove();
        }, 300);
    }
}

// Global functions for HTML onclick handlers
window.editUser = editUser;
window.confirmDeleteUser = confirmDeleteUser;
window.openUserModal = openUserModal;
window.closeUserModal = closeUserModal;
window.closeConfirmModal = closeConfirmModal;
window.removeToast = removeToast;
