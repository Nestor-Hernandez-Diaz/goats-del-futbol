package com.goats.api.controller;

import com.goats.api.dto.PlayerStatsDto;
import com.goats.api.service.PlayerStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerStatsControllerTest {

    @Mock
    private PlayerStatsService statsService;

    @InjectMocks
    private PlayerStatsController statsController;

    private PlayerStatsDto testStats;

    @BeforeEach
    void setUp() {
        testStats = new PlayerStatsDto();
        testStats.setId(1L);
        testStats.setPlayerId(1L);
        testStats.setGoals(850);
        testStats.setAssists(400);
        testStats.setMatchesPlayed(1000);
        testStats.setTrophies(45);
        testStats.setBallonDOrWins(8);
    }

    @Test
    void testGetByPlayerId_ReturnsStats() {
        when(statsService.getByPlayerId(1L)).thenReturn(testStats);

        ResponseEntity<PlayerStatsDto> response = statsController.getByPlayerId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(850, response.getBody().getGoals());
        verify(statsService, times(1)).getByPlayerId(1L);
    }

    @Test
    void testCreate_Success() {
        when(statsService.create(any(PlayerStatsDto.class))).thenReturn(testStats);

        ResponseEntity<PlayerStatsDto> response = statsController.create(testStats);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(850, response.getBody().getGoals());
        verify(statsService, times(1)).create(testStats);
    }

    @Test
    void testUpdate_Success() {
        PlayerStatsDto updated = new PlayerStatsDto();
        updated.setGoals(900);
        
        when(statsService.update(eq(1L), any(PlayerStatsDto.class))).thenReturn(updated);

        ResponseEntity<PlayerStatsDto> response = statsController.update(1L, updated);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(900, response.getBody().getGoals());
        verify(statsService, times(1)).update(1L, updated);
    }

    @Test
    void testDelete_Success() {
        doNothing().when(statsService).delete(1L);

        ResponseEntity<Void> response = statsController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(statsService, times(1)).delete(1L);
    }

    @Test
    void testGetTopByGoals_ReturnsRanking() {
        List<PlayerStatsDto> topGoals = Arrays.asList(testStats);
        
        when(statsService.getTopByGoals(10)).thenReturn(topGoals);

        ResponseEntity<List<PlayerStatsDto>> response = statsController.getTopByGoals(10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(850, response.getBody().get(0).getGoals());
        verify(statsService, times(1)).getTopByGoals(10);
    }

    @Test
    void testGetTopByAssists_ReturnsRanking() {
        List<PlayerStatsDto> topAssists = Arrays.asList(testStats);
        
        when(statsService.getTopByAssists(10)).thenReturn(topAssists);

        ResponseEntity<List<PlayerStatsDto>> response = statsController.getTopByAssists(10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(400, response.getBody().get(0).getAssists());
        verify(statsService, times(1)).getTopByAssists(10);
    }

    @Test
    void testGetTopByTrophies_ReturnsRanking() {
        List<PlayerStatsDto> topTrophies = Arrays.asList(testStats);
        
        when(statsService.getTopByTrophies(10)).thenReturn(topTrophies);

        ResponseEntity<List<PlayerStatsDto>> response = statsController.getTopByTrophies(10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(45, response.getBody().get(0).getTrophies());
        verify(statsService, times(1)).getTopByTrophies(10);
    }

    @Test
    void testGetTopByBallonDOr_ReturnsRanking() {
        List<PlayerStatsDto> topBallonDOr = Arrays.asList(testStats);
        
        when(statsService.getTopByBallonDOr(10)).thenReturn(topBallonDOr);

        ResponseEntity<List<PlayerStatsDto>> response = statsController.getTopByBallonDOr(10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(8, response.getBody().get(0).getBallonDOrWins());
        verify(statsService, times(1)).getTopByBallonDOr(10);
    }

    @Test
    void testGetTopByGoals_WithCustomLimit() {
        List<PlayerStatsDto> topGoals = Arrays.asList(testStats, testStats, testStats);
        
        when(statsService.getTopByGoals(3)).thenReturn(topGoals);

        ResponseEntity<List<PlayerStatsDto>> response = statsController.getTopByGoals(3);

        assertNotNull(response);
        assertEquals(3, response.getBody().size());
        verify(statsService, times(1)).getTopByGoals(3);
    }

    @Test
    void testGetTopByAssists_EmptyResult() {
        when(statsService.getTopByAssists(10)).thenReturn(Arrays.asList());

        ResponseEntity<List<PlayerStatsDto>> response = statsController.getTopByAssists(10);

        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetByPlayerId_DifferentPlayers() {
        PlayerStatsDto stats1 = new PlayerStatsDto();
        stats1.setPlayerId(1L);
        stats1.setGoals(850);
        
        PlayerStatsDto stats2 = new PlayerStatsDto();
        stats2.setPlayerId(2L);
        stats2.setGoals(700);
        
        when(statsService.getByPlayerId(1L)).thenReturn(stats1);
        when(statsService.getByPlayerId(2L)).thenReturn(stats2);

        ResponseEntity<PlayerStatsDto> response1 = statsController.getByPlayerId(1L);
        ResponseEntity<PlayerStatsDto> response2 = statsController.getByPlayerId(2L);

        assertEquals(850, response1.getBody().getGoals());
        assertEquals(700, response2.getBody().getGoals());
    }
}
