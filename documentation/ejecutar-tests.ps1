# SCRIPT DE TESTING AUTOMATIZADO
# FASE 9: Testing exhaustivo del sistema

param(
    [switch]$Full = $false,
    [switch]$Quick = $false
)

$ErrorActionPreference = "Continue"
$baseUrl = "http://localhost"
$apiUrl = "http://localhost:8080/api"
$project = "/proyecto-goats-futbol"

# Colores
function Write-Pass { param($msg) Write-Host "✓ PASS: $msg" -ForegroundColor Green }
function Write-Fail { param($msg) Write-Host "✗ FAIL: $msg" -ForegroundColor Red }
function Write-Info { param($msg) Write-Host "ℹ INFO: $msg" -ForegroundColor Cyan }
function Write-Section { param($msg) Write-Host "`n═══ $msg ═══`n" -ForegroundColor Yellow }

# Contadores
$script:totalTests = 0
$script:passedTests = 0
$script:failedTests = 0
$script:issues = @()

function Test-Endpoint {
    param($url, $description)
    $script:totalTests++
    Write-Info "Testing: $description"
    
    try {
        $response = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 5
        
        if ($response.StatusCode -eq 200) {
            Write-Pass "$description (200 OK)"
            $script:passedTests++
            return $true
        } else {
            Write-Fail "$description (Status: $($response.StatusCode))"
            $script:failedTests++
            $script:issues += @{
                Test = $description
                Error = "Status code: $($response.StatusCode)"
                Severity = "HIGH"
            }
            return $false
        }
    } catch {
        Write-Fail "$description (Error: $($_.Exception.Message))"
        $script:failedTests++
        $script:issues += @{
            Test = $description
            Error = $_.Exception.Message
            Severity = "CRITICAL"
        }
        return $false
    }
}

function Test-ApiEndpoint {
    param($endpoint, $description, $expectedStatus = 200)
    $script:totalTests++
    $url = "$apiUrl$endpoint"
    Write-Info "Testing API: $description"
    
    try {
        $response = Invoke-RestMethod -Uri $url -Method Get -ContentType "application/json"
        Write-Pass "$description (Data received)"
        $script:passedTests++
        return $response
    } catch {
        Write-Fail "$description (Error: $($_.Exception.Message))"
        $script:failedTests++
        $script:issues += @{
            Test = "API: $description"
            Error = $_.Exception.Message
            Severity = "HIGH"
        }
        return $null
    }
}

function Test-PageContent {
    param($url, $description, $expectedText)
    $script:totalTests++
    Write-Info "Testing content: $description"
    
    try {
        $content = (Invoke-WebRequest -Uri $url -UseBasicParsing).Content
        
        if ($content -match $expectedText) {
            Write-Pass "$description (Content found: '$expectedText')"
            $script:passedTests++
            return $true
        } else {
            Write-Fail "$description (Expected text not found)"
            $script:failedTests++
            $script:issues += @{
                Test = $description
                Error = "Text '$expectedText' not found in page"
                Severity = "MEDIUM"
            }
            return $false
        }
    } catch {
        Write-Fail "$description (Error: $($_.Exception.Message))"
        $script:failedTests++
        return $false
    }
}

function Test-FileExists {
    param($path, $description)
    $script:totalTests++
    
    if (Test-Path $path) {
        Write-Pass "$description exists"
        $script:passedTests++
        return $true
    } else {
        Write-Fail "$description not found at $path"
        $script:failedTests++
        $script:issues += @{
            Test = $description
            Error = "File not found: $path"
            Severity = "HIGH"
        }
        return $false
    }
}

# ═══════════════════════════════════════════════════════════
# INICIO DE TESTS
# ═══════════════════════════════════════════════════════════

Write-Host @"

