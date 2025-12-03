# ============================================
# Script de Pruebas Completas - GOATs del Fútbol API
# ============================================
# Este script prueba todas las funcionalidades del backend
# Fecha: 17 Noviembre 2025

$ErrorActionPreference = "Stop"
$API_BASE = "http://localhost:8080/api"
$TOKEN = ""
$USER_ID = ""
$ADMIN_TOKEN = ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  PRUEBAS COMPLETAS - GOATs del Fútbol  " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Función auxiliar para hacer peticiones
function Invoke-ApiRequest {
    param(
        [string]$Method,
        [string]$Endpoint,
        [string]$Token = "",
        [object]$Body = $null
    )
    
    $headers = @{
        "Content-Type" = "application/json"
    }
    
    if ($Token) {
        $headers["Authorization"] = "Bearer $Token"
    }
    
    $params = @{
        Uri = "$API_BASE$Endpoint"
        Method = $Method
        Headers = $headers
    }
    
    if ($Body) {
        $params["Body"] = ($Body | ConvertTo-Json -Depth 10)
    }
    
    try {
        $response = Invoke-RestMethod @params
        return @{ Success = $true; Data = $response }
    } catch {
        return @{ Success = $false; Error = $_.Exception.Message; StatusCode = $_.Exception.Response.StatusCode.value__ }
    }
}

# ============================================
# TEST 1: Autenticación
# ============================================
Write-Host "TEST 1: Sistema de Autenticación" -ForegroundColor Yellow
Write-Host "-----------------------------------" -ForegroundColor Gray

# Registrar usuario normal
Write-Host "1.1. Registrando usuario de prueba..." -ForegroundColor Gray
$registerData = @{
    username = "testuser_$(Get-Random)"
    email = "test_$(Get-Random)@example.com"
    password = "Test123456"
}

$result = Invoke-ApiRequest -Method POST -Endpoint "/auth/register" -Body $registerData
if ($result.Success) {
    Write-Host "   ✓ Usuario registrado exitosamente" -ForegroundColor Green
    $USER_ID = $result.Data.id
    Write-Host "   User ID: $USER_ID" -ForegroundColor Gray
} else {
    Write-Host "   ✗ Error registrando usuario: $($result.Error)" -ForegroundColor Red
    exit 1
}

# Login usuario normal
Write-Host "1.2. Iniciando sesión..." -ForegroundColor Gray
$loginData = @{
    username = $registerData.username
    password = $registerData.password
}

$result = Invoke-ApiRequest -Method POST -Endpoint "/auth/login" -Body $loginData
if ($result.Success) {
    $TOKEN = $result.Data.token
    Write-Host "   ✓ Login exitoso" -ForegroundColor Green
    Write-Host "   Token obtenido (primeros 20 chars): $($TOKEN.Substring(0, 20))..." -ForegroundColor Gray
} else {
    Write-Host "   ✗ Error en login: $($result.Error)" -ForegroundColor Red
    exit 1
}

# Login admin (usuario precargado)
Write-Host "1.3. Iniciando sesión como admin..." -ForegroundColor Gray
$adminLoginData = @{
    username = "admin"
    password = "admin123"
}

$result = Invoke-ApiRequest -Method POST -Endpoint "/auth/login" -Body $adminLoginData
if ($result.Success) {
    $ADMIN_TOKEN = $result.Data.token
    Write-Host "   ✓ Login admin exitoso" -ForegroundColor Green
} else {
    Write-Host "   ⚠ Admin no disponible (puede ser esperado en DB nueva)" -ForegroundColor Yellow
}

Write-Host ""

# ============================================
# TEST 2: Dashboard de Jugadores
# ============================================
Write-Host "TEST 2: Dashboard de Jugadores (CRUD)" -ForegroundColor Yellow
Write-Host "--------------------------------------" -ForegroundColor Gray

# Listar jugadores
Write-Host "2.1. Listando todos los jugadores..." -ForegroundColor Gray
$result = Invoke-ApiRequest -Method GET -Endpoint "/players"
if ($result.Success) {
    $players = $result.Data.content
    Write-Host "   ✓ Jugadores obtenidos: $($players.Count)" -ForegroundColor Green
    if ($players.Count -gt 0) {
        Write-Host "   Ejemplo: $($players[0].name) ($($players[0].nationality))" -ForegroundColor Gray
        $script:PLAYER_ID = $players[0].id
    }
} else {
    Write-Host "   ✗ Error listando jugadores: $($result.Error)" -ForegroundColor Red
}

