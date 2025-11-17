# ============================================
# Script de Inicio Completo - GOATs del Fútbol
# ============================================
# Este script inicia MySQL y el Backend de forma robusta
# Fecha: 16 Noviembre 2025

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  GOATs del Fútbol - Sistema de Inicio  " -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Configuración
$XAMPP_PATH = "C:\xampp"
$MYSQL_BIN = "$XAMPP_PATH\mysql\bin"
$MYSQL_DATA = "$XAMPP_PATH\mysql\data"
$PROJECT_PATH = "c:\xampp\htdocs\proyecto-goats-futbol"
$BACKEND_PATH = "$PROJECT_PATH\goats-api"
$JAVA_HOME = "C:\Program Files\Java\jdk-17"
$BACKEND_JAR = "$BACKEND_PATH\target\goats-api-0.0.1-SNAPSHOT.jar"

# ============================================
# PASO 1: Verificar y Reparar MySQL
# ============================================
Write-Host "1. Verificando MySQL..." -ForegroundColor Yellow

# Detener cualquier instancia de MySQL
$mysqlProcesses = Get-Process -Name "mysqld" -ErrorAction SilentlyContinue
if ($mysqlProcesses) {
    Write-Host "   Deteniendo procesos MySQL existentes..." -ForegroundColor Gray
    Stop-Process -Name "mysqld" -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 3
}

# Verificar archivos Aria
Write-Host "   Verificando archivos Aria..." -ForegroundColor Gray
$ariaControl = "$MYSQL_DATA\aria_log_control"
$ariaLog = "$MYSQL_DATA\aria_log.00000001"

if (-not (Test-Path $ariaControl) -or (Get-Item $ariaControl).Length -eq 0) {
    Write-Host "   Recreando archivos Aria..." -ForegroundColor Gray
    if (Test-Path $ariaControl) { Remove-Item $ariaControl -Force }
    if (Test-Path $ariaLog) { Remove-Item $ariaLog -Force }
    
    # Crear archivos vacíos
    New-Item -ItemType File -Path $ariaControl -Force | Out-Null
    New-Item -ItemType File -Path $ariaLog -Force | Out-Null
}

# Reparar tablas si es necesario
$needsRepair = $false
$errorLog = "$MYSQL_DATA\mysql_error.log"
if (Test-Path $errorLog) {
    $lastErrors = Get-Content $errorLog -Tail 10 -ErrorAction SilentlyContinue
    if ($lastErrors -match "crashed|marked as crashed") {
        $needsRepair = $true
    }
}

if ($needsRepair) {
    Write-Host "   Reparando tablas MySQL..." -ForegroundColor Yellow
    Get-ChildItem "$MYSQL_DATA\mysql\*.MAI" | ForEach-Object {
        & "$MYSQL_BIN\aria_chk.exe" -r $_.FullName 2>&1 | Out-Null
    }
    Write-Host "   Tablas reparadas." -ForegroundColor Green
}

# ============================================
# PASO 2: Iniciar MySQL
# ============================================
Write-Host ""
Write-Host "2. Iniciando MySQL..." -ForegroundColor Yellow

# Iniciar MySQL
$mysqlProcess = Start-Process -FilePath "$MYSQL_BIN\mysqld.exe" `
    -ArgumentList "--defaults-file=$MYSQL_BIN\my.ini" `
    -WindowStyle Hidden `
    -PassThru

Write-Host "   MySQL iniciado (PID: $($mysqlProcess.Id))" -ForegroundColor Gray
Write-Host "   Esperando que MySQL esté listo..." -ForegroundColor Gray

# Esperar a que MySQL esté listo (máximo 30 segundos)
$maxAttempts = 30
$attempt = 0
$mysqlReady = $false

while ($attempt -lt $maxAttempts -and -not $mysqlReady) {
    Start-Sleep -Seconds 1
    $attempt++
    
    # Intentar conectarse
    $testResult = & "$MYSQL_BIN\mysql.exe" -u root -e "SELECT 1;" 2>&1
    if ($LASTEXITCODE -eq 0) {
        $mysqlReady = $true
    }
    
    # Mostrar progreso cada 5 segundos
    if ($attempt % 5 -eq 0) {
        Write-Host "   Esperando... ($attempt segundos)" -ForegroundColor Gray
    }
}

if ($mysqlReady) {
    Write-Host "   ✓ MySQL listo y funcionando" -ForegroundColor Green
    
    # Verificar base de datos
    $dbCheck = & "$MYSQL_BIN\mysql.exe" -u root -e "USE goats_futbol; SELECT COUNT(*) FROM comments;" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   ✓ Base de datos 'goats_futbol' accesible" -ForegroundColor Green
    } else {
        Write-Host "   ⚠ Advertencia: No se pudo acceder a 'goats_futbol'" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ✗ ERROR: MySQL no respondió después de 30 segundos" -ForegroundColor Red
    Write-Host "   Revise el log: $errorLog" -ForegroundColor Red
    Read-Host "Presione Enter para salir"
    exit 1
}

# ============================================
# PASO 3: Verificar Apache
# ============================================
Write-Host ""
Write-Host "3. Verificando Apache..." -ForegroundColor Yellow

$apacheProcesses = Get-Process -Name "httpd" -ErrorAction SilentlyContinue
if ($apacheProcesses) {
    Write-Host "   ✓ Apache está corriendo" -ForegroundColor Green
} else {
    Write-Host "   ⚠ Apache no está corriendo" -ForegroundColor Yellow
    Write-Host "   Inicie Apache desde el Panel de Control de XAMPP" -ForegroundColor Yellow
}

# ============================================
# PASO 4: Verificar Backend
# ============================================
Write-Host ""
Write-Host "4. Verificando Backend..." -ForegroundColor Yellow

# Verificar que existe el JAR
if (-not (Test-Path $BACKEND_JAR)) {
    Write-Host "   ⚠ JAR no encontrado. Recompilando..." -ForegroundColor Yellow
    
    Set-Location $BACKEND_PATH
    & "$BACKEND_PATH\mvnw.cmd" clean package -DskipTests
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "   ✗ ERROR: No se pudo compilar el backend" -ForegroundColor Red
        Read-Host "Presione Enter para salir"
        exit 1
    }
    
    Write-Host "   ✓ Backend compilado exitosamente" -ForegroundColor Green
}

# Detener cualquier instancia de Java
$javaProcesses = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($javaProcesses) {
    Write-Host "   Deteniendo backend anterior..." -ForegroundColor Gray
    Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}

# ============================================
# PASO 5: Iniciar Backend
# ============================================
Write-Host ""
Write-Host "5. Iniciando Backend..." -ForegroundColor Yellow

# Configurar JAVA_HOME
$env:JAVA_HOME = $JAVA_HOME

# Iniciar el backend
Set-Location $BACKEND_PATH
$backendProcess = Start-Process -FilePath "$JAVA_HOME\bin\java.exe" `
    -ArgumentList "-jar", "$BACKEND_JAR" `
    -WindowStyle Hidden `
    -PassThru

