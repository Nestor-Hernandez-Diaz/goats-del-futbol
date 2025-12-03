-- ============================================================
-- NEYMAR JR - DATOS COMPLETOS
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
    "birthDate": "5 de febrero de 1992",
    "clubs": "Santos, Barcelona, PSG, Al-Hilal",
    "quote": "Soy feliz siendo quien soy, jugando el fútbol que me gusta."
  }',
  profile_image = '../assets/images/neymar-profile.png',
  profile_stats = '{
    "Nombre completo": "Neymar da Silva Santos Júnior",
    "Fecha de nacimiento": "5 de febrero de 1992",
    "Lugar de nacimiento": "Mogi das Cruzes, Brasil",
    "Altura": "1,75 m",
    "Peso": "68 kg",
    "Pie dominante": "Derecho",
    "Posición principal": "Extremo izquierdo / Mediapunta",
    "Dorsal habitual": "10 / 11"
  }',
  career_highlights = '[
    {"year": "2011", "event": "Revelación con Santos FC"},
    {"year": "2013", "event": "Fichaje por FC Barcelona"},
    {"year": "2015", "event": "Campeón de Champions con Barcelona"},
    {"year": "2016", "event": "Medalla de Oro Olímpica con Brasil"},
    {"year": "2017", "event": "Transferencia récord mundial al PSG"},
    {"year": "2019", "event": "Campeón de Copa América"}
  ]'
WHERE id = 3;

-- ============================================================
-- ACTUALIZAR ESTILO DE JUEGO (playing_style)
-- ============================================================
UPDATE players SET playing_style = '{
  "description": "Neymar Jr representa el ''Jogo Bonito'' brasileño en su máxima expresión. Su estilo de juego combina creatividad, técnica excepcional y un repertorio infinito de regates y fintas que han hecho vibrar a millones de aficionados.\\n\\nSu habilidad para el uno contra uno es sublime. Con el balón pegado al pie, es capaz de desequilibrar cualquier defensa con cambios de ritmo, amagues y túneles. Su visión de juego le permite crear ocasiones desde cualquier posición ofensiva, actuando tanto de extremo como de mediapunta.\\n\\nAunque criticado por su tendencia a caer en el área, no se puede negar que es uno de los futbolistas más faltado del mundo debido a su peligrosidad. Su velocidad, agilidad y capacidad para ejecutar jugadas espectaculares lo convierten en un jugador único. Además, es un excelente cobrador de faltas y penales.",
  "attributes": {
    "Regate": 97,
    "Velocidad": 93,
    "Creatividad": 98,
    "Técnica": 96,
    "Pase": 91,
    "Finalización": 89,
    "Tiro Libre": 92,
    "Agilidad": 97
  }
}' WHERE id = 3;

-- ============================================================
-- ACTUALIZAR LEGADO (legacy)
-- ============================================================
UPDATE players SET legacy = '{
  "text": "Neymar Jr es el heredero de la gran tradición del fútbol brasileño. Desde sus inicios en Santos, donde siguió los pasos de Pelé, ha llevado la bandera del Jogo Bonito por el mundo.\\n\\nSu transferencia al FC Barcelona por 57 millones de euros en 2013 y posteriormente al PSG por 222 millones en 2017 (récord mundial) demuestran su valor de mercado. En Barcelona formó parte del tridente MSN junto a Messi y Suárez, considerado uno de los mejores ataques de la historia.\\n\\nMás allá del fútbol, Neymar es un icono global del entretenimiento. Su presencia en redes sociales, videojuegos y cultura popular lo han convertido en una de las figuras más influyentes del deporte mundial. Su Instituto Neymar Jr ayuda a niños desfavorecidos en Brasil, demostrando su compromiso social.",
  "quotes": [
    {
      "text": "Neymar tiene un talento único. Es el mejor jugador brasileño de su generación",
      "author": "Pelé"
    },
    {
      "text": "Jugar con Neymar es un placer. Su creatividad no tiene límites",
      "author": "Lionel Messi"
    },
    {
      "text": "Neymar es puro fútbol brasileño. Es magia con el balón",
      "author": "Ronaldinho"
    }
  ]
}' WHERE id = 3;

