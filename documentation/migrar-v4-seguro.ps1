# Migración V4 - Paso a paso con verificación
# Base de datos: goats_futbol

$mysql = "C:\xampp\mysql\bin\mysql.exe"
$db = "goats_futbol"

Write-Host "`n🔄 Iniciando migración V4 - Campos extendidos para player.html" -ForegroundColor Cyan
Write-Host "=" * 70 -ForegroundColor Gray

# Función para ejecutar SQL con manejo de errores
function Exec-SQL {
    param([string]$sql, [string]$description)
    
    Write-Host "`n▶ $description" -ForegroundColor Yellow
    $result = & $mysql -u root $db -e $sql 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✅ Éxito" -ForegroundColor Green
        return $true
    } else {
        if ($result -match "Duplicate column") {
            Write-Host "  ⚠️  Columna ya existe (saltando)" -ForegroundColor Yellow
            return $true
        } else {
            Write-Host "  ❌ Error: $result" -ForegroundColor Red
            return $false
        }
    }
}

# Paso 1: Agregar columnas JSON
Write-Host "`n📊 PASO 1: Agregar 11 columnas JSON" -ForegroundColor Cyan

$columns = @(
    @{sql="ALTER TABLE players ADD COLUMN hero_info JSON COMMENT 'Info hero: birthDate, clubs, quote';"; desc="Agregando hero_info"},
    @{sql="ALTER TABLE players ADD COLUMN profile_image VARCHAR(255) COMMENT 'URL imagen perfil';"; desc="Agregando profile_image"},
    @{sql="ALTER TABLE players ADD COLUMN profile_stats JSON COMMENT 'Estadísticas sidebar';"; desc="Agregando profile_stats"},
    @{sql="ALTER TABLE players ADD COLUMN career_highlights JSON COMMENT 'Momentos clave';"; desc="Agregando career_highlights"},
    @{sql="ALTER TABLE players ADD COLUMN playing_style JSON COMMENT 'Estilo de juego';"; desc="Agregando playing_style"},
    @{sql="ALTER TABLE players ADD COLUMN achievements JSON COMMENT 'Logros completos';"; desc="Agregando achievements"},
    @{sql="ALTER TABLE players ADD COLUMN stats JSON COMMENT 'Stats resumidas';"; desc="Agregando stats"},
    @{sql="ALTER TABLE players ADD COLUMN season_stats JSON COMMENT 'Stats por temporada';"; desc="Agregando season_stats"},
    @{sql="ALTER TABLE players ADD COLUMN gallery JSON COMMENT 'Galería imágenes';"; desc="Agregando gallery"},
    @{sql="ALTER TABLE players ADD COLUMN legacy JSON COMMENT 'Legado e impacto';"; desc="Agregando legacy"},
    @{sql="ALTER TABLE players ADD COLUMN videos JSON COMMENT 'Videos destacados';"; desc="Agregando videos"}
)

$successCount = 0
foreach ($col in $columns) {
    if (Exec-SQL -sql $col.sql -description $col.desc) {
        $successCount++
    }
}

Write-Host "`n✅ Columnas agregadas: $successCount/11" -ForegroundColor Green

# Verificar estructura
Write-Host "`n📋 Verificando estructura de tabla players:" -ForegroundColor Cyan
& $mysql -u root $db -e "SHOW COLUMNS FROM players WHERE Field LIKE '%info%' OR Field LIKE '%stats%' OR Field LIKE '%style%' OR Field LIKE '%gallery%' OR Field LIKE '%legacy%' OR Field LIKE '%videos%' OR Field LIKE '%achievements%' OR Field LIKE '%highlights%' OR Field LIKE '%image%';"

Write-Host "`n" -NoNewline
Read-Host "Presiona ENTER para continuar con la migración de datos"

# Paso 2: Migrar datos de Messi (simplificado primero)
Write-Host "`n📊 PASO 2: Migrando datos de MESSI (ID=1)" -ForegroundColor Cyan

$messiHeroInfo = '{"birthDate": "24 de junio de 1987", "clubs": "Barcelona, PSG, Inter Miami", "quote": "No juego para ser el mejor de la historia, juego porque amo el fútbol."}'
$messiStats = '{"goals": "800", "assists": "350", "matches": "1000", "titles": "42"}'

Exec-SQL "UPDATE players SET hero_info = '$messiHeroInfo' WHERE id = 1;" "Actualizando hero_info de Messi"
Exec-SQL "UPDATE players SET stats = '$messiStats' WHERE id = 1;" "Actualizando stats de Messi"
Exec-SQL "UPDATE players SET profile_image = '../assets/images/messi-profile.png' WHERE id = 1;" "Actualizando profile_image de Messi"

Write-Host "`n📊 PASO 3: Verificando datos migrados" -ForegroundColor Cyan
& $mysql -u root $db -e "SELECT id, name, hero_info, stats FROM players WHERE id = 1\G"

Write-Host "`n✅ Migración V4 completada!" -ForegroundColor Green
Write-Host "📌 Siguiente paso: Reiniciar backend y probar player.html?id=1" -ForegroundColor Yellow
