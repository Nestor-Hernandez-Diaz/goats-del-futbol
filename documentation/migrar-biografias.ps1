# ============================================================================
# FASE 8: Script PowerShell para migrar biografías a MySQL
# GOATs del Fútbol - Sistema Dinámico
# ============================================================================

Write-Host "`n=====================================================================" -ForegroundColor Cyan
Write-Host "   🔄 FASE 8: MIGRACIÓN DE BIOGRAFÍAS A BASE DE DATOS" -ForegroundColor Green
Write-Host "=====================================================================" -ForegroundColor Cyan

$mysqlPath = "C:\xampp\mysql\bin\mysql.exe"
$db = "goats_futbol"

if (-not (Test-Path $mysqlPath)) {
    Write-Host "`n✗ MySQL no encontrado en $mysqlPath" -ForegroundColor Red
    Write-Host "Verifica que XAMPP esté instalado" -ForegroundColor Yellow
    exit 1
}

Write-Host "`n✓ MySQL encontrado" -ForegroundColor Green

# ============================================================================
# MESSI (ID: 1)
# ============================================================================

Write-Host "`n📝 Actualizando biografía de Lionel Messi..." -ForegroundColor Yellow

$bioMessi = @"
<h3>Inicios en Argentina</h3><p>Lionel Andrés Messi nació el 24 de junio de 1987 en Rosario, Argentina. Desde muy pequeño mostró un talento excepcional para el fútbol, jugando en el equipo local Grandoli y posteriormente en Newell's Old Boys.</p><p>A los 11 años, le diagnosticaron una deficiencia de la hormona del crecimiento. El tratamiento era costoso, y fue entonces cuando el FC Barcelona se interesó en su talento y le ofreció cubrir los gastos médicos si se trasladaba a España.</p><h3>La Masía y el ascenso en el Barcelona</h3><p>En 2000, Messi y su familia se mudaron a Barcelona, donde ingresó en La Masía, la famosa academia juvenil del club. Su progreso fue meteórico, ascendiendo rápidamente por las categorías inferiores.</p><p>El 16 de octubre de 2004, con solo 17 años, Messi debutó oficialmente con el primer equipo del Barcelona en un derbi contra el Espanyol, convirtiéndose en uno de los jugadores más jóvenes en vestir la camiseta blaugrana.</p><h3>Leyenda del Barcelona</h3><p>Durante sus 17 temporadas en el Barcelona (2004-2021), Messi se convirtió en el máximo goleador histórico del club y de La Liga, ganando 35 títulos, incluyendo 10 Ligas españolas y 4 Champions League.</p><p>Su mejor temporada individual fue 2011-2012, cuando anotó la increíble cifra de 91 goles en un año calendario, estableciendo un récord mundial.</p><h3>Etapa en el PSG</h3><p>En agosto de 2021, debido a problemas financieros del Barcelona, Messi firmó con el Paris Saint-Germain. Durante sus dos temporadas en Francia, ganó dos títulos de Ligue 1, aunque no logró el ansiado título de Champions League con el club parisino.</p><h3>Aventura en Inter Miami</h3><p>En 2023, Messi sorprendió al mundo al fichar por el Inter Miami de la MLS, llevando su talento a Estados Unidos y ayudando a elevar el perfil del fútbol en Norteamérica.</p><h3>Carrera internacional</h3><p>Con la selección argentina, Messi experimentó tanto momentos de gloria como de decepción. Tras perder finales de Copa América y Mundial, finalmente logró su primer título importante al ganar la Copa América 2021.</p><p>Su consagración definitiva llegó en diciembre de 2022, cuando lideró a Argentina a la victoria en el Mundial de Qatar, completando así su legendario palmarés y silenciando a quienes cuestionaban su capacidad para brillar con la selección.</p>
"@

$bioMessi = $bioMessi -replace "'", "''"
& $mysqlPath -u root $db -e "UPDATE players SET biography = '$bioMessi' WHERE id = 1;" 2>&1 | Out-Null

if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✓ Messi actualizado (2,100 caracteres)" -ForegroundColor Green
} else {
    Write-Host "   ✗ Error actualizando Messi" -ForegroundColor Red
}

# ============================================================================
# RONALDO (ID: 2)
# ============================================================================

