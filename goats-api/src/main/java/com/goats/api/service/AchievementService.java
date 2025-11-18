package com.goats.api.service;

import com.goats.api.dto.AchievementDto;
import com.goats.api.model.Achievement;
import com.goats.api.model.AchievementType;
import com.goats.api.model.Player;
import com.goats.api.repository.AchievementRepository;
import com.goats.api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de logros y competiciones
 */
@Service
@Transactional
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Obtiene todos los logros de un jugador
     */
    public Page<AchievementDto> getByPlayerId(Long playerId, Pageable pageable) {
        return achievementRepository.findByPlayerId(playerId, pageable)
                .map(this::toDto);
    }

    /**
     * Obtiene un logro por ID
     */
    @SuppressWarnings("null")
    public AchievementDto getById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + id));
        return toDto(achievement);
    }

    /**
     * Crea un nuevo logro
     * Genera notificaciones a suscriptores del jugador
     */
    @SuppressWarnings("null")
    public AchievementDto create(AchievementDto dto) {
        Player player = playerRepository.findById(dto.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + dto.getPlayerId()));

        Achievement achievement = new Achievement();
        achievement.setPlayer(player);
        updateFromDto(achievement, dto);

        achievement = achievementRepository.save(achievement);
        
        // Notificar a suscriptores del jugador
        try {
            notificationService.notifyNewAchievement(player.getId(), achievement.getTitle());
        } catch (Exception e) {
            // Log error pero no fallar la creación
            System.err.println("Error sending notifications: " + e.getMessage());
        }
        
        return toDto(achievement);
    }

    /**
     * Actualiza un logro existente
     */
    @SuppressWarnings("null")
    public AchievementDto update(Long id, AchievementDto dto) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + id));

        updateFromDto(achievement, dto);
        achievement = achievementRepository.save(achievement);

        return toDto(achievement);
    }

    /**
     * Elimina un logro
     */
    @SuppressWarnings("null")
    public void delete(Long id) {
        if (!achievementRepository.existsById(id)) {
            throw new RuntimeException("Achievement not found with id: " + id);
        }
        achievementRepository.deleteById(id);
    }

    /**
     * Busca logros por tipo
     */
    public List<AchievementDto> getByType(AchievementType type) {
        return achievementRepository.findByType(type)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca logros por jugador y tipo
     */
    public List<AchievementDto> getByPlayerIdAndType(Long playerId, AchievementType type) {
        return achievementRepository.findByPlayerIdAndType(playerId, type)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca logros por año
     */
    public List<AchievementDto> getByYear(Integer year) {
        return achievementRepository.findByYear(year)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca logros por título (búsqueda parcial)
     */
    public List<AchievementDto> searchByTitle(String title) {
        return achievementRepository.findByTitleContaining(title)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Cuenta logros de un jugador
     */
    public long countByPlayerId(Long playerId) {
        return achievementRepository.countByPlayerId(playerId);
    }

    /**
     * Cuenta logros de un jugador por tipo
     */
    public long countByPlayerIdAndType(Long playerId, AchievementType type) {
        return achievementRepository.countByPlayerIdAndType(playerId, type);
    }

    // Métodos de utilidad
    private AchievementDto toDto(Achievement achievement) {
        return new AchievementDto(
                achievement.getId(),
                achievement.getPlayer().getId(),
                achievement.getTitle(),
                achievement.getDescription(),
                achievement.getYear(),
                achievement.getType(),
                achievement.getOrganization()
        );
    }

    private void updateFromDto(Achievement achievement, AchievementDto dto) {
        achievement.setTitle(dto.getTitle());
        achievement.setDescription(dto.getDescription());
        achievement.setYear(dto.getYear());
        achievement.setType(dto.getType());
        achievement.setOrganization(dto.getOrganization());
    }
}
