/**
 * Sistema de Comentarios - GOATs del Fútbol
 * Gestión de comentarios en páginas de jugadores
 * Version: 2.4 - 2025 (Sistema de moderación PENDING)
 */

// Verificar que no esté ya cargado
if (typeof window.commentsSystemLoaded === 'undefined') {
    console.log('🚀 Cargando sistema de comentarios...');
    window.commentsSystemLoaded = true;

    // Configuración
    const API_BASE_URL = 'http://localhost:8080/api';
    const PLAYER_IDS = {
        messi: 1,
        ronaldo: 2,
        neymar: 3
    };

    // Estado global
    let currentPlayerId = null;
    let currentUser = null;
    let commentsCache = [];

    /**
     * Inicializar sistema de comentarios con un playerId
     */
    function initCommentsSystem(playerId) {
        if (!playerId) {
            console.error('No se pudo determinar el ID del jugador');
            return;
        }
        
        currentPlayerId = playerId;
        console.log(`Cargando comentarios del jugador ID: ${currentPlayerId}`);
        
        // Verificar autenticación
        checkUserAuthentication();
        
        // Cargar comentarios
        loadComments();
        
        // Configurar eventos del formulario
        setupCommentForm();
    }

    /**
     * Inicialización al cargar la página
     */
    document.addEventListener('DOMContentLoaded', function() {
        console.log('Inicializando sistema de comentarios...');
        
        // Obtener ID del jugador desde la URL
        currentPlayerId = getCurrentPlayerId();
        
        if (currentPlayerId) {
            initCommentsSystem(currentPlayerId);
        }
    });
    
    /**
     * Escuchar evento playerLoaded para páginas dinámicas (player.html)
     */
    window.addEventListener('playerLoaded', function(event) {
        console.log('🎯 Comentarios: Detectado evento playerLoaded');
        const playerId = event.detail.id;
        
        if (playerId && playerId !== currentPlayerId) {
            initCommentsSystem(playerId);
        }
    });

    /**
     * Obtiene el ID del jugador desde la URL
     */
    function getCurrentPlayerId() {
        // Primero intentar obtener desde el parámetro ?id (player.html dinámico)
        const urlParams = new URLSearchParams(window.location.search);
        const idParam = urlParams.get('id');
        
        if (idParam) {
            const parsedId = parseInt(idParam, 10);
            console.log(`Detectando jugador desde URL param: ${parsedId}`);
            return parsedId;
        }
        
        // Fallback: detectar desde nombre de archivo (páginas estáticas)
        const path = window.location.pathname;
        const filename = path.split('/').pop().split('.')[0].toLowerCase();
        
        console.log(`Detectando jugador desde filename: ${filename}`);
        
        if (PLAYER_IDS[filename]) {
            return PLAYER_IDS[filename];
        }
        
        // Si no se pudo determinar, intentar desde window.currentPlayerId (player-loader.js)
        if (window.currentPlayerId) {
            console.log(`Detectando jugador desde window.currentPlayerId: ${window.currentPlayerId}`);
            return window.currentPlayerId;
        }
        
        return null;
    }

    /**
     * Verifica si el usuario está autenticado
     */
    function checkUserAuthentication() {
    const token = localStorage.getItem('jwtToken');
    
    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            
            // Verificar si el token ha expirado
            const now = Math.floor(Date.now() / 1000);
            if (payload.exp && payload.exp < now) {
                console.warn('Token expirado. Limpiando sesión...');
                localStorage.removeItem('jwtToken');
                localStorage.removeItem('currentUser');
                currentUser = null;
                showLoginPrompt();
                return;
            }
            
            currentUser = {
                username: payload.sub,
                roles: payload.roles || payload.authorities || [],
                userId: payload.userId
            };
            
            console.log('✓ Usuario autenticado:', currentUser.username, '| Token válido hasta:', new Date(payload.exp * 1000).toLocaleString());
            showCommentForm();
        } catch (error) {
            console.error('Error al decodificar token:', error);
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('currentUser');
            currentUser = null;
            showLoginPrompt();
        }
    } else {
        console.log('Usuario no autenticado');
        showLoginPrompt();
    }
}