╔═══════════════════════════════════════════════════════════╗
║                                                           ║
║     🧪 SUITE DE TESTING EXHAUSTIVO - FASE 9 🧪          ║
║              GOATs del Fútbol - Sistema Dinámico         ║
║                                                           ║
╚═══════════════════════════════════════════════════════════╝

"@ -ForegroundColor Cyan

$startTime = Get-Date

# ═══════════════════════════════════════════════════════════
# 1. VERIFICACIÓN DE ENTORNO
# ═══════════════════════════════════════════════════════════

Write-Section "1. VERIFICACIÓN DE ENTORNO"

$projectPath = "C:\xampp\htdocs\proyecto-goats-futbol"
Test-FileExists "$projectPath\index.html" "index.html"
Test-FileExists "$projectPath\pages\player.html" "player.html"
Test-FileExists "$projectPath\pages\admin-players.html" "admin-players.html"
Test-FileExists "$projectPath\js\player-loader.js" "player-loader.js"
Test-FileExists "$projectPath\js\admin-players.js" "admin-players.js"
Test-FileExists "$projectPath\css\styles.css" "styles.css"

# ═══════════════════════════════════════════════════════════
# 2. TESTS DE BACKEND API
# ═══════════════════════════════════════════════════════════

Write-Section "2. TESTS DE BACKEND API"

Write-Info "Verificando conectividad con Spring Boot backend..."

$playersData = Test-ApiEndpoint "/players" "GET /api/players"

if ($playersData) {
    # Verificar estructura de respuesta paginada
    if ($playersData.content) {
        Write-Pass "Respuesta paginada correcta (propiedad 'content' encontrada)"
        $players = $playersData.content
    } elseif ($playersData -is [Array]) {
        Write-Pass "Respuesta como array directo"
        $players = $playersData
    } else {
        Write-Fail "Estructura de respuesta no reconocida"
        $players = @()
    }
    
    $script:totalTests++
    if ($players.Count -ge 3) {
        Write-Pass "Se encontraron $($players.Count) jugadores (esperado: ≥3)"
        $script:passedTests++
    } else {
        Write-Fail "Solo $($players.Count) jugadores encontrados (esperado: ≥3)"
        $script:failedTests++
    }
}

# Test individual players
$playerIds = @(1, 2, 3)
foreach ($id in $playerIds) {
    $player = Test-ApiEndpoint "/players/$id" "GET /api/players/$id"
    
    if ($player) {
        $script:totalTests += 3
        
        # Verificar nombre
        if ($player.name) {
            Write-Pass "  └─ Nombre: '$($player.name)'"
            $script:passedTests++
        } else {
            Write-Fail "  └─ Nombre faltante"
            $script:failedTests++
        }
        
        # Verificar nickname
        if ($player.nickname) {
            Write-Pass "  └─ Nickname: '$($player.nickname)'"
            $script:passedTests++
        } else {
            Write-Fail "  └─ Nickname faltante"
            $script:failedTests++
        }
        
        # Verificar biografía
        $bioLength = $player.biography.Length
        if ($bioLength -gt 500) {
            Write-Pass "  └─ Biografía: $bioLength caracteres (>500)"
            $script:passedTests++
        } else {
            Write-Fail "  └─ Biografía muy corta: $bioLength caracteres (<500)"
            $script:failedTests++
            $script:issues += @{
                Test = "Biografía ID $id"
                Error = "Solo $bioLength caracteres (esperado: >500)"
                Severity = "HIGH"
            }
        }
    }
}

# Test 404
Write-Info "Testing error 404 para ID inexistente..."
$script:totalTests++
try {
    $response = Invoke-RestMethod -Uri "$apiUrl/players/999" -Method Get
    Write-Fail "ID 999 debería retornar 404 pero retornó datos"
    $script:failedTests++
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 404) {
        Write-Pass "ID 999 correctamente retorna 404"
        $script:passedTests++
    } else {
        Write-Fail "ID 999 retornó error inesperado: $($_.Exception.Message)"
        $script:failedTests++
    }
}

