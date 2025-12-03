-- Forzar UTF8MB4 en la sesión y reconvertir datos
SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET CHARACTER SET utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

USE goats_futbol;

-- Convertir columnas a utf8mb4 si no lo están
ALTER TABLE players 
  MODIFY COLUMN playing_style LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  MODIFY COLUMN legacy LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  MODIFY COLUMN achievements LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  MODIFY COLUMN gallery LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  MODIFY COLUMN videos LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  MODIFY COLUMN stats LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  MODIFY COLUMN season_stats LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Ahora insertar datos limpios
UPDATE players SET playing_style = NULL, legacy = NULL, achievements = NULL, gallery = NULL, videos = NULL, stats = NULL, season_stats = NULL WHERE id = 1;

-- PLAYING STYLE con UTF8MB4
UPDATE players SET playing_style = '{"description": "Lionel Messi es considerado uno de los jugadores más completos de la historia del fútbol. Su baja estatura y centro de gravedad le otorgan un equilibrio excepcional, permitiéndole cambiar de dirección a gran velocidad.\\n\\nSu control del balón es prácticamente perfecto, con una capacidad única para mantener el esférico pegado a su pie izquierdo mientras supera a varios defensores. La visión de juego de Messi es extraordinaria, siendo capaz de encontrar pases que pocos jugadores pueden siquiera visualizar.\\n\\nA lo largo de su carrera, Messi ha evolucionado desde un extremo derecho que cortaba hacia el centro para disparar con su pie izquierdo, hasta convertirse en un mediapunta que organiza el juego y crea oportunidades para sus compañeros, sin perder su instinto goleador.", "attributes": {"Regate": 98, "Visión": 95, "Finalización": 96, "Pase": 94, "Tiro libre": 92, "Aceleración": 90}}' WHERE id = 1;

-- LEGACY con UTF8MB4
UPDATE players SET legacy = '{"text": "El impacto de Lionel Messi en el fútbol trasciende los números y trofeos. Ha redefinido lo que significa ser un jugador completo, combinando capacidad goleadora con visión de juego y habilidad técnica.\\n\\nSu estilo de juego ha inspirado a toda una generación de jóvenes futbolistas que intentan emular su técnica, visión y capacidad para decidir partidos. Más allá del campo, Messi ha utilizado su plataforma para causas benéficas a través de la Fundación Leo Messi, que apoya la educación y la salud de niños desfavorecidos.\\n\\nEl debate sobre si es el mejor jugador de todos los tiempos continuará, pero su lugar entre los más grandes está asegurado. Su carrera representa la perseverancia, habiendo superado obstáculos desde su diagnóstico de deficiencia hormonal hasta las críticas por su rendimiento con Argentina, culminando con la gloria mundial en 2022.", "quotes": [{"text": "Messi es el mejor jugador de la historia, y lo digo después de haber jugado contra Maradona y Pelé.", "author": "Arrigo Sacchi"}, {"text": "No creo que haya un jugador que pueda hacer lo que hace Messi. Es único.", "author": "Pep Guardiola"}, {"text": "Para mí, Messi es el mejor jugador de la historia. Lo que hace con el balón es increíble.", "author": "Zinedine Zidane"}]}' WHERE id = 1;

-- ACHIEVEMENTS con UTF8MB4
UPDATE players SET achievements = '{"clubs": [{"name": "FC Barcelona", "logo": "../assets/images/barcelona-logo.png", "titles": [{"count": "10×", "name": "La Liga"}, {"count": "7×", "name": "Copa del Rey"}, {"count": "8×", "name": "Supercopa de España"}, {"count": "4×", "name": "UEFA Champions League"}, {"count": "3×", "name": "Supercopa de Europa"}, {"count": "3×", "name": "Mundial de Clubes"}]}, {"name": "Paris Saint-Germain", "logo": "../assets/images/psg-logo.png", "titles": [{"count": "2×", "name": "Ligue 1"}, {"count": "1×", "name": "Supercopa de Francia"}]}, {"name": "Inter Miami", "logo": "../assets/images/inter-miami-logo.png", "titles": [{"count": "1×", "name": "Leagues Cup"}]}], "national": {"name": "Selección Argentina", "logo": "../assets/images/argentina-logo.png", "titles": [{"count": "1×", "name": "Copa Mundial de la FIFA (2022)"}, {"count": "1×", "name": "Copa América (2021)"}, {"count": "1×", "name": "Finalissima (2022)"}, {"count": "1×", "name": "Juegos Olímpicos - Medalla de Oro (2008)"}, {"count": "1×", "name": "Mundial Sub-20 (2005)"}]}, "individual": [{"count": "8×", "name": "Balón de Oro"}, {"count": "6×", "name": "Bota de Oro Europea"}, {"count": "2×", "name": "Mejor Jugador de la FIFA"}, {"count": "1×", "name": "Balón de Oro del Mundial (2022)"}, {"count": "6×", "name": "Pichichi (Máximo goleador de La Liga)"}, {"count": "1×", "name": "Laureus al Mejor Deportista del Año"}, {"count": "12×", "name": "Equipo del Año de la UEFA"}]}' WHERE id = 1;

