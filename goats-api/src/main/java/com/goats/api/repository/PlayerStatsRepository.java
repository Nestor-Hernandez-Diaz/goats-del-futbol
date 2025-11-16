package com.goats.api.repository;

import com.goats.api.model.PlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para PlayerStats
 * Gestiona las estadísticas de los jugadores
 */
@Repository
public interface PlayerStatsRepository extends JpaRepository<PlayerStats, Long> {

    /**
     * Busca las estadísticas por ID de jugador
     */
    Optional<PlayerStats> findByPlayerId(Long playerId);

    /**
     * Verifica si existen estadísticas para un jugador
     */
    boolean existsByPlayerId(Long playerId);

    /**
     * Obtiene los top N jugadores por goles
     */
    @Query("SELECT ps FROM PlayerStats ps ORDER BY ps.goals DESC")
    java.util.List<PlayerStats> findTopByGoals(org.springframework.data.domain.Pageable pageable);

    /**
     * Obtiene los top N jugadores por asistencias
     */
    @Query("SELECT ps FROM PlayerStats ps ORDER BY ps.assists DESC")
    java.util.List<PlayerStats> findTopByAssists(org.springframework.data.domain.Pageable pageable);

    /**
     * Obtiene los top N jugadores por trofeos
     */
    @Query("SELECT ps FROM PlayerStats ps ORDER BY ps.trophies DESC")
    java.util.List<PlayerStats> findTopByTrophies(org.springframework.data.domain.Pageable pageable);

    /**
     * Obtiene los top N jugadores por Balones de Oro
     */
    @Query("SELECT ps FROM PlayerStats ps WHERE ps.ballonDOrWins > 0 ORDER BY ps.ballonDOrWins DESC")
    java.util.List<PlayerStats> findTopByBallonDOr(org.springframework.data.domain.Pageable pageable);
}
