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
    image: '../assets/images/messi-hero.png',
    page: 'messi.html'
  },
  2: {
    name: 'Cristiano Ronaldo',
    image: '../assets/images/ronaldo-hero.png',
    page: 'ronaldo.html'
  },
  3: {
    name: 'Neymar Jr',
    image: '../assets/images/neymar-hero.png',
    page: 'neymar.html'
  }
};

/**
 * Cargar suscripciones del usuario
 */
async function loadSubscriptions() {
  console.log('📡 Cargando suscripciones desde API...');
  
  const loadingSpinner = document.getElementById('loadingSpinner');
  const subscriptionsGrid = document.getElementById('subscriptionsGrid');
  const emptyState = document.getElementById('emptyState');
  
  if (!subscriptionsGrid) {
    console.error('❌ No se encontró elemento subscriptionsGrid');
    return;
  }
  
  loadingSpinner.style.display = 'flex';
  subscriptionsGrid.innerHTML = '';
  emptyState.style.display = 'none';

  try {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      console.log('❌ No hay token en loadSubscriptions');
      window.location.href = 'login.html';
      return;
    }

    // Obtener información del usuario desde el token
    const payload = JSON.parse(atob(token.split('.')[1]));
    const userId = payload.userId;
    
    console.log(`📨 Solicitando suscripciones ACTIVAS del usuario ${userId}...`);

    const response = await fetch(`${API_URL}/subscriptions/user/${userId}?active=true`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      console.error(`❌ Error en respuesta: ${response.status}`);
      throw new Error('Error al cargar suscripciones');
    }

    const subscriptionsData = await response.json();
    // El endpoint devuelve un objeto Page con content array
    const subscriptions = subscriptionsData.content || [];
    
    console.log(`✅ Suscripciones cargadas: ${subscriptions.length}`);
    
    loadingSpinner.style.display = 'none';

    if (subscriptions.length === 0) {
      emptyState.style.display = 'block';
      updateStats(0, 0);
      return;
    }

    // Contar notificaciones habilitadas (jugadores con notif activada)
    const notificationsEnabled = subscriptions.filter(s => s.notificationsEnabled).length;
    
    console.log(`📢 Estadísticas:`);
    console.log(`   • Total suscripciones activas: ${subscriptions.length}`);
    console.log(`   • Jugadores con notificaciones ON: ${notificationsEnabled}`);
    
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

  // Usar subscribedAt en lugar de createdAt
  const subscriptionDate = new Date(subscription.subscribedAt).toLocaleDateString('es-ES', {
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
      <button class="btn-unsubscribe" data-player-id="${subscription.playerId}" data-subscription-id="${subscription.id}">
        <i class="fas fa-times"></i> Dejar de seguir
      </button>
    </div>
  `;

  // Event listener para el botón de desuscribirse
  const unsubscribeBtn = card.querySelector('.btn-unsubscribe');
  unsubscribeBtn.addEventListener('click', (e) => {
    e.stopPropagation(); // Evitar que se active el click de la tarjeta
    const playerId = parseInt(e.currentTarget.dataset.playerId);
    const subscriptionId = parseInt(e.currentTarget.dataset.subscriptionId);
    handleUnsubscribe(playerId, subscriptionId);
  });

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
async function handleUnsubscribe(playerId, subscriptionId) {
  console.log(`🗑️ Intentando desuscribirse del jugador ${playerId}, subscription ${subscriptionId}`);
  
  if (!confirm('¿Estás seguro que deseas dejar de seguir a este jugador?')) {
    console.log('❌ Usuario canceló la desuscripción');
    return;
  }

  try {
    const token = localStorage.getItem('jwtToken');
    
    console.log(`📡 DELETE ${API_URL}/subscriptions/player/${playerId}`);
    
    const response = await fetch(`${API_URL}/subscriptions/player/${playerId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    console.log(`📨 Respuesta del servidor: ${response.status}`);

    if (response.ok) {
      console.log('✅ Desuscripción exitosa');
      showToast('Dejaste de seguir al jugador', 'success');
      
      // Disparar evento para sincronizar otras páginas
      window.dispatchEvent(new CustomEvent('subscriptionChanged', { 
        detail: { playerId, action: 'unsubscribe' } 
      }));
      
      // Recargar la lista completa desde el servidor
      setTimeout(() => {
        console.log('🔄 Recargando lista de suscripciones...');
        loadSubscriptions();
      }, 300);
    } else {
      const errorText = await response.text();
      console.error(`❌ Error en desuscripción: ${errorText}`);
      throw new Error('Error al cancelar suscripción');
    }
  } catch (error) {
    console.error('❌ Error unsubscribing:', error);
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
  console.log('🎯 Inicializando página de suscripciones...');
  
  // Verificar autenticación
  const token = localStorage.getItem('jwtToken');
  if (!token) {
    console.log('❌ No hay token, redirigiendo a login');
    window.location.href = 'login.html';
    return;
  }

  console.log('✅ Usuario autenticado, cargando suscripciones...');
  // Cargar suscripciones
  loadSubscriptions();
}

// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initSubscriptions);
} else {
  initSubscriptions();
}

// Escuchar cambios en suscripciones desde otras páginas
window.addEventListener('subscriptionChanged', () => {
  console.log('🔔 Evento subscriptionChanged recibido');
  // Recargar suscripciones si estamos en la página de suscripciones
  if (window.location.pathname.includes('subscriptions.html')) {
    console.log('🔄 Recargando suscripciones por evento...');
    loadSubscriptions();
  }
});
