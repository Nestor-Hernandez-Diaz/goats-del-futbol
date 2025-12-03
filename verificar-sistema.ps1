# ========================================
# Script de Verificación - GOATs del Fútbol
# ========================================
# Este script verifica que todos los componentes estén corriendo correctamente
# y realiza pruebas de conexión API

Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║    GOATs del Fútbol - Verificación de Componentes    ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Variables
$mysqlProcess = "mysqld"
$javaProcess = "java"
$apiUrl = "http://localhost:8080/api/players"
$apiUrlMessi = "http://localhost:8080/api/players/1"

# ========================================
# 1. VERIFICAR MYSQL
# ========================================
Write-Host "[1/5] " -NoNewline -ForegroundColor Yellow
Write-Host "Verificando MySQL..." -NoNewline

try {
    $mysql = Get-Process -Name $mysqlProcess -ErrorAction Stop
    Write-Host " ✅ CORRIENDO" -ForegroundColor Green
    Write-Host "      PID: $($mysql.Id), CPU: $($mysql.CPU.ToString('0.00'))s" -ForegroundColor Gray
} catch {
    Write-Host " ❌ NO CORRIENDO" -ForegroundColor Red
    Write-Host "      Inicia XAMPP Control Panel → MySQL" -ForegroundColor Gray
    exit 1
}

# ========================================
# 2. VERIFICAR JAVA/SPRING BOOT
# ========================================
Write-Host "`n[2/5] " -NoNewline -ForegroundColor Yellow
Write-Host "Verificando Backend Spring Boot..." -NoNewline

try {
    $java = Get-Process -Name $javaProcess -ErrorAction SilentlyContinue
    if ($java) {
        Write-Host " ✅ PROCESO DETECTADO" -ForegroundColor Green
        Write-Host "      Procesos Java activos: $($java.Count)" -ForegroundColor Gray
    } else {
        Write-Host " ⚠️ NO DETECTADO" -ForegroundColor Yellow
        Write-Host "      Verifica con prueba de API..." -ForegroundColor Gray
    }
} catch {
    Write-Host " ⚠️ NO DETECTADO" -ForegroundColor Yellow
}

# ========================================
# 3. PROBAR API - GET /api/players
# ========================================
Write-Host "`n[3/5] " -NoNewline -ForegroundColor Yellow
Write-Host "Probando GET /api/players..." -NoNewline

try {
    $response = Invoke-WebRequest -Uri $apiUrl -Method GET -UseBasicParsing -TimeoutSec 5
    
    if ($response.StatusCode -eq 200) {
        Write-Host " ✅ OK (200)" -ForegroundColor Green
        
        $data = $response.Content | ConvertFrom-Json
        $players = $data.content
        
        Write-Host "      Total jugadores: $($players.Count)" -ForegroundColor Gray
        
        foreach ($player in $players) {
            Write-Host "        • ID $($player.id): $($player.name) ($($player.nickname))" -ForegroundColor Cyan
        }
    } else {
        Write-Host " ⚠️ Status: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    Write-Host " ❌ ERROR" -ForegroundColor Red
    Write-Host "      $($_.Exception.Message)" -ForegroundColor Gray
    Write-Host "      ¿Está corriendo el backend en puerto 8080?" -ForegroundColor Gray
    exit 1
}

# ========================================
# 4. PROBAR API - GET /api/players/1 (MESSI)
# ========================================
Write-Host "`n[4/5] " -NoNewline -ForegroundColor Yellow
Write-Host "Probando GET /api/players/1 (Messi)..." -NoNewline

try {
    $response = Invoke-WebRequest -Uri $apiUrlMessi -Method GET -UseBasicParsing -TimeoutSec 5
    
    if ($response.StatusCode -eq 200) {
        Write-Host " ✅ OK (200)" -ForegroundColor Green
        
        $messi = $response.Content | ConvertFrom-Json
        
        Write-Host "      Nombre: $($messi.name)" -ForegroundColor Gray
        Write-Host "      Apodo: $($messi.nickname)" -ForegroundColor Gray
        Write-Host "      País: $($messi.country)" -ForegroundColor Gray
        Write-Host "      Posición: $($messi.position)" -ForegroundColor Gray
        
        # Verificar videos
        if ($messi.videos) {
            $videos = $messi.videos | ConvertFrom-Json
            Write-Host "      Videos: $($videos.Count) encontrados" -ForegroundColor Gray
            
            foreach ($video in $videos) {
                Write-Host "        📹 $($video.title)" -ForegroundColor Cyan
                Write-Host "           $($video.url)" -ForegroundColor DarkGray
            }
        } else {
            Write-Host "      Videos: No cargados (ejecuta SQL actualizado)" -ForegroundColor Yellow
        }
    }
} catch {
    Write-Host " ❌ ERROR" -ForegroundColor Red
    Write-Host "      $($_.Exception.Message)" -ForegroundColor Gray
    exit 1
}

# ========================================
# 5. VERIFICAR OTROS JUGADORES
# ========================================
Write-Host "`n[5/5] " -NoNewline -ForegroundColor Yellow
Write-Host "Verificando Ronaldo y Neymar..." -NoNewline

$allOk = $true

# Ronaldo (ID=2)
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/players/2" -Method GET -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        $ronaldo = $response.Content | ConvertFrom-Json
        Write-Host " ✅ OK" -ForegroundColor Green
        Write-Host "      Ronaldo: $($ronaldo.name) ($($ronaldo.nickname))" -ForegroundColor Gray
    }
} catch {
    Write-Host " ❌ Ronaldo ERROR" -ForegroundColor Red
    $allOk = $false
}

# Neymar (ID=3)
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/players/3" -Method GET -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        $neymar = $response.Content | ConvertFrom-Json
        Write-Host "      Neymar: $($neymar.name) ($($neymar.nickname))" -ForegroundColor Gray
    }
} catch {
    Write-Host " ❌ Neymar ERROR" -ForegroundColor Red
    $allOk = $false
}

# ========================================
# RESUMEN FINAL
# ========================================
Write-Host "`n" -NoNewline
Write-Host "╔═══════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║                  RESUMEN DE PRUEBAS                   ║" -ForegroundColor Cyan
Write-Host "╚═══════════════════════════════════════════════════════╝" -ForegroundColor Cyan

if ($allOk) {
    Write-Host "`n✅ TODOS LOS COMPONENTES ESTÁN FUNCIONANDO CORRECTAMENTE" -ForegroundColor Green
    Write-Host ""
    Write-Host "Puedes proceder con la demostración:" -ForegroundColor White
    Write-Host "  1. phpMyAdmin: http://localhost/phpmyadmin" -ForegroundColor Cyan
    Write-Host "  2. Backend API: http://localhost:8080/api/players" -ForegroundColor Cyan
    Write-Host "  3. Frontend: http://127.0.0.1:5500/index.html" -ForegroundColor Cyan
    Write-Host "  4. Admin Panel: http://127.0.0.1:5500/pages/admin-players.html" -ForegroundColor Cyan
    Write-Host ""
} else {
    Write-Host "`n⚠️ ALGUNOS COMPONENTES TIENEN PROBLEMAS" -ForegroundColor Yellow
    Write-Host "Revisa los mensajes de error arriba." -ForegroundColor Gray
}

Write-Host "Presiona cualquier tecla para continuar..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