/**
 * Carga los comentarios del jugador
 */
async function loadComments() {
    console.log(`📡 Cargando comentarios APROBADOS del jugador ID ${currentPlayerId}...`);
    
    try {
        const url = `${API_BASE_URL}/comments/player/${currentPlayerId}?status=APPROVED&size=100`;
        console.log(`🔍 URL: ${url}`);
        
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        console.log(`📨 Respuesta: ${response.status} ${response.statusText}`);
        
        if (response.ok) {
            const data = await response.json();
            commentsCache = data.content || [];
            
            console.log(`✅ ${commentsCache.length} comentarios APROBADOS cargados`);
            
            // Verificar que todos sean APPROVED
            const nonApproved = commentsCache.filter(c => c.status !== 'APPROVED');
            if (nonApproved.length > 0) {
                console.warn(`⚠️ ADVERTENCIA: ${nonApproved.length} comentarios NO aprobados detectados:`, nonApproved);
            } else {
                console.log('✅ Todos los comentarios tienen status APPROVED');
            }
            
            renderComments(commentsCache);
        } else {
            console.error('❌ Error al cargar comentarios:', response.status);
            showCommentsError('No se pudieron cargar los comentarios');
        }
    } catch (error) {
        console.error('❌ Error de red al cargar comentarios:', error);
        showCommentsError('Error de conexión. Intenta de nuevo más tarde.');
    }
}

/**
 * Renderiza la lista de comentarios
 */
