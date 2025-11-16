-- Migration V4: Create achievements table
-- Tabla de logros y competiciones ganadas

CREATE TABLE IF NOT EXISTS achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    year INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    organization VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_achievements_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    
    INDEX idx_player_id (player_id),
    INDEX idx_year (year),
    INDEX idx_type (type),
    INDEX idx_player_year (player_id, year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar logros para Messi
INSERT INTO achievements (player_id, title, description, year, type, organization, created_at)
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2009, 'INDIVIDUAL', 'France Football', CURRENT_TIMESTAMP
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2010, 'INDIVIDUAL', 'France Football', CURRENT_TIMESTAMP
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2011, 'INDIVIDUAL', 'France Football', CURRENT_TIMESTAMP
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2012, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2015, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2019, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2021, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2023, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Copa Mundial FIFA', 'Campeón del Mundo', 2022, 'NATIONAL_TEAM', 'FIFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Copa América', 'Campeón de Sudamérica', 2021, 'NATIONAL_TEAM', 'CONMEBOL'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2009, 'CLUB', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2011, 'CLUB', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Lionel Messi';

-- Insertar logros para Cristiano
INSERT INTO achievements (player_id, title, description, year, type, organization, created_at)
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2008, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2013, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2014, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2016, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2017, 'INDIVIDUAL', 'France Football'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2008, 'CLUB', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2014, 'CLUB', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2016, 'CLUB', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2017, 'CLUB', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2018, 'CLUB', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Eurocopa', 'Campeón de Europa', 2016, 'NATIONAL_TEAM', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Cristiano Ronaldo';

-- Insertar logros para Neymar
INSERT INTO achievements (player_id, title, description, year, type, organization, created_at)
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2015, 'CLUB', 'UEFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Neymar Jr'
UNION ALL
SELECT p.id, 'Copa América', 'Campeón de Sudamérica', 2013, 'NATIONAL_TEAM', 'CONMEBOL'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Neymar Jr'
UNION ALL
SELECT p.id, 'Medalla de Oro Olímpica', 'Campeón Olímpico', 2016, 'NATIONAL_TEAM', 'FIFA'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Neymar Jr'
UNION ALL
SELECT p.id, 'Bota de Oro Europea', 'Máximo goleador de Europa', 2015, 'INDIVIDUAL', 'ESM'
, CURRENT_TIMESTAMP FROM players p WHERE p.name = 'Neymar Jr';

