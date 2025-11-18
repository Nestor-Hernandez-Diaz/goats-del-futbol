-- V9: Crear tabla de respuestas a comentarios
-- Fecha: 2025-01-17
-- Descripción: Tabla para respuestas anidadas a comentarios con soporte para hilos de conversación

CREATE TABLE IF NOT EXISTS comment_replies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    
    -- Foreign keys
    CONSTRAINT fk_reply_comment FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    CONSTRAINT fk_reply_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    -- Indices para optimizar consultas
    INDEX idx_reply_comment (comment_id),
    INDEX idx_reply_user (user_id),
    INDEX idx_reply_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar respuestas de ejemplo
INSERT INTO comment_replies (comment_id, user_id, content) VALUES
(1, 2, '¡Totalmente de acuerdo! El Mundial de Qatar fue el broche de oro perfecto.'),
(1, 1, 'Gracias por el comentario. Ese título era todo lo que le faltaba.'),
(2, 1, 'Ronaldo es un fenómeno, pero mi corazón es de Messi jaja');
