/**
 * player-subscription.js - Botón de suscripción en páginas de jugadores
 * 
 * Este módulo agrega el botón de suscripción dinámicamente
 */

const API_URL = 'http://localhost:8080/api';

// Mapeo de páginas a IDs de jugadores
const PAGE_TO_PLAYER_ID = {
  'messi.html': 1,
  'ronaldo.html': 2,
  'neymar.html': 3
};

/**
 * Obtener ID del jugador según la página actual
 */
function getCurrentPlayerId() {
  const currentPage = window.location.pathname.split('/').pop();
  return PAGE_TO_PLAYER_ID[currentPage] || null;
}

/**
 * Verificar si el usuario está suscrito al jugador
 */
async function checkSubscriptionStatus(playerId) {
  try {
    const response = await fetch(`${API_URL}/subscriptions/player/${playerId}/check`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
        'Content-Type': 'application/json'
      }
    });

    if (response.ok) {
      return await response.json(); // true o false
    }
    return false;
  } catch (error) {
    console.error('Error checking subscription:', error);
    return false;
  }
}

/**
 * Obtener contador de suscriptores
 */
async function getSubscriberCount(playerId) {
  try {
    const response = await fetch(`${API_URL}/subscriptions/player/${playerId}/count`, {
      headers: { 'Content-Type': 'application/json' }
    });

    if (response.ok) {
      return await response.json();
    }
    return 0;
  } catch (error) {
    console.error('Error fetching subscriber count:', error);
    return 0;
  }
}

/**
 * Suscribirse al jugador
 */
async function subscribeToPlayer(playerId) {
  try {
    const response = await fetch(`${API_URL}/subscriptions/player/${playerId}`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
        'Content-Type': 'application/json'
      }
    });

    if (response.ok) {
      return true;
    } else if (response.status === 401) {
      alert('Debes iniciar sesión para suscribirte');
      window.location.href = 'login.html';
      return false;
    } else {
      const error = await response.text();
      console.error('Error subscribing:', error);
      return false;
    }
  } catch (error) {
    console.error('Error subscribing:', error);
    return false;
  }
}

/**
 * Desuscribirse del jugador
 */
async function unsubscribeFromPlayer(playerId) {
  try {
    const response = await fetch(`${API_URL}/subscriptions/player/${playerId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`,
        'Content-Type': 'application/json'
      }
    });

    return response.ok;
  } catch (error) {
    console.error('Error unsubscribing:', error);
    return false;
  }
}

/**
 * Toggle suscripción
 */
async function toggleSubscription(playerId) {
  const btn = document.getElementById('subscribeBtn');
  if (!btn) return;

  const isSubscribed = btn.dataset.subscribed === 'true';

  // Deshabilitar botón temporalmente
  btn.disabled = true;

  if (isSubscribed) {
    // Desuscribirse
    const success = await unsubscribeFromPlayer(playerId);
    if (success) {
      updateSubscribeButton(false, playerId);
      showToast('Dejaste de seguir a este jugador', 'info');
      updateSubscriberCount(playerId);
    } else {
      showToast('Error al cancelar suscripción', 'error');
    }
  } else {
    // Verificar autenticación
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      alert('Debes iniciar sesión para suscribirte');
      window.location.href = 'login.html';
      return;
    }

    // Suscribirse
    const success = await subscribeToPlayer(playerId);
    if (success) {
      updateSubscribeButton(true, playerId);
      showToast('¡Te suscribiste exitosamente! Recibirás notificaciones de sus logros.', 'success');
      updateSubscriberCount(playerId);
    } else {
      showToast('Error al suscribirte', 'error');
    }
  }

  btn.disabled = false;
}

/**
 * Actualizar UI del botón de suscripción
 */
function updateSubscribeButton(isSubscribed, playerId) {
  const btn = document.getElementById('subscribeBtn');
  if (!btn) return;

  btn.dataset.subscribed = isSubscribed;

  if (isSubscribed) {
    btn.innerHTML = '<i class="fas fa-check-circle"></i> Suscrito';
    btn.className = 'btn-subscribe subscribed';
  } else {
    btn.innerHTML = '<i class="fas fa-star"></i> Suscribirse';
    btn.className = 'btn-subscribe';
  }
}

/**
 * Actualizar contador de suscriptores
 */
async function updateSubscriberCount(playerId) {
  const countElement = document.getElementById('subscriberCount');
  if (!countElement) return;

  const count = await getSubscriberCount(playerId);
  countElement.textContent = count;
}

/**
 * Mostrar toast
 */
