# 🎯 Guía de Demostración - Conexión Base de Datos

**GOATs del Fútbol - Proyecto Full Stack**  
**Fecha:** 3 de diciembre de 2025  
**Autor:** Nestor Hernandez Diaz

---

## 📋 Índice

1. [Arquitectura del Sistema](#arquitectura-del-sistema)
2. [Verificación de Componentes](#verificación-de-componentes)
3. [Actualizar Datos desde phpMyAdmin](#actualizar-datos-desde-phpmyadmin)
4. [Pruebas de Conexión API](#pruebas-de-conexión-api)
5. [Actualizar desde Panel Admin](#actualizar-desde-panel-admin)
6. [Checklist de Demostración](#checklist-de-demostración)

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                      FRONTEND (HTML/CSS/JS)                 │
│  • index.html, player.html (usuarios)                       │
│  • admin-players.html (administración)                      │
│  • auth.js (JWT autenticación)                              │
│  • admin-players.js (CRUD operaciones)                      │
└───────────────────────┬─────────────────────────────────────┘
                        │ HTTP Requests (fetch/AJAX)
                        ▼
┌─────────────────────────────────────────────────────────────┐
│              BACKEND API (Spring Boot 3.5.7)                │
│  • Puerto: 8080                                             │
│  • Endpoints: /api/players, /api/auth, etc.                 │
│  • Seguridad: JWT + Spring Security                         │
│  • Validación: @Valid, Jakarta Validation                   │
└───────────────────────┬─────────────────────────────────────┘
                        │ JDBC/JPA
                        ▼
┌─────────────────────────────────────────────────────────────┐
│              BASE DE DATOS (MySQL 8.0)                      │
│  • Servidor: localhost:3306                                 │
│  • Base de datos: goats_futbol                              │
│  • Tablas: players, users, roles, player_stats, etc.        │
│  • Charset: utf8mb4_unicode_ci                              │
└─────────────────────────────────────────────────────────────┘
```

---

## ✅ Verificación de Componentes

### 1. MySQL (XAMPP)

**Estado:** ✅ **CORRIENDO** (Proceso mysqld PID: 6008)

**Verificar manualmente:**
```powershell
# PowerShell
Get-Process -Name mysqld
```

**Acceso phpMyAdmin:**
```
URL: http://localhost/phpmyadmin
Usuario: root
Contraseña: (vacío)
Base de datos: goats_futbol
```

---

### 2. Backend Spring Boot

**Estado:** ✅ **CORRIENDO** (Puerto 8080)

**Configuración actual** (`application.properties`):
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/goats_futbol?useSSL=false
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

**Verificar manualmente:**
```powershell
# PowerShell - Probar endpoint
Invoke-WebRequest -Uri "http://localhost:8080/api/players" -Method GET
```

**Respuesta esperada:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Lionel Messi",
      "nickname": "La Pulga",
      "country": "Argentina",
      "position": "Delantero",
      "biography": "...",
      "heroInfo": {...},
      "videos": [...]
    },
    {
      "id": 2,
      "name": "Cristiano Ronaldo",
      ...
    },
    {
      "id": 3,
      "name": "Neymar Jr",
      ...
    }
  ],
  "pageable": {...},
  "totalElements": 3
}
```

---

### 3. Frontend

**URLs de prueba:**
```
• Página principal: http://127.0.0.1:5500/index.html
• Jugadores dinámicos: http://127.0.0.1:5500/pages/player.html?id=1
• Login: http://127.0.0.1:5500/pages/login.html
• Admin: http://127.0.0.1:5500/pages/admin-players.html
```

**Servidor:** Live Server (VS Code) en puerto 5500

---

## 🗄️ Actualizar Datos desde phpMyAdmin

### Paso 1: Abrir phpMyAdmin

1. Abre el navegador
2. Ve a: `http://localhost/phpmyadmin`
3. Selecciona base de datos: `goats_futbol`
4. Clic en pestaña **"SQL"**

---

### Paso 2: Ejecutar Script de Actualización

**Archivo:** `COPIAR_EN_PHPMYADMIN_TODOS.sql` (652 líneas)

**Contenido:** 
- ✅ Actualiza `playing_style` de 3 jugadores
- ✅ Actualiza `legacy` de 3 jugadores
- ✅ Actualiza `gallery` de 3 jugadores
- ✅ Actualiza `achievements` de 3 jugadores
- ✅ Actualiza `stats` de 3 jugadores
- ✅ Actualiza `season_stats` de 3 jugadores
- ✅ **Actualiza `videos` con URLs correctas** (últimas modificaciones)

**Videos actualizados:**

**Messi:**
```json
[
  {"url": "https://www.youtube.com/watch?v=uYuUFhW7Vi8", "title": "Messi en la final del Mundial 2022"},
  {"url": "https://www.youtube.com/watch?v=waETo-ZWCRw", "title": "El gol maradoniano contra Getafe"},
  {"url": "https://www.youtube.com/watch?v=Sy6emSOKlQY", "title": "Hat-trick contra el Real Madrid"}
]
```

**Cristiano Ronaldo:**
```json
[
  {"url": "https://www.youtube.com/watch?v=P-jRW5RLlKg", "title": "La chilena legendaria contra la Juventus"},
  {"url": "https://www.youtube.com/watch?v=uJZ5H_DDVfM", "title": "El liderazgo en la final de la Eurocopa 2016"},
  {"url": "https://www.youtube.com/watch?v=cx3B-9ZPN6s", "title": "Hat-trick contra España en el Mundial 2018"}
]
```

**Neymar:**
```json
[
  {"url": "https://www.youtube.com/watch?v=1wvwSER_w-M", "title": "El gol que le valió el Premio Puskás 2011"},
  {"url": "https://www.youtube.com/watch?v=ERODrQXI-hY", "title": "Su actuación en la remontada 6-1 contra el PSG"},
  {"url": "https://www.youtube.com/watch?v=oNgE5SY5oGQ", "title": "El penal decisivo en la final olímpica de Río 2016"}
]
```

---

### Paso 3: Copiar y Ejecutar Script

1. Abre `COPIAR_EN_PHPMYADMIN_TODOS.sql` en VS Code
2. Selecciona **TODO** el contenido (Ctrl+A)
3. Copia (Ctrl+C)
4. Vuelve a phpMyAdmin → Pestaña "SQL"
5. Pega el contenido (Ctrl+V)
6. Clic en **"Continuar"** o **"Go"**

**Resultado esperado:**
```
✓ Messi - playing_style actualizado
✓ Messi - legacy actualizado
✓ Messi - gallery actualizada
✓ Messi - achievements actualizados
✓ Messi - stats actualizadas
✓ Messi - season_stats actualizadas
✓ Messi - videos actualizados

✓ Ronaldo - playing_style actualizado
✓ Ronaldo - legacy actualizado
...

✓ Neymar - playing_style actualizado
✓ Neymar - legacy actualizado
...

============================================
TODOS LOS DATOS ACTUALIZADOS CORRECTAMENTE ✓
```

---

### Paso 4: Verificar en phpMyAdmin

1. Ve a tabla **`players`**
2. Clic en **"Examinar"** (Browse)
3. Verifica los 3 registros:
   - `id=1` → Lionel Messi
   - `id=2` → Cristiano Ronaldo
   - `id=3` → Neymar Jr

4. Clic en **"Edit"** (lápiz) de cualquier jugador
5. Verifica campos JSON:
   - `videos` debe contener 3 URLs de YouTube actualizadas
   - `playing_style` debe tener description + attributes
   - `achievements` debe tener clubs + national + individual

---

## 🔌 Pruebas de Conexión API

### Prueba 1: GET /api/players (Lista completa)

**PowerShell:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/players" -Method GET | 
    Select-Object -ExpandProperty Content | 
    ConvertFrom-Json | 
    Select-Object -ExpandProperty content | 
    Select-Object id, name, nickname, country
```

**Resultado esperado:**
```
id name                 nickname    country
-- ----                 --------    -------
 1 Lionel Messi         La Pulga    Argentina
 2 Cristiano Ronaldo    CR7         Portugal
 3 Neymar Jr            Ney         Brasil
```

---

### Prueba 2: GET /api/players/1 (Messi individual)

**PowerShell:**
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/players/1" -Method GET
$player = $response.Content | ConvertFrom-Json
Write-Host "Nombre: $($player.name)"
Write-Host "Apodo: $($player.nickname)"
Write-Host "Videos: $($player.videos.Count) videos cargados"
```

**Resultado esperado:**
```
Nombre: Lionel Messi
Apodo: La Pulga
Videos: 3 videos cargados
```

---

### Prueba 3: Verificar Videos de YouTube

**PowerShell:**
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/players/1" -Method GET
$player = $response.Content | ConvertFrom-Json
$videos = $player.videos | ConvertFrom-Json

Write-Host "VIDEOS DE MESSI:"
$videos | ForEach-Object {
    Write-Host "• $($_.title)"
    Write-Host "  URL: $($_.url)"
}
```

**Resultado esperado:**
```
VIDEOS DE MESSI:
• Messi en la final del Mundial 2022
  URL: https://www.youtube.com/watch?v=uYuUFhW7Vi8
• El gol maradoniano contra Getafe
  URL: https://www.youtube.com/watch?v=waETo-ZWCRw
• Hat-trick contra el Real Madrid
  URL: https://www.youtube.com/watch?v=Sy6emSOKlQY
```

---

## 👨‍💼 Actualizar desde Panel Admin

### Paso 1: Login como Administrador

1. Abre: `http://127.0.0.1:5500/pages/login.html`
2. Credenciales:
   ```
   Usuario: admin
   Contraseña: admin123
   ```
3. Clic en **"Iniciar Sesión"**
4. Verifica que aparezca mensaje: "Bienvenido, admin"

---

### Paso 2: Acceder al Panel de Jugadores

1. En el menú de navegación, clic en **"⚙️ Admin"**
2. O ve directo a: `http://127.0.0.1:5500/pages/admin-players.html`
3. Debes ver tarjetas de los 3 jugadores:
   - Lionel Messi (La Pulga)
   - Cristiano Ronaldo (CR7)
   - Neymar Jr (Ney)

---

### Paso 3: Editar Jugador (Ejemplo: Messi)

1. En la tarjeta de **Messi**, clic en botón **"Editar"** (azul)
2. Se abrirá modal con formulario pre-cargado
3. **Campos editables:**
   - Nombre completo
   - Apodo
   - País
   - Posición
   - Biografía (editor WYSIWYG con TinyMCE)

4. **Hacer un cambio de prueba:**
   ```
   Campo: Apodo
   Valor original: "La Pulga"
   Nuevo valor: "La Pulga Atómica"
   ```

5. Clic en **"Guardar Cambios"**

---

### Paso 4: Verificar Actualización en BD

**Opción A: phpMyAdmin**
1. Ve a `http://localhost/phpmyadmin`
2. Base de datos: `goats_futbol`
3. Tabla: `players`
4. Clic en "Examinar"
5. Busca registro con `id=1` (Messi)
6. Verifica columna `nickname` → debe mostrar **"La Pulga Atómica"**

**Opción B: PowerShell**
```powershell
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/players/1" -Method GET
$player = $response.Content | ConvertFrom-Json
Write-Host "Apodo actualizado: $($player.nickname)"
```

**Resultado esperado:**
```
Apodo actualizado: La Pulga Atómica
```

---

### Paso 5: Verificar en Frontend Público

1. Abre: `http://127.0.0.1:5500/pages/player.html?id=1`
2. Espera que cargue la página de Messi
3. En la sección **Hero**, verifica que aparezca: **"La Pulga Atómica"**
4. Esto demuestra que:
   - ✅ Admin actualizó dato
   - ✅ Se guardó en MySQL
   - ✅ Backend API devuelve dato actualizado
   - ✅ Frontend lo muestra correctamente

---

## 📝 Checklist de Demostración

### Preparación Previa (5 min)

- [x] MySQL corriendo (verificar con `Get-Process mysqld`)
- [x] Backend Spring Boot corriendo en puerto 8080
- [ ] Script SQL ejecutado en phpMyAdmin
- [ ] Frontend con Live Server activo en puerto 5500
- [ ] Usuario admin creado en base de datos
- [ ] Navegador con pestañas preparadas:
  - phpMyAdmin
  - admin-players.html
  - player.html?id=1

---

### Demostración en Vivo (10-15 min)

#### 1️⃣ Mostrar Arquitectura (2 min)
- [ ] Explicar diagrama de arquitectura (Frontend → Backend → BD)
- [ ] Mostrar estructura de carpetas en VS Code
- [ ] Explicar tecnologías: Spring Boot, MySQL, JWT

#### 2️⃣ Mostrar Base de Datos (3 min)
- [ ] Abrir phpMyAdmin
- [ ] Mostrar base de datos `goats_futbol`
- [ ] Mostrar tabla `players` con 3 registros
- [ ] Abrir editor de Messi, mostrar campos JSON:
  - `videos` (3 URLs de YouTube)
  - `playing_style` (description + attributes)
  - `achievements` (clubs + national + individual)

#### 3️⃣ Probar API Backend (3 min)
- [ ] Abrir PowerShell o Postman
- [ ] GET `http://localhost:8080/api/players` → Mostrar JSON con 3 jugadores
- [ ] GET `http://localhost:8080/api/players/1` → Mostrar datos de Messi
- [ ] Mostrar consola de Spring Boot con logs SQL:
  ```
  Hibernate: select ... from players where player0_.id=?
  ```

#### 4️⃣ Actualizar desde Admin (4 min)
- [ ] Hacer login en `login.html` con usuario `admin`
- [ ] Ir a `admin-players.html`
- [ ] Editar Messi: Cambiar apodo de "La Pulga" a "La Pulga Atómica"
- [ ] Guardar cambios
- [ ] Mostrar mensaje de éxito: "Jugador actualizado exitosamente"

#### 5️⃣ Verificar Cambios (3 min)
- [ ] Refrescar phpMyAdmin → Verificar cambio en columna `nickname`
- [ ] Abrir `player.html?id=1` → Verificar que muestre "La Pulga Atómica"
- [ ] Explicar flujo completo:
  ```
  Admin Web → PUT /api/players/1 → Spring Boot → UPDATE MySQL → 
  GET /api/players/1 → Spring Boot → Frontend → Usuario ve cambio
  ```

---

## 🎬 Script de Presentación

### Introducción (1 min)
> "Buenos días/tardes. Les presento **GOATs del Fútbol**, un proyecto full-stack que combina frontend moderno con backend robusto. El objetivo principal de esta demostración es mostrar la **conexión completa entre la aplicación web y la base de datos MySQL**, demostrando cómo se actualizan los datos en tiempo real."

### Arquitectura (2 min)
> "La aplicación sigue una arquitectura de 3 capas:
> 
> 1. **Frontend**: HTML5, CSS3 con Flexbox/Grid, JavaScript ES6+ con fetch API
> 2. **Backend**: Spring Boot 3.5.7 con Spring Security, JWT, JPA/Hibernate
> 3. **Base de datos**: MySQL 8.0 con charset utf8mb4 para soporte Unicode completo
> 
> La comunicación se realiza mediante API RESTful con endpoints como:
> - GET /api/players → Lista todos los jugadores
> - GET /api/players/{id} → Obtiene un jugador específico
> - PUT /api/players/{id} → Actualiza un jugador (requiere autenticación ADMIN)
> - DELETE /api/players/{id} → Elimina un jugador (requiere autenticación ADMIN)"

### Demostración BD (3 min)
> "Primero, veamos la base de datos en phpMyAdmin. Aquí tenemos la tabla `players` con 3 registros: Messi, Ronaldo y Neymar.
> 
> [Abrir registro de Messi]
> 
> Pueden ver que usamos columnas JSON para almacenar datos complejos:
> - `videos`: Array de 3 videos de YouTube con URLs, títulos y thumbnails
> - `playing_style`: Objeto con description (texto largo) y attributes (habilidades numéricas)
> - `achievements`: Estructura jerárquica con logros de clubes, selección nacional y premios individuales
> 
> Este diseño permite flexibilidad sin crear decenas de tablas adicionales."

### Demostración API (3 min)
> "Ahora probemos el backend. Voy a hacer una petición GET a la API:
> 
> [Ejecutar PowerShell o Postman]
> 
> Como pueden ver, el backend devuelve un JSON con paginación. El campo `content` contiene los 3 jugadores con todos sus datos. El backend está configurado con Spring Security, por lo que los endpoints GET son públicos, pero los endpoints de modificación (POST, PUT, DELETE) requieren autenticación JWT con rol ADMIN."

### Demostración Admin (4 min)
> "Ahora viene la parte interesante. Voy a hacer login como administrador y actualizar un jugador desde el panel de administración.
> 
> [Login → Admin Panel → Editar Messi]
> 
> Cambio el apodo de 'La Pulga' a 'La Pulga Atómica' y guardo.
> 
> [Mostrar mensaje de éxito]
> 
> Ahora, para demostrar que el cambio se guardó en la base de datos:
> 
> 1. [Refrescar phpMyAdmin] → El campo `nickname` ahora muestra 'La Pulga Atómica'
> 2. [Abrir player.html?id=1] → El frontend público también muestra el cambio
> 
> Esto demuestra el flujo completo: Admin Web → API REST → MySQL → Frontend Público"

### Cierre (1 min)
> "En resumen, he demostrado:
> 
> ✅ Conexión exitosa Frontend ↔ Backend ↔ Base de datos  
> ✅ CRUD completo con Spring Boot  
> ✅ Autenticación JWT con roles  
> ✅ Actualización en tiempo real desde panel de administración  
> ✅ Datos JSON complejos almacenados correctamente en MySQL  
> 
> ¿Preguntas?"

---

## 🛠️ Troubleshooting

### Problema: Backend no responde

**Síntomas:**
```
❌ Backend NO RESPONDE: No se puede establecer conexión con el servidor remoto
```

**Solución:**
1. Verificar que Spring Boot esté corriendo:
   ```powershell
   Get-Process -Name java
   ```
2. Si no está corriendo, iniciar desde VS Code:
   - Clic derecho en `GoatsApiApplication.java`
   - "Run Java" o F5 (Debug)
3. Esperar a ver en consola:
   ```
   Started GoatsApiApplication in X.XXX seconds
   ```

---

### Problema: MySQL no conecta

**Síntomas:**
```
Caused by: com.mysql.cj.jdbc.exceptions.CommunicationsException: 
Communications link failure
```

**Solución:**
1. Abrir XAMPP Control Panel
2. Iniciar módulo MySQL
3. Verificar puerto 3306:
   ```powershell
   Test-NetConnection -ComputerName localhost -Port 3306
   ```

---

### Problema: Error 403 Forbidden en PUT/DELETE

**Síntomas:**
```
403 Forbidden - Permisos insuficientes
```

**Solución:**
1. Verificar que el token JWT es de usuario ADMIN:
   ```javascript
   const token = localStorage.getItem('jwtToken');
   const payload = JSON.parse(atob(token.split('.')[1]));
   console.log(payload.roles); // Debe incluir "ROLE_ADMIN"
   ```
2. Si no es admin, hacer logout y login con usuario `admin`

---

### Problema: Videos de YouTube no cargan

**Síntomas:**
- Iframe muestra "Video no disponible"
- Error de CORS o embedding deshabilitado

**Solución:**
1. Verificar que las URLs son correctas (formato: `youtube.com/watch?v=XXXXXXXXXXX`)
2. Algunos videos no permiten embedding, usar alternativas
3. Ejecutar script SQL actualizado con URLs verificadas

---

## 📊 Métricas del Proyecto

### Base de Datos
- **Tablas:** 9 (players, users, roles, user_roles, player_stats, achievements, comments, subscriptions, notifications)
- **Registros de jugadores:** 3 (Messi, Ronaldo, Neymar)
- **Campos JSON por jugador:** 10 (hero_info, profile_stats, career_highlights, playing_style, achievements, stats, season_stats, gallery, legacy, videos)
- **Charset:** utf8mb4_unicode_ci (soporte completo UTF-8)

### Backend API
- **Framework:** Spring Boot 3.5.7
- **Lenguaje:** Java 17
- **Dependencias principales:** Spring Web, Spring Data JPA, Spring Security, JWT (jjwt 0.12.3), MySQL Connector, Flyway
- **Endpoints:** 15+ (players, auth, stats, achievements, comments, subscriptions, notifications)
- **Seguridad:** JWT con roles (ROLE_USER, ROLE_ADMIN)
- **Documentación:** Swagger/OpenAPI en `/swagger-ui/index.html`

### Frontend
- **Páginas:** 13 HTML
- **CSS:** 4122 líneas (styles.css consolidado)
- **JavaScript:** 5 archivos principales (main.js, auth.js, admin-players.js, player-loader.js, comments.js)
- **Frameworks CSS:** Font Awesome 6.4.0
- **Editor WYSIWYG:** TinyMCE 6
- **Responsive:** 3 breakpoints (desktop 1200px+, tablet 768-992px, mobile <768px)

---

## 🎓 Puntos Clave para la Presentación

1. **Separación de Responsabilidades**
   - Frontend no accede directo a BD, usa API REST
   - Backend maneja lógica de negocio y seguridad
   - BD solo es accesible por backend

2. **Seguridad Implementada**
   - JWT para autenticación stateless
   - Roles para autorización (ADMIN vs USER)
   - Contraseñas hasheadas con BCrypt
   - Endpoints sensibles protegidos con `@PreAuthorize`

3. **Persistencia Robusta**
   - JPA/Hibernate para ORM
   - Transacciones con `@Transactional`
   - Migraciones con Flyway (deshabilitado, se usa DDL auto)
   - Cascade para relaciones (OneToMany, OneToOne)

4. **Escalabilidad**
   - Paginación en lista de jugadores
   - Campos JSON para datos flexibles
   - Índices en columnas clave
   - Preparado para microservicios

5. **Testing Realizado**
   - ✅ CRUD completo probado
   - ✅ Autenticación JWT verificada
   - ✅ Roles y permisos validados
   - ✅ Conexión BD → API → Frontend confirmada

---

## 📞 Contacto y Soporte

**Desarrollador:** Nestor Hernandez Diaz  
**Repositorio:** https://github.com/Nestor-Hernandez-Diaz/goats-del-futbol  
**Tecnologías:** Spring Boot, MySQL, HTML5, CSS3, JavaScript ES6+

---

**Última actualización:** 3 de diciembre de 2025  
**Versión del documento:** 1.0
