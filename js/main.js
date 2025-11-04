document.addEventListener('DOMContentLoaded', () => {
  const burger = document.getElementById('burger-toggle');
  const navLinks = document.querySelector('.enlaces-navegacion');

  if (burger && navLinks) {
    const closeMenu = () => {
      burger.checked = false;
      document.body.style.overflow = 'auto';
    };

    burger.addEventListener('change', () => {
      document.body.style.overflow = burger.checked ? 'hidden' : 'auto';
    });

    navLinks.addEventListener('click', (e) => {
      if (e.target.tagName === 'A') closeMenu();
    });

    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && burger.checked) closeMenu();
    });
  }

  document.addEventListener('click', (e) => {
    const link = e.target.closest('a[href^="#"]');
    if (!link) return;
    const target = document.querySelector(link.getAttribute('href'));
    if (target) {
      e.preventDefault();
      target.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  });

  const topSentinel = document.createElement('div');
  topSentinel.id = 'top-sentinel';
  topSentinel.style.position = 'absolute';
  topSentinel.style.top = '0';
  topSentinel.style.left = '0';
  topSentinel.style.width = '1px';
  topSentinel.style.height = '1px';
  document.body.prepend(topSentinel);

  const backToTop = document.createElement('button');
  backToTop.id = 'back-to-top';
  backToTop.type = 'button';
  backToTop.title = 'Volver arriba';
  backToTop.textContent = '↑';
  backToTop.style.position = 'fixed';
  backToTop.style.bottom = '24px';
  backToTop.style.right = '24px';
  backToTop.style.width = '44px';
  backToTop.style.height = '44px';
  backToTop.style.borderRadius = '50%';
  backToTop.style.border = 'none';
  backToTop.style.background = 'var(--color-primary, #0073ff)';
  backToTop.style.color = '#fff';
  backToTop.style.boxShadow = '0 4px 12px rgba(0,0,0,0.2)';
  backToTop.style.cursor = 'pointer';
  backToTop.style.zIndex = '1000';
  backToTop.style.display = 'none';
  backToTop.style.alignItems = 'center';
  backToTop.style.justifyContent = 'center';
  backToTop.style.fontSize = '20px';
  backToTop.style.lineHeight = '1';
  document.body.appendChild(backToTop);

  backToTop.addEventListener('click', () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  });

  const showObserver = new IntersectionObserver(
    (entries) => {
      const entry = entries[0];
      backToTop.style.display = entry.isIntersecting ? 'none' : 'flex';
    },
    { root: null, threshold: 0 }
  );
  showObserver.observe(topSentinel);

  // Semana 2: revelar secciones al hacer scroll
  const revealTargets = document.querySelectorAll('[data-reveal]');
  if (revealTargets.length) {
    const sectionObserver = new IntersectionObserver(
      (entries, obs) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('is-visible');
            obs.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.1 }
    );
    revealTargets.forEach((el) => sectionObserver.observe(el));
  }

  // Semana 2: lazy y decoding para imágenes
  const imgs = document.querySelectorAll('img');
  imgs.forEach((img) => {
    if (!img.hasAttribute('loading')) img.setAttribute('loading', 'lazy');
    if (!img.hasAttribute('decoding')) img.setAttribute('decoding', 'async');
  });

  // Semana 3: Lightbox para galería
  document.addEventListener('click', (e) => {
    const img = e.target.closest('.elemento-galeria img');
    if (!img) return;
    e.preventDefault();

    const overlay = document.createElement('div');
    overlay.className = 'lightbox-overlay';
    overlay.innerHTML = `
      <div class="lightbox-content" role="dialog" aria-modal="true">
        <button class="close-lightbox" aria-label="Cerrar">×</button>
        <img src="${img.src}" alt="${img.alt || ''}">
      </div>
    `;
    document.body.appendChild(overlay);

    const removeOverlay = () => overlay.remove();
    overlay.addEventListener('click', (evt) => {
      if (evt.target.classList.contains('close-lightbox') || evt.target === overlay) removeOverlay();
    });
    document.addEventListener('keydown', (evt) => { if (evt.key === 'Escape') removeOverlay(); }, { once: true });
  });

  // Semana 3: Modal de videos YouTube usando data-video-id
  document.addEventListener('click', (e) => {
    const videoEl = e.target.closest('.elemento-video');
    if (!videoEl) return;
    const videoId = videoEl.dataset.videoId;
    if (!videoId) return;

    const modal = document.createElement('div');
    modal.className = 'video-modal';
    modal.innerHTML = `
      <div class="video-modal-content" role="dialog" aria-modal="true">
        <button class="close-modal" aria-label="Cerrar">×</button>
        <iframe width="100%" height="500"
          src="https://www.youtube.com/embed/${videoId}?autoplay=1"
          title="Video YouTube" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen>
        </iframe>
      </div>`;
    document.body.appendChild(modal);

    const closeModal = () => modal.remove();
    modal.addEventListener('click', (evt) => {
      if (evt.target.classList.contains('close-modal') || evt.target === modal) closeModal();
    });
    document.addEventListener('keydown', (evt) => { if (evt.key === 'Escape') closeModal(); }, { once: true });
  });
});