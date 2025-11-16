/**
 * Admin Dashboard - Moderación de Comentarios
 * GOATs del Fútbol
 */

// Configuración
const API_BASE_URL = 'http://localhost:8080/api';
let currentFilter = 'all';
let currentCommentId = null;

// Verificar autenticación al cargar
document.addEventListener('DOMContentLoaded', () => {
  checkAuthentication();
  loadStatistics();
  loadComments();
  setupEventListeners();
});

/**
 * Verificar si hay token JWT y si es ADMIN
 */
function checkAuthentication() {
  const token = localStorage.getItem('jwtToken');
  
  if (!token) {
    showAuthAlert();
    disableDashboard();
    return;
  }

  // Verificar rol ADMIN (el token contiene roles en el payload)
  try {
    const payload = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payload));
    const roles = decodedPayload.roles || [];
    
    if (!roles.includes('ROLE_ADMIN') && !roles.includes('ADMIN')) {
      showAuthAlert();
      disableDashboard();
    }
  } catch (error) {
    console.error('Error decodificando token:', error);
    showAuthAlert();
    disableDashboard();
  }
}

function showAuthAlert() {
  document.getElementById('auth-alert').style.display = 'block';
}

function disableDashboard() {
  document.getElementById('stats-grid').style.opacity = '0.5';
  document.querySelector('.comments-section').style.opacity = '0.5';
  document.querySelectorAll('.filter-btn').forEach(btn => btn.disabled = true);
}

/**
 * Cargar estadísticas generales
 */
async function loadStatistics() {
  const token = localStorage.getItem('jwtToken');
  if (!token) return;

  try {
    const response = await fetch(`${API_BASE_URL}/comments?size=1000`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    const data = await response.json();
    
    const comments = data.content || [];
    const pending = comments.filter(c => c.status === 'PENDING').length;
    const approved = comments.filter(c => c.status === 'APPROVED').length;
    const rejected = comments.filter(c => c.status === 'REJECTED').length;
    
    document.getElementById('total-comments').textContent = comments.length;
    document.getElementById('pending-comments').textContent = pending;
    document.getElementById('approved-comments').textContent = approved;
    document.getElementById('rejected-comments').textContent = rejected;
  } catch (error) {
    console.error('Error cargando estadísticas:', error);
    showError('Error al cargar estadísticas');
  }
}

/**
 * Cargar comentarios con filtro
 */
async function loadComments(status = 'all') {
  const token = localStorage.getItem('jwtToken');
  if (!token) return;

  showLoading();
  hideMessages();

  try {
    let url = `${API_BASE_URL}/comments?size=1000`;
    
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }

    const data = await response.json();
    let comments = data.content || [];

    // Filtrar por estado si no es 'all'
    if (status !== 'all') {
      comments = comments.filter(c => c.status === status);
    }

    hideLoading();

    if (comments.length === 0) {
      showEmptyState();
    } else {
      renderComments(comments);
    }
  } catch (error) {
    console.error('Error cargando comentarios:', error);
    hideLoading();
    showError('Error al cargar comentarios. Por favor, intenta de nuevo.');
  }
}

/**
 * Renderizar lista de comentarios
 */
function renderComments(comments) {
  const container = document.getElementById('comments-list');
  container.innerHTML = '';
  
  comments.forEach(comment => {
    const card = createCommentCard(comment);
    container.appendChild(card);
  });
}

/**
 * Crear tarjeta de comentario
 */
function createCommentCard(comment) {
  const card = document.createElement('div');
  card.className = `comment-card ${comment.status.toLowerCase()}`;
  card.setAttribute('data-comment-id', comment.id);

  const statusClass = comment.status.toLowerCase();
  const statusText = {
    'PENDING': 'Pendiente',
    'APPROVED': 'Aprobado',
    'REJECTED': 'Rechazado',
    'EDITED': 'Editado'
  }[comment.status] || comment.status;

  card.innerHTML = `
    <div class="comment-header">
      <div class="comment-meta">
        <div class="comment-author">
          <i class="fas fa-user"></i> ${comment.username || 'Usuario desconocido'}
        </div>
        <div class="comment-player">
          <i class="fas fa-futbol"></i> ${comment.playerName || 'Jugador desconocido'}
        </div>
        <div class="comment-date">
          <i class="fas fa-clock"></i> ${formatDate(comment.createdAt)}
        </div>
      </div>
      <span class="comment-status ${statusClass}">${statusText}</span>
    </div>
    <div class="comment-content">
      ${comment.content}
    </div>
    ${comment.moderationReason ? `
      <div style="margin-top: 1rem; padding: 0.75rem; background: rgba(244, 67, 54, 0.1); border-left: 3px solid #f44336; border-radius: 4px;">
        <strong style="color: #f44336;">Motivo de rechazo:</strong><br>
        ${comment.moderationReason}
      </div>
    ` : ''}
    <div class="comment-actions">
      ${comment.status === 'PENDING' ? `
        <button class="action-btn approve" onclick="approveComment(${comment.id})">
          <i class="fas fa-check"></i> Aprobar
        </button>
        <button class="action-btn reject" onclick="openRejectModal(${comment.id})">
          <i class="fas fa-ban"></i> Rechazar
        </button>
      ` : ''}
      <button class="action-btn delete" onclick="deleteComment(${comment.id})">
        <i class="fas fa-trash"></i> Eliminar
      </button>
    </div>
  `;

  return card;
}

