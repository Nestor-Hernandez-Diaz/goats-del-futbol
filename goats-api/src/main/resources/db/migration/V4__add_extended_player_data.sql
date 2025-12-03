-- ============================================================================
-- Migración V4: Agregar columnas extendidas para player.html dinámico
-- Fecha: Diciembre 2025
-- Objetivo: Soportar todas las secciones de las páginas legacy (8 secciones)
-- ============================================================================

-- Agregar columnas JSON para datos extendidos
ALTER TABLE players ADD COLUMN hero_info JSON COMMENT 'Info del hero: birthDate, clubs, quote';
ALTER TABLE players ADD COLUMN profile_image VARCHAR(255) COMMENT 'URL de imagen de perfil';
ALTER TABLE players ADD COLUMN profile_stats JSON COMMENT 'Estadísticas para sidebar: altura, peso, pie, etc.';
ALTER TABLE players ADD COLUMN career_highlights JSON COMMENT 'Momentos clave de la carrera';
ALTER TABLE players ADD COLUMN playing_style JSON COMMENT 'Estilo de juego: descripción + atributos';
ALTER TABLE players ADD COLUMN achievements JSON COMMENT 'Logros: clubes + selección + individuales';
ALTER TABLE players ADD COLUMN stats JSON COMMENT 'Estadísticas resumidas: goles, asistencias, partidos, títulos';
ALTER TABLE players ADD COLUMN season_stats JSON COMMENT 'Estadísticas por temporada';
ALTER TABLE players ADD COLUMN gallery JSON COMMENT 'Galería de imágenes con leyendas';
ALTER TABLE players ADD COLUMN legacy JSON COMMENT 'Legado: texto + citas de personalidades';
ALTER TABLE players ADD COLUMN videos JSON COMMENT 'Videos destacados con miniaturas';

-- ============================================================================
-- MIGRACIÓN DE DATOS: LIONEL MESSI (ID = 1)
-- ============================================================================

