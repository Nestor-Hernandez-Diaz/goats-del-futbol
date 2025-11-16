package com.goats.api.controller;

import com.goats.api.dto.PlayerDto;
import com.goats.api.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador REST para gestión de jugadores
 * GET endpoints públicos, POST/PUT/DELETE requieren rol ADMIN
 */
@RestController
@RequestMapping("/api/players")
@Tag(name = "Players", description = "Endpoints para gestión de jugadores")
public class PlayerController {
  private final PlayerService service;

  public PlayerController(PlayerService service) {
    this.service = service;
  }

  @GetMapping
  @Operation(summary = "Listar jugadores", 
             description = "Obtiene una lista paginada de jugadores con filtros opcionales")
  public Page<PlayerDto> list(@PageableDefault(size = 12) Pageable pageable,
                              @RequestParam Optional<String> name,
                              @RequestParam Optional<String> country,
                              @RequestParam Optional<String> position) {
    return service.list(pageable, name, country, position);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener jugador por ID", 
             description = "Devuelve la información detallada de un jugador")
  public PlayerDto get(@PathVariable Long id) {
    return service.get(id);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Crear jugador", 
             description = "Crea un nuevo jugador (requiere rol ADMIN)")
  public ResponseEntity<PlayerDto> create(@Valid @RequestBody PlayerDto playerDto) {
    PlayerDto created = service.create(playerDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Actualizar jugador", 
             description = "Actualiza la información de un jugador existente (requiere rol ADMIN)")
  public ResponseEntity<PlayerDto> update(@PathVariable Long id, @Valid @RequestBody PlayerDto playerDto) {
    PlayerDto updated = service.update(id, playerDto);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Eliminar jugador", 
             description = "Elimina un jugador del sistema (requiere rol ADMIN)")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}