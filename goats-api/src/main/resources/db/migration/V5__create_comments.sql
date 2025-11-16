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
