# Script de migración simplificado V4
# Agrega columnas JSON a la tabla players

$mysql = "C:\xampp\mysql\bin\mysql.exe"
$db = "goats_futbol"

Write-Host "🔄 Iniciando migración V4..." -ForegroundColor Cyan

# Agregar columnas una por una
$columns = @(
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS hero_info JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS profile_image VARCHAR(255);",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS profile_stats JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS career_highlights JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS playing_style JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS achievements JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS stats JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS season_stats JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS gallery JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS legacy JSON;",
    "ALTER TABLE players ADD COLUMN IF NOT EXISTS videos JSON;"
)

foreach ($sql in $columns) {
    & $mysql -u root $db -e $sql
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Columna agregada" -ForegroundColor Green
    }
}

Write-Host "`n✅ Migración V4 completada - 11 columnas JSON agregadas" -ForegroundColor Green
Write-Host "📊 Verificando columnas..." -ForegroundColor Cyan

& $mysql -u root $db -e "SHOW COLUMNS FROM players LIKE '%info%' OR Field LIKE '%stats%' OR Field LIKE '%style%';"
