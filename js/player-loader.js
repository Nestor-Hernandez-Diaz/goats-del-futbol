/**
 * GOATs del Fútbol - Player Loader Dinámico
 * Sistema de carga dinámica de jugadores desde API
 * @version 1.0
 * @date Diciembre 2025
 */

(function() {
  'use strict';

  // ============================================================================
  // CONFIGURACIÓN
  // ============================================================================

  const CONFIG = {
    API_BASE_URL: 'http://localhost:8080/api',
    FADE_DURATION: 300,
    SKELETON_MIN_DURATION: 500 // Mínimo tiempo visible del skeleton
  };

  // Mapeo de IDs de jugadores a temas CSS
  const PLAYER_THEMES = {
    1: { class: 'pagina-messi', hero: 'hero-messi' },
    2: { class: 'pagina-ronaldo', hero: 'hero-ronaldo' },
    3: { class: 'pagina-neymar', hero: 'hero-neymar' }
  };

  // Mapeo temporal de imágenes hero (hasta implementar en BD)
  const HERO_IMAGES = {
    1: '../assets/images/messi-hero.png',
    2: '../assets/images/ronaldo-hero.png',
    3: '../assets/images/neymar-hero.png'
  };

  // Estado global
  let currentPlayer = null;
  let loadStartTime = null;

  // ============================================================================
  // UTILIDADES
  // ============================================================================

  /**
   * Obtiene el parámetro 'id' de la URL
   * @returns {string|null} El ID del jugador o null si no existe
   */
  function getPlayerIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
  }

  /**
   * Registra mensajes en consola con prefijo
   * @param {string} message - Mensaje a registrar
   * @param {string} type - Tipo de log (info, warn, error)
   */
  function log(message, type = 'info') {
    const prefix = '[PlayerLoader]';
    const styles = {
      info: 'color: #4CAF50',
      warn: 'color: #FF9800',
      error: 'color: #F44336'
    };

    if (type === 'error') {
      console.error(`%c${prefix} ${message}`, styles[type]);
    } else if (type === 'warn') {
      console.warn(`%c${prefix} ${message}`, styles[type]);
    } else {
      console.log(`%c${prefix} ${message}`, styles[type]);
    }
  }

  /**
   * Escapa HTML para prevenir XSS
   * @param {string} text - Texto a escapar
   * @returns {string} Texto escapado
   */
  function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }

  /**
   * Espera un tiempo mínimo antes de resolver
   * @param {number} ms - Milisegundos a esperar
   * @returns {Promise}
   */
  function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  // ============================================================================
  // API
  // ============================================================================

  /**
   * Obtiene datos de un jugador desde la API
   * @param {string} playerId - ID del jugador
   * @returns {Promise<Object>} Datos del jugador
   */
  async function fetchPlayerData(playerId) {
    const url = `${CONFIG.API_BASE_URL}/players/${playerId}`;
    
    log(`Cargando jugador ID: ${playerId}`);

    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });

      // Manejar respuestas de error
      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('PLAYER_NOT_FOUND');
        } else if (response.status === 403) {
          throw new Error('FORBIDDEN');
        } else {
          throw new Error(`HTTP_ERROR_${response.status}`);
        }
      }

      const data = await response.json();
      log(`Jugador cargado: ${data.name}`);
      
      return data;

    } catch (error) {
      if (error.message.startsWith('HTTP_ERROR') || 
          error.message === 'PLAYER_NOT_FOUND' ||
          error.message === 'FORBIDDEN') {
        throw error;
      }
      
      // Error de red o CORS
      log(`Error de red: ${error.message}`, 'error');
      throw new Error('NETWORK_ERROR');
    }
  }

  // ============================================================================
  // RENDERIZADO
  // ============================================================================

  /**
   * Renderiza los datos del jugador en la página
   * @param {Object} player - Datos del jugador
   */
  function renderPlayerData(player) {
    log(`Renderizando datos de ${player.name}`);

    // 1. Meta tags SEO
    renderMetaTags(player);

    // 2. Hero section básico
    renderHeroSection(player);

    // 3. Hero info extendida (nacimiento, clubes, quote)
    renderHeroInfo(player);

    // 4. Biografía
    renderBiography(player);

    // 5. Imagen de perfil
    renderProfileImage(player);

    // 6. Estadísticas de perfil (sidebar)
    renderProfileStats(player);

    // 7. Momentos clave de la carrera
    renderCareerHighlights(player);

    // 8. NUEVA: Estilo de juego con atributos
    renderPlayingStyle(player);

    // 9. NUEVA: Logros y palmarés
    renderAchievements(player);

    // 10. Estadísticas resumidas (tarjetas)
    renderStatsSummary(player);

    // 11. Tabla de estadísticas por temporada
    renderStatsTable(player);

    // 12. NUEVA: Galería de imágenes
    renderGallery(player);

    // 13. NUEVA: Legado e impacto
    renderLegacy(player);

    // 14. NUEVA: Videos destacados
    renderVideos(player);

    // 15. Aplicar tema CSS dinámico
    applyPlayerTheme(player.id);

    // 16. Guardar jugador actual
    currentPlayer = player;

    // Exponer globalmente para otros scripts
    window.currentPlayerData = player;
    window.currentPlayerId = player.id;

    // Disparar evento personalizado
    const event = new CustomEvent('playerLoaded', { detail: player });
    window.dispatchEvent(event);

    log('Renderizado completo');
  }

  /**
   * Actualiza meta tags SEO
   * @param {Object} player - Datos del jugador
   */
  function renderMetaTags(player) {
    // Title
    const titleEl = document.getElementById('page-title');
    if (titleEl) {
      titleEl.textContent = `${player.name} "${player.nickname}" | GOATs del Fútbol`;
    }

    // Description
    const descEl = document.getElementById('page-description');
    if (descEl) {
      const shortBio = player.biography.substring(0, 150).replace(/<[^>]*>/g, '') + '...';
      descEl.setAttribute('content', shortBio);
    }

    // Keywords
    const keywordsEl = document.getElementById('page-keywords');
    if (keywordsEl) {
      const keywords = `${player.name}, ${player.nickname}, ${player.country}, ${player.position}, fútbol, GOATs`;
      keywordsEl.setAttribute('content', keywords);
    }
  }

  /**
   * Renderiza la sección hero
   * @param {Object} player - Datos del jugador
   */
  function renderHeroSection(player) {
    // Nombre
    const nameEl = document.getElementById('player-name');
    if (nameEl) {
      nameEl.textContent = player.name;
    }

    // Nickname
    const nicknameEl = document.getElementById('player-nickname-display');
    if (nicknameEl) {
      nicknameEl.textContent = `"${player.nickname}"`;
    }

    // País
    const countryEl = document.getElementById('player-country');
    if (countryEl) {
      countryEl.textContent = player.country;
    }

    // Posición
    const positionEl = document.getElementById('player-position');
    if (positionEl) {
      positionEl.textContent = player.position;
    }

    // Imagen hero (con mapeo temporal)
    const heroImageEl = document.getElementById('player-hero-image');
    if (heroImageEl && HERO_IMAGES[player.id]) {
      heroImageEl.innerHTML = `
        <img src="${HERO_IMAGES[player.id]}" 
             alt="${escapeHtml(player.name)} celebrando - Banner principal" 
             loading="lazy" 
             decoding="async" />
      `;
    }
  }

  /**
   * Renderiza la biografía
   * @param {Object} player - Datos del jugador
   */
  function renderBiography(player) {
    const bioEl = document.getElementById('player-biography');
    if (bioEl) {
      // La biografía viene como HTML, pero validamos
      if (player.biography && player.biography.trim().length > 0) {
        // Si contiene tags HTML, insertar directamente
        if (/<[^>]+>/.test(player.biography)) {
          bioEl.innerHTML = player.biography;
        } else {
          // Si es texto plano, convertir saltos de línea
          const paragraphs = player.biography.split('\n\n')
            .filter(p => p.trim())
            .map(p => `<p>${escapeHtml(p)}</p>`)
            .join('');
          bioEl.innerHTML = paragraphs || `<p>${escapeHtml(player.biography)}</p>`;
        }
      } else {
        bioEl.innerHTML = '<p><em>Biografía no disponible.</em></p>';
      }
    }
  }

  /**
   * Renderiza la tarjeta de perfil lateral
   * @param {Object} player - Datos del jugador
   */
  function renderProfileCard(player) {
    // Nombre completo
    const fullnameEl = document.getElementById('profile-fullname');
    if (fullnameEl) {
      fullnameEl.textContent = player.name;
    }

    // Nickname
    const nicknameEl = document.getElementById('profile-nickname');
    if (nicknameEl) {
      nicknameEl.textContent = player.nickname;
    }

    // País
    const countryEl = document.getElementById('profile-country');
    if (countryEl) {
      countryEl.textContent = player.country;
    }

    // Posición
    const positionEl = document.getElementById('profile-position');
    if (positionEl) {
      positionEl.textContent = player.position;
    }
  }

  /**
   * Aplica el tema CSS del jugador
   * @param {number} playerId - ID del jugador
   */
  function applyPlayerTheme(playerId) {
    const theme = PLAYER_THEMES[playerId];
    if (!theme) {
      log(`Tema no encontrado para jugador ID: ${playerId}`, 'warn');
      return;
    }

    // Aplicar clase al body
    const body = document.body;
    body.classList.add(theme.class);

    // Aplicar clase al hero
    const hero = document.getElementById('player-hero');
    if (hero) {
      hero.classList.add(theme.hero);
    }

    log(`Tema aplicado: ${theme.class}`);
  }

  // ============================================================================
  // NUEVAS FUNCIONES DE RENDERIZADO (SECCIONES COMPLETAS)
  // ============================================================================

  /**
   * Renderiza la sección de estilo de juego con atributos
   * @param {Object} player - Datos del jugador
   */
  function renderPlayingStyle(player) {
    if (!player.playingStyle || typeof player.playingStyle !== 'object') {
      document.getElementById('playing-style-section')?.remove();
      return;
    }

    // Descripción del estilo
    const descEl = document.getElementById('playing-style-description');
    if (descEl && player.playingStyle && player.playingStyle.description) {
      const paragraphs = player.playingStyle.description.split('\n\n')
        .filter(p => p.trim())
        .map(p => `<p>${escapeHtml(p)}</p>`)
        .join('');
      descEl.innerHTML = paragraphs;
    }

    // Atributos con barras de progreso
    const attributesEl = document.getElementById('player-attributes');
    if (attributesEl && player.playingStyle && player.playingStyle.attributes) {
      const attributesHTML = Object.entries(player.playingStyle.attributes)
        .map(([name, value]) => `
          <div class="atributo">
            <span class="nombre-atributo">${escapeHtml(name)}</span>
            <div class="barra-atributo">
              <div class="relleno-atributo w-${value}"></div>
            </div>
            <span class="valor-atributo">${value}/100</span>
          </div>
        `).join('');
      attributesEl.innerHTML = attributesHTML;
    }
  }

  /**
   * Renderiza la sección de logros y palmarés
   * @param {Object} player - Datos del jugador
   */
  function renderAchievements(player) {
    if (!player.achievements || typeof player.achievements !== 'object') {
      document.getElementById('achievements-section')?.remove();
      return;
    }

    // Logros de clubes
    const clubAchievementsEl = document.getElementById('club-achievements');
    if (clubAchievementsEl && player.achievements.clubs && Array.isArray(player.achievements.clubs)) {
      const clubsHTML = player.achievements.clubs.map(club => `
        <div class="logros-clubes">
          <div class="logotipo-club">
            <img src="${escapeHtml(club.logo)}" alt="Escudo oficial de ${escapeHtml(club.name)}" />
            <h4>${escapeHtml(club.name)}</h4>
          </div>
          <ul class="lista-logros">
            ${Array.isArray(club.titles) ? club.titles.map(title => `
              <li><span class="achievement-count">${escapeHtml(title.count)}</span> ${escapeHtml(title.name)}</li>
            `).join('') : ''}
          </ul>
        </div>
      `).join('');
      clubAchievementsEl.innerHTML = `<h3>Títulos de Club</h3>${clubsHTML}`;
    }

    // Logros nacionales
    const nationalAchievementsEl = document.getElementById('national-achievements');
    if (nationalAchievementsEl && player.achievements.national) {
      const nationalHTML = `
        <div class="logotipo-nacional">
          <img src="${escapeHtml(player.achievements.national.logo || '')}" alt="Escudo de ${escapeHtml(player.achievements.national.name || '')}" />
          <h4>${escapeHtml(player.achievements.national.name || '')}</h4>
        </div>
        <ul class="lista-logros">
          ${Array.isArray(player.achievements.national.titles) ? player.achievements.national.titles.map(title => `
            <li><span class="achievement-count">${escapeHtml(title.count)}</span> ${escapeHtml(title.name)}</li>
          `).join('') : ''}
        </ul>
      `;
      nationalAchievementsEl.innerHTML = `<h3>Títulos con Selección</h3>${nationalHTML}`;
    }

    // Premios individuales
    const individualAwardsEl = document.getElementById('individual-awards-list');
    if (individualAwardsEl && player.achievements.individual && Array.isArray(player.achievements.individual)) {
      const awardsHTML = player.achievements.individual.map(award => `
        <li><span class="achievement-count">${escapeHtml(award.count)}</span> ${escapeHtml(award.name)}</li>
      `).join('');
      individualAwardsEl.innerHTML = awardsHTML;
    }
    
    // Forzar visibilidad inmediata de elementos cargados dinámicamente
    setTimeout(() => {
      const achievementSections = document.querySelectorAll('#club-achievements .logros-clubes, #national-achievements .logotipo-nacional, #individual-achievements');
      achievementSections.forEach(section => {
        section.style.opacity = '1';
        section.style.transform = 'translateY(0)';
      });
    }, 100);
  }

  /**
   * Renderiza la sección de galería de imágenes
   * @param {Object} player - Datos del jugador
   */
  function renderGallery(player) {
    if (!player.gallery || !Array.isArray(player.gallery) || player.gallery.length === 0) {
      document.getElementById('gallery-section')?.remove();
      return;
    }

    const galleryGridEl = document.getElementById('gallery-grid');
    if (galleryGridEl) {
      const galleryHTML = player.gallery.map(image => `
        <div class="elemento-galeria">
          <img src="${escapeHtml(image.url)}" alt="${escapeHtml(image.alt)}" />
          <div class="leyenda-galeria">${escapeHtml(image.caption)}</div>
        </div>
      `).join('');
      galleryGridEl.innerHTML = galleryHTML;
      
      // Forzar visibilidad inmediata de elementos cargados dinámicamente
      setTimeout(() => {
        const galleryItems = galleryGridEl.querySelectorAll('.elemento-galeria');
        galleryItems.forEach(item => {
          item.style.opacity = '1';
          item.style.transform = 'translateY(0)';
        });
      }, 100);
    }
  }

  /**
   * Renderiza la sección de videos destacados
   * @param {Object} player - Datos del jugador
   */
  function renderVideos(player) {
    if (!player.videos || !Array.isArray(player.videos) || player.videos.length === 0) {
      document.getElementById('videos-section')?.remove();
      return;
    }

    const videosGridEl = document.getElementById('videos-grid');
    if (videosGridEl) {
      const videosHTML = player.videos.map(video => `
        <div class="elemento-video" data-video-url="${escapeHtml(video.url)}">
          <div class="miniatura-video">
            <img src="${escapeHtml(video.thumbnail)}" alt="${escapeHtml(video.title)}" loading="lazy" />
            <div class="boton-reproducir">▶</div>
          </div>
          <h3 class="titulo-video">${escapeHtml(video.title)}</h3>
        </div>
      `).join('');
      videosGridEl.innerHTML = videosHTML;

      // Event listener para abrir videos
      videosGridEl.querySelectorAll('.elemento-video').forEach(element => {
        element.addEventListener('click', function() {
          const videoUrl = this.dataset.videoUrl;
          if (videoUrl) {
            window.open(videoUrl, '_blank');
          }
        });
      });
    }
  }

  /**
   * Renderiza la sección de legado e impacto
   * @param {Object} player - Datos del jugador
   */
  function renderLegacy(player) {
    if (!player.legacy || typeof player.legacy !== 'object') {
      document.getElementById('legacy-section')?.remove();
      return;
    }

    // Texto del legado
    const legacyTextEl = document.getElementById('legacy-text');
    if (legacyTextEl && player.legacy && player.legacy.text) {
      const paragraphs = player.legacy.text.split('\n\n')
        .filter(p => p.trim())
        .map(p => `<p>${escapeHtml(p)}</p>`)
        .join('');
      legacyTextEl.innerHTML = paragraphs;
    }

    // Citas sobre el jugador
    const quotesContainerEl = document.getElementById('quotes-container');
    if (quotesContainerEl && player.legacy && player.legacy.quotes && Array.isArray(player.legacy.quotes)) {
      const quotesHTML = player.legacy.quotes.map(quote => `
        <div class="tarjeta-cita">
          <blockquote>"${escapeHtml(quote.text)}"</blockquote>
          <cite>— ${escapeHtml(quote.author)}</cite>
        </div>
      `).join('');
      quotesContainerEl.innerHTML = quotesHTML;
    }
  }

  /**
   * Renderiza la info hero extendida (nacimiento, clubes, etc.)
   * @param {Object} player - Datos del jugador
   */
  function renderHeroInfo(player) {
    const heroInfoEl = document.getElementById('hero-info');
    if (!heroInfoEl || !player.heroInfo) return;

    const infoHTML = `
      <div class="elemento-info">
        <span class="etiqueta-info">Nacimiento:</span>
        <span class="valor-info">${escapeHtml(player.heroInfo.birthDate || 'N/A')}</span>
      </div>
      <div class="elemento-info">
        <span class="etiqueta-info">Nacionalidad:</span>
        <span class="valor-info">${escapeHtml(player.country)}</span>
      </div>
      <div class="elemento-info">
        <span class="etiqueta-info">Posición:</span>
        <span class="valor-info">${escapeHtml(player.position)}</span>
      </div>
      <div class="elemento-info">
        <span class="etiqueta-info">Clubes:</span>
        <span class="valor-info">${escapeHtml(player.heroInfo.clubs || 'N/A')}</span>
      </div>
    `;
    heroInfoEl.innerHTML = infoHTML;

    // Quote hero
    if (player.heroInfo.quote) {
      const quoteContainer = document.getElementById('player-quote-container');
      const quoteEl = document.getElementById('player-quote');
      if (quoteContainer && quoteEl) {
        quoteEl.textContent = player.heroInfo.quote;
        quoteContainer.style.display = 'block';
      }
    }
  }

  /**
   * Renderiza momentos clave de la carrera
   * @param {Object} player - Datos del jugador
   */
  function renderCareerHighlights(player) {
    if (!player.careerHighlights || player.careerHighlights.length === 0) {
      document.getElementById('career-highlights')?.remove();
      return;
    }

    const highlightsListEl = document.getElementById('highlights-list');
    if (highlightsListEl) {
      const highlightsHTML = player.careerHighlights.map(highlight => `
        <li>
          <span class="año-destacado">${escapeHtml(highlight.year)}</span>
          <span class="evento-destacado">${escapeHtml(highlight.event)}</span>
        </li>
      `).join('');
      highlightsListEl.innerHTML = highlightsHTML;
    }
  }

  /**
   * Renderiza estadísticas resumidas (tarjetas)
   * @param {Object} player - Datos del jugador
   */
  function renderStatsSummary(player) {
    if (!player.stats) return;

    const statsSummaryEl = document.getElementById('stats-summary');
    if (statsSummaryEl) {
      const statsHTML = `
        <div class="tarjeta-estadistica">
          <div class="numero-estadistica">${escapeHtml(player.stats.goals || '0')}+</div>
          <div class="etiqueta-estadistica">Goles en carrera</div>
        </div>
        <div class="tarjeta-estadistica">
          <div class="numero-estadistica">${escapeHtml(player.stats.assists || '0')}+</div>
          <div class="etiqueta-estadistica">Asistencias</div>
        </div>
        <div class="tarjeta-estadistica">
          <div class="numero-estadistica">${escapeHtml(player.stats.matches || '0')}+</div>
          <div class="etiqueta-estadistica">Partidos jugados</div>
        </div>
        <div class="tarjeta-estadistica">
          <div class="numero-estadistica">${escapeHtml(player.stats.titles || '0')}</div>
          <div class="etiqueta-estadistica">Títulos ganados</div>
        </div>
      `;
      statsSummaryEl.innerHTML = statsHTML;
      
      // Forzar visibilidad inmediata de tarjetas cargadas dinámicamente
      setTimeout(() => {
        const statsCards = statsSummaryEl.querySelectorAll('.tarjeta-estadistica');
        statsCards.forEach(card => {
          card.style.opacity = '1';
          card.style.transform = 'translateY(0)';
        });
      }, 100);
    }
  }

  /**
   * Renderiza tabla de estadísticas por temporada
   * @param {Object} player - Datos del jugador
   */
  function renderStatsTable(player) {
    if (!player.seasonStats || player.seasonStats.length === 0) {
      document.getElementById('stats-details')?.remove();
      return;
    }

    const statsTableBodyEl = document.getElementById('stats-table-body');
    if (statsTableBodyEl) {
      const rowsHTML = player.seasonStats.map(season => `
        <tr>
          <td>${escapeHtml(season.season)}</td>
          <td>${escapeHtml(season.club)}</td>
          <td>${escapeHtml(season.matches)}</td>
          <td>${escapeHtml(season.goals)}</td>
          <td>${escapeHtml(season.assists)}</td>
        </tr>
      `).join('');
      statsTableBodyEl.innerHTML = rowsHTML;
    }
  }

  /**
   * Renderiza imagen de perfil en sidebar
   * @param {Object} player - Datos del jugador
   */
  function renderProfileImage(player) {
    const profileImageEl = document.getElementById('player-profile-image');
    if (profileImageEl && player.profileImage) {
      profileImageEl.innerHTML = `
        <img src="${escapeHtml(player.profileImage)}" 
             alt="${escapeHtml(player.name)} - Imagen de perfil" 
             loading="lazy" 
             decoding="async" />
      `;
    }
  }

  /**
   * Renderiza estadísticas de perfil (sidebar)
   * @param {Object} player - Datos del jugador
   */
  function renderProfileStats(player) {
    const profileStatsEl = document.getElementById('player-profile-stats');
    if (!profileStatsEl || !player.profileStats) return;

    const statsHTML = Object.entries(player.profileStats)
      .map(([label, value]) => `
        <li><span>${escapeHtml(label)}:</span> ${escapeHtml(value)}</li>
      `).join('');
    profileStatsEl.innerHTML = statsHTML;
  }

  // ============================================================================
  // ESTADOS DE CARGA
  // ============================================================================

  /**
   * Muestra el skeleton loader
   */
  function showSkeleton() {
    const skeleton = document.getElementById('skeleton-loader');
    const hero = document.getElementById('player-hero');
    const content = document.getElementById('main-content');
    
    if (skeleton) skeleton.style.display = 'block';
    if (hero) hero.style.display = 'none';
    if (content) content.style.display = 'none';
  }

  /**
   * Oculta el skeleton loader y muestra el contenido
   */
  async function hideSkeleton() {
    // Asegurar duración mínima del skeleton
    if (loadStartTime) {
      const elapsed = Date.now() - loadStartTime;
      const remaining = CONFIG.SKELETON_MIN_DURATION - elapsed;
      
      if (remaining > 0) {
        await delay(remaining);
      }
    }

    const skeleton = document.getElementById('skeleton-loader');
    const hero = document.getElementById('player-hero');
    const content = document.getElementById('main-content');
    
    if (skeleton) {
      skeleton.style.opacity = '0';
      setTimeout(() => {
        skeleton.style.display = 'none';
      }, CONFIG.FADE_DURATION);
    }
    
    // Mostrar hero section
    if (hero) {
      hero.style.display = 'block';
      hero.style.opacity = '0';
      setTimeout(() => {
        hero.style.opacity = '1';
      }, 10);
    }
    
    // Mostrar contenido principal
    if (content) {
      content.style.display = 'block';
      content.style.opacity = '0';
      setTimeout(() => {
        content.style.opacity = '1';
      }, 10);
    }
  }

  /**
   * Muestra la página de error 404
   */
  function showError404() {
    log('Mostrando error 404', 'warn');
    
    const skeleton = document.getElementById('skeleton-loader');
    const content = document.getElementById('main-content');
    const error404 = document.getElementById('error-404');
    
    if (skeleton) skeleton.style.display = 'none';
    if (content) content.style.display = 'none';
    if (error404) {
      error404.style.display = 'block';
      error404.style.opacity = '0';
      setTimeout(() => {
        error404.style.opacity = '1';
      }, 10);
    }

    // Actualizar title
    document.title = '404 - Jugador no encontrado | GOATs del Fútbol';
  }

  /**
   * Muestra la página de error general
   * @param {string} message - Mensaje de error personalizado
   */
  function showErrorGeneral(message = 'Ha ocurrido un error inesperado al cargar el jugador.') {
    log(`Mostrando error general: ${message}`, 'error');
    
    const skeleton = document.getElementById('skeleton-loader');
    const content = document.getElementById('main-content');
    const errorGeneral = document.getElementById('error-general');
    const errorMessage = document.getElementById('error-message');
    
    if (skeleton) skeleton.style.display = 'none';
    if (content) content.style.display = 'none';
    
    if (errorMessage) {
      errorMessage.textContent = message;
    }
    
    if (errorGeneral) {
      errorGeneral.style.display = 'block';
      errorGeneral.style.opacity = '0';
      setTimeout(() => {
        errorGeneral.style.opacity = '1';
      }, 10);
    }

    // Actualizar title
    document.title = 'Error - GOATs del Fútbol';
  }

  /**
   * Parsea campos JSON de strings a objetos
   * @param {Object} player - Datos del jugador
   */
  function parseJsonFields(player) {
    const jsonFields = [
      'heroInfo',
      'profileStats',
      'careerHighlights',
      'playingStyle',
      'achievements',
      'achievementsData',
      'statsData',
      'stats',
      'seasonStats',
      'gallery',
      'legacy',
      'videos'
    ];

    jsonFields.forEach(field => {
      if (player[field] && typeof player[field] === 'string') {
        try {
          player[field] = JSON.parse(player[field]);
        } catch (e) {
          log(`Error parseando ${field}: ${e.message}`, 'warn');
          player[field] = null;
        }
      }
    });

    log('Campos JSON parseados correctamente');
  }

  // ============================================================================
  // FLUJO PRINCIPAL
  // ============================================================================

  /**
   * Carga y renderiza el jugador
   */
  async function loadPlayer() {
    loadStartTime = Date.now();

    // 1. Obtener ID de la URL
    const playerId = getPlayerIdFromUrl();
    
    // 2. Validar que existe el parámetro
    if (!playerId) {
      log('No se especificó ID de jugador en URL', 'error');
      showError404();
      return;
    }

    // 3. Validar que sea un número
    if (!/^\d+$/.test(playerId)) {
      log(`ID inválido: ${playerId}`, 'error');
      showError404();
      return;
    }

    // 4. Mostrar skeleton
    showSkeleton();

    try {
      // 5. Fetch datos del jugador
      const playerData = await fetchPlayerData(playerId);

      // 5.5. Parsear campos JSON (de strings a objetos)
      parseJsonFields(playerData);

      // 6. Renderizar datos
      renderPlayerData(playerData);

      // 7. Ocultar skeleton y mostrar contenido
      await hideSkeleton();

    } catch (error) {
      // Log del error real para debugging
      log(`Error al cargar jugador: ${error.message}`, 'error');
      console.error('Stack trace:', error);

      // Manejar errores específicos
      if (error.message === 'PLAYER_NOT_FOUND') {
        showError404();
      } else if (error.message === 'NETWORK_ERROR') {
        showErrorGeneral('No se pudo conectar con el servidor. Por favor, verifica tu conexión a internet.');
      } else if (error.message === 'FORBIDDEN') {
        showErrorGeneral('No tienes permisos para acceder a este jugador.');
      } else {
        showErrorGeneral();
      }
    }
  }

  // ============================================================================
  // INICIALIZACIÓN
  // ============================================================================

  /**
   * Inicializa el sistema de carga de jugadores
   */
  function init() {
    log('Inicializando Player Loader...');

    // Verificar que estamos en player.html
    const isPlayerPage = window.location.pathname.includes('player.html');
    
    if (!isPlayerPage) {
      log('No estamos en player.html, saltando inicialización', 'warn');
      return;
    }

    // Cargar jugador cuando el DOM esté listo
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', loadPlayer);
    } else {
      loadPlayer();
    }
  }

  // Exponer funciones globales para debugging
  window.PlayerLoader = {
    reload: loadPlayer,
    getCurrentPlayer: () => currentPlayer,
    version: '1.0'
  };

  // Iniciar
  init();

})();
