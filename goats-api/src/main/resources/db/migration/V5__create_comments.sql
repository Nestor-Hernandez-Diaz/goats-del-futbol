-- Migration V5: Create comments table
-- Tabla de comentarios de usuarios sobre jugadores con sistema de moderación

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    moderated_at TIMESTAMP NULL,
    moderated_by_id BIGINT NULL,
    moderation_reason VARCHAR(500) NULL,
    
    CONSTRAINT fk_comments_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_moderator FOREIGN KEY (moderated_by_id) REFERENCES users(id) ON DELETE SET NULL,
    
    INDEX idx_player_id (player_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_player_status (player_id, status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar comentarios de ejemplo (algunos aprobados, otros pendientes)
INSERT INTO comments (player_id, user_id, content, status) VALUES
((SELECT id FROM players WHERE name = 'Lionel Messi'), 
 (SELECT id FROM users WHERE username = 'admin'), 
 '¡El mejor jugador de la historia! Su habilidad, visión y capacidad goleadora son incomparables.',
 'APPROVED'),
 
((SELECT id FROM players WHERE name = 'Lionel Messi'), 
 (SELECT id FROM users WHERE username = 'testuser'), 
 'Ver jugar a Messi es presenciar magia pura en el campo. Un genio del fútbol.',
 'APPROVED'),

((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 
 (SELECT id FROM users WHERE username = 'admin'), 
 'CR7 es sinónimo de dedicación, trabajo duro y resultados. Un verdadero profesional.',
 'APPROVED'),

((SELECT id FROM players WHERE name = 'Cristiano Ronaldo'), 
 (SELECT id FROM users WHERE name = 'testuser'), 
 'La mentalidad ganadora de Cristiano es impresionante. Siempre presente en los momentos clave.',
 'APPROVED'),

((SELECT id FROM players WHERE name = 'Neymar Jr'), 
 (SELECT id FROM users WHERE username = 'admin'), 
 'Neymar tiene un talento increíble. Su habilidad técnica y creatividad son excepcionales.',
 'APPROVED'),

((SELECT id FROM players WHERE name = 'Neymar Jr'), 
 (SELECT id FROM users WHERE username = 'testuser'), 
 'Un jugador espectacular que siempre entretiene. Brasil tiene una joya.',
 'PENDING');
