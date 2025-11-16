-- Migración V2: Crear tablas de usuarios y roles
-- Fecha: 2025-11-15
-- Descripción: Crea las tablas necesarias para autenticación JWT

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    INDEX idx_role_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_username (username),
    INDEX idx_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de relación usuarios-roles (many-to-many)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar roles predefinidos
INSERT INTO roles (name) VALUES 
    ('ROLE_ADMIN'),
    ('ROLE_USER'),
    ('ROLE_GUEST')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Crear usuario administrador por defecto
-- Usuario: admin
-- Password: admin123 (debe cambiarse en producción)
-- Hash BCrypt de "admin123"
INSERT INTO users (username, email, password_hash, enabled) VALUES 
    ('admin', 'admin@goats-futbol.com', '$2a$10$5o3lPvB4jUhcaQBUqJ9X3OwMnXjqCPFnBpFg8u7mS8s1EZqJJ8qLG', TRUE)
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- Asignar rol ADMIN al usuario admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
CROSS JOIN roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);
