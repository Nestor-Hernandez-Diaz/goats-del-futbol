package com.goats.api.controller;

import com.goats.api.dto.SubscriptionDto;
import com.goats.api.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * Controlador REST para suscripciones de usuarios a jugadores
 * Requiere autenticación para todas las operaciones
 */
@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscriptions", description = "Endpoints para suscripciones de usuarios a jugadores")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar suscripciones de usuario",
               description = "Obtiene todas las suscripciones de un usuario (requiere autenticación)")
    public Page<SubscriptionDto> getByUserId(@PathVariable Long userId,
                                              @RequestParam(required = false) Boolean active,
                                              @PageableDefault(size = 20) Pageable pageable) {
        return subscriptionService.getByUserId(userId, active, pageable);
    }

    @GetMapping("/player/{playerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar suscriptores de jugador",
               description = "Obtiene todos los suscriptores de un jugador (requiere rol ADMIN)")
    public Page<SubscriptionDto> getByPlayerId(@PathVariable Long playerId,
                                                @RequestParam(required = false) Boolean active,
                                                @PageableDefault(size = 20) Pageable pageable) {
        return subscriptionService.getByPlayerId(playerId, active, pageable);
    }

    @GetMapping("/player/{playerId}/check")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Verificar suscripción",
               description = "Verifica si el usuario actual está suscrito a un jugador")
    public ResponseEntity<Boolean> checkSubscription(@PathVariable Long playerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        boolean isSubscribed = subscriptionService.isSubscribed(username, playerId);
        return ResponseEntity.ok(isSubscribed);
    }

    @PostMapping("/player/{playerId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Suscribirse a jugador",
               description = "Crea una suscripción a un jugador (requiere autenticación)")
    public ResponseEntity<SubscriptionDto> subscribe(@PathVariable Long playerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        SubscriptionDto created = subscriptionService.subscribe(playerId, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/player/{playerId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Desuscribirse de jugador",
               description = "Desactiva una suscripción a un jugador (requiere autenticación)")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long playerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        subscriptionService.unsubscribe(playerId, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/player/{playerId}/notifications")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Alternar notificaciones",
               description = "Activa o desactiva las notificaciones de una suscripción")
    public ResponseEntity<SubscriptionDto> toggleNotifications(@PathVariable Long playerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        return ResponseEntity.ok(subscriptionService.toggleNotifications(playerId, username));
    }

    @GetMapping("/player/{playerId}/count")
    @Operation(summary = "Contar suscriptores",
               description = "Cuenta los suscriptores activos de un jugador (público)")
    public ResponseEntity<Long> countActiveSubscribers(@PathVariable Long playerId) {
        return ResponseEntity.ok(subscriptionService.countActiveSubscribers(playerId));
    }

    @GetMapping("/player/{playerId}/notifications")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Suscriptores con notificaciones",
               description = "Obtiene suscriptores con notificaciones habilitadas (requiere rol ADMIN)")
    public ResponseEntity<List<SubscriptionDto>> getSubscribersWithNotifications(@PathVariable Long playerId) {
        return ResponseEntity.ok(subscriptionService.getSubscribersWithNotifications(playerId));
    }
}
