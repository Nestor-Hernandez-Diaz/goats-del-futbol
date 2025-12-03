# 🎯 CHECKLIST RÁPIDO - Demostración en Vivo

**GOATs del Fútbol - Conexión Base de Datos**

---

## ⚡ Antes de la Presentación (5 min)

### 1. Verificar Componentes Activos

```powershell
# Ejecutar script de verificación automática
cd c:\xampp\htdocs\proyecto-goats-futbol
.\verificar-sistema.ps1
```

**✅ Resultado esperado:**
- MySQL: ✅ CORRIENDO
- Backend: ✅ PROCESO DETECTADO
- API: ✅ OK (200)
- 3 jugadores cargados: Messi, Ronaldo, Neymar

---

### 2. Ejecutar Script SQL Actualizado

1. Abrir: `http://localhost/phpmyadmin`
2. Seleccionar BD: `goats_futbol`
3. Pestaña: **SQL**
4. Abrir: `COPIAR_EN_PHPMYADMIN_TODOS.sql`
5. **Ctrl+A** → **Ctrl+C** → Pegar en phpMyAdmin
6. Clic: **"Continuar"**

**✅ Verificar mensaje:** `TODOS LOS DATOS ACTUALIZADOS CORRECTAMENTE ✓`

---

### 3. Preparar Pestañas del Navegador

**Abrir 4 pestañas:**

1. **phpMyAdmin**
   - `http://localhost/phpmyadmin`
   - BD: `goats_futbol` → Tabla: `players`

2. **Backend API** (Swagger)
   - `http://localhost:8080/swagger-ui/index.html`

3. **Frontend Público**
   - `http://127.0.0.1:5500/pages/player.html?id=1`

4. **Admin Panel**
   - `http://127.0.0.1:5500/pages/admin-players.html`
   - Login: `admin` / `admin123`

---

### 4. Preparar PowerShell

**Comandos para copiar/pegar:**

```powershell
# 1. Listar todos los jugadores
Invoke-WebRequest -Uri "http://localhost:8080/api/players" | Select-Object -ExpandProperty Content | ConvertFrom-Json | Select-Object -ExpandProperty content | Select-Object id, name, nickname

# 2. Ver datos de Messi
$messi = (Invoke-WebRequest -Uri "http://localhost:8080/api/players/1").Content | ConvertFrom-Json
Write-Host "Nombre: $($messi.name)"
Write-Host "Apodo: $($messi.nickname)"
$videos = $messi.videos | ConvertFrom-Json
$videos | ForEach-Object { Write-Host "• $($_.title)" }

# 3. Monitorear logs del backend
Get-Content "c:\xampp\htdocs\proyecto-goats-futbol\goats-api\logs\spring.log" -Tail 20 -Wait
```

---

## 🎬 Durante la Presentación (10-15 min)

### DEMO 1: Mostrar Base de Datos (3 min)

**phpMyAdmin → goats_futbol → players**

1. Clic en **"Examinar"** (Browse)
2. Mostrar 3 registros: Messi, Ronaldo, Neymar
3. Clic en **"Editar"** (lápiz) de Messi
4. Scroll hasta campos JSON:
   - `videos` → Mostrar 3 URLs de YouTube
   - `playing_style` → Mostrar attributes (Regate: 98, etc.)
   - `achievements` → Mostrar estructura JSON

**💬 Script:**
> "Aquí tenemos la base de datos MySQL con 3 jugadores. Usamos columnas JSON para almacenar datos complejos como videos, estadísticas y logros. Esto nos da flexibilidad sin crear múltiples tablas."

---

### DEMO 2: Probar API Backend (3 min)

**PowerShell**

1. Ejecutar comando 1 (listar jugadores)
   ```
   id name                 nickname
   -- ----                 --------
    1 Lionel Messi         La Pulga
    2 Cristiano Ronaldo    CR7
    3 Neymar Jr            Ney
   ```

2. Ejecutar comando 2 (ver Messi)
   ```
   Nombre: Lionel Messi
   Apodo: La Pulga
   • Messi en la final del Mundial 2022
   • El gol maradoniano contra Getafe
   • Hat-trick contra el Real Madrid
   ```

3. **Alternativamente:** Usar Swagger UI
   - `http://localhost:8080/swagger-ui/index.html`
   - GET `/api/players`
   - "Try it out" → "Execute"
   - Mostrar JSON response

**💬 Script:**
> "El backend Spring Boot expone una API RESTful. Aquí probamos GET /api/players que devuelve los 3 jugadores con paginación. Los videos de YouTube que actualizamos en la BD se devuelven correctamente."

---

### DEMO 3: Actualizar desde Admin (5 min)

**Admin Panel**

1. Ir a: `http://127.0.0.1:5500/pages/admin-players.html`
2. Si no está logueado:
   - Login: `admin` / `admin123`
3. Ver tarjetas de 3 jugadores
4. Clic en **"Editar"** de Messi
5. Cambiar:
   - **Campo:** Apodo
   - **De:** "La Pulga"
   - **A:** "La Pulga Atómica"
6. Clic en **"Guardar Cambios"**
7. ✅ Mensaje: "Jugador actualizado exitosamente"

