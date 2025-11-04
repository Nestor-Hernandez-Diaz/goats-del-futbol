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
});