-- ============================================================
-- ACTUALIZAR GALERÍA (gallery)
-- ============================================================
UPDATE players SET gallery = '[
  {
    "url": "../assets/images/neymar-barcelona.jpg",
    "alt": "Neymar Jr celebrando con el FC Barcelona - Parte del histórico tridente MSN",
    "caption": "Neymar con el Barcelona"
  },
  {
    "url": "../assets/images/neymar-psg.jpg",
    "alt": "Neymar Jr en acción con el Paris Saint-Germain - Transferencia récord mundial",
    "caption": "Neymar con el PSG"
  },
  {
    "url": "../assets/images/neymar-brasil.jpg",
    "alt": "Neymar Jr con la camiseta de la Selección Brasileña - Estrella de la Canarinha",
    "caption": "Neymar con Brasil"
  },
  {
    "url": "../assets/images/neymar-mundial-clubes.jpg",
    "alt": "Neymar Jr celebrando título del Mundial de Clubes con Barcelona - Conquista internacional",
    "caption": "Neymar campeón Mundial de Clubes"
  },
  {
    "url": "../assets/images/neymar-santos.jpg",
    "alt": "Neymar Jr en sus inicios con Santos FC - Revelación del fútbol brasileño",
    "caption": "Neymar en Santos FC"
  },
  {
    "url": "../assets/images/neymar-al-hilal.jpg",
    "alt": "Neymar Jr en Al-Hilal - Nueva aventura en Arabia Saudita",
    "caption": "Neymar en Al-Hilal"
  }
]' WHERE id = 3;

-- ============================================================
-- ACTUALIZAR LOGROS Y PALMARÉS (achievements)
-- ============================================================
UPDATE players SET achievements = '{
  "clubs": [
    {
      "name": "Santos FC",
      "logo": "../assets/images/santos-logo.png",
      "titles": [
        {"count": "1×", "name": "Copa Libertadores"},
        {"count": "1×", "name": "Copa do Brasil"},
        {"count": "3×", "name": "Campeonato Paulista"}
      ]
    },
    {
      "name": "FC Barcelona",
      "logo": "../assets/images/barcelona-logo.png",
      "titles": [
        {"count": "2×", "name": "La Liga"},
        {"count": "3×", "name": "Copa del Rey"},
        {"count": "2×", "name": "Supercopa de España"},
        {"count": "1×", "name": "UEFA Champions League"},
        {"count": "1×", "name": "Supercopa de Europa"},
        {"count": "1×", "name": "Mundial de Clubes"}
      ]
    },
    {
      "name": "Paris Saint-Germain",
      "logo": "../assets/images/psg-logo.png",
      "titles": [
        {"count": "5×", "name": "Ligue 1"},
        {"count": "3×", "name": "Copa de Francia"},
        {"count": "3×", "name": "Copa de la Liga"},
        {"count": "4×", "name": "Supercopa de Francia"}
      ]
    }
  ],
  "national": {
    "name": "Selección de Brasil",
    "logo": "../assets/images/brasil-logo.png",
    "titles": [
      {"count": "1×", "name": "Copa América (2019)"},
      {"count": "1×", "name": "Copa Confederaciones (2013)"},
      {"count": "1×", "name": "Juegos Olímpicos - Medalla de Oro (2016)"},
      {"count": "1×", "name": "Campeonato Sudamericano Sub-20 (2011)"}
    ]
  },
  "individual": [
    {"count": "2×", "name": "Jugador Sudamericano del Año"},
    {"count": "1×", "name": "Bota de Bronce Mundial (2014)"},
    {"count": "3×", "name": "Mejor Jugador Ligue 1"},
    {"count": "1×", "name": "Puskas Award"},
    {"count": "2×", "name": "Samba de Oro"},
    {"count": "1×", "name": "Balón de Oro Copa Confederaciones"},
    {"count": "6×", "name": "Equipo del Año de la UEFA"}
  ]
}' WHERE id = 3;

