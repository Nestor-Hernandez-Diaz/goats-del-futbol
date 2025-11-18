package com.goats.api.model;

/**
 * Enum para tipos de logros y competiciones
 * Categoriza los diferentes tipos de achievements que puede tener un jugador
 */
public enum AchievementType {
    /**
     * Logros individuales: Balón de Oro, Bota de Oro, The Best, etc.
     */
    INDIVIDUAL,
    
    /**
     * Logros de club: Champions League, Liga, Copa, etc.
     */
    CLUB,
    
    /**
     * Logros con selección nacional: Mundial, Copa América, Eurocopa, etc.
     */
    NATIONAL_TEAM,
    
    /**
     * Récords personales: más goles en una temporada, etc.
     */
    RECORD,
    
    /**
     * Otros logros no categorizados
     */
    OTHER
}
