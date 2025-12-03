USE goats_futbol;

-- Actualizar logros/palmares de Messi con estructura completa
UPDATE players SET 
achievements = JSON_OBJECT(
  'clubs', JSON_ARRAY(
    JSON_OBJECT(
      'name', 'FC Barcelona',
      'logo', '../assets/images/barcelona-logo.png',
      'titles', JSON_ARRAY(
        JSON_OBJECT('count', '10×', 'name', 'La Liga'),
        JSON_OBJECT('count', '7×', 'name', 'Copa del Rey'),
        JSON_OBJECT('count', '8×', 'name', 'Supercopa de España'),
        JSON_OBJECT('count', '4×', 'name', 'UEFA Champions League'),
        JSON_OBJECT('count', '3×', 'name', 'Supercopa de Europa'),
        JSON_OBJECT('count', '3×', 'name', 'Mundial de Clubes')
      )
    ),
    JSON_OBJECT(
      'name', 'Paris Saint-Germain',
      'logo', '../assets/images/psg-logo.png',
      'titles', JSON_ARRAY(
        JSON_OBJECT('count', '2×', 'name', 'Ligue 1'),
        JSON_OBJECT('count', '1×', 'name', 'Supercopa de Francia')
      )
    ),
    JSON_OBJECT(
      'name', 'Inter Miami',
      'logo', '../assets/images/inter-miami-logo.png',
      'titles', JSON_ARRAY(
        JSON_OBJECT('count', '1×', 'name', 'Leagues Cup')
      )
    )
  ),
  'national', JSON_OBJECT(
    'name', 'Selección Argentina',
    'logo', '../assets/images/argentina-logo.png',
    'titles', JSON_ARRAY(
      JSON_OBJECT('count', '1×', 'name', 'Copa Mundial de la FIFA (2022)'),
      JSON_OBJECT('count', '1×', 'name', 'Copa América (2021)'),
      JSON_OBJECT('count', '1×', 'name', 'Finalissima (2022)'),
      JSON_OBJECT('count', '1×', 'name', 'Juegos Olímpicos - Medalla de Oro (2008)'),
      JSON_OBJECT('count', '1×', 'name', 'Mundial Sub-20 (2005)')
    )
  ),
  'individual', JSON_ARRAY(
    JSON_OBJECT('count', '8×', 'name', 'Balón de Oro'),
    JSON_OBJECT('count', '6×', 'name', 'Bota de Oro Europea'),
    JSON_OBJECT('count', '2×', 'name', 'Mejor Jugador de la FIFA'),
    JSON_OBJECT('count', '1×', 'name', 'Balón de Oro del Mundial (2022)'),
    JSON_OBJECT('count', '6×', 'name', 'Pichichi (Máximo goleador de La Liga)'),
    JSON_OBJECT('count', '1×', 'name', 'Laureus al Mejor Deportista del Año'),
    JSON_OBJECT('count', '12×', 'name', 'Equipo del Año de la UEFA')
  )
)
WHERE id = 1;

SELECT 'Logros y palmares de Messi actualizados correctamente' as resultado;
