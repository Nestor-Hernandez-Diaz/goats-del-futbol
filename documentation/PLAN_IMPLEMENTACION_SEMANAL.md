# 📅 Plan de Implementación Semanal — GOATs del Fútbol (3 Meses)

Objetivo: evolucionar el sitio estático (HTML/CSS) a una aplicación dinámica con JavaScript (frontend), PHP (backend) y MySQL (persistencia), manteniendo calidad (Lighthouse ≥ 80), seguridad y mantenibilidad.

- Alcance: interactividad frontend (JS nativo con `defer`, `IntersectionObserver`, `fetch API`), backend con PHP 8.x + PDO y prepared statements, base de datos MySQL con esquema sólido (índices y constraints), pruebas y optimización.
- Entorno: XAMPP (Apache/PHP/MySQL), rutas del proyecto bajo `c:\xampp\htdocs\proyecto-goats-futbol`.

---

## 🧭 Fases Mensuales

1. Fase 1 (Mes 1): Interactividad Frontend y fundamentos JS.
2. Fase 2 (Mes 2): Backend PHP + MySQL, API y persistencia.
3. Fase 3 (Mes 3): Integración, pruebas, optimización y Release Candidate.

---

## 📆 Mes 1 — Interactividad Frontend

Meta mensual: implementar JS con `defer`, navegación fluida, lightbox, modal de videos YouTube, `IntersectionObserver`, validación y mejoras UX; Lighthouse ≥ 80.

### Semana 1: Base JS y navegación accesible
- Objetivo: crear `js/main.js` y navegación con menú hamburguesa mejorado.
- Tareas:
  - [ ] Crear `js/main.js` y cargarlo con `defer` en `index.html` y `pages/*.html`.
  - [ ] Menú hamburguesa: bloquear scroll al abrir, cerrar al navegar, accesible (teclado/ARIA).
  - [ ] Smooth scroll nativo a anclas (`scrollIntoView`), sin jQuery.
  - [ ] Botón “Volver arriba” con aparición controlada por `IntersectionObserver`.
  - [ ] Revisar IDs/targets de secciones para enlaces internos consistentes.
- Entregables:
  - Script funcional `js/main.js` modular (utilitarios, UI, integraciones).
  - Navegación accesible con roles y etiquetas ARIA básicas.
- Duración: 4 días (32–40 h).

### Semana 2: Validación, animaciones y lazy
- Objetivo: validar newsletter, animaciones al scroll y lazy loading.
- Tareas:
  - [ ] Validación del formulario newsletter en cliente (patrones, mensajes claros).
  - [ ] `IntersectionObserver` para animaciones progresivas de secciones/elements `.animable`.
  - [ ] `loading="lazy"` en imágenes; revisión de defer/orden de scripts.
  - [ ] Utilitarios mínimos: helpers de selección, delegación de eventos, focus management.
- Entregables:
  - Formulario robusto con mensajes accesibles.
  - Animaciones discretas que no penalicen performance.
- Duración: 4 días (32–40 h).

### Semana 3: Lightbox y modales YouTube
- Objetivo: galería con lightbox y modales YouTube utilizando `data-video-id`.
- Tareas:
  - [ ] Lightbox sin librerías: overlay, cierre por click/escape, gestión de foco.
  - [ ] Modal de videos (YouTube): `iframe` con `autoplay` controlado y `aria-modal`.
  - [ ] Añadir `data-video-id` a `.elemento-video` en HTML.
  - [ ] Estilos mínimos de overlay y modal (reusar esquema visual existente).
- Entregables:
  - Lightbox y modal reutilizables con APIs simples.
- Duración: 5 días (40–48 h).

### Semana 4: Performance y Lighthouse
- Objetivo: optimizar performance y accesibilidad para alcanzar ≥ 80.
- Tareas:
  - [ ] Ejecutar Lighthouse (CLI/DevTools) y corregir hallazgos (a11y, SEO, performance).
  - [ ] Optimización de imágenes (WebP/AVIF si viable, compresión).
  - [ ] Revisar CSS no usado y minificar en producción.
  - [ ] Carga condicional de JS por página; revisar `defer` y orden de ejecución.
- Entregables:
  - Informe Lighthouse con ≥ 80 (performance y a11y), checklist de mejoras.
