// Login Script
class LoginManager {
    constructor() {
        this.selectedSystem = null;
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.loadStoredSession();
    }

    setupEventListeners() {
        // System selection
        document.querySelectorAll('.system-option').forEach(option => {
            option.addEventListener('click', (e) => {
                this.selectSystem(e.currentTarget);
            });
        });

        // Login form
        document.getElementById('loginForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleLogin();
        });

        // Enter key on system selection
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' && this.selectedSystem) {
                this.showLoginForm();
            }
        });
    }

    selectSystem(optionElement) {
        // Remove previous selections
        document.querySelectorAll('.system-option').forEach(opt => {
            opt.classList.remove('selected');
        });

        // Select current system
        optionElement.classList.add('selected');
        this.selectedSystem = optionElement.dataset.system;

        // Show login form after a short delay
        setTimeout(() => {
            this.showLoginForm();
        }, 300);
    }

    showLoginForm() {
        if (!this.selectedSystem) return;

        const systemSelector = document.querySelector('.system-selector');
        const loginForm = document.getElementById('loginForm');
        const demoCredentials = document.getElementById('demoCredentials');

        systemSelector.style.display = 'none';
        loginForm.style.display = 'block';
        
        // Show demo credentials only for web system
        if (this.selectedSystem === 'empleados-web') {
            demoCredentials.style.display = 'block';
            this.loadAvailableUsers(); // Cargar usuarios dinámicamente
        } else {
            demoCredentials.style.display = 'none';
        }

        // Focus on username field
        document.getElementById('username').focus();
    }

    showSystemSelector() {
        const systemSelector = document.querySelector('.system-selector');
        const loginForm = document.getElementById('loginForm');
        const demoCredentials = document.getElementById('demoCredentials');

        systemSelector.style.display = 'block';
        loginForm.style.display = 'none';
        demoCredentials.style.display = 'none';
        
        this.selectedSystem = null;
        
        // Remove selections
        document.querySelectorAll('.system-option').forEach(opt => {
            opt.classList.remove('selected');
        });

        // Clear form
        document.getElementById('loginForm').reset();
        this.clearMessages();
    }

    async handleLogin() {
        const email = document.getElementById('username').value.trim();
        const nombre = document.getElementById('password').value.trim();

        if (!email || !nombre) {
            this.showError('Por favor complete todos los campos');
            return;
        }

        this.showLoading(true);
        this.clearMessages();

        try {
            const result = await this.authenticate(email, nombre);
            
            if (result.success) {
                this.showSuccess('Inicio de sesión exitoso');
                this.storeSession(result.user, this.selectedSystem);
                
                setTimeout(() => {
                    this.redirectToSystem();
                }, 1500);
            } else {
                this.showError(result.message || 'Credenciales incorrectas');
            }
        } catch (error) {
            console.error('Login error:', error);
            this.showError('Error de conexión. Verifique que el servidor esté funcionando.');
        } finally {
            this.showLoading(false);
        }
    }

    async authenticate(email, nombre) {
        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    name: nombre
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            return data;
            
        } catch (error) {
            console.error('Error en autenticación:', error);
            throw error;
        }
    }

    redirectToSystem() {
        switch (this.selectedSystem) {
            case 'empleados-web':
                window.location.href = 'empleados.html';
                break;
            case 'empleados-desktop':
                this.showDesktopDownloadOptions();
                break;
            case 'usuarios-desktop':
                this.showError('Sistema desktop no disponible desde web. Use la aplicación de escritorio.');
                break;
            default:
                this.showError('Sistema no reconocido');
        }
    }

    storeSession(user, system) {
        const sessionData = {
            user: user,
            system: system,
            loginTime: new Date().toISOString(),
            expiryTime: new Date(Date.now() + 8 * 60 * 60 * 1000).toISOString() // 8 horas
        };
        
        localStorage.setItem('employeeSystemSession', JSON.stringify(sessionData));
        sessionStorage.setItem('currentUser', JSON.stringify(user));
    }

    loadStoredSession() {
        try {
            const sessionData = localStorage.getItem('employeeSystemSession');
            if (sessionData) {
                const session = JSON.parse(sessionData);
                const now = new Date();
                const expiry = new Date(session.expiryTime);
                
                if (now < expiry && session.system === 'empleados-web') {
                    // Sesión válida, redirigir automáticamente
                    sessionStorage.setItem('currentUser', JSON.stringify(session.user));
                    window.location.href = 'empleados.html';
                }
            }
        } catch (error) {
            console.log('No hay sesión almacenada o es inválida');
        }
    }

    showLoading(show) {
        const loading = document.getElementById('loading');
        loading.style.display = show ? 'flex' : 'none';
    }

    showError(message) {
        this.clearMessages();
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.innerHTML = `
            <i class="material-icons">error</i>
            ${message}
        `;
        
        const form = document.getElementById('loginForm');
        form.insertBefore(errorDiv, form.firstChild);
    }

    showSuccess(message) {
        this.clearMessages();
        const successDiv = document.createElement('div');
        successDiv.className = 'success-message';
        successDiv.innerHTML = `
            <i class="material-icons">check_circle</i>
            ${message}
        `;
        
        const form = document.getElementById('loginForm');
        form.insertBefore(successDiv, form.firstChild);
    }

    clearMessages() {
        const messages = document.querySelectorAll('.error-message, .success-message');
        messages.forEach(msg => msg.remove());
        
        // Clear input error states
        document.querySelectorAll('.input-group').forEach(group => {
            group.classList.remove('error');
        });
    }

    logout() {
        localStorage.removeItem('employeeSystemSession');
        sessionStorage.removeItem('currentUser');
        window.location.href = 'login.html';
    }

    async loadAvailableUsers() {
        try {
            const response = await fetch('/api/auth/users');
            if (response.ok) {
                const users = await response.json();
                this.updateDemoCredentials(users);
            }
        } catch (error) {
            console.log('No se pudieron cargar los usuarios:', error);
        }
    }

    updateDemoCredentials(users) {
        const demoCredentials = document.getElementById('demoCredentials');
        if (!demoCredentials || !users || users.length === 0) return;

        let html = '<h4>Usuarios disponibles del sistema:</h4>';
        
        users.forEach(user => {
            html += `
                <div class="credential-item">
                    <strong>Email:</strong> ${user.email}
                    <strong>Nombre:</strong> ${user.name}
                </div>
            `;
        });

        demoCredentials.innerHTML = html;
    }

    showDesktopDownloadOptions() {
        const loginForm = document.getElementById('loginForm');
        
        // Ocultar el formulario de login
        loginForm.style.display = 'none';
        
        // Crear modal de descarga
        this.createDownloadModal();
    }

    createDownloadModal() {
        // Crear el modal
        const modal = document.createElement('div');
        modal.className = 'download-modal';
        modal.id = 'downloadModal';
        
        modal.innerHTML = `
            <div class="download-content">
                <div class="download-header">
                    <i class="material-icons">get_app</i>
                    <h3>Descargar Aplicación de Escritorio</h3>
                    <p>Sistema de Empleados - Versión Desktop</p>
                </div>
                
                <div class="download-options">
                    <div class="download-option" onclick="loginManager.downloadJar()">
                        <div class="option-icon">
                            <i class="material-icons">archive</i>
                        </div>
                        <div class="option-details">
                            <h4>Descargar JAR Ejecutable</h4>
                            <p>Archivo Java ejecutable independiente</p>
                            <small>Requiere Java 11+ instalado</small>
                        </div>
                        <i class="material-icons arrow">arrow_forward_ios</i>
                    </div>
                    
                    <div class="download-option" onclick="loginManager.downloadLauncher()">
                        <div class="option-icon">
                            <i class="material-icons">launch</i>
                        </div>
                        <div class="option-details">
                            <h4>Descargar Launcher</h4>
                            <p>Script que descarga y ejecuta automáticamente</p>
                            <small>Descarga la aplicación cuando sea necesario</small>
                        </div>
                        <i class="material-icons arrow">arrow_forward_ios</i>
                    </div>
                </div>
                
                <div class="download-instructions">
                    <div class="instruction">
                        <i class="material-icons">info</i>
                        <span>Asegúrate de tener Java 11 o superior instalado</span>
                    </div>
                    <div class="instruction">
                        <i class="material-icons">security</i>
                        <span>La aplicación se ejecuta localmente y es completamente segura</span>
                    </div>
                </div>
                
                <div class="download-actions">
                    <button type="button" class="back-btn" onclick="loginManager.closeDownloadModal()">
                        <i class="material-icons">arrow_back</i>
                        <span>Volver</span>
                    </button>
                    <button type="button" class="web-btn" onclick="loginManager.goToWebVersion()">
                        <i class="material-icons">web</i>
                        <span>Usar Versión Web</span>
                    </button>
                </div>
            </div>
        `;
        
        document.body.appendChild(modal);
        
        // Mostrar modal con animación
        setTimeout(() => {
            modal.classList.add('show');
        }, 10);
    }

    downloadJar() {
        this.showLoading(true);
        this.showSuccess('Iniciando descarga del JAR ejecutable...');
        
        // Crear enlace de descarga
        const link = document.createElement('a');
        link.href = '/api/desktop/download';
        link.download = 'SupermercadoMVC-Desktop.jar';
        link.style.display = 'none';
        
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        setTimeout(() => {
            this.showLoading(false);
            this.showSuccess('¡Descarga completada! Ejecuta el archivo JAR para iniciar la aplicación.');
        }, 2000);
    }

    downloadLauncher() {
        this.showLoading(true);
        this.showSuccess('Iniciando descarga del launcher...');
        
        // Crear enlace de descarga
        const link = document.createElement('a');
        link.href = '/api/desktop/launcher';
        link.download = 'launcher.bat';
        link.style.display = 'none';
        
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        setTimeout(() => {
            this.showLoading(false);
            this.showSuccess('¡Descarga completada! Ejecuta el archivo launcher.bat para iniciar.');
        }, 2000);
    }

    closeDownloadModal() {
        const modal = document.getElementById('downloadModal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => {
                modal.remove();
            }, 300);
        }
        
        // Volver a mostrar el selector de sistemas
        this.showSystemSelector();
    }

    goToWebVersion() {
        this.closeDownloadModal();
        // Cambiar a sistema web y proceder con login
        this.selectedSystem = 'empleados-web';
        this.showLoginForm();
    }
}

// Global functions for HTML onclick events
function showSystemSelector() {
    loginManager.showSystemSelector();
}

// Initialize login manager when DOM is loaded
let loginManager;
document.addEventListener('DOMContentLoaded', () => {
    loginManager = new LoginManager();
});

// Export for potential external use
window.LoginManager = LoginManager;