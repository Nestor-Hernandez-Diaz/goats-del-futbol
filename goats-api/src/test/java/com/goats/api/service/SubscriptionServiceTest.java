package com.goats.api.service;

import com.goats.api.dto.SubscriptionDto;
import com.goats.api.model.Player;
import com.goats.api.model.Subscription;
import com.goats.api.model.User;
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
 * Tests unitarios para SubscriptionService
 */
@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private User testUser;
    private Player testPlayer;
    private Subscription testSubscription;
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

        // Setup test subscription
        testSubscription = new Subscription(testUser, testPlayer);
        testSubscription.setId(1L);
        testSubscription.setActive(true);
        testSubscription.setNotificationsEnabled(true);
        testSubscription.setSubscribedAt(LocalDateTime.now());

        pageable = PageRequest.of(0, 20);
    }

    @Test
    void testGetByUserId_WithoutActiveFilter() {
        // Arrange
        Page<Subscription> subscriptionPage = new PageImpl<>(Arrays.asList(testSubscription));
        when(subscriptionRepository.findByUserId(1L, pageable)).thenReturn(subscriptionPage);

        // Act
        Page<SubscriptionDto> result = subscriptionService.getByUserId(1L, null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testuser", result.getContent().get(0).getUsername());
        assertEquals("Lionel Messi", result.getContent().get(0).getPlayerName());
        verify(subscriptionRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void testGetByUserId_WithActiveFilter() {
        // Arrange
        Page<Subscription> subscriptionPage = new PageImpl<>(Arrays.asList(testSubscription));
        when(subscriptionRepository.findByUserIdAndActive(1L, true, pageable)).thenReturn(subscriptionPage);

        // Act
        Page<SubscriptionDto> result = subscriptionService.getByUserId(1L, true, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getActive());
        verify(subscriptionRepository, times(1)).findByUserIdAndActive(1L, true, pageable);
    }

    @Test
    void testGetByPlayerId_WithActiveFilter() {
        // Arrange
        Page<Subscription> subscriptionPage = new PageImpl<>(Arrays.asList(testSubscription));
        when(subscriptionRepository.findByPlayerIdAndActive(1L, true, pageable)).thenReturn(subscriptionPage);

        // Act
        Page<SubscriptionDto> result = subscriptionService.getByPlayerId(1L, true, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getPlayerId());
        verify(subscriptionRepository, times(1)).findByPlayerIdAndActive(1L, true, pageable);
    }

    @Test
    void testIsSubscribed_ReturnsTrue() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(subscriptionRepository.existsActiveSubscription(1L, 1L)).thenReturn(true);

        // Act
        boolean result = subscriptionService.isSubscribed("testuser", 1L);

        // Assert
        assertTrue(result);
        verify(subscriptionRepository, times(1)).existsActiveSubscription(1L, 1L);
    }

    @Test
    void testIsSubscribed_ReturnsFalse() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(subscriptionRepository.existsActiveSubscription(1L, 1L)).thenReturn(false);

        // Act
        boolean result = subscriptionService.isSubscribed("testuser", 1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsSubscribed_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subscriptionService.isSubscribed("nonexistent", 1L);
        });
    }

    @Test
    void testSubscribe_NewSubscription() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(subscriptionRepository.findByUserIdAndPlayerId(1L, 1L)).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);

        // Act
        SubscriptionDto result = subscriptionService.subscribe(1L, "testuser");

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getPlayerId());
        assertEquals("Lionel Messi", result.getPlayerName());
        assertTrue(result.getActive());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testSubscribe_ReactivateInactive() {
        // Arrange
        testSubscription.setActive(false);
        testSubscription.setUnsubscribedAt(LocalDateTime.now());
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(subscriptionRepository.findByUserIdAndPlayerId(1L, 1L)).thenReturn(Optional.of(testSubscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> {
            Subscription sub = invocation.getArgument(0);
            sub.setActive(true);
            sub.setUnsubscribedAt(null);
            return sub;
        });

        // Act
        SubscriptionDto result = subscriptionService.subscribe(1L, "testuser");

        // Assert
        assertNotNull(result);
        assertTrue(result.getActive());
        assertNull(result.getUnsubscribedAt());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testSubscribe_AlreadyActive() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(subscriptionRepository.findByUserIdAndPlayerId(1L, 1L)).thenReturn(Optional.of(testSubscription));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            subscriptionService.subscribe(1L, "testuser");
        });
        
        assertTrue(exception.getMessage().contains("already subscribed"));
    }

    @Test
    void testSubscribe_PlayerNotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(playerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            subscriptionService.subscribe(999L, "testuser");
        });
        
        assertTrue(exception.getMessage().contains("Player not found"));
    }

    @Test
    void testUnsubscribe_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(subscriptionRepository.findByUserIdAndPlayerId(1L, 1L)).thenReturn(Optional.of(testSubscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);

        // Act
        subscriptionService.unsubscribe(1L, "testuser");

        // Assert
        verify(subscriptionRepository, times(1)).save(argThat(sub -> 
            !sub.getActive() && sub.getUnsubscribedAt() != null
        ));
    }

    @Test
    void testUnsubscribe_SubscriptionNotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(subscriptionRepository.findByUserIdAndPlayerId(1L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subscriptionService.unsubscribe(1L, "testuser");
        });
    }

    @Test
    void testToggleNotifications_EnableToDisable() {
        // Arrange
        testSubscription.setNotificationsEnabled(true);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(subscriptionRepository.findByUserIdAndPlayerId(1L, 1L)).thenReturn(Optional.of(testSubscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> {
            Subscription sub = invocation.getArgument(0);
            sub.setNotificationsEnabled(false);
            return sub;
        });

        // Act
        SubscriptionDto result = subscriptionService.toggleNotifications(1L, "testuser");

        // Assert
        assertNotNull(result);
        assertFalse(result.getNotificationsEnabled());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testToggleNotifications_DisableToEnable() {
        // Arrange
        testSubscription.setNotificationsEnabled(false);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(subscriptionRepository.findByUserIdAndPlayerId(1L, 1L)).thenReturn(Optional.of(testSubscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> {
            Subscription sub = invocation.getArgument(0);
            sub.setNotificationsEnabled(true);
            return sub;
        });

        // Act
        SubscriptionDto result = subscriptionService.toggleNotifications(1L, "testuser");

        // Assert
        assertNotNull(result);
        assertTrue(result.getNotificationsEnabled());
    }

    @Test
    void testToggleNotifications_InactiveSubscription() {
        // Arrange
        testSubscription.setActive(false);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(subscriptionRepository.findByUserIdAndPlayerId(1L, 1L)).thenReturn(Optional.of(testSubscription));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            subscriptionService.toggleNotifications(1L, "testuser");
        });
        
        assertTrue(exception.getMessage().contains("inactive subscription"));
    }

    @Test
    void testCountActiveSubscribers() {
        // Arrange
        when(subscriptionRepository.countActiveSubscribers(1L)).thenReturn(42L);

        // Act
        long result = subscriptionService.countActiveSubscribers(1L);

        // Assert
        assertEquals(42L, result);
        verify(subscriptionRepository, times(1)).countActiveSubscribers(1L);
    }

    @Test
    void testCountUserActiveSubscriptions() {
        // Arrange
        when(subscriptionRepository.countByUserIdAndActive(1L, true)).thenReturn(5L);

        // Act
        long result = subscriptionService.countUserActiveSubscriptions(1L);

        // Assert
        assertEquals(5L, result);
        verify(subscriptionRepository, times(1)).countByUserIdAndActive(1L, true);
    }

    @Test
    void testGetSubscribersWithNotifications() {
        // Arrange
        Subscription sub1 = new Subscription(testUser, testPlayer);
        sub1.setId(1L);
        sub1.setActive(true);
        sub1.setNotificationsEnabled(true);
        
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        Subscription sub2 = new Subscription(user2, testPlayer);
        sub2.setId(2L);
        sub2.setActive(true);
        sub2.setNotificationsEnabled(true);
        
        when(subscriptionRepository.findActiveSubscribersWithNotifications(1L))
                .thenReturn(Arrays.asList(sub1, sub2));

        // Act
        List<SubscriptionDto> result = subscriptionService.getSubscribersWithNotifications(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getNotificationsEnabled()));
        assertTrue(result.stream().allMatch(dto -> dto.getActive()));
        verify(subscriptionRepository, times(1)).findActiveSubscribersWithNotifications(1L);
    }

    @Test
    void testGetSubscribersWithNotifications_EmptyList() {
        // Arrange
        when(subscriptionRepository.findActiveSubscribersWithNotifications(1L))
                .thenReturn(Arrays.asList());

        // Act
        List<SubscriptionDto> result = subscriptionService.getSubscribersWithNotifications(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