- Duración: 4 días (32–40 h).

---

## 🗄️ Mes 2 — Backend PHP + MySQL

Meta mensual: esquema MySQL completo, conexión PDO, endpoints JSON, CRUD básico, seguridad con prepared statements y `password_hash()`.

### Semana 1: Esquema de BD y conexión PDO
- Objetivo: crear base de datos con tablas, índices y constraints; configurar PDO.
- Tareas:
  - [ ] Redactar `database/migrations/001_init.sql` con `InnoDB`, `utf8mb4`.
  - [ ] Definir índices: compuestos, únicos y foráneos con reglas `ON DELETE`.
  - [ ] Configurar `app/config/database.php` con PDO, modo excepción y `FETCH_ASSOC`.
  - [ ] Variables de entorno/config seguro (no credenciales en Git).
- Entregables:
  - Script SQL ejecutado y verificado en MySQL (XAMPP).
  - Clase `Database` lista (conexión, begin/commit/rollback).
- Duración: 5 días (40–48 h).

### Semana 2: API de Jugadores y render dinámico
- Objetivo: exponer `api/jugadores.php` y listar jugadores en frontend usando `fetch`.
- Tareas:
  - [ ] Endpoint `GET /api/jugadores.php` con JSON (paginación, campos controlados).
  - [ ] Endpoint `POST` para crear jugadores (validación, prepared statements).
  - [ ] Integración en `index.html` y `pages/*.html` con `fetch API` (render tarjetas/listas).
  - [ ] Manejo de errores con códigos HTTP y mensajes consistentes.
- Entregables:
  - API funcional y UI mostrando datos dinámicos desde BD.
- Duración: 4 días (32–40 h).

### Semana 3: Relaciones y contenido (títulos, clubes, estadísticas)
- Objetivo: implementar/sembrar relaciones y servir endpoints asociados.
- Tareas:
  - [ ] Tablas: `titulos`, `clubes`, `jugador_titulos`, `jugador_clubes`, `estadisticas_temporada`.
  - [ ] Seeds iniciales: Messi, Ronaldo, Neymar con títulos y clubes.
  - [ ] Endpoints: `GET /api/titulos.php?jugador_id`, `GET /api/estadisticas.php?jugador_id`.
  - [ ] Render dinámico de palmarés (con fechas) y comparativas.
- Entregables:
  - Contenido desde BD en palmarés y estadísticas visibles en UI.
- Duración: 5 días (40–48 h).

### Semana 4: Newsletter y usuarios (base segura)
- Objetivo: persistir suscriptores y preparar la base de usuarios.
- Tareas:
  - [ ] Tabla `newsletter_suscriptores` con verificación (`token_verificacion`).
  - [ ] Endpoint `POST /api/newsletter/subscribe.php` con prepared y validación.
  - [ ] Tabla `usuarios` y `comentarios` (estructura); uso de `password_hash()`.
  - [ ] Guías de seguridad: sanitización, CSRF tokens, cabeceras seguras.
- Entregables:
  - Newsletter funcional y base para autenticación futura.
- Duración: 4 días (32–40 h).

---

## 🔧 Mes 3 — Integración, Pruebas y Optimización

Meta mensual: convertir páginas a plantillas PHP, cubrir pruebas, CI, optimizar consultas e implementar RC.

### Semana 1: Plantillas PHP y vistas
- Objetivo: convertir HTML a PHP con componentes y layouts.
- Tareas:
  - [ ] `public/index.php`, `public/pages/*.php` usando vistas (`app/views/`).
  - [ ] Componentes en `app/views/components` (tarjetas, modales, galerías).
  - [ ] Carga de contenido desde BD con controladores simples (`app/controllers`).
  - [ ] Cache ligera (fragment caching) y cabeceras de caché.
- Entregables:
  - Sitio dinámico con vistas reutilizables y datos desde BD.
- Duración: 5 días (40–48 h).

### Semana 2: Pruebas y CI
- Objetivo: configurar pruebas unitarias/integración y pipeline CI.
- Tareas:
  - [ ] PHPUnit para modelos/controladores (coverage ≥ 60% inicial, objetivo 80%).
  - [ ] Pruebas JS (Vitest/Jest) para utilitarios y componentes.
  - [ ] E2E (Playwright/Cypress) para flujos clave (navegación, newsletter, modales).
  - [ ] Pipeline CI (GitHub Actions): lint, test, Lighthouse CI.