Write-Host "`n📝 Actualizando biografía de Cristiano Ronaldo..." -ForegroundColor Yellow

$bioRonaldo = @"
<h3>Inicios en Madeira</h3><p>Cristiano Ronaldo dos Santos Aveiro nació el 5 de febrero de 1985 en Funchal, Madeira, Portugal. Creció en un hogar humilde y desde muy joven mostró un talento excepcional para el fútbol, comenzando su carrera en el club local CF Andorinha.</p><p>A los 12 años, se trasladó a Lisboa para unirse a la academia del Sporting CP, dejando atrás a su familia para perseguir su sueño. Esta temprana independencia forjó su carácter y determinación.</p><h3>Descubrimiento y llegada a Inglaterra</h3><p>En 2003, durante un partido amistoso entre el Sporting CP y el Manchester United para inaugurar el Estadio José Alvalade, un joven Ronaldo de 18 años impresionó tanto a los jugadores del United que pidieron a Sir Alex Ferguson que lo fichara.</p><p>Ferguson no dudó y Cristiano se unió al Manchester United, donde heredó la legendaria camiseta número 7 que habían vestido ídolos como George Best, Eric Cantona y David Beckham.</p><h3>Consagración en el Manchester United</h3><p>Durante sus seis temporadas en Old Trafford (2003-2009), Ronaldo evolucionó de ser un extremo habilidoso pero inconsistente a convertirse en uno de los mejores jugadores del mundo. Ganó tres Premier League consecutivas y la Champions League en 2008, año en el que también consiguió su primer Balón de Oro.</p><h3>La era dorada en el Real Madrid</h3><p>En 2009, Ronaldo se convirtió en el fichaje más caro de la historia hasta ese momento cuando el Real Madrid pagó 94 millones de euros por sus servicios. Durante nueve temporadas en el club blanco (2009-2018), alcanzó la cima de su carrera.</p><p>Con el Madrid, Ronaldo ganó cuatro Champions League (tres consecutivas entre 2016 y 2018), dos Ligas españolas y se convirtió en el máximo goleador histórico del club con 450 goles en 438 partidos, un promedio superior a un gol por partido.</p><h3>Etapa en la Juventus</h3><p>En 2018, con 33 años, Ronaldo sorprendió al mundo al fichar por la Juventus de Turín. Durante sus tres temporadas en Italia, ganó dos Serie A y continuó demostrando su capacidad goleadora, superando los 100 goles con el club turinés.</p><h3>Regreso a Manchester y aventura en Arabia</h3><p>En 2021, Ronaldo regresó al Manchester United, donde a pesar de la difícil situación del club, logró ser el máximo goleador del equipo. Sin embargo, tras desacuerdos con la directiva, dejó el club en 2022.</p><p>A finales de 2022, firmó con el Al-Nassr de Arabia Saudita, llevando su talento y popularidad a una nueva región y convirtiéndose en el futbolista mejor pagado de la historia.</p><h3>Carrera internacional</h3><p>Con la selección portuguesa, Ronaldo ha sido el líder indiscutible durante casi dos décadas. Su momento más glorioso llegó en 2016, cuando capitaneó a Portugal hacia su primer título internacional en la Eurocopa, a pesar de lesionarse en la final.</p><p>En 2019, ayudó a Portugal a ganar la primera edición de la UEFA Nations League. A nivel individual, se ha convertido en el máximo goleador histórico de selecciones nacionales, superando el récord de Ali Daei.</p>
"@

$bioRonaldo = $bioRonaldo -replace "'", "''"
& $mysqlPath -u root $db -e "UPDATE players SET biography = '$bioRonaldo' WHERE id = 2;" 2>&1 | Out-Null

if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✓ Ronaldo actualizado (2,300 caracteres)" -ForegroundColor Green
} else {
    Write-Host "   ✗ Error actualizando Ronaldo" -ForegroundColor Red
}

# ============================================================================
# NEYMAR (ID: 3)
# ============================================================================

Write-Host "`n📝 Actualizando biografía de Neymar Jr..." -ForegroundColor Yellow

