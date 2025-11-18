-- ============================================
-- Migración V8: Sistema de Notificaciones
-- ============================================
-- Fecha: 17 Noviembre 2025
-- Descripción: Tabla de notificaciones para usuarios

-- Crear tabla de notificaciones
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    message VARCHAR(500) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    related_player_id BIGINT NULL,
    related_comment_id BIGINT NULL,
    related_achievement_id BIGINT NULL,
    
    -- Foreign Keys
    CONSTRAINT fk_notifications_user 
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT fk_notifications_player
        FOREIGN KEY (related_player_id)
        REFERENCES players(id)
        ON DELETE SET NULL,
    
    CONSTRAINT fk_notifications_comment
        FOREIGN KEY (related_comment_id)
        REFERENCES comments(id)
        ON DELETE SET NULL,
    
    CONSTRAINT fk_notifications_achievement
        FOREIGN KEY (related_achievement_id)
        REFERENCES achievements(id)
        ON DELETE SET NULL,
    
    -- Índices optimizados para consultas frecuentes
    INDEX idx_user_read (user_id, is_read),
    INDEX idx_user_created (user_id, created_at),
    INDEX idx_type (type),
    INDEX idx_created_at (created_at)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Comentarios para documentación
ALTER TABLE notifications 
    COMMENT = 'Tabla de notificaciones para usuarios. Notifica nuevos comentarios y logros de jugadores seguidos';
