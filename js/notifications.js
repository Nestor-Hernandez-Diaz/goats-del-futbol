/**
 * notifications.js - Gestión de Notificaciones
 * 
 * Este módulo maneja la visualización y gestión de notificaciones
 */

const API_URL = 'http://localhost:8080/api';

// Estado
let currentFilter = 'all';
let currentPage = 0;
let totalPages = 1;

// Iconos por tipo de notificación
const NOTIFICATION_ICONS = {
  'ACHIEVEMENT': { icon: 'fa-trophy', class: 'logro' },
  'COMMENT': { icon: 'fa-comment', class: 'comentario' },
  'GENERAL': { icon: 'fa-bell', class: 'general' }
};

/**
 * Cargar notificaciones
 */
async function loadNotifications(page = 0) {
  const loadingSpinner = document.getElementById('loadingSpinner');
  const notificationsList = document.getElementById('notificationsList');
  const emptyState = document.getElementById('emptyState');
  
  loadingSpinner.style.display = 'flex';
  notificationsList.innerHTML = '';
  emptyState.style.display = 'none';

  try {
    let url = `${API_URL}/notifications?page=${page}&size=10`;
    
    // Aplicar filtro
    if (currentFilter === 'unread') {
      url = `${API_URL}/notifications?unreadOnly=true&page=${page}&size=10`;
    } else if (currentFilter !== 'all') {
      url = `${API_URL}/notifications/type/${currentFilter}?page=${page}&size=10`;
    }

    const token = localStorage.getItem('jwtToken');
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Error al cargar notificaciones');
    }

    const data = await response.json();
    const notifications = Array.isArray(data) ? data : data.content || [];
    
    loadingSpinner.style.display = 'none';

    if (notifications.length === 0) {
      emptyState.style.display = 'block';
      return;
    }

    // Renderizar notificaciones
    notifications.forEach(notification => {
      notificationsList.appendChild(createNotificationCard(notification));
    });

    // Actualizar paginación si es necesario
    if (data.totalPages) {
      totalPages = data.totalPages;
      currentPage = page;
      updatePagination();
    }

  } catch (error) {
    console.error('Error loading notifications:', error);
    loadingSpinner.style.display = 'none';
    emptyState.style.display = 'block';
    emptyState.querySelector('h3').textContent = 'Error al cargar notificaciones';
    emptyState.querySelector('p').textContent = 'Intenta recargar la página.';
  }
}

/**
 * Crear tarjeta de notificación
 */
function createNotificationCard(notification) {
  const card = document.createElement('div');
  card.className = `notification-item ${notification.read ? '' : 'no-leida'}`;
  card.dataset.id = notification.id;

  const iconInfo = NOTIFICATION_ICONS[notification.type] || NOTIFICATION_ICONS.GENERAL;
  
  card.innerHTML = `
    <div class="notification-icon ${iconInfo.class}">
      <i class="fas ${iconInfo.icon}"></i>
    </div>
    <div class="notification-content">
      <p class="notification-message">${escapeHtml(notification.message)}</p>
      <span class="notification-time">
        <i class="fas fa-clock"></i>
        ${formatNotificationTime(notification.createdAt)}
      </span>
    </div>
    ${!notification.read ? `
      <div class="notification-actions">
        <button class="btn-mark-read" onclick="markAsRead(${notification.id})">
          <i class="fas fa-check"></i> Marcar como leída
        </button>
      </div>
    ` : ''}
  `;

  // Click en la notificación para marcar como leída
  if (!notification.read) {
    card.addEventListener('click', (e) => {
      if (!e.target.closest('.btn-mark-read')) {
        markAsRead(notification.id);
      }
    });
  }

  return card;
}

/**
 * Marcar notificación como leída
 */
