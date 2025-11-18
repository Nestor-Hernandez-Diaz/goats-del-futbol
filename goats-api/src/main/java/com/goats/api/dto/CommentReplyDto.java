package com.goats.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para CommentReply
 * 
 * Objeto de transferencia de datos para respuestas a comentarios,
 * con información del usuario que responde.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReplyDto {

    private Long id;
    private Long commentId;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    /**
     * Constructor sin timestamps para creación
     */
    public CommentReplyDto(Long commentId, Long userId, String username, String content) {
        this.commentId = commentId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.isDeleted = false;
    }
}
