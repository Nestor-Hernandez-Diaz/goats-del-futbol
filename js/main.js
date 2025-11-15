/**
 * GOATs del Fútbol - JavaScript Principal
 * @author Nestor Hernández Díaz
 * @version 1.0.0
 * @date Noviembre 2025
 * 
 * Implementación Mes 1 - Semana 1: Base JS y navegación accesible
 */

(function() {
  'use strict';

  /**
   * Índice de secciones
   * 1) Utilidades Generales (Utils)
   * 2) Navegación (Navigation)
   * 3) Scroll Suave (SmoothScroll)
   * 4) Volver Arriba (BackToTop)
   * 5) Animaciones de Scroll (ScrollAnimations)
   * 6) Optimización de Imágenes (ImageOptimization)
   * 7) Newsletter (Newsletter)
   * 8) Lightbox de Galería (Lightbox)
   * 9) Modal de Videos (VideoModal)
   * 10) ARIA Live Regions (AriaLive)
   * 11) Inicialización Principal (init)
   */

  // ============================================================================
  // UTILIDADES GENERALES
  // ============================================================================
  
  /**
   * Utilidades compartidas para selección de nodos, control de scroll,
   * optimización de eventos (debounce) y gestión de foco accesible.
   */
  const Utils = {
    /**
     * Selector de elementos con validación
     */
    qs: (selector, parent = document) => parent.querySelector(selector),
    
    /**
     * Selector múltiple de elementos
     */
    qsa: (selector, parent = document) => parent.querySelectorAll(selector),
    
    /**
     * Bloquear/desbloquear scroll del body
     */
    toggleBodyScroll: (lock) => {
      document.body.classList.toggle('no-scroll', !!lock);
    },
    
    /**
     * Debounce para optimizar eventos
     */
    debounce: (func, wait = 300) => {
      let timeout;
      return function executedFunction(...args) {
        const later = () => {
          clearTimeout(timeout);
          func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
      };
    },
    
    /**
     * Focus Trap para modales y overlays
     * @param {Element} element - Elemento a trapear el foco
     * @returns {Function} - Función para remover el trap
     */
    createFocusTrap: (element) => {
      const focusableSelector = 'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])';
      let focusableElements = [];
      let firstFocusable = null;
      let lastFocusable = null;
      
      const updateFocusableElements = () => {
        focusableElements = Array.from(element.querySelectorAll(focusableSelector))
          .filter(el => !el.disabled && el.offsetParent !== null);
        firstFocusable = focusableElements[0];
        lastFocusable = focusableElements[focusableElements.length - 1];
      };
      
      const handleKeyDown = (e) => {
        if (e.key !== 'Tab') return;
        
        updateFocusableElements();
        
        if (e.shiftKey) {
          // Shift + Tab
          if (document.activeElement === firstFocusable) {
            e.preventDefault();
            lastFocusable?.focus();
          }
        } else {
          // Tab
          if (document.activeElement === lastFocusable) {
            e.preventDefault();
            firstFocusable?.focus();
          }
        }
      };
      
      element.addEventListener('keydown', handleKeyDown);
      updateFocusableElements();
      firstFocusable?.focus();
      
      // Retornar función para limpiar
      return () => {
        element.removeEventListener('keydown', handleKeyDown);
      };
    }
  };

  // ============================================================================
  // NAVEGACIÓN Y MENÚ HAMBURGUESA
  // ============================================================================
  
  /**
   * Gestiona el menú hamburguesa, enlaces de navegación y manejo de teclado.
   * Usa Utils para selección y bloqueo de scroll.
   */
  const Navigation = {
    burger: null,
    navLinks: null,
    
    init() {
      this.burger = Utils.qs('#burger-toggle');
      this.navLinks = Utils.qs('.enlaces-navegacion');
      
      if (!this.burger || !this.navLinks) {
        console.warn('⚠️ Elementos de navegación no encontrados');
        return;
      }
      
      this.setupBurgerMenu();
      this.setupNavLinks();
      this.setupKeyboardNav();
      console.log('✅ Navegación inicializada');
    },
    
    setupBurgerMenu() {
      this.burger.addEventListener('change', () => {
        const isOpen = this.burger.checked;
        Utils.toggleBodyScroll(isOpen);
        
        // ARIA para accesibilidad
        this.navLinks.setAttribute('aria-hidden', !isOpen);
        
        if (isOpen) {
          const firstLink = Utils.qs('a', this.navLinks);
          if (firstLink) setTimeout(() => firstLink.focus(), 100);
        }
      });
    },
    
    setupNavLinks() {
      this.navLinks.addEventListener('click', (e) => {
        if (e.target.tagName === 'A') {
          this.closeMenu();
        }
      });
    },
    
    setupKeyboardNav() {
      document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && this.burger.checked) {
          this.closeMenu();
          this.burger.focus();
        }
      });
    },
    
    closeMenu() {
      this.burger.checked = false;
      Utils.toggleBodyScroll(false);
      this.navLinks.setAttribute('aria-hidden', 'true');
    }
  };

  // ============================================================================
  // SMOOTH SCROLL
  // ============================================================================
  
  /**
   * Implementa desplazamiento suave hacia anclas, respetando
   * 'prefers-reduced-motion'. Funciona con jQuery (si está disponible)
   * y con fallback en JavaScript nativo.
   */
  const SmoothScroll = {
    offset: 80, // Altura del header fijo
    
    init() {
      if (window.jQuery) {
        const $ = window.jQuery;
        $('body').on('click', 'a[href^="#"]', (e) => {
          const link = e.currentTarget;
          const href = link.getAttribute('href');
          if (href === '#' || href === '#!') return;
          const target = Utils.qs(href);
          if (!target) return;
          e.preventDefault();
          this.scrollToElement(target);
        });
      } else {
        document.addEventListener('click', (e) => {
          const link = e.target.closest('a[href^="#"]');
          if (!link) return;
          const href = link.getAttribute('href');
          if (href === '#' || href === '#!') return;
          const target = Utils.qs(href);
          if (!target) return;
          e.preventDefault();
          this.scrollToElement(target);
        });
      }
      console.log('✅ Smooth scroll inicializado');
    },
    
    scrollToElement(element) {
      const targetPosition = element.getBoundingClientRect().top + window.pageYOffset;
      const offsetPosition = targetPosition - this.offset;
      
      const prefersReduced = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
      window.scrollTo({
        top: offsetPosition,
        behavior: prefersReduced ? 'auto' : 'smooth'
      });
      
      // Manejar foco para accesibilidad
      setTimeout(() => {
        element.setAttribute('tabindex', '-1');
        element.focus();
        element.removeAttribute('tabindex');
      }, 500);
    }
  };

  // ============================================================================
  // BOTÓN VOLVER ARRIBA
  // ============================================================================
  
  /**
   * Crea un botón flotante para volver al tope de la página.
   * Observa el sentinel superior para alternar visibilidad.
   */
  const BackToTop = {
    button: null,
    sentinel: null,
    observer: null,
    
    init() {
      this.createSentinel();
      this.createButton();
      this.setupObserver();
      this.setupClickHandler();
      console.log('✅ Botón volver arriba inicializado');
    },
    
    createSentinel() {
      this.sentinel = document.createElement('div');
      this.sentinel.id = 'top-sentinel';
      this.sentinel.className = 'top-sentinel';
      document.body.prepend(this.sentinel);
    },
    
    createButton() {
      this.button = document.createElement('button');
      this.button.id = 'back-to-top';
      this.button.type = 'button';
      this.button.className = 'back-to-top';
      this.button.setAttribute('aria-label', 'Volver arriba');
      this.button.title = 'Volver arriba';
      this.button.textContent = '↑';
      document.body.appendChild(this.button);
    },
    
    setupObserver() {
      this.observer = new IntersectionObserver(
        (entries) => {
          const entry = entries[0];
          if (entry.isIntersecting) {
            this.button.classList.remove('is-visible');
            this.button.setAttribute('aria-hidden', 'true');
          } else {
            this.button.classList.add('is-visible');
            this.button.setAttribute('aria-hidden', 'false');
          }
        },
        { root: null, threshold: 0 }
      );
      this.observer.observe(this.sentinel);
    },
    
    setupClickHandler() {
      const prefersReduced = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
      const scrollHandler = (e) => {
        e.preventDefault();
        window.scrollTo({ top: 0, behavior: prefersReduced ? 'auto' : 'smooth' });
      };

      if (window.jQuery) {
        const $btn = jQuery(this.button);
        $btn.off('click.backToTop').on('click.backToTop', scrollHandler);
      } else {
        this.button.addEventListener('click', scrollHandler);
      }
    }
  };

  // ============================================================================
  // INTERSECTION OBSERVER PARA ANIMACIONES
  // ============================================================================
  
  /**
   * Aplica animaciones al revelar elementos en viewport y
   * efecto staggered en grupos de tarjetas, galería y videos.
   */
  const ScrollAnimations = {
    observer: null,
    staggerObserver: null,
    
    init() {
      // Observar elementos con [data-reveal]
      const revealTargets = Utils.qsa('[data-reveal]');
      
      if (revealTargets.length) {
        this.observer = new IntersectionObserver(
          (entries, obs) => {
            entries.forEach((entry) => {
              if (entry.isIntersecting) {
                entry.target.classList.add('is-visible');
                obs.unobserve(entry.target);
              }
            });
          },
          { threshold: 0.1, rootMargin: '0px 0px -50px 0px' }
        );
        
        revealTargets.forEach((el) => this.observer.observe(el));
        console.log(`✅ Animaciones de scroll inicializadas (${revealTargets.length} elementos)`);
      }
      
      // Observar secciones principales
      this.observeSections();
      
      // Animaciones staggered para tarjetas y elementos de galería
      this.setupStaggeredAnimations();
    },
    
    observeSections() {
      const sections = Utils.qsa('section');
      if (!sections.length) return;
      
      const sectionObserver = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              entry.target.classList.add('animate-in');
            }
          });
        },
        { threshold: 0.05 }
      );
      
      sections.forEach((el) => sectionObserver.observe(el));
    },
    
    setupStaggeredAnimations() {
      // Seleccionar grupos de elementos para efecto cascada
      const staggerGroups = [
        { selector: '.seccion-jugadores .tarjeta-jugador', delay: 150 },
        { selector: '.cuadricula-galeria .elemento-galeria', delay: 100 },
        { selector: '.cuadricula-videos .elemento-video', delay: 120 },
        { selector: '.comparison-section .stat-card', delay: 100 }
      ];
      
      staggerGroups.forEach(group => {
        const elements = Utils.qsa(group.selector);
        if (!elements.length) return;
        
        // Determinar clase de paso según delay
        const stepClass = group.delay === 150
          ? 'stagger-step-150'
          : group.delay === 120
            ? 'stagger-step-120'
            : 'stagger-step-100';
        
        // Aplicar clases por índice (sin estilos inline)
        elements.forEach((el, index) => {
          const idx = Math.min(index, 24); // clamp para evitar clases excesivas
          el.classList.add('stagger-item', `stagger-i-${idx}`);
        });
        
        // Observar el contenedor padre
        const containers = new Set();
        elements.forEach(el => {
          const container = el.parentElement;
          if (container && !containers.has(container)) {
            containers.add(container);
            this.observeStaggerContainer(container);
          }
        });
        // Añadir clase de paso al contenedor para heredar variable CSS
        containers.forEach(container => {
          container.classList.add(stepClass, 'stagger-group');
        });
        
        console.log(`✅ Animación staggered aplicada a ${elements.length} elementos (${group.selector})`);
      });
    },
    
    observeStaggerContainer(container) {
      if (!this.staggerObserver) {
        this.staggerObserver = new IntersectionObserver(
          (entries) => {
            entries.forEach((entry) => {
              if (entry.isIntersecting) {
                const items = Utils.qsa('.stagger-item', entry.target);
                items.forEach(item => item.classList.add('stagger-visible'));
                this.staggerObserver.unobserve(entry.target);
              }
            });
          },
          { threshold: 0.1 }
        );
      }
      
      this.staggerObserver.observe(container);
    }
  };

  // ============================================================================
  // OPTIMIZACIÓN DE IMÁGENES
  // ============================================================================
  
  /**
   * Refuerza lazy loading y decoding, aplica skeleton loaders,
   * y detecta soporte para WebP/AVIF.
   */
  const ImageOptimization = {
    init() {
      const imgs = Utils.qsa('img');
      let count = 0;
      
      imgs.forEach((img) => {
        if (!img.hasAttribute('loading')) {
          img.setAttribute('loading', 'lazy');
          count++;
        }
        if (!img.hasAttribute('decoding')) {
          img.setAttribute('decoding', 'async');
        }
      });
      
      if (count > 0) {
        console.log(`✅ Lazy loading aplicado a ${count} imágenes`);
      }
      
      // Inicializar skeleton screens
      this.setupSkeletonScreens();
      
      // Detectar soporte de formatos modernos
      this.detectImageFormatSupport();
    },
    
    setupSkeletonScreens() {
      const lazyImages = Utils.qsa('img[loading="lazy"]');
      
      lazyImages.forEach((img) => {
        // No aplicar a imágenes ya cargadas
        if (img.complete && img.naturalHeight !== 0) return;
        
        // Crear skeleton wrapper
        const wrapper = document.createElement('div');
        wrapper.className = 'skeleton-wrapper';
        
        // Mantener aspect ratio
        const aspectRatio = (img.dataset.aspectRatio || '16/9').trim();
        const ratioClassMap = {
          '16/9': 'ratio-16-9',
          '3/4': 'ratio-3-4',
          '4/3': 'ratio-4-3',
          '1/1': 'ratio-1-1'
        };
        const ratioClass = ratioClassMap[aspectRatio] || 'ratio-16-9';
        wrapper.classList.add(ratioClass);
        
        // Envolver imagen
        img.parentNode.insertBefore(wrapper, img);
        wrapper.appendChild(img);
        
        // Añadir clase para ocultar imagen mientras carga
        img.classList.add('skeleton-img');
        
        // Remover skeleton cuando la imagen cargue
        const handleLoad = () => {
          img.classList.add('is-loaded');
          setTimeout(() => {
            if (wrapper.parentNode) {
              wrapper.replaceWith(img);
            }
          }, 400);
          img.removeEventListener('load', handleLoad);
          img.removeEventListener('error', handleError);
        };
        
        const handleError = () => {
          // En caso de error, mostrar placeholder semántico
          const errorEl = document.createElement('div');
          errorEl.className = 'skeleton-error';
          errorEl.textContent = '📷';
          wrapper.innerHTML = '';
          wrapper.appendChild(errorEl);
          img.removeEventListener('load', handleLoad);
          img.removeEventListener('error', handleError);
        };
        
        img.addEventListener('load', handleLoad);
        img.addEventListener('error', handleError);
      });
      
      console.log(`✅ Skeleton screens aplicados a ${lazyImages.length} imágenes`);
    },
    
    detectImageFormatSupport() {
      // Detectar soporte WebP
      const webpTest = new Image();
      webpTest.onload = webpTest.onerror = () => {
        const hasWebP = webpTest.height === 2;
        if (hasWebP) {
          document.documentElement.classList.add('webp');
          console.log('✅ Soporte WebP detectado');
        }
      };
      webpTest.src = 'data:image/webp;base64,UklGRjoAAABXRUJQVlA4IC4AAACyAgCdASoCAAIALmk0mk0iIiIiIgBoSygABc6WWgAA/veff/0PP8bA//LwYAAA';
      
      // Detectar soporte AVIF
      const avifTest = new Image();
      avifTest.onload = avifTest.onerror = () => {
        const hasAVIF = avifTest.height === 2;
        if (hasAVIF) {
          document.documentElement.classList.add('avif');
          console.log('✅ Soporte AVIF detectado');
        }
      };
      avifTest.src = 'data:image/avif;base64,AAAAIGZ0eXBhdmlmAAAAAGF2aWZtaWYxbWlhZk1BMUIAAADybWV0YQAAAAAAAAAoaGRscgAAAAAAAAAAcGljdAAAAAAAAAAAAAAAAGxpYmF2aWYAAAAADnBpdG0AAAAAAAEAAAAeaWxvYwAAAABEAAABAAEAAAABAAABGgAAAB0AAAAoaWluZgAAAAAAAQAAABppbmZlAgAAAAABAABhdjAxQ29sb3IAAAAAamlwcnAAAABLaXBjbwAAABRpc3BlAAAAAAAAAAIAAAACAAAAEHBpeGkAAAAAAwgICAAAAAxhdjFDgQ0MAAAAABNjb2xybmNseAACAAIAAYAAAAAXaXBtYQAAAAAAAAABAAEEAQKDBAAAACVtZGF0EgAKCBgANogQEAwgMg8f8D///8WfhwB8+ErK42A=';
    }
  };

  // ============================================================================
  // VALIDACIÓN DE FORMULARIO NEWSLETTER
  // ============================================================================
  
  /**
   * Valida emails con feedback en tiempo real y simula suscripción.
   * Anuncia estados mediante ARIA Live cuando está disponible.
   */
  const Newsletter = {
    form: null,
    input: null,
    button: null,
    isValidating: false,
    
    init() {
      this.form = Utils.qs('.formulario-newsletter');
      if (!this.form) return;
      
      this.input = Utils.qs('input[type="email"]', this.form);
      this.button = Utils.qs('button[type="submit"]', this.form);
      
      if (this.input) {
        this.setupValidation();
        this.setupRealtimeValidation();
        console.log('✅ Validación de newsletter inicializada con feedback en tiempo real');
      }
    },
    
    setupValidation() {
      this.form.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const email = this.input.value.trim();
        
        if (!this.validateEmail(email)) {
          this.showError('Por favor, ingresa un email válido');
          return;
        }
        
        await this.subscribe(email);
      });
    },
    
    setupRealtimeValidation() {
      // Validación mientras escribe (con debounce)
      this.input.addEventListener('input', Utils.debounce(() => {
        const email = this.input.value.trim();
        
        if (email.length === 0) {
          this.clearError();
          this.clearSuccess();
          return;
        }
        
        if (email.length > 3) {
          if (this.validateEmail(email)) {
            this.showRealtimeSuccess();
          } else {
            this.showRealtimeError();
          }
        }
      }, 500));
      
      // Limpiar mensajes al enfocar
      this.input.addEventListener('focus', () => {
        this.clearMessages();
      });
      
      // Validar al perder foco
      this.input.addEventListener('blur', () => {
        const email = this.input.value.trim();
        if (email.length > 0 && !this.validateEmail(email)) {
          this.showRealtimeError();
        }
      });
    },
    
    validateEmail(email) {
      // Regex más estricta para emails
      const regex = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
      return regex.test(email);
    },
    
    async subscribe(email) {
      if (this.isValidating) return;
      
      this.isValidating = true;
      this.button.disabled = true;
      this.button.textContent = 'Enviando...';
      
      // Simular llamada async (sustituir por fetch real en Mes 2)
      try {
        await this.simulateAPICall(email);
        
        this.showSuccess('¡Gracias por suscribirte! Recibirás noticias muy pronto.');
        this.form.reset();
        
        // Placeholder para futura integración
        /*
        const response = await fetch('/api/newsletter/subscribe.php', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email })
        });
        const data = await response.json();
        
        if (response.ok) {
          this.showSuccess(data.message);
          this.form.reset();
        } else {
          this.showError(data.error || 'Error al suscribirse');
        }
        */
      } catch (err) {
        this.showError('Error al procesar tu suscripción. Intenta nuevamente.');
        console.error('Newsletter error:', err);
      } finally {
        this.isValidating = false;
        this.button.disabled = false;
        this.button.textContent = 'Suscribirse';
      }
    },
    
    simulateAPICall(email) {
      return new Promise((resolve) => {
        console.log('📧 Simulando suscripción:', email);
        setTimeout(resolve, 1500); // Simular delay de red
      });
    },
    
    showRealtimeError() {
      this.input.classList.add('error');
      this.input.classList.remove('success');
      
      // Opcional: mostrar icono de error
      const icon = this.getOrCreateIcon();
      icon.textContent = '✕';
      icon.classList.add('is-error');
      icon.classList.remove('is-success');
    },
    
    showRealtimeSuccess() {
      this.input.classList.remove('error');
      this.input.classList.add('success');
      
      // Opcional: mostrar icono de éxito
      const icon = this.getOrCreateIcon();
      icon.textContent = '✓';
      icon.classList.add('is-success');
      icon.classList.remove('is-error');
    },
    
    getOrCreateIcon() {
      let icon = this.form.querySelector('.validation-icon');
      
      if (!icon) {
        icon = document.createElement('span');
        icon.className = 'validation-icon';
        this.form.appendChild(icon);
      }
      
      return icon;
    },
    
    showError(message) {
      this.clearMessages();
      const errorDiv = document.createElement('div');
      errorDiv.className = 'newsletter-error';
      errorDiv.textContent = message;
      errorDiv.setAttribute('role', 'alert');
      this.form.appendChild(errorDiv);
      this.input.classList.add('error');
      this.input.focus();
      
      // Anunciar error a lectores de pantalla
      if (window.GOATsApp?.ariaLive) {
        window.GOATsApp.ariaLive.announceUrgent(message);
      }
    },
    
    showSuccess(message) {
      this.clearMessages();
      const successDiv = document.createElement('div');
      successDiv.className = 'newsletter-success';
      successDiv.textContent = message;
      successDiv.setAttribute('role', 'status');
      this.form.appendChild(successDiv);
      
      // Limpiar el icono de validación
      const icon = this.form.querySelector('.validation-icon');
      if (icon) icon.remove();
      
      // Anunciar éxito a lectores de pantalla
      if (window.GOATsApp?.ariaLive) {
        window.GOATsApp.ariaLive.announce(message);
      }
    },
    
    clearMessages() {
      const messages = Utils.qsa('.newsletter-error, .newsletter-success', this.form);
      messages.forEach((msg) => msg.remove());
    },
    
    clearError() {
      this.input.classList.remove('error');
      const errors = Utils.qsa('.newsletter-error', this.form);
      errors.forEach((err) => err.remove());
    },
    
    clearSuccess() {
      this.input.classList.remove('success');
      const icon = this.form.querySelector('.validation-icon');
      if (icon) icon.remove();
    }
  };

  // ============================================================================
  // LIGHTBOX PARA GALERÍA
  // ============================================================================
  
  /**
   * Visor de imágenes accesible con overlay, teclado y gestos básicos.
   * Preferencia por jQuery para delegación; fallback a Vanilla.
   */
  const Lightbox = {
    overlay: null,
    content: null,
    image: null,
    currentIndex: 0,
    images: [],
    removeFocusTrap: null,
    
    init() {
      const galleryImages = Utils.qsa('.elemento-galeria img, .galeria-imagenes img');
      
      if (!galleryImages.length) {
        console.warn('⚠️ No se encontraron imágenes para lightbox');
        return;
      }
      
      this.images = Array.from(galleryImages);
      this.createLightbox();
      this.setupEventListeners();
      console.log(`✅ Lightbox inicializado con ${this.images.length} imágenes`);
    },
    
    createLightbox() {
      // Crear overlay
      this.overlay = document.createElement('div');
      this.overlay.className = 'lightbox-overlay';
      this.overlay.setAttribute('role', 'dialog');
      this.overlay.setAttribute('aria-modal', 'true');
      this.overlay.setAttribute('aria-label', 'Visor de imágenes');
      
      // Crear contenedor de contenido
      this.content = document.createElement('div');
      this.content.className = 'lightbox-content';
      
      // Crear imagen
      this.image = document.createElement('img');
      this.image.setAttribute('alt', 'Imagen ampliada');
      
      // Crear contador
      const counter = document.createElement('div');
      counter.className = 'lightbox-counter';
      
      // Botón cerrar
      const closeBtn = document.createElement('button');
      closeBtn.className = 'lightbox-close';
      closeBtn.innerHTML = '×';
      closeBtn.setAttribute('aria-label', 'Cerrar lightbox');
      
      // Botón anterior
      const prevBtn = document.createElement('button');
      prevBtn.className = 'lightbox-prev';
      prevBtn.innerHTML = '‹';
      prevBtn.setAttribute('aria-label', 'Imagen anterior');
      
      // Botón siguiente
      const nextBtn = document.createElement('button');
      nextBtn.className = 'lightbox-next';
      nextBtn.innerHTML = '›';
      nextBtn.setAttribute('aria-label', 'Imagen siguiente');
      
      // Ensamblar lightbox
      this.content.appendChild(this.image);
      this.content.appendChild(counter);
      this.overlay.appendChild(closeBtn);
      this.overlay.appendChild(prevBtn);
      this.overlay.appendChild(nextBtn);
      this.overlay.appendChild(this.content);
      document.body.appendChild(this.overlay);
      
      // Guardar referencias a elementos
      this.closeBtn = closeBtn;
      this.prevBtn = prevBtn;
      this.nextBtn = nextBtn;
      this.counter = counter;
    },
    
    setupEventListeners() {
      // jQuery si está disponible; fallback a Vanilla
      if (window.jQuery) {
        const $ = window.jQuery;
        $(this.images)
          .each((index, img) => {
            $(img).on('click', () => this.open(index));
          });

        $(this.closeBtn).on('click', () => this.close());
        $(this.overlay).on('click', (e) => {
          if (e.target === this.overlay) this.close();
        });
        $(this.prevBtn).on('click', () => this.prev());
        $(this.nextBtn).on('click', () => this.next());

        $(document).on('keydown.lightbox', (e) => {
          if (!this.overlay.classList.contains('is-open')) return;
          switch (e.key) {
            case 'Escape':
              this.close();
              break;
            case 'ArrowLeft':
              this.prev();
              break;
            case 'ArrowRight':
              this.next();
              break;
          }
        });
      } else {
        // Fallback Vanilla
        this.images.forEach((img, index) => {
          img.addEventListener('click', () => this.open(index));
        });
        this.closeBtn.addEventListener('click', () => this.close());
        this.overlay.addEventListener('click', (e) => {
          if (e.target === this.overlay) this.close();
        });
        this.prevBtn.addEventListener('click', () => this.prev());
        this.nextBtn.addEventListener('click', () => this.next());
        document.addEventListener('keydown', (e) => {
          if (!this.overlay.classList.contains('is-open')) return;
          switch(e.key) {
            case 'Escape':
              this.close();
              break;
            case 'ArrowLeft':
              this.prev();
              break;
            case 'ArrowRight':
              this.next();
              break;
          }
        });
      }
      
      // Touch/Swipe (básico)
      let touchStartX = 0;
      this.overlay.addEventListener('touchstart', (e) => {
        touchStartX = e.touches[0].clientX;
      });
      this.overlay.addEventListener('touchend', (e) => {
        const touchEndX = e.changedTouches[0].clientX;
        const diff = touchStartX - touchEndX;
        
        if (Math.abs(diff) > 50) {
          if (diff > 0) {
            this.next();
          } else {
            this.prev();
          }
        }
      });
    },
    
    open(index) {
      this.currentIndex = index;
      this.updateImage();
      this.overlay.classList.add('is-open');
      Utils.toggleBodyScroll(true);
      
      // Aplicar focus trap
      this.removeFocusTrap = Utils.createFocusTrap(this.overlay);
      
      // Anunciar apertura a lectores de pantalla
      if (window.GOATsApp?.ariaLive) {
        window.GOATsApp.ariaLive.announce(`Visor de imágenes abierto. Imagen ${index + 1} de ${this.images.length}`);
      }
    },
    
    close() {
      // Remover focus trap
      if (this.removeFocusTrap) {
        this.removeFocusTrap();
        this.removeFocusTrap = null;
      }
      this.overlay.classList.remove('is-open');
      Utils.toggleBodyScroll(false);
      
      // Anunciar cierre
      if (window.GOATsApp?.ariaLive) {
        window.GOATsApp.ariaLive.announce('Visor de imágenes cerrado');
      }
    },
    
    prev() {
      this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length;
      this.updateImage();
      
      // Anunciar cambio de imagen
      if (window.GOATsApp?.ariaLive) {
        window.GOATsApp.ariaLive.announce(`Imagen ${this.currentIndex + 1} de ${this.images.length}`);
      }
    },
    
    next() {
      this.currentIndex = (this.currentIndex + 1) % this.images.length;
      this.updateImage();
      
      // Anunciar cambio de imagen
      if (window.GOATsApp?.ariaLive) {
        window.GOATsApp.ariaLive.announce(`Imagen ${this.currentIndex + 1} de ${this.images.length}`);
      }
    },
    
    updateImage() {
      const currentImg = this.images[this.currentIndex];
      this.image.src = currentImg.src;
      this.image.alt = currentImg.alt || 'Imagen ampliada';
      this.counter.textContent = `${this.currentIndex + 1} / ${this.images.length}`;
      
      // Actualizar visibilidad de botones
      this.overlay.classList.toggle('is-multi', this.images.length > 1);
    }
  };

  // ============================================================================
  // MODAL DE VIDEOS YOUTUBE
  // ============================================================================
  
  /**
   * Modal para reproducir videos de YouTube mediante ID o URL completa.
   * Respeta parámetros t/start, añade origin/referrerPolicy y
   * ofrece enlace alternativo "Ver en YouTube" si el embed está restringido.
   */
  const VideoModal = {
    modal: null,
    removeFocusTrap: null,
    
    init() {
      const videoElements = Utils.qsa('.elemento-video, [data-video-id], [data-video-url]');
      
      if (!videoElements.length) {
        console.info('ℹ️ Página sin elementos de video');
        return;
      }
      
      this.setupEventListeners(videoElements);
      console.log(`✅ Modal de videos inicializado con ${videoElements.length} videos`);
    },
    
    setupEventListeners(elements) {
      if (window.jQuery) {
        const $ = window.jQuery;
        $(document)
          .on('click', '.elemento-video, [data-video-id], [data-video-url]', (e) => {
            const el = e.currentTarget;
            const value = el.dataset.videoUrl || el.dataset.videoId;
            const info = this.getEmbedConfig(value);
            if (info && info.id) {
              this.open(info.id, info);
            } else {
              console.warn('⚠️ No se encontró data-video-id ni data-video-url en el elemento');
            }
          });
        // Cursor se define desde CSS en .elemento-video
      } else {
        elements.forEach(el => {
          el.addEventListener('click', () => {
            const value = el.dataset.videoUrl || el.dataset.videoId;
            const info = this.getEmbedConfig(value);
            if (info && info.id) {
              this.open(info.id, info);
            } else {
              console.warn('⚠️ No se encontró data-video-id ni data-video-url en el elemento');
            }
          });
        });
      }
    },
    
    open(videoId, opts = {}) {
      // Crear modal
      this.modal = document.createElement('div');
      this.modal.className = 'video-modal';
      this.modal.setAttribute('role', 'dialog');
      this.modal.setAttribute('aria-modal', 'true');
      this.modal.setAttribute('aria-label', 'Reproductor de video');
      
      // Contenedor del video
      const content = document.createElement('div');
      content.className = 'video-modal-content';
      
      // Botón cerrar
      const closeBtn = document.createElement('button');
      closeBtn.className = 'video-modal-close';
      closeBtn.innerHTML = '×';
      closeBtn.setAttribute('aria-label', 'Cerrar video');
      
      // Iframe de YouTube
      const iframe = document.createElement('iframe');
      const start = typeof opts.start === 'number' && opts.start > 0 ? `&start=${opts.start}` : '';
      const origin = `&origin=${encodeURIComponent(location.origin)}`;
      iframe.src = `https://www.youtube.com/embed/${videoId}?autoplay=1&rel=0${start}${origin}`;
      iframe.title = 'Video YouTube';
      iframe.allow = 'accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture';
      iframe.allowFullscreen = true;
      iframe.referrerPolicy = 'strict-origin-when-cross-origin';

      const openLink = document.createElement('a');
      const direct = opts.originalUrl || `https://www.youtube.com/watch?v=${videoId}${opts.start ? `&t=${opts.start}s` : ''}`;
      openLink.className = 'video-modal-open-youtube';
      openLink.href = direct;
      openLink.target = '_blank';
      openLink.rel = 'noopener noreferrer';
      openLink.textContent = 'Ver en YouTube';
      
      // Ensamblar modal
      content.appendChild(closeBtn);
      content.appendChild(openLink);
      content.appendChild(iframe);
      this.modal.appendChild(content);
      document.body.appendChild(this.modal);
      
      // Fade in con clase
      setTimeout(() => {
        this.modal.classList.add('is-open');
      }, 10);
      
      Utils.toggleBodyScroll(true);
      
      // Aplicar focus trap
      this.removeFocusTrap = Utils.createFocusTrap(this.modal);
      
      // Event listeners para cerrar
      if (window.jQuery) {
        const $ = window.jQuery;
        $(closeBtn).on('click', () => this.close());
        $(this.modal).on('click', (e) => {
          if (e.target === this.modal) this.close();
        });
        $(document).on('keydown.videomodal', (e) => {
          if (e.key === 'Escape' && this.modal) {
            this.close();
          }
        });
      } else {
        closeBtn.addEventListener('click', () => this.close());
        this.modal.addEventListener('click', (e) => {
          if (e.target === this.modal) this.close();
        });
        document.addEventListener('keydown', (e) => {
          if (e.key === 'Escape' && this.modal) {
            this.close();
          }
        }, { once: true });
      }
    },
    
    close() {
      if (!this.modal) return;
      
      // Remover focus trap
      if (this.removeFocusTrap) {
        this.removeFocusTrap();
        this.removeFocusTrap = null;
      }
      this.modal.classList.remove('is-open');
      setTimeout(() => {
        this.modal.remove();
        this.modal = null;
        Utils.toggleBodyScroll(false);
      }, 300);
    },
    getVideoId(input) {
      if (!input) return null;
      const isUrl = /^https?:\/\//i.test(input);
      if (isUrl) return this.parseVideoIdFromUrl(input);
      return String(input).trim();
    },
    getEmbedConfig(input) {
      if (!input) return null;
      const isUrl = /^https?:\/\//i.test(input);
      if (!isUrl) return { id: String(input).trim() };
      const id = this.parseVideoIdFromUrl(input);
      const start = this.parseStartFromUrl(input);
      const info = { id, originalUrl: input };
      if (typeof start === 'number' && start > 0) info.start = start;
      return info;
    },
    parseVideoIdFromUrl(url) {
      try {
        const u = new URL(url);
        const host = u.hostname.replace(/^www\./, '');
        if (host === 'youtu.be') {
          return u.pathname.split('/')[1] || null;
        }
        if (host.endsWith('youtube.com')) {
          if (u.pathname.startsWith('/watch')) {
            return u.searchParams.get('v');
          }
          if (u.pathname.startsWith('/embed/')) {
            return u.pathname.split('/')[2] || null;
          }
          if (u.pathname.startsWith('/shorts/')) {
            return u.pathname.split('/')[2] || null;
          }
        }
      } catch (e) {}
      const m = url.match(/(?:youtu\.be\/|v=|\/embed\/|\/shorts\/)([A-Za-z0-9_-]{6,})/);
      return m ? m[1] : null;
    },
    parseStartFromUrl(url) {
      try {
        const u = new URL(url);
        const t = u.searchParams.get('t') || u.searchParams.get('start');
        if (!t) return null;
        const s = this.parseTimeToSeconds(t);
        return typeof s === 'number' && s >= 0 ? s : null;
      } catch (e) {
        const m = url.match(/[?&#](?:t|start)=([^&#]+)/);
        if (!m) return null;
        const s = this.parseTimeToSeconds(m[1]);
        return typeof s === 'number' && s >= 0 ? s : null;
      }
    },
    parseTimeToSeconds(str) {
      const v = String(str).trim();
      if (/^\d+$/.test(v)) return parseInt(v, 10);
      const m = v.match(/(?:(\d+)h)?(?:(\d+)m)?(?:(\d+)s)?/i);
      if (!m) return null;
      const h = parseInt(m[1] || '0', 10);
      const min = parseInt(m[2] || '0', 10);
      const sec = parseInt(m[3] || '0', 10);
      return h * 3600 + min * 60 + sec;
    }
  };

  // ============================================================================
  // ARIA LIVE REGIONS - NOTIFICACIONES ACCESIBLES
  // ============================================================================
  
  /**
   * Crea regiones ARIA (polite y assertive) para mensajes accesibles
   * y expone utilidades para anunciar eventos.
   */
  const AriaLive = {
    politeRegion: null,
    assertiveRegion: null,
    
    init() {
      this.createLiveRegions();
      console.log('✅ ARIA Live Regions inicializadas');
    },
    
    createLiveRegions() {
      // Región polite (no interrumpe)
      this.politeRegion = document.createElement('div');
      this.politeRegion.setAttribute('aria-live', 'polite');
      this.politeRegion.setAttribute('aria-atomic', 'true');
      this.politeRegion.className = 'sr-only';
      
      // Región assertive (interrumpe)
      this.assertiveRegion = document.createElement('div');
      this.assertiveRegion.setAttribute('aria-live', 'assertive');
      this.assertiveRegion.setAttribute('aria-atomic', 'true');
      this.assertiveRegion.className = 'sr-only';
      
      document.body.appendChild(this.politeRegion);
      document.body.appendChild(this.assertiveRegion);
    },
    
    /**
     * Anunciar mensaje de forma cortés (polite)
     * @param {string} message - Mensaje a anunciar
     */
    announce(message) {
      this.politeRegion.textContent = '';
      setTimeout(() => {
        this.politeRegion.textContent = message;
      }, 100);
    },
    
    /**
     * Anunciar mensaje urgente (assertive)
     * @param {string} message - Mensaje urgente a anunciar
     */
    announceUrgent(message) {
      this.assertiveRegion.textContent = '';
      setTimeout(() => {
        this.assertiveRegion.textContent = message;
      }, 100);
    }
  };

  // ============================================================================
  // INICIALIZACIÓN PRINCIPAL
  // ============================================================================
  
  /**
   * Inicializa todos los módulos cuando el DOM está listo y
   * expone una API pública para debugging.
   */
  function init() {
    console.log('🚀 GOATs del Fútbol - JavaScript inicializado');
    console.log('📅 Implementación: Mes 1, Semana 3 - Optimización y Accesibilidad');
    
    // Inicializar módulos
    Navigation.init();
    SmoothScroll.init();
    BackToTop.init();
    ScrollAnimations.init();
    ImageOptimization.init();
    Newsletter.init();
    Lightbox.init();
    VideoModal.init();
    AriaLive.init();
    
    console.log('✅ Todos los módulos cargados correctamente');
    console.log('📊 Versión: 1.2.0');
  }

  // Inicializar cuando el DOM esté listo
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  // Exponer API pública (opcional para debugging y extensiones futuras)
  window.GOATsApp = {
    version: '1.2.0',
    utils: Utils,
    navigation: Navigation,
    smoothScroll: SmoothScroll,
    newsletter: Newsletter,
    lightbox: Lightbox,
    videoModal: VideoModal,
    ariaLive: AriaLive
  };

})();