-- ============================================================
-- ACTUALIZAR ESTADÍSTICAS (stats)
-- ============================================================
UPDATE players SET stats = '{
  "total": {
    "appearances": 733,
    "goals": 436,
    "assists": 259,
    "minutesPlayed": 59420
  },
  "club": {
    "appearances": 606,
    "goals": 358,
    "assists": 225
  },
  "nationalTeam": {
    "appearances": 127,
    "goals": 78,
    "assists": 34
  },
  "averages": {
    "goalsPerGame": 0.59,
    "assistsPerGame": 0.35,
    "minutesPerGoal": 136,
    "minutesPerGoalOrAssist": 85
  }
}' WHERE id = 3;

-- ============================================================
-- ACTUALIZAR ESTADÍSTICAS POR TEMPORADA (season_stats)
-- ============================================================
UPDATE players SET season_stats = '[
  {"season": "2023-24", "club": "Al-Hilal", "appearances": 7, "goals": 1, "assists": 3},
  {"season": "2022-23", "club": "PSG", "appearances": 29, "goals": 18, "assists": 17},
  {"season": "2021-22", "club": "PSG", "appearances": 28, "goals": 13, "assists": 8},
  {"season": "2020-21", "club": "PSG", "appearances": 30, "goals": 17, "assists": 10},
  {"season": "2019-20", "club": "PSG", "appearances": 27, "goals": 19, "assists": 12},
  {"season": "2018-19", "club": "PSG", "appearances": 28, "goals": 23, "assists": 13},
  {"season": "2017-18", "club": "PSG", "appearances": 30, "goals": 28, "assists": 16},
  {"season": "2016-17", "club": "Barcelona", "appearances": 45, "goals": 20, "assists": 27},
  {"season": "2015-16", "club": "Barcelona", "appearances": 49, "goals": 31, "assists": 30},
  {"season": "2014-15", "club": "Barcelona", "appearances": 51, "goals": 39, "assists": 10},
  {"season": "2013-14", "club": "Barcelona", "appearances": 41, "goals": 15, "assists": 17},
  {"season": "2012-13", "club": "Santos", "appearances": 47, "goals": 20, "assists": 18},
  {"season": "2011-12", "club": "Santos", "appearances": 48, "goals": 43, "assists": 18},
  {"season": "2010-11", "club": "Santos", "appearances": 47, "goals": 24, "assists": 11},
  {"season": "2009-10", "club": "Santos", "appearances": 42, "goals": 14, "assists": 8}
]' WHERE id = 3;

-- ============================================================
-- ACTUALIZAR VIDEOS (videos)
-- ============================================================
UPDATE players SET videos = '[
  {"url": "https://www.youtube.com/watch?v=lRjgHZtYmM4", "title": "Neymar Jr - El Arte del Regate", "thumbnail": "../assets/images/video-neymar-santos.jpg"},
  {"url": "https://www.youtube.com/watch?v=5A6vHzT4kBY", "title": "Neymar Jr - Mejores Jugadas y Goles", "thumbnail": "../assets/images/video-neymar-remontada.jpg"},
  {"url": "https://www.youtube.com/watch?v=MX7h5A8qvuY", "title": "Neymar Jr - Jogo Bonito", "thumbnail": "../assets/images/video-neymar-olimpiadas.jpg"}
]' WHERE id = 3;

-- ============================================================
-- VERIFICACIÓN FINAL
-- ============================================================
SELECT 
  'Verificación Neymar UTF-8:' as tipo,
  SUBSTRING(playing_style, 1, 100) as texto
FROM players WHERE id = 3
UNION ALL
SELECT 
  'Legacy:',
  SUBSTRING(legacy, 1, 100)
FROM players WHERE id = 3
UNION ALL
SELECT 
  'Achievements:',
  SUBSTRING(achievements, 1, 100)
FROM players WHERE id = 3;
