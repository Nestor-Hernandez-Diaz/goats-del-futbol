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
