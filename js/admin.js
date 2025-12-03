/**
 * Admin Dashboard - Moderación de Comentarios
 * GOATs del Fútbol
 * Version: 1.4 - 2025 (Logs de moderación completos)
 */

// Configuración
const API_BASE_URL = 'http://localhost:8080/api';
let currentFilter = 'all';
let currentCommentId = null;

// Verificar autenticación al cargar
document.addEventListener('DOMContentLoaded', async () => {
  console.log('🚀 Iniciando Admin Panel v1.4...');
  
  const isAuthenticated = await checkAuthentication();
  if (isAuthenticated) {
    await loadStatistics();
    await loadComments();
  }
  setupEventListeners();
});

/**
 * Verificar si hay token JWT y si es ADMIN
 */
async function checkAuthentication() {
  const token = localStorage.getItem('jwtToken');
  
  if (!token) {
    console.warn('No hay token JWT. Redirigiendo a login...');
    showAuthAlert('No has iniciado sesión');
    disableDashboard();
    setTimeout(() => {
      window.location.href = 'login.html';
    }, 2000);
    return false;
  }

  // Verificar rol ADMIN (el token contiene roles en el payload)
  try {
    const payload = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payload));
    let roles = decodedPayload.roles || decodedPayload.authorities || [];
    
    // Si roles es un string, convertirlo a array
    if (typeof roles === 'string') {
      roles = [roles];
    }
    
    console.log('Token decodificado:', { sub: decodedPayload.sub, roles, exp: decodedPayload.exp });
    
    // Verificar si el token expiró
    const exp = decodedPayload.exp;
    if (exp && Date.now() >= exp * 1000) {
      console.warn('Token JWT expirado. Redirigiendo a login...');
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('currentUser');
      showAuthAlert('Tu sesión ha expirado');
      disableDashboard();
      setTimeout(() => {
        window.location.href = 'login.html';
      }, 2000);
      return false;
    }
    
    // Verificar rol ADMIN (puede ser 'ROLE_ADMIN' o solo 'ADMIN')
    const hasAdminRole = roles.some(role => 
      role === 'ROLE_ADMIN' || 
      role === 'ADMIN' || 
      role.includes('ADMIN')
    );
    
    if (!hasAdminRole) {
      console.warn('Usuario no tiene rol ADMIN. Roles:', roles);
      showAuthAlert('No tienes permisos de administrador');
      disableDashboard();
      return false;
    }
    
    console.log('✓ Autenticación válida. Roles:', Array.isArray(roles) ? roles.join(', ') : roles);
    return true;
  } catch (error) {
    console.error('Error decodificando token:', error);
    showAuthAlert('Token inválido');
    disableDashboard();
    localStorage.removeItem('jwtToken');
    setTimeout(() => {
      window.location.href = 'login.html';
    }, 2000);
    return false;
  }
}

function showAuthAlert(message = 'Acceso denegado: Necesitas iniciar sesión con una cuenta de administrador') {
  const alert = document.getElementById('auth-alert');
  alert.innerHTML = `
    <i class="fas fa-exclamation-triangle"></i>
    <div>
      <strong>Atención:</strong> ${message}
      <br><small>Redirigiendo a login...</small>
    </div>
  `;
  alert.style.display = 'flex';
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
  console.log('📊 Cargando estadísticas...');
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    console.error('❌ No hay token JWT');
    return;
  }

  try {
    console.log('📡 GET /comments?size=1000');
    const response = await fetch(`${API_BASE_URL}/comments?size=1000`, {
      headers: { 
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json'
      }
    });
    
    console.log(`📨 Respuesta: ${response.status} ${response.statusText}`);
    
    if (!response.ok) {
      const errorText = await response.text();
      console.error('❌ Error en respuesta:', errorText);
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const data = await response.json();
    console.log('✅ Datos recibidos:', data);
    
    const comments = data.content || [];
    console.log(`📝 Total comentarios en BD: ${comments.length}`);
    
    const pending = comments.filter(c => c.status === 'PENDING').length;
    const approved = comments.filter(c => c.status === 'APPROVED').length;
    const rejected = comments.filter(c => c.status === 'REJECTED').length;
    
    console.log(`📊 Estadísticas: Pending=${pending}, Approved=${approved}, Rejected=${rejected}`);
    
    document.getElementById('total-comments').textContent = comments.length;
    document.getElementById('pending-comments').textContent = pending;
    document.getElementById('approved-comments').textContent = approved;
    document.getElementById('rejected-comments').textContent = rejected;
  } catch (error) {
    console.error('❌ Error cargando estadísticas:', error);
    
    // Si es 403, el token es inválido o expiró
    if (error.message.includes('403')) {
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('currentUser');
      showError('Sesión expirada. Redirigiendo a login...');
      setTimeout(() => {
        window.location.href = 'login.html';
      }, 2000);
    } else {
      // Mostrar 0 en lugar de error
      document.getElementById('total-comments').textContent = '0';
      document.getElementById('pending-comments').textContent = '0';
      document.getElementById('approved-comments').textContent = '0';
      document.getElementById('rejected-comments').textContent = '0';
    }
  }
}

