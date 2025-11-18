/**
 * subscriptions.js - Gestión de Suscripciones
 * 
 * Este módulo maneja la visualización y gestión de suscripciones a jugadores
 */

const API_URL = 'http://localhost:8080/api';

// Mapeo de IDs de jugadores
const PLAYER_DATA = {
  1: {
    name: 'Lionel Messi',
    image: '../assets/images/messi-hero.jpg',
    page: 'messi.html'
  },
  2: {
    name: 'Cristiano Ronaldo',
    image: '../assets/images/ronaldo-hero.jpg',
    page: 'ronaldo.html'
  },
  3: {
    name: 'Neymar Jr',
    image: '../assets/images/neymar-hero.jpg',
    page: 'neymar.html'
  }
};

/**
 * Cargar suscripciones del usuario
 */
async function loadSubscriptions() {
  const loadingSpinner = document.getElementById('loadingSpinner');
  const subscriptionsGrid = document.getElementById('subscriptionsGrid');
  const emptyState = document.getElementById('emptyState');
  
  loadingSpinner.style.display = 'flex';
  subscriptionsGrid.innerHTML = '';
  emptyState.style.display = 'none';

  try {
    const user = window.AuthModule.getCurrentUser();
    if (!user) {
      window.location.href = 'login.html';
      return;
    }

    const response = await window.AuthModule.fetchWithAuth(
      `${API_URL}/subscriptions/user/${user.id}`
    );

    if (!response.ok) {
      throw new Error('Error al cargar suscripciones');
    }

    const subscriptions = await response.json();
    
    loadingSpinner.style.display = 'none';

    if (subscriptions.length === 0) {
      emptyState.style.display = 'block';
      updateStats(0, 0);
      return;
    }

    // Contar notificaciones habilitadas
    const notificationsEnabled = subscriptions.filter(s => s.notificationsEnabled).length;
    updateStats(subscriptions.length, notificationsEnabled);

    // Renderizar suscripciones
    subscriptions.forEach(subscription => {
      subscriptionsGrid.appendChild(createSubscriptionCard(subscription));
    });

  } catch (error) {
    console.error('Error loading subscriptions:', error);
    loadingSpinner.style.display = 'none';
    emptyState.style.display = 'block';
    emptyState.querySelector('h3').textContent = 'Error al cargar suscripciones';
    emptyState.querySelector('p').textContent = 'Intenta recargar la página.';
  }
}

/**
 * Crear tarjeta de suscripción
 */
function createSubscriptionCard(subscription) {
  const card = document.createElement('div');
  card.className = 'subscription-card';
  card.dataset.id = subscription.id;

  const playerData = PLAYER_DATA[subscription.playerId] || {
    name: `Jugador ${subscription.playerId}`,
    image: '../assets/images/default-player.jpg',
    page: '#'
  };

  const subscriptionDate = new Date(subscription.createdAt).toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });

  card.innerHTML = `
    <img src="${playerData.image}" alt="${playerData.name}" class="player-avatar" />
    <h3>${playerData.name}</h3>
    <span class="subscription-date">
      <i class="fas fa-calendar"></i>
      Desde ${subscriptionDate}
    </span>
    <div class="subscription-actions">
      <button class="btn-unsubscribe" onclick="unsubscribe(${subscription.playerId}, ${subscription.id})">
        <i class="fas fa-times"></i> Dejar de seguir
      </button>
    </div>
  `;

  // Click en la tarjeta para ir a la página del jugador
  card.style.cursor = 'pointer';
  card.addEventListener('click', (e) => {
    if (!e.target.closest('button')) {
      window.location.href = playerData.page;
    }
  });

  return card;
}

/**
 * Desuscribirse de un jugador
 */
async function unsubscribe(playerId, subscriptionId) {
  if (!confirm('¿Estás seguro que deseas dejar de seguir a este jugador?')) {
    return;
  }

  try {
    const response = await window.AuthModule.fetchWithAuth(
      `${API_URL}/subscriptions/player/${playerId}`,
      { method: 'DELETE' }
    );

    if (response.ok) {
      // Remover tarjeta de la UI
      const card = document.querySelector(`[data-id="${subscriptionId}"]`);
      if (card) {
        card.style.animation = 'slideOutRight 0.3s ease-out';
        setTimeout(() => {
          card.remove();
          
          // Verificar si quedaron suscripciones
          const remainingCards = document.querySelectorAll('.subscription-card');
          if (remainingCards.length === 0) {
            document.getElementById('emptyState').style.display = 'block';
            updateStats(0, 0);
          } else {
            // Actualizar estadísticas
            const total = remainingCards.length;
            updateStats(total, total); // Simplificado, recalcular si es necesario
          }
        }, 300);
      }

      showToast('Dejaste de seguir al jugador', 'success');
    } else {
      throw new Error('Error al cancelar suscripción');
    }
  } catch (error) {
    console.error('Error unsubscribing:', error);
    showToast('Error al cancelar suscripción', 'error');
  }
}

/**
 * Actualizar estadísticas
 */
function updateStats(total, notificationsEnabled) {
  const totalElement = document.getElementById('totalSubscriptions');
  const notifElement = document.getElementById('notificationsEnabled');

  if (totalElement) totalElement.textContent = total;
  if (notifElement) notifElement.textContent = notificationsEnabled;
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
 * Inicializar página de suscripciones
 */
function initSubscriptions() {
  // Verificar autenticación
  if (!window.AuthModule.isAuthenticated()) {
    window.location.href = 'login.html';
    return;
  }

  // Cargar suscripciones
  loadSubscriptions();
}

// Exponer funciones globales
window.unsubscribe = unsubscribe;

// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initSubscriptions);
} else {
  initSubscriptions();
}
