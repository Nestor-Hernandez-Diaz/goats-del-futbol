-- Migration V3: Create player_stats table
-- Tabla de estadísticas detalladas de jugadores

CREATE TABLE IF NOT EXISTS player_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL UNIQUE,
    goals INT NOT NULL DEFAULT 0,
    assists INT NOT NULL DEFAULT 0,
    matches_played INT NOT NULL DEFAULT 0,
    trophies INT NOT NULL DEFAULT 0,
    yellow_cards INT NOT NULL DEFAULT 0,
    red_cards INT NOT NULL DEFAULT 0,
    minutes_played DOUBLE DEFAULT 0.0,
    ballon_d_or_wins INT DEFAULT 0,
    champions_league_wins INT DEFAULT 0,
    world_cup_wins INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_player_stats_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    
    INDEX idx_goals (goals),
    INDEX idx_assists (assists),
    INDEX idx_trophies (trophies),
    INDEX idx_ballon_dor (ballon_d_or_wins)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar estadísticas para los jugadores existentes (Messi, Cristiano, Neymar)

INSERT INTO player_stats (player_id, goals, assists, matches_played, trophies, yellow_cards, red_cards, 
                         minutes_played, ballon_d_or_wins, champions_league_wins, world_cup_wins)
SELECT 
    p.id,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 820
        WHEN p.name = 'Cristiano Ronaldo' THEN 895
        WHEN p.name = 'Neymar Jr' THEN 436
        ELSE 0
    END as goals,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 375
        WHEN p.name = 'Cristiano Ronaldo' THEN 255
        WHEN p.name = 'Neymar Jr' THEN 251
        ELSE 0
    END as assists,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 1038
        WHEN p.name = 'Cristiano Ronaldo' THEN 1203
        WHEN p.name = 'Neymar Jr' THEN 729
        ELSE 0
    END as matches_played,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 44
        WHEN p.name = 'Cristiano Ronaldo' THEN 35
        WHEN p.name = 'Neymar Jr' THEN 32
        ELSE 0
    END as trophies,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 89
        WHEN p.name = 'Cristiano Ronaldo' THEN 121
        WHEN p.name = 'Neymar Jr' THEN 107
        ELSE 0
    END as yellow_cards,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 4
        WHEN p.name = 'Cristiano Ronaldo' THEN 11
        WHEN p.name = 'Neymar Jr' THEN 7
        ELSE 0
    END as red_cards,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 87450.0
        WHEN p.name = 'Cristiano Ronaldo' THEN 101230.0
        WHEN p.name = 'Neymar Jr' THEN 62180.0
        ELSE 0.0
    END as minutes_played,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 8
        WHEN p.name = 'Cristiano Ronaldo' THEN 5
        WHEN p.name = 'Neymar Jr' THEN 0
        ELSE 0
    END as ballon_d_or_wins,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 4
        WHEN p.name = 'Cristiano Ronaldo' THEN 5
        WHEN p.name = 'Neymar Jr' THEN 1
        ELSE 0
    END as champions_league_wins,
    CASE 
        WHEN p.name = 'Lionel Messi' THEN 1
        WHEN p.name = 'Cristiano Ronaldo' THEN 0
        WHEN p.name = 'Neymar Jr' THEN 0
        ELSE 0
    END as world_cup_wins
FROM players p
WHERE NOT EXISTS (SELECT 1 FROM player_stats ps WHERE ps.player_id = p.id);
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
INSERT INTO achievements (player_id, title, description, year, type, organization)
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2009, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2010, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2011, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2012, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2015, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2019, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2021, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2023, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Copa Mundial FIFA', 'Campeón del Mundo', 2022, 'NATIONAL_TEAM', 'FIFA'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'Copa América', 'Campeón de Sudamérica', 2021, 'NATIONAL_TEAM', 'CONMEBOL'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2009, 'CLUB', 'UEFA'
FROM players p WHERE p.name = 'Lionel Messi'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2011, 'CLUB', 'UEFA'
FROM players p WHERE p.name = 'Lionel Messi';

-- Insertar logros para Cristiano
INSERT INTO achievements (player_id, title, description, year, type, organization)
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2008, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2013, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2014, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2016, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Balón de Oro', 'Mejor jugador del mundo', 2017, 'INDIVIDUAL', 'France Football'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2008, 'CLUB', 'UEFA'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2014, 'CLUB', 'UEFA'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2016, 'CLUB', 'UEFA'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2017, 'CLUB', 'UEFA'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2018, 'CLUB', 'UEFA'
FROM players p WHERE p.name = 'Cristiano Ronaldo'
UNION ALL
SELECT p.id, 'Eurocopa', 'Campeón de Europa', 2016, 'NATIONAL_TEAM', 'UEFA'
FROM players p WHERE p.name = 'Cristiano Ronaldo';

