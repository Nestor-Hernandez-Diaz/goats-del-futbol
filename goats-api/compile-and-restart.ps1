# compile-and-restart.ps1
# Script para compilar backend y reiniciar servidor

Write-Host "======================================"
Write-Host "  COMPILACIÓN Y REINICIO DEL BACKEND"
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# 1. Detener procesos Java del backend
Write-Host "[1/5] Deteniendo backend..." -ForegroundColor Yellow
Get-Process -Name "java" -ErrorAction SilentlyContinue | Where-Object {
    $_.CommandLine -like "*goats-api*" -or $_.MainWindowTitle -like "*goats*"
} | Stop-Process -Force
Start-Sleep -Seconds 2
Write-Host "✓ Backend detenido" -ForegroundColor Green
Write-Host ""

# 2. Ir al directorio del proyecto
Write-Host "[2/5] Navegando a directorio del proyecto..." -ForegroundColor Yellow
Set-Location "c:\xampp\htdocs\proyecto-goats-futbol\goats-api"
Write-Host "✓ Directorio: $(Get-Location)" -ForegroundColor Green
Write-Host ""

# 3. Compilar proyecto con Maven
Write-Host "[3/5] Compilando proyecto Maven..." -ForegroundColor Yellow
Write-Host "Esto puede tomar 1-2 minutos..." -ForegroundColor Gray

# Usar cmd para ejecutar mvnw.cmd
$compileProcess = Start-Process cmd -ArgumentList "/c", "mvnw.cmd clean package -DskipTests" -PassThru -NoNewWindow -Wait

if ($compileProcess.ExitCode -eq 0) {
    Write-Host "✓ Compilación exitosa" -ForegroundColor Green
} else {
    Write-Host "✗ Error en compilación (código: $($compileProcess.ExitCode))" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 4. Verificar que el JAR fue creado
Write-Host "[4/5] Verificando archivo JAR..." -ForegroundColor Yellow
$jarPath = "target\goats-api-0.0.1-SNAPSHOT.jar"

if (Test-Path $jarPath) {
    $jarSize = (Get-Item $jarPath).Length / 1MB
    Write-Host "✓ JAR encontrado: $jarPath ($([Math]::Round($jarSize, 2)) MB)" -ForegroundColor Green
} else {
    Write-Host "✗ JAR no encontrado" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 5. Iniciar servidor
Write-Host "[5/5] Iniciando servidor en puerto 8080..." -ForegroundColor Yellow
Start-Process java -ArgumentList "-jar", $jarPath -NoNewWindow
Start-Sleep -Seconds 5

# Verificar que el servidor está corriendo
Write-Host "Esperando que el servidor inicie..." -ForegroundColor Gray
Start-Sleep -Seconds 5

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -TimeoutSec 5 -ErrorAction Stop
    Write-Host "✓ Servidor corriendo en http://localhost:8080" -ForegroundColor Green
} catch {
    Write-Host "⚠ Servidor iniciando... (puede tomar unos segundos más)" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "======================================"
Write-Host "  ¡PROCESO COMPLETADO!" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend compilado y reiniciado correctamente" -ForegroundColor White
Write-Host "URL: http://localhost:8080/api" -ForegroundColor Cyan
Write-Host ""
