package com.goats.api.service;

import com.goats.api.dto.SubscriptionDto;
import com.goats.api.model.Player;
import com.goats.api.model.Subscription;
import com.goats.api.model.User;
import com.goats.api.repository.PlayerRepository;
import com.goats.api.repository.SubscriptionRepository;
import com.goats.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de suscripciones de usuarios a jugadores
 */
@Service
@Transactional
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Obtiene todas las suscripciones de un usuario
     */
    public Page<SubscriptionDto> getByUserId(Long userId, Boolean active, Pageable pageable) {
        if (active != null) {
            return subscriptionRepository.findByUserIdAndActive(userId, active, pageable)
                    .map(this::toDto);
        } else {
            return subscriptionRepository.findByUserId(userId, pageable)
                    .map(this::toDto);
        }
    }

    /**
     * Obtiene todos los suscriptores de un jugador
     */
    @SuppressWarnings("null")
    public Page<SubscriptionDto> getByPlayerId(Long playerId, Boolean active, Pageable pageable) {
        if (active != null) {
            return subscriptionRepository.findByPlayerIdAndActive(playerId, active, pageable)
                    .map(this::toDto);
        } else {
            return subscriptionRepository.findByPlayerId(playerId, pageable)
                    .map(this::toDto);
        }
    }

    /**
     * Verifica si existe una suscripción activa
     */
    public boolean isSubscribed(String username, Long playerId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return subscriptionRepository.existsActiveSubscription(user.getId(), playerId);
    }

    /**
     * Crea una nueva suscripción (subscribe)
     */
    @SuppressWarnings("null")
    public SubscriptionDto subscribe(Long playerId, String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

        // Verificar si ya existe una suscripción
        var existingSub = subscriptionRepository.findByUserIdAndPlayerId(user.getId(), playerId);
        
        if (existingSub.isPresent()) {
            Subscription subscription = existingSub.get();
            if (subscription.getActive()) {
                throw new RuntimeException("You are already subscribed to this player");
            } else {
                // Reactivar suscripción
                subscription.setActive(true);
                subscription.setUnsubscribedAt(null);
                subscription = subscriptionRepository.save(subscription);
                return toDto(subscription);
            }
        }

        // Crear nueva suscripción
        Subscription subscription = new Subscription(user, player);
        subscription = subscriptionRepository.save(subscription);

        return toDto(subscription);
    }

    /**
     * Desactiva una suscripción (unsubscribe)
     */
    public void unsubscribe(Long playerId, String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        Subscription subscription = subscriptionRepository.findByUserIdAndPlayerId(user.getId(), playerId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setActive(false);
        subscription.setUnsubscribedAt(LocalDateTime.now());
        subscriptionRepository.save(subscription);
    }

    /**
     * Alterna el estado de notificaciones
     */
    public SubscriptionDto toggleNotifications(Long playerId, String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        Subscription subscription = subscriptionRepository.findByUserIdAndPlayerId(user.getId(), playerId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        if (!subscription.getActive()) {
            throw new RuntimeException("Cannot toggle notifications for inactive subscription");
        }

        subscription.setNotificationsEnabled(!subscription.getNotificationsEnabled());
        subscription = subscriptionRepository.save(subscription);

        return toDto(subscription);
    }

    /**
     * Cuenta suscriptores activos de un jugador
     */
    public long countActiveSubscribers(Long playerId) {
        return subscriptionRepository.countActiveSubscribers(playerId);
    }

    /**
     * Cuenta suscripciones activas de un usuario
     */
    public long countUserActiveSubscriptions(Long userId) {
        return subscriptionRepository.countByUserIdAndActive(userId, true);
    }

    /**
     * Obtiene suscriptores con notificaciones habilitadas
     */
    public List<SubscriptionDto> getSubscribersWithNotifications(Long playerId) {
        return subscriptionRepository.findActiveSubscribersWithNotifications(playerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Métodos de utilidad
    private SubscriptionDto toDto(Subscription subscription) {
        return new SubscriptionDto(
                subscription.getId(),
                subscription.getUser().getId(),
                subscription.getUser().getUsername(),
                subscription.getPlayer().getId(),
                subscription.getPlayer().getName(),
                subscription.getActive(),
                subscription.getNotificationsEnabled(),
                subscription.getSubscribedAt(),
                subscription.getUnsubscribedAt()
        );
    }
}
