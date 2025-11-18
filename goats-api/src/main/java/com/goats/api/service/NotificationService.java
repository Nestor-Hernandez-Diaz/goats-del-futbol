package com.goats.api.service;

import com.goats.api.dto.NotificationDto;
import com.goats.api.model.Notification;
import com.goats.api.model.NotificationType;
import com.goats.api.model.Player;
import com.goats.api.model.Subscription;
import com.goats.api.model.User;
import com.goats.api.repository.NotificationRepository;
import com.goats.api.repository.PlayerRepository;
import com.goats.api.repository.SubscriptionRepository;
import com.goats.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de notificaciones de usuarios
 * Crea notificaciones automáticas para suscriptores cuando hay nuevos comentarios/logros
 */
@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    /**
     * Obtiene todas las notificaciones de un usuario
     */
    public Page<NotificationDto> getByUserId(Long userId, Boolean unreadOnly, Pageable pageable) {
        if (unreadOnly != null && unreadOnly) {
            return notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false, pageable)
                    .map(this::toDto);
        } else {
            return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                    .map(this::toDto);
        }
    }

    /**
     * Obtiene notificaciones del usuario autenticado
     */
    public Page<NotificationDto> getMyNotifications(String username, Boolean unreadOnly, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return getByUserId(user.getId(), unreadOnly, pageable);
    }

    /**
     * Obtiene notificaciones por tipo
     */
    public Page<NotificationDto> getByType(String username, NotificationType type, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return notificationRepository.findByUserIdAndTypeOrderByCreatedAtDesc(user.getId(), type, pageable)
                .map(this::toDto);
    }

    /**
     * Cuenta notificaciones no leídas
     */
    public long countUnread(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return notificationRepository.countByUserIdAndIsRead(user.getId(), false);
    }

    /**
     * Obtiene notificaciones recientes no leídas (últimas 10)
     */
    public List<NotificationDto> getRecentUnread(String username, int limit) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return notificationRepository.findRecentUnreadNotifications(user.getId(), PageRequest.of(0, limit))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Marca una notificación como leída
     */
    public NotificationDto markAsRead(Long notificationId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        
        // Verificar que la notificación pertenece al usuario
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot mark this notification as read");
        }
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        
        return toDto(notification);
    }

    /**
     * Marca múltiples notificaciones como leídas
     */
    public int markMultipleAsRead(List<Long> notificationIds, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return notificationRepository.markAsReadByIds(notificationIds, user.getId());
    }

    /**
     * Marca todas las notificaciones como leídas
     */
    public int markAllAsRead(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return notificationRepository.markAllAsRead(user.getId());
    }

    /**
     * Crea notificaciones para suscriptores cuando hay un nuevo comentario
     */
    public void notifyNewComment(Long playerId, String commentContent) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
        
        // Obtener suscriptores activos con notificaciones habilitadas
        List<Subscription> subscribers = subscriptionRepository.findActiveSubscribersWithNotifications(playerId);
        
        // Crear notificación para cada suscriptor
        String message = "Nuevo comentario en " + player.getName();
        String details = commentContent.length() > 100 
                ? commentContent.substring(0, 100) + "..." 
                : commentContent;
        
        for (Subscription subscription : subscribers) {
            Notification notification = new Notification(
                    subscription.getUser(),
                    player,
                    NotificationType.COMMENT,
                    message
            );
            notification.setDetails(details);
            notificationRepository.save(notification);
        }
    }

    /**
     * Crea notificaciones para suscriptores cuando hay un nuevo logro
     */
    public void notifyNewAchievement(Long playerId, String achievementTitle) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
        
        // Obtener suscriptores activos con notificaciones habilitadas
        List<Subscription> subscribers = subscriptionRepository.findActiveSubscribersWithNotifications(playerId);
        
        // Crear notificación para cada suscriptor
        String message = player.getName() + " obtuvo un nuevo logro";
        String details = achievementTitle;
        
        for (Subscription subscription : subscribers) {
            Notification notification = new Notification(
                    subscription.getUser(),
                    player,
                    NotificationType.ACHIEVEMENT,
                    message
            );
            notification.setDetails(details);
            notificationRepository.save(notification);
        }
    }

    /**
     * Elimina notificación (solo si pertenece al usuario)
     */
    public void delete(Long notificationId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        
        // Verificar que la notificación pertenece al usuario
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot delete this notification");
        }
        
        notificationRepository.delete(notification);
    }

    /**
     * Limpia notificaciones leídas antiguas (tarea de mantenimiento)
     */
    @Transactional
    public int cleanupOldNotifications() {
        return notificationRepository.deleteOldReadNotifications();
    }

    // Método de utilidad para convertir a DTO
    private NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getUser().getId(),
                notification.getUser().getUsername(),
                notification.getPlayer().getId(),
                notification.getPlayer().getName(),
                notification.getType(),
                notification.getMessage(),
                notification.getDetails(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }
}
