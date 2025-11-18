package com.goats.api.controller;

import com.goats.api.dto.AchievementDto;
import com.goats.api.model.AchievementType;
import com.goats.api.service.AchievementService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementController achievementController;

    private AchievementDto testAchievement;

    @BeforeEach
    void setUp() {
        testAchievement = new AchievementDto();
        testAchievement.setId(1L);
        testAchievement.setPlayerId(1L);
        testAchievement.setTitle("Balón de Oro");
        testAchievement.setYear(2023);
        testAchievement.setType(AchievementType.INDIVIDUAL);
    }

    @Test
    void testGetByPlayerId_ReturnsAchievements() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<AchievementDto> expectedPage = new PageImpl<>(Arrays.asList(testAchievement));
        
        when(achievementService.getByPlayerId(eq(1L), any(Pageable.class))).thenReturn(expectedPage);

        Page<AchievementDto> result = achievementController.getByPlayerId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Balón de Oro", result.getContent().get(0).getTitle());
        verify(achievementService, times(1)).getByPlayerId(1L, pageable);
    }

    @Test
    void testGetById_ReturnsAchievement() {
        when(achievementService.getById(1L)).thenReturn(testAchievement);

        ResponseEntity<AchievementDto> response = achievementController.getById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Balón de Oro", response.getBody().getTitle());
        verify(achievementService, times(1)).getById(1L);
    }

    @Test
    void testCreate_Success() {
        when(achievementService.create(any(AchievementDto.class))).thenReturn(testAchievement);

        ResponseEntity<AchievementDto> response = achievementController.create(testAchievement);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Balón de Oro", response.getBody().getTitle());
        verify(achievementService, times(1)).create(testAchievement);
    }

    @Test
    void testUpdate_Success() {
        AchievementDto updated = new AchievementDto();
        updated.setTitle("Champions League");
        
        when(achievementService.update(eq(1L), any(AchievementDto.class))).thenReturn(updated);

        ResponseEntity<AchievementDto> response = achievementController.update(1L, updated);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Champions League", response.getBody().getTitle());
        verify(achievementService, times(1)).update(1L, updated);
    }

    @Test
    void testDelete_Success() {
        doNothing().when(achievementService).delete(1L);

        ResponseEntity<Void> response = achievementController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(achievementService, times(1)).delete(1L);
    }

    @Test
    void testGetByType_ReturnsAchievements() {
        List<AchievementDto> achievements = Arrays.asList(testAchievement);
        
        when(achievementService.getByType(AchievementType.INDIVIDUAL)).thenReturn(achievements);

        ResponseEntity<List<AchievementDto>> response = achievementController.getByType(AchievementType.INDIVIDUAL);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(achievementService, times(1)).getByType(AchievementType.INDIVIDUAL);
    }

    @Test
    void testGetByPlayerIdAndType_ReturnsAchievements() {
        List<AchievementDto> achievements = Arrays.asList(testAchievement);
        
        when(achievementService.getByPlayerIdAndType(1L, AchievementType.INDIVIDUAL)).thenReturn(achievements);

        ResponseEntity<List<AchievementDto>> response = achievementController.getByPlayerIdAndType(1L, AchievementType.INDIVIDUAL);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(achievementService, times(1)).getByPlayerIdAndType(1L, AchievementType.INDIVIDUAL);
    }

    @Test
    void testGetByYear_ReturnsAchievements() {
        List<AchievementDto> achievements = Arrays.asList(testAchievement);
        
        when(achievementService.getByYear(2023)).thenReturn(achievements);

        ResponseEntity<List<AchievementDto>> response = achievementController.getByYear(2023);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(achievementService, times(1)).getByYear(2023);
    }

    @Test
    void testSearchByTitle_ReturnsAchievements() {
        List<AchievementDto> achievements = Arrays.asList(testAchievement);
        
        when(achievementService.searchByTitle("Balón")).thenReturn(achievements);

        ResponseEntity<List<AchievementDto>> response = achievementController.searchByTitle("Balón");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(achievementService, times(1)).searchByTitle("Balón");
    }

    @Test
    void testCountByPlayerId_ReturnsCount() {
        when(achievementService.countByPlayerId(1L)).thenReturn(10L);

        ResponseEntity<Long> response = achievementController.countByPlayerId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10L, response.getBody());
        verify(achievementService, times(1)).countByPlayerId(1L);
    }

    @Test
    void testCountByPlayerIdAndType_ReturnsCount() {
        when(achievementService.countByPlayerIdAndType(1L, AchievementType.CLUB)).thenReturn(5L);

        ResponseEntity<Long> response = achievementController.countByPlayerIdAndType(1L, AchievementType.CLUB);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
        verify(achievementService, times(1)).countByPlayerIdAndType(1L, AchievementType.CLUB);
    }

    @Test
    void testGetByPlayerId_EmptyResult() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<AchievementDto> emptyPage = Page.empty();
        
        when(achievementService.getByPlayerId(eq(999L), any(Pageable.class))).thenReturn(emptyPage);

        Page<AchievementDto> result = achievementController.getByPlayerId(999L, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }
}
