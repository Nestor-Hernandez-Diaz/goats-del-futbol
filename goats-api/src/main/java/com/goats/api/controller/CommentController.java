package com.goats.api.controller;

import com.goats.api.dto.CommentDto;
import com.goats.api.model.ModerationStatus;
import com.goats.api.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para comentarios con sistema de moderación
 * GET públicos (aprobados), POST requiere autenticación, moderación requiere ADMIN
 */
@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Endpoints para comentarios con moderación")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los comentarios (ADMIN)",
               description = "Obtiene todos los comentarios sin filtro (requiere rol ADMIN)")
    public Page<CommentDto> getAll(@RequestParam(required = false) ModerationStatus status,
                                    @PageableDefault(size = 1000) Pageable pageable) {
        return commentService.getAll(status, pageable);
    }

    @GetMapping("/player/{playerId}")
    @Operation(summary = "Listar comentarios de jugador",
               description = "Obtiene comentarios aprobados de un jugador (público)")
    public Page<CommentDto> getByPlayerId(@PathVariable Long playerId,
                                           @PageableDefault(size = 20) Pageable pageable) {
        // Solo mostrar comentarios aprobados al público
        return commentService.getByPlayerId(playerId, ModerationStatus.APPROVED, pageable);
    }

    @GetMapping("/player/{playerId}/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los comentarios de jugador",
               description = "Obtiene todos los comentarios sin filtro (requiere rol ADMIN)")
    public Page<CommentDto> getAllByPlayerId(@PathVariable Long playerId,
                                              @RequestParam(required = false) ModerationStatus status,
                                              @PageableDefault(size = 20) Pageable pageable) {
        return commentService.getByPlayerId(playerId, status, pageable);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar comentarios de usuario",
               description = "Obtiene todos los comentarios de un usuario (requiere autenticación)")
    public Page<CommentDto> getByUserId(@PathVariable Long userId,
                                         @PageableDefault(size = 20) Pageable pageable) {
        return commentService.getByUserId(userId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener comentario por ID",
               description = "Devuelve un comentario específico")
    public ResponseEntity<CommentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    @PostMapping
    // @PreAuthorize("isAuthenticated()") // TEMPORAL: Removido para debugging
    @Operation(summary = "Crear comentario",
               description = "Crea un nuevo comentario (requiere autenticación, estado PENDING)")
    public ResponseEntity<CommentDto> create(@Valid @RequestBody CommentDto dto) {
        log.info("=== POST /api/comments - Intento de crear comentario ===");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null) {
            log.error("Authentication es NULL");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Authentication presente: {}", auth.getClass().getName());
        log.info("Is Authenticated: {}", auth.isAuthenticated());
        log.info("Username: {}", auth.getName());
        log.info("Authorities: {}", auth.getAuthorities());
        
        // Si es anonymous, rechazar
        if (auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            log.warn("Usuario anónimo intentando crear comentario");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String username = auth.getName();
        
        CommentDto created = commentService.create(dto, username);
        log.info("Comentario creado exitosamente para user: {}", username);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar comentario",
               description = "Actualiza el contenido de un comentario propio (requiere autenticación)")
    public ResponseEntity<CommentDto> update(@PathVariable Long id,
                                              @Valid @RequestBody CommentDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return ResponseEntity.ok(commentService.update(id, dto, username));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Eliminar comentario",
               description = "Elimina un comentario propio o cualquiera si es ADMIN")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        
        commentService.delete(id, username, isAdmin);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Aprobar comentario (ADMIN)",
               description = "Aprueba un comentario pendiente (requiere rol ADMIN)")
    public ResponseEntity<CommentDto> approve(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return ResponseEntity.ok(commentService.approve(id, username));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Rechazar comentario (ADMIN)",
               description = "Rechaza un comentario con motivo (requiere rol ADMIN)")
    public ResponseEntity<CommentDto> reject(@PathVariable Long id,
                                              @RequestBody Map<String, String> body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String reason = body.getOrDefault("reason", "No reason provided");
        
        return ResponseEntity.ok(commentService.reject(id, reason, username));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Comentarios pendientes",
               description = "Obtiene comentarios pendientes de moderación (requiere rol ADMIN)")
    public ResponseEntity<List<CommentDto>> getPendingComments(@RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(commentService.getPendingComments(limit));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar por estado",
               description = "Obtiene comentarios filtrados por estado (requiere rol ADMIN)")
    public Page<CommentDto> getByStatus(@PathVariable ModerationStatus status,
                                         @PageableDefault(size = 20) Pageable pageable) {
        return commentService.getByStatus(status, pageable);
    }

    @GetMapping("/status/{status}/count")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Contar por estado",
               description = "Cuenta comentarios por estado (requiere rol ADMIN)")
    public ResponseEntity<Long> countByStatus(@PathVariable ModerationStatus status) {
        return ResponseEntity.ok(commentService.countByStatus(status));
    }

    @GetMapping("/player/{playerId}/recent")
    @Operation(summary = "Comentarios recientes aprobados",
               description = "Obtiene los comentarios más recientes aprobados de un jugador")
    public ResponseEntity<List<CommentDto>> getRecentApprovedComments(@PathVariable Long playerId,
                                                                       @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(commentService.getRecentApprovedComments(playerId, limit));
    }
}
