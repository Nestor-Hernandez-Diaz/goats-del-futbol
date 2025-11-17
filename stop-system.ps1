# Script para Detener Todos los Servicios
# GOATs del Fútbol

Write-Host "Deteniendo servicios del sistema GOATs..." -ForegroundColor Yellow

# Detener Backend (Java)
$javaProcesses = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($javaProcesses) {
    Write-Host "Deteniendo Backend..." -ForegroundColor Gray
    Stop-Process -Name "java" -Force -ErrorAction SilentlyContinue
    Write-Host "✓ Backend detenido" -ForegroundColor Green
} else {
    Write-Host "Backend no estaba corriendo" -ForegroundColor Gray
}

# Detener MySQL
$mysqlProcesses = Get-Process -Name "mysqld" -ErrorAction SilentlyContinue
if ($mysqlProcesses) {
    Write-Host "Deteniendo MySQL..." -ForegroundColor Gray
    Stop-Process -Name "mysqld" -Force -ErrorAction SilentlyContinue
    Write-Host "✓ MySQL detenido" -ForegroundColor Green
} else {
    Write-Host "MySQL no estaba corriendo" -ForegroundColor Gray
}

Write-Host ""
Write-Host "Todos los servicios detenidos." -ForegroundColor Green
Write-Host ""
Read-Host "Presione Enter para salir"
