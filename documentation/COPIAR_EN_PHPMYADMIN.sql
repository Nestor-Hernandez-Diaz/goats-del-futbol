-- ============================================================
-- INSTRUCCIONES: 
-- 1. Abre http://localhost/phpmyadmin
-- 2. Selecciona base de datos "goats_futbol"
-- 3. Clic en pestaña "SQL"
-- 4. Copia TODO este contenido
-- 5. Pégalo en el cuadro de texto
-- 6. Clic en "Continuar" o "Go"
-- ============================================================

SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ============================================================
-- ACTUALIZAR DATOS DE PERFIL (hero_info, profile_image, profile_stats, career_highlights)
-- ============================================================
UPDATE players SET 
  hero_info = '{
    "birthDate": "24 de junio de 1987",
    "clubs": "Barcelona, PSG, Inter Miami",
    "quote": "No juego para ser el mejor de la historia, juego porque amo el fútbol."
  }',
  profile_image = '../assets/images/messi-profile.png',
  profile_stats = '{
    "Nombre completo": "Lionel Andrés Messi Cuccittini",
    "Fecha de nacimiento": "24 de junio de 1987",
    "Lugar de nacimiento": "Rosario, Argentina",
    "Altura": "1,70 m",
    "Peso": "72 kg",
    "Pie dominante": "Izquierdo",
    "Posición principal": "Extremo derecho / Mediapunta",
    "Dorsal habitual": "10"
  }',
  career_highlights = '[
    {"year": "2005", "event": "Debut con la selección argentina"},
    {"year": "2009", "event": "Primer Balón de Oro"},
    {"year": "2012", "event": "91 goles en un año calendario"},
    {"year": "2021", "event": "Campeón de Copa América"},
    {"year": "2022", "event": "Campeón del Mundo con Argentina"},
    {"year": "2023", "event": "Octavo Balón de Oro"}
  ]'
WHERE id = 1;

-- ============================================================
-- ACTUALIZAR ESTILO DE JUEGO (playing_style)
-- ============================================================
UPDATE players SET playing_style = '{
  "description": "Lionel Messi es considerado uno de los jugadores más completos de la historia del fútbol. Su baja estatura y centro de gravedad le otorgan un equilibrio excepcional, permitiéndole cambiar de dirección a gran velocidad.\\n\\nSu control del balón es prácticamente perfecto, con una capacidad única para mantener la posesión en espacios reducidos. Su visión de juego y creatividad le permiten encontrar pases que otros jugadores no ven, convirtiéndolo en un creador de oportunidades excepcional.\\n\\nAunque no es el más rápido en velocidad punta, su aceleración en los primeros metros es devastadora, especialmente cuando combina velocidad con cambios de dirección. Su finalización con ambas piernas (aunque es zurdo natural) es de clase mundial.",
  "attributes": {
    "Regate": 98,
    "Velocidad": 92,
    "Finalización": 97,
    "Pase": 95,
    "Visión de Juego": 99,
    "Tiro Libre": 94,
    "Control": 99,
    "Aceleración": 96
  }
}' WHERE id = 1;

-- ============================================================
-- ACTUALIZAR LEGADO (legacy)
-- ============================================================
UPDATE players SET legacy = '{
  "text": "El legado de Lionel Messi trasciende los números y los trofeos. Ha redefinido lo que significa ser un futbolista de élite, demostrando que el talento puro y la dedicación pueden superar cualquier limitación física.\\n\\nSu impacto en el FC Barcelona fue transformador, llevando al club a su época más exitosa y convirtiéndose en su máximo goleador histórico. Su posterior éxito con Argentina, culminando en la Copa del Mundo 2022, consolidó su estatus como el mejor jugador de todos los tiempos.\\n\\nMás allá del fútbol, Messi representa la humildad y el profesionalismo. Su fundación trabaja en educación y salud infantil, y es embajador de UNICEF. Su influencia ha inspirado a millones de jóvenes futbolistas en todo el mundo a perseguir sus sueños.",
  "quotes": [
    {
      "text": "No hay duda de que Messi es el mejor jugador del mundo y probablemente el mejor de todos los tiempos",
      "author": "Pep Guardiola"
    },
    {
      "text": "Messi es el Mozart del fútbol",
      "author": "Hristo Stoichkov"
    },
    {
      "text": "Cuando ves a Messi jugar, ves algo especial. Ves magia en el campo",
      "author": "Ronaldinho"
    }
  ]
}' WHERE id = 1;