Write-Host "   Backend iniciado (PID: $($backendProcess.Id))" -ForegroundColor Gray
Write-Host "   Esperando que el backend esté listo..." -ForegroundColor Gray

# Esperar a que el backend esté listo (máximo 60 segundos)
$maxAttempts = 60
$attempt = 0
$backendReady = $false

while ($attempt -lt $maxAttempts -and -not $backendReady) {
    Start-Sleep -Seconds 1
    $attempt++
    
    # Intentar conectarse al endpoint
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/stats/player/1" -Method Get -TimeoutSec 2 -ErrorAction SilentlyContinue
        if ($response) {
            $backendReady = $true
        }
    } catch {
        # Silenciar errores mientras el backend inicia
    }
    
    # Mostrar progreso cada 5 segundos
    if ($attempt % 5 -eq 0) {
        Write-Host "   Esperando... ($attempt segundos)" -ForegroundColor Gray
    }
}

if ($backendReady) {
    Write-Host "   ✓ Backend listo y respondiendo" -ForegroundColor Green
} else {
    Write-Host "   ⚠ Backend no respondió después de 60 segundos" -ForegroundColor Yellow
    Write-Host "   El backend puede estar aún iniciando..." -ForegroundColor Yellow
}

# ============================================
# RESUMEN FINAL
# ============================================
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  RESUMEN DEL SISTEMA" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Estado MySQL
$mysqlStatus = Get-Process -Name "mysqld" -ErrorAction SilentlyContinue
if ($mysqlStatus) {
    Write-Host "MySQL:     ✓ CORRIENDO (PID: $($mysqlStatus.Id))" -ForegroundColor Green
} else {
    Write-Host "MySQL:     ✗ DETENIDO" -ForegroundColor Red
}

# Estado Apache
$apacheStatus = Get-Process -Name "httpd" -ErrorAction SilentlyContinue
if ($apacheStatus) {
    Write-Host "Apache:    ✓ CORRIENDO ($($apacheStatus.Count) procesos)" -ForegroundColor Green
} else {
    Write-Host "Apache:    ✗ DETENIDO" -ForegroundColor Red
}

# Estado Backend
$backendStatus = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($backendStatus) {
    Write-Host "Backend:   ✓ CORRIENDO (PID: $($backendStatus.Id))" -ForegroundColor Green
} else {
    Write-Host "Backend:   ✗ DETENIDO" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  URLs DEL SISTEMA" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Frontend:  http://localhost/proyecto-goats-futbol" -ForegroundColor Cyan
Write-Host "Backend:   http://localhost:8080/api" -ForegroundColor Cyan
Write-Host "Swagger:   http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host "Admin:     http://localhost/proyecto-goats-futbol/pages/admin.html" -ForegroundColor Cyan
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Preguntar si desea abrir el navegador
$openBrowser = Read-Host "¿Desea abrir el frontend en el navegador? (S/N)"
if ($openBrowser -eq "S" -or $openBrowser -eq "s") {
    Start-Process "http://localhost/proyecto-goats-futbol"
}

Write-Host ""
Write-Host "Sistema iniciado. Presione Ctrl+C para detener todos los servicios." -ForegroundColor Yellow
Write-Host ""

# Mantener el script corriendo
try {
    while ($true) {
        Start-Sleep -Seconds 10
        
        # Verificar que todos los servicios sigan corriendo
        $mysqlRunning = Get-Process -Name "mysqld" -ErrorAction SilentlyContinue
        $backendRunning = Get-Process -Name "java" -ErrorAction SilentlyContinue
        
        if (-not $mysqlRunning) {
            Write-Host "⚠ ALERTA: MySQL se detuvo inesperadamente" -ForegroundColor Red
        }
        
        if (-not $backendRunning) {
            Write-Host "⚠ ALERTA: Backend se detuvo inesperadamente" -ForegroundColor Red
        }
    }
} finally {
    # Cleanup al presionar Ctrl+C
    Write-Host ""
    Write-Host "Deteniendo servicios..." -ForegroundColor Yellow
    Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue
    Stop-Process -Name "mysqld" -Force -ErrorAction SilentlyContinue
    Write-Host "Servicios detenidos." -ForegroundColor Green
}
