package com.goats.api.repository;

import com.goats.api.model.Achievement;
import com.goats.api.model.AchievementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para Achievement
 * Gestiona los logros y competiciones ganadas por los jugadores
 */
@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    /**
     * Busca todos los logros de un jugador
     */
    List<Achievement> findByPlayerId(Long playerId);

    /**
     * Busca logros de un jugador paginados
     */
    Page<Achievement> findByPlayerId(Long playerId, Pageable pageable);

    /**
     * Busca logros por tipo
     */
    List<Achievement> findByType(AchievementType type);

    /**
     * Busca logros por jugador y tipo
     */
    List<Achievement> findByPlayerIdAndType(Long playerId, AchievementType type);

    /**
     * Busca logros por año
     */
    List<Achievement> findByYear(Integer year);

    /**
     * Busca logros por jugador y año
     */
    List<Achievement> findByPlayerIdAndYear(Long playerId, Integer year);

    /**
     * Busca logros por título (búsqueda parcial, case-insensitive)
     */
    @Query("SELECT a FROM Achievement a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Achievement> findByTitleContaining(@Param("title") String title);

    /**
     * Cuenta los logros de un jugador
     */
    long countByPlayerId(Long playerId);

    /**
     * Cuenta los logros de un jugador por tipo
     */
    long countByPlayerIdAndType(Long playerId, AchievementType type);

    /**
     * Obtiene los logros más recientes de un jugador
     */
    List<Achievement> findByPlayerIdOrderByYearDesc(Long playerId, Pageable pageable);
}