UPDATE players SET 
  hero_info = JSON_OBJECT(
    'birthDate', '24 de junio de 1987',
    'clubs', 'Barcelona, PSG, Inter Miami',
    'quote', 'No juego para ser el mejor de la historia, juego porque amo el fútbol.'
  ),
  profile_image = '../assets/images/messi-profile.png',
  profile_stats = JSON_OBJECT(
    'Nombre completo', 'Lionel Andrés Messi Cuccittini',
    'Fecha de nacimiento', '24 de junio de 1987',
    'Lugar de nacimiento', 'Rosario, Argentina',
    'Altura', '1,70 m',
    'Peso', '72 kg',
    'Pie dominante', 'Izquierdo',
    'Posición principal', 'Extremo derecho / Mediapunta',
    'Dorsal habitual', '10'
  ),
  career_highlights = JSON_ARRAY(
    JSON_OBJECT('year', '2005', 'event', 'Debut con la selección argentina'),
    JSON_OBJECT('year', '2009', 'event', 'Primer Balón de Oro'),
    JSON_OBJECT('year', '2012', 'event', '91 goles en un año calendario'),
    JSON_OBJECT('year', '2021', 'event', 'Campeón de Copa América'),
    JSON_OBJECT('year', '2022', 'event', 'Campeón del Mundo con Argentina'),
    JSON_OBJECT('year', '2023', 'event', 'Octavo Balón de Oro')
  ),
  playing_style = JSON_OBJECT(
    'description', 'Lionel Messi es considerado uno de los jugadores más completos de la historia del fútbol. Su baja estatura y centro de gravedad le otorgan un equilibrio excepcional, permitiéndole cambiar de dirección a gran velocidad. Su control del balón es prácticamente perfecto, con una capacidad única para mantener el esférico pegado a su pie izquierdo mientras supera a varios defensores.',
    'attributes', JSON_OBJECT(
      'Regate', 98,
      'Visión', 95,
      'Finalización', 96,
      'Pase', 94,
      'Tiro libre', 92,
      'Aceleración', 90
    )
  ),
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
  ),
  stats = JSON_OBJECT(
    'goals', '800',
    'assists', '350',
    'matches', '1000',
    'titles', '42'
  ),
  season_stats = JSON_ARRAY(
    JSON_OBJECT('season', '2004-05', 'club', 'Barcelona', 'matches', '9', 'goals', '1', 'assists', '0'),
    JSON_OBJECT('season', '2005-06', 'club', 'Barcelona', 'matches', '25', 'goals', '8', 'assists', '4'),
    JSON_OBJECT('season', '2006-07', 'club', 'Barcelona', 'matches', '36', 'goals', '17', 'assists', '3'),
    JSON_OBJECT('season', '2007-08', 'club', 'Barcelona', 'matches', '40', 'goals', '16', 'assists', '13'),
    JSON_OBJECT('season', '2008-09', 'club', 'Barcelona', 'matches', '51', 'goals', '38', 'assists', '18'),
    JSON_OBJECT('season', '2009-10', 'club', 'Barcelona', 'matches', '53', 'goals', '47', 'assists', '11'),
    JSON_OBJECT('season', '2010-11', 'club', 'Barcelona', 'matches', '55', 'goals', '53', 'assists', '24'),
    JSON_OBJECT('season', '2011-12', 'club', 'Barcelona', 'matches', '60', 'goals', '73', 'assists', '29'),
    JSON_OBJECT('season', '2012-13', 'club', 'Barcelona', 'matches', '50', 'goals', '60', 'assists', '16'),
    JSON_OBJECT('season', '2021-22', 'club', 'PSG', 'matches', '34', 'goals', '11', 'assists', '15'),
    JSON_OBJECT('season', '2022-23', 'club', 'PSG', 'matches', '41', 'goals', '21', 'assists', '20')
  ),
  gallery = JSON_ARRAY(
    JSON_OBJECT('url', '../assets/images/messi-barcelona.jpg', 'alt', 'Lionel Messi celebrando un gol emblemático con la camiseta azulgrana del FC Barcelona', 'caption', 'Messi celebrando un gol con el Barcelona'),
    JSON_OBJECT('url', '../assets/images/messi-copa-mundial.jpg', 'alt', 'Lionel Messi levantando la Copa del Mundo FIFA 2022 en Qatar', 'caption', 'Messi levantando la Copa del Mundo 2022'),
    JSON_OBJECT('url', '../assets/images/messi-copa-america.jpg', 'alt', 'Lionel Messi celebrando con el trofeo de la Copa América 2021', 'caption', 'Messi con la Copa América 2021'),
    JSON_OBJECT('url', '../assets/images/messi-balon-oro.jpg', 'alt', 'Lionel Messi posando con su octavo Balón de Oro 2023', 'caption', 'Messi con su octavo Balón de Oro'),
    JSON_OBJECT('url', '../assets/images/messi-psg.jpg', 'alt', 'Lionel Messi durante su etapa en el Paris Saint-Germain', 'caption', 'Messi durante su etapa en el Paris Saint-Germain'),
    JSON_OBJECT('url', '../assets/images/messi-inter-miami.jpg', 'alt', 'Lionel Messi con la camiseta rosa del Inter Miami CF', 'caption', 'Messi con la camiseta del Inter Miami')
  ),
  legacy = JSON_OBJECT(
    'text', 'El impacto de Lionel Messi en el fútbol trasciende los números y trofeos. Ha redefinido lo que significa ser un jugador completo, combinando capacidad goleadora con visión de juego y habilidad técnica.\n\nSu estilo de juego ha inspirado a toda una generación de jóvenes futbolistas que intentan emular su técnica, visión y capacidad para decidir partidos. Más allá del campo, Messi ha utilizado su plataforma para causas benéficas a través de la Fundación Leo Messi.\n\nEl debate sobre si es el mejor jugador de todos los tiempos continuará, pero su lugar entre los más grandes está asegurado.',
    'quotes', JSON_ARRAY(
      JSON_OBJECT('text', 'Messi es el mejor jugador de la historia, y lo digo después de haber jugado contra Maradona y Pelé.', 'author', 'Arrigo Sacchi, entrenador'),
      JSON_OBJECT('text', 'No creo que haya un jugador que pueda hacer lo que hace Messi. Es único.', 'author', 'Pep Guardiola, entrenador'),
      JSON_OBJECT('text', 'Para mí, Messi es el mejor jugador de la historia. Lo que hace con el balón es increíble.', 'author', 'Zinedine Zidane, exjugador y entrenador')
    )
  ),
  videos = JSON_ARRAY(
    JSON_OBJECT('url', 'https://www.youtube.com/watch?v=uYuUFhW7Vi8', 'thumbnail', '../assets/images/video-messi-mundial.jpg', 'title', 'Messi en la final del Mundial 2022'),
    JSON_OBJECT('url', 'https://www.youtube.com/shorts/SWeZwyqKRM4', 'thumbnail', '../assets/images/video-messi-gol-getafe.jpg', 'title', 'El gol maradoniano contra Getafe'),
    JSON_OBJECT('url', 'https://www.youtube.com/watch?v=5-mZA6MTaK4', 'thumbnail', '../assets/images/video-messi-hat-trick.jpg', 'title', 'Hat-trick contra el Real Madrid')
  )
