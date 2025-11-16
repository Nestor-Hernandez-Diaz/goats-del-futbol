package com.goats.api.service;

import com.goats.api.dto.PlayerDto;
import com.goats.api.model.Player;
import com.goats.api.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio de gestión de jugadores
 * Proporciona operaciones CRUD y búsquedas con filtros
 */
@Service
@Transactional
public class PlayerService {
  private final PlayerRepository repository;

  public PlayerService(PlayerRepository repository) {
    this.repository = repository;
  }

  /**
   * Lista jugadores con paginación y filtros opcionales
   */
  public Page<PlayerDto> list(Pageable pageable, Optional<String> name, Optional<String> country, Optional<String> position) {
    Specification<Player> byName = name.filter(s -> !s.isBlank())
      .map(v -> (Specification<Player>) (root, q, cb) -> cb.like(cb.lower(root.get("name")), "%" + v.toLowerCase() + "%"))
      .orElse(null);
    Specification<Player> byCountry = country.filter(s -> !s.isBlank())
      .map(v -> (Specification<Player>) (root, q, cb) -> cb.equal(root.get("country"), v))
      .orElse(null);
    Specification<Player> byPosition = position.filter(s -> !s.isBlank())
      .map(v -> (Specification<Player>) (root, q, cb) -> cb.equal(root.get("position"), v))
      .orElse(null);

    Specification<Player> spec = null;
    if (byName != null) spec = (spec == null) ? byName : spec.and(byName);
    if (byCountry != null) spec = (spec == null) ? byCountry : spec.and(byCountry);
    if (byPosition != null) spec = (spec == null) ? byPosition : spec.and(byPosition);

    return (spec != null ? repository.findAll(spec, pageable) : repository.findAll(pageable))
      .map(PlayerDto::from);
  }

  /**
   * Obtiene un jugador por ID
   */
  public PlayerDto get(Long id) {
    if (id == null) throw new IllegalArgumentException("id must not be null");
    Player p = repository.findById(id).orElseThrow(() -> 
      new RuntimeException("Player not found with id: " + id));
    return PlayerDto.from(p);
  }

  /**
   * Crea un nuevo jugador
   */
  public PlayerDto create(PlayerDto playerDto) {
    Player player = new Player();
    player.setName(playerDto.getName());
    player.setNickname(playerDto.getNickname());
    player.setCountry(playerDto.getCountry());
    player.setPosition(playerDto.getPosition());
    player.setBiography(playerDto.getBiography());
    
    Player saved = repository.save(player);
    return PlayerDto.from(saved);
  }

  /**
   * Actualiza un jugador existente
   */
  public PlayerDto update(Long id, PlayerDto playerDto) {
    Player player = repository.findById(id).orElseThrow(() -> 
      new RuntimeException("Player not found with id: " + id));
    
    player.setName(playerDto.getName());
    player.setNickname(playerDto.getNickname());
    player.setCountry(playerDto.getCountry());
    player.setPosition(playerDto.getPosition());
    player.setBiography(playerDto.getBiography());
    
    Player updated = repository.save(player);
    return PlayerDto.from(updated);
  }

  /**
   * Elimina un jugador
   */
  public void delete(Long id) {
    Player player = repository.findById(id).orElseThrow(() -> 
      new RuntimeException("Player not found with id: " + id));
    repository.delete(player);
  }
}