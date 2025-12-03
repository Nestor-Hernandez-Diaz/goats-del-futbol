/**
 * Admin Players - Gestión CRUD de Jugadores
 * GOATs del Fútbol
 * Version: 1.0 - 2025
 */

// ============================================================================
// CONFIGURACIÓN
// ============================================================================

const API_BASE_URL = 'http://localhost:8080/api';
let allPlayers = [];
let currentEditingId = null;
let tinyMCEInstance = null;

// ============================================================================
// INICIALIZACIÓN
// ============================================================================

document.addEventListener('DOMContentLoaded', async () => {
  console.log('🚀 Iniciando Admin Players v1.0...');
  
  const isAuthenticated = await checkAuthentication();
  if (isAuthenticated) {
    await loadPlayers();
    initializeTinyMCE();
  }
  setupEventListeners();
});

// ============================================================================
// AUTENTICACIÓN
// ============================================================================

/**
 * Verificar si hay token JWT y si es ADMIN
 */
async function checkAuthentication() {
  const token = localStorage.getItem('jwtToken');
  
  if (!token) {
    console.warn('⚠️ No hay token JWT. Redirigiendo a login...');
    showAuthAlert('No has iniciado sesión');
    disableDashboard();
    setTimeout(() => {
      window.location.href = 'login.html';
    }, 2000);
    return false;
  }

  try {
    const payload = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payload));
    let roles = decodedPayload.roles || decodedPayload.authorities || [];
    
    if (typeof roles === 'string') {
      roles = [roles];
    }
    
    console.log('🔑 Token decodificado:', { 
      sub: decodedPayload.sub, 
      roles, 
      exp: decodedPayload.exp 
    });
    
    // Verificar expiración
    const exp = decodedPayload.exp;
    if (exp && Date.now() >= exp * 1000) {
      console.warn('⚠️ Token JWT expirado');
      localStorage.removeItem('jwtToken');
      localStorage.removeItem('currentUser');
      showAuthAlert('Tu sesión ha expirado');
      disableDashboard();
      setTimeout(() => {
        window.location.href = 'login.html';
      }, 2000);
      return false;
    }
    
    // Verificar rol ADMIN
    const hasAdminRole = roles.some(role => 
      role === 'ROLE_ADMIN' || 
      role === 'ADMIN' || 
      role.includes('ADMIN')
    );
    
    if (!hasAdminRole) {
      console.warn('⚠️ Usuario no tiene rol ADMIN. Roles:', roles);
      showAuthAlert('No tienes permisos de administrador');
      disableDashboard();
      return false;
    }
    
    console.log('✅ Autenticación válida. Roles:', Array.isArray(roles) ? roles.join(', ') : roles);
    return true;
  } catch (error) {
    console.error('❌ Error decodificando token:', error);
    showAuthAlert('Token inválido');
    disableDashboard();
    localStorage.removeItem('jwtToken');
    setTimeout(() => {
      window.location.href = 'login.html';
    }, 2000);
    return false;
  }
}

function showAuthAlert(message) {
  const alert = document.getElementById('auth-alert');
  if (alert) {
    alert.innerHTML = `
      <i class="fas fa-exclamation-triangle"></i>
      <strong>Atención:</strong> ${message}
    `;
    alert.style.display = 'block';
  }
}

function disableDashboard() {
  const grid = document.getElementById('players-grid');
  const actions = document.querySelector('.section-actions');
  if (grid) grid.style.display = 'none';
  if (actions) actions.style.display = 'none';
}

// ============================================================================
// CRUD OPERATIONS
// ============================================================================

/**
 * GET - Cargar todos los jugadores
 */