-- ============================================================
-- ACTUALIZAR GALERÍA (gallery)
-- ============================================================
UPDATE players SET gallery = '[
  {
    "url": "../assets/images/messi-barcelona.jpg",
    "alt": "Lionel Messi celebrando un gol emblemático con la camiseta azulgrana del FC Barcelona - Era dorada en el Camp Nou",
    "caption": "Messi celebrando un gol con el Barcelona"
  },
  {
    "url": "../assets/images/messi-copa-mundial.jpg",
    "alt": "Lionel Messi levantando la Copa del Mundo FIFA 2022 en Qatar - Momento histórico de máxima gloria",
    "caption": "Messi levantando la Copa del Mundo 2022"
  },
  {
    "url": "../assets/images/messi-copa-america.jpg",
    "alt": "Lionel Messi celebrando con el trofeo de la Copa América 2021 en Brasil - Primer título con Argentina",
    "caption": "Messi con la Copa América 2021"
  },
  {
    "url": "../assets/images/messi-balon-oro.jpg",
    "alt": "Lionel Messi posando con su octavo Balón de Oro 2023 - Récord histórico de galardones individuales",
    "caption": "Messi con su octavo Balón de Oro"
  },
  {
    "url": "../assets/images/messi-psg.jpg",
    "alt": "Lionel Messi en acción con la camiseta del Paris Saint-Germain - Nueva etapa en su carrera",
    "caption": "Messi con el PSG"
  },
  {
    "url": "../assets/images/messi-inter-miami.jpg",
    "alt": "Lionel Messi celebrando con Inter Miami CF en la MLS - Impacto revolucionario en el fútbol estadounidense",
    "caption": "Messi con el Inter Miami"
  }
]' WHERE id = 1;

-- ============================================================
-- ACTUALIZAR LOGROS Y PALMARÉS (achievements)
-- ============================================================
UPDATE players SET achievements = '{
  "clubs": [
    {
      "name": "FC Barcelona",
      "logo": "../assets/images/barcelona-logo.png",
      "titles": [
        {"count": "10×", "name": "La Liga"},
        {"count": "7×", "name": "Copa del Rey"},
        {"count": "8×", "name": "Supercopa de España"},
        {"count": "4×", "name": "UEFA Champions League"},
        {"count": "3×", "name": "Supercopa de Europa"},
        {"count": "3×", "name": "Mundial de Clubes"}
      ]
    },
    {
      "name": "Paris Saint-Germain",
      "logo": "../assets/images/psg-logo.png",
      "titles": [
        {"count": "2×", "name": "Ligue 1"},
        {"count": "1×", "name": "Supercopa de Francia"}
      ]
    },
    {
      "name": "Inter Miami",
      "logo": "../assets/images/inter-miami-logo.png",
      "titles": [
        {"count": "1×", "name": "Leagues Cup"}
      ]
    }
  ],
  "national": {
    "name": "Selección Argentina",
    "logo": "../assets/images/argentina-logo.png",
    "titles": [
      {"count": "1×", "name": "Copa Mundial de la FIFA (2022)"},
      {"count": "1×", "name": "Copa América (2021)"},
      {"count": "1×", "name": "Finalissima (2022)"},
      {"count": "1×", "name": "Juegos Olímpicos - Medalla de Oro (2008)"},
      {"count": "1×", "name": "Mundial Sub-20 (2005)"}
    ]
  },
  "individual": [
    {"count": "8×", "name": "Balón de Oro"},
    {"count": "6×", "name": "Bota de Oro Europea"},
    {"count": "2×", "name": "Mejor Jugador de la FIFA"},
    {"count": "1×", "name": "Balón de Oro del Mundial (2022)"},
    {"count": "6×", "name": "Pichichi (Máximo goleador de La Liga)"},
    {"count": "1×", "name": "Laureus al Mejor Deportista del Año"},
    {"count": "12×", "name": "Equipo del Año de la UEFA"}
  ]
}' WHERE id = 1;

