package com.goats.api.controller;

import com.goats.api.dto.NotificationDto;
import com.goats.api.model.NotificationType;
import com.goats.api.service.NotificationService;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para NotificationController
 */
@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private NotificationController notificationController;

    private NotificationDto testNotificationDto;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testNotificationDto = new NotificationDto(
                1L,
                1L,
                "testuser",
                1L,
                "Lionel Messi",
                NotificationType.COMMENT,
                "Nuevo comentario en Lionel Messi",
                "Detalle del comentario",
                false,
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
    void testGetMyNotifications_All() {
        // Arrange
        mockAuthentication();
        Page<NotificationDto> notificationPage = new PageImpl<>(Arrays.asList(testNotificationDto));
        when(notificationService.getMyNotifications("testuser", null, pageable)).thenReturn(notificationPage);

        // Act
        Page<NotificationDto> result = notificationController.getMyNotifications(null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testuser", result.getContent().get(0).getUsername());
        verify(notificationService, times(1)).getMyNotifications("testuser", null, pageable);
    }

    @Test
    void testGetMyNotifications_UnreadOnly() {
        // Arrange
        mockAuthentication();
        Page<NotificationDto> notificationPage = new PageImpl<>(Arrays.asList(testNotificationDto));
        when(notificationService.getMyNotifications("testuser", true, pageable)).thenReturn(notificationPage);

        // Act
        Page<NotificationDto> result = notificationController.getMyNotifications(true, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertFalse(result.getContent().get(0).getIsRead());
        verify(notificationService, times(1)).getMyNotifications("testuser", true, pageable);
    }

    @Test
    void testGetByType() {
        // Arrange
        mockAuthentication();
        Page<NotificationDto> notificationPage = new PageImpl<>(Arrays.asList(testNotificationDto));
        when(notificationService.getByType("testuser", NotificationType.COMMENT, pageable))
                .thenReturn(notificationPage);

        // Act
        Page<NotificationDto> result = notificationController.getByType(NotificationType.COMMENT, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(NotificationType.COMMENT, result.getContent().get(0).getType());
        verify(notificationService, times(1)).getByType("testuser", NotificationType.COMMENT, pageable);
    }

    @Test
    void testCountUnread() {
        // Arrange
        mockAuthentication();
        when(notificationService.countUnread("testuser")).thenReturn(5L);

        // Act
        ResponseEntity<Long> response = notificationController.countUnread();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
        verify(notificationService, times(1)).countUnread("testuser");
    }

    @Test
    void testCountUnread_Zero() {
        // Arrange
        mockAuthentication();
        when(notificationService.countUnread("testuser")).thenReturn(0L);

        // Act
        ResponseEntity<Long> response = notificationController.countUnread();

        // Assert
        assertNotNull(response);
        assertEquals(0L, response.getBody());
    }

    @Test
    void testGetRecentUnread() {
        // Arrange
        mockAuthentication();
        NotificationDto notif1 = new NotificationDto(1L, 1L, "testuser", 1L, "Messi", NotificationType.COMMENT, "Msg1", null, false, LocalDateTime.now(), null);
        NotificationDto notif2 = new NotificationDto(2L, 1L, "testuser", 1L, "Messi", NotificationType.ACHIEVEMENT, "Msg2", null, false, LocalDateTime.now(), null);
        
        when(notificationService.getRecentUnread("testuser", 10)).thenReturn(Arrays.asList(notif1, notif2));

        // Act
        ResponseEntity<List<NotificationDto>> response = notificationController.getRecentUnread(10);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(notificationService, times(1)).getRecentUnread("testuser", 10);
    }

    @Test
    void testGetRecentUnread_DefaultLimit() {
        // Arrange
        mockAuthentication();
        when(notificationService.getRecentUnread("testuser", 10)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<List<NotificationDto>> response = notificationController.getRecentUnread(10);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testMarkAsRead() {
        // Arrange
        mockAuthentication();
        testNotificationDto.setIsRead(true);
        testNotificationDto.setReadAt(LocalDateTime.now());
        
        when(notificationService.markAsRead(1L, "testuser")).thenReturn(testNotificationDto);

        // Act
        ResponseEntity<NotificationDto> response = notificationController.markAsRead(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getIsRead());
        assertNotNull(response.getBody().getReadAt());
        verify(notificationService, times(1)).markAsRead(1L, "testuser");
    }

    @Test
    void testMarkMultipleAsRead() {
        // Arrange
        mockAuthentication();
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(notificationService.markMultipleAsRead(ids, "testuser")).thenReturn(3);

        // Act
        ResponseEntity<Map<String, Object>> response = notificationController.markMultipleAsRead(
                Map.of("notificationIds", ids)
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().get("updated"));
        assertTrue(response.getBody().get("message").toString().contains("3 notificaciones"));
        verify(notificationService, times(1)).markMultipleAsRead(ids, "testuser");
    }

    @Test
    void testMarkMultipleAsRead_EmptyList() {
        // Arrange
        mockAuthentication();
        List<Long> ids = Arrays.asList();
        when(notificationService.markMultipleAsRead(ids, "testuser")).thenReturn(0);

        // Act
        ResponseEntity<Map<String, Object>> response = notificationController.markMultipleAsRead(
                Map.of("notificationIds", ids)
        );

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getBody().get("updated"));
    }

    @Test
    void testMarkAllAsRead() {
        // Arrange
        mockAuthentication();
        when(notificationService.markAllAsRead("testuser")).thenReturn(10);

        // Act
        ResponseEntity<Map<String, Object>> response = notificationController.markAllAsRead();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().get("updated"));
        assertTrue(response.getBody().get("message").toString().contains("10 notificaciones"));
        verify(notificationService, times(1)).markAllAsRead("testuser");
    }

    @Test
    void testMarkAllAsRead_NoNotifications() {
        // Arrange
        mockAuthentication();
        when(notificationService.markAllAsRead("testuser")).thenReturn(0);

        // Act
        ResponseEntity<Map<String, Object>> response = notificationController.markAllAsRead();

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getBody().get("updated"));
    }

    @Test
    void testDelete() {
        // Arrange
        mockAuthentication();
        doNothing().when(notificationService).delete(1L, "testuser");

        // Act
        ResponseEntity<Void> response = notificationController.delete(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService, times(1)).delete(1L, "testuser");
    }

    @Test
    void testCleanupOldNotifications() {
        // Arrange
        when(notificationService.cleanupOldNotifications()).thenReturn(42);

        // Act
        ResponseEntity<Map<String, Object>> response = notificationController.cleanupOldNotifications();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(42, response.getBody().get("deleted"));
        assertTrue(response.getBody().get("message").toString().contains("42 notificaciones"));
        verify(notificationService, times(1)).cleanupOldNotifications();
    }

    @Test
    void testCleanupOldNotifications_NothingToClean() {
        // Arrange
        when(notificationService.cleanupOldNotifications()).thenReturn(0);

        // Act
        ResponseEntity<Map<String, Object>> response = notificationController.cleanupOldNotifications();

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getBody().get("deleted"));
    }
}