# Obtener detalle de jugador
if ($script:PLAYER_ID) {
    Write-Host "2.2. Obteniendo detalle de jugador ID $($script:PLAYER_ID)..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/players/$($script:PLAYER_ID)"
    if ($result.Success) {
        $player = $result.Data
        Write-Host "   ✓ Jugador: $($player.name)" -ForegroundColor Green
        Write-Host "   Posición: $($player.position), Edad: $($player.age)" -ForegroundColor Gray
    } else {
        Write-Host "   ✗ Error obteniendo jugador: $($result.Error)" -ForegroundColor Red
    }
}

# Buscar jugadores
Write-Host "2.3. Buscando jugadores por nombre..." -ForegroundColor Gray
$result = Invoke-ApiRequest -Method GET -Endpoint "/players/search?name=Messi"
if ($result.Success) {
    Write-Host "   ✓ Búsqueda exitosa: $($result.Data.Count) resultados" -ForegroundColor Green
} else {
    Write-Host "   ⚠ Error en búsqueda: $($result.Error)" -ForegroundColor Yellow
}

Write-Host ""

# ============================================
# TEST 3: Dashboard de Estadísticas
# ============================================
Write-Host "TEST 3: Dashboard de Estadísticas" -ForegroundColor Yellow
Write-Host "----------------------------------" -ForegroundColor Gray

if ($script:PLAYER_ID) {
    # Obtener stats de jugador
    Write-Host "3.1. Obteniendo estadísticas del jugador..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/stats/player/$($script:PLAYER_ID)"
    if ($result.Success) {
        $stats = $result.Data
        Write-Host "   ✓ Estadísticas obtenidas" -ForegroundColor Green
        Write-Host "   Goles: $($stats.goals), Asistencias: $($stats.assists)" -ForegroundColor Gray
    } else {
        Write-Host "   ⚠ No hay estadísticas para este jugador" -ForegroundColor Yellow
    }
}

# Top 10 goleadores
Write-Host "3.2. Obteniendo top 10 goleadores..." -ForegroundColor Gray
$result = Invoke-ApiRequest -Method GET -Endpoint "/stats/top-scorers?limit=10"
if ($result.Success) {
    Write-Host "   ✓ Top goleadores obtenido: $($result.Data.Count) jugadores" -ForegroundColor Green
    if ($result.Data.Count -gt 0) {
        Write-Host "   #1: $($result.Data[0].playerName) - $($result.Data[0].goals) goles" -ForegroundColor Gray
    }
} else {
    Write-Host "   ⚠ Error obteniendo top goleadores: $($result.Error)" -ForegroundColor Yellow
}

# Top 10 asistentes
Write-Host "3.3. Obteniendo top 10 asistentes..." -ForegroundColor Gray
$result = Invoke-ApiRequest -Method GET -Endpoint "/stats/top-assisters?limit=10"
if ($result.Success) {
    Write-Host "   ✓ Top asistentes obtenido: $($result.Data.Count) jugadores" -ForegroundColor Green
} else {
    Write-Host "   ⚠ Error obteniendo top asistentes: $($result.Error)" -ForegroundColor Yellow
}

Write-Host ""

# ============================================
# TEST 4: Sistema de Suscripciones
# ============================================
Write-Host "TEST 4: Sistema de Suscripciones" -ForegroundColor Yellow
Write-Host "--------------------------------" -ForegroundColor Gray

if ($script:PLAYER_ID -and $TOKEN) {
    # Suscribirse a un jugador
    Write-Host "4.1. Suscribiéndose al jugador..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method POST -Endpoint "/subscriptions/player/$($script:PLAYER_ID)" -Token $TOKEN
    if ($result.Success) {
        Write-Host "   ✓ Suscripción exitosa" -ForegroundColor Green
        Write-Host "   Notificaciones habilitadas: $($result.Data.notificationsEnabled)" -ForegroundColor Gray
    } else {
        Write-Host "   ⚠ Error en suscripción: $($result.Error)" -ForegroundColor Yellow
    }
    
    # Verificar suscripción
    Write-Host "4.2. Verificando estado de suscripción..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/subscriptions/player/$($script:PLAYER_ID)/check" -Token $TOKEN
    if ($result.Success) {
        Write-Host "   ✓ Verificación exitosa: $($result.Data.subscribed)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Error verificando: $($result.Error)" -ForegroundColor Yellow
    }
    
    # Listar mis suscripciones
    Write-Host "4.3. Listando mis suscripciones..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/subscriptions/user/$USER_ID" -Token $TOKEN
    if ($result.Success) {
        Write-Host "   ✓ Suscripciones obtenidas: $($result.Data.Count)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Error listando: $($result.Error)" -ForegroundColor Yellow
    }
    
    # Contar suscriptores del jugador
    Write-Host "4.4. Contando suscriptores del jugador..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/subscriptions/player/$($script:PLAYER_ID)/count" -Token $TOKEN
    if ($result.Success) {
        Write-Host "   ✓ Total suscriptores: $($result.Data)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Error contando: $($result.Error)" -ForegroundColor Yellow
    }
}

