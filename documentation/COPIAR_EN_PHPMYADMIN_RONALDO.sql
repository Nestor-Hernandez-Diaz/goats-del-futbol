-- ============================================================
-- CRISTIANO RONALDO - DATOS COMPLETOS
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
    "birthDate": "5 de febrero de 1985",
    "clubs": "Man United, Real Madrid, Juventus, Al-Nassr",
    "quote": "Tu amor me hace fuerte, tu odio me hace imparable."
  }',
  profile_image = '../assets/images/ronaldo-profile.png',
  profile_stats = '{
    "Nombre completo": "Cristiano Ronaldo dos Santos Aveiro",
    "Fecha de nacimiento": "5 de febrero de 1985",
    "Lugar de nacimiento": "Funchal, Madeira, Portugal",
    "Altura": "1,87 m",
    "Peso": "84 kg",
    "Pie dominante": "Derecho (ambidiestro)",
    "Posición principal": "Extremo / Delantero centro",
    "Dorsal habitual": "7"
  }',
  career_highlights = '[
    {"year": "2003", "event": "Debut en el Manchester United"},
    {"year": "2008", "event": "Primer Balón de Oro"},
    {"year": "2009", "event": "Fichaje récord por el Real Madrid"},
    {"year": "2014", "event": "Ganador de La Décima con Real Madrid"},
    {"year": "2016", "event": "Campeón de Europa con Portugal"},
    {"year": "2021", "event": "Máximo goleador histórico del fútbol"}
  ]'
WHERE id = 2;

-- ============================================================
-- ACTUALIZAR ESTILO DE JUEGO (playing_style)
-- ============================================================
UPDATE players SET playing_style = '{
  "description": "Cristiano Ronaldo es el ejemplo definitivo de la perfección física y mental aplicada al fútbol. Su dedicación al entrenamiento y preparación física lo ha mantenido en la élite durante más de dos décadas.\\n\\nSu juego aéreo es legendario, con una capacidad de salto vertical de más de 78cm y un tiempo de suspensión que desafía la física. Su remate de cabeza es considerado el mejor de la historia. Además, es ambidiestro de forma natural, lo que lo hace impredecible en el área.\\n\\nSu velocidad, potencia y técnica de tiro libre lo convierten en una amenaza constante. Ha evolucionado de extremo explosivo a delantero centro letal, demostrando una capacidad de adaptación excepcional. Su mentalidad ganadora y hambre de goles son incomparables.",
  "attributes": {
    "Remate": 98,
    "Velocidad": 94,
    "Salto": 99,
    "Potencia": 97,
    "Finalización": 98,
    "Tiro Libre": 93,
    "Físico": 99,
    "Mentalidad": 99
  }
}' WHERE id = 2;

-- ============================================================
-- ACTUALIZAR LEGADO (legacy)
-- ============================================================
UPDATE players SET legacy = '{
  "text": "Cristiano Ronaldo ha redefinido lo que significa ser un atleta profesional. Su dedicación obsesiva al entrenamiento, nutrición y recuperación ha establecido nuevos estándares en el deporte mundial.\\n\\nCon más de 890 goles oficiales, es el máximo goleador de la historia del fútbol profesional. Ha conquistado títulos en Inglaterra, España e Italia, demostrando su capacidad para dominar en cualquier liga. Sus 140 goles en Champions League son un récord que parece inalcanzable.\\n\\nMás allá de lo deportivo, CR7 es una marca global y un empresario exitoso. Su línea CR7 abarca hoteles, ropa, fragancias y más. Su influencia en redes sociales es la más grande del deporte, inspirando a millones a trabajar duro por sus sueños.",
  "quotes": [
    {
      "text": "No hay jugador más completo que Cristiano. Su dedicación es de otro mundo",
      "author": "Sir Alex Ferguson"
    },
    {
      "text": "Cristiano es un ganador nato. Su mentalidad es su mayor fortaleza",
      "author": "Zinedine Zidane"
    },
    {
      "text": "He jugado con grandes jugadores, pero ninguno como Cristiano. Es único",
      "author": "Wayne Rooney"
    }
  ]
}' WHERE id = 2;

