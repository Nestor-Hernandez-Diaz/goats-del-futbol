package com.goats.api.controller;

import com.goats.api.dto.CommentReplyDto;
import com.goats.api.model.User;
import com.goats.api.service.CommentReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para CommentReply
 * 
 * API REST para gestionar respuestas a comentarios.
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CommentReplyController {

    private final CommentReplyService commentReplyService;

    /**
     * Crear una respuesta a un comentario
     * POST /api/comments/{commentId}/replies
     */
    @PostMapping("/{commentId}/replies")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createReply(
            @PathVariable Long commentId,
            @RequestBody Map<String, String> payload) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            String content = payload.get("content");
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El contenido de la respuesta no puede estar vacío"));
            }

            if (content.length() > 500) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El contenido no puede exceder 500 caracteres"));
            }

            CommentReplyDto reply = commentReplyService.createReply(commentId, username, content.trim());
            return ResponseEntity.status(HttpStatus.CREATED).body(reply);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtener todas las respuestas de un comentario
     * GET /api/comments/{commentId}/replies
     */
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<?> getRepliesByComment(@PathVariable Long commentId) {
        try {
            List<CommentReplyDto> replies = commentReplyService.getRepliesByCommentId(commentId);
            return ResponseEntity.ok(replies);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtener una respuesta específica
     * GET /api/comments/replies/{replyId}
     */
    @GetMapping("/replies/{replyId}")
    public ResponseEntity<?> getReplyById(@PathVariable Long replyId) {
        try {
            CommentReplyDto reply = commentReplyService.getReplyById(replyId);
            return ResponseEntity.ok(reply);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtener todas las respuestas de un usuario
     * GET /api/comments/replies/user/{userId}
     */
    @GetMapping("/replies/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRepliesByUser(@PathVariable Long userId) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            // Obtener el ID del usuario autenticado para verificar permisos
            User currentUser = commentReplyService.getUserByUsername(username);
            
            // Solo el mismo usuario o admin puede ver sus respuestas
            if (!currentUser.getId().equals(userId) && !isAdmin(auth)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permiso para ver las respuestas de este usuario"));
            }

            List<CommentReplyDto> replies = commentReplyService.getRepliesByUserId(userId);
            return ResponseEntity.ok(replies);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Actualizar una respuesta
     * PUT /api/comments/replies/{replyId}
     */
    @PutMapping("/replies/{replyId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateReply(
            @PathVariable Long replyId,
            @RequestBody Map<String, String> payload) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            String content = payload.get("content");
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El contenido no puede estar vacío"));
            }

            if (content.length() > 500) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El contenido no puede exceder 500 caracteres"));
            }

            CommentReplyDto updatedReply = commentReplyService.updateReply(replyId, username, content.trim());
            return ResponseEntity.ok(updatedReply);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Eliminar una respuesta
     * DELETE /api/comments/replies/{replyId}
     */
    @DeleteMapping("/replies/{replyId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteReply(@PathVariable Long replyId) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            boolean isAdmin = isAdmin(auth);

            commentReplyService.deleteReply(replyId, username, isAdmin);
            return ResponseEntity.ok(Map.of("message", "Respuesta eliminada exitosamente"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Contar respuestas de un comentario
     * GET /api/comments/{commentId}/replies/count
     */
    @GetMapping("/{commentId}/replies/count")
    public ResponseEntity<?> countReplies(@PathVariable Long commentId) {
        try {
            Long count = commentReplyService.countRepliesByCommentId(commentId);
            return ResponseEntity.ok(count);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Verificar si un usuario ha respondido a un comentario
     * GET /api/comments/{commentId}/replies/check
     */
    @GetMapping("/{commentId}/replies/check")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> hasUserReplied(@PathVariable Long commentId) {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            Boolean hasReplied = commentReplyService.hasUserReplied(commentId, username);
            return ResponseEntity.ok(hasReplied);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Verificar si el usuario tiene rol ADMIN
     */
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
}
