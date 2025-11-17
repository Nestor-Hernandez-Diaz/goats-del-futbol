package com.goats.api.service;

import com.goats.api.dto.PlayerStatsDto;
import com.goats.api.model.Player;
import com.goats.api.model.PlayerStats;
import com.goats.api.repository.PlayerRepository;
import com.goats.api.repository.PlayerStatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para PlayerStatsService
 */
@ExtendWith(MockitoExtension.class)
class PlayerStatsServiceTest {

    @Mock
    private PlayerStatsRepository statsRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerStatsService playerStatsService;

    private Player testPlayer;
    private PlayerStats testStats;
    private PlayerStatsDto testStatsDto;

    @BeforeEach
    void setUp() {
        // Setup test player
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("Lionel Messi");
        testPlayer.setCountry("Argentina");
        testPlayer.setPosition("Delantero");

        // Setup test stats
        testStats = new PlayerStats(testPlayer);
        testStats.setId(1L);
        testStats.setGoals(850);
        testStats.setAssists(400);
        testStats.setMatchesPlayed(1050);
        testStats.setTrophies(45);
        testStats.setYellowCards(95);
        testStats.setRedCards(3);
        testStats.setMinutesPlayed(90000.0);
        testStats.setBallonDOrWins(8);
        testStats.setChampionsLeagueWins(4);
        testStats.setWorldCupWins(1);

        // Setup test DTO
        testStatsDto = new PlayerStatsDto(
                1L, 1L, 850, 400, 1050, 45, 95, 3, 90000.0, 8, 4, 1
        );
    }

    @Test
    void testGetByPlayerId_Success() {
        // Given
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(statsRepository.findByPlayerId(1L)).thenReturn(Optional.of(testStats));

        // When
        PlayerStatsDto result = playerStatsService.getByPlayerId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getPlayerId());
        assertEquals(850, result.getGoals());
        assertEquals(400, result.getAssists());
        assertEquals(8, result.getBallonDOrWins());
        verify(playerRepository, times(1)).findById(1L);
        verify(statsRepository, times(1)).findByPlayerId(1L);
    }

    @Test
    void testGetByPlayerId_PlayerNotFound_ThrowsException() {
        // Given
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> playerStatsService.getByPlayerId(999L));
        verify(playerRepository, times(1)).findById(999L);
        verify(statsRepository, never()).findByPlayerId(anyLong());
    }

    @Test
    void testGetByPlayerId_StatsNotFound_ThrowsException() {
        // Given
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(statsRepository.findByPlayerId(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> playerStatsService.getByPlayerId(1L));
        verify(playerRepository, times(1)).findById(1L);
        verify(statsRepository, times(1)).findByPlayerId(1L);
    }

    @Test
    void testCreate_Success() {
        // Given
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(statsRepository.existsByPlayerId(1L)).thenReturn(false);
        when(statsRepository.save(any(PlayerStats.class))).thenReturn(testStats);

        // When
        PlayerStatsDto result = playerStatsService.create(testStatsDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getPlayerId());
        assertEquals(850, result.getGoals());
        verify(playerRepository, times(1)).findById(1L);
        verify(statsRepository, times(1)).existsByPlayerId(1L);
        verify(statsRepository, times(1)).save(any(PlayerStats.class));
    }

    @Test
    void testCreate_PlayerNotFound_ThrowsException() {
        // Given
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> playerStatsService.create(testStatsDto));
        verify(playerRepository, times(1)).findById(1L);
        verify(statsRepository, never()).save(any(PlayerStats.class));
    }

    @Test
    void testCreate_StatsAlreadyExist_ThrowsException() {
        // Given
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(statsRepository.existsByPlayerId(1L)).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> playerStatsService.create(testStatsDto));
        verify(playerRepository, times(1)).findById(1L);
        verify(statsRepository, times(1)).existsByPlayerId(1L);
        verify(statsRepository, never()).save(any(PlayerStats.class));
    }

    @Test
    void testUpdate_Success() {
        // Given
        when(statsRepository.findByPlayerId(1L)).thenReturn(Optional.of(testStats));
        when(statsRepository.save(any(PlayerStats.class))).thenReturn(testStats);

        PlayerStatsDto updateDto = new PlayerStatsDto(
                1L, 1L, 900, 450, 1100, 50, 100, 3, 95000.0, 9, 5, 1
        );

        // When
        PlayerStatsDto result = playerStatsService.update(1L, updateDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getPlayerId());
        verify(statsRepository, times(1)).findByPlayerId(1L);
        verify(statsRepository, times(1)).save(any(PlayerStats.class));
    }

    @Test
    void testUpdate_StatsNotFound_ThrowsException() {
        // Given
        when(statsRepository.findByPlayerId(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> playerStatsService.update(999L, testStatsDto));
        verify(statsRepository, times(1)).findByPlayerId(999L);
        verify(statsRepository, never()).save(any(PlayerStats.class));
    }

    @Test
    void testDelete_Success() {
        // Given
        when(statsRepository.findByPlayerId(1L)).thenReturn(Optional.of(testStats));
        doNothing().when(statsRepository).delete(any(PlayerStats.class));

        // When
        playerStatsService.delete(1L);

        // Then
        verify(statsRepository, times(1)).findByPlayerId(1L);
        verify(statsRepository, times(1)).delete(any(PlayerStats.class));
    }

    @Test
    void testDelete_StatsNotFound_ThrowsException() {
        // Given
        when(statsRepository.findByPlayerId(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> playerStatsService.delete(999L));
        verify(statsRepository, times(1)).findByPlayerId(999L);
        verify(statsRepository, never()).delete(any(PlayerStats.class));
    }

    @Test
    void testGetTopByGoals() {
        // Given
        List<PlayerStats> topStats = Arrays.asList(testStats);
        when(statsRepository.findTopByGoals(any(PageRequest.class))).thenReturn(topStats);

        // When
        List<PlayerStatsDto> result = playerStatsService.getTopByGoals(10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(850, result.get(0).getGoals());
        verify(statsRepository, times(1)).findTopByGoals(any(PageRequest.class));
    }

    @Test
    void testGetTopByAssists() {
        // Given
        List<PlayerStats> topStats = Arrays.asList(testStats);
        when(statsRepository.findTopByAssists(any(PageRequest.class))).thenReturn(topStats);

        // When
        List<PlayerStatsDto> result = playerStatsService.getTopByAssists(10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(400, result.get(0).getAssists());
        verify(statsRepository, times(1)).findTopByAssists(any(PageRequest.class));
    }

    @Test
    void testGetTopByTrophies() {
        // Given
        List<PlayerStats> topStats = Arrays.asList(testStats);
        when(statsRepository.findTopByTrophies(any(PageRequest.class))).thenReturn(topStats);

        // When
        List<PlayerStatsDto> result = playerStatsService.getTopByTrophies(10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(45, result.get(0).getTrophies());
        verify(statsRepository, times(1)).findTopByTrophies(any(PageRequest.class));
    }

    @Test
    void testGetTopByBallonDOr() {
        // Given
        List<PlayerStats> topStats = Arrays.asList(testStats);
        when(statsRepository.findTopByBallonDOr(any(PageRequest.class))).thenReturn(topStats);

        // When
        List<PlayerStatsDto> result = playerStatsService.getTopByBallonDOr(10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(8, result.get(0).getBallonDOrWins());
        verify(statsRepository, times(1)).findTopByBallonDOr(any(PageRequest.class));
    }
}