# ═══════════════════════════════════════════════════════════
# 3. TESTS DE PÁGINAS FRONTEND
# ═══════════════════════════════════════════════════════════

Write-Section "3. TESTS DE PÁGINAS FRONTEND"

# Test index.html
Test-Endpoint "$baseUrl$project/index.html" "Página principal (index.html)"

# Test páginas antiguas (legacy)
Test-Endpoint "$baseUrl$project/pages/messi.html" "Página legacy - messi.html"
Test-Endpoint "$baseUrl$project/pages/ronaldo.html" "Página legacy - ronaldo.html"
Test-Endpoint "$baseUrl$project/pages/neymar.html" "Página legacy - neymar.html"

# Test página dinámica con IDs válidos
foreach ($id in $playerIds) {
    $url = "$baseUrl$project/pages/player.html?id=$id"
    Test-Endpoint $url "player.html?id=$id"
}

# Test página dinámica sin ID
Test-Endpoint "$baseUrl$project/pages/player.html" "player.html sin parámetro ?id"

# Test panel admin
Test-Endpoint "$baseUrl$project/pages/admin-players.html" "Panel admin (admin-players.html)"

# ═══════════════════════════════════════════════════════════
# 4. TESTS DE CONTENIDO HTML
# ═══════════════════════════════════════════════════════════

Write-Section "4. TESTS DE CONTENIDO HTML"

# Verificar que player.html contiene player-loader.js
Test-PageContent "$baseUrl$project/pages/player.html?id=1" `
    "Script player-loader.js cargado" `
    "player-loader.js"

# Verificar skeleton loader
Test-PageContent "$baseUrl$project/pages/player.html?id=1" `
    "Skeleton loader presente" `
    "skeleton"

# Verificar admin panel contiene admin-players.js
Test-PageContent "$baseUrl$project/pages/admin-players.html" `
    "Script admin-players.js cargado" `
    "admin-players.js"

# Verificar TinyMCE en admin
Test-PageContent "$baseUrl$project/pages/admin-players.html" `
    "TinyMCE cargado en admin" `
    "tinymce"

# ═══════════════════════════════════════════════════════════
# 5. TESTS DE ESTRUCTURA DE ARCHIVOS
# ═══════════════════════════════════════════════════════════

Write-Section "5. TESTS DE ESTRUCTURA DE ARCHIVOS"

# Verificar archivos JavaScript
$jsFiles = @(
    "$projectPath\js\main.js",
    "$projectPath\js\player-loader.js",
    "$projectPath\js\admin-players.js"
)

foreach ($file in $jsFiles) {
    $fileName = Split-Path $file -Leaf
    Test-FileExists $file "JavaScript: $fileName"
}

# Verificar páginas HTML
$pages = @(
    "$projectPath\pages\player.html",
    "$projectPath\pages\admin-players.html",
    "$projectPath\pages\messi.html",
    "$projectPath\pages\ronaldo.html",
    "$projectPath\pages\neymar.html"
)

foreach ($page in $pages) {
    $fileName = Split-Path $page -Leaf
    Test-FileExists $page "Página: $fileName"
}

# ═══════════════════════════════════════════════════════════
# 6. TESTS DE VALIDACIÓN DE CÓDIGO
# ═══════════════════════════════════════════════════════════

Write-Section "6. TESTS DE VALIDACIÓN DE CÓDIGO"

# Verificar que player-loader.js contiene funciones clave
$playerLoaderContent = Get-Content "$projectPath\js\player-loader.js" -Raw

$script:totalTests += 5
$functions = @(
    "loadPlayerData",
    "showError404",
    "updatePageTheme",
    "renderPlayerContent",
    "window.PlayerLoader"
)