async function loadPlayers() {
  showLoading(true);
  
  try {
    console.log('📥 Cargando jugadores desde API...');
    
    const response = await fetch(`${API_BASE_URL}/players`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}: ${response.statusText}`);
    }

    const data = await response.json();
    
    // La API devuelve paginación, extraer content
    allPlayers = data.content || data;
    
    console.log(`✅ ${allPlayers.length} jugadores cargados`);
    
    renderPlayers(allPlayers);
    showLoading(false);
    
  } catch (error) {
    console.error('❌ Error cargando jugadores:', error);
    showMessage('Error al cargar jugadores. Verifica que el backend esté corriendo.', 'error');
    showLoading(false);
    showEmptyState(true);
  }
}

/**
 * POST - Crear nuevo jugador
 */
async function createPlayer(playerData) {
  showLoadingOverlay(true);
  
  try {
    console.log('📤 Creando jugador:', playerData.name);
    
    const response = await fetch(`${API_BASE_URL}/players`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
      },
      body: JSON.stringify(playerData)
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || `HTTP ${response.status}`);
    }

    const newPlayer = await response.json();
    console.log('✅ Jugador creado:', newPlayer);
    
    showMessage(`Jugador "${playerData.name}" creado exitosamente`, 'success');
    closeModal();
    await loadPlayers();
    
  } catch (error) {
    console.error('❌ Error creando jugador:', error);
    showMessage(`Error al crear jugador: ${error.message}`, 'error');
  } finally {
    showLoadingOverlay(false);
  }
}

/**
 * PUT - Actualizar jugador existente
 */
async function updatePlayer(playerId, playerData) {
  showLoadingOverlay(true);
  
  try {
    console.log('📤 Actualizando jugador ID:', playerId);
    
    const response = await fetch(`${API_BASE_URL}/players/${playerId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
      },
      body: JSON.stringify(playerData)
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      
      // Manejo especial para error 403
      if (response.status === 403) {
        throw new Error('Permisos insuficientes. Verifica tu rol de administrador.');
      }
      
      throw new Error(errorData.message || `HTTP ${response.status}`);
    }

    const updatedPlayer = await response.json();
    console.log('✅ Jugador actualizado:', updatedPlayer);
    
    showMessage(`Jugador "${playerData.name}" actualizado exitosamente`, 'success');
    closeModal();
    await loadPlayers();
    
  } catch (error) {
    console.error('❌ Error actualizando jugador:', error);
    showMessage(`Error al actualizar jugador: ${error.message}`, 'error');
  } finally {
    showLoadingOverlay(false);
  }
}

/**
 * DELETE - Eliminar jugador (con confirmación)
 */