WHERE id = 1;

-- ============================================================================
-- MIGRACIÓN DE DATOS: CRISTIANO RONALDO (ID = 2)
-- ============================================================================

UPDATE players SET 
  hero_info = JSON_OBJECT(
    'birthDate', '5 de febrero de 1985',
    'clubs', 'Sporting, Manchester United, Real Madrid, Juventus, Al-Nassr',
    'quote', 'Tu amor te hace fuerte. Tu odio me hace imparable.'
  ),
  profile_image = '../assets/images/ronaldo-profile.png',
  profile_stats = JSON_OBJECT(
    'Nombre completo', 'Cristiano Ronaldo dos Santos Aveiro',
    'Fecha de nacimiento', '5 de febrero de 1985',
    'Lugar de nacimiento', 'Funchal, Madeira, Portugal',
    'Altura', '1,87 m',
    'Peso', '84 kg',
    'Pie dominante', 'Derecho',
    'Posición principal', 'Extremo / Delantero',
    'Dorsal habitual', '7'
  ),
  career_highlights = JSON_ARRAY(
    JSON_OBJECT('year', '2008', 'event', 'Primer Balón de Oro'),
    JSON_OBJECT('year', '2014', 'event', 'Champions League con Real Madrid'),
    JSON_OBJECT('year', '2016', 'event', 'Eurocopa con Portugal'),
    JSON_OBJECT('year', '2018', 'event', 'Quinto Balón de Oro'),
    JSON_OBJECT('year', '2021', 'event', 'Regreso a Manchester United'),
    JSON_OBJECT('year', '2023', 'event', 'Llegada a Al-Nassr')
  ),
  playing_style = JSON_OBJECT(
    'description', 'Cristiano Ronaldo combina velocidad, potencia física y habilidad técnica. Su capacidad atlética le permite destacar en el juego aéreo y en sprints explosivos. Con ambos pies y excepcional en tiros libres, es un goleador completo que ha evolucionado de extremo a delantero centro.',
    'attributes', JSON_OBJECT(
      'Velocidad', 96,
      'Remate', 97,
      'Salto', 95,
      'Potencia', 96,
      'Tiro libre', 91,
      'Físico', 94
    )
  ),
  stats = JSON_OBJECT(
    'goals', '850',
    'assists', '250',
    'matches', '1200',
    'titles', '35'
  ),
  season_stats = JSON_ARRAY(
    JSON_OBJECT('season', '2003-04', 'club', 'Manchester United', 'matches', '40', 'goals', '6', 'assists', '7'),
    JSON_OBJECT('season', '2007-08', 'club', 'Manchester United', 'matches', '49', 'goals', '42', 'assists', '8'),
    JSON_OBJECT('season', '2013-14', 'club', 'Real Madrid', 'matches', '47', 'goals', '51', 'assists', '13'),
    JSON_OBJECT('season', '2014-15', 'club', 'Real Madrid', 'matches', '54', 'goals', '61', 'assists', '22')
  )
