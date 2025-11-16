package com.goats.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Subscription para suscripciones de usuarios a jugadores
 * Permite notificaciones sobre actualizaciones del jugador
 */
@Entity
@Table(name = "subscriptions", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "player_id"}))
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean notificationsEnabled = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime subscribedAt;

    @Column
    private LocalDateTime unsubscribedAt;

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        subscribedAt = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
        if (notificationsEnabled == null) {
            notificationsEnabled = true;
        }
    }

    // Constructores
    public Subscription() {
    }

    public Subscription(User user, Player player) {
        this.user = user;
        this.player = player;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", playerId=" + (player != null ? player.getId() : null) +
                ", active=" + active +
                ", subscribedAt=" + subscribedAt +
                '}';
    }
}
