# Pruebas de Endpoints - Semana 2
## Backend GOATs del Fútbol - PlayerStats, Achievement, Comment, Subscription

### Configuración
- **Base URL:** `http://localhost:8080`
- **Puerto:** 8080
- **Base de datos:** MySQL 5.7 (goats_futbol)
- **Autenticación:** JWT Bearer Token

### Usuarios de prueba
```json
{
  "admin": {
    "username": "admin",
    "password": "admin123",
    "roles": ["ROLE_ADMIN"]
  },
  "testuser": {
    "username": "testuser",
    "password": "Test123!",
    "roles": ["ROLE_USER"]
  }
}
```

---

## 1. Autenticación (prerequisito)

### 1.1 Login como Admin
```powershell
$adminToken = (Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username":"admin","password":"admin123"}').token

Write-Host "Admin Token: $adminToken"
```

### 1.2 Login como User
```powershell
$userToken = (Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"username":"testuser","password":"Test123!"}').token

Write-Host "User Token: $userToken"
```

---

## 2. PlayerStats - Estadísticas de Jugadores

### 2.1 GET /api/stats/player/{playerId} (Público)
```powershell
# Obtener estadísticas de Messi (player_id = 1)
Invoke-RestMethod -Uri "http://localhost:8080/api/stats/player/1" `
  -Method GET `
  -Headers @{"Content-Type"="application/json"} | ConvertTo-Json
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "playerId": 1,
  "goals": 820,
  "assists": 375,
  "matchesPlayed": 1038,
  "trophies": 44,
  "yellowCards": 89,
  "redCards": 4,
  "minutesPlayed": 87450.0,
  "ballonDOrWins": 8,
  "championsLeagueWins": 4,
  "worldCupWins": 1
}
```

### 2.2 POST /api/stats (Admin)
```powershell
# Crear estadísticas para un nuevo jugador
$body = @{
  playerId = 4
  goals = 500
  assists = 200
  matchesPlayed = 700
  trophies = 25
  yellowCards = 50
  redCards = 2
  minutesPlayed = 60000.0
  ballonDOrWins = 0
  championsLeagueWins = 2
  worldCupWins = 0
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/stats" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"; "Authorization"="Bearer $adminToken"} `
  -Body $body | ConvertTo-Json
```

### 2.3 GET /api/stats/top/goals (Público)
```powershell
# Top 10 goleadores
Invoke-RestMethod -Uri "http://localhost:8080/api/stats/top/goals?limit=10" `
  -Method GET | ConvertTo-Json
```

### 2.4 GET /api/stats/top/ballondor (Público)
```powershell
# Jugadores con Balones de Oro
Invoke-RestMethod -Uri "http://localhost:8080/api/stats/top/ballondor?limit=10" `
  -Method GET | ConvertTo-Json
```

---

## 3. Achievement - Logros y Competiciones

### 3.1 GET /api/achievements/player/{playerId} (Público)
```powershell
# Logros de Messi
Invoke-RestMethod -Uri "http://localhost:8080/api/achievements/player/1" `
  -Method GET | ConvertTo-Json
```

**Respuesta esperada:**
```json
{
  "content": [
    {
      "id": 1,
      "playerId": 1,
      "title": "Balón de Oro",
      "description": "Mejor jugador del mundo",
      "year": 2023,
      "type": "INDIVIDUAL",
      "organization": "France Football"
    }
  ],
  "totalElements": 12
}
```

### 3.2 POST /api/achievements (Admin)
```powershell
$body = @{
  playerId = 1
  title = "Copa América 2024"
  description = "Campeón con Argentina"
  year = 2024
  type = "NATIONAL_TEAM"
  organization = "CONMEBOL"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/achievements" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"; "Authorization"="Bearer $adminToken"} `
  -Body $body | ConvertTo-Json
```

### 3.3 GET /api/achievements/type/{type} (Público)
```powershell
# Logros individuales
Invoke-RestMethod -Uri "http://localhost:8080/api/achievements/type/INDIVIDUAL" `
  -Method GET | ConvertTo-Json
```

### 3.4 GET /api/achievements/search (Público)
```powershell
# Buscar por título
Invoke-RestMethod -Uri "http://localhost:8080/api/achievements/search?title=Champions" `
  -Method GET | ConvertTo-Json
