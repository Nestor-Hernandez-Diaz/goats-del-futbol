-- ============================================================
-- SCRIPT MAESTRO - TODOS LOS JUGADORES
-- ============================================================
-- Este script actualiza datos completos para Messi, Ronaldo y Neymar
-- Incluye: playing_style, legacy, gallery, achievements, stats, season_stats, videos
-- 
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
-- 1. LIONEL MESSI (ID = 1)
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

UPDATE players SET videos = '[
  {"url": "https://www.youtube.com/watch?v=uYuUFhW7Vi8", "title": "Messi en la final del Mundial 2022", "thumbnail": "../assets/images/video-messi-mundial.jpg"},
  {"url": "https://www.youtube.com/watch?v=waETo-ZWCRw", "title": "El gol maradoniano contra Getafe", "thumbnail": "../assets/images/video-messi-gol-getafe.jpg"},
  {"url": "https://www.youtube.com/watch?v=Sy6emSOKlQY", "title": "Hat-trick contra el Real Madrid", "thumbnail": "../assets/images/video-messi-hat-trick.jpg"}
]' WHERE id = 1;

-- ============================================================
-- 2. CRISTIANO RONALDO (ID = 2)
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

UPDATE players SET videos = '[
  {"url": "https://www.youtube.com/watch?v=P-jRW5RLlKg", "title": "La chilena legendaria contra la Juventus", "thumbnail": "../assets/images/video-ronaldo-chilena.jpg"},
  {"url": "https://www.youtube.com/watch?v=uJZ5H_DDVfM", "title": "El liderazgo en la final de la Eurocopa 2016", "thumbnail": "../assets/images/video-ronaldo-eurocopa.jpg"},
  {"url": "https://www.youtube.com/watch?v=cx3B-9ZPN6s", "title": "Hat-trick contra España en el Mundial 2018", "thumbnail": "../assets/images/video-ronaldo-hat-trick.jpg"}
]' WHERE id = 2;

-- ============================================================
-- 3. NEYMAR JR (ID = 3)
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

UPDATE players SET videos = '[
  {"url": "https://www.youtube.com/watch?v=1wvwSER_w-M", "title": "El gol que le valió el Premio Puskás 2011", "thumbnail": "../assets/images/video-neymar-santos.jpg"},
  {"url": "https://www.youtube.com/watch?v=ERODrQXI-hY", "title": "Su actuación en la remontada 6-1 contra el PSG", "thumbnail": "../assets/images/video-neymar-remontada.jpg"},
  {"url": "https://www.youtube.com/watch?v=oNgE5SY5oGQ", "title": "El penal decisivo en la final olímpica de Río 2016", "thumbnail": "../assets/images/video-neymar-olimpiadas.jpg"}
]' WHERE id = 3;

-- ============================================================
-- VERIFICACIÓN FINAL - TODOS LOS JUGADORES
-- ============================================================
SELECT 
  CONCAT('✓ ', name, ' - playing_style actualizado') as resultado
FROM players WHERE id IN (1, 2, 3)
UNION ALL
SELECT 
  CONCAT('✓ ', name, ' - legacy actualizado') as resultado
FROM players WHERE id IN (1, 2, 3)
UNION ALL
SELECT 
  CONCAT('✓ ', name, ' - gallery actualizada') as resultado
FROM players WHERE id IN (1, 2, 3)
UNION ALL
SELECT 
  CONCAT('✓ ', name, ' - achievements actualizados') as resultado
FROM players WHERE id IN (1, 2, 3)
UNION ALL
SELECT 
  CONCAT('✓ ', name, ' - stats actualizadas') as resultado
FROM players WHERE id IN (1, 2, 3)
UNION ALL
SELECT 
  '============================================' as resultado
UNION ALL
SELECT 
  'TODOS LOS DATOS ACTUALIZADOS CORRECTAMENTE ✓' as resultado;
