package com.goats.api.controller;

import com.goats.api.dto.AchievementDto;
import com.goats.api.model.Achievement.AchievementType;
import com.goats.api.service.AchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para logros y competiciones
 * GET endpoints públicos, POST/PUT/DELETE requieren rol ADMIN
 */
@RestController
@RequestMapping("/api/achievements")
@Tag(name = "Achievements", description = "Endpoints para logros y competiciones")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping("/player/{playerId}")
    @Operation(summary = "Listar logros de jugador",
               description = "Obtiene todos los logros de un jugador con paginación")
    public Page<AchievementDto> getByPlayerId(@PathVariable Long playerId,
                                               @PageableDefault(size = 20) Pageable pageable) {
        return achievementService.getByPlayerId(playerId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener logro por ID",
               description = "Devuelve la información de un logro específico")
    public ResponseEntity<AchievementDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(achievementService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear logro",
               description = "Crea un nuevo logro para un jugador (requiere rol ADMIN)")
    public ResponseEntity<AchievementDto> create(@Valid @RequestBody AchievementDto dto) {
        AchievementDto created = achievementService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar logro",
               description = "Actualiza un logro existente (requiere rol ADMIN)")
    public ResponseEntity<AchievementDto> update(@PathVariable Long id,
                                                  @Valid @RequestBody AchievementDto dto) {
        return ResponseEntity.ok(achievementService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar logro",
               description = "Elimina un logro (requiere rol ADMIN)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        achievementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Buscar logros por tipo",
               description = "Obtiene logros filtrados por tipo (INDIVIDUAL, CLUB, NATIONAL_TEAM, RECORD, OTHER)")
    public ResponseEntity<List<AchievementDto>> getByType(@PathVariable AchievementType type) {
        return ResponseEntity.ok(achievementService.getByType(type));
    }

    @GetMapping("/player/{playerId}/type/{type}")
    @Operation(summary = "Buscar logros de jugador por tipo",
               description = "Obtiene logros de un jugador filtrados por tipo")
    public ResponseEntity<List<AchievementDto>> getByPlayerIdAndType(@PathVariable Long playerId,
                                                                      @PathVariable AchievementType type) {
        return ResponseEntity.ok(achievementService.getByPlayerIdAndType(playerId, type));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Buscar logros por año",
               description = "Obtiene logros del año especificado")
    public ResponseEntity<List<AchievementDto>> getByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(achievementService.getByYear(year));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar logros por título",
               description = "Búsqueda de logros por título (búsqueda parcial, case-insensitive)")
    public ResponseEntity<List<AchievementDto>> searchByTitle(@RequestParam String title) {
        return ResponseEntity.ok(achievementService.searchByTitle(title));
    }

    @GetMapping("/player/{playerId}/count")
    @Operation(summary = "Contar logros de jugador",
               description = "Cuenta el total de logros de un jugador")
    public ResponseEntity<Long> countByPlayerId(@PathVariable Long playerId) {
        return ResponseEntity.ok(achievementService.countByPlayerId(playerId));
    }

    @GetMapping("/player/{playerId}/count/type/{type}")
    @Operation(summary = "Contar logros de jugador por tipo",
               description = "Cuenta logros de un jugador filtrados por tipo")
    public ResponseEntity<Long> countByPlayerIdAndType(@PathVariable Long playerId,
                                                        @PathVariable AchievementType type) {
        return ResponseEntity.ok(achievementService.countByPlayerIdAndType(playerId, type));
    }
}
