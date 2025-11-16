package com.goats.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO para Subscription
 * Gestiona datos de suscripciones de usuarios a jugadores
 */
public class SubscriptionDto {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String username; // Para respuestas

    @NotNull(message = "Player ID is required")
    private Long playerId;

    private String playerName; // Para respuestas

    private Boolean active;

    private Boolean notificationsEnabled;

    private LocalDateTime subscribedAt;

    private LocalDateTime unsubscribedAt;

    // Constructores
    public SubscriptionDto() {
    }

    public SubscriptionDto(Long id, Long userId, String username, Long playerId, String playerName,
                           Boolean active, Boolean notificationsEnabled,
                           LocalDateTime subscribedAt, LocalDateTime unsubscribedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.playerId = playerId;
        this.playerName = playerName;
        this.active = active;
        this.notificationsEnabled = notificationsEnabled;
        this.subscribedAt = subscribedAt;
        this.unsubscribedAt = unsubscribedAt;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public LocalDateTime getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(LocalDateTime subscribedAt) {
        this.subscribedAt = subscribedAt;
    }

    public LocalDateTime getUnsubscribedAt() {
        return unsubscribedAt;
    }

    public void setUnsubscribedAt(LocalDateTime unsubscribedAt) {
        this.unsubscribedAt = unsubscribedAt;
    }
}