-- ============================================================
-- ACTUALIZAR GALERÍA (gallery)
-- ============================================================
UPDATE players SET gallery = '[
  {
    "url": "../assets/images/ronaldo-real-madrid.jpg",
    "alt": "Cristiano Ronaldo celebrando un gol con el Real Madrid en el Santiago Bernabéu - Época dorada merengue",
    "caption": "CR7 celebrando con el Real Madrid"
  },
  {
    "url": "../assets/images/ronaldo-eurocopa.jpg",
    "alt": "Cristiano Ronaldo levantando la Eurocopa 2016 con Portugal - Capitán campeón de Europa",
    "caption": "Ronaldo campeón de la Eurocopa 2016"
  },
  {
    "url": "../assets/images/ronaldo-al-nassr.jpg",
    "alt": "Cristiano Ronaldo en Al-Nassr - Nueva etapa en Arabia Saudita",
    "caption": "CR7 en Al-Nassr"
  },
  {
    "url": "../assets/images/ronaldo-sporting.jpg",
    "alt": "Cristiano Ronaldo en sus inicios con Sporting Lisboa - El comienzo de una leyenda",
    "caption": "Ronaldo en Sporting Lisboa"
  },
  {
    "url": "../assets/images/ronaldo-manchester.jpg",
    "alt": "Cristiano Ronaldo en su etapa con Manchester United - Inicios de una leyenda",
    "caption": "CR7 en Manchester United"
  },
  {
    "url": "../assets/images/ronaldo-juventus.jpg",
    "alt": "Cristiano Ronaldo celebrando con la Juventus de Turín - Conquista de Italia",
    "caption": "Ronaldo con la Juventus"
  }
]' WHERE id = 2;

-- ============================================================
-- ACTUALIZAR LOGROS Y PALMARÉS (achievements)
-- ============================================================
UPDATE players SET achievements = '{
  "clubs": [
    {
      "name": "Manchester United",
      "logo": "../assets/images/manchester-united-logo.png",
      "titles": [
        {"count": "3×", "name": "Premier League"},
        {"count": "1×", "name": "FA Cup"},
        {"count": "2×", "name": "Copa de la Liga"},
        {"count": "1×", "name": "UEFA Champions League"},
        {"count": "1×", "name": "Mundial de Clubes"}
      ]
    },
    {
      "name": "Real Madrid",
      "logo": "../assets/images/real-madrid-logo.png",
      "titles": [
        {"count": "2×", "name": "La Liga"},
        {"count": "2×", "name": "Copa del Rey"},
        {"count": "2×", "name": "Supercopa de España"},
        {"count": "4×", "name": "UEFA Champions League"},
        {"count": "3×", "name": "Supercopa de Europa"},
        {"count": "4×", "name": "Mundial de Clubes"}
      ]
    },
    {
      "name": "Juventus",
      "logo": "../assets/images/juventus-logo.png",
      "titles": [
        {"count": "2×", "name": "Serie A"},
        {"count": "2×", "name": "Supercopa de Italia"},
        {"count": "1×", "name": "Copa Italia"}
      ]
    }
  ],
  "national": {
    "name": "Selección de Portugal",
    "logo": "../assets/images/portugal-logo.png",
    "titles": [
      {"count": "1×", "name": "Eurocopa (2016)"},
      {"count": "1×", "name": "UEFA Nations League (2019)"}
    ]
  },
  "individual": [
    {"count": "5×", "name": "Balón de Oro"},
    {"count": "4×", "name": "Bota de Oro Europea"},
    {"count": "5×", "name": "FIFA The Best"},
    {"count": "4×", "name": "Pichichi La Liga"},
    {"count": "7×", "name": "Máximo Goleador Champions League"},
    {"count": "3×", "name": "Jugador del Año UEFA"},
    {"count": "11×", "name": "Equipo del Año de la UEFA"}
  ]
}' WHERE id = 2;

-- ============================================================
-- ACTUALIZAR ESTADÍSTICAS (stats)
-- ============================================================
UPDATE players SET stats = '{
  "total": {
    "appearances": 1237,
    "goals": 895,
    "assists": 255,
    "minutesPlayed": 105840
  },
  "club": {
    "appearances": 1042,
    "goals": 769,
    "assists": 229
  },
  "nationalTeam": {
    "appearances": 212,
    "goals": 130,
    "assists": 26
  },
  "averages": {
    "goalsPerGame": 0.72,
    "assistsPerGame": 0.21,
    "minutesPerGoal": 118,
    "minutesPerGoalOrAssist": 92
  }
}' WHERE id = 2;

