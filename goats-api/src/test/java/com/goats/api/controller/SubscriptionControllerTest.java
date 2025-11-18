package com.goats.api.controller;

import com.goats.api.dto.SubscriptionDto;
import com.goats.api.service.SubscriptionService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para SubscriptionController
 */
@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SubscriptionController subscriptionController;

    private SubscriptionDto testSubscriptionDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testSubscriptionDto = new SubscriptionDto(
                1L,
                1L,
                "testuser",
                1L,
                "Lionel Messi",
                true,
                true,
                LocalDateTime.now(),
                null
        );

        pageable = PageRequest.of(0, 20);
    }

    private void mockAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetByUserId_WithoutActiveFilter() {
        // Arrange
        Page<SubscriptionDto> subscriptionPage = new PageImpl<>(Arrays.asList(testSubscriptionDto));
        when(subscriptionService.getByUserId(1L, null, pageable)).thenReturn(subscriptionPage);

        // Act
        Page<SubscriptionDto> result = subscriptionController.getByUserId(1L, null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testuser", result.getContent().get(0).getUsername());
        verify(subscriptionService, times(1)).getByUserId(1L, null, pageable);
    }

    @Test
    void testGetByUserId_WithActiveFilter() {
        // Arrange
        Page<SubscriptionDto> subscriptionPage = new PageImpl<>(Arrays.asList(testSubscriptionDto));
        when(subscriptionService.getByUserId(1L, true, pageable)).thenReturn(subscriptionPage);

        // Act
        Page<SubscriptionDto> result = subscriptionController.getByUserId(1L, true, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getActive());
        verify(subscriptionService, times(1)).getByUserId(1L, true, pageable);
    }

    @Test
    void testGetByPlayerId_WithActiveFilter() {
        // Arrange
        Page<SubscriptionDto> subscriptionPage = new PageImpl<>(Arrays.asList(testSubscriptionDto));
        when(subscriptionService.getByPlayerId(1L, true, pageable)).thenReturn(subscriptionPage);

        // Act
        Page<SubscriptionDto> result = subscriptionController.getByPlayerId(1L, true, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getPlayerId());
        verify(subscriptionService, times(1)).getByPlayerId(1L, true, pageable);
    }

    @Test
    void testCheckSubscription_IsSubscribed() {
        // Arrange
        mockAuthentication();
        when(subscriptionService.isSubscribed("testuser", 1L)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = subscriptionController.checkSubscription(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(subscriptionService, times(1)).isSubscribed("testuser", 1L);
    }

    @Test
    void testCheckSubscription_NotSubscribed() {
        // Arrange
        mockAuthentication();
        when(subscriptionService.isSubscribed("testuser", 1L)).thenReturn(false);

        // Act
        ResponseEntity<Boolean> response = subscriptionController.checkSubscription(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void testSubscribe_Success() {
        // Arrange
        mockAuthentication();
        when(subscriptionService.subscribe(1L, "testuser")).thenReturn(testSubscriptionDto);

        // Act
        ResponseEntity<SubscriptionDto> response = subscriptionController.subscribe(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Lionel Messi", response.getBody().getPlayerName());
        assertTrue(response.getBody().getActive());
        verify(subscriptionService, times(1)).subscribe(1L, "testuser");
    }

    @Test
    void testUnsubscribe_Success() {
        // Arrange
        mockAuthentication();
        doNothing().when(subscriptionService).unsubscribe(1L, "testuser");

        // Act
        ResponseEntity<Void> response = subscriptionController.unsubscribe(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(subscriptionService, times(1)).unsubscribe(1L, "testuser");
    }

    @Test
    void testToggleNotifications_Success() {
        // Arrange
        mockAuthentication();
        testSubscriptionDto.setNotificationsEnabled(false);
        when(subscriptionService.toggleNotifications(1L, "testuser")).thenReturn(testSubscriptionDto);

        // Act
        ResponseEntity<SubscriptionDto> response = subscriptionController.toggleNotifications(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getNotificationsEnabled());
        verify(subscriptionService, times(1)).toggleNotifications(1L, "testuser");
    }

    @Test
    void testCountActiveSubscribers() {
        // Arrange
        when(subscriptionService.countActiveSubscribers(1L)).thenReturn(42L);

        // Act
        ResponseEntity<Long> response = subscriptionController.countActiveSubscribers(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(42L, response.getBody());
        verify(subscriptionService, times(1)).countActiveSubscribers(1L);
    }

    @Test
    void testCountActiveSubscribers_ZeroSubscribers() {
        // Arrange
        when(subscriptionService.countActiveSubscribers(1L)).thenReturn(0L);

        // Act
        ResponseEntity<Long> response = subscriptionController.countActiveSubscribers(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0L, response.getBody());
    }

    @Test
    void testGetSubscribersWithNotifications() {
        // Arrange
        SubscriptionDto sub1 = new SubscriptionDto(1L, 1L, "user1", 1L, "Lionel Messi", true, true, LocalDateTime.now(), null);
        SubscriptionDto sub2 = new SubscriptionDto(2L, 2L, "user2", 1L, "Lionel Messi", true, true, LocalDateTime.now(), null);
        List<SubscriptionDto> subscribers = Arrays.asList(sub1, sub2);
        
        when(subscriptionService.getSubscribersWithNotifications(1L)).thenReturn(subscribers);

        // Act
        ResponseEntity<List<SubscriptionDto>> response = subscriptionController.getSubscribersWithNotifications(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().stream().allMatch(dto -> dto.getNotificationsEnabled()));
        verify(subscriptionService, times(1)).getSubscribersWithNotifications(1L);
    }

    @Test
    void testGetSubscribersWithNotifications_EmptyList() {
        // Arrange
        when(subscriptionService.getSubscribersWithNotifications(1L)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<SubscriptionDto>> response = subscriptionController.getSubscribersWithNotifications(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
}