```

---

## 4. Comment - Sistema de Comentarios con Moderación

### 4.1 POST /api/comments (Autenticado)
```powershell
$body = @{
  userId = 2
  playerId = 1
  content = "Messi es el mejor jugador de todos los tiempos. Su visión de juego y técnica son incomparables."
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/comments" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"; "Authorization"="Bearer $userToken"} `
  -Body $body | ConvertTo-Json
```

**Respuesta esperada:**
```json
{
  "id": 6,
  "userId": 2,
  "username": "testuser",
  "playerId": 1,
  "playerName": "Lionel Messi",
  "content": "Messi es el mejor jugador de todos los tiempos...",
  "status": "PENDING",
  "createdAt": "2025-11-15T21:00:00"
}
```

### 4.2 GET /api/comments/player/{playerId} (Público)
```powershell
# Solo devuelve comentarios APPROVED
Invoke-RestMethod -Uri "http://localhost:8080/api/comments/player/1" `
  -Method GET | ConvertTo-Json
```

### 4.3 GET /api/comments/pending (Admin)
```powershell
# Comentarios pendientes de moderación
Invoke-RestMethod -Uri "http://localhost:8080/api/comments/pending?limit=50" `
  -Method GET `
  -Headers @{"Authorization"="Bearer $adminToken"} | ConvertTo-Json
```

### 4.4 POST /api/comments/{id}/approve (Admin)
```powershell
# Aprobar comentario
Invoke-RestMethod -Uri "http://localhost:8080/api/comments/6/approve" `
  -Method POST `
  -Headers @{"Authorization"="Bearer $adminToken"} | ConvertTo-Json
```

### 4.5 POST /api/comments/{id}/reject (Admin)
```powershell
$body = @{
  reason = "Contenido inapropiado"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/comments/7/reject" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"; "Authorization"="Bearer $adminToken"} `
  -Body $body | ConvertTo-Json
```

---

## 5. Subscription - Suscripciones de Usuarios

### 5.1 POST /api/subscriptions/player/{playerId} (Autenticado)
```powershell
# Suscribirse a Messi
Invoke-RestMethod -Uri "http://localhost:8080/api/subscriptions/player/1" `
  -Method POST `
  -Headers @{"Authorization"="Bearer $userToken"} | ConvertTo-Json
```

**Respuesta esperada:**
```json
{
  "id": 4,
  "userId": 2,
  "username": "testuser",
  "playerId": 1,
  "playerName": "Lionel Messi",
  "active": true,
  "notificationsEnabled": true,
  "subscribedAt": "2025-11-15T21:00:00"
}
```

### 5.2 GET /api/subscriptions/player/{playerId}/check (Autenticado)
```powershell
# Verificar si está suscrito
Invoke-RestMethod -Uri "http://localhost:8080/api/subscriptions/player/1/check" `
  -Method GET `
  -Headers @{"Authorization"="Bearer $userToken"}
```

### 5.3 PATCH /api/subscriptions/player/{playerId}/notifications (Autenticado)
```powershell
# Alternar notificaciones
Invoke-RestMethod -Uri "http://localhost:8080/api/subscriptions/player/1/notifications" `
  -Method PATCH `
  -Headers @{"Authorization"="Bearer $userToken"} | ConvertTo-Json
```

### 5.4 GET /api/subscriptions/player/{playerId}/count (Público)
```powershell
# Contar suscriptores activos
Invoke-RestMethod -Uri "http://localhost:8080/api/subscriptions/player/1/count" `
  -Method GET
```

### 5.5 DELETE /api/subscriptions/player/{playerId} (Autenticado)
```powershell
# Desuscribirse
Invoke-RestMethod -Uri "http://localhost:8080/api/subscriptions/player/1" `
  -Method DELETE `
  -Headers @{"Authorization"="Bearer $userToken"}