async function markAsRead(notificationId) {
  try {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch(
      `${API_URL}/notifications/${notificationId}/read`,
      {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );

    if (response.ok) {
      // Actualizar UI
      const card = document.querySelector(`[data-id="${notificationId}"]`);
      if (card) {
        card.classList.remove('no-leida');
        const actions = card.querySelector('.notification-actions');
        if (actions) actions.remove();
      }

      console.log('✅ Notificación marcada como leída');
    }
  } catch (error) {
    console.error('Error marking notification as read:', error);
  }
}

/**
 * Marcar todas como leídas
 */
async function markAllAsRead() {
  try {
    const token = localStorage.getItem('jwtToken');
    const response = await fetch(
      `${API_URL}/notifications/read-all`,
      {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }
    );

    if (response.ok) {
      // Recargar notificaciones
      await loadNotifications(currentPage);
      
      showToast('Todas las notificaciones marcadas como leídas', 'success');
    }
  } catch (error) {
    console.error('Error marking all as read:', error);
    showToast('Error al marcar notificaciones', 'error');
  }
}

/**
 * Formatear tiempo de notificación
 */
function formatNotificationTime(timestamp) {
  const date = new Date(timestamp);
  const now = new Date();
  const diffMs = now - date;
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMs / 3600000);
  const diffDays = Math.floor(diffMs / 86400000);

  if (diffMins < 1) return 'Hace un momento';
  if (diffMins < 60) return `Hace ${diffMins} min`;
  if (diffHours < 24) return `Hace ${diffHours}h`;
  if (diffDays < 7) return `Hace ${diffDays}d`;
  
  return date.toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'short',
    year: date.getFullYear() !== now.getFullYear() ? 'numeric' : undefined
  });
}

/**
 * Escape HTML para prevenir XSS
 */
function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

/**
 * Actualizar paginación
 */
function updatePagination() {
  const paginationControls = document.getElementById('paginationControls');
  const prevBtn = document.getElementById('prevPageBtn');
  const nextBtn = document.getElementById('nextPageBtn');
  const pageInfo = document.getElementById('pageInfo');

  if (totalPages <= 1) {
    paginationControls.style.display = 'none';
    return;
  }

  paginationControls.style.display = 'flex';
  pageInfo.textContent = `Página ${currentPage + 1} de ${totalPages}`;
  
  prevBtn.disabled = currentPage === 0;
  nextBtn.disabled = currentPage >= totalPages - 1;
}

/**
 * Mostrar toast
 */
function showToast(message, type = 'info') {
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.textContent = message;
  toast.style.cssText = `
    position: fixed;
    bottom: 30px;
    right: 30px;
    background: ${type === 'success' ? '#48bb78' : '#f56565'};
    color: white;
    padding: 15px 25px;
    border-radius: 10px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.2);
    z-index: 10000;
    animation: slideInRight 0.3s ease-out;
  `;

  document.body.appendChild(toast);

  setTimeout(() => {
    toast.style.animation = 'slideOutRight 0.3s ease-out';
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

/**
 * Inicializar página de notificaciones
 */
function initNotifications() {
  // Verificar autenticación
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    window.location.href = 'login.html';
    return;
  }

  // Cargar notificaciones iniciales
  loadNotifications(0);

  // Setup filtros
  const filterBtns = document.querySelectorAll('.filter-btn');
  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      filterBtns.forEach(b => b.classList.remove('activo'));
      btn.classList.add('activo');
      
      currentFilter = btn.dataset.filter;
      loadNotifications(0);
    });
  });

  // Setup marcar todas como leídas
  const markAllReadBtn = document.getElementById('markAllReadBtn');
  if (markAllReadBtn) {
    markAllReadBtn.addEventListener('click', () => {
      if (confirm('¿Marcar todas las notificaciones como leídas?')) {
        markAllAsRead();
      }
    });
  }

  // Setup paginación
  const prevPageBtn = document.getElementById('prevPageBtn');
  const nextPageBtn = document.getElementById('nextPageBtn');
  
  if (prevPageBtn) {
    prevPageBtn.addEventListener('click', () => {
      if (currentPage > 0) {
        loadNotifications(currentPage - 1);
      }
    });
  }
  
  if (nextPageBtn) {
    nextPageBtn.addEventListener('click', () => {
      if (currentPage < totalPages - 1) {
        loadNotifications(currentPage + 1);
      }
    });
  }
}

// Exponer funciones globales
window.markAsRead = markAsRead;
window.markAllAsRead = markAllAsRead;

// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initNotifications);
} else {
  initNotifications();
}
