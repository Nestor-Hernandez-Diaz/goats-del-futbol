/**
 * Manejador del menú de navegación para usuarios autenticados y no autenticados
 * GOATs del Fútbol
 */

document.addEventListener('DOMContentLoaded', function() {
    const userMenu = document.getElementById('userMenu');
    const authButtons = document.getElementById('authButtons');
    
    // Verificar estado de autenticación
    const token = localStorage.getItem('jwtToken');
    const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
    
    if (token && currentUser.username) {
        // Usuario autenticado: mostrar userMenu, ocultar authButtons
        if (userMenu) {
            userMenu.style.display = 'flex';
            // Actualizar nombre de usuario
            const usernameSpan = userMenu.querySelector('#username');
            if (usernameSpan) {
                usernameSpan.textContent = currentUser.username;
            }
        }
        if (authButtons) authButtons.style.display = 'none';
    } else {
        // Usuario no autenticado: mostrar authButtons, ocultar userMenu
        if (userMenu) userMenu.style.display = 'none';
        if (authButtons) authButtons.style.display = 'flex';
    }
    
    // Manejar dropdown de usuario autenticado si existe
    const userDropdown = document.getElementById('userDropdown');
    const dropdownMenu = document.getElementById('dropdownMenu');
    
    if (userDropdown && dropdownMenu) {
        userDropdown.addEventListener('click', function(e) {
            e.stopPropagation();
            dropdownMenu.classList.toggle('show');
        });
        
        document.addEventListener('click', function(e) {
            if (userMenu && !userMenu.contains(e.target)) {
                dropdownMenu.classList.remove('show');
            }
        });
    }
    
    // Manejar logout
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('currentUser');
            window.location.reload();
        });
    }
});
