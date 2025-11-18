package com.goats.api.controller;

import com.goats.api.dto.NotificationDto;
import com.goats.api.model.NotificationType;
import com.goats.api.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para notificaciones de usuarios
 * Requiere autenticación para todas las operaciones
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Endpoints para notificaciones de usuarios")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar mis notificaciones",
               description = "Obtiene todas las notificaciones del usuario autenticado (requiere autenticación)")
    public Page<NotificationDto> getMyNotifications(@RequestParam(required = false) Boolean unreadOnly,
                                                     @PageableDefault(size = 20) Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return notificationService.getMyNotifications(username, unreadOnly, pageable);
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar notificaciones por tipo",
               description = "Obtiene notificaciones filtradas por tipo (COMMENT, ACHIEVEMENT, GENERAL)")
    public Page<NotificationDto> getByType(@PathVariable NotificationType type,
                                            @PageableDefault(size = 20) Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return notificationService.getByType(username, type, pageable);
    }

    @GetMapping("/unread/count")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Contar no leídas",
               description = "Cuenta el total de notificaciones no leídas del usuario")
    public ResponseEntity<Long> countUnread() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return ResponseEntity.ok(notificationService.countUnread(username));
    }

    @GetMapping("/unread/recent")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Notificaciones recientes no leídas",
               description = "Obtiene las últimas notificaciones no leídas (por defecto 10)")
    public ResponseEntity<List<NotificationDto>> getRecentUnread(@RequestParam(defaultValue = "10") int limit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return ResponseEntity.ok(notificationService.getRecentUnread(username, limit));
    }

    @PatchMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marcar como leída",
               description = "Marca una notificación específica como leída")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return ResponseEntity.ok(notificationService.markAsRead(id, username));
    }

    @PatchMapping("/read/multiple")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marcar múltiples como leídas",
               description = "Marca varias notificaciones como leídas")
    public ResponseEntity<Map<String, Object>> markMultipleAsRead(@RequestBody Map<String, List<Long>> request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        List<Long> ids = request.get("notificationIds");
        int updated = notificationService.markMultipleAsRead(ids, username);
        
        return ResponseEntity.ok(Map.of(
                "updated", updated,
                "message", updated + " notificaciones marcadas como leídas"
        ));
    }

    @PatchMapping("/read/all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marcar todas como leídas",
               description = "Marca todas las notificaciones del usuario como leídas")
    public ResponseEntity<Map<String, Object>> markAllAsRead() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        int updated = notificationService.markAllAsRead(username);
        
        return ResponseEntity.ok(Map.of(
                "updated", updated,
                "message", updated + " notificaciones marcadas como leídas"
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Eliminar notificación",
               description = "Elimina una notificación específica del usuario")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        notificationService.delete(id, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Limpiar notificaciones antiguas (ADMIN)",
               description = "Elimina notificaciones leídas con más de 30 días (requiere rol ADMIN)")
    public ResponseEntity<Map<String, Object>> cleanupOldNotifications() {
        int deleted = notificationService.cleanupOldNotifications();
        
        return ResponseEntity.ok(Map.of(
                "deleted", deleted,
                "message", deleted + " notificaciones antiguas eliminadas"
        ));
    }
}
