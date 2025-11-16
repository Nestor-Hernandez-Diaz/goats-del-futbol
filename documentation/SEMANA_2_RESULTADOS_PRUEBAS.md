# 📊 Semana 2: Resultados de Pruebas - Backend GOATs del Fútbol

## 📅 Fecha de Ejecución
**16 de Noviembre de 2025 - 21:45:00**

---

## ✅ RESUMEN EJECUTIVO

### Estado General
- **Backend:** ✅ Compilado y ejecutándose en puerto 8080
- **Base de Datos:** ✅ MySQL 5.7 conectada
- **Tablas Creadas:** ✅ 8 tablas (players, users, roles, user_roles, player_stats, achievements, comments, subscriptions)
- **Datos Pre-cargados:** ⚠️ Parciales (usuarios sí, estadísticas insertadas manualmente)

### Resultados por Módulo

| Módulo | Endpoints Probados | Exitosos | Fallidos | Estado |
|--------|-------------------|----------|----------|---------|
| **Players** | 3 | 3 | 0 | ✅ 100% |
| **Authentication** | 2 | 2 | 0 | ✅ 100% |
| **PlayerStats** | 1 | 0 | 1 | ❌ 0% |
| **Achievements** | 0 | 0 | 0 | ⏳ Pendiente |
| **Comments** | 0 | 0 | 0 | ⏳ Pendiente |
| **Subscriptions** | 0 | 0 | 0 | ⏳ Pendiente |

**Total Probados:** 6/40+ endpoints (15%)  
**Tasa de Éxito:** 5/6 (83%)

---

## 🧪 PRUEBAS EJECUTADAS

### 1. ✅ Players (GET Público)

#### GET /api/players
```powershell
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/players" -Method Get
```

**Resultado:**
```json
{
    "content": [
        {
            "id": 1,
            "name": "Lionel Messi",
            "nickname": "La Pulga",
            "country": "Argentina",
            "position": "Delantero",
            "biography": "Jugador histórico con múltiples Balones de Oro."
        },
        {
            "id": 2,
            "name": "Cristiano Ronaldo",
            "nickname": "CR7",
            "country": "Portugal",
            "position": "Delantero",
            "biography": "Máximo goleador histórico en competiciones europeas."
        },
        {
            "id": 3,
            "name": "Neymar Jr",
            "nickname": "Ney",
            "country": "Brasil",
            "position": "Delantero",
            "biography": "Figura destacada del fútbol brasileño y europeo."
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 12,
        "sort": { "empty": true, "sorted": false, "unsorted": true },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 3,
    "last": true,
    "size": 12,
    "number": 0,
    "first": true,
    "numberOfElements": 3,
    "empty": false
}
```

**Estado:** ✅ **EXITOSO**
- Paginación funcionando correctamente
- 3 jugadores retornados
- Todos los campos presentes

---

### 2. ✅ Authentication - Registro

#### POST /api/auth/register
```powershell
$registerBody = @{
    username = "testadmin"
    email = "testadmin@test.com"
    password = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/register" `
    -Method Post -Body $registerBody -ContentType "application/json"
