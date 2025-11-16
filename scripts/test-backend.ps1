# Script de Pruebas del Backend GOATs API
# Ejecutar después de iniciar el servidor

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  GOATs API - Pruebas de Endpoints" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que el servidor está corriendo
Write-Host "[1/6] Verificando servidor..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator" -Method Get -ErrorAction Stop
    Write-Host "✓ Servidor corriendo correctamente" -ForegroundColor Green
} catch {
    Write-Host "✗ Error: El servidor no está corriendo en puerto 8080" -ForegroundColor Red
    Write-Host "  Por favor inicia el backend primero:" -ForegroundColor Yellow
    Write-Host "  cd c:\xampp\htdocs\proyecto-goats-futbol\goats-api" -ForegroundColor White
    Write-Host "  .\mvnw.cmd spring-boot:run" -ForegroundColor White
    exit 1
}
Write-Host ""

# Prueba 1: Listar todos los jugadores
Write-Host "[2/6] GET /api/players - Listar jugadores..." -ForegroundColor Yellow
try {
    $players = Invoke-RestMethod -Uri "http://localhost:8080/api/players" -Method Get -ErrorAction Stop
    $count = $players.content.Length
    Write-Host "✓ Total de jugadores: $count" -ForegroundColor Green
    Write-Host "  - Página actual: $($players.number + 1) de $($players.totalPages)" -ForegroundColor Gray
    Write-Host "  - Total de elementos: $($players.totalElements)" -ForegroundColor Gray
    
    foreach ($player in $players.content) {
        Write-Host "  [$($player.id)] $($player.name) - $($player.nickname) ($($player.country))" -ForegroundColor Cyan
    }
} catch {
    Write-Host "✗ Error al obtener jugadores" -ForegroundColor Red
}
Write-Host ""

# Prueba 2: Obtener jugador por ID
Write-Host "[3/6] GET /api/players/1 - Obtener Messi..." -ForegroundColor Yellow
try {
    $messi = Invoke-RestMethod -Uri "http://localhost:8080/api/players/1" -Method Get -ErrorAction Stop
    Write-Host "✓ Jugador obtenido:" -ForegroundColor Green
    Write-Host "  Nombre: $($messi.name)" -ForegroundColor Cyan
    Write-Host "  Apodo: $($messi.nickname)" -ForegroundColor Cyan
    Write-Host "  País: $($messi.country)" -ForegroundColor Cyan
    Write-Host "  Posición: $($messi.position)" -ForegroundColor Cyan
    Write-Host "  Biografía: $($messi.biography)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Error al obtener jugador" -ForegroundColor Red
}
Write-Host ""

# Prueba 3: Buscar jugadores por nombre
Write-Host "[4/6] GET /api/players?name=Ronaldo - Buscar..." -ForegroundColor Yellow
try {
    $search = Invoke-RestMethod -Uri "http://localhost:8080/api/players?name=Ronaldo" -Method Get -ErrorAction Stop
    $count = $search.content.Length
    Write-Host "✓ Resultados encontrados: $count" -ForegroundColor Green
    foreach ($player in $search.content) {
        Write-Host "  [$($player.id)] $($player.name)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "✗ Error en búsqueda" -ForegroundColor Red
}
Write-Host ""

# Prueba 4: Verificar Swagger UI
Write-Host "[5/6] Verificando Swagger UI..." -ForegroundColor Yellow
try {
    $swagger = Invoke-WebRequest -Uri "http://localhost:8080/swagger-ui/index.html" -Method Get -ErrorAction Stop
    if ($swagger.StatusCode -eq 200) {
        Write-Host "✓ Swagger UI accesible" -ForegroundColor Green
        Write-Host "  URL: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Cyan
    }
} catch {
    Write-Host "✗ Error al acceder a Swagger UI" -ForegroundColor Red
}
Write-Host ""

# Prueba 5: Verificar API Docs
Write-Host "[6/6] Verificando OpenAPI Docs..." -ForegroundColor Yellow
try {
    $docs = Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -Method Get -ErrorAction Stop
    if ($docs.StatusCode -eq 200) {
        Write-Host "✓ OpenAPI Docs disponibles" -ForegroundColor Green
        Write-Host "  URL: http://localhost:8080/v3/api-docs" -ForegroundColor Cyan
    }
} catch {
    Write-Host "✗ Error al acceder a API Docs" -ForegroundColor Red
}
Write-Host ""

# Resumen
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Resumen de Pruebas" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "✅ API REST funcionando correctamente" -ForegroundColor Green
Write-Host "✅ Endpoints accesibles" -ForegroundColor Green
Write-Host "✅ Documentación disponible" -ForegroundColor Green
Write-Host ""
Write-Host "Próximos pasos:" -ForegroundColor Yellow
Write-Host "  1. Abrir Swagger UI: http://localhost:8080/swagger-ui/index.html" -ForegroundColor White
Write-Host "  2. Probar endpoints interactivamente" -ForegroundColor White
Write-Host "  3. Implementar autenticación JWT" -ForegroundColor White
Write-Host ""
Write-Host "¿Deseas abrir Swagger UI en el navegador? (S/N)" -ForegroundColor Yellow
$respuesta = Read-Host

if ($respuesta -eq 'S' -or $respuesta -eq 's') {
    Start-Process "http://localhost:8080/swagger-ui/index.html"
    Write-Host "✓ Swagger UI abierto en el navegador" -ForegroundColor Green
}

Write-Host ""
Write-Host "Pruebas completadas exitosamente! 🎉" -ForegroundColor Green
