package com.goats.api.repository;

import com.goats.api.model.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para CommentReply
 * 
 * Interfaz para acceso a datos de respuestas a comentarios.
 */
@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {

    /**
     * Obtener todas las respuestas de un comentario (sin eliminar)
     */
    @Query("SELECT r FROM CommentReply r WHERE r.comment.id = :commentId AND r.isDeleted = false ORDER BY r.createdAt ASC")
    List<CommentReply> findByCommentIdAndNotDeleted(@Param("commentId") Long commentId);

    /**
     * Obtener todas las respuestas de un comentario (incluyendo eliminadas)
     */
    List<CommentReply> findByCommentIdOrderByCreatedAtAsc(Long commentId);

    /**
     * Obtener todas las respuestas de un usuario
     */
    @Query("SELECT r FROM CommentReply r WHERE r.user.id = :userId AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<CommentReply> findByUserIdAndNotDeleted(@Param("userId") Long userId);

    /**
     * Contar respuestas de un comentario (sin eliminar)
     */
    @Query("SELECT COUNT(r) FROM CommentReply r WHERE r.comment.id = :commentId AND r.isDeleted = false")
    Long countByCommentIdAndNotDeleted(@Param("commentId") Long commentId);

    /**
     * Contar respuestas de un usuario
     */
    Long countByUserId(Long userId);

    /**
     * Verificar si un usuario tiene respuestas en un comentario
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM CommentReply r WHERE r.comment.id = :commentId AND r.user.id = :userId AND r.isDeleted = false")
    Boolean existsByCommentIdAndUserId(@Param("commentId") Long commentId, @Param("userId") Long userId);
}
