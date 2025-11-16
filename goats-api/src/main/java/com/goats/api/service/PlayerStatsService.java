package com.goats.api.service;

import com.goats.api.dto.PlayerStatsDto;
import com.goats.api.model.Player;
import com.goats.api.model.PlayerStats;
import com.goats.api.repository.PlayerRepository;
import com.goats.api.repository.PlayerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de estadísticas de jugadores
 */
@Service
@Transactional
public class PlayerStatsService {

    @Autowired
    private PlayerStatsRepository statsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Obtiene las estadísticas de un jugador
     */
    public PlayerStatsDto getByPlayerId(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

        PlayerStats stats = statsRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Stats not found for player id: " + playerId));

        return toDto(stats);
    }

    /**
     * Crea estadísticas para un jugador
     */
    public PlayerStatsDto create(PlayerStatsDto dto) {
        Player player = playerRepository.findById(dto.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + dto.getPlayerId()));

        if (statsRepository.existsByPlayerId(dto.getPlayerId())) {
            throw new RuntimeException("Stats already exist for player id: " + dto.getPlayerId());
        }

        PlayerStats stats = new PlayerStats(player);
        updateFromDto(stats, dto);

        stats = statsRepository.save(stats);
        return toDto(stats);
    }

    /**
     * Actualiza estadísticas de un jugador
     */
    public PlayerStatsDto update(Long playerId, PlayerStatsDto dto) {
        PlayerStats stats = statsRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Stats not found for player id: " + playerId));

        updateFromDto(stats, dto);
        stats = statsRepository.save(stats);

        return toDto(stats);
    }

    /**
     * Elimina estadísticas de un jugador
     */
    public void delete(Long playerId) {
        PlayerStats stats = statsRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Stats not found for player id: " + playerId));

        statsRepository.delete(stats);
    }

    /**
     * Obtiene top jugadores por goles
     */
    public List<PlayerStatsDto> getTopByGoals(int limit) {
        return statsRepository.findTopByGoals(PageRequest.of(0, limit))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene top jugadores por asistencias
     */
    public List<PlayerStatsDto> getTopByAssists(int limit) {
        return statsRepository.findTopByAssists(PageRequest.of(0, limit))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene top jugadores por trofeos
     */
    public List<PlayerStatsDto> getTopByTrophies(int limit) {
        return statsRepository.findTopByTrophies(PageRequest.of(0, limit))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene top jugadores por Balones de Oro
     */
    public List<PlayerStatsDto> getTopByBallonDOr(int limit) {
        return statsRepository.findTopByBallonDOr(PageRequest.of(0, limit))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Métodos de utilidad
    private PlayerStatsDto toDto(PlayerStats stats) {
        return new PlayerStatsDto(
                stats.getId(),
                stats.getPlayer().getId(),
                stats.getGoals(),
                stats.getAssists(),
                stats.getMatchesPlayed(),
                stats.getTrophies(),
                stats.getYellowCards(),
                stats.getRedCards(),
                stats.getMinutesPlayed(),
                stats.getBallonDOrWins(),
                stats.getChampionsLeagueWins(),
                stats.getWorldCupWins()
        );
    }

    private void updateFromDto(PlayerStats stats, PlayerStatsDto dto) {
        stats.setGoals(dto.getGoals());
        stats.setAssists(dto.getAssists());
        stats.setMatchesPlayed(dto.getMatchesPlayed());
        stats.setTrophies(dto.getTrophies());
        stats.setYellowCards(dto.getYellowCards());
        stats.setRedCards(dto.getRedCards());
        stats.setMinutesPlayed(dto.getMinutesPlayed());
        stats.setBallonDOrWins(dto.getBallonDOrWins());
        stats.setChampionsLeagueWins(dto.getChampionsLeagueWins());
        stats.setWorldCupWins(dto.getWorldCupWins());
    }
}
