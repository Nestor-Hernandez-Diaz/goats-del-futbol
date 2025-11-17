/**
 * Sistema de Comentarios - GOATs del Fútbol
 * Gestión de comentarios en páginas de jugadores
 */

// Verificar que no esté ya cargado
if (typeof window.commentsSystemLoaded === 'undefined') {
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
     * Inicialización al cargar la página
     */
    document.addEventListener('DOMContentLoaded', function() {
        console.log('💬 Inicializando sistema de comentarios...');
        
        // Obtener ID del jugador desde la URL
        currentPlayerId = getCurrentPlayerId();
        if (!currentPlayerId) {
            console.error('No se pudo determinar el ID del jugador');
            return;
        }
        
        console.log(`📊 Cargando comentarios del jugador ID: ${currentPlayerId}`);
        
        // Verificar autenticación
        checkUserAuthentication();
        
        // Cargar comentarios
        loadComments();
        
        // Configurar eventos del formulario
        setupCommentForm();
    });

    /**
     * Obtiene el ID del jugador desde la URL
     */
    function getCurrentPlayerId() {
        const path = window.location.pathname;
        const filename = path.split('/').pop().split('.')[0].toLowerCase();
        
        console.log(`🔍 Detectando jugador desde: ${filename}`);
        
        if (PLAYER_IDS[filename]) {
            return PLAYER_IDS[filename];
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
            currentUser = {
                username: payload.sub,
                roles: payload.roles || payload.authorities || [],
                userId: payload.userId
            };
            
            console.log('✓ Usuario autenticado:', currentUser.username);
            showCommentForm();
        } catch (error) {
            console.error('Error al decodificar token:', error);
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
    try {
        const response = await fetch(
            `${API_BASE_URL}/comments/player/${currentPlayerId}?status=APPROVED&size=100`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        );
        
        if (response.ok) {
            const data = await response.json();
            commentsCache = data.content || [];
            console.log(`✓ ${commentsCache.length} comentarios cargados`);
            renderComments(commentsCache);
        } else {
            console.error('Error al cargar comentarios:', response.status);
            showCommentsError('No se pudieron cargar los comentarios');
        }
    } catch (error) {
        console.error('Error de red al cargar comentarios:', error);
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
 * Crea el HTML de un comentario
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
            ${isOwnComment && comment.status === 'APPROVED' ? `
                <div class="comment-actions">
                    <button class="btn-comment-delete" onclick="deleteComment(${comment.id})">
                        <i class="fas fa-trash"></i> Eliminar
                    </button>
                </div>
            ` : ''}
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
            <h3>💬 Dejar un Comentario</h3>
            <p class="comment-form-subtitle">Comparte tu opinión sobre este jugador</p>
            <form id="comment-form" class="comment-form">
                <div class="form-group">
                    <textarea 
                        id="comment-content" 
                        name="content" 
                        rows="4" 
                        placeholder="Escribe tu comentario aquí..."
                        maxlength="500"
                        required
                    ></textarea>
                    <div class="char-counter">
                        <span id="char-count">0</span> / 500 caracteres
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
        charCount.textContent = this.value.length;
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
        
        if (response.ok) {
            const newComment = await response.json();
            showNotification('¡Comentario enviado! Será publicado tras moderación.', 'success');
            form.reset();
            document.getElementById('char-count').textContent = '0';
            
            // Agregar comentario a la lista (con estado PENDING)
            commentsCache.unshift(newComment);
            renderComments(commentsCache);
        } else if (response.status === 401) {
            showNotification('Tu sesión ha expirado. Por favor inicia sesión de nuevo.', 'error');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        } else {
            const error = await response.json();
            showNotification(error.message || 'Error al enviar comentario', 'error');
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

// Exponer función para eliminar comentario globalmente
window.deleteComment = deleteComment;

} // Fin de verificación de carga
