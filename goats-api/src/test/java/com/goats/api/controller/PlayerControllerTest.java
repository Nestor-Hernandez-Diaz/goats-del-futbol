package com.goats.api.controller;

import com.goats.api.dto.PlayerDto;
import com.goats.api.service.PlayerService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    private PlayerDto testPlayer;

    @BeforeEach
    void setUp() {
        testPlayer = new PlayerDto();
        testPlayer.setId(1L);
        testPlayer.setName("Lionel Messi");
        testPlayer.setCountry("Argentina");
        testPlayer.setPosition("Delantero");
        testPlayer.setNickname("La Pulga");
    }

    @Test
    void testList_WithoutFilters() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<PlayerDto> expectedPage = new PageImpl<>(Arrays.asList(testPlayer));
        
        when(playerService.list(any(Pageable.class), eq(Optional.empty()), 
                               eq(Optional.empty()), eq(Optional.empty())))
            .thenReturn(expectedPage);

        Page<PlayerDto> result = playerController.list(pageable, Optional.empty(), 
                                                       Optional.empty(), Optional.empty());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testPlayer.getName(), result.getContent().get(0).getName());
        verify(playerService, times(1)).list(any(), any(), any(), any());
    }

    @Test
    void testList_WithNameFilter() {
        Pageable pageable = PageRequest.of(0, 12);
        Optional<String> nameFilter = Optional.of("Messi");
        Page<PlayerDto> expectedPage = new PageImpl<>(Arrays.asList(testPlayer));
        
        when(playerService.list(any(Pageable.class), eq(nameFilter), 
                               eq(Optional.empty()), eq(Optional.empty())))
            .thenReturn(expectedPage);

        Page<PlayerDto> result = playerController.list(pageable, nameFilter, 
                                                       Optional.empty(), Optional.empty());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(playerService, times(1)).list(pageable, nameFilter, Optional.empty(), Optional.empty());
    }

    @Test
    void testList_WithCountryFilter() {
        Pageable pageable = PageRequest.of(0, 12);
        Optional<String> countryFilter = Optional.of("Argentina");
        Page<PlayerDto> expectedPage = new PageImpl<>(Arrays.asList(testPlayer));
        
        when(playerService.list(any(Pageable.class), eq(Optional.empty()), 
                               eq(countryFilter), eq(Optional.empty())))
            .thenReturn(expectedPage);

        Page<PlayerDto> result = playerController.list(pageable, Optional.empty(), 
                                                       countryFilter, Optional.empty());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(playerService, times(1)).list(pageable, Optional.empty(), countryFilter, Optional.empty());
    }

    @Test
    void testList_WithPositionFilter() {
        Pageable pageable = PageRequest.of(0, 12);
        Optional<String> positionFilter = Optional.of("Delantero");
        Page<PlayerDto> expectedPage = new PageImpl<>(Arrays.asList(testPlayer));
        
        when(playerService.list(any(Pageable.class), eq(Optional.empty()), 
                               eq(Optional.empty()), eq(positionFilter)))
            .thenReturn(expectedPage);

        Page<PlayerDto> result = playerController.list(pageable, Optional.empty(), 
                                                       Optional.empty(), positionFilter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(playerService, times(1)).list(pageable, Optional.empty(), Optional.empty(), positionFilter);
    }

    @Test
    void testGet_PlayerExists() {
        when(playerService.get(1L)).thenReturn(testPlayer);

        PlayerDto result = playerController.get(1L);

        assertNotNull(result);
        assertEquals(testPlayer.getId(), result.getId());
        assertEquals(testPlayer.getName(), result.getName());
        verify(playerService, times(1)).get(1L);
    }

    @Test
    void testCreate_Success() {
        when(playerService.create(any(PlayerDto.class))).thenReturn(testPlayer);

        ResponseEntity<PlayerDto> response = playerController.create(testPlayer);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testPlayer.getName(), response.getBody().getName());
        verify(playerService, times(1)).create(testPlayer);
    }

    @Test
    void testUpdate_Success() {
        PlayerDto updatedPlayer = new PlayerDto();
        updatedPlayer.setId(1L);
        updatedPlayer.setName("Lionel Messi Updated");
        
        when(playerService.update(eq(1L), any(PlayerDto.class))).thenReturn(updatedPlayer);

        ResponseEntity<PlayerDto> response = playerController.update(1L, updatedPlayer);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Lionel Messi Updated", response.getBody().getName());
        verify(playerService, times(1)).update(1L, updatedPlayer);
    }

    @Test
    void testDelete_Success() {
        doNothing().when(playerService).delete(1L);

        ResponseEntity<Void> response = playerController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(playerService, times(1)).delete(1L);
    }

    @Test
    void testList_EmptyResult() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<PlayerDto> emptyPage = Page.empty();
        
        when(playerService.list(any(Pageable.class), any(), any(), any())).thenReturn(emptyPage);

        Page<PlayerDto> result = playerController.list(pageable, Optional.empty(), 
                                                       Optional.empty(), Optional.empty());

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(playerService, times(1)).list(any(), any(), any(), any());
    }

    @Test
    void testList_WithAllFilters() {
        Pageable pageable = PageRequest.of(0, 12);
        Optional<String> nameFilter = Optional.of("Messi");
        Optional<String> countryFilter = Optional.of("Argentina");
        Optional<String> positionFilter = Optional.of("Delantero");
        Page<PlayerDto> expectedPage = new PageImpl<>(Arrays.asList(testPlayer));
        
        when(playerService.list(pageable, nameFilter, countryFilter, positionFilter))
            .thenReturn(expectedPage);

        Page<PlayerDto> result = playerController.list(pageable, nameFilter, 
                                                       countryFilter, positionFilter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(playerService, times(1)).list(pageable, nameFilter, countryFilter, positionFilter);
    }

    @Test
    void testList_WithMultiplePlayers() {
        PlayerDto player2 = new PlayerDto();
        player2.setId(2L);
        player2.setName("Cristiano Ronaldo");
        player2.setCountry("Portugal");
        player2.setPosition("Delantero");
        
        Pageable pageable = PageRequest.of(0, 12);
        Page<PlayerDto> expectedPage = new PageImpl<>(Arrays.asList(testPlayer, player2));
        
        when(playerService.list(any(Pageable.class), any(), any(), any()))
            .thenReturn(expectedPage);

        Page<PlayerDto> result = playerController.list(pageable, Optional.empty(), 
                                                       Optional.empty(), Optional.empty());

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Lionel Messi", result.getContent().get(0).getName());
        assertEquals("Cristiano Ronaldo", result.getContent().get(1).getName());
        verify(playerService, times(1)).list(any(), any(), any(), any());
    }
}