-- ============================================================
-- ACTUALIZAR ESTADÍSTICAS POR TEMPORADA (season_stats)
-- ============================================================
UPDATE players SET season_stats = '[
  {"season": "2023-24", "club": "Al-Nassr", "appearances": 45, "goals": 44, "assists": 13},
  {"season": "2022-23", "club": "Al-Nassr", "appearances": 16, "goals": 14, "assists": 2},
  {"season": "2021-22", "club": "Manchester United", "appearances": 39, "goals": 24, "assists": 3},
  {"season": "2020-21", "club": "Juventus", "appearances": 44, "goals": 36, "assists": 4},
  {"season": "2019-20", "club": "Juventus", "appearances": 46, "goals": 37, "assists": 7},
  {"season": "2018-19", "club": "Juventus", "appearances": 43, "goals": 28, "assists": 10},
  {"season": "2017-18", "club": "Real Madrid", "appearances": 44, "goals": 44, "assists": 8},
  {"season": "2016-17", "club": "Real Madrid", "appearances": 46, "goals": 42, "assists": 12},
  {"season": "2015-16", "club": "Real Madrid", "appearances": 48, "goals": 51, "assists": 15},
  {"season": "2014-15", "club": "Real Madrid", "appearances": 54, "goals": 61, "assists": 22},
  {"season": "2013-14", "club": "Real Madrid", "appearances": 47, "goals": 51, "assists": 13},
  {"season": "2012-13", "club": "Real Madrid", "appearances": 55, "goals": 55, "assists": 12},
  {"season": "2011-12", "club": "Real Madrid", "appearances": 55, "goals": 60, "assists": 15},
  {"season": "2010-11", "club": "Real Madrid", "appearances": 54, "goals": 53, "assists": 13},
  {"season": "2009-10", "club": "Real Madrid", "appearances": 35, "goals": 33, "assists": 9},
  {"season": "2008-09", "club": "Manchester United", "appearances": 53, "goals": 26, "assists": 7},
  {"season": "2007-08", "club": "Manchester United", "appearances": 49, "goals": 42, "assists": 8},
  {"season": "2006-07", "club": "Manchester United", "appearances": 53, "goals": 23, "assists": 18},
  {"season": "2005-06", "club": "Manchester United", "appearances": 47, "goals": 12, "assists": 9},
  {"season": "2004-05", "club": "Manchester United", "appearances": 50, "goals": 9, "assists": 11}
]' WHERE id = 2;

-- ============================================================
-- ACTUALIZAR VIDEOS (videos)
-- ============================================================
UPDATE players SET videos = '[
  {"url": "https://www.youtube.com/watch?v=cF5LCn3sXsw", "title": "Cristiano Ronaldo - Todos los Goles en Champions League", "thumbnail": "../assets/images/video-ronaldo-chilena.jpg"},
  {"url": "https://www.youtube.com/watch?v=XrSzT85GRWY", "title": "CR7 - El Arte del Juego Aéreo", "thumbnail": "../assets/images/video-ronaldo-hat-trick.jpg"},
  {"url": "https://www.youtube.com/watch?v=gCZ6l4KDZHs", "title": "Cristiano Ronaldo - Evolución 2003-2024", "thumbnail": "../assets/images/video-ronaldo-eurocopa.jpg"}
]' WHERE id = 2;

-- ============================================================
-- VERIFICACIÓN FINAL
-- ============================================================
SELECT 
  'Verificación Ronaldo UTF-8:' as tipo,
  SUBSTRING(playing_style, 1, 100) as texto
FROM players WHERE id = 2
UNION ALL
SELECT 
  'Legacy:',
  SUBSTRING(legacy, 1, 100)
FROM players WHERE id = 2
UNION ALL
SELECT 
  'Achievements:',
  SUBSTRING(achievements, 1, 100)
FROM players WHERE id = 2;