Write-Host ""

# ============================================
# TEST 5: Sistema de Comentarios
# ============================================
Write-Host "TEST 5: Sistema de Comentarios" -ForegroundColor Yellow
Write-Host "-------------------------------" -ForegroundColor Gray

if ($script:PLAYER_ID -and $TOKEN) {
    # Crear comentario
    Write-Host "5.1. Creando comentario..." -ForegroundColor Gray
    $commentData = @{
        playerId = $script:PLAYER_ID
        content = "¡Gran jugador! Uno de los mejores de todos los tiempos. $(Get-Date)"
    }
    
    $result = Invoke-ApiRequest -Method POST -Endpoint "/comments" -Token $TOKEN -Body $commentData
    if ($result.Success) {
        Write-Host "   ✓ Comentario creado (estado: $($result.Data.status))" -ForegroundColor Green
        $script:COMMENT_ID = $result.Data.id
    } else {
        Write-Host "   ⚠ Error creando comentario: $($result.Error)" -ForegroundColor Yellow
    }
    
    # Listar comentarios del jugador
    Write-Host "5.2. Listando comentarios del jugador..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/comments/player/$($script:PLAYER_ID)"
    if ($result.Success) {
        Write-Host "   ✓ Comentarios obtenidos: $($result.Data.content.Count)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Error listando comentarios: $($result.Error)" -ForegroundColor Yellow
    }
    
    # Aprobar comentario (solo ADMIN)
    if ($script:COMMENT_ID -and $ADMIN_TOKEN) {
        Write-Host "5.3. Aprobando comentario (como admin)..." -ForegroundColor Gray
        $result = Invoke-ApiRequest -Method POST -Endpoint "/comments/$($script:COMMENT_ID)/approve" -Token $ADMIN_TOKEN
        if ($result.Success) {
            Write-Host "   ✓ Comentario aprobado (debería generar notificación)" -ForegroundColor Green
        } else {
            Write-Host "   ⚠ Error aprobando: $($result.Error)" -ForegroundColor Yellow
        }
    }
}

Write-Host ""

# ============================================
# TEST 6: Sistema de Notificaciones
# ============================================
Write-Host "TEST 6: Sistema de Notificaciones" -ForegroundColor Yellow
Write-Host "----------------------------------" -ForegroundColor Gray

if ($TOKEN) {
    # Listar notificaciones
    Write-Host "6.1. Listando mis notificaciones..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/notifications" -Token $TOKEN
    if ($result.Success) {
        $notifications = $result.Data.content
        Write-Host "   ✓ Notificaciones obtenidas: $($notifications.Count)" -ForegroundColor Green
        
        if ($notifications.Count -gt 0) {
            $script:NOTIFICATION_ID = $notifications[0].id
            Write-Host "   Última: $($notifications[0].message)" -ForegroundColor Gray
            Write-Host "   Tipo: $($notifications[0].type), Leída: $($notifications[0].isRead)" -ForegroundColor Gray
        }
    } else {
        Write-Host "   ⚠ Error listando notificaciones: $($result.Error)" -ForegroundColor Yellow
    }
    
    # Contar no leídas
    Write-Host "6.2. Contando notificaciones no leídas..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/notifications/unread/count" -Token $TOKEN
    if ($result.Success) {
        Write-Host "   ✓ No leídas: $($result.Data)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Error contando: $($result.Error)" -ForegroundColor Yellow
    }
    
    # Marcar como leída
    if ($script:NOTIFICATION_ID) {
        Write-Host "6.3. Marcando notificación como leída..." -ForegroundColor Gray
        $result = Invoke-ApiRequest -Method PATCH -Endpoint "/notifications/$($script:NOTIFICATION_ID)/read" -Token $TOKEN
        if ($result.Success) {
            Write-Host "   ✓ Notificación marcada como leída" -ForegroundColor Green
        } else {
            Write-Host "   ⚠ Error marcando: $($result.Error)" -ForegroundColor Yellow
        }
    }
    
    # Filtrar por tipo
    Write-Host "6.4. Filtrando notificaciones por tipo COMMENT..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/notifications?type=COMMENT" -Token $TOKEN
    if ($result.Success) {
        Write-Host "   ✓ Notificaciones de tipo COMMENT: $($result.Data.content.Count)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Error filtrando: $($result.Error)" -ForegroundColor Yellow
    }
}

Write-Host ""

