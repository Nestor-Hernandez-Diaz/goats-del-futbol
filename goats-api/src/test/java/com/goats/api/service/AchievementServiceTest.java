package com.goats.api.service;

import com.goats.api.dto.AchievementDto;
import com.goats.api.model.Achievement;
import com.goats.api.model.Achievement.AchievementType;
import com.goats.api.model.Player;
import com.goats.api.repository.AchievementRepository;
import com.goats.api.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para AchievementService
 */
@ExtendWith(MockitoExtension.class)
class AchievementServiceTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private AchievementService achievementService;

    private Player testPlayer;
    private Achievement testAchievement;
    private AchievementDto testAchievementDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        // Setup test player
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("Lionel Messi");
        testPlayer.setCountry("Argentina");
        testPlayer.setPosition("Delantero");

        // Setup test achievement
        testAchievement = new Achievement();
        testAchievement.setId(1L);
        testAchievement.setPlayer(testPlayer);
        testAchievement.setTitle("Copa del Mundo");
        testAchievement.setDescription("Campeón del mundo con Argentina");
        testAchievement.setYear(2022);
        testAchievement.setType(AchievementType.NATIONAL_TEAM);
        testAchievement.setOrganization("FIFA");

        // Setup test DTO
        testAchievementDto = new AchievementDto(
                1L, 1L, "Copa del Mundo", "Campeón del mundo con Argentina",
                2022, AchievementType.NATIONAL_TEAM, "FIFA"
        );
    }

    @Test
    void testGetByPlayerId_Success() {
        // Given
        List<Achievement> achievements = Arrays.asList(testAchievement);
        Page<Achievement> achievementPage = new PageImpl<>(achievements);
        when(achievementRepository.findByPlayerId(1L, pageable)).thenReturn(achievementPage);

        // When
        Page<AchievementDto> result = achievementService.getByPlayerId(1L, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Copa del Mundo", result.getContent().get(0).getTitle());
        verify(achievementRepository, times(1)).findByPlayerId(1L, pageable);
    }

    @Test
    void testGetById_Success() {
        // Given
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(testAchievement));

        // When
        AchievementDto result = achievementService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Copa del Mundo", result.getTitle());
        assertEquals(2022, result.getYear());
        verify(achievementRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound_ThrowsException() {
        // Given
        when(achievementRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> achievementService.getById(999L));
        verify(achievementRepository, times(1)).findById(999L);
    }

    @Test
    void testCreate_Success() {
        // Given
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(testAchievement);

        // When
        AchievementDto result = achievementService.create(testAchievementDto);

        // Then
        assertNotNull(result);
        assertEquals("Copa del Mundo", result.getTitle());
        assertEquals(1L, result.getPlayerId());
        verify(playerRepository, times(1)).findById(1L);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void testCreate_PlayerNotFound_ThrowsException() {
        // Given
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> achievementService.create(testAchievementDto));
        verify(playerRepository, times(1)).findById(1L);
        verify(achievementRepository, never()).save(any(Achievement.class));
    }

    @Test
    void testUpdate_Success() {
        // Given
        when(achievementRepository.findById(1L)).thenReturn(Optional.of(testAchievement));
        when(achievementRepository.save(any(Achievement.class))).thenReturn(testAchievement);

        AchievementDto updateDto = new AchievementDto(
                1L, 1L, "Champions League", "Campeón de Europa", 2015,
                AchievementType.CLUB, "UEFA"
        );

        // When
        AchievementDto result = achievementService.update(1L, updateDto);

        // Then
        assertNotNull(result);
        verify(achievementRepository, times(1)).findById(1L);
        verify(achievementRepository, times(1)).save(any(Achievement.class));
    }

    @Test
    void testUpdate_NotFound_ThrowsException() {
        // Given
        when(achievementRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> achievementService.update(999L, testAchievementDto));
        verify(achievementRepository, times(1)).findById(999L);
        verify(achievementRepository, never()).save(any(Achievement.class));
    }

    @Test
    void testDelete_Success() {
        // Given
        when(achievementRepository.existsById(1L)).thenReturn(true);
        doNothing().when(achievementRepository).deleteById(1L);

        // When
        achievementService.delete(1L);

        // Then
        verify(achievementRepository, times(1)).existsById(1L);
        verify(achievementRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound_ThrowsException() {
        // Given
        when(achievementRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> achievementService.delete(999L));
        verify(achievementRepository, times(1)).existsById(999L);
        verify(achievementRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetByType() {
        // Given
        List<Achievement> achievements = Arrays.asList(testAchievement);
        when(achievementRepository.findByType(AchievementType.NATIONAL_TEAM)).thenReturn(achievements);

        // When
        List<AchievementDto> result = achievementService.getByType(AchievementType.NATIONAL_TEAM);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(AchievementType.NATIONAL_TEAM, result.get(0).getType());
        verify(achievementRepository, times(1)).findByType(AchievementType.NATIONAL_TEAM);
    }

    @Test
    void testGetByPlayerIdAndType() {
        // Given
        List<Achievement> achievements = Arrays.asList(testAchievement);
        when(achievementRepository.findByPlayerIdAndType(1L, AchievementType.NATIONAL_TEAM))
                .thenReturn(achievements);

        // When
        List<AchievementDto> result = achievementService.getByPlayerIdAndType(1L, AchievementType.NATIONAL_TEAM);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(achievementRepository, times(1)).findByPlayerIdAndType(1L, AchievementType.NATIONAL_TEAM);
    }

    @Test
    void testGetByYear() {
        // Given
        List<Achievement> achievements = Arrays.asList(testAchievement);
        when(achievementRepository.findByYear(2022)).thenReturn(achievements);

        // When
        List<AchievementDto> result = achievementService.getByYear(2022);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2022, result.get(0).getYear());
        verify(achievementRepository, times(1)).findByYear(2022);
    }

    @Test
    void testSearchByTitle() {
        // Given
        List<Achievement> achievements = Arrays.asList(testAchievement);
        when(achievementRepository.findByTitleContaining("Copa")).thenReturn(achievements);

        // When
        List<AchievementDto> result = achievementService.searchByTitle("Copa");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().contains("Copa"));
        verify(achievementRepository, times(1)).findByTitleContaining("Copa");
    }

    @Test
    void testCountByPlayerId() {
        // Given
        when(achievementRepository.countByPlayerId(1L)).thenReturn(45L);

        // When
        long count = achievementService.countByPlayerId(1L);

        // Then
        assertEquals(45L, count);
        verify(achievementRepository, times(1)).countByPlayerId(1L);
    }

    @Test
    void testCountByPlayerIdAndType() {
        // Given
        when(achievementRepository.countByPlayerIdAndType(1L, AchievementType.NATIONAL_TEAM))
                .thenReturn(12L);

        // When
        long count = achievementService.countByPlayerIdAndType(1L, AchievementType.NATIONAL_TEAM);

        // Then
        assertEquals(12L, count);
        verify(achievementRepository, times(1)).countByPlayerIdAndType(1L, AchievementType.NATIONAL_TEAM);
    }
}
