package com.goats.api.dto;

import com.goats.api.model.Comment.ModerationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO para Comment
 * Gestiona datos de comentarios con moderación
 */
public class CommentDto {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String username; // Para respuestas

    @NotNull(message = "Player ID is required")
    private Long playerId;

    private String playerName; // Para respuestas

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 1000, message = "Content must be between 10 and 1000 characters")
    private String content;

    private ModerationStatus status;

    private String moderationReason;

    private Long moderatedBy;

    private LocalDateTime moderatedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructores
    public CommentDto() {
    }

    public CommentDto(Long id, Long userId, String username, Long playerId, String playerName,
                      String content, ModerationStatus status, String moderationReason,
                      Long moderatedBy, LocalDateTime moderatedAt, LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.playerId = playerId;
        this.playerName = playerName;
        this.content = content;
        this.status = status;
        this.moderationReason = moderationReason;
        this.moderatedBy = moderatedBy;
        this.moderatedAt = moderatedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Long getModeratedBy() {
        return moderatedBy;
    }

    public void setModeratedBy(Long moderatedBy) {
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
}