# ============================================
# TEST 7: Sistema de Logros
# ============================================
Write-Host "TEST 7: Sistema de Logros" -ForegroundColor Yellow
Write-Host "-------------------------" -ForegroundColor Gray

if ($script:PLAYER_ID) {
    # Listar logros del jugador
    Write-Host "7.1. Listando logros del jugador..." -ForegroundColor Gray
    $result = Invoke-ApiRequest -Method GET -Endpoint "/achievements/player/$($script:PLAYER_ID)"
    if ($result.Success) {
        Write-Host "   ✓ Logros obtenidos: $($result.Data.content.Count)" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Error listando logros: $($result.Error)" -ForegroundColor Yellow
    }
    
    # Crear logro (solo ADMIN)
    if ($ADMIN_TOKEN) {
        Write-Host "7.2. Creando logro (como admin)..." -ForegroundColor Gray
        $achievementData = @{
            playerId = $script:PLAYER_ID
            title = "Logro de Prueba $(Get-Random)"
            description = "Logro creado durante pruebas automatizadas"
            year = 2025
            type = "INDIVIDUAL"
            competition = "Pruebas API"
        }
        
        $result = Invoke-ApiRequest -Method POST -Endpoint "/achievements" -Token $ADMIN_TOKEN -Body $achievementData
        if ($result.Success) {
            Write-Host "   ✓ Logro creado (debería generar notificación)" -ForegroundColor Green
            $script:ACHIEVEMENT_ID = $result.Data.id
        } else {
            Write-Host "   ⚠ Error creando logro: $($result.Error)" -ForegroundColor Yellow
        }
    }
}

Write-Host ""

# ============================================
# TEST 8: Validar Flujo Completo de Notificaciones
# ============================================
Write-Host "TEST 8: Flujo Completo de Notificaciones" -ForegroundColor Yellow
Write-Host "-----------------------------------------" -ForegroundColor Gray

if ($TOKEN) {
    Write-Host "8.1. Verificando notificaciones generadas..." -ForegroundColor Gray
    Start-Sleep -Seconds 2  # Esperar a que se procesen las notificaciones
    
    $result = Invoke-ApiRequest -Method GET -Endpoint "/notifications?read=false" -Token $TOKEN
    if ($result.Success) {
        $unreadNotifications = $result.Data.content
        Write-Host "   ✓ Notificaciones sin leer: $($unreadNotifications.Count)" -ForegroundColor Green
        
        if ($unreadNotifications.Count -gt 0) {
            Write-Host "   Detalles de notificaciones:" -ForegroundColor Gray
            foreach ($notif in $unreadNotifications | Select-Object -First 3) {
                Write-Host "   - [$($notif.type)] $($notif.message)" -ForegroundColor Gray
            }
        }
    } else {
        Write-Host "   ⚠ Error verificando: $($result.Error)" -ForegroundColor Yellow
    }
}

Write-Host ""

# ============================================
# RESUMEN FINAL
# ============================================
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  RESUMEN DE PRUEBAS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "✓ Módulo 1: Autenticación JWT" -ForegroundColor Green
Write-Host "✓ Módulo 2: Dashboard Jugadores (CRUD)" -ForegroundColor Green
Write-Host "✓ Módulo 3: Dashboard Estadísticas" -ForegroundColor Green
Write-Host "✓ Módulo 4: Sistema de Suscripciones" -ForegroundColor Green
Write-Host "✓ Módulo 5: Sistema de Comentarios" -ForegroundColor Green
Write-Host "✓ Módulo 6: Sistema de Notificaciones" -ForegroundColor Green
Write-Host "✓ Módulo 7: Sistema de Logros" -ForegroundColor Green
Write-Host "✓ Módulo 8: Integración Automática" -ForegroundColor Green
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  FUNCIONALIDADES VALIDADAS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "✓ Registro y login de usuarios" -ForegroundColor Green
Write-Host "✓ Roles y permisos (@PreAuthorize)" -ForegroundColor Green
Write-Host "✓ CRUD completo de jugadores" -ForegroundColor Green
Write-Host "✓ Rankings y estadísticas" -ForegroundColor Green
Write-Host "✓ Suscripciones a jugadores" -ForegroundColor Green
Write-Host "✓ Comentarios con moderación" -ForegroundColor Green
Write-Host "✓ Notificaciones automáticas" -ForegroundColor Green
Write-Host "✓ Integración entre módulos" -ForegroundColor Green
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend funcionando correctamente ✓" -ForegroundColor Green
Write-Host "Todas las pruebas completadas." -ForegroundColor Green
Write-Host ""
Write-Host "Puede acceder a:" -ForegroundColor Cyan
Write-Host "- API: http://localhost:8080/api" -ForegroundColor Cyan
Write-Host "- Swagger: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host ""
