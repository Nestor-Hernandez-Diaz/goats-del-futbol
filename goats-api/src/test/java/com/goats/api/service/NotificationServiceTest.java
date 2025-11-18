package com.goats.api.service;

import com.goats.api.dto.NotificationDto;
import com.goats.api.model.Notification;
import com.goats.api.model.NotificationType;
import com.goats.api.model.Player;
import com.goats.api.model.Subscription;
import com.goats.api.model.User;
import com.goats.api.repository.NotificationRepository;
import com.goats.api.repository.PlayerRepository;
import com.goats.api.repository.SubscriptionRepository;
import com.goats.api.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para NotificationService
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User testUser;
    private Player testPlayer;
    private Notification testNotification;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Setup test player
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("Lionel Messi");
        testPlayer.setCountry("Argentina");

        // Setup test notification
        testNotification = new Notification(testUser, testPlayer, NotificationType.COMMENT, "Nuevo comentario en Lionel Messi");
        testNotification.setId(1L);
        testNotification.setDetails("Este es un comentario de prueba");
        testNotification.setIsRead(false);
        testNotification.setCreatedAt(LocalDateTime.now());

        pageable = PageRequest.of(0, 20);
    }

    @Test
    void testGetByUserId_AllNotifications() {
        // Arrange
        Page<Notification> notificationPage = new PageImpl<>(Arrays.asList(testNotification));
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L, pageable)).thenReturn(notificationPage);

        // Act
        Page<NotificationDto> result = notificationService.getByUserId(1L, null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Nuevo comentario en Lionel Messi", result.getContent().get(0).getMessage());
        verify(notificationRepository, times(1)).findByUserIdOrderByCreatedAtDesc(1L, pageable);
    }

    @Test
    void testGetByUserId_UnreadOnly() {
        // Arrange
        Page<Notification> notificationPage = new PageImpl<>(Arrays.asList(testNotification));
        when(notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(1L, false, pageable))
                .thenReturn(notificationPage);

        // Act
        Page<NotificationDto> result = notificationService.getByUserId(1L, true, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertFalse(result.getContent().get(0).getIsRead());
        verify(notificationRepository, times(1))
                .findByUserIdAndIsReadOrderByCreatedAtDesc(1L, false, pageable);
    }

    @Test
    void testGetMyNotifications() {
        // Arrange
        Page<Notification> notificationPage = new PageImpl<>(Arrays.asList(testNotification));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L, pageable))
                .thenReturn(notificationPage);

        // Act
        Page<NotificationDto> result = notificationService.getMyNotifications("testuser", null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testuser", result.getContent().get(0).getUsername());
    }

    @Test
    void testGetMyNotifications_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            notificationService.getMyNotifications("nonexistent", null, pageable);
        });
    }

    @Test
    void testGetByType() {
        // Arrange
        Page<Notification> notificationPage = new PageImpl<>(Arrays.asList(testNotification));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.findByUserIdAndTypeOrderByCreatedAtDesc(1L, NotificationType.COMMENT, pageable))
                .thenReturn(notificationPage);

        // Act
        Page<NotificationDto> result = notificationService.getByType("testuser", NotificationType.COMMENT, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(NotificationType.COMMENT, result.getContent().get(0).getType());
    }

    @Test
    void testCountUnread() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.countByUserIdAndIsRead(1L, false)).thenReturn(5L);

        // Act
        long result = notificationService.countUnread("testuser");

        // Assert
        assertEquals(5L, result);
        verify(notificationRepository, times(1)).countByUserIdAndIsRead(1L, false);
    }

    @Test
    void testGetRecentUnread() {
        // Arrange
        Notification notif1 = new Notification(testUser, testPlayer, NotificationType.COMMENT, "Mensaje 1");
        notif1.setId(1L);
        notif1.setIsRead(false);
        
        Notification notif2 = new Notification(testUser, testPlayer, NotificationType.ACHIEVEMENT, "Mensaje 2");
        notif2.setId(2L);
        notif2.setIsRead(false);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.findRecentUnreadNotifications(eq(1L), any(Pageable.class)))
                .thenReturn(Arrays.asList(notif1, notif2));

        // Act
        List<NotificationDto> result = notificationService.getRecentUnread("testuser", 10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> !dto.getIsRead()));
    }

    @Test
    void testMarkAsRead_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            n.setIsRead(true);
            n.setReadAt(LocalDateTime.now());
            return n;
        });

        // Act
        NotificationDto result = notificationService.markAsRead(1L, "testuser");

        // Assert
        assertNotNull(result);
        assertTrue(result.getIsRead());
        assertNotNull(result.getReadAt());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testMarkAsRead_NotificationNotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            notificationService.markAsRead(999L, "testuser");
        });
    }

    @Test
    void testMarkAsRead_WrongUser() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otheruser");
        
        when(userRepository.findByUsername("otheruser")).thenReturn(Optional.of(otherUser));
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.markAsRead(1L, "otheruser");
        });
        
        assertTrue(exception.getMessage().contains("cannot mark this notification"));
    }

    @Test
    void testMarkMultipleAsRead() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.markAsReadByIds(ids, 1L)).thenReturn(3);

        // Act
        int result = notificationService.markMultipleAsRead(ids, "testuser");

        // Assert
        assertEquals(3, result);
        verify(notificationRepository, times(1)).markAsReadByIds(ids, 1L);
    }

    @Test
    void testMarkAllAsRead() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.markAllAsRead(1L)).thenReturn(10);

        // Act
        int result = notificationService.markAllAsRead("testuser");

        // Assert
        assertEquals(10, result);
        verify(notificationRepository, times(1)).markAllAsRead(1L);
    }

    @Test
    void testNotifyNewComment() {
        // Arrange
        User subscriber1 = new User();
        subscriber1.setId(2L);
        subscriber1.setUsername("subscriber1");
        
        User subscriber2 = new User();
        subscriber2.setId(3L);
        subscriber2.setUsername("subscriber2");
        
        Subscription sub1 = new Subscription(subscriber1, testPlayer);
        Subscription sub2 = new Subscription(subscriber2, testPlayer);
        
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(subscriptionRepository.findActiveSubscribersWithNotifications(1L))
                .thenReturn(Arrays.asList(sub1, sub2));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        notificationService.notifyNewComment(1L, "Este es un comentario muy largo que debería ser truncado después de cien caracteres para que el detalle no sea demasiado largo en la notificación");

        // Assert
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    void testNotifyNewComment_ShortContent() {
        // Arrange
        User subscriber = new User();
        subscriber.setId(2L);
        Subscription sub = new Subscription(subscriber, testPlayer);
        
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(subscriptionRepository.findActiveSubscribersWithNotifications(1L))
                .thenReturn(Arrays.asList(sub));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        notificationService.notifyNewComment(1L, "Comentario corto");

        // Assert
        verify(notificationRepository, times(1)).save(argThat(notification -> 
            notification.getDetails().equals("Comentario corto")
        ));
    }

    @Test
    void testNotifyNewAchievement() {
        // Arrange
        User subscriber1 = new User();
        subscriber1.setId(2L);
        subscriber1.setUsername("subscriber1");
        
        Subscription sub1 = new Subscription(subscriber1, testPlayer);
        
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(subscriptionRepository.findActiveSubscribersWithNotifications(1L))
                .thenReturn(Arrays.asList(sub1));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        notificationService.notifyNewAchievement(1L, "Balón de Oro 2023");

        // Assert
        verify(notificationRepository, times(1)).save(argThat(notification -> 
            notification.getType() == NotificationType.ACHIEVEMENT &&
            notification.getDetails().equals("Balón de Oro 2023")
        ));
    }

    @Test
    void testNotifyNewAchievement_NoSubscribers() {
        // Arrange
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(subscriptionRepository.findActiveSubscribersWithNotifications(1L))
                .thenReturn(Arrays.asList());

        // Act
        notificationService.notifyNewAchievement(1L, "Balón de Oro 2023");

        // Assert
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void testDelete_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));
        doNothing().when(notificationRepository).delete(any(Notification.class));

        // Act
        notificationService.delete(1L, "testuser");

        // Assert
        verify(notificationRepository, times(1)).delete(testNotification);
    }

    @Test
    void testDelete_WrongUser() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otheruser");
        
        when(userRepository.findByUsername("otheruser")).thenReturn(Optional.of(otherUser));
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.delete(1L, "otheruser");
        });
        
        assertTrue(exception.getMessage().contains("cannot delete this notification"));
    }

    @Test
    void testCleanupOldNotifications() {
        // Arrange
        when(notificationRepository.deleteOldReadNotifications()).thenReturn(42);

        // Act
        int result = notificationService.cleanupOldNotifications();

        // Assert
        assertEquals(42, result);
        verify(notificationRepository, times(1)).deleteOldReadNotifications();
    }
}