```

**Resultado:**
```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0YWRtaW4iLCJyb2xlcyI6IlJPTEVfVVNFUiIsImlhdCI6MTc2MzI2MDY2NSwiZXhwIjoxNzYzMzQ3MDY1fQ.jfpnzBro1Gdh90v-woGYx7Zmo0lbaUzOoGAYMlW8QXyYKt_g1d4ETDKgkzokWxAA",
    "type": "Bearer",
    "id": 3,
    "username": "testadmin",
    "email": "testadmin@test.com",
    "roles": ["ROLE_USER"]
}
```

**Estado:** ✅ **EXITOSO**
- Usuario creado exitosamente
- Token JWT generado
- Rol `ROLE_USER` asignado por defecto
- Password hasheado con BCrypt

---

### 3. ✅ Authentication - Login

#### POST /api/auth/login
```powershell
$loginBody = @{
    username = "testadmin"
    password = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
    -Method Post -Body $loginBody -ContentType "application/json"
```

**Resultado:**
```json
{
    "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0YWRtaW4iLCJyb2xlcyI6IlJPTEVfVVNFUiIsImlhdCI6MTc2MzI2MTI4MywiZXhwIjoxNzYzMzQ3NjgzfQ...",
    "type": "Bearer",
    "id": 3,
    "username": "testadmin",
    "email": "testadmin@test.com",
    "roles": ["ROLE_USER"]
}
```

**Estado:** ✅ **EXITOSO**
- Login correcto
- Token válido generado
- Usuario autenticado

---

### 4. ❌ PlayerStats - GET Estadísticas de Jugador

#### GET /api/stats/player/1
```powershell
# Sin autenticación
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/stats/player/1" -Method Get

# Con token
$headers = @{ "Authorization" = "Bearer $token" }
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/stats/player/1" `
    -Method Get -Headers $headers
```

**Resultado:**
```
❌ HTTP 403 Forbidden
Error: Error en el servidor remoto: (403) Prohibido.
```

**Estado:** ❌ **FALLIDO**

**Causa Identificada:**
- SecurityConfig solo permite sin autenticación: `/api/auth/**` y `GET /api/players/**`
- Endpoint `/api/stats/**` requiere autenticación según configuración actual
- El controlador PlayerStatsController no tiene `@PreAuthorize` en GET, pero SecurityConfig bloquea por defecto

**Solución Propuesta:**
Actualizar `SecurityConfig.java` línea 53:
```java
// Antes:
.requestMatchers(HttpMethod.GET, "/api/players/**").permitAll()

// Después:
.requestMatchers(HttpMethod.GET, "/api/players/**", "/api/stats/**", 
                                 "/api/achievements/**", "/api/comments/**", 
                                 "/api/subscriptions/*/count").permitAll()
```

---

## 📊 DATOS EN BASE DE DATOS

### Verificación de Tablas

#### ✅ Tabla `players`
```sql
SELECT * FROM players;
```
**Resultado:** 3 filas (Messi, CR7, Neymar) ✅

#### ✅ Tabla `users`
```sql
SELECT id, username, email FROM users;
```
**Resultado:**
```
+----+----------+------------------------+
| id | username | email                  |
+----+----------+------------------------+
|  1 | admin    | admin@goats-futbol.com |
|  2 | testuser | test@test.com          |
|  3 | testadmin| testadmin@test.com     |
+----+----------+------------------------+
```
✅ 3 usuarios

#### ✅ Tabla `roles`
```sql
SELECT * FROM roles;
```
**Resultado:** 3 roles (ADMIN, USER, GUEST) ✅

#### ✅ Tabla `player_stats` (Insertada Manualmente)
```sql
SELECT player_id, goals, assists, ballondor_wins FROM player_stats;
```
**Resultado:**
```
+-----------+-------+---------+----------------+
| player_id | goals | assists | ballondor_wins |
+-----------+-------+---------+----------------+
|         1 |   820 |     375 |              8 |
|         2 |   895 |     255 |              5 |
|         3 |   436 |     251 |              0 |
+-----------+-------+---------+----------------+
```
✅ 3 estadísticas insertadas manualmente

**Comando de Inserción:**
```sql
INSERT INTO player_stats 
(player_id, goals, assists, matches_played, trophies, yellow_cards, red_cards, 
 minutes_played, ballondor_wins, champions_league_wins, world_cup_wins)
VALUES 
(1, 820, 375, 1038, 44, 89, 4, 87450.0, 8, 4, 1),
(2, 895, 255, 1203, 35, 121, 11, 101230.0, 5, 5, 0),
(3, 436, 251, 729, 32, 107, 7, 62180.0, 0, 1, 0);
```

#### ⏳ Tabla `achievements` (Vacía)
```sql
SELECT COUNT(*) FROM achievements;
```
**Resultado:** 0 filas ⚠️ Pendiente inserción

#### ⏳ Tabla `comments` (Vacía)
```sql
SELECT COUNT(*) FROM comments;
```
**Resultado:** 0 filas ⚠️ Pendiente inserción

#### ⏳ Tabla `subscriptions` (Vacía)
```sql
SELECT COUNT(*) FROM subscriptions;
```
**Resultado:** 0 filas (esperado, se crean dinámicamente)

---

## 🐛 PROBLEMAS IDENTIFICADOS

### 1. Scripts SQL No Ejecutados Automáticamente

**Problema:**
- Hibernate crea tablas con DDL automático (`spring.jpa.hibernate.ddl-auto=update`)
- Scripts en `src/main/resources/db/migration/` NO se ejecutan automáticamente
- Solo funcionan con Flyway o Liquibase habilitado

**Evidencia:**
- Tablas `player_stats`, `achievements`, `comments`, `subscriptions` creadas VACÍAS
- Script V3__create_player_stats.sql tiene INSERT pero no se ejecutó

**Solución:**
1. **Opción A (Recomendada):** Habilitar Flyway
   ```properties
   # application.properties
   spring.flyway.enabled=true
   spring.flyway.locations=classpath:db/migration
   spring.jpa.hibernate.ddl-auto=validate
   ```

2. **Opción B:** Insertar datos manualmente vía SQL (usado actualmente)

3. **Opción C:** Crear archivos `data.sql` en `src/main/resources/` (ejecuta después de DDL)

---

### 2. Discrepancia en Nombres de Columna

**Problema:**
- Script SQL usa: `ballon_d_or_wins`
- Hibernate crea: `ballondor_wins`

**Causa:**
- Modelo `PlayerStats.java` tiene campo `ballonDOrWins`
- Hibernate naming strategy convierte a `ballondor_wins` (elimina guiones bajos)

**Solución:**
Actualizar scripts SQL V3, V4, V5, V6 para usar nombres correctos:
```sql
-- Antes:
ballon_d_or_wins INT DEFAULT 0,

-- Después:
ballondor_wins INT DEFAULT 0,
```

O agregar anotación explícita en modelo:
```java
@Column(name = "ballon_d_or_wins")
private Integer ballonDOrWins;
```

---

### 3. SecurityConfig Restrictivo

**Problema:**
- Solo `/api/auth/**` y `GET /api/players/**` son públicos
- Endpoints de estadísticas, logros, comentarios (GET) deberían ser públicos según diseño

**Impacto:**
- Frontend no podrá consultar estadísticas sin autenticación
- Rankings y logros requieren login (no deseable)

**Solución:**
Actualizar `SecurityConfig.java`:
```java
.authorizeHttpRequests(auth -> auth
  .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
  .requestMatchers("/api/auth/**").permitAll()
  .requestMatchers(HttpMethod.GET, 
    "/api/players/**", 
    "/api/stats/**", 
    "/api/achievements/**", 
    "/api/comments/**",
    "/api/subscriptions/*/count"
  ).permitAll()
  .anyRequest().authenticated()
)
```

---

### 4. MySQL 5.7 Warning

**Problema:**
```
HHH000511: The 5.5.5 version for [org.hibernate.dialect.MySQLDialect] 
is no longer supported, hence certain features may not work properly. 
The minimum supported version is 8.0.0.
```

**Impacto:** 
- Warning no crítico, backend funciona
- Algunas features de Hibernate 6.6 pueden no funcionar

**Solución:**
1. Actualizar MySQL a 8.0+ (recomendado)
2. O especificar dialect legacy:
   ```properties
   spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
   ```

---

### 5. Java Version Mismatch en Terminal

**Problema:**
- Java 11 en PATH por defecto
- JAR compilado con Java 17
- Error: `UnsupportedClassVersionError: class file version 61.0`

**Solución Aplicada:**
```powershell
& "C:\Program Files\Java\jdk-17\bin\java.exe" -jar target\goats-api-0.0.1-SNAPSHOT.jar
```

**Solución Permanente:**
Actualizar variable de entorno PATH:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

---

## 📋 CHECKLIST DE FUNCIONALIDADES

### ✅ Completadas y Probadas
- ✅ Backend arranca correctamente
- ✅ Conecta a MySQL
- ✅ Crea 8 tablas automáticamente
- ✅ Endpoint GET /api/players funciona
- ✅ Paginación de players funciona
- ✅ Registro de usuarios funciona
- ✅ Login JWT funciona
- ✅ Tokens JWT se generan correctamente
- ✅ Passwords se hashean con BCrypt
- ✅ Rol ROLE_USER se asigna por defecto

### ⚠️ Parcialmente Completadas
- ⚠️ Datos pre-cargados (solo players y users, falta achievements/comments)
- ⚠️ SecurityConfig funciona pero muy restrictivo

### ❌ Pendientes de Prueba
- ❌ Endpoints de PlayerStats (bloqueados por seguridad)
- ❌ Endpoints de Achievements (no probados)
- ❌ Endpoints de Comments (no probados)
- ❌ Endpoints de Subscriptions (no probados)
- ❌ Sistema de moderación de comentarios
- ❌ Rankings (top goals, assists, ballondor)
- ❌ Filtros y búsquedas avanzadas
- ❌ Protección de endpoints ADMIN

---

## 🎯 PRÓXIMOS PASOS RECOMENDADOS

### Prioridad ALTA 🔴
1. **Actualizar SecurityConfig** para permitir GET públicos en stats/achievements/comments
2. **Habilitar Flyway** para ejecutar scripts SQL automáticamente
3. **Corregir nombres de columnas** en scripts SQL (ballondor_wins)
4. **Insertar datos de achievements y comments** (ejecutar V4, V5, V6)
5. **Probar todos los endpoints GET públicos** (stats, achievements, comments)

### Prioridad MEDIA 🟡
6. **Crear usuario ADMIN en base de datos** para probar endpoints protegidos
7. **Probar sistema de moderación** (approve/reject comments)
8. **Probar rankings** (top goals, assists, ballondor)
9. **Probar suscripciones** (subscribe, unsubscribe, notifications)
10. **Validar paginación** en todos los endpoints

### Prioridad BAJA 🟢
11. Actualizar MySQL a 8.0+ (eliminar warning)
12. Configurar Java 17 en PATH permanente
13. Agregar tests unitarios (JUnit 5)
14. Documentar Swagger con ejemplos
15. Optimizar queries (N+1 problems)

---

## 📈 MÉTRICAS DE COBERTURA

### Endpoints Implementados vs Probados

| Categoría | Implementados | Probados | Exitosos | % Cobertura | % Éxito |
|-----------|---------------|----------|----------|-------------|---------|
| Players | 6 | 3 | 3 | 50% | 100% |
| Authentication | 3 | 2 | 2 | 67% | 100% |
| PlayerStats | 8 | 1 | 0 | 13% | 0% |
| Achievements | 10+ | 0 | 0 | 0% | - |
| Comments | 12+ | 0 | 0 | 0% | - |
| Subscriptions | 8 | 0 | 0 | 0% | - |
| **TOTAL** | **40+** | **6** | **5** | **15%** | **83%** |

### Modelos vs Datos

| Modelo | Tabla Creada | Datos Insertados | Cantidad | Estado |
|--------|--------------|------------------|----------|---------|
| Player | ✅ | ✅ | 3 | ✅ Completo |
| User | ✅ | ✅ | 3 | ✅ Completo |
| Role | ✅ | ✅ | 3 | ✅ Completo |
| PlayerStats | ✅ | ✅ Manual | 3 | ⚠️ Manual |
| Achievement | ✅ | ❌ | 0 | ❌ Vacía |
| Comment | ✅ | ❌ | 0 | ❌ Vacía |
| Subscription | ✅ | N/A | 0 | ✅ Esperado |

---

## 🔧 COMANDOS ÚTILES PARA CONTINUAR

### Iniciar Backend
```powershell
cd c:\xampp\htdocs\proyecto-goats-futbol\goats-api
& "C:\Program Files\Java\jdk-17\bin\java.exe" -jar target\goats-api-0.0.1-SNAPSHOT.jar
```

### Insertar Achievements (después de corregir SecurityConfig)
```powershell
& "C:\xampp\mysql\bin\mysql.exe" -u root goats_futbol < `
  goats-api\src\main\resources\db\migration\V4__create_achievements.sql
```

