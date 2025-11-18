package com.goats.api.model;

/**
 * Enum para estado de moderación de comentarios
 * Sistema de moderación de contenido generado por usuarios
 */
public enum ModerationStatus {
    /**
     * Comentario pendiente de revisión por moderador
     */
    PENDING,
    
    /**
     * Comentario aprobado y visible públicamente
     */
    APPROVED,
    
    /**
     * Comentario rechazado por incumplir reglas
     */
    REJECTED,
    
    /**
     * Comentario editado después de haber sido aprobado
     */
    EDITED
}
