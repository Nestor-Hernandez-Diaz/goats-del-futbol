package com.goats.api.service;

import com.goats.api.dto.CommentReplyDto;
import com.goats.api.exception.ResourceNotFoundException;
import com.goats.api.model.Comment;
import com.goats.api.model.CommentReply;
import com.goats.api.model.User;
import com.goats.api.repository.CommentReplyRepository;
import com.goats.api.repository.CommentRepository;
import com.goats.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para CommentReply
 * 
 * Lógica de negocio para respuestas a comentarios.
 */
@Service
@RequiredArgsConstructor
public class CommentReplyService {

    private final CommentReplyRepository commentReplyRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /**
     * Crear una nueva respuesta a un comentario
     */
    @Transactional
    public CommentReplyDto createReply(Long commentId, String username, String content) {
        // Validar que el comentario existe y está aprobado
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con id: " + commentId));

        if (comment.getStatus() != com.goats.api.model.ModerationStatus.APPROVED) {
            throw new IllegalStateException("No se puede responder a un comentario que no está aprobado");
        }

        // Validar que el usuario existe
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));

        // Crear la respuesta
        CommentReply reply = new CommentReply(comment, user, content);
        CommentReply savedReply = commentReplyRepository.save(reply);

        // Convertir a DTO
        return convertToDto(savedReply);
    }

    /**
     * Obtener todas las respuestas de un comentario
     */
    @Transactional(readOnly = true)
    public List<CommentReplyDto> getRepliesByCommentId(Long commentId) {
        // Verificar que el comentario existe
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comentario no encontrado con id: " + commentId);
        }

        List<CommentReply> replies = commentReplyRepository.findByCommentIdAndNotDeleted(commentId);
        return replies.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las respuestas de un usuario
     */
    @Transactional(readOnly = true)
    public List<CommentReplyDto> getRepliesByUserId(Long userId) {
        // Verificar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + userId);
        }

        List<CommentReply> replies = commentReplyRepository.findByUserIdAndNotDeleted(userId);
        return replies.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener una respuesta por su ID
     */
    @Transactional(readOnly = true)
    public CommentReplyDto getReplyById(Long replyId) {
        CommentReply reply = commentReplyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Respuesta no encontrada con id: " + replyId));

        if (reply.getIsDeleted()) {
            throw new ResourceNotFoundException("Respuesta eliminada");
        }

        return convertToDto(reply);
    }

    /**
     * Actualizar el contenido de una respuesta
     */
    @Transactional
    public CommentReplyDto updateReply(Long replyId, String username, String newContent) {
        CommentReply reply = commentReplyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Respuesta no encontrada con id: " + replyId));

        // Verificar que el usuario es el propietario
        if (!reply.getUser().getUsername().equals(username)) {
            throw new IllegalStateException("No tienes permiso para editar esta respuesta");
        }

        if (reply.getIsDeleted()) {
            throw new IllegalStateException("No se puede editar una respuesta eliminada");
        }

        // Actualizar contenido
        reply.setContent(newContent);
        CommentReply updatedReply = commentReplyRepository.save(reply);

        return convertToDto(updatedReply);
    }

    /**
     * Eliminar una respuesta (soft delete)
     */
    @Transactional
    public void deleteReply(Long replyId, String username, boolean isAdmin) {
        CommentReply reply = commentReplyRepository.findById(replyId)
                .orElseThrow(() -> new ResourceNotFoundException("Respuesta no encontrada con id: " + replyId));

        // Solo el propietario o un admin pueden eliminar
        if (!reply.getUser().getUsername().equals(username) && !isAdmin) {
            throw new IllegalStateException("No tienes permiso para eliminar esta respuesta");
        }

        // Soft delete
        reply.setIsDeleted(true);
        commentReplyRepository.save(reply);
    }

    /**
     * Contar respuestas de un comentario
     */
    @Transactional(readOnly = true)
    public Long countRepliesByCommentId(Long commentId) {
        return commentReplyRepository.countByCommentIdAndNotDeleted(commentId);
    }

    /**
     * Verificar si un usuario ha respondido a un comentario
     */
    @Transactional(readOnly = true)
    public Boolean hasUserReplied(Long commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
        return commentReplyRepository.existsByCommentIdAndUserId(commentId, user.getId());
    }

    /**
     * Obtener usuario por username
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    }

    /**
     * Convertir entidad a DTO
     */
    private CommentReplyDto convertToDto(CommentReply reply) {
        return new CommentReplyDto(
                reply.getId(),
                reply.getComment().getId(),
                reply.getUser().getId(),
                reply.getUser().getUsername(),
                reply.getContent(),
                reply.getCreatedAt(),
                reply.getUpdatedAt(),
                reply.getIsDeleted()
        );
    }
}