async function deletePlayer(playerId, playerName) {
  const confirmed = confirm(
    `⚠️ ¿Estás seguro de eliminar a "${playerName}"?\n\n` +
    `Esta acción eliminará:\n` +
    `• Perfil del jugador\n` +
    `• Todas sus estadísticas\n` +
    `• Comentarios asociados\n` +
    `• Suscripciones de usuarios\n\n` +
    `Esta acción NO se puede deshacer.`
  );
  
  if (!confirmed) {
    console.log('❌ Eliminación cancelada por el usuario');
    return;
  }
  
  showLoadingOverlay(true);
  
  try {
    console.log('🗑️ Eliminando jugador ID:', playerId);
    
    const response = await fetch(`${API_BASE_URL}/players/${playerId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
      }
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      
      if (response.status === 403) {
        throw new Error('Permisos insuficientes para eliminar jugadores.');
      }
      
      throw new Error(errorData.message || `HTTP ${response.status}`);
    }

    console.log('✅ Jugador eliminado exitosamente');
    
    showMessage(`Jugador "${playerName}" eliminado exitosamente`, 'success');
    await loadPlayers();
    
  } catch (error) {
    console.error('❌ Error eliminando jugador:', error);
    showMessage(`Error al eliminar jugador: ${error.message}`, 'error');
  } finally {
    showLoadingOverlay(false);
  }
}

// ============================================================================
// RENDERIZADO
// ============================================================================

/**
 * Renderizar grid de jugadores
 */
function renderPlayers(players) {
  const grid = document.getElementById('players-grid');
  const emptyState = document.getElementById('empty-state');
  
  if (!players || players.length === 0) {
    grid.innerHTML = '';
    showEmptyState(true);
    return;
  }
  
  showEmptyState(false);
  
  grid.innerHTML = players.map(player => `
    <div class="player-card" data-player-id="${player.id}">
      <div class="player-card-header">
        <div class="player-avatar">
          ${getInitials(player.name)}
        </div>
        <div class="player-info">
          <h3>${escapeHtml(player.name)}</h3>
          <p class="player-nickname">"${escapeHtml(player.nickname)}"</p>
        </div>
      </div>
      
      <div class="player-details">
        <div class="detail-row">
          <span class="detail-label">País:</span>
          <span class="detail-value">${escapeHtml(player.country)}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">Posición:</span>
          <span class="detail-value">${escapeHtml(player.position)}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">ID:</span>
          <span class="detail-value">#${player.id}</span>
        </div>
      </div>
      
      <div class="player-bio-preview">
        ${stripHtml(player.biography).substring(0, 80)}...
      </div>
      
      <div class="player-actions">
        <button class="btn-player btn-view" onclick="viewPlayer(${player.id})">
          <i class="fas fa-eye"></i> Ver
        </button>
        <button class="btn-player btn-edit" onclick="editPlayer(${player.id})">
          <i class="fas fa-edit"></i> Editar
        </button>
        <button class="btn-player btn-delete" onclick="deletePlayer(${player.id}, '${escapeHtml(player.name).replace(/'/g, "\\'")}')">
          <i class="fas fa-trash"></i>
        </button>
      </div>
    </div>
  `).join('');
}

/**
 * Obtener iniciales del nombre
 */
function getInitials(name) {
  return name
    .split(' ')
    .map(word => word[0])
    .slice(0, 2)
    .join('')
    .toUpperCase();
}

/**
 * Eliminar tags HTML de un texto
 */
function stripHtml(html) {
  const tmp = document.createElement('div');
  tmp.innerHTML = html;
  return tmp.textContent || tmp.innerText || '';
}

/**
 * Escapar HTML para prevenir XSS
 */
function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

// ============================================================================
// MODAL Y FORMULARIO
// ============================================================================

/**
 * Abrir modal para crear nuevo jugador
 */
function openCreateModal() {
  currentEditingId = null;
  
  document.getElementById('modal-title-text').textContent = 'Nuevo Jugador';
  document.getElementById('player-form').reset();
  document.getElementById('player-id').value = '';
  
  if (tinyMCEInstance) {
    tinyMCEInstance.setContent('');
  }
  
  document.getElementById('player-modal').classList.add('active');
  
  console.log('📝 Modal abierto: Crear jugador');
}

/**
 * Abrir modal para editar jugador existente
 */
function editPlayer(playerId) {
  const player = allPlayers.find(p => p.id === playerId);
  
  if (!player) {
    console.error('❌ Jugador no encontrado:', playerId);
    showMessage('Jugador no encontrado', 'error');
    return;
  }
  
  currentEditingId = playerId;
  
  document.getElementById('modal-title-text').textContent = 'Editar Jugador';
  document.getElementById('player-id').value = player.id;
  document.getElementById('player-name').value = player.name;
  document.getElementById('player-nickname').value = player.nickname;
  document.getElementById('player-country').value = player.country;
  document.getElementById('player-position').value = player.position;
  
  if (tinyMCEInstance) {
    tinyMCEInstance.setContent(player.biography || '');
  }
  
  updateCharCounter();
  
  document.getElementById('player-modal').classList.add('active');
  
  console.log('✏️ Modal abierto: Editar jugador', player.name);
}

/**
 * Ver perfil del jugador en nueva pestaña
 */
function viewPlayer(playerId) {
  const url = `player.html?id=${playerId}`;
  window.open(url, '_blank');
  console.log('👁️ Abriendo perfil del jugador ID:', playerId);
}

/**
 * Cerrar modal
 */
function closeModal() {
  document.getElementById('player-modal').classList.remove('active');
  document.getElementById('player-form').reset();
  currentEditingId = null;
  
  if (tinyMCEInstance) {
    tinyMCEInstance.setContent('');
  }
  
  // Limpiar errores de validación
  document.querySelectorAll('.form-control.error').forEach(el => {
    el.classList.remove('error');
  });
  
  console.log('❌ Modal cerrado');
}

/**
 * Inicializar TinyMCE editor
 */
function initializeTinyMCE() {
  if (typeof tinymce === 'undefined') {
    console.warn('⚠️ TinyMCE no está cargado');
    return;
  }
  
  tinymce.init({
    selector: '#player-biography',
    height: 400,
    menubar: false,
    plugins: [
      'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
      'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
      'insertdatetime', 'media', 'table', 'code', 'help', 'wordcount'
    ],
    toolbar: 'undo redo | blocks | ' +
      'bold italic forecolor | alignleft aligncenter ' +
      'alignright alignjustify | bullist numlist outdent indent | ' +
      'removeformat | help',
    content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
    setup: (editor) => {
      tinyMCEInstance = editor;
      editor.on('change keyup', updateCharCounter);
    }
  });
  
  console.log('✅ TinyMCE inicializado');
}

/**
 * Actualizar contador de caracteres
 */
function updateCharCounter() {
  if (!tinyMCEInstance) return;
  
  const content = tinyMCEInstance.getContent({ format: 'text' });
  const counter = document.getElementById('bio-char-count');
  
  if (counter) {
    counter.textContent = content.length;
  }
}

// ============================================================================
// VALIDACIÓN DEL FORMULARIO
// ============================================================================

/**
 * Validar formulario antes de enviar
 */
function validateForm() {
  let isValid = true;
  
  // Limpiar errores previos
  document.querySelectorAll('.form-control.error').forEach(el => {
    el.classList.remove('error');
  });
  
  // Validar nombre
  const name = document.getElementById('player-name').value.trim();
  if (!name || name.length < 3) {
    document.getElementById('player-name').classList.add('error');
    isValid = false;
  }
  
  // Validar nickname
  const nickname = document.getElementById('player-nickname').value.trim();
  if (!nickname || nickname.length < 2) {
    document.getElementById('player-nickname').classList.add('error');
    isValid = false;
  }
  
  // Validar país
  const country = document.getElementById('player-country').value.trim();
  if (!country || country.length < 2) {
    document.getElementById('player-country').classList.add('error');
    isValid = false;
  }
  
  // Validar posición
  const position = document.getElementById('player-position').value;
  if (!position) {
    document.getElementById('player-position').classList.add('error');
    isValid = false;
  }
  
  // Validar biografía
  const biography = tinyMCEInstance ? tinyMCEInstance.getContent() : '';
  const bioText = tinyMCEInstance ? tinyMCEInstance.getContent({ format: 'text' }) : '';
  
  if (!biography || bioText.length < 100) {
    showMessage('La biografía debe tener al menos 100 caracteres', 'error');
    isValid = false;
  }
  
  if (!isValid) {
    showMessage('Por favor completa todos los campos correctamente', 'error');
  }
  
  return isValid;
}

/**
 * Manejar envío del formulario
 */
async function handleFormSubmit(event) {
  event.preventDefault();
  
  if (!validateForm()) {
    return;
  }
  
  const playerData = {
    name: document.getElementById('player-name').value.trim(),
    nickname: document.getElementById('player-nickname').value.trim(),
    country: document.getElementById('player-country').value.trim(),
    position: document.getElementById('player-position').value,
    biography: tinyMCEInstance ? tinyMCEInstance.getContent() : ''
  };
  
  if (currentEditingId) {
    // Actualizar jugador existente
    await updatePlayer(currentEditingId, playerData);
  } else {
    // Crear nuevo jugador
    await createPlayer(playerData);
  }
}

// ============================================================================
// BÚSQUEDA Y FILTROS
// ============================================================================

/**
 * Filtrar jugadores por búsqueda
 */
function handleSearch(event) {
  const searchTerm = event.target.value.toLowerCase().trim();
  
  if (!searchTerm) {
    renderPlayers(allPlayers);
    return;
  }
  
  const filtered = allPlayers.filter(player => 
    player.name.toLowerCase().includes(searchTerm) ||
    player.nickname.toLowerCase().includes(searchTerm) ||
    player.country.toLowerCase().includes(searchTerm) ||
    player.position.toLowerCase().includes(searchTerm)
  );
  
  console.log(`🔍 Búsqueda: "${searchTerm}" - ${filtered.length} resultados`);
  
  renderPlayers(filtered);
}

// ============================================================================
// UI HELPERS
// ============================================================================

/**
 * Mostrar/ocultar loading spinner
 */
function showLoading(show) {
  const spinner = document.getElementById('loading-spinner');
  if (spinner) {
    spinner.style.display = show ? 'block' : 'none';
  }
}

/**
 * Mostrar/ocultar loading overlay
 */
function showLoadingOverlay(show) {
  const overlay = document.getElementById('loading-overlay');
  if (overlay) {
    overlay.classList.toggle('active', show);
  }
}

/**
 * Mostrar/ocultar empty state
 */
function showEmptyState(show) {
  const emptyState = document.getElementById('empty-state');
  if (emptyState) {
    emptyState.style.display = show ? 'block' : 'none';
  }
}

/**
 * Mostrar mensaje de éxito/error
 */
function showMessage(message, type = 'info') {
  const container = document.getElementById('message-container');
  if (!container) return;
  
  const colors = {
    success: '#48bb78',
    error: '#f56565',
    info: '#4299e1',
    warning: '#ed8936'
  };
  
  const icons = {
    success: 'fa-check-circle',
    error: 'fa-exclamation-circle',
    info: 'fa-info-circle',
    warning: 'fa-exclamation-triangle'
  };
  
  const messageEl = document.createElement('div');
  messageEl.className = `message message-${type}`;
  messageEl.style.cssText = `
    padding: 15px 20px;
    margin: 20px 0;
    border-radius: 8px;
    background: ${colors[type]};
    color: white;
    display: flex;
    align-items: center;
    gap: 10px;
    animation: slideIn 0.3s ease;
  `;
  messageEl.innerHTML = `
    <i class="fas ${icons[type]}"></i>
    <span>${message}</span>
  `;
  
  container.innerHTML = '';
  container.appendChild(messageEl);
  
  // Auto-ocultar después de 5 segundos
  setTimeout(() => {
    messageEl.style.animation = 'slideOut 0.3s ease';
    setTimeout(() => messageEl.remove(), 300);
  }, 5000);
  
  // Scroll al mensaje
  messageEl.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

// ============================================================================
// EVENT LISTENERS
// ============================================================================

function setupEventListeners() {
  // Botón agregar jugador
  const btnAdd = document.getElementById('btn-add-player');
  if (btnAdd) {
    btnAdd.addEventListener('click', openCreateModal);
  }
  
  // Búsqueda
  const searchInput = document.getElementById('search-input');
  if (searchInput) {
    searchInput.addEventListener('input', handleSearch);
  }
  
  // Formulario
  const form = document.getElementById('player-form');
  if (form) {
    form.addEventListener('submit', handleFormSubmit);
  }
  
  // Botones del modal
  const btnCloseModal = document.getElementById('btn-close-modal');
  if (btnCloseModal) {
    btnCloseModal.addEventListener('click', closeModal);
  }
  
  const btnCancel = document.getElementById('btn-cancel');
  if (btnCancel) {
    btnCancel.addEventListener('click', closeModal);
  }
  
  // Cerrar modal al hacer clic fuera
  const modal = document.getElementById('player-modal');
  if (modal) {
    modal.addEventListener('click', (e) => {
      if (e.target === modal) {
        closeModal();
      }
    });
  }
  
  // Tecla ESC para cerrar modal
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && modal.classList.contains('active')) {
      closeModal();
    }
  });
  
  console.log('✅ Event listeners configurados');
}

// ============================================================================
// EXPONER FUNCIONES GLOBALES
// ============================================================================

// Funciones que se llaman desde el HTML
window.openCreateModal = openCreateModal;
window.editPlayer = editPlayer;
window.viewPlayer = viewPlayer;
window.deletePlayer = deletePlayer;
window.closeModal = closeModal;

// API pública para debugging
window.AdminPlayers = {
  loadPlayers,
  allPlayers: () => allPlayers,
  version: '1.0'
};

console.log('✅ admin-players.js v1.0 cargado');
