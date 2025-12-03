-- Limpiar y reinsertar TODOS los datos con UTF-8 correcto
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
USE goats_futbol;

-- Limpiar campos JSON de Messi
UPDATE players SET 
  playing_style = NULL,
  legacy = NULL,
  achievements = NULL,
  gallery = NULL,
  videos = NULL,
  stats = NULL,
  season_stats = NULL
WHERE id = 1;

-- 1. PLAYING STYLE
UPDATE players SET 
playing_style = CAST(JSON_OBJECT(
  'description', 'Lionel Messi es considerado uno de los jugadores más completos de la historia del fútbol. Su baja estatura y centro de gravedad le otorgan un equilibrio excepcional, permitiéndole cambiar de dirección a gran velocidad.\n\nSu control del balón es prácticamente perfecto, con una capacidad única para mantener el esférico pegado a su pie izquierdo mientras supera a varios defensores. La visión de juego de Messi es extraordinaria, siendo capaz de encontrar pases que pocos jugadores pueden siquiera visualizar.\n\nA lo largo de su carrera, Messi ha evolucionado desde un extremo derecho que cortaba hacia el centro para disparar con su pie izquierdo, hasta convertirse en un mediapunta que organiza el juego y crea oportunidades para sus compañeros, sin perder su instinto goleador.',
  'attributes', JSON_OBJECT(
    'Regate', 98, 'Visión', 95, 'Finalización', 96,
    'Pase', 94, 'Tiro libre', 92, 'Aceleración', 90
  )
) AS CHAR CHARACTER SET utf8mb4)
WHERE id = 1;

-- 2. LEGACY
UPDATE players SET 
legacy = CAST(JSON_OBJECT(
  'text', 'El impacto de Lionel Messi en el fútbol trasciende los números y trofeos. Ha redefinido lo que significa ser un jugador completo, combinando capacidad goleadora con visión de juego y habilidad técnica.\n\nSu estilo de juego ha inspirado a toda una generación de jóvenes futbolistas que intentan emular su técnica, visión y capacidad para decidir partidos. Más allá del campo, Messi ha utilizado su plataforma para causas benéficas a través de la Fundación Leo Messi, que apoya la educación y la salud de niños desfavorecidos.\n\nEl debate sobre si es el mejor jugador de todos los tiempos continuará, pero su lugar entre los más grandes está asegurado. Su carrera representa la perseverancia, habiendo superado obstáculos desde su diagnóstico de deficiencia hormonal hasta las críticas por su rendimiento con Argentina, culminando con la gloria mundial en 2022.',
  'quotes', JSON_ARRAY(
    JSON_OBJECT('text', 'Messi es el mejor jugador de la historia, y lo digo después de haber jugado contra Maradona y Pelé.', 'author', 'Arrigo Sacchi'),
    JSON_OBJECT('text', 'No creo que haya un jugador que pueda hacer lo que hace Messi. Es único.', 'author', 'Pep Guardiola'),
    JSON_OBJECT('text', 'Para mí, Messi es el mejor jugador de la historia. Lo que hace con el balón es increíble.', 'author', 'Zinedine Zidane')
  )
) AS CHAR CHARACTER SET utf8mb4)
WHERE id = 1;