-- ============================================================
-- ACTUALIZAR ESTADÍSTICAS (stats)
-- ============================================================
UPDATE players SET stats = '{
  "total": {
    "appearances": 1069,
    "goals": 820,
    "assists": 379,
    "minutesPlayed": 89453
  },
  "club": {
    "appearances": 925,
    "goals": 709,
    "assists": 338
  },
  "nationalTeam": {
    "appearances": 187,
    "goals": 111,
    "assists": 56
  },
  "averages": {
    "goalsPerGame": 0.77,
    "assistsPerGame": 0.35,
    "minutesPerGoal": 109,
    "minutesPerGoalOrAssist": 75
  }
}' WHERE id = 1;

-- ============================================================
-- ACTUALIZAR ESTADÍSTICAS POR TEMPORADA (season_stats)
-- ============================================================
UPDATE players SET season_stats = '[
  {"season": "2023-24", "club": "Inter Miami", "appearances": 24, "goals": 25, "assists": 16},
  {"season": "2022-23", "club": "PSG", "appearances": 41, "goals": 21, "assists": 20},
  {"season": "2021-22", "club": "PSG", "appearances": 34, "goals": 11, "assists": 15},
  {"season": "2020-21", "club": "Barcelona", "appearances": 47, "goals": 38, "assists": 14},
  {"season": "2019-20", "club": "Barcelona", "appearances": 44, "goals": 31, "assists": 27},
  {"season": "2018-19", "club": "Barcelona", "appearances": 50, "goals": 51, "assists": 22},
  {"season": "2017-18", "club": "Barcelona", "appearances": 54, "goals": 45, "assists": 18},
  {"season": "2016-17", "club": "Barcelona", "appearances": 52, "goals": 54, "assists": 16},
  {"season": "2015-16", "club": "Barcelona", "appearances": 49, "goals": 41, "assists": 23},
  {"season": "2014-15", "club": "Barcelona", "appearances": 57, "goals": 58, "assists": 27},
  {"season": "2013-14", "club": "Barcelona", "appearances": 46, "goals": 41, "assists": 14},
  {"season": "2012-13", "club": "Barcelona", "appearances": 50, "goals": 60, "assists": 16},
  {"season": "2011-12", "club": "Barcelona", "appearances": 60, "goals": 73, "assists": 29},
  {"season": "2010-11", "club": "Barcelona", "appearances": 55, "goals": 53, "assists": 24},
  {"season": "2009-10", "club": "Barcelona", "appearances": 53, "goals": 47, "assists": 11},
  {"season": "2008-09", "club": "Barcelona", "appearances": 51, "goals": 38, "assists": 18},
  {"season": "2007-08", "club": "Barcelona", "appearances": 40, "goals": 16, "assists": 13},
  {"season": "2006-07", "club": "Barcelona", "appearances": 36, "goals": 17, "assists": 3},
  {"season": "2005-06", "club": "Barcelona", "appearances": 25, "goals": 8, "assists": 5},
  {"season": "2004-05", "club": "Barcelona", "appearances": 9, "goals": 1, "assists": 0}
]' WHERE id = 1;

-- ============================================================
-- ACTUALIZAR VIDEOS (videos)
-- ============================================================
UPDATE players SET videos = '[
  {"url": "https://www.youtube.com/watch?v=uYuUFhW7Vi8", "title": "Messi - Final Mundial 2022 Completa", "thumbnail": "../assets/images/video-messi-mundial.jpg"},
  {"url": "https://www.youtube.com/watch?v=5-mZA6MTaK4", "title": "Messi - Todos sus Goles Históricos", "thumbnail": "../assets/images/video-messi-hat-trick.jpg"},
  {"url": "https://www.youtube.com/watch?v=gWVEvyvJaYE", "title": "Messi - El Gol del Siglo vs Getafe", "thumbnail": "../assets/images/video-messi-gol-getafe.jpg"}
]' WHERE id = 1;

-- ============================================================
-- VERIFICACIÓN FINAL
-- ============================================================
SELECT 
  'Verificación UTF-8:' as tipo,
  SUBSTRING(playing_style, 1, 100) as texto
FROM players WHERE id = 1
UNION ALL
SELECT 
  'Legacy:',
  SUBSTRING(legacy, 1, 100)
FROM players WHERE id = 1
UNION ALL
SELECT 
  'Achievements:',
  SUBSTRING(achievements, 1, 100)
FROM players WHERE id = 1;