function renderComments(comments) {
    const container = document.getElementById('comments-list');
    
    if (!container) {
        console.error('Contenedor de comentarios no encontrado');
        return;
    }
    
    if (comments.length === 0) {
        container.innerHTML = `
            <div class="no-comments">
                <i class="fas fa-comments"></i>
                <p>¡Sé el primero en comentar!</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = comments.map(comment => createCommentHTML(comment)).join('');
    
    // Animar entrada de comentarios
    setTimeout(() => {
        const commentElements = container.querySelectorAll('.comment-card');
        commentElements.forEach((el, index) => {
            setTimeout(() => {
                el.classList.add('comment-visible');
            }, index * 100);
        });
    }, 100);
}

/**
 * Crea el HTML de un comentario con respuestas
 */
function createCommentHTML(comment) {
    const isOwnComment = currentUser && comment.username === currentUser.username;
    const isPending = comment.status === 'PENDING';
    const date = new Date(comment.createdAt);
    const formattedDate = formatDate(date);
    
    return `
        <div class="comment-card ${isPending ? 'comment-pending' : ''}" data-comment-id="${comment.id}">
            <div class="comment-header">
                <div class="comment-author">
                    <i class="fas fa-user-circle"></i>
                    <span class="comment-username">${escapeHtml(comment.username)}</span>
                    ${isOwnComment ? '<span class="badge-own">Tú</span>' : ''}
                    ${isPending ? '<span class="badge-pending">Pendiente de moderación</span>' : ''}
                </div>
                <div class="comment-date">
                    <i class="far fa-clock"></i>
                    <span>${formattedDate}</span>
                </div>
            </div>
            <div class="comment-body">
                <p>${escapeHtml(comment.content)}</p>
            </div>
            <div class="comment-actions">
                ${isOwnComment && comment.status === 'APPROVED' ? `
                    <button class="btn-comment-delete" onclick="deleteComment(${comment.id})">
                        <i class="fas fa-trash"></i> Eliminar
                    </button>
                ` : ''}
                ${currentUser && comment.status === 'APPROVED' ? `
                    <button class="btn-comment-reply" onclick="toggleReplyForm(${comment.id})">
                        <i class="fas fa-reply"></i> Responder
                    </button>
                ` : ''}
                ${!isPending ? `
                    <button class="btn-view-replies" onclick="loadReplies(${comment.id})" data-comment-id="${comment.id}">
                        <i class="fas fa-comments"></i> <span class="replies-count">Ver respuestas</span>
                    </button>
                ` : ''}
            </div>
            
            <!-- Formulario de respuesta (oculto por defecto) -->
            <div id="reply-form-${comment.id}" class="reply-form-container" style="display: none;">
                <form class="reply-form" onsubmit="submitReply(event, ${comment.id})">
                    <textarea 
                        name="replyContent" 
                        placeholder="Escribe tu respuesta..." 
                        maxlength="500"
                        rows="3"
                        required
                    ></textarea>
                    <div class="reply-form-actions">
                        <button type="submit" class="btn-submit-reply">
                            <i class="fas fa-paper-plane"></i> Enviar
                        </button>
                        <button type="button" class="btn-cancel-reply" onclick="toggleReplyForm(${comment.id})">
                            Cancelar
                        </button>
                    </div>
                </form>
            </div>
            
            <!-- Contenedor de respuestas -->
            <div id="replies-${comment.id}" class="replies-container" style="display: none;"></div>
        </div>
    `;
}

/**
 * Muestra el formulario de comentarios
 */
function showCommentForm() {
    const formContainer = document.getElementById('comment-form-container');
    if (!formContainer) return;
    
    formContainer.innerHTML = `
        <div class="comment-form-wrapper">
            <h3>Dejar un Comentario</h3>
            <p class="comment-form-subtitle">Comparte tu opinión sobre este jugador</p>
            <form id="comment-form" class="comment-form">
                <div class="form-group">
                    <textarea 
                        id="comment-content" 
                        name="content" 
                        rows="4" 
                        placeholder="Escribe tu comentario aquí..."
                        maxlength="1000"
                        required
                    ></textarea>
                    <div class="char-counter">
                        <span id="char-count">0</span> / 1000 caracteres (mínimo 10)
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-submit-comment">
                        <i class="fas fa-paper-plane"></i> Publicar Comentario
                    </button>
                    <p class="form-note">
                        <i class="fas fa-info-circle"></i>
                        Tu comentario será revisado antes de publicarse
                    </p>
                </div>
            </form>
        </div>
    `;
    
    // Configurar contador de caracteres
    const textarea = document.getElementById('comment-content');
    const charCount = document.getElementById('char-count');
    
    textarea.addEventListener('input', function() {
        const length = this.value.length;
        charCount.textContent = length;
        
        // Cambiar color según validación
        if (length === 0) {
            charCount.style.color = '#888';
        } else if (length < 10) {
            charCount.style.color = '#ff6b6b'; // Rojo - muy corto
        } else if (length > 1000) {
            charCount.style.color = '#ff6b6b'; // Rojo - muy largo
        } else {
            charCount.style.color = '#4CAF50'; // Verde - válido
        }
    });
}

/**
 * Muestra prompt de login
 */
function showLoginPrompt() {
    const formContainer = document.getElementById('comment-form-container');
    if (!formContainer) return;
    
    formContainer.innerHTML = `
        <div class="login-prompt">
            <i class="fas fa-lock"></i>
            <h3>Inicia sesión para comentar</h3>
            <p>Únete a la conversación y comparte tu opinión</p>
            <a href="login.html" class="btn-login">
                <i class="fas fa-sign-in-alt"></i> Iniciar Sesión
            </a>
        </div>
    `;
}

/**
 * Configura los eventos del formulario
 */
function setupCommentForm() {
    document.addEventListener('submit', async function(e) {
        if (e.target.id === 'comment-form') {
            e.preventDefault();
            await submitComment(e.target);
        }
    });
}

/**
 * Envía un nuevo comentario
 */
async function submitComment(form) {
    const content = form.content.value.trim();
    const token = localStorage.getItem('jwtToken');
    
    if (!content) {
        showNotification('Por favor escribe un comentario', 'error');
        return;
    }
    
    if (content.length < 10) {
        showNotification('El comentario debe tener al menos 10 caracteres', 'error');
        return;
    }
    
    if (content.length > 1000) {
        showNotification('El comentario no puede exceder 1000 caracteres', 'error');
        return;
    }
    
    if (!token) {
        showNotification('Debes iniciar sesión para comentar', 'error');
        return;
    }
    
    // Deshabilitar botón de envío
    const submitBtn = form.querySelector('button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
    
    try {
        console.log('=== ENVIANDO COMENTARIO ===');
        console.log('Token completo:', token);
        console.log('Token length:', token ? token.length : 0);
        console.log('Player ID:', currentPlayerId);
        console.log('Content:', content);
        console.log('API URL:', `${API_BASE_URL}/comments`);
        
        // Validar token antes de enviar
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            console.log('Token payload:', payload);
            console.log('Token expira:', new Date(payload.exp * 1000).toLocaleString());
            console.log('Ahora es:', new Date().toLocaleString());
            console.log('Token válido:', payload.exp > (Date.now() / 1000));
        } catch (e) {
            console.error('Error al decodificar token:', e);
        }
        
        const response = await fetch(`${API_BASE_URL}/comments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                playerId: currentPlayerId,
                content: content
            })
        });
        
        console.log('Response status:', response.status, response.statusText);
        console.log('Response headers:', Object.fromEntries(response.headers.entries()));
        
        if (response.ok) {
            const newComment = await response.json();
            console.log('✅ Comentario creado:', newComment);
            console.log(`   Status: ${newComment.status}`);
            
            if (newComment.status === 'PENDING') {
                showNotification('¡Comentario enviado! Será visible una vez aprobado por el administrador.', 'success');
            } else if (newComment.status === 'APPROVED') {
                showNotification('¡Comentario publicado exitosamente!', 'success');
            }
            
            form.reset();
            document.getElementById('char-count').textContent = '0';
            document.getElementById('char-count').style.color = '#888';
            
            // Recargar comentarios para mostrar el nuevo (si está aprobado)
            await loadComments();
        } else if (response.status === 401 || response.status === 403) {
            console.error('Error de autenticación:', response.status);
            // Limpiar token inválido
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('currentUser');
            showNotification('Tu sesión ha expirado. Por favor inicia sesión de nuevo.', 'error');
            setTimeout(() => {
                const isInPagesFolder = window.location.pathname.includes('/pages/');
                window.location.href = isInPagesFolder ? 'login.html' : 'pages/login.html';
            }, 2000);
        } else {
            // Intentar parsear error solo si hay contenido
            let errorMessage = 'Error al enviar comentario';
            try {
                const errorData = await response.json();
                errorMessage = errorData.message || errorMessage;
            } catch (e) {
                errorMessage = `Error ${response.status}: ${response.statusText}`;
            }
            showNotification(errorMessage, 'error');
        }
    } catch (error) {
        console.error('Error al enviar comentario:', error);
        showNotification('Error de conexión. Intenta de nuevo.', 'error');
    } finally {
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalText;
    }
}