-- STATS
UPDATE players SET stats = '{"goals": 820, "assists": 375, "matches": 1038, "titles": 42}' WHERE id = 1;

-- SEASON STATS
UPDATE players SET season_stats = '[{"season": "2004-05", "club": "Barcelona", "matches": 9, "goals": 1, "assists": 0}, {"season": "2005-06", "club": "Barcelona", "matches": 25, "goals": 8, "assists": 4}, {"season": "2006-07", "club": "Barcelona", "matches": 36, "goals": 17, "assists": 3}, {"season": "2007-08", "club": "Barcelona", "matches": 40, "goals": 16, "assists": 13}, {"season": "2008-09", "club": "Barcelona", "matches": 51, "goals": 38, "assists": 18}, {"season": "2009-10", "club": "Barcelona", "matches": 53, "goals": 47, "assists": 11}, {"season": "2010-11", "club": "Barcelona", "matches": 55, "goals": 53, "assists": 24}, {"season": "2011-12", "club": "Barcelona", "matches": 60, "goals": 73, "assists": 29}, {"season": "2012-13", "club": "Barcelona", "matches": 50, "goals": 60, "assists": 16}, {"season": "2013-14", "club": "Barcelona", "matches": 46, "goals": 41, "assists": 14}, {"season": "2014-15", "club": "Barcelona", "matches": 57, "goals": 58, "assists": 27}, {"season": "2015-16", "club": "Barcelona", "matches": 49, "goals": 41, "assists": 23}, {"season": "2016-17", "club": "Barcelona", "matches": 52, "goals": 54, "assists": 16}, {"season": "2017-18", "club": "Barcelona", "matches": 54, "goals": 45, "assists": 18}, {"season": "2018-19", "club": "Barcelona", "matches": 50, "goals": 51, "assists": 19}, {"season": "2019-20", "club": "Barcelona", "matches": 44, "goals": 31, "assists": 27}, {"season": "2020-21", "club": "Barcelona", "matches": 47, "goals": 38, "assists": 14}, {"season": "2021-22", "club": "PSG", "matches": 34, "goals": 11, "assists": 15}, {"season": "2022-23", "club": "PSG", "matches": 41, "goals": 21, "assists": 20}, {"season": "2023-24", "club": "Inter Miami", "matches": 19, "goals": 11, "assists": 5}]' WHERE id = 1;

-- GALLERY
UPDATE players SET gallery = '[{"url": "../assets/images/messi-barcelona.jpg", "alt": "Messi con Barcelona", "caption": "Celebrando con el Barcelona"}, {"url": "../assets/images/messi-copa-mundial.jpg", "alt": "Messi Copa Mundial", "caption": "Levantando la Copa del Mundo 2022"}, {"url": "../assets/images/messi-copa-america.jpg", "alt": "Messi Copa América", "caption": "Con la Copa América 2021"}, {"url": "../assets/images/messi-balon-oro.jpg", "alt": "Messi Balón de Oro", "caption": "Con su octavo Balón de Oro"}, {"url": "../assets/images/messi-psg.jpg", "alt": "Messi PSG", "caption": "Con el PSG"}, {"url": "../assets/images/messi-inter-miami.jpg", "alt": "Messi Inter Miami", "caption": "Con el Inter Miami"}]' WHERE id = 1;

-- VIDEOS
UPDATE players SET videos = '[{"url": "https://www.youtube.com/watch?v=uYuUFhW7Vi8", "thumbnail": "../assets/images/video-messi-mundial.jpg", "title": "Messi en la final del Mundial 2022"}, {"url": "https://www.youtube.com/shorts/SWeZwyqKRM4", "thumbnail": "../assets/images/video-messi-gol-getafe.jpg", "title": "El gol maradoniano contra Getafe"}, {"url": "https://www.youtube.com/watch?v=5-mZA6MTaK4", "thumbnail": "../assets/images/video-messi-hat-trick.jpg", "title": "Hat-trick contra el Real Madrid"}]' WHERE id = 1;

SELECT '✓ Columnas convertidas a utf8mb4 y datos insertados correctamente' as resultado;