/**
 * Aprobar comentario
 */
async function approveComment(commentId) {
  const token = localStorage.getItem('jwtToken');
  if (!token) return;

  if (!confirm('¿Estás seguro de aprobar este comentario?')) return;

  try {
    const response = await fetch(`${API_BASE_URL}/comments/${commentId}/approve`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Error al aprobar comentario');
    }

    showSuccess('Comentario aprobado exitosamente');
    await loadStatistics();
    await loadComments(currentFilter);
  } catch (error) {
    console.error('Error:', error);
    showError('Error al aprobar comentario. Por favor, intenta de nuevo.');
  }
}

/**
 * Abrir modal para rechazar
 */
function openRejectModal(commentId) {
  currentCommentId = commentId;
  document.getElementById('reject-reason').value = '';
  document.getElementById('reject-modal').classList.add('active');
}

/**
 * Rechazar comentario
 */
async function rejectComment() {
  const token = localStorage.getItem('jwtToken');
  if (!token || !currentCommentId) return;

  const reason = document.getElementById('reject-reason').value.trim();
  
  if (!reason) {
    alert('Por favor, indica el motivo del rechazo');
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/comments/${currentCommentId}/reject`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ reason })
    });

    if (!response.ok) {
      throw new Error('Error al rechazar comentario');
    }

    closeRejectModal();
    showSuccess('Comentario rechazado exitosamente');
    await loadStatistics();
    await loadComments(currentFilter);
  } catch (error) {
    console.error('Error:', error);
    showError('Error al rechazar comentario. Por favor, intenta de nuevo.');
  }
}

/**
 * Cerrar modal de rechazo
 */
function closeRejectModal() {
  document.getElementById('reject-modal').classList.remove('active');
  currentCommentId = null;
}

/**
 * Eliminar comentario
 */
async function deleteComment(commentId) {
  const token = localStorage.getItem('jwtToken');
  if (!token) return;

  if (!confirm('¿Estás seguro de eliminar este comentario? Esta acción no se puede deshacer.')) return;

  try {
    const response = await fetch(`${API_BASE_URL}/comments/${commentId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      throw new Error('Error al eliminar comentario');
    }

    showSuccess('Comentario eliminado exitosamente');
    await loadStatistics();
    await loadComments(currentFilter);
  } catch (error) {
    console.error('Error:', error);
    showError('Error al eliminar comentario. Por favor, intenta de nuevo.');
  }
}

/**
 * Setup de event listeners
 */
function setupEventListeners() {
  // Filtros
  document.querySelectorAll('.filter-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      // Remover active de todos
      document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
      // Agregar active al clickeado
      e.currentTarget.classList.add('active');
      
      const status = e.currentTarget.getAttribute('data-status');
      currentFilter = status;
      loadComments(status);
    });
  });

  // Modal
  document.getElementById('modal-cancel').addEventListener('click', closeRejectModal);
  document.getElementById('modal-confirm').addEventListener('click', rejectComment);
  
  // Cerrar modal al hacer click en overlay
  document.getElementById('reject-modal').addEventListener('click', (e) => {
    if (e.target.id === 'reject-modal') {
      closeRejectModal();
    }
  });

  // Cerrar modal con ESC
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      closeRejectModal();
    }
  });
}

/**
 * Utilidades de UI
 */
function showLoading() {
  document.getElementById('loading-spinner').style.display = 'block';
  document.getElementById('comments-list').innerHTML = '';
  document.getElementById('empty-state').style.display = 'none';
}

function hideLoading() {
  document.getElementById('loading-spinner').style.display = 'none';
}

function showEmptyState() {
  document.getElementById('empty-state').style.display = 'block';
  document.getElementById('comments-list').innerHTML = '';
}

function showError(message) {
  const container = document.getElementById('message-container');
  container.innerHTML = `
    <div class="error-message">
      <i class="fas fa-exclamation-circle"></i> ${message}
    </div>
  `;
  setTimeout(() => container.innerHTML = '', 5000);
}

function showSuccess(message) {
  const container = document.getElementById('message-container');
  container.innerHTML = `
    <div class="success-message">
      <i class="fas fa-check-circle"></i> ${message}
    </div>
  `;
  setTimeout(() => container.innerHTML = '', 5000);
}

function hideMessages() {
  document.getElementById('message-container').innerHTML = '';
}

function formatDate(dateString) {
  if (!dateString) return 'Fecha desconocida';
  
  const date = new Date(dateString);
  const now = new Date();
  const diffMs = now - date;
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMs / 3600000);
  const diffDays = Math.floor(diffMs / 86400000);

  if (diffMins < 1) return 'Hace un momento';
  if (diffMins < 60) return `Hace ${diffMins} minuto${diffMins > 1 ? 's' : ''}`;
  if (diffHours < 24) return `Hace ${diffHours} hora${diffHours > 1 ? 's' : ''}`;
  if (diffDays < 7) return `Hace ${diffDays} día${diffDays > 1 ? 's' : ''}`;
  
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
}

// Exponer funciones globales para onclick
window.approveComment = approveComment;
window.openRejectModal = openRejectModal;
window.deleteComment = deleteComment;