- Entregables:
  - Informe de cobertura y pipeline verde.
- Duración: 4 días (32–40 h).

### Semana 3: Optimización avanzada
- Objetivo: performance y consulta eficiente.
- Tareas:
  - [ ] Profiling de consultas (EXPLAIN) y ajuste de índices.
  - [ ] Reducción de payloads JSON, compresión GZIP, cache de API.
  - [ ] Bundle JS (Vite/Rollup) y minificación en producción.
  - [ ] Lighthouse ≥ 90 en performance (objetivo stretch).
- Entregables:
  - Reporte de performance y cambios aplicados.
- Duración: 4 días (32–40 h).

### Semana 4: Release Candidate y documentación
- Objetivo: cierre, documentación y backlog próximo.
- Tareas:
  - [ ] RC con changelog, versión semántica y tag.
  - [ ] Documentación: setup, despliegue, seguridad, arquitectura.
  - [ ] Backlog priorizado: autenticación, comentarios y API REST.
- Entregables:
  - RC listo, docs completas y plan siguiente.
- Duración: 3–4 días (24–32 h).

---

## 📋 Checklists Granulares

- Frontend:
  - [ ] Añadir `defer` a `<script src="/js/main.js" defer></script>` en todas las páginas.
  - [ ] IDs únicos y consistentes para secciones enlazadas.
  - [ ] `IntersectionObserver` aplicado a `section` y `.animable`.
  - [ ] Lightbox y modal con cierre por `Esc` y click en overlay.
  - [ ] `loading="lazy"` en imágenes; `object-fit` donde aplique.
  - [ ] Validación de newsletter del lado del cliente.

- Backend/API:
  - [ ] Conexión PDO con `ERRMODE_EXCEPTION` y `FETCH_ASSOC`.
  - [ ] Prepared statements en TODOS los endpoints.
  - [ ] Validación y sanitización de inputs; códigos HTTP correctos.
  - [ ] `password_hash()` y `password_verify()` para usuarios.
  - [ ] Estructura MVC básica (`app/models`, `app/controllers`, `app/views`).

- Base de datos:
  - [ ] Tablas con `ENGINE=InnoDB`, `CHARSET=utf8mb4`.
  - [ ] Índices compuestos donde haya filtros frecuentes.
  - [ ] Claves foráneas con reglas `ON DELETE`/`ON UPDATE`.
  - [ ] Scripts de seeds para datos iniciales.

- Calidad/DevOps:
  - [ ] Git Flow activo (branches: `main`, `develop`, `feature/*`).
  - [ ] CI: lint + tests + Lighthouse CI.
  - [ ] Documentación actualizada en `documentation/`.

---

## 💻 Detalles Técnicos (Ejemplos de Código)

### JavaScript — `defer`, `IntersectionObserver`, `fetch API`

```html
<!-- Cargar JS sin bloquear render -->
<script src="/js/main.js" defer></script>
```

```javascript
// js/main.js
// Smooth scroll nativo
document.addEventListener('click', (e) => {
  const link = e.target.closest('a[href^="#"]');
  if (!link) return;
  const target = document.querySelector(link.getAttribute('href'));
  if (target) {
    e.preventDefault();
    target.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
});

// Menú hamburguesa accesible
const burger = document.getElementById('burger-toggle');
const navLinks = document.querySelector('.enlaces-navegacion');
if (burger && navLinks) {
  burger.addEventListener('change', () => {
    document.body.style.overflow = burger.checked ? 'hidden' : 'auto';
  });
  navLinks.addEventListener('click', (e) => {
    if (e.target.tagName === 'A') {
      burger.checked = false;
      document.body.style.overflow = 'auto';
    }
  });
}

// IntersectionObserver para animaciones
const observer = new IntersectionObserver((entries) => {
  entries.forEach((entry) => {
    if (entry.isIntersecting) entry.target.classList.add('visible');
  });
}, { threshold: 0.1 });

document.querySelectorAll('section, .animable').forEach((el) => observer.observe(el));

// Modal de video (YouTube)
document.querySelectorAll('.elemento-video').forEach((el) => {
  el.addEventListener('click', () => {
    const videoId = el.dataset.videoId;
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
    modal.addEventListener('click', (e) => {
      if (e.target.classList.contains('close-modal') || e.target === modal) modal.remove();
    });
    document.addEventListener('keydown', (e) => { if (e.key === 'Escape') modal.remove(); }, { once: true });
  });
});

// fetch API (listado de jugadores)
async function cargarJugadores() {
  try {
    const res = await fetch('/api/jugadores.php', { headers: { 'Accept': 'application/json' } });
    if (!res.ok) throw new Error('Error cargando jugadores');
    const jugadores = await res.json();
    // TODO: Renderizar tarjetas/listado en el DOM
  } catch (err) {
    console.error(err);
  }
}
```