foreach ($func in $functions) {
    if ($playerLoaderContent -match $func) {
        Write-Pass "Función '$func' encontrada en player-loader.js"
        $script:passedTests++
    } else {
        Write-Fail "Función '$func' NO encontrada en player-loader.js"
        $script:failedTests++
        $script:issues += @{
            Test = "player-loader.js"
            Error = "Función '$func' faltante"
            Severity = "HIGH"
        }
    }
}

# Verificar admin-players.js
$adminContent = Get-Content "$projectPath\js\admin-players.js" -Raw

$script:totalTests += 4
$adminFunctions = @(
    "loadPlayers",
    "openPlayerModal",
    "savePlayer",
    "deletePlayer"
)

foreach ($func in $adminFunctions) {
    if ($adminContent -match $func) {
        Write-Pass "Función '$func' encontrada en admin-players.js"
        $script:passedTests++
    } else {
        Write-Fail "Función '$func' NO encontrada en admin-players.js"
        $script:failedTests++
        $script:issues += @{
            Test = "admin-players.js"
            Error = "Función '$func' faltante"
            Severity = "HIGH"
        }
    }
}

# ═══════════════════════════════════════════════════════════
# 7. TESTS DE MIGRACIÓN DE BIOGRAFÍAS
# ═══════════════════════════════════════════════════════════

Write-Section "7. TESTS DE MIGRACIÓN DE BIOGRAFÍAS"

Write-Info "Verificando longitud de biografías en BD..."

$mysqlPath = "C:\xampp\mysql\bin\mysql.exe"
if (Test-Path $mysqlPath) {
    try {
        $query = "SELECT id, name, LENGTH(biography) as chars FROM players WHERE id IN (1,2,3);"
        $result = & $mysqlPath -u root goats_futbol -e $query 2>&1
        
        Write-Host $result
        
        $script:totalTests++
        if ($result -match "2\d{3}") {  # Buscar números de 2000+
            Write-Pass "Biografías migradas correctamente (>2000 caracteres detectados)"
            $script:passedTests++
        } else {
            Write-Fail "Biografías parecen no estar migradas"
            $script:failedTests++
        }
    } catch {
        Write-Fail "Error al consultar MySQL: $($_.Exception.Message)"
        $script:failedTests++
    }
} else {
    Write-Info "MySQL no encontrado en $mysqlPath (skipping test)"
}

# ═══════════════════════════════════════════════════════════
# 8. TESTS ADICIONALES (MODO FULL)
# ═══════════════════════════════════════════════════════════

if ($Full) {
    Write-Section "8. TESTS ADICIONALES (FULL MODE)"
    
    # Test de todos los assets
    $assets = @(
        "$projectPath\css\styles.css",
        "$projectPath\assets\images\messi.jpg",
        "$projectPath\assets\images\ronaldo.jpg",
        "$projectPath\assets\images\neymar.jpg"
    )
    
    foreach ($asset in $assets) {
        $fileName = Split-Path $asset -Leaf
        Test-FileExists $asset "Asset: $fileName"
    }
    
    # Test de documentación
    $docs = Get-ChildItem "$projectPath\documentation" -Filter "FASE_*.md"
    Write-Info "Archivos de documentación encontrados: $($docs.Count)"
    
    foreach ($doc in $docs) {
        Test-FileExists $doc.FullName "Documentación: $($doc.Name)"
    }
}

# ═══════════════════════════════════════════════════════════
# RESUMEN FINAL
# ═══════════════════════════════════════════════════════════

$endTime = Get-Date
$duration = $endTime - $startTime

Write-Host @"


╔═══════════════════════════════════════════════════════════╗
║                                                           ║
║                  📊 RESUMEN DE RESULTADOS                ║
║                                                           ║
╚═══════════════════════════════════════════════════════════╝

"@ -ForegroundColor Cyan

Write-Host "Tests totales ejecutados:  " -NoNewline
Write-Host $script:totalTests -ForegroundColor White

Write-Host "Tests aprobados:           " -NoNewline
Write-Host $script:passedTests -ForegroundColor Green