/**
 * Elimina un comentario (solo el autor)
 */
async function deleteComment(commentId) {
    if (!confirm('¿Estás seguro de eliminar este comentario?')) {
        return;
    }
    
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        showNotification('Debes iniciar sesión', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/comments/${commentId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            showNotification('Comentario eliminado correctamente', 'success');
            
            // Remover del cache y re-renderizar
            commentsCache = commentsCache.filter(c => c.id !== commentId);
            renderComments(commentsCache);
        } else if (response.status === 401) {
            showNotification('Tu sesión ha expirado', 'error');
        } else {
            showNotification('Error al eliminar comentario', 'error');
        }
    } catch (error) {
        console.error('Error al eliminar comentario:', error);
        showNotification('Error de conexión', 'error');
    }
}

/**
 * Muestra mensaje de error en la sección de comentarios
 */
function showCommentsError(message) {
    const container = document.getElementById('comments-list');
    if (!container) return;
    
    container.innerHTML = `
        <div class="comments-error">
            <i class="fas fa-exclamation-circle"></i>
            <p>${message}</p>
        </div>
    `;
}

/**
 * Muestra notificación temporal
 */
function showNotification(message, type = 'info') {
    // Remover notificaciones previas
    const existingNotif = document.querySelector('.notification-toast');
    if (existingNotif) {
        existingNotif.remove();
    }
    
    const notification = document.createElement('div');
    notification.className = `notification-toast notification-${type}`;
    
    const icon = type === 'success' ? 'check-circle' : 
                 type === 'error' ? 'exclamation-circle' : 
                 'info-circle';
    
    notification.innerHTML = `
        <i class="fas fa-${icon}"></i>
        <span>${message}</span>
    `;
    
    document.body.appendChild(notification);
    
    // Mostrar animación
    setTimeout(() => notification.classList.add('show'), 100);
    
    // Ocultar después de 4 segundos
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => notification.remove(), 300);
    }, 4000);
}