### PHP — PDO, prepared statements, `password_hash()`

```php
<?php // app/config/database.php
final class Database {
  private static ?PDO $pdo = null;
  public static function get(): PDO {
    if (self::$pdo === null) {
      $dsn = 'mysql:host=127.0.0.1;dbname=goats_futbol;charset=utf8mb4';
      self::$pdo = new PDO($dsn, 'root', '', [
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
      ]);
    }
    return self::$pdo;
  }
}
```

```php
<?php // api/newsletter/subscribe.php (POST)
require_once __DIR__ . '/../../app/config/database.php';
header('Content-Type: application/json');
$pdo = Database::get();
$data = json_decode(file_get_contents('php://input'), true);
$email = filter_var($data['email'] ?? '', FILTER_VALIDATE_EMAIL);
if (!$email) { http_response_code(422); echo json_encode(['error'=>'Email inválido']); exit; }

$token = bin2hex(random_bytes(16));
$stmt = $pdo->prepare('INSERT INTO newsletter_suscriptores (email, token_verificacion) VALUES (?, ?)');
try {
  $stmt->execute([$email, $token]);
  echo json_encode(['ok'=>true]);
} catch (PDOException $e) {
  http_response_code(409);
  echo json_encode(['error'=>'Email ya suscrito']);
}
```

```php
<?php // usuarios: crear password seguro
$passwordHash = password_hash($passwordPlano, PASSWORD_DEFAULT);
// Verificación: password_verify($passwordPlano, $passwordHashAlmacenado);
```

### MySQL — Script SQL con índices y constraints

