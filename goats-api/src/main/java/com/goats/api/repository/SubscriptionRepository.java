package com.goats.api.repository;

import com.goats.api.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Subscription
 * Gestiona las suscripciones de usuarios a jugadores
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Busca una suscripción específica de usuario a jugador
     */
    Optional<Subscription> findByUserIdAndPlayerId(Long userId, Long playerId);

    /**
     * Verifica si existe una suscripción activa
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Subscription s " +
           "WHERE s.user.id = :userId AND s.player.id = :playerId AND s.active = true")
    boolean existsActiveSubscription(@Param("userId") Long userId, @Param("playerId") Long playerId);

    /**
     * Busca todas las suscripciones de un usuario
     */
    Page<Subscription> findByUserId(Long userId, Pageable pageable);

    /**
     * Busca suscripciones activas de un usuario
     */
    Page<Subscription> findByUserIdAndActive(Long userId, Boolean active, Pageable pageable);

    /**
     * Busca todos los suscriptores de un jugador
     */
    Page<Subscription> findByPlayerId(Long playerId, Pageable pageable);

    /**
     * Busca suscriptores activos de un jugador
     */
    Page<Subscription> findByPlayerIdAndActive(Long playerId, Boolean active, Pageable pageable);

    /**
     * Busca suscriptores activos con notificaciones habilitadas
     */
    @Query("SELECT s FROM Subscription s WHERE s.player.id = :playerId " +
           "AND s.active = true AND s.notificationsEnabled = true")
    List<Subscription> findActiveSubscribersWithNotifications(@Param("playerId") Long playerId);

    /**
     * Cuenta suscriptores activos de un jugador
     */
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.player.id = :playerId AND s.active = true")
    long countActiveSubscribers(@Param("playerId") Long playerId);

    /**
     * Cuenta suscripciones activas de un usuario
     */
    long countByUserIdAndActive(Long userId, Boolean active);

    /**
     * Obtiene todas las suscripciones activas de un usuario (para notificaciones)
     */
    @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId " +
           "AND s.active = true AND s.notificationsEnabled = true")
    List<Subscription> findUserActiveSubscriptionsWithNotifications(@Param("userId") Long userId);
}
