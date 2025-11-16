package com.goats.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Comment para comentarios de usuarios sobre jugadores
 * Incluye sistema de moderación por roles
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ModerationStatus status = ModerationStatus.PENDING;

    @Column
    private String moderationReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderated_by")
    private User moderatedBy;

    @Column
    private LocalDateTime moderatedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Enum para estado de moderación
    public enum ModerationStatus {
        PENDING,    // Pendiente de moderación
        APPROVED,   // Aprobado por moderador
        REJECTED,   // Rechazado por moderador
        EDITED      // Editado después de aprobación
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = ModerationStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructores
    public Comment() {
    }

    public Comment(User user, Player player, String content) {
        this.user = user;
        this.player = player;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ModerationStatus getStatus() {
        return status;
    }

    public void setStatus(ModerationStatus status) {
        this.status = status;
    }

    public String getModerationReason() {
        return moderationReason;
    }

    public void setModerationReason(String moderationReason) {
        this.moderationReason = moderationReason;
    }

    public User getModeratedBy() {
        return moderatedBy;
    }

    public void setModeratedBy(User moderatedBy) {
        this.moderatedBy = moderatedBy;
    }

    public LocalDateTime getModeratedAt() {
        return moderatedAt;
    }

    public void setModeratedAt(LocalDateTime moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", playerId=" + (player != null ? player.getId() : null) +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