```sql
-- database/migrations/001_init.sql
CREATE DATABASE IF NOT EXISTS goats_futbol CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE goats_futbol;

-- Tabla jugadores
CREATE TABLE jugadores (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre_completo VARCHAR(100) NOT NULL,
  apodo VARCHAR(50),
  fecha_nacimiento DATE,
  nacionalidad VARCHAR(50),
  altura DECIMAL(3,2),
  peso INT,
  pie_dominante ENUM('Derecho','Izquierdo','Ambidiestro'),
  posicion VARCHAR(50),
  dorsal INT,
  biografia_corta TEXT,
  biografia_completa LONGTEXT,
  imagen_perfil VARCHAR(255),
  imagen_hero VARCHAR(255),
  cita_destacada TEXT,
  estado ENUM('Activo','Retirado') DEFAULT 'Activo',
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Tabla clubes
CREATE TABLE clubes (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  pais VARCHAR(50),
  logo VARCHAR(255),
  estadio VARCHAR(100),
  fundacion YEAR,
  UNIQUE KEY uk_clubes_nombre (nombre)
) ENGINE=InnoDB;

-- Relación jugador_clubes (M:N)
CREATE TABLE jugador_clubes (
  id INT PRIMARY KEY AUTO_INCREMENT,
  jugador_id INT NOT NULL,
  club_id INT NOT NULL,
  fecha_inicio DATE,
  fecha_fin DATE,
  partidos_jugados INT DEFAULT 0,
  goles INT DEFAULT 0,
  asistencias INT DEFAULT 0,
  titulos_ganados INT DEFAULT 0,
  CONSTRAINT fk_jc_jugador FOREIGN KEY (jugador_id) REFERENCES jugadores(id) ON DELETE CASCADE,
  CONSTRAINT fk_jc_club FOREIGN KEY (club_id) REFERENCES clubes(id) ON DELETE CASCADE,
  KEY idx_jc_jugador_club (jugador_id, club_id)
) ENGINE=InnoDB;

-- Tabla titulos
CREATE TABLE titulos (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  tipo ENUM('Club','Nacional','Individual'),
  organizacion VARCHAR(100),
  descripcion TEXT,
  UNIQUE KEY uk_titulos_nombre (nombre)
) ENGINE=InnoDB;

-- Relación jugador_titulos
CREATE TABLE jugador_titulos (
  id INT PRIMARY KEY AUTO_INCREMENT,
  jugador_id INT NOT NULL,
  titulo_id INT NOT NULL,
  año YEAR NOT NULL,
  club_id INT NULL,
  detalles TEXT,
  CONSTRAINT fk_jt_jugador FOREIGN KEY (jugador_id) REFERENCES jugadores(id) ON DELETE CASCADE,
  CONSTRAINT fk_jt_titulo FOREIGN KEY (titulo_id) REFERENCES titulos(id) ON DELETE RESTRICT,
  CONSTRAINT fk_jt_club FOREIGN KEY (club_id) REFERENCES clubes(id) ON DELETE SET NULL,
  KEY idx_jt_jugador_año (jugador_id, año)
) ENGINE=InnoDB;

-- Estadísticas por temporada
CREATE TABLE estadisticas_temporada (
  id INT PRIMARY KEY AUTO_INCREMENT,
  jugador_id INT NOT NULL,
  club_id INT NOT NULL,
  temporada VARCHAR(20) NOT NULL,
  competicion VARCHAR(100),
  partidos_jugados INT DEFAULT 0,
  goles INT DEFAULT 0,
  asistencias INT DEFAULT 0,
  tarjetas_amarillas INT DEFAULT 0,
  tarjetas_rojas INT DEFAULT 0,
  minutos_jugados INT DEFAULT 0,
  CONSTRAINT fk_et_jugador FOREIGN KEY (jugador_id) REFERENCES jugadores(id) ON DELETE CASCADE,
  CONSTRAINT fk_et_club FOREIGN KEY (club_id) REFERENCES clubes(id) ON DELETE CASCADE,
  KEY idx_et_jugador_temp (jugador_id, temporada)
) ENGINE=InnoDB;

-- Videos
CREATE TABLE videos (
  id INT PRIMARY KEY AUTO_INCREMENT,
  jugador_id INT NOT NULL,
  titulo VARCHAR(200) NOT NULL,
  descripcion TEXT,
  url_youtube VARCHAR(255),
  miniatura VARCHAR(255),
  duracion TIME,
  fecha_publicacion DATE,
  vistas INT DEFAULT 0,
  CONSTRAINT fk_videos_jugador FOREIGN KEY (jugador_id) REFERENCES jugadores(id) ON DELETE CASCADE,
  KEY idx_videos_jugador (jugador_id)
) ENGINE=InnoDB;

-- Galería
CREATE TABLE galeria (
  id INT PRIMARY KEY AUTO_INCREMENT,
  jugador_id INT NOT NULL,
  titulo VARCHAR(200),
  descripcion TEXT,
  url_imagen VARCHAR(255) NOT NULL,
  fecha_evento DATE,
  tipo ENUM('Celebracion','Entrenamiento','Partido','Premio','Otro'),
  orden INT DEFAULT 0,
  CONSTRAINT fk_galeria_jugador FOREIGN KEY (jugador_id) REFERENCES jugadores(id) ON DELETE CASCADE,
  KEY idx_galeria_jugador (jugador_id)
) ENGINE=InnoDB;

-- Newsletter
CREATE TABLE newsletter_suscriptores (
  id INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(150) UNIQUE NOT NULL,
  fecha_suscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  activo BOOLEAN DEFAULT TRUE,
  token_verificacion VARCHAR(100)
) ENGINE=InnoDB;

-- Usuarios
CREATE TABLE usuarios (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  nombre_completo VARCHAR(100),
  avatar VARCHAR(255),
  rol ENUM('Admin','Usuario') DEFAULT 'Usuario',
  fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ultimo_acceso TIMESTAMP NULL
) ENGINE=InnoDB;

-- Comentarios
CREATE TABLE comentarios (
  id INT PRIMARY KEY AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  jugador_id INT NOT NULL,
  comentario TEXT NOT NULL,
  fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  aprobado BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_comentarios_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  CONSTRAINT fk_comentarios_jugador FOREIGN KEY (jugador_id) REFERENCES jugadores(id) ON DELETE CASCADE,
  KEY idx_comentarios_rel (jugador_id, usuario_id)
) ENGINE=InnoDB;
```

