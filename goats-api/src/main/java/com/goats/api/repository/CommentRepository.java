package com.goats.api.repository;

import com.goats.api.model.Comment;
import com.goats.api.model.Comment.ModerationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para Comment
 * Gestiona los comentarios de usuarios sobre jugadores con sistema de moderación
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Busca todos los comentarios de un jugador
     */
    Page<Comment> findByPlayerId(Long playerId, Pageable pageable);

    /**
     * Busca comentarios aprobados de un jugador (para vista pública)
     */
    Page<Comment> findByPlayerIdAndStatus(Long playerId, ModerationStatus status, Pageable pageable);

    /**
     * Busca todos los comentarios de un usuario
     */
    Page<Comment> findByUserId(Long userId, Pageable pageable);

    /**
     * Busca comentarios de un usuario sobre un jugador específico
     */
    List<Comment> findByUserIdAndPlayerId(Long userId, Long playerId);

    /**
     * Busca comentarios por estado de moderación
     */
    Page<Comment> findByStatus(ModerationStatus status, Pageable pageable);

    /**
     * Busca comentarios pendientes de moderación
     */
    @Query("SELECT c FROM Comment c WHERE c.status = 'PENDING' ORDER BY c.createdAt ASC")
    List<Comment> findPendingComments(Pageable pageable);

    /**
     * Cuenta comentarios por estado
     */
    long countByStatus(ModerationStatus status);

    /**
     * Cuenta comentarios de un jugador por estado
     */
    long countByPlayerIdAndStatus(Long playerId, ModerationStatus status);

    /**
     * Cuenta comentarios de un usuario
     */
    long countByUserId(Long userId);

    /**
     * Busca comentarios moderados por un usuario específico
     */
    Page<Comment> findByModeratedById(Long moderatorId, Pageable pageable);

    /**
     * Obtiene los comentarios más recientes de un jugador (aprobados)
     */
    @Query("SELECT c FROM Comment c WHERE c.player.id = :playerId AND c.status = 'APPROVED' ORDER BY c.createdAt DESC")
    List<Comment> findRecentApprovedComments(@Param("playerId") Long playerId, Pageable pageable);
}
