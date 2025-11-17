package com.goats.api.repository;

import com.goats.api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repositorio para la entidad Player
 * Proporciona operaciones CRUD y búsquedas personalizadas
 */
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {
    
    /**
     * Busca un jugador por ID o lanza excepción si no existe
     * Método helper para evitar código duplicado
     * @param id ID del jugador
     * @return Player encontrado
     * @throws RuntimeException si el jugador no existe
     */
    default Player findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + id));
    }
}