-- Insertar logros para Neymar
INSERT INTO achievements (player_id, title, description, year, type, organization)
SELECT p.id, 'UEFA Champions League', 'Campeón de Europa', 2015, 'CLUB', 'UEFA'
FROM players p WHERE p.name = 'Neymar Jr'
UNION ALL
SELECT p.id, 'Copa América', 'Campeón de Sudamérica', 2013, 'NATIONAL_TEAM', 'CONMEBOL'
FROM players p WHERE p.name = 'Neymar Jr'
UNION ALL
SELECT p.id, 'Medalla de Oro Olímpica', 'Campeón Olímpico', 2016, 'NATIONAL_TEAM', 'FIFA'
FROM players p WHERE p.name = 'Neymar Jr'
UNION ALL
SELECT p.id, 'Bota de Oro Europea', 'Máximo goleador de Europa', 2015, 'INDIVIDUAL', 'ESM'
FROM players p WHERE p.name = 'Neymar Jr';
-- Migration V5: Create comments table
-- Tabla de comentarios con sistema de moderación

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    moderation_reason VARCHAR(500),
    moderated_by BIGINT,
    moderated_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_moderator FOREIGN KEY (moderated_by) REFERENCES users(id) ON DELETE SET NULL,
    
    INDEX idx_user_id (user_id),
    INDEX idx_player_id (player_id),
    INDEX idx_status (status),
    INDEX idx_player_status (player_id, status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar comentarios de ejemplo (aprobados automáticamente para demo)
INSERT INTO comments (user_id, player_id, content, status, created_at)
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Lionel Messi' LIMIT 1),
    '¡El mejor jugador de todos los tiempos! Su visión de juego es incomparable.',
    'APPROVED',
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)
FROM users u 
WHERE u.username = 'admin'
UNION ALL
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Cristiano Ronaldo' LIMIT 1),
    'Una máquina de goles. Su mentalidad ganadora es inspiradora.',
    'APPROVED',
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 25) DAY)
FROM users u 
WHERE u.username = 'admin'
UNION ALL
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Neymar Jr' LIMIT 1),
    'El jugador más talentoso de Brasil. Su técnica es espectacular.',
    'APPROVED',
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 20) DAY)
FROM users u 
WHERE u.username = 'admin'
UNION ALL
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Lionel Messi' LIMIT 1),
    'Nadie puede hacer lo que hace Messi con el balón. Pura magia.',
    'APPROVED',
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 15) DAY)
FROM users u 
WHERE u.username IN (SELECT username FROM users WHERE username != 'admin' LIMIT 1)
UNION ALL
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Cristiano Ronaldo' LIMIT 1),
    'El trabajador más duro del fútbol. Un ejemplo para todos.',
    'APPROVED',
    DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 10) DAY)
FROM users u 
WHERE u.username IN (SELECT username FROM users WHERE username != 'admin' LIMIT 1);
-- Migration V6: Create subscriptions table
-- Tabla de suscripciones de usuarios a jugadores

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    subscribed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unsubscribed_at TIMESTAMP NULL,
    
    CONSTRAINT fk_subscriptions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_subscriptions_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_player UNIQUE (user_id, player_id),
    
    INDEX idx_user_id (user_id),
    INDEX idx_player_id (player_id),
    INDEX idx_active (active),
    INDEX idx_user_active (user_id, active),
    INDEX idx_player_active (player_id, active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar suscripciones de ejemplo
INSERT INTO subscriptions (user_id, player_id, active, notifications_enabled)
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Lionel Messi' LIMIT 1),
    TRUE,
    TRUE
FROM users u 
WHERE u.username = 'admin'
UNION ALL
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Cristiano Ronaldo' LIMIT 1),
    TRUE,
    TRUE
FROM users u 
WHERE u.username = 'admin'
UNION ALL
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Neymar Jr' LIMIT 1),
    TRUE,
    FALSE
FROM users u 
WHERE u.username = 'admin';

-- Suscripciones de otros usuarios si existen
INSERT INTO subscriptions (user_id, player_id, active, notifications_enabled)
SELECT 
    u.id,
    (SELECT id FROM players WHERE name = 'Lionel Messi' LIMIT 1),
    TRUE,
    TRUE
FROM users u 
WHERE u.username != 'admin' 
AND u.id NOT IN (SELECT user_id FROM subscriptions WHERE player_id = (SELECT id FROM players WHERE name = 'Lionel Messi' LIMIT 1))
LIMIT 1;