WHERE id = 2;

-- ============================================================================
-- MIGRACIÓN DE DATOS: NEYMAR JR (ID = 3)
-- ============================================================================

UPDATE players SET 
  hero_info = JSON_OBJECT(
    'birthDate', '5 de febrero de 1992',
    'clubs', 'Santos, Barcelona, PSG, Al-Hilal',
    'quote', 'Tengo que disfrutar del fútbol para poder seguir creciendo.'
  ),
  profile_image = '../assets/images/neymar-profile.png',
  profile_stats = JSON_OBJECT(
    'Nombre completo', 'Neymar da Silva Santos Júnior',
    'Fecha de nacimiento', '5 de febrero de 1992',
    'Lugar de nacimiento', 'Mogi das Cruzes, Brasil',
    'Altura', '1,75 m',
    'Peso', '68 kg',
    'Pie dominante', 'Derecho',
    'Posición principal', 'Extremo izquierdo / Mediapunta',
    'Dorsal habitual', '10'
  ),
  career_highlights = JSON_ARRAY(
    JSON_OBJECT('year', '2011', 'event', 'Revelación en el Santos'),
    JSON_OBJECT('year', '2013', 'event', 'Fichaje por el Barcelona'),
    JSON_OBJECT('year', '2015', 'event', 'Champions League con Barcelona'),
    JSON_OBJECT('year', '2017', 'event', 'Fichaje récord por el PSG (222M€)'),
    JSON_OBJECT('year', '2021', 'event', 'Copa América con Brasil'),
    JSON_OBJECT('year', '2023', 'event', 'Llegada a Al-Hilal')
  ),
  playing_style = JSON_OBJECT(
    'description', 'Neymar destaca por su técnica excepcional, creatividad y habilidad para el regate. Su arsenal de fintas y trucos lo hace impredecible. Excelente en espacios reducidos, combina velocidad con visión de juego, siendo tanto goleador como asistente.',
    'attributes', JSON_OBJECT(
      'Técnica', 96,
      'Regate', 95,
      'Creatividad', 97,
      'Velocidad', 92,
      'Pase', 91,
      'Finalización', 90
    )
  ),
  stats = JSON_OBJECT(
    'goals', '430',
    'assists', '275',
    'matches', '700',
    'titles', '28'
  ),
  season_stats = JSON_ARRAY(
    JSON_OBJECT('season', '2013-14', 'club', 'Barcelona', 'matches', '41', 'goals', '15', 'assists', '11'),
    JSON_OBJECT('season', '2014-15', 'club', 'Barcelona', 'matches', '51', 'goals', '39', 'assists', '10'),
    JSON_OBJECT('season', '2017-18', 'club', 'PSG', 'matches', '30', 'goals', '28', 'assists', '16'),
    JSON_OBJECT('season', '2019-20', 'club', 'PSG', 'matches', '27', 'goals', '18', 'assists', '12')
  )
WHERE id = 3;

-- Verificar migración
SELECT id, name, 
       JSON_EXTRACT(hero_info, '$.birthDate') as birth_date,
       JSON_EXTRACT(stats, '$.goals') as goals,
       JSON_LENGTH(gallery) as gallery_count,
       JSON_LENGTH(videos) as videos_count
FROM players
WHERE id IN (1, 2, 3);
