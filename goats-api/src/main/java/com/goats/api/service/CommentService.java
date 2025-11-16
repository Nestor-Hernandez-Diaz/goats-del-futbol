package com.goats.api.service;

import com.goats.api.dto.CommentDto;
import com.goats.api.model.Comment;
import com.goats.api.model.Comment.ModerationStatus;
import com.goats.api.model.Player;
import com.goats.api.model.User;
import com.goats.api.repository.CommentRepository;
import com.goats.api.repository.PlayerRepository;
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
 * Servicio para gestión de comentarios con sistema de moderación
 */
@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Obtiene todos los comentarios (filtrado opcional por estado) - ADMIN
     */
    public Page<CommentDto> getAll(ModerationStatus status, Pageable pageable) {
        if (status != null) {
            return commentRepository.findByStatus(status, pageable)
                    .map(this::toDto);
        } else {
            return commentRepository.findAll(pageable)
                    .map(this::toDto);
        }
    }

    /**
     * Obtiene comentarios de un jugador (filtrado por estado)
     */
    public Page<CommentDto> getByPlayerId(Long playerId, ModerationStatus status, Pageable pageable) {
        if (status != null) {
            return commentRepository.findByPlayerIdAndStatus(playerId, status, pageable)
                    .map(this::toDto);
        } else {
            return commentRepository.findByPlayerId(playerId, pageable)
                    .map(this::toDto);
        }
    }

    /**
     * Obtiene comentarios de un usuario
     */
    public Page<CommentDto> getByUserId(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable)
                .map(this::toDto);
    }

    /**
     * Obtiene un comentario por ID
     */
    @SuppressWarnings("null")
    public CommentDto getById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return toDto(comment);
    }

    /**
     * Crea un nuevo comentario (estado PENDING por defecto)
     */
    @SuppressWarnings("null")
    public CommentDto create(CommentDto dto, String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        Player player = playerRepository.findById(dto.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + dto.getPlayerId()));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPlayer(player);
        comment.setContent(dto.getContent());
        comment.setStatus(ModerationStatus.PENDING);

        comment = commentRepository.save(comment);
        return toDto(comment);
    }

    /**
     * Actualiza el contenido de un comentario (solo el autor)
     */
    @SuppressWarnings("null")
    public CommentDto update(Long id, CommentDto dto, String currentUsername) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        // Verificar que el usuario sea el autor
        if (!comment.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You can only edit your own comments");
        }

        comment.setContent(dto.getContent());
        comment.setStatus(ModerationStatus.EDITED);

        comment = commentRepository.save(comment);
        return toDto(comment);
    }

    /**
     * Elimina un comentario (solo el autor o ADMIN)
     */
    @SuppressWarnings("null")
    public void delete(Long id, String currentUsername, boolean isAdmin) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        // Verificar permisos
        if (!isAdmin && !comment.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }

    /**
     * Aprueba un comentario (solo ADMIN)
     */
    @SuppressWarnings("null")
    public CommentDto approve(Long id, String moderatorUsername) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        User moderator = userRepository.findByUsername(moderatorUsername)
                .orElseThrow(() -> new RuntimeException("Moderator not found: " + moderatorUsername));

        comment.setStatus(ModerationStatus.APPROVED);
        comment.setModeratedBy(moderator);
        comment.setModeratedAt(LocalDateTime.now());

        comment = commentRepository.save(comment);
        return toDto(comment);
    }

    /**
     * Rechaza un comentario (solo ADMIN)
     */
    @SuppressWarnings("null")
    public CommentDto reject(Long id, String reason, String moderatorUsername) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        User moderator = userRepository.findByUsername(moderatorUsername)
                .orElseThrow(() -> new RuntimeException("Moderator not found: " + moderatorUsername));

        comment.setStatus(ModerationStatus.REJECTED);
        comment.setModerationReason(reason);
        comment.setModeratedBy(moderator);
        comment.setModeratedAt(LocalDateTime.now());

        comment = commentRepository.save(comment);
        return toDto(comment);
    }

    /**
     * Obtiene comentarios pendientes de moderación
     */
    public List<CommentDto> getPendingComments(int limit) {
        return commentRepository.findPendingComments(PageRequest.of(0, limit))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene comentarios por estado
     */
    public Page<CommentDto> getByStatus(ModerationStatus status, Pageable pageable) {
        return commentRepository.findByStatus(status, pageable)
                .map(this::toDto);
    }

    /**
     * Cuenta comentarios por estado
     */
    public long countByStatus(ModerationStatus status) {
        return commentRepository.countByStatus(status);
    }

    /**
     * Obtiene comentarios recientes aprobados de un jugador
     */
    public List<CommentDto> getRecentApprovedComments(Long playerId, int limit) {
        return commentRepository.findRecentApprovedComments(playerId, PageRequest.of(0, limit))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Métodos de utilidad
    private CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getPlayer().getId(),
                comment.getPlayer().getName(),
                comment.getContent(),
                comment.getStatus(),
                comment.getModerationReason(),
                comment.getModeratedBy() != null ? comment.getModeratedBy().getId() : null,
                comment.getModeratedAt(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
