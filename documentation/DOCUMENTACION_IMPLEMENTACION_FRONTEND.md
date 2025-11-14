# Documentación de Implementación Frontend (HTML, CSS, jQuery)

> Objetivo: Alcanzar 4/4 en el indicador “Codifica todo el FRONTEND (HTML, CSS, JQUERY)” con óptimo criterio técnico.

## Alcance y criterios
- Semántica: `header`, `nav`, `main`, `section`, `footer` correctos.
- Responsive: breakpoints `1200/992/768/576/480` y `prefers-reduced-motion`.
- Rendimiento: `IntersectionObserver`, `loading="lazy"`, `decoding="async"`, skeleton para imágenes.
- Accesibilidad: textos `alt` descriptivos, `role="dialog"` y `aria-modal` en overlays.
- jQuery: integrar y usar en 2–3 interacciones clave (lightbox, video modal, smooth scroll) sin romper el JS existente.

## Estado actual (14-11-2025)
- jQuery: no está incluido; el frontend usa Vanilla JS.
- Overlays: `Lightbox` y `VideoModal` activos desde `js/main.js` con estilos inline; estilos CSS de overlays fueron removidos previamente.
- Galería y videos: `cuadricula-galeria` y `elemento-video` presentes; `data-video-id` implementado en páginas de jugadores.
- Animaciones: `data-reveal` + `IntersectionObserver` y skeleton para `img[loading="lazy"]`.

## Plan de trabajo (iterativo)
1) Incluir jQuery 3.7.1 vía CDN en `index.html` y `pages/*.html`.
   - Entregable: jQuery disponible (`typeof window.jQuery === 'function'`).
   - Commit: `feat(frontend): incluir jQuery 3.7.1 en páginas principales`.
2) Restaurar estilos CSS de overlays y modal (clases, transiciones y media queries).
   - Entregable: `.lightbox-overlay`, `.lightbox-content`, `.video-modal`, `.video-modal-content`, `.video-modal-close` en `css/styles.css`.
   - Commit: `style(overlay, modal): restaurar clases y transiciones responsivas`.
3) Reescribir Lightbox con jQuery (delegación y accesibilidad).
   - Entregable: apertura en click sobre `'.elemento-galeria img'`, cierre con overlay/`Esc`, focus trap.
   - Commit: `feat(lightbox): implementación con jQuery y mejoras de accesibilidad`.
4) Reescribir Video Modal con jQuery (YouTube embed y cierre seguro).
   - Entregable: click en `'.elemento-video'` usa `data-video-id`, cierre por overlay/`Esc`, pausa al cerrar.
   - Commit: `feat(video-modal): implementación con jQuery y control de reproducción`.
5) Smooth scroll con jQuery a anclas, con fallback nativo.
   - Entregable: navegación suave respetando `prefers-reduced-motion`.
   - Commit: `feat(navegacion): smooth scroll con jQuery y fallback`.
6) QA + Documentación final.
   - Entregable: checklist completo, capturas y verificación en `http://127.0.0.1:8000/`.
   - Commit: `docs: actualizar guía y checklist para rúbrica`.

## Registro de implementación (llenar en cada commit)
- Fecha: YYYY-MM-DD
- Rama: `main`
- Commit: `<hash>` — `<mensaje>`
- Archivos: lista de archivos tocados
- Pruebas: pasos de verificación manual y resultado
- Observaciones: notas relevantes

### Entrada 1 (creación de esta documentación)
- Fecha: 2025-11-14
- Rama: `main`
- Commit: (se completará tras el push)
- Archivos: `documentation/DOCUMENTACION_IMPLEMENTACION_FRONTEND.md`
- Pruebas: revisión de lint (N/A), visual (N/A)
- Observaciones: documento base con plan y plantilla de registro.

### Entrada 2 (inclusión de jQuery 3.7.1)
- Fecha: 2025-11-14
- Rama: `main`
- Commit: 4340af6 — feat(frontend): incluir jQuery 3.7.1 en index y páginas de jugadores
- Archivos: `index.html`, `pages/messi.html`, `pages/neymar.html`, `pages/ronaldo.html`
- Pruebas: abrir páginas y comprobar en consola `jQuery.fn.jquery` devuelve `3.7.1`. Sin errores en consola.
- Observaciones: jQuery cargado en `<head>` antes de `main.js` (defer), listo para implementar interacciones.

## Checklist para 4/4 (Indicador 1)
- [ ] jQuery 3.7.1 agregado y verificado en todas las páginas.
- [ ] Lightbox funcional con jQuery, accesible y responsivo.
- [ ] Video modal funcional con jQuery, accesible y responsivo.
- [ ] Smooth scroll con jQuery y fallback respetando accesibilidad.
- [ ] CSS de overlays y modal centralizado (sin estilos inline en JS).
- [ ] Sin errores en consola; UI estable en todos los breakpoints.

## Guía de verificación manual
- Abrir `http://127.0.0.1:8000/` y páginas de jugadores.
- Comprobar carga de jQuery: ejecutar `jQuery.fn.jquery` en la consola.
- Galería: click en cualquier `'.elemento-galeria img'` abre lightbox; clicks internos no cierran; overlay/Esc sí.
- Videos: click en `'.elemento-video'` abre modal; reproduce; cierre correcto y pausa.
- Responsive: probar `480/576/768/992/1200` px; transiciones suaves y sin reflow excesivo.

## Notas
- Mantener mensajes de commit claros, en español y con prefijo por tipo (`feat`, `fix`, `style`, `docs`).
- Evitar cambios no relacionados durante cada commit para trazabilidad.