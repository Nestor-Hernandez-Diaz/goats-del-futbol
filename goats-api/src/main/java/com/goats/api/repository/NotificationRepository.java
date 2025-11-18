package com.goats.api.repository;

import com.goats.api.model.Notification;
import com.goats.api.model.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para Notification
 * Gestiona notificaciones de usuarios sobre jugadores seguidos
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Busca todas las notificaciones de un usuario ordenadas por fecha
     */
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Busca notificaciones no leídas de un usuario
     */
    Page<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Long userId, Boolean isRead, Pageable pageable);

    /**
     * Busca notificaciones por tipo
     */
    Page<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, NotificationType type, Pageable pageable);

    /**
     * Cuenta notificaciones no leídas de un usuario
     */
    long countByUserIdAndIsRead(Long userId, Boolean isRead);

    /**
     * Obtiene las últimas N notificaciones no leídas
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND n.isRead = false " +
           "ORDER BY n.createdAt DESC")
    List<Notification> findRecentUnreadNotifications(@Param("userId") Long userId, Pageable pageable);

    /**
     * Marca todas las notificaciones de un usuario como leídas
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP " +
           "WHERE n.user.id = :userId AND n.isRead = false")
    int markAllAsRead(@Param("userId") Long userId);

    /**
     * Marca notificaciones específicas como leídas
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP " +
           "WHERE n.id IN :ids AND n.user.id = :userId")
    int markAsReadByIds(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    /**
     * Elimina notificaciones leídas antiguas (más de 30 días)
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.isRead = true " +
           "AND n.readAt < CURRENT_TIMESTAMP - 30 DAY")
    int deleteOldReadNotifications();

    /**
     * Busca notificaciones de un jugador específico
     */
    Page<Notification> findByUserIdAndPlayerIdOrderByCreatedAtDesc(Long userId, Long playerId, Pageable pageable);

    /**
     * Cuenta total de notificaciones de un usuario
     */
    long countByUserId(Long userId);
}
