package com.goats.api.controller;

import com.goats.api.dto.PlayerStatsDto;
import com.goats.api.service.PlayerStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para estadísticas de jugadores
 * GET endpoints públicos, POST/PUT/DELETE requieren rol ADMIN
 */
@RestController
@RequestMapping("/api/stats")
@Tag(name = "Player Stats", description = "Endpoints para estadísticas de jugadores")
public class PlayerStatsController {

    @Autowired
    private PlayerStatsService statsService;

    @GetMapping("/player/{playerId}")
    @Operation(summary = "Obtener estadísticas de jugador",
               description = "Devuelve las estadísticas detalladas de un jugador")
    public ResponseEntity<PlayerStatsDto> getByPlayerId(@PathVariable Long playerId) {
        return ResponseEntity.ok(statsService.getByPlayerId(playerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear estadísticas",
               description = "Crea estadísticas para un jugador (requiere rol ADMIN)")
    public ResponseEntity<PlayerStatsDto> create(@Valid @RequestBody PlayerStatsDto dto) {
        PlayerStatsDto created = statsService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/player/{playerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar estadísticas",
               description = "Actualiza las estadísticas de un jugador (requiere rol ADMIN)")
    public ResponseEntity<PlayerStatsDto> update(@PathVariable Long playerId,
                                                  @Valid @RequestBody PlayerStatsDto dto) {
        return ResponseEntity.ok(statsService.update(playerId, dto));
    }

    @DeleteMapping("/player/{playerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar estadísticas",
               description = "Elimina las estadísticas de un jugador (requiere rol ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Long playerId) {
        statsService.delete(playerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top/goals")
    @Operation(summary = "Top goleadores",
               description = "Obtiene el ranking de jugadores por goles")
    public ResponseEntity<List<PlayerStatsDto>> getTopByGoals(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statsService.getTopByGoals(limit));
    }

    @GetMapping("/top/assists")
    @Operation(summary = "Top asistentes",
               description = "Obtiene el ranking de jugadores por asistencias")
    public ResponseEntity<List<PlayerStatsDto>> getTopByAssists(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statsService.getTopByAssists(limit));
    }

    @GetMapping("/top/trophies")
    @Operation(summary = "Top ganadores",
               description = "Obtiene el ranking de jugadores por trofeos")
    public ResponseEntity<List<PlayerStatsDto>> getTopByTrophies(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statsService.getTopByTrophies(limit));
    }

    @GetMapping("/top/ballondor")
    @Operation(summary = "Top Balones de Oro",
               description = "Obtiene el ranking de jugadores por Balones de Oro ganados")
    public ResponseEntity<List<PlayerStatsDto>> getTopByBallonDOr(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statsService.getTopByBallonDOr(limit));
    }
}
