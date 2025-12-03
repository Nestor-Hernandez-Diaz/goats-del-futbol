/**
 * profile.js - Gestión de Perfil de Usuario
 * 
 * Carga y muestra información real del usuario y sus estadísticas
 */

const API_BASE_URL = 'http://localhost:8080/api';

console.log('🚀 Cargando sistema de perfil...');

/**
 * Cargar información del perfil
 */
async function loadProfile() {
    try {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            window.location.href = 'login.html';
            return;
        }

        // Decodificar token para obtener información básica
        const payload = JSON.parse(atob(token.split('.')[1]));
        const username = payload.sub;
        const userId = payload.userId;
        const roles = payload.roles || [];

        console.log('Usuario:', username, '| ID:', userId);

        // Actualizar información básica desde el token
        document.getElementById('profileUsername').textContent = username;
        document.getElementById('infoUsername').textContent = username;

        // Determinar rol
        const isAdmin = roles.includes('ROLE_ADMIN');
        const roleName = isAdmin ? 'Administrador' : 'Usuario';
        document.getElementById('profileRole').textContent = roleName;
        document.getElementById('infoRole').textContent = roleName;

        if (isAdmin) {
            document.getElementById('profileRole').style.background = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
        }

        // Obtener información completa del usuario
        const userResponse = await fetch(`${API_BASE_URL}/auth/me`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (userResponse.ok) {
            const userData = await userResponse.json();
            
            // Actualizar email
            if (userData.email) {
                document.getElementById('profileEmail').textContent = userData.email;
                document.getElementById('infoEmail').textContent = userData.email;
            }

            // Actualizar fecha de creación
            if (userData.createdAt) {
                const memberSince = new Date(userData.createdAt).toLocaleDateString('es-ES', {
                    day: 'numeric',
                    month: 'long',
                    year: 'numeric'
                });
                document.getElementById('infoMemberSince').textContent = memberSince;
            }
        }

        // Cargar estadísticas de actividad
        await loadUserStats(userId, token);

    } catch (error) {
        console.error('Error al cargar perfil:', error);
        showNotification('Error al cargar la información del perfil', 'error');
    }
}

/**
 * Cargar estadísticas del usuario
 */
async function loadUserStats(userId, token) {
    try {
        // Cargar comentarios del usuario usando el endpoint específico
        const commentsResponse = await fetch(`${API_BASE_URL}/comments/user/${userId}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        let totalComments = 0;
        if (commentsResponse.ok) {
            const commentsData = await commentsResponse.json();
            totalComments = Array.isArray(commentsData) ? commentsData.length : 
                           (commentsData.content ? commentsData.content.length : 0);
        }

        // Cargar suscripciones ACTIVAS
        const subscriptionsResponse = await fetch(`${API_BASE_URL}/subscriptions/user/${userId}?active=true`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        let totalSubscriptions = 0;
        if (subscriptionsResponse.ok) {
            const subscriptionsData = await subscriptionsResponse.json();
            // El endpoint devuelve un objeto Page con content array
            totalSubscriptions = subscriptionsData.content ? subscriptionsData.content.length : 0;
            console.log(`✅ Suscripciones activas: ${totalSubscriptions}`);
        }

        // Cargar notificaciones NO LEÍDAS
        const notificationsResponse = await fetch(`${API_BASE_URL}/notifications?unreadOnly=true`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        let totalNotifications = 0;
        if (notificationsResponse.ok) {
            const notifications = await notificationsResponse.json();
            totalNotifications = notifications.content ? notifications.content.length : notifications.length;
            console.log(`✅ Notificaciones no leídas: ${totalNotifications}`);
        }

        // Actualizar UI con animación
        animateNumber('statsComments', totalComments);
        animateNumber('statsSubscriptions', totalSubscriptions);
        animateNumber('statsNotifications', totalNotifications);

        console.log('✓ Estadísticas cargadas:', {
            comentarios: totalComments,
            suscripciones: totalSubscriptions,
            notificaciones: totalNotifications
        });

    } catch (error) {
        console.error('Error al cargar estadísticas:', error);
        // Mostrar 0 en caso de error
        document.getElementById('statsComments').textContent = '0';
        document.getElementById('statsSubscriptions').textContent = '0';
        document.getElementById('statsNotifications').textContent = '0';
    }
}

/**
 * Animar cambio de número
 */
function animateNumber(elementId, targetValue) {
    const element = document.getElementById(elementId);
    const duration = 1000; // 1 segundo
    const steps = 30;
    const increment = targetValue / steps;
    let current = 0;
    let step = 0;

    const timer = setInterval(() => {
        step++;
        current += increment;
        
        if (step >= steps) {
            current = targetValue;
            clearInterval(timer);
        }
        
        element.textContent = Math.floor(current);
    }, duration / steps);
}

/**
 * Mostrar notificación
 */
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        background: ${type === 'error' ? '#ff6b6b' : '#4CAF50'};
        color: white;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        z-index: 10000;
        animation: slideInRight 0.3s ease-out;
    `;
    notification.textContent = message;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.3s ease-out';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

/**
 * Inicializar perfil
 */
document.addEventListener('DOMContentLoaded', () => {
    console.log('Inicializando perfil de usuario...');
    loadProfile();
});

// Escuchar cambios de suscripción desde otras páginas para actualizar estadísticas
window.addEventListener('subscriptionChanged', (event) => {
    console.log('🔔 Evento subscriptionChanged recibido en Mi Perfil:', event.detail);
    // Recargar estadísticas
    const token = localStorage.getItem('jwtToken');
    if (token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const userId = payload.userId;
        console.log('🔄 Actualizando estadísticas de Mi Perfil...');
        loadUserStats(userId, token);
    }
});