-- 3. ACHIEVEMENTS
UPDATE players SET 
achievements = CAST(JSON_OBJECT(
  'clubs', JSON_ARRAY(
    JSON_OBJECT('name', 'FC Barcelona', 'logo', '../assets/images/barcelona-logo.png',
      'titles', JSON_ARRAY(
        JSON_OBJECT('count', '10×', 'name', 'La Liga'),
        JSON_OBJECT('count', '7×', 'name', 'Copa del Rey'),
        JSON_OBJECT('count', '8×', 'name', 'Supercopa de España'),
        JSON_OBJECT('count', '4×', 'name', 'UEFA Champions League'),
        JSON_OBJECT('count', '3×', 'name', 'Supercopa de Europa'),
        JSON_OBJECT('count', '3×', 'name', 'Mundial de Clubes')
      )
    ),
    JSON_OBJECT('name', 'Paris Saint-Germain', 'logo', '../assets/images/psg-logo.png',
      'titles', JSON_ARRAY(
        JSON_OBJECT('count', '2×', 'name', 'Ligue 1'),
        JSON_OBJECT('count', '1×', 'name', 'Supercopa de Francia')
      )
    ),
    JSON_OBJECT('name', 'Inter Miami', 'logo', '../assets/images/inter-miami-logo.png',
      'titles', JSON_ARRAY(JSON_OBJECT('count', '1×', 'name', 'Leagues Cup'))
    )
  ),
  'national', JSON_OBJECT('name', 'Selección Argentina', 'logo', '../assets/images/argentina-logo.png',
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
) AS CHAR CHARACTER SET utf8mb4)
WHERE id = 1;

-- 4. STATS
UPDATE players SET 
stats = CAST(JSON_OBJECT('goals', 820, 'assists', 375, 'matches', 1038, 'titles', 42) AS CHAR CHARACTER SET utf8mb4)
WHERE id = 1;

-- 5. SEASON STATS
UPDATE players SET 
season_stats = CAST(JSON_ARRAY(
  JSON_OBJECT('season', '2004-05', 'club', 'Barcelona', 'matches', 9, 'goals', 1, 'assists', 0),
  JSON_OBJECT('season', '2005-06', 'club', 'Barcelona', 'matches', 25, 'goals', 8, 'assists', 4),
  JSON_OBJECT('season', '2006-07', 'club', 'Barcelona', 'matches', 36, 'goals', 17, 'assists', 3),
  JSON_OBJECT('season', '2007-08', 'club', 'Barcelona', 'matches', 40, 'goals', 16, 'assists', 13),
  JSON_OBJECT('season', '2008-09', 'club', 'Barcelona', 'matches', 51, 'goals', 38, 'assists', 18),
  JSON_OBJECT('season', '2009-10', 'club', 'Barcelona', 'matches', 53, 'goals', 47, 'assists', 11),
  JSON_OBJECT('season', '2010-11', 'club', 'Barcelona', 'matches', 55, 'goals', 53, 'assists', 24),
  JSON_OBJECT('season', '2011-12', 'club', 'Barcelona', 'matches', 60, 'goals', 73, 'assists', 29),
  JSON_OBJECT('season', '2012-13', 'club', 'Barcelona', 'matches', 50, 'goals', 60, 'assists', 16),
  JSON_OBJECT('season', '2013-14', 'club', 'Barcelona', 'matches', 46, 'goals', 41, 'assists', 14),
  JSON_OBJECT('season', '2014-15', 'club', 'Barcelona', 'matches', 57, 'goals', 58, 'assists', 27),
  JSON_OBJECT('season', '2015-16', 'club', 'Barcelona', 'matches', 49, 'goals', 41, 'assists', 23),
  JSON_OBJECT('season', '2016-17', 'club', 'Barcelona', 'matches', 52, 'goals', 54, 'assists', 16),
  JSON_OBJECT('season', '2017-18', 'club', 'Barcelona', 'matches', 54, 'goals', 45, 'assists', 18),
  JSON_OBJECT('season', '2018-19', 'club', 'Barcelona', 'matches', 50, 'goals', 51, 'assists', 19),
  JSON_OBJECT('season', '2019-20', 'club', 'Barcelona', 'matches', 44, 'goals', 31, 'assists', 27),
  JSON_OBJECT('season', '2020-21', 'club', 'Barcelona', 'matches', 47, 'goals', 38, 'assists', 14),
  JSON_OBJECT('season', '2021-22', 'club', 'PSG', 'matches', 34, 'goals', 11, 'assists', 15),
  JSON_OBJECT('season', '2022-23', 'club', 'PSG', 'matches', 41, 'goals', 21, 'assists', 20),
  JSON_OBJECT('season', '2023-24', 'club', 'Inter Miami', 'matches', 19, 'goals', 11, 'assists', 5)
) AS CHAR CHARACTER SET utf8mb4)
WHERE id = 1;

-- 6. GALLERY
UPDATE players SET 
gallery = CAST(JSON_ARRAY(
  JSON_OBJECT('url', '../assets/images/messi-barcelona.jpg', 'alt', 'Lionel Messi celebrando con el Barcelona', 'caption', 'Celebrando con el Barcelona'),
  JSON_OBJECT('url', '../assets/images/messi-copa-mundial.jpg', 'alt', 'Messi con la Copa del Mundo', 'caption', 'Levantando la Copa del Mundo 2022'),
  JSON_OBJECT('url', '../assets/images/messi-copa-america.jpg', 'alt', 'Messi con Copa América', 'caption', 'Con la Copa América 2021'),
  JSON_OBJECT('url', '../assets/images/messi-balon-oro.jpg', 'alt', 'Messi con Balón de Oro', 'caption', 'Con su octavo Balón de Oro'),
  JSON_OBJECT('url', '../assets/images/messi-psg.jpg', 'alt', 'Messi con PSG', 'caption', 'Con el PSG'),
  JSON_OBJECT('url', '../assets/images/messi-inter-miami.jpg', 'alt', 'Messi con Inter Miami', 'caption', 'Con el Inter Miami')
) AS CHAR CHARACTER SET utf8mb4)
WHERE id = 1;

-- 7. VIDEOS
UPDATE players SET 
videos = CAST(JSON_ARRAY(
  JSON_OBJECT('url', 'https://www.youtube.com/watch?v=uYuUFhW7Vi8', 'thumbnail', '../assets/images/video-messi-mundial.jpg', 'title', 'Messi en la final del Mundial 2022'),
  JSON_OBJECT('url', 'https://www.youtube.com/shorts/SWeZwyqKRM4', 'thumbnail', '../assets/images/video-messi-gol-getafe.jpg', 'title', 'El gol maradoniano contra Getafe'),
  JSON_OBJECT('url', 'https://www.youtube.com/watch?v=5-mZA6MTaK4', 'thumbnail', '../assets/images/video-messi-hat-trick.jpg', 'title', 'Hat-trick contra el Real Madrid')
) AS CHAR CHARACTER SET utf8mb4)
WHERE id = 1;

SELECT 'TODOS los datos reconvertidos con UTF-8 correcto usando CAST' as resultado;
