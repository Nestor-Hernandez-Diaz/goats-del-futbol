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

-- Insertar logros para Messi (8 Balones de Oro)
INSERT INTO achievements (player_id, title, description, year, type, organization) VALUES
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Balón de Oro', 'Mejor jugador del mundo', 2009, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Balón de Oro', 'Mejor jugador del mundo', 2010, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Balón de Oro', 'Mejor jugador del mundo', 2011, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Balón de Oro', 'Mejor jugador del mundo', 2012, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Balón de Oro', 'Mejor jugador del mundo', 2015, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Balón de Oro', 'Mejor jugador del mundo', 2019, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Balón de Oro', 'Mejor jugador del mundo', 2021, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Balón de Oro', 'Mejor jugador del mundo', 2023, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Copa del Mundo FIFA', 'Campeón del mundo con Argentina', 2022, 'NATIONAL_TEAM', 'FIFA'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Copa América', 'Campeón con Argentina', 2021, 'NATIONAL_TEAM', 'CONMEBOL'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Champions League', 'Campeón con Barcelona', 2006, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Champions League', 'Campeón con Barcelona', 2009, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Champions League', 'Campeón con Barcelona', 2011, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Lionel Messi'), 'Champions League', 'Campeón con Barcelona', 2015, 'CLUB', 'UEFA');

-- Insertar logros para Cristiano Ronaldo (5 Balones de Oro, 5 Champions)
INSERT INTO achievements (player_id, title, description, year, type, organization) VALUES
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Balón de Oro', 'Mejor jugador del mundo', 2008, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Balón de Oro', 'Mejor jugador del mundo', 2013, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Balón de Oro', 'Mejor jugador del mundo', 2014, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Balón de Oro', 'Mejor jugador del mundo', 2016, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Balón de Oro', 'Mejor jugador del mundo', 2017, 'INDIVIDUAL', 'France Football'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Champions League', 'Campeón con Manchester United', 2008, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Champions League', 'Campeón con Real Madrid', 2014, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Champions League', 'Campeón con Real Madrid', 2016, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Champions League', 'Campeón con Real Madrid', 2017, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Champions League', 'Campeón con Real Madrid', 2018, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 'Eurocopa', 'Campeón con Portugal', 2016, 'NATIONAL_TEAM', 'UEFA');

-- Insertar logros para Neymar
INSERT INTO achievements (player_id, title, description, year, type, organization) VALUES
((SELECT id FROM players WHERE name = 'Neymar Jr'), 'Champions League', 'Campeón con Barcelona', 2015, 'CLUB', 'UEFA'),
((SELECT id FROM players WHERE name = 'Neymar Jr'), 'Copa América', 'Campeón con Brasil (Sub-20)', 2011, 'NATIONAL_TEAM', 'CONMEBOL'),
((SELECT id FROM players WHERE name = 'Neymar Jr'), 'Juegos Olímpicos', 'Medalla de Oro con Brasil', 2016, 'NATIONAL_TEAM', 'COI'),
((SELECT id FROM players WHERE name = 'Neymar Jr'), 'Copa Libertadores', 'Campeón con Santos', 2011, 'CLUB', 'CONMEBOL');