/**
 * Cargar comentarios con filtro
 */
async function loadComments(status = 'all') {
  console.log(`🔍 Cargando comentarios con filtro: ${status}`);
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    console.error('❌ No hay token JWT');
    return;
  }

  showLoading();
  hideMessages();

  try {
    // Construir URL con parámetros
    let url = `${API_BASE_URL}/comments?size=1000`;
    
    // Si el status no es 'all', agregarlo como parámetro
    if (status !== 'all') {
      url += `&status=${status}`;
    }
    
    console.log('📡 Solicitando:', url);
    
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json'
      }
    });

    console.log(`📨 Respuesta: ${response.status} ${response.statusText}`);

    if (!response.ok) {
      const errorText = await response.text();
      console.error('❌ Error en respuesta:', errorText);
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }

    const data = await response.json();
    console.log('✅ Datos recibidos:', data);
    
    let comments = data.content || [];
    console.log(`📝 ${comments.length} comentarios cargados con filtro "${status}"`);

    hideLoading();

    if (comments.length === 0) {
      console.log('ℹ️ No hay comentarios, mostrando estado vacío');
      showEmptyState();
    } else {
      console.log('✅ Renderizando comentarios...');
      renderComments(comments);
    }
  } catch (error) {
    console.error('Error cargando comentarios:', error);
    hideLoading();
    
    // Si es 403, el token es inválido o expiró
    if (error.message.includes('403')) {
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('currentUser');
      showError('Sesión expirada. Redirigiendo a login...');
      setTimeout(() => {
        window.location.href = 'login.html';
      }, 2000);
    } else {
      showError('Error al cargar comentarios. Por favor, intenta de nuevo.');
    }
  }
}

/**
 * Renderizar lista de comentarios
 */
function renderComments(comments) {
  console.log(`🎨 Renderizando ${comments.length} comentarios...`);
  
  const container = document.getElementById('comments-list');
  
  if (!container) {
    console.error('❌ Elemento #comments-list no encontrado en el DOM');
    return;
  }
  
  container.innerHTML = '';
  
  comments.forEach((comment, index) => {
    console.log(`  📄 Comentario ${index + 1}:`, {
      id: comment.id,
      status: comment.status,
      username: comment.username,
      player: comment.playerName,
      content: comment.content?.substring(0, 50) + '...'
    });
    
    const card = createCommentCard(comment);
    container.appendChild(card);
    
    // Verificar que se agregó al DOM
    const cardInDOM = document.querySelector(`[data-comment-id="${comment.id}"]`);
    if (cardInDOM) {
      console.log(`  ✅ Comentario ${comment.id} insertado en DOM con clases:`, cardInDOM.className);
    } else {
      console.error(`  ❌ Comentario ${comment.id} NO se insertó en el DOM`);
    }
  });
  
  console.log(`✅ ${comments.length} comentarios renderizados exitosamente en el DOM`);
  
  // 🔍 DIAGNÓSTICO CSS
  const computedStyles = window.getComputedStyle(container);
  console.log('🎨 Estilos aplicados a #comments-list:', {
    display: computedStyles.display,
    gap: computedStyles.gap,
    visibility: computedStyles.visibility,
    opacity: computedStyles.opacity,
    height: container.offsetHeight + 'px',
    children: container.children.length
  });
  
  if (container.children.length > 0) {
    const firstCard = container.children[0];
    const cardStyles = window.getComputedStyle(firstCard);
    console.log('🎨 Estilos de primera tarjeta:', {
      display: cardStyles.display,
      visibility: cardStyles.visibility,
      opacity: cardStyles.opacity,
      height: firstCard.offsetHeight + 'px',
      backgroundColor: cardStyles.backgroundColor,
      border: cardStyles.border
    });
  }
}

