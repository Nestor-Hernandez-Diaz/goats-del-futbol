package com.goats.api.service;

import com.goats.api.dto.PlayerDto;
import com.goats.api.model.Player;
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
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para PlayerService
 */
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player testPlayer;
    private PlayerDto testPlayerDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Setup test player
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("Lionel Messi");
        testPlayer.setNickname("La Pulga");
        testPlayer.setCountry("Argentina");
        testPlayer.setPosition("Forward");
        testPlayer.setBiography("Best player ever");

        // Setup test DTO
        testPlayerDto = new PlayerDto();
        testPlayerDto.setName("Lionel Messi");
        testPlayerDto.setNickname("La Pulga");
        testPlayerDto.setCountry("Argentina");
        testPlayerDto.setPosition("Forward");
        testPlayerDto.setBiography("Best player ever");

        // Setup pageable
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void testList_WithoutFilters_ReturnsAllPlayers() {
        // Given
        List<Player> players = Arrays.asList(testPlayer);
        Page<Player> playerPage = new PageImpl<>(players);
        when(playerRepository.findAll(pageable)).thenReturn(playerPage);

        // When
        Page<PlayerDto> result = playerService.list(pageable, Optional.empty(), Optional.empty(), Optional.empty());

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(playerRepository, times(1)).findAll(pageable);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testList_WithNameFilter_ReturnsFilteredPlayers() {
        // Given
        List<Player> players = Arrays.asList(testPlayer);
        Page<Player> playerPage = new PageImpl<>(players);
        when(playerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(playerPage);

        // When
        Page<PlayerDto> result = playerService.list(pageable, Optional.of("Messi"), Optional.empty(), Optional.empty());

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(playerRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testList_WithMultipleFilters_ReturnsFilteredPlayers() {
        // Given
        List<Player> players = Arrays.asList(testPlayer);
        Page<Player> playerPage = new PageImpl<>(players);
        when(playerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(playerPage);

        // When
        Page<PlayerDto> result = playerService.list(
            pageable, 
            Optional.of("Messi"), 
            Optional.of("Argentina"), 
            Optional.of("Forward")
        );

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(playerRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testGet_Success() {
        // Given
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));

        // When
        PlayerDto result = playerService.get(1L);

        // Then
        assertNotNull(result);
        assertEquals(testPlayer.getName(), result.getName());
        assertEquals(testPlayer.getCountry(), result.getCountry());
        verify(playerRepository, times(1)).findById(1L);
    }

    @Test
    void testGet_NotFound_ThrowsException() {
        // Given
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> playerService.get(999L));
        verify(playerRepository, times(1)).findById(999L);
    }

    @Test
    void testGet_NullId_ThrowsException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> playerService.get(null));
        verify(playerRepository, never()).findById(any());
    }

    @Test
    void testCreate_Success() {
        // Given
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer);

        // When
        PlayerDto result = playerService.create(testPlayerDto);

        // Then
        assertNotNull(result);
        assertEquals(testPlayerDto.getName(), result.getName());
        assertEquals(testPlayerDto.getCountry(), result.getCountry());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testUpdate_Success() {
        // Given
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer);

        // When
        PlayerDto result = playerService.update(1L, testPlayerDto);

        // Then
        assertNotNull(result);
        assertEquals(testPlayerDto.getName(), result.getName());
        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testUpdate_NotFound_ThrowsException() {
        // Given
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> playerService.update(999L, testPlayerDto));
        verify(playerRepository, times(1)).findById(999L);
        verify(playerRepository, never()).save(any());
    }

    @Test
    void testDelete_Success() {
        // Given
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        doNothing().when(playerRepository).delete(any(Player.class));

        // When
        playerService.delete(1L);

        // Then
        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).delete(any(Player.class));
    }

    @Test
    void testDelete_NotFound_ThrowsException() {
        // Given
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> playerService.delete(999L));
        verify(playerRepository, times(1)).findById(999L);
        verify(playerRepository, never()).delete(any(Player.class));
    }
}
