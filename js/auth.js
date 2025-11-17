/**
 * Sistema de Autenticación JWT - GOATs del Fútbol
 * Manejo de sesión de usuario en navbar
 */

// Verificar que no esté ya cargado
if (typeof window.authSystemLoaded === 'undefined') {
    console.log('Cargando sistema de autenticación...');
    window.authSystemLoaded = true;

    // Estado global
    let currentUser = null;

    /**
     * Detecta si estamos en la raíz (index.html) o en subdirectorio (pages/*)
     */
    function isRootPage() {
        const path = window.location.pathname;
        return path.endsWith('index.html') || path.endsWith('proyecto-goats-futbol/') || path.endsWith('proyecto-goats-futbol');
    }

    /**
     * Obtiene la ruta correcta para login.html según ubicación
     */
    function getLoginUrl() {
        return isRootPage() ? 'pages/login.html' : '../pages/login.html';
    }

    /**
     * Obtiene la ruta correcta para dashboard según ubicación
     */
    function getDashboardUrl() {
        return isRootPage() ? 'pages/admin.html' : 'admin.html';
    }

    /**
     * Inicialización al cargar la página
     */
    document.addEventListener('DOMContentLoaded', function() {
        console.log('Inicializando sistema de autenticación...');
        checkLoginStatus();
        setupAuthListeners();
    });

    /**
     * Verifica el estado de login del usuario
     */
    function checkLoginStatus() {
        const token = localStorage.getItem('jwtToken');
        
        if (token) {
            try {
                const payload = parseJWT(token);
                
                // Verificar si el token ha expirado
                if (isTokenExpired(payload)) {
                    console.log('Token expirado, cerrando sesión...');
                    logout();
                    return;
                }
                
                // Token válido, extraer información del usuario
                currentUser = {
                    username: payload.sub,
                    roles: payload.roles || payload.authorities || [],
                    userId: payload.userId || null,
                    exp: payload.exp
                };
                
                console.log('Usuario autenticado:', currentUser.username);
                showUserMenu();
            } catch (error) {
                console.error('Error al decodificar token:', error);
                logout();
            }
        } else {
            console.log('Usuario no autenticado');
            showLoginButton();
        }
    }

    /**
     * Parsea un JWT token
     */
    function parseJWT(token) {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
            return JSON.parse(jsonPayload);
        } catch (error) {
            throw new Error('Token JWT inválido');
        }
    }

    /**
     * Verifica si el token ha expirado
     */
    function isTokenExpired(payload) {
        if (!payload.exp) return false;
        const currentTime = Math.floor(Date.now() / 1000);
        return currentTime > payload.exp;
    }

    /**
     * Muestra el menú de usuario autenticado
     */
    function showUserMenu() {
        const navLinks = document.querySelector('.enlaces-navegacion');
        if (!navLinks) return;

        // Verificar si ya existe el menú de usuario
        let userMenuItem = navLinks.querySelector('.user-menu-item');
        if (userMenuItem) {
            userMenuItem.remove();
        }

        // Crear elemento de menú de usuario
        const userMenu = document.createElement('li');
        userMenu.className = 'user-menu-item';
        
        const isAdmin = currentUser.roles.includes('ROLE_ADMIN');
        
        userMenu.innerHTML = `
            <div class="user-menu-trigger">
                <i class="fas fa-user-circle"></i>
                <span class="username">${escapeHtml(currentUser.username)}</span>
                <i class="fas fa-chevron-down"></i>
            </div>
            <div class="user-dropdown">
                <div class="dropdown-header">
                    <strong>${escapeHtml(currentUser.username)}</strong>
                    ${isAdmin ? '<span class="badge-admin">Admin</span>' : '<span class="badge-user">Usuario</span>'}
                </div>
                <div class="dropdown-divider"></div>
                ${isAdmin ? `<a href="${getDashboardUrl()}" class="dropdown-item"><i class="fas fa-shield-alt"></i> Dashboard Admin</a>` : ''}
                <a href="#" class="dropdown-item" id="btn-logout">
                    <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                </a>
            </div>
        `;

        navLinks.appendChild(userMenu);

        // Configurar eventos del menú
        setupUserMenuEvents(userMenu);
    }

    /**
     * Configura eventos del menú de usuario
     */
    function setupUserMenuEvents(userMenu) {
        const trigger = userMenu.querySelector('.user-menu-trigger');
        const dropdown = userMenu.querySelector('.user-dropdown');
        
        // Toggle del dropdown
        trigger.addEventListener('click', function(e) {
            e.stopPropagation();
            dropdown.classList.toggle('show');
        });

        // Cerrar dropdown al hacer click fuera
        document.addEventListener('click', function(e) {
            if (!userMenu.contains(e.target)) {
                dropdown.classList.remove('show');
            }
        });

        // Evento de logout
        const logoutBtn = userMenu.querySelector('#btn-logout');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', function(e) {
                e.preventDefault();
                logout();
            });
        }
    }

    /**
     * Muestra el botón de login
     */
    function showLoginButton() {
        const navLinks = document.querySelector('.enlaces-navegacion');
        if (!navLinks) return;

        // Verificar si ya existe el enlace de login
        let loginItem = navLinks.querySelector('.login-item');
        if (loginItem) {
            loginItem.remove();
        }

        // Crear enlace de navegación estándar
        const loginLink = document.createElement('li');
        loginLink.innerHTML = `<a href="${getLoginUrl()}">Acceder</a>`;

        navLinks.appendChild(loginLink);
    }

    /**
     * Cierra la sesión del usuario
     */
    function logout() {
        // Limpiar localStorage
        localStorage.removeItem('jwtToken');
        
        // Limpiar estado
        currentUser = null;
        
        console.log('Sesión cerrada');
        
        // Mostrar notificación
        showNotification('Sesión cerrada correctamente', 'info');
        
        // Recargar la página después de un momento
        setTimeout(function() {
            window.location.reload();
        }, 1000);
    }

    /**
     * Configura listeners adicionales
     */
    function setupAuthListeners() {
        // Listener para cambios en localStorage (sincronización entre pestañas)
        window.addEventListener('storage', function(e) {
            if (e.key === 'jwtToken') {
                if (e.newValue === null) {
                    // Token removido, recargar
                    window.location.reload();
                } else if (e.oldValue === null) {
                    // Token agregado, recargar
                    window.location.reload();
                }
            }
        });

        // Verificar token cada 5 minutos
        setInterval(function() {
            const token = localStorage.getItem('jwtToken');
            if (token) {
                try {
                    const payload = parseJWT(token);
                    if (isTokenExpired(payload)) {
                        console.log('Token expirado durante verificación periódica');
                        logout();
                    }
                } catch (error) {
                    console.error('Error en verificación periódica:', error);
                    logout();
                }
            }
        }, 5 * 60 * 1000); // 5 minutos
    }

    /**
     * Muestra una notificación temporal
     */
    function showNotification(message, type = 'info') {
        // Remover notificación existente
        const existingNotif = document.querySelector('.auth-notification');
        if (existingNotif) {
            existingNotif.remove();
        }

        const notification = document.createElement('div');
        notification.className = `auth-notification notification-${type}`;
        
        let icon = 'fa-info-circle';
        if (type === 'success') icon = 'fa-check-circle';
        if (type === 'error') icon = 'fa-exclamation-circle';
        
        notification.innerHTML = `
            <i class="fas ${icon}"></i>
            <span>${message}</span>
        `;

        document.body.appendChild(notification);

        // Mostrar notificación
        setTimeout(function() {
            notification.classList.add('show');
        }, 100);

        // Ocultar y remover después de 3 segundos
        setTimeout(function() {
            notification.classList.remove('show');
            setTimeout(function() {
                notification.remove();
            }, 300);
        }, 3000);
    }

    /**
     * Escapa HTML para prevenir XSS
     */
    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    /**
     * Obtiene el usuario actual
     */
    function getCurrentUser() {
        return currentUser;
    }

    /**
     * Verifica si el usuario tiene un rol específico
     */
    function hasRole(role) {
        if (!currentUser) return false;
        return currentUser.roles.includes(role);
    }

    /**
     * Verifica si el usuario es administrador
     */
    function isAdmin() {
        return hasRole('ROLE_ADMIN');
    }

    // Exponer funciones globalmente si es necesario
    window.authSystem = {
        getCurrentUser: getCurrentUser,
        hasRole: hasRole,
        isAdmin: isAdmin,
        logout: logout,
        checkLoginStatus: checkLoginStatus
    };

} // Fin de verificación de carga
