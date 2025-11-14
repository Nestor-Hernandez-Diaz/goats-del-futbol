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

  // ============================================================================
  // UTILIDADES GENERALES
  // ============================================================================
  
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
      document.body.style.overflow = lock ? 'hidden' : 'auto';
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
  
  const SmoothScroll = {
    offset: 80, // Altura del header fijo
    
    init() {
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
      console.log('✅ Smooth scroll inicializado');
    },
    
    scrollToElement(element) {
      const targetPosition = element.getBoundingClientRect().top + window.pageYOffset;
      const offsetPosition = targetPosition - this.offset;
      
      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth'
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
      this.sentinel.style.cssText = 'position:absolute;top:0;left:0;width:1px;height:1px;pointer-events:none;';
      document.body.prepend(this.sentinel);
    },
    
    createButton() {
      this.button = document.createElement('button');
      this.button.id = 'back-to-top';
      this.button.type = 'button';
      this.button.setAttribute('aria-label', 'Volver arriba');
      this.button.title = 'Volver arriba';
      this.button.textContent = '↑';
      this.button.style.cssText = `
        position:fixed;
        bottom:24px;
        right:24px;
        width:48px;
        height:48px;
        border-radius:50%;
        border:none;
        background:var(--color-primary,#0073ff);
        color:#fff;
        box-shadow:0 4px 12px rgba(0,0,0,0.2);
        cursor:pointer;
        z-index:1000;
        display:none;
        align-items:center;
        justify-content:center;
        font-size:20px;
        font-weight:bold;
        transition:all 0.3s ease;
      `;
      
      // Efectos hover (inline por simplicidad)
      this.button.addEventListener('mouseenter', () => {
        this.button.style.transform = 'translateY(-4px)';
        this.button.style.boxShadow = '0 6px 16px rgba(0,0,0,0.3)';
      });
      this.button.addEventListener('mouseleave', () => {
        this.button.style.transform = 'translateY(0)';
        this.button.style.boxShadow = '0 4px 12px rgba(0,0,0,0.2)';
      });
      
      document.body.appendChild(this.button);
    },
    
    setupObserver() {
      this.observer = new IntersectionObserver(
        (entries) => {
          const entry = entries[0];
          this.button.style.display = entry.isIntersecting ? 'none' : 'flex';
          this.button.setAttribute('aria-hidden', entry.isIntersecting);
        },
        { root: null, threshold: 0 }
      );
      this.observer.observe(this.sentinel);
    },
    
    setupClickHandler() {
      this.button.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
      });
    }
  };

  // ============================================================================
  // INTERSECTION OBSERVER PARA ANIMACIONES
  // ============================================================================
  
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
        
        // Aplicar delay progresivo a cada elemento
        elements.forEach((el, index) => {
          el.style.transitionDelay = `${index * group.delay}ms`;
          el.classList.add('stagger-item');
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
        wrapper.style.cssText = `
          position: relative;
          overflow: hidden;
          background: linear-gradient(
            90deg,
            rgba(255, 255, 255, 0.05) 25%,
            rgba(255, 255, 255, 0.1) 50%,
            rgba(255, 255, 255, 0.05) 75%
          );
          background-size: 200% 100%;
          animation: shimmer 1.5s infinite;
          border-radius: 8px;
        `;
        
        // Mantener aspect ratio
        const aspectRatio = img.dataset.aspectRatio || '16/9';
        wrapper.style.aspectRatio = aspectRatio;
        
        // Envolver imagen
        img.parentNode.insertBefore(wrapper, img);
        wrapper.appendChild(img);
        
        // Añadir clase para ocultar imagen mientras carga
        img.style.opacity = '0';
        img.style.transition = 'opacity 0.4s ease-in';
        
        // Remover skeleton cuando la imagen cargue
        const handleLoad = () => {
          img.style.opacity = '1';
          setTimeout(() => {
            if (wrapper.parentNode) {
              wrapper.replaceWith(img);
            }
          }, 400);
          img.removeEventListener('load', handleLoad);
          img.removeEventListener('error', handleError);
        };
        
        const handleError = () => {
          // En caso de error, mostrar placeholder
          wrapper.innerHTML = `
            <div style="
              display: flex;
              align-items: center;
              justify-content: center;
              height: 100%;
              color: rgba(255,255,255,0.3);
              font-size: 3rem;
            ">📷</div>
          `;
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
      this.input.style.borderColor = '#e74c3c';
      
      // Opcional: mostrar icono de error
      const icon = this.getOrCreateIcon();
      icon.textContent = '✕';
      icon.style.color = '#e74c3c';
    },
    
    showRealtimeSuccess() {
      this.input.classList.remove('error');
      this.input.classList.add('success');
      this.input.style.borderColor = '#27ae60';
      
      // Opcional: mostrar icono de éxito
      const icon = this.getOrCreateIcon();
      icon.textContent = '✓';
      icon.style.color = '#27ae60';
    },
    
    getOrCreateIcon() {
      let icon = this.form.querySelector('.validation-icon');
      
      if (!icon) {
        icon = document.createElement('span');
        icon.className = 'validation-icon';
        icon.style.cssText = `
          position: absolute;
          right: 12px;
          top: 50%;
          transform: translateY(-50%);
          font-size: 1.2rem;
          font-weight: bold;
          pointer-events: none;
        `;
        
        // Posicionar el form relativamente si no lo está
        if (getComputedStyle(this.form).position === 'static') {
          this.form.style.position = 'relative';
        }
        
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
      this.input.style.borderColor = '';
      const errors = Utils.qsa('.newsletter-error', this.form);
      errors.forEach((err) => err.remove());
    },
    
    clearSuccess() {
      this.input.classList.remove('success');
      this.input.style.borderColor = '';
      const icon = this.form.querySelector('.validation-icon');
      if (icon) icon.remove();
    }
  };

  // ============================================================================
  // LIGHTBOX PARA GALERÍA
  // ============================================================================
  
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
      // Click en imágenes para abrir lightbox
      this.images.forEach((img, index) => {
        img.style.cursor = 'pointer';
        img.addEventListener('click', () => this.open(index));
      });
      
      // Cerrar lightbox
      this.closeBtn.addEventListener('click', () => this.close());
      this.overlay.addEventListener('click', (e) => {
        if (e.target === this.overlay) this.close();
      });
      
      // Navegación
      this.prevBtn.addEventListener('click', () => this.prev());
      this.nextBtn.addEventListener('click', () => this.next());
      
      // Teclado
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
      this.prevBtn.style.display = this.images.length > 1 ? 'block' : 'none';
      this.nextBtn.style.display = this.images.length > 1 ? 'block' : 'none';
    }
  };

  // ============================================================================
  // MODAL DE VIDEOS YOUTUBE
  // ============================================================================
  
  const VideoModal = {
    modal: null,
    removeFocusTrap: null,
    
    init() {
      const videoElements = Utils.qsa('.elemento-video, [data-video-id]');
      
      if (!videoElements.length) {
        console.warn('⚠️ No se encontraron elementos de video');
        return;
      }
      
      this.setupEventListeners(videoElements);
      console.log(`✅ Modal de videos inicializado con ${videoElements.length} videos`);
    },
    
    setupEventListeners(elements) {
      elements.forEach(el => {
        el.style.cursor = 'pointer';
        el.addEventListener('click', () => {
          const videoId = el.dataset.videoId;
          if (videoId) {
            this.open(videoId);
          } else {
            console.warn('⚠️ No se encontró data-video-id en el elemento');
          }
        });
      });
    },
    
    open(videoId) {
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
      iframe.src = `https://www.youtube.com/embed/${videoId}?autoplay=1&rel=0`;
      iframe.title = 'Video YouTube';
      iframe.allow = 'accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture';
      iframe.allowFullscreen = true;
      
      // Ensamblar modal
      content.appendChild(closeBtn);
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
      closeBtn.addEventListener('click', () => this.close());
      this.modal.addEventListener('click', (e) => {
        if (e.target === this.modal) this.close();
      });
      
      document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && this.modal) {
          this.close();
        }
      }, { once: true });
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
    }
  };

  // ============================================================================
  // ARIA LIVE REGIONS - NOTIFICACIONES ACCESIBLES
  // ============================================================================
  
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
      this.politeRegion.style.cssText = `
        position: absolute;
        width: 1px;
        height: 1px;
        padding: 0;
        margin: -1px;
        overflow: hidden;
        clip: rect(0,0,0,0);
        white-space: nowrap;
        border: 0;
      `;
      
      // Región assertive (interrumpe)
      this.assertiveRegion = document.createElement('div');
      this.assertiveRegion.setAttribute('aria-live', 'assertive');
      this.assertiveRegion.setAttribute('aria-atomic', 'true');
      this.assertiveRegion.className = 'sr-only';
      this.assertiveRegion.style.cssText = this.politeRegion.style.cssText;
      
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