/**
 * Formatea una fecha a formato legible
 */
function formatDate(date) {
    const now = new Date();
    const diff = now - date;
    const seconds = Math.floor(diff / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    
    if (seconds < 60) return 'Hace un momento';
    if (minutes < 60) return `Hace ${minutes} minuto${minutes > 1 ? 's' : ''}`;
    if (hours < 24) return `Hace ${hours} hora${hours > 1 ? 's' : ''}`;
    if (days < 7) return `Hace ${days} día${days > 1 ? 's' : ''}`;
    
    return date.toLocaleDateString('es-ES', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
    });
}

/**
 * Escapa HTML para prevenir XSS
 */
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Exponer funciones globales para onclick
window.deleteComment = deleteComment;
window.toggleReplyForm = toggleReplyForm;
window.submitReply = submitReply;
window.loadReplies = loadReplies;
window.deleteReply = deleteReply;

// ==================== SISTEMA DE RESPUESTAS ====================

/**
 * Alterna la visibilidad del formulario de respuesta
 */
function toggleReplyForm(commentId) {
    const formContainer = document.getElementById(`reply-form-${commentId}`);
    const isVisible = formContainer.style.display !== 'none';
    
    // Ocultar todos los formularios de respuesta abiertos
    document.querySelectorAll('.reply-form-container').forEach(form => {
        form.style.display = 'none';
    });
    
    // Mostrar el formulario si estaba oculto
    if (!isVisible) {
        formContainer.style.display = 'block';
        const textarea = formContainer.querySelector('textarea');
        textarea.focus();
    }
}

/**
 * Carga las respuestas de un comentario
 */
async function loadReplies(commentId) {
    const repliesContainer = document.getElementById(`replies-${commentId}`);
    const loadBtn = document.querySelector(`[data-comment-id="${commentId}"].btn-view-replies`);
    
    // Si las respuestas ya están visibles, ocultarlas
    if (repliesContainer.style.display !== 'none') {
        repliesContainer.style.display = 'none';
        if (loadBtn) {
            loadBtn.innerHTML = '<i class="fas fa-comments"></i> <span class="replies-count">Ver respuestas</span>';
        }
        return;
    }
    
    try {
        // Mostrar loading
        if (loadBtn) {
            loadBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Cargando...';
            loadBtn.disabled = true;
        }
        
        const response = await fetch(`${API_BASE_URL}/comments/${commentId}/replies`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error('Error al cargar las respuestas');
        }
        
        const replies = await response.json();
        
        // Renderizar respuestas
        if (replies.length === 0) {
            repliesContainer.innerHTML = '<div class="no-replies">No hay respuestas aún</div>';
        } else {
            repliesContainer.innerHTML = replies.map(reply => createReplyHTML(reply)).join('');
        }
        
        repliesContainer.style.display = 'block';
        
        // Actualizar botón
        if (loadBtn) {
            loadBtn.innerHTML = `<i class="fas fa-chevron-up"></i> Ocultar respuestas (${replies.length})`;
            loadBtn.disabled = false;
        }
        
    } catch (error) {
        console.error('Error loading replies:', error);
        showNotification('Error al cargar las respuestas', 'error');
        if (loadBtn) {
            loadBtn.innerHTML = '<i class="fas fa-comments"></i> <span class="replies-count">Ver respuestas</span>';
            loadBtn.disabled = false;
        }
    }
}