---

## 🧠 Buenas Prácticas

- Git Flow:
  - Branches: `main` (estable), `develop` (integración), `feature/*`, `hotfix/*`).
  - PRs con revisión, checklist y CI obligatorio.
  - Commits: Conventional Commits (`feat:`, `fix:`, `docs:`, `perf:`, `refactor:`).
- Nomenclatura:
  - JS: `camelCase` para funciones/variables, módulos por feature.
  - PHP: `PascalCase` en clases (`JugadorController`), `snake_case` para BD.
  - CSS: BEM en español (ej. `.tarjeta__titulo--destacado`).
- Optimización:
  - `defer` y carga condicional de scripts.
  - Imágenes optimizadas (WebP/AVIF si viable), `loading="lazy"`.
  - Minificación CSS/JS en producción; cache HTTP, GZIP.
- Seguridad:
  - Validar y sanitizar entradas.
  - PDO + prepared statements; nunca concatenar SQL.
  - `password_hash()` y `password_verify()`.
  - CSRF tokens en formularios sensibles.
  - Cabeceras seguras: `Content-Security-Policy`, `X-Frame-Options`, `X-Content-Type-Options`.

---

## 📈 Métricas y KPIs

- Progreso: % completitud por semana (objetivo ≥ 80% de tareas clave).
- Pruebas: cobertura 60% → 80% mensual (PHPUnit, Vitest/Jest).
- Performance: Lighthouse ≥ 80 (Mes 1), ≥ 85 (Mes 2), objetivo 90 (Mes 3).
- Código: lint sin errores (ESLint, PHPCS/PHP-CS-Fixer); PRs aprobados.

---

## ⚠️ Riesgos y Mitigación

- Scope creep: congelar alcance por sprint; backlog controlado y priorizado.
- Performance insuficiente: iteraciones de Lighthouse y optimización.
- Seguridad: checklist obligatoria en PR y pruebas de entradas maliciosas.
- Datos inconsistentes: constraints y transacciones (`BEGIN/COMMIT/ROLLBACK`).
- Entorno local (XAMPP): reproducir entorno con scripts y documentación; evitar credenciales en repositorio.

---

## 🛠️ Recursos y Herramientas

- Entorno: XAMPP (Apache/PHP/MySQL), Composer, npm.
- Calidad: ESLint, Prettier, PHPStan, PHPCS/PHP-CS-Fixer, PHPUnit.
- Pruebas: Vitest/Jest (JS), Playwright/Cypress (E2E).
- DevOps: GitHub Actions (lint+test+Lighthouse CI), Git.
- DB: MySQL Workbench/DBeaver, `EXPLAIN`, `SHOW INDEX`.
- Performance: Lighthouse CLI, TinyPNG/ImageOptim.
- Documentación: README, `documentation/` y comentarios útiles.

---

## ✅ Criterios de Aceptación

- Semana a semana:
  - Objetivo cumplido, entregables presentes y checklist en verde.
  - Lighthouse ejecutado y score cumple meta.
  - PR mergeado en `develop` con CI verde.
- Por fase:
  - Fase 1: interactividad y performance ≥ 80.
  - Fase 2: API y persistencia funcionales, seguridad básica aplicada.
  - Fase 3: vistas PHP dinámicas, pruebas y RC emitida.

---

## 🚀 Guía de Ejecución

1. Crear branch `feature/plan-semanal` y agregar este archivo.
2. Ejecutar el script SQL en MySQL (Mes 2 S1).
3. Implementar JS con `defer` (Mes 1 S1–S3) y verificar con Lighthouse (S4).
4. Desarrollar endpoints PHP (Mes 2 S2–S4) y consumir con `fetch API`.
5. Convertir HTML a vistas PHP (Mes 3 S1) y configurar pruebas y CI (S2).
6. Optimizar consultas y recursos (Mes 3 S3) y preparar Release Candidate (S4).