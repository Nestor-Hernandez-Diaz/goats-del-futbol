# Script de Configuración del Backend GOATs API
# Autor: Sistema de Configuración Automática
# Fecha: 2025-11-15

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  GOATs API - Configuración Backend" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Paso 1: Configurar Java 17
Write-Host "[1/6] Configurando Java 17..." -ForegroundColor Yellow
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

$javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.ToString() }
Write-Host "✓ Java configurado: $javaVersion" -ForegroundColor Green
Write-Host ""

# Paso 2: Verificar MySQL
Write-Host "[2/6] Verificando MySQL..." -ForegroundColor Yellow
$mysqlRunning = netstat -an | Select-String "3306" | Select-Object -First 1
if ($mysqlRunning) {
    Write-Host "✓ MySQL está corriendo en puerto 3306" -ForegroundColor Green
} else {
    Write-Host "✗ MySQL NO está corriendo. Por favor inicia MySQL desde XAMPP Control Panel" -ForegroundColor Red
    Write-Host "  1. Abre XAMPP Control Panel" -ForegroundColor Yellow
    Write-Host "  2. Haz clic en 'Start' junto a MySQL" -ForegroundColor Yellow
    Write-Host "  3. Vuelve a ejecutar este script" -ForegroundColor Yellow
    exit 1
}
Write-Host ""

# Paso 3: Limpiar datos duplicados
Write-Host "[3/6] Limpiando datos duplicados en la base de datos..." -ForegroundColor Yellow
$cleanupSQL = @"
USE goats_futbol;
DELETE p1 FROM players p1 
INNER JOIN players p2 
WHERE p1.id > p2.id 
AND p1.name = p2.name 
AND IFNULL(p1.nickname,'') = IFNULL(p2.nickname,'') 
AND IFNULL(p1.country,'') = IFNULL(p2.country,'');
"@

$cleanupSQL | C:\xampp\mysql\bin\mysql.exe -u root 2>&1 | Out-Null
$count = C:\xampp\mysql\bin\mysql.exe -u root goats_futbol -e "SELECT COUNT(*) as total FROM players;" --batch --skip-column-names
Write-Host "✓ Limpieza completada. Total de jugadores: $count" -ForegroundColor Green
Write-Host ""

# Paso 4: Verificar estructura de la base de datos
Write-Host "[4/6] Verificando estructura de la base de datos..." -ForegroundColor Yellow
$tables = C:\xampp\mysql\bin\mysql.exe -u root goats_futbol -e "SHOW TABLES;" --batch --skip-column-names
Write-Host "✓ Tablas encontradas:" -ForegroundColor Green
foreach ($table in $tables) {
    Write-Host "  - $table" -ForegroundColor Gray
}
Write-Host ""

# Paso 5: Configurar variables de entorno para la aplicación
Write-Host "[5/6] Configurando variables de entorno..." -ForegroundColor Yellow
$env:DB_URL = "jdbc:mysql://localhost:3306/goats_futbol?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USER = "root"
$env:DB_PASS = ""
Write-Host "✓ Variables configuradas:" -ForegroundColor Green
Write-Host "  DB_URL: $env:DB_URL" -ForegroundColor Gray
Write-Host "  DB_USER: $env:DB_USER" -ForegroundColor Gray
Write-Host ""

# Paso 6: Información de siguiente paso
Write-Host "[6/6] Configuración completada" -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Siguiente paso: Iniciar la aplicación" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Opción 1: Iniciar con Maven Wrapper" -ForegroundColor Yellow
Write-Host "  cd c:\xampp\htdocs\proyecto-goats-futbol\goats-api" -ForegroundColor White
Write-Host "  .\mvnw.cmd spring-boot:run" -ForegroundColor White
Write-Host ""
Write-Host "Opción 2: Compilar y ejecutar JAR" -ForegroundColor Yellow
Write-Host "  cd c:\xampp\htdocs\proyecto-goats-futbol\goats-api" -ForegroundColor White
Write-Host "  .\mvnw.cmd clean package -DskipTests" -ForegroundColor White
Write-Host "  java -jar target\goats-api-0.0.1-SNAPSHOT.jar" -ForegroundColor White
Write-Host ""
Write-Host "Una vez iniciado, accede a:" -ForegroundColor Yellow
Write-Host "  - Swagger UI: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host "  - API REST:   http://localhost:8080/api/players" -ForegroundColor Cyan
Write-Host ""
Write-Host "¿Deseas iniciar la aplicación ahora? (S/N)" -ForegroundColor Yellow
$respuesta = Read-Host

if ($respuesta -eq 'S' -or $respuesta -eq 's') {
    Write-Host ""
    Write-Host "Iniciando aplicación..." -ForegroundColor Green
    Set-Location "c:\xampp\htdocs\proyecto-goats-futbol\goats-api"
    .\mvnw.cmd spring-boot:run
} else {
    Write-Host ""
    Write-Host "Configuración lista. Puedes iniciar la aplicación cuando quieras." -ForegroundColor Green
}
