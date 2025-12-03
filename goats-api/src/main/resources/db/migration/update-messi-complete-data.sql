-- Script para actualizar datos completos de Messi en BD
USE goats_futbol;

-- 1. Actualizar playingStyle con descripción completa (3 párrafos)
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

-- 2. Actualizar legacy con texto completo (3 párrafos) y citas
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

-- 3. Actualizar videos con URLs correctas de YouTube
UPDATE players SET 
videos = JSON_ARRAY(
  JSON_OBJECT('url', 'https://www.youtube.com/watch?v=uYuUFhW7Vi8', 'thumbnail', '../assets/images/video-messi-mundial.jpg', 'title', 'Messi en la final del Mundial 2022'),
  JSON_OBJECT('url', 'https://www.youtube.com/shorts/SWeZwyqKRM4', 'thumbnail', '../assets/images/video-messi-gol-getafe.jpg', 'title', 'El gol maradoniano contra Getafe'),
  JSON_OBJECT('url', 'https://www.youtube.com/watch?v=5-mZA6MTaK4', 'thumbnail', '../assets/images/video-messi-hat-trick.jpg', 'title', 'Hat-trick contra el Real Madrid')
)
WHERE id = 1;

-- 4. Actualizar profileImage con ruta correcta
UPDATE players SET 
profile_image = '../assets/images/messi-profile.png'
WHERE id = 1;

SELECT 'Datos actualizados correctamente' as resultado;