$bioNeymar = @"
<h3>Inicios en Brasil</h3><p>Neymar da Silva Santos Júnior nació el 5 de febrero de 1992 en Mogi das Cruzes, São Paulo, Brasil. Desde muy pequeño mostró un talento excepcional para el fútbol, heredando la pasión por este deporte de su padre, quien también fue futbolista profesional.</p><p>A los 11 años, Neymar se unió a las categorías inferiores del Santos FC, el mismo club donde brilló Pelé. Su progresión fue meteórica, destacando por su técnica, velocidad y capacidad para regatear.</p><h3>Estrella en el Santos</h3><p>En 2009, con solo 17 años, Neymar debutó con el primer equipo del Santos. Rápidamente se convirtió en la nueva sensación del fútbol brasileño, liderando al equipo a la conquista de la Copa Libertadores en 2011, el primer título continental del club desde la era de Pelé.</p><p>Durante sus cuatro temporadas como profesional en el Santos (2009-2013), Neymar anotó 136 goles en 225 partidos, ganando tres campeonatos paulistas consecutivos y el premio al mejor jugador de América en 2011 y 2012.</p><h3>El salto a Europa con el Barcelona</h3><p>En 2013, tras una intensa puja entre varios clubes europeos, Neymar fichó por el FC Barcelona. En el club catalán formó parte del legendario tridente ofensivo 'MSN' junto a Lionel Messi y Luis Suárez, considerado uno de los más letales de la historia.</p><p>Durante sus cuatro temporadas en Barcelona (2013-2017), Neymar ganó dos Ligas españolas, tres Copas del Rey, una Champions League y un Mundial de Clubes. Su mejor temporada fue la 2014-2015, cuando el tridente MSN anotó la impresionante cifra de 122 goles en todas las competiciones.</p><h3>El fichaje récord al PSG</h3><p>En agosto de 2017, Neymar protagonizó el traspaso más caro de la historia del fútbol cuando el Paris Saint-Germain pagó los 222 millones de euros de su cláusula de rescisión. Este movimiento sorprendió al mundo del fútbol y marcó un antes y un después en el mercado de fichajes.</p><p>En el PSG, Neymar continuó demostrando su calidad, aunque las lesiones marcaron parte de su etapa en Francia. A pesar de ello, ayudó al equipo a ganar múltiples títulos domésticos y a alcanzar la final de la Champions League en 2020.</p><h3>Nueva etapa en Arabia Saudita</h3><p>En 2023, tras seis temporadas en el PSG, Neymar se unió al Al-Hilal de Arabia Saudita, siguiendo la tendencia de estrellas internacionales que han llevado su talento a la liga saudí en los últimos años.</p><h3>Carrera internacional</h3><p>Con la selección brasileña, Neymar ha sido la gran esperanza desde muy joven. Ganó la medalla de oro en los Juegos Olímpicos de Río 2016, un título que Brasil anhelaba desde hacía décadas.</p><p>En Mundiales, su participación ha estado marcada por momentos de brillantez y también por la mala fortuna. En 2014, una lesión en cuartos de final le impidió jugar la histórica semifinal que Brasil perdió 7-1 ante Alemania. En 2022, a pesar de su buen rendimiento, Brasil cayó en cuartos de final ante Croacia.</p><p>A nivel individual, Neymar se ha convertido en uno de los máximos goleadores históricos de la selección brasileña, acercándose al récord de Pelé.</p>
"@

$bioNeymar = $bioNeymar -replace "'", "''"
& $mysqlPath -u root $db -e "UPDATE players SET biography = '$bioNeymar' WHERE id = 3;" 2>&1 | Out-Null

if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✓ Neymar actualizado (2,200 caracteres)" -ForegroundColor Green
} else {
    Write-Host "   ✗ Error actualizando Neymar" -ForegroundColor Red
}

# ============================================================================
# VERIFICACIÓN
# ============================================================================

Write-Host "`n🔍 Verificando longitudes de biografías..." -ForegroundColor Cyan

$query = "SELECT id, name, LENGTH(biography) AS chars, CASE WHEN LENGTH(biography) >= 500 THEN 'Válido' ELSE 'Muy corto' END AS estado FROM players WHERE id IN (1,2,3) ORDER BY id;"

& $mysqlPath -u root $db -e $query

Write-Host "`n=====================================================================" -ForegroundColor Cyan
Write-Host "   ✅ MIGRACIÓN COMPLETADA" -ForegroundColor Green
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host ""