/**
 * Crear tarjeta de comentario
 */
function createCommentCard(comment) {
  console.log(`  🏗️ Creando tarjeta para comentario ID ${comment.id}`);
  
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
  
  console.log(`  ✅ Tarjeta creada para comentario ID ${comment.id}`);

  return card;
}

/**
 * Aprobar comentario
 */
async function approveComment(commentId) {
  console.log(`✅ Iniciando aprobación de comentario ID ${commentId}...`);
  
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    console.error('❌ No hay token JWT');
    return;
  }

  if (!confirm('¿Estás seguro de aprobar este comentario?')) {
    console.log('⚠️ Aprobación cancelada por el usuario');
    return;
  }

  try {
    console.log(`📡 POST /comments/${commentId}/approve`);
    
    const response = await fetch(`${API_BASE_URL}/comments/${commentId}/approve`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    console.log(`📨 Respuesta: ${response.status} ${response.statusText}`);

    if (!response.ok) {
      const errorText = await response.text();
      console.error('❌ Error en respuesta:', errorText);
      throw new Error('Error al aprobar comentario');
    }

    const data = await response.json();
    console.log('✅ Comentario aprobado:', data);
    
    showSuccess('Comentario aprobado exitosamente');
    console.log('🔄 Recargando estadísticas y comentarios...');
    await loadStatistics();
    await loadComments(currentFilter);
    console.log('✅ Aprobación completada');
  } catch (error) {
    console.error('❌ Error al aprobar:', error);
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
  console.log(`🚫 Iniciando rechazo de comentario ID ${currentCommentId}...`);
  
  const token = localStorage.getItem('jwtToken');
  if (!token || !currentCommentId) {
    console.error('❌ No hay token o ID de comentario');
    return;
  }

  const reason = document.getElementById('reject-reason').value.trim();
  
  if (!reason) {
    console.warn('⚠️ Motivo de rechazo vacío');
    alert('Por favor, indica el motivo del rechazo');
    return;
  }

  console.log(`📝 Motivo: "${reason}"`);

  try {
    console.log(`📡 POST /comments/${currentCommentId}/reject`);
    
    const response = await fetch(`${API_BASE_URL}/comments/${currentCommentId}/reject`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ reason })
    });

    console.log(`📨 Respuesta: ${response.status} ${response.statusText}`);

    if (!response.ok) {
      const errorText = await response.text();
      console.error('❌ Error en respuesta:', errorText);
      throw new Error('Error al rechazar comentario');
    }

    const data = await response.json();
    console.log('✅ Comentario rechazado:', data);

    closeRejectModal();
    showSuccess('Comentario rechazado exitosamente');
    console.log('🔄 Recargando estadísticas y comentarios...');
    await loadStatistics();
    await loadComments(currentFilter);
    console.log('✅ Rechazo completado');
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
  console.log(`🗑️ Iniciando eliminación de comentario ID ${commentId}...`);
  
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    console.error('❌ No hay token JWT');
    return;
  }

  if (!confirm('¿Estás seguro de eliminar este comentario? Esta acción no se puede deshacer.')) {
    console.log('⚠️ Eliminación cancelada por el usuario');
    return;
  }

  try {
    console.log(`📡 DELETE /comments/${commentId}`);
    
    const response = await fetch(`${API_BASE_URL}/comments/${commentId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    console.log(`📨 Respuesta: ${response.status} ${response.statusText}`);

    if (!response.ok) {
      const errorText = await response.text();
      console.error('❌ Error en respuesta:', errorText);
      throw new Error('Error al eliminar comentario');
    }

    console.log('✅ Comentario eliminado exitosamente');
    
    showSuccess('Comentario eliminado exitosamente');
    console.log('🔄 Recargando estadísticas y comentarios...');
    await loadStatistics();
    await loadComments(currentFilter);
    console.log('✅ Eliminación completada');
  } catch (error) {
    console.error('❌ Error al eliminar:', error);
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