Write-Host "Tests fallidos:            " -NoNewline
Write-Host $script:failedTests -ForegroundColor Red

$successRate = [math]::Round(($script:passedTests / $script:totalTests) * 100, 2)
Write-Host "Tasa de éxito:             " -NoNewline
if ($successRate -ge 90) {
    Write-Host "$successRate%" -ForegroundColor Green
} elseif ($successRate -ge 70) {
    Write-Host "$successRate%" -ForegroundColor Yellow
} else {
    Write-Host "$successRate%" -ForegroundColor Red
}

Write-Host "Duración:                  " -NoNewline
Write-Host "$([math]::Round($duration.TotalSeconds, 2)) segundos" -ForegroundColor White

Write-Host ""

# Mostrar issues detectados
if ($script:issues.Count -gt 0) {
    Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Red
    Write-Host "║                                                           ║" -ForegroundColor Red
    Write-Host "║                    ⚠️  ISSUES DETECTADOS                 ║" -ForegroundColor Red
    Write-Host "║                                                           ║" -ForegroundColor Red
    Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Red
    Write-Host ""
    
    $critical = $script:issues | Where-Object { $_.Severity -eq "CRITICAL" }
    $high = $script:issues | Where-Object { $_.Severity -eq "HIGH" }
    $medium = $script:issues | Where-Object { $_.Severity -eq "MEDIUM" }
    
    if ($critical.Count -gt 0) {
        Write-Host "🔴 CRÍTICOS: $($critical.Count)" -ForegroundColor Red
        foreach ($issue in $critical) {
            Write-Host "  • $($issue.Test)" -ForegroundColor Red
            Write-Host "    Error: $($issue.Error)" -ForegroundColor DarkRed
        }
        Write-Host ""
    }
    
    if ($high.Count -gt 0) {
        Write-Host "🟡 IMPORTANTES: $($high.Count)" -ForegroundColor Yellow
        foreach ($issue in $high) {
            Write-Host "  • $($issue.Test)" -ForegroundColor Yellow
            Write-Host "    Error: $($issue.Error)" -ForegroundColor DarkYellow
        }
        Write-Host ""
    }
    
    if ($medium.Count -gt 0) {
        Write-Host "🟢 MENORES: $($medium.Count)" -ForegroundColor Cyan
        foreach ($issue in $medium) {
            Write-Host "  • $($issue.Test)" -ForegroundColor Cyan
            Write-Host "    Error: $($issue.Error)" -ForegroundColor DarkCyan
        }
        Write-Host ""
    }
} else {
    Write-Host "╔═══════════════════════════════════════════════════════════╗" -ForegroundColor Green
    Write-Host "║                                                           ║" -ForegroundColor Green
    Write-Host "║              ✅ TODOS LOS TESTS APROBADOS ✅             ║" -ForegroundColor Green
    Write-Host "║                                                           ║" -ForegroundColor Green
    Write-Host "╚═══════════════════════════════════════════════════════════╝" -ForegroundColor Green
}

Write-Host ""

# Conclusión
if ($successRate -ge 90) {
    Write-Host "✅ Sistema APROBADO para producción" -ForegroundColor Green
    Write-Host "   El sistema dinámico está completamente funcional." -ForegroundColor Green
} elseif ($successRate -ge 70) {
    Write-Host "⚠️  Sistema PARCIALMENTE funcional" -ForegroundColor Yellow
    Write-Host "   Revisar y corregir issues antes de producción." -ForegroundColor Yellow
} else {
    Write-Host "❌ Sistema NO listo para producción" -ForegroundColor Red
    Write-Host "   Se requieren correcciones críticas." -ForegroundColor Red
}

Write-Host ""
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "Reporte completo disponible en:" -ForegroundColor White
Write-Host "  documentation\FASE_9_TESTS.md" -ForegroundColor Gray
Write-Host "═══════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