/**
 * Crea el HTML de una respuesta
 */
function createReplyHTML(reply) {
    const isOwnReply = currentUser && reply.username === currentUser.username;
    const date = new Date(reply.createdAt);
    const formattedDate = formatDate(date);
    
    return `
        <div class="reply-card" data-reply-id="${reply.id}">
            <div class="reply-header">
                <div class="reply-author">
                    <i class="fas fa-user-circle"></i>
                    <span class="reply-username">${escapeHtml(reply.username)}</span>
                    ${isOwnReply ? '<span class="badge-own">Tú</span>' : ''}
                </div>
                <div class="reply-date">
                    <i class="far fa-clock"></i>
                    <span>${formattedDate}</span>
                </div>
            </div>
            <div class="reply-body">
                <p>${escapeHtml(reply.content)}</p>
            </div>
            ${isOwnReply ? `
                <div class="reply-actions">
                    <button class="btn-reply-delete" onclick="deleteReply(${reply.id}, ${reply.commentId})">
                        <i class="fas fa-trash"></i> Eliminar
                    </button>
                </div>
            ` : ''}
        </div>
    `;
}

/**
 * Envía una respuesta a un comentario
 */
async function submitReply(event, commentId) {
    event.preventDefault();
    
    const form = event.target;
    const textarea = form.querySelector('textarea[name="replyContent"]');
    const content = textarea.value.trim();
    const submitBtn = form.querySelector('.btn-submit-reply');
    
    // Validaciones
    if (!content) {
        showNotification('Escribe una respuesta', 'error');
        return;
    }
    
    if (content.length > 500) {
        showNotification('La respuesta no puede exceder 500 caracteres', 'error');
        return;
    }
    
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        showNotification('Debes iniciar sesión para responder', 'error');
        return;
    }
    
    try {
        // Deshabilitar botón y mostrar loading
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
        
        const response = await fetch(`${API_BASE_URL}/comments/${commentId}/replies`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                content: content
            })
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Error al enviar la respuesta');
        }
        
        const newReply = await response.json();
        
        // Limpiar formulario y ocultarlo
        textarea.value = '';
        toggleReplyForm(commentId);
        
        // Mostrar notificación de éxito
        showNotification('¡Respuesta publicada con éxito!', 'success');
        
        // Recargar las respuestas para mostrar la nueva
        const repliesContainer = document.getElementById(`replies-${commentId}`);
        if (repliesContainer.style.display !== 'none') {
            loadReplies(commentId); // Esto ocultará
            setTimeout(() => loadReplies(commentId), 100); // Y mostrará actualizado
        }
        
    } catch (error) {
        console.error('Error submitting reply:', error);
        showNotification(error.message || 'Error al enviar la respuesta', 'error');
    } finally {
        // Rehabilitar botón
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i> Enviar';
    }
}

/**
 * Elimina una respuesta
 */
async function deleteReply(replyId, commentId) {
    if (!confirm('¿Estás seguro de que deseas eliminar esta respuesta?')) {
        return;
    }
    
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        showNotification('Debes iniciar sesión', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/comments/replies/${replyId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (!response.ok) {
            throw new Error('Error al eliminar la respuesta');
        }
        
        showNotification('Respuesta eliminada correctamente', 'success');
        
        // Recargar las respuestas
        loadReplies(commentId); // Esto ocultará
        setTimeout(() => loadReplies(commentId), 100); // Y mostrará actualizado
        
    } catch (error) {
        console.error('Error deleting reply:', error);
        showNotification('Error al eliminar la respuesta', 'error');
    }
}

} // Fin de verificación de carga

