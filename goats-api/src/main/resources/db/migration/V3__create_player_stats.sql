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
    ballondor_wins INT DEFAULT 0,
    champions_league_wins INT DEFAULT 0,
    world_cup_wins INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_player_stats_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    
    INDEX idx_goals (goals),
    INDEX idx_assists (assists),
    INDEX idx_trophies (trophies),
    INDEX idx_ballon_dor (ballondor_wins)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar estadísticas para los jugadores existentes (Messi, Cristiano, Neymar)

INSERT INTO player_stats (player_id, goals, assists, matches_played, trophies, yellow_cards, red_cards, 
                         minutes_played, ballondor_wins, champions_league_wins, world_cup_wins,
                         created_at, updated_at)
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
    END as ballondor_wins,
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
    END as world_cup_wins,
    CURRENT_TIMESTAMP as created_at,
    CURRENT_TIMESTAMP as updated_at
FROM players p
WHERE NOT EXISTS (SELECT 1 FROM player_stats ps WHERE ps.player_id = p.id);
