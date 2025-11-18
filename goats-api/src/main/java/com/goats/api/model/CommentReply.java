package com.goats.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad CommentReply - Respuestas a comentarios
 * 
 * Permite a los usuarios responder a comentarios existentes,
 * creando hilos de conversación en las páginas de jugadores.
 */
@Entity
@Table(name = "comment_replies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * Constructor de conveniencia para crear una respuesta
     */
    public CommentReply(Comment comment, User user, String content) {
        this.comment = comment;
        this.user = user;
        this.content = content;
        this.isDeleted = false;
    }
}