### Probar Endpoint con Token
```powershell
$token = "YOUR_JWT_TOKEN"
$headers = @{ "Authorization" = "Bearer $token" }
Invoke-RestMethod -Uri "http://localhost:8080/api/stats/player/1" -Headers $headers
```

### Verificar Logs del Backend
```powershell
Get-Job | Receive-Job
```

### Crear Usuario ADMIN Manualmente
```sql
-- 1. Insertar usuario
INSERT INTO users (username, email, password_hash, enabled)
VALUES ('superadmin', 'admin@goats.com', 
        '$2a$10$5o3lPvB4jUhcaQBUqJ9X3OwMnXjqCPFnBpFg8u7mS8s1EZqJJ8qLG', 1);

-- 2. Asignar rol ADMIN
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'superadmin' AND r.name = 'ADMIN';
```

---

## 📝 NOTAS FINALES

### Logros de la Sesión de Pruebas ✅
1. Backend compilado y ejecutándose exitosamente
2. Conexión a MySQL verificada
3. Autenticación JWT funciona correctamente
4. Endpoint de Players 100% funcional
5. Identificados 5 problemas con soluciones propuestas
6. Datos de player_stats insertados manualmente

### Aprendizajes 💡
1. Hibernate DDL no ejecuta scripts SQL de migración
2. Necesario Flyway o Liquibase para migraciones automáticas
3. SecurityConfig debe configurarse cuidadosamente para APIs públicas
4. Nombres de columnas: Hibernate naming strategy puede causar discrepancias
5. Java version mismatch es un problema común (PATH vs JAVA_HOME)

### Estado del Proyecto 📊
- **Frontend:** 100% ✅
- **Backend - Semana 1 (JWT):** 100% ✅
- **Backend - Semana 2 (Modelos):** 100% implementado, 15% probado ⚠️
- **Integración:** 0% ⏳

### Próxima Sesión 🎯
1. Actualizar SecurityConfig
2. Habilitar Flyway
3. Insertar todos los datos pre-cargados
4. Probar los 40+ endpoints completos
5. Documentar resultados completos
6. Commit de correcciones

---

**Generado el:** 16 de Noviembre de 2025 - 21:45:00  
**Backend Version:** 0.0.1-SNAPSHOT  
**Spring Boot:** 3.5.7  
**Java:** 17.0.12  
**MySQL:** 5.7 (XAMPP)  
**Commit:** `61aa1fd` (Semana 2 completa)

---

*¡Vamos crack! 🚀⚽ La Semana 2 está casi completa, solo faltan ajustes de configuración.*