function showToast(message, type = 'info') {
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.style.cssText = `
    position: fixed;
    bottom: 30px;
    right: 30px;
    background: ${type === 'success' ? '#48bb78' : type === 'error' ? '#f56565' : '#4299e1'};
    color: white;
    padding: 15px 25px;
    border-radius: 10px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.2);
    z-index: 10000;
    animation: slideInRight 0.3s ease-out;
    max-width: 350px;
  `;
  toast.textContent = message;

  document.body.appendChild(toast);

  setTimeout(() => {
    toast.style.animation = 'slideOutRight 0.3s ease-out';
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

/**
 * Insertar botón de suscripción en la página
 */
function insertSubscribeButton() {
  const playerId = getCurrentPlayerId();
  if (!playerId) return;

  // Buscar el contenedor del título del héroe
  const heroText = document.querySelector('.texto-hero-jugador');
  if (!heroText) return;

  // Verificar si ya existe el botón
  if (document.getElementById('subscriptionContainer')) return;

  // Crear contenedor de suscripción
  const subscriptionContainer = document.createElement('div');
  subscriptionContainer.id = 'subscriptionContainer';
  subscriptionContainer.className = 'subscription-container';
  subscriptionContainer.innerHTML = `
    <button id="subscribeBtn" class="btn-subscribe" data-player-id="${playerId}" data-subscribed="false">
      <i class="fas fa-star"></i> Suscribirse
    </button>
    <span class="subscriber-info">
      <i class="fas fa-users"></i>
      <span id="subscriberCount">0</span> seguidores
    </span>
  `;

  // Insertar después del h1
  const titulo = heroText.querySelector('.titulo-hero-jugador');
  if (titulo) {
    titulo.parentNode.insertBefore(subscriptionContainer, titulo.nextSibling);
  } else {
    heroText.insertBefore(subscriptionContainer, heroText.firstChild);
  }

  // Agregar estilos si no existen
  addSubscriptionStyles();

  // Setup event listener
  const btn = document.getElementById('subscribeBtn');
  if (btn) {
    btn.addEventListener('click', () => toggleSubscription(playerId));
  }

  // Cargar estado inicial
  initializeSubscriptionState(playerId);
}

/**
 * Agregar estilos CSS
 */
function addSubscriptionStyles() {
  if (document.getElementById('subscription-styles')) return;

  const style = document.createElement('style');
  style.id = 'subscription-styles';
  style.textContent = `
    .subscription-container {
      display: flex;
      align-items: center;
      gap: 20px;
      margin: 20px 0;
      flex-wrap: wrap;
    }

    .btn-subscribe {
      background: linear-gradient(135deg, #f6ad55 0%, #ed8936 100%);
      color: white;
      border: none;
      padding: 12px 25px;
      border-radius: 25px;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 8px;
      transition: all 0.3s ease;
      box-shadow: 0 4px 15px rgba(246, 173, 85, 0.3);
    }

    .btn-subscribe:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(246, 173, 85, 0.5);
    }

    .btn-subscribe:disabled {
      opacity: 0.6;
      cursor: not-allowed;
      transform: none;
    }

    .btn-subscribe.subscribed {
      background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);
      box-shadow: 0 4px 15px rgba(72, 187, 120, 0.3);
    }

    .btn-subscribe.subscribed:hover {
      box-shadow: 0 6px 20px rgba(72, 187, 120, 0.5);
    }

    .subscriber-info {
      display: flex;
      align-items: center;
      gap: 8px;
      color: white;
      font-size: 1rem;
      font-weight: 600;
      background: rgba(255, 255, 255, 0.1);
      backdrop-filter: blur(10px);
      padding: 10px 20px;
      border-radius: 20px;
      border: 2px solid rgba(255, 255, 255, 0.2);
    }

    .subscriber-info i {
      font-size: 1.1rem;
    }

    @keyframes slideInRight {
      from {
        opacity: 0;
        transform: translateX(50px);
      }
      to {
        opacity: 1;
        transform: translateX(0);
      }
    }

    @keyframes slideOutRight {
      from {
        opacity: 1;
        transform: translateX(0);
      }
      to {
        opacity: 0;
        transform: translateX(50px);
      }
    }

    @media (max-width: 768px) {
      .subscription-container {
        flex-direction: column;
        align-items: flex-start;
        gap: 15px;
      }

      .btn-subscribe,
      .subscriber-info {
        width: 100%;
        justify-content: center;
      }
    }
  `;

  document.head.appendChild(style);
}

/**
 * Inicializar estado de suscripción
 */
async function initializeSubscriptionState(playerId) {
  // Actualizar contador de suscriptores
  await updateSubscriberCount(playerId);

  // Verificar si el usuario está autenticado
  const token = localStorage.getItem('jwtToken');
  if (!token) return;

  // Verificar si está suscrito
  const isSubscribed = await checkSubscriptionStatus(playerId);
  updateSubscribeButton(isSubscribed, playerId);
}

// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', insertSubscribeButton);
} else {
  insertSubscribeButton();
}

// Exponer funciones globales
window.PlayerSubscription = {
  toggleSubscription,
  getCurrentPlayerId,
  updateSubscriberCount
};
