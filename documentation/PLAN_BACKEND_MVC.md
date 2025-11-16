# Plan de Acción Backend (MVC) — GOATs del Fútbol

> Objetivo: Codificar el BACKEND con óptimo criterio técnico (4/4) usando Java + Spring Boot y MySQL/XAMPP, bajo arquitectura MVC.

## Diagrama de arquitectura (MVC)

```
               ┌───────────────────────────────────────────────────────┐
               │                        Frontend                       │
               │  HTML/CSS/JS/jQuery  →  REST API (HTTP/JSON)          │
               └───────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│                                Spring Boot                                   │
│                                                                              │
│  Controllers (Web)     Services (Business)      Repositories (Data)          │
│  ──────────────────     ───────────────────      ──────────────────           │
│  AuthController         AuthService              UserRepository               │
│  PlayerController       PlayerService            PlayerRepository             │
│  CommentController      CommentService           CommentRepository            │
│  SubscriptionController SubscriptionService      SubscriptionRepository       │
│  AchievementController  AchievementService       AchievementRepository        │
│                                                                              │
│  DTOs / Mappers → validaciones (javax.validation)                            │
│  Security (JWT) → roles: ADMIN/USER/GUEST                                    │
│  Swagger/OpenAPI → documentación                                              │
└──────────────────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
                 ┌─────────────────────────────────────────┐
                 │                MySQL/XAMPP               │
                 │  Esquema: usuarios, jugadores, stats,   │
                 │  logros, comentarios, suscripciones     │
                 │  Migraciones: Flyway (V1..Vn)           │
                 └─────────────────────────────────────────┘
```

## Modelos, Controladores y Vistas (REST)

- Modelos (JPA)
  - `User(id, username, email, passwordHash, roles, enabled, createdAt)`
  - `Role(id, name)` (relación con `User`)
  - `Player(id, name, nickname, country, position, biography)`
  - `PlayerStats(id, playerId, goals, assists, matches, trophies, updatedAt)`
  - `Achievement(id, playerId, title, year, competition, description)`
  - `Comment(id, playerId, userId, content, createdAt, status)`
  - `Subscription(id, userId, playerId, createdAt, active)`
  - `Notification(id, userId, type, payload, sentAt, status)`

- Controladores (REST)
  - `AuthController`: `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/me`
  - `PlayerController`: `GET /api/players`, `GET /api/players/{id}`, `POST/PUT/DELETE /api/players` (ADMIN)
  - `StatsController`: `GET /api/players/{id}/stats`
  - `AchievementController`: `GET/POST /api/players/{id}/achievements` (POST ADMIN)
  - `CommentController`: `GET/POST /api/players/{id}/comments`, `PUT/DELETE /api/comments/{id}` (ADMIN)
  - `SubscriptionController`: `GET/POST/DELETE /api/subscriptions` (USER)
  - `NotificationController`: `POST /api/notifications/test` (ADMIN)

- Vistas (REST)
  - JSON responses con DTOs (requests/responses)
  - Documentadas en Swagger UI: `/swagger-ui/index.html` y `/v3/api-docs`

## Cronograma por fases

- Semana 1 — Base del proyecto
  - Inicializar Spring Boot (Java 17), estructura de paquetes MVC
  - Configurar MySQL/XAMPP y `application.yml`
  - Flyway V1: esquema base y claves foráneas
  - Seguridad mínima: Spring Security + JWT (login/register)
  - Swagger/OpenAPI y CORS (frontend `http://localhost`)

- Semana 2 — Dominio jugadores
  - CRUD `Player`, `PlayerStats`, `Achievement` con paginación y filtros
  - Flyway V2/V3: seeds de roles y jugadores de prueba
  - Tests de repositorio y servicios (H2/JUnit)

- Semana 3 — Interacción social
  - `Comment` con moderación (roles)
  - `Subscription` y `Notification` (simulación de envío)
  - Endpoints de informes básicos (ej. top goleadores)
  - Tests de controladores con `MockMvc`

- Semana 4 — Calidad y despliegue local
  - Manejo de errores global (`@ControllerAdvice`)
  - Validaciones (`javax.validation`), auditoría y logging
  - Optimización de consultas e índices
  - Documentación definitiva de endpoints y guía de integración

## Dependencias técnicas requeridas

- Java 17, Spring Boot 3.x
- `spring-boot-starter-web`, `spring-boot-starter-security`, `spring-boot-starter-data-jpa`
- `springdoc-openapi-starter-webmvc-ui` (Swagger UI)
- `mysql-connector-j`, `flyway-core`
- `validation-api`/`hibernate-validator`
- Opcional: `lombok`, `mapstruct`
- Tests: JUnit 5, Mockito, H2 (para integración)

## Estrategia de integración con el frontend

- Base URL: `http://localhost:8080/api` (configurable en `window.GOATsApp.apiBase` si se expone en el frontend)
- CORS: permitir origen del XAMPP (`http://localhost` y `http://127.0.0.1`)
- Newsletter: reemplazar simulación por endpoint real (`POST /api/newsletter/subscribe`)
- Comentarios: UI de jugadores consume `GET/POST /api/players/{id}/comments`
- Jugadores y búsquedas: listas y filtros consumen `GET /api/players` con queryparams
- Seguridad: formularios de login/registro consumen `POST /api/auth/*` y almacenan JWT en `sessionStorage`/`localStorage`
- Errores: manejar respuestas estandarizadas (códigos y mensajes) para UX coherente

## Notas y políticas

- Backups automáticos de MySQL (script `mysqldump` diario), tabla de historial Flyway
- Configuración segura: usuarios de DB con permisos mínimos, secreto JWT fuera del repositorio
- Convenciones de commit en español y prefijos (`feat`, `fix`, `docs`, `refactor`)
- Documentación viva: mantener actualizado este plan y la referencia en `DOCUMENTACION_IMPLEMENTACION_FRONTEND.md`