**💬 Script:**
> "Ahora voy a actualizar un jugador desde el panel de administración. Cambio el apodo de Messi de 'La Pulga' a 'La Pulga Atómica'. Esto envía una petición PUT al backend con autenticación JWT."

---

### DEMO 4: Verificar Cambio en BD (2 min)

**phpMyAdmin**

1. Refrescar tabla `players`
2. Buscar registro `id=1` (Messi)
3. Columna `nickname` ahora muestra: **"La Pulga Atómica"**

**💬 Script:**
> "Verificamos en phpMyAdmin que el cambio se guardó correctamente en MySQL. El campo nickname ahora tiene el nuevo valor."

---

### DEMO 5: Verificar en Frontend (2 min)

**Frontend Público**

1. Ir a: `http://127.0.0.1:5500/pages/player.html?id=1`
2. Esperar que cargue
3. Ver sección Hero con: **"La Pulga Atómica"**

**💬 Script:**
> "Finalmente, verificamos en el frontend público que el cambio se refleja automáticamente. Esto demuestra el flujo completo: Admin Web → API REST → MySQL → Frontend Público."

---

## 📊 Puntos Clave para Mencionar

### Arquitectura
- ✅ **Separación de capas:** Frontend, Backend API, Base de Datos
- ✅ **Comunicación:** HTTP/REST con JSON
- ✅ **Seguridad:** JWT con roles (ADMIN/USER)

### Tecnologías
- ✅ **Frontend:** HTML5, CSS3 (Flexbox/Grid), JavaScript ES6+
- ✅ **Backend:** Spring Boot 3.5.7, Spring Security, JPA/Hibernate
- ✅ **BD:** MySQL 8.0, utf8mb4, columnas JSON

### Funcionalidades
- ✅ **CRUD completo:** Crear, Leer, Actualizar, Eliminar jugadores
- ✅ **Autenticación:** Login con JWT, roles diferenciados
- ✅ **Persistencia:** Transacciones, cascade, relaciones OneToMany
- ✅ **Validación:** Jakarta Validation en backend, validación cliente

---

## ⚠️ Troubleshooting Rápido

### Backend no responde
```powershell
# Reiniciar desde VS Code
# F5 (Debug) o Run → GoatsApiApplication
# Esperar: "Started GoatsApiApplication in X seconds"
```

### MySQL no conecta
```
1. XAMPP Control Panel → Start MySQL
2. Verificar puerto 3306 disponible
```

### Error 403 en actualización
```
Verificar token JWT:
localStorage.getItem('jwtToken')

Verificar rol:
payload.roles debe incluir "ROLE_ADMIN"

Hacer logout y login con usuario "admin"
```

### Videos no cargan en frontend
```
1. Verificar que ejecutaste SQL actualizado
2. Verificar URLs en BD: youtube.com/watch?v=XXXXXXXXXXX
3. Probar URLs manualmente en navegador
```

---

## ✅ Checklist Final

Antes de presentar, confirmar:

- [ ] MySQL corriendo (PID visible)
- [ ] Backend corriendo (Puerto 8080 responde)
- [ ] Script SQL ejecutado (Videos actualizados)
- [ ] Usuario admin creado y puede hacer login
- [ ] Frontend con Live Server activo
- [ ] 4 pestañas de navegador abiertas
- [ ] PowerShell con comandos listos
- [ ] Presentación/diapositivas preparadas (si aplica)

---

## 🎓 Cierre de Presentación

**💬 Script de cierre:**

> "En resumen, he demostrado:
> 
> ✅ **Conexión completa** Frontend ↔ Backend ↔ Base de datos  
> ✅ **CRUD funcional** con Spring Boot y JPA  
> ✅ **Autenticación JWT** con roles ADMIN/USER  
> ✅ **Actualización en tiempo real** desde panel de administración  
> ✅ **Datos JSON complejos** almacenados correctamente en MySQL  
> 
> El proyecto demuestra una arquitectura full-stack moderna, escalable y segura. Las 3 capas están correctamente separadas, y la comunicación mediante API REST permite que el frontend y backend evolucionen de forma independiente.
> 
> ¿Preguntas?"

---

## 📁 Archivos Importantes

```
proyecto-goats-futbol/
├── COPIAR_EN_PHPMYADMIN_TODOS.sql   ← Script SQL maestro (652 líneas)
├── verificar-sistema.ps1             ← Script de verificación automática
├── documentation/
│   └── GUIA_DEMOSTRACION_BD.md       ← Guía completa (este documento padre)
├── goats-api/
│   ├── pom.xml                       ← Dependencias Maven
│   └── src/main/
│       ├── java/com/goats/api/
│       │   ├── controller/PlayerController.java  ← Endpoints REST
│       │   ├── service/PlayerService.java        ← Lógica de negocio
│       │   └── model/Player.java                 ← Entidad JPA
│       └── resources/
│           └── application.properties            ← Configuración BD
├── pages/
│   ├── admin-players.html            ← Panel de administración
│   └── player.html                   ← Página pública de jugadores
└── js/
    ├── auth.js                       ← Autenticación JWT
    └── admin-players.js              ← CRUD operaciones
```

---

**Última actualización:** 3 de diciembre de 2025  
**Versión:** 1.0 - Checklist Rápido  
**Tiempo estimado:** 10-15 minutos
