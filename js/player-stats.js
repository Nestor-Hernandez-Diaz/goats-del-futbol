/**
 * Player Stats - Carga dinámica de estadísticas
 * GOATs del Fútbol
 */

const API_BASE_URL = 'http://localhost:8080/api';

// Mapeo de jugadores a sus IDs
const PLAYER_IDS = {
  'messi': 1,
  'ronaldo': 2,
  'neymar': 3
};

/**
 * Obtiene el ID del jugador basado en la URL actual
 */
function getCurrentPlayerId() {
  const path = window.location.pathname;
  const filename = path.substring(path.lastIndexOf('/') + 1).replace('.html', '');
  return PLAYER_IDS[filename];
}

/**
 * Carga las estadísticas del jugador desde el backend
 */
async function loadPlayerStats() {
  const playerId = getCurrentPlayerId();
  
  if (!playerId) {
    console.warn('No se pudo determinar el ID del jugador');
    return;
  }

  try {
    console.log(`📊 Cargando estadísticas del jugador ID: ${playerId}`);
    
    const response = await fetch(`${API_BASE_URL}/stats/player/${playerId}`);
    
    if (!response.ok) {
      throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
    
    const stats = await response.json();
    console.log('✓ Estadísticas cargadas:', stats);
    
    // Actualizar las tarjetas de estadísticas
    updateStatsCards(stats);
    
    // Actualizar logros si existen
    await loadPlayerAchievements(playerId);
    
  } catch (error) {
    console.error('Error cargando estadísticas:', error);
    showStatsFallback();
  }
}

/**
 * Actualiza las tarjetas de estadísticas en el DOM
 */
function updateStatsCards(stats) {
  // Selector de tarjetas de estadísticas
  const statsCards = document.querySelectorAll('.tarjeta-estadistica');
  
  if (statsCards.length < 4) {
    console.warn('No se encontraron suficientes tarjetas de estadísticas');
    return;
  }

  // Actualizar goles
  const goalsCard = statsCards[0];
  const goalsNumber = goalsCard.querySelector('.numero-estadistica');
  if (goalsNumber) {
    goalsNumber.textContent = formatNumber(stats.goals);
    goalsNumber.classList.add('stat-loaded');
  }

  // Actualizar asistencias
  const assistsCard = statsCards[1];
  const assistsNumber = assistsCard.querySelector('.numero-estadistica');
  if (assistsNumber) {
    assistsNumber.textContent = formatNumber(stats.assists);
    assistsNumber.classList.add('stat-loaded');
  }

  // Actualizar partidos
  const matchesCard = statsCards[2];
  const matchesNumber = matchesCard.querySelector('.numero-estadistica');
  if (matchesNumber) {
    matchesNumber.textContent = formatNumber(stats.matchesPlayed);
    matchesNumber.classList.add('stat-loaded');
  }

  // Actualizar títulos (trophies)
  const trophiesCard = statsCards[3];
  const trophiesNumber = trophiesCard.querySelector('.numero-estadistica');
  if (trophiesNumber) {
    trophiesNumber.textContent = stats.trophies || 0;
    trophiesNumber.classList.add('stat-loaded');
  }

  console.log('✓ Tarjetas de estadísticas actualizadas');
}

/**
 * Carga los logros del jugador
 */
async function loadPlayerAchievements(playerId) {
  try {
    const response = await fetch(`${API_BASE_URL}/achievements/player/${playerId}`);
    
    if (!response.ok) {
      console.warn('No se pudieron cargar los logros');
      return;
    }
    
    const data = await response.json();
    const achievements = data.content || [];
    
    console.log(`✓ ${achievements.length} logros cargados`);
    
    // Aquí podrías actualizar una sección de logros si existe
    updateAchievementsSection(achievements);
    
  } catch (error) {
    console.error('Error cargando logros:', error);
  }
}

/**
 * Actualiza la sección de logros (opcional)
 */
function updateAchievementsSection(achievements) {
  // Buscar si existe una sección de logros
  const achievementsContainer = document.querySelector('.logros-destacados, .achievements-list');
  
  if (!achievementsContainer || achievements.length === 0) {
    return;
  }

  // Filtrar solo los últimos logros más importantes
  const recentAchievements = achievements
    .sort((a, b) => new Date(b.achievedAt) - new Date(a.achievedAt))
    .slice(0, 5);

  console.log(`✓ Mostrando ${recentAchievements.length} logros recientes`);
}

/**
 * Formatea números grandes (ej: 800+ si es mayor a cierto umbral)
 */
function formatNumber(num) {
  if (!num) return '0';
  
  // Si es un número muy grande, mostrar con +
  if (num >= 800) return '800+';
  if (num >= 500) return '500+';
  if (num >= 350) return '350+';
  if (num >= 100) return '100+';
  
  return num.toString();
}

/**
 * Muestra valores por defecto si falla la carga
 */
function showStatsFallback() {
  console.warn('⚠️ Usando estadísticas estáticas de respaldo');
  
  // Agregar clase de error para styling
  const statsSection = document.querySelector('.seccion-estadisticas');
  if (statsSection) {
    statsSection.classList.add('stats-fallback');
  }
}

/**
 * Agrega animación a las tarjetas cuando se cargan
 */
function animateStatsCards() {
  const statsCards = document.querySelectorAll('.tarjeta-estadistica');
  
  statsCards.forEach((card, index) => {
    setTimeout(() => {
      card.classList.add('animate-in');
    }, index * 100);
  });
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
  // Esperar un momento para que main.js se inicialice primero
  setTimeout(async () => {
    console.log('🎯 Inicializando carga de estadísticas dinámicas...');
    await loadPlayerStats();
    animateStatsCards();
  }, 500);
});

// Exponer funciones globales si es necesario
window.loadPlayerStats = loadPlayerStats;
window.getCurrentPlayerId = getCurrentPlayerId;
