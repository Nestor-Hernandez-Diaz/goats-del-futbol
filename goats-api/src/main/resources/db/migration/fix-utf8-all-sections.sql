-- Script completo para actualizar TODAS las secciones de Messi con UTF-8 correcto
-- Ejecutar con: mysql -u root goats_futbol --default-character-set=utf8mb4 < script.sql

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE goats_futbol;

-- 1. PLAYING STYLE (Estilo de Juego)
UPDATE players SET 
playing_style = JSON_OBJECT(
  'description', 'Lionel Messi es considerado uno de los jugadores más completos de la historia del fútbol. Su baja estatura y centro de gravedad le otorgan un equilibrio excepcional, permitiéndole cambiar de dirección a gran velocidad.\n\nSu control del balón es prácticamente perfecto, con una capacidad única para mantener el esférico pegado a su pie izquierdo mientras supera a varios defensores. La visión de juego de Messi es extraordinaria, siendo capaz de encontrar pases que pocos jugadores pueden siquiera visualizar.\n\nA lo largo de su carrera, Messi ha evolucionado desde un extremo derecho que cortaba hacia el centro para disparar con su pie izquierdo, hasta convertirse en un mediapunta que organiza el juego y crea oportunidades para sus compañeros, sin perder su instinto goleador.',
  'attributes', JSON_OBJECT(
    'Regate', 98,
    'Visión', 95,
    'Finalización', 96,
    'Pase', 94,
    'Tiro libre', 92,
    'Aceleración', 90
  )
)
WHERE id = 1;

-- 2. LEGACY (Legado)
UPDATE players SET 
legacy = JSON_OBJECT(
  'text', 'El impacto de Lionel Messi en el fútbol trasciende los números y trofeos. Ha redefinido lo que significa ser un jugador completo, combinando capacidad goleadora con visión de juego y habilidad técnica.\n\nSu estilo de juego ha inspirado a toda una generación de jóvenes futbolistas que intentan emular su técnica, visión y capacidad para decidir partidos. Más allá del campo, Messi ha utilizado su plataforma para causas benéficas a través de la Fundación Leo Messi, que apoya la educación y la salud de niños desfavorecidos.\n\nEl debate sobre si es el mejor jugador de todos los tiempos continuará, pero su lugar entre los más grandes está asegurado. Su carrera representa la perseverancia, habiendo superado obstáculos desde su diagnóstico de deficiencia hormonal hasta las críticas por su rendimiento con Argentina, culminando con la gloria mundial en 2022.',
  'quotes', JSON_ARRAY(
    JSON_OBJECT('text', 'Messi es el mejor jugador de la historia, y lo digo después de haber jugado contra Maradona y Pelé.', 'author', 'Arrigo Sacchi'),
    JSON_OBJECT('text', 'No creo que haya un jugador que pueda hacer lo que hace Messi. Es único.', 'author', 'Pep Guardiola'),
    JSON_OBJECT('text', 'Para mí, Messi es el mejor jugador de la historia. Lo que hace con el balón es increíble.', 'author', 'Zinedine Zidane')
  )
)
WHERE id = 1;

-- 3. ACHIEVEMENTS (Logros)
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

-- 4. GALLERY (actualizar captions con tildes)
UPDATE players SET 
gallery = JSON_ARRAY(
  JSON_OBJECT(
    'url', '../assets/images/messi-barcelona.jpg',
    'alt', 'Lionel Messi celebrando un gol emblemático con la camiseta azulgrana del FC Barcelona',
    'caption', 'Celebrando con el Barcelona'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-copa-mundial.jpg',
    'alt', 'Lionel Messi levantando la Copa del Mundo FIFA 2022 en Qatar',
    'caption', 'Levantando la Copa del Mundo 2022'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-copa-america.jpg',
    'alt', 'Lionel Messi celebrando con el trofeo de la Copa América 2021',
    'caption', 'Con la Copa América 2021'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-balon-oro.jpg',
    'alt', 'Lionel Messi posando con su octavo Balón de Oro 2023',
    'caption', 'Con su octavo Balón de Oro'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-psg.jpg',
    'alt', 'Lionel Messi en acción con la camiseta del Paris Saint-Germain',
    'caption', 'Con el PSG'
  ),
  JSON_OBJECT(
    'url', '../assets/images/messi-inter-miami.jpg',
    'alt', 'Lionel Messi celebrando con Inter Miami CF en la MLS',
    'caption', 'Con el Inter Miami'
  )
)
WHERE id = 1;

SELECT 'Todas las secciones actualizadas con UTF-8 correcto' as resultado;