```

---

## 6. Pruebas de Seguridad

### 6.1 Sin autenticación (debe fallar)
```powershell
# Intentar crear estadísticas sin token
try {
  Invoke-RestMethod -Uri "http://localhost:8080/api/stats" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body '{"playerId":1,"goals":100}'
} catch {
  Write-Host "Error esperado: $($_.Exception.Message)"
}
```

### 6.2 Con rol USER (debe fallar)
```powershell
# Usuario normal intenta crear estadísticas
try {
  Invoke-RestMethod -Uri "http://localhost:8080/api/stats" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"; "Authorization"="Bearer $userToken"} `
    -Body '{"playerId":1,"goals":100}'
} catch {
  Write-Host "Error esperado: $($_.Exception.Message)"
}
```

---

## 7. Resultados Esperados

### ✅ Endpoints Públicos (sin autenticación)
- GET /api/players
- GET /api/stats/player/{id}
- GET /api/stats/top/* 
- GET /api/achievements/player/{id}
- GET /api/achievements/type/{type}
- GET /api/comments/player/{id} (solo APPROVED)
- GET /api/subscriptions/player/{id}/count

### 🔒 Endpoints Autenticados (requieren JWT)
- POST /api/comments
- PUT /api/comments/{id}
- DELETE /api/comments/{id}
- POST /api/subscriptions/player/{id}
- DELETE /api/subscriptions/player/{id}
- PATCH /api/subscriptions/player/{id}/notifications
- GET /api/subscriptions/user/{id}

### 👑 Endpoints Admin (requieren rol ADMIN)
- POST /api/stats
- PUT /api/stats/player/{id}
- DELETE /api/stats/player/{id}
- POST /api/achievements
- PUT /api/achievements/{id}
- DELETE /api/achievements/{id}
- POST /api/comments/{id}/approve
- POST /api/comments/{id}/reject
- GET /api/comments/pending
- GET /api/subscriptions/player/{id}/notifications

---

## 8. Checklist de Funcionalidades

### PlayerStats ✅
- [x] Obtener estadísticas por jugador
- [x] Crear estadísticas (ADMIN)
- [x] Actualizar estadísticas (ADMIN)
- [x] Eliminar estadísticas (ADMIN)
- [x] Rankings top goleadores
- [x] Rankings top asistentes
- [x] Rankings top trofeos
- [x] Rankings Balones de Oro

### Achievement ✅
- [x] Listar logros por jugador
- [x] Crear logro (ADMIN)
- [x] Actualizar logro (ADMIN)
- [x] Eliminar logro (ADMIN)
- [x] Filtrar por tipo
- [x] Filtrar por año
- [x] Buscar por título
- [x] Contar logros

### Comment ✅
- [x] Crear comentario (autenticado, estado PENDING)
- [x] Listar comentarios aprobados (público)
- [x] Aprobar comentario (ADMIN)
- [x] Rechazar comentario (ADMIN)
- [x] Editar comentario propio
- [x] Eliminar comentario propio o ADMIN
- [x] Listar pendientes (ADMIN)
- [x] Filtrar por estado (ADMIN)

### Subscription ✅
- [x] Suscribirse a jugador (autenticado)
- [x] Desuscribirse (autenticado)
- [x] Verificar suscripción (autenticado)
- [x] Alternar notificaciones (autenticado)
- [x] Contar suscriptores (público)
- [x] Listar suscriptores (ADMIN)
- [x] Constraint único user-player

---

## 9. Notas de Implementación

### Base de Datos
- Tablas creadas por Hibernate automáticamente
- Scripts SQL de migración incluidos con datos de ejemplo
- Índices optimizados para consultas frecuentes
- Constraints de integridad referencial aplicados

### Seguridad
- JWT con expiración 24 horas
- BCrypt para passwords (factor 10)
- @PreAuthorize en endpoints protegidos
- CORS configurado para localhost

### Validaciones
- Jakarta Validation en todos los DTOs
- @NotNull, @NotBlank, @Size, @Min, @Max
- Validación de roles en servicios
- Mensajes de error descriptivos

### Rendimiento
- Fetch strategy LAZY en relaciones
- Paginación en listas grandes
- Índices en columnas de búsqueda
- Connection pool HikariCP

---

## 10. Próximos Pasos

### Semana 3 (Pendiente)
- [ ] Sistema de notificaciones en tiempo real
- [ ] Moderación avanzada de comentarios
- [ ] Reportes y estadísticas administrativas
- [ ] Dashboard de administración
- [ ] Logs de auditoría
- [ ] Rate limiting y throttling

### Semana 4 (Pendiente)
- [ ] Integración frontend-backend completa
- [ ] Tests unitarios con JUnit 5 y Mockito
- [ ] Tests de integración con TestContainers
- [ ] Documentación OpenAPI/Swagger completa
- [ ] Optimización de consultas SQL
- [ ] Deploy y configuración de producción
