package com.goats.api.service;

import com.goats.api.dto.AuthResponse;
import com.goats.api.dto.LoginRequest;
import com.goats.api.dto.RegisterRequest;
import com.goats.api.dto.UserResponse;
import com.goats.api.model.Role;
import com.goats.api.model.User;
import com.goats.api.repository.RoleRepository;
import com.goats.api.repository.UserRepository;
import com.goats.api.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para AuthService
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private Role userRole;
    private Role adminRole;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        // Setup roles
        userRole = new Role();
        userRole.setId(1L);
        userRole.setName("ROLE_USER");

        adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setName("ROLE_ADMIN");

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@test.com");
        testUser.setPasswordHash("$2a$10$hashedpassword");
        testUser.setEnabled(true);
        testUser.setCreatedAt(LocalDateTime.now());
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        testUser.setRoles(roles);

        // Setup register request
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("newuser@test.com");
        registerRequest.setPassword("password123");

        // Setup login request
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void testRegister_Success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(tokenProvider.generateTokenFromUser(any(User.class))).thenReturn("jwt-token");

        // When
        AuthResponse result = authService.register(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertTrue(result.getRoles().contains("ROLE_USER"));
        
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).existsByEmail("newuser@test.com");
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenProvider, times(1)).generateTokenFromUser(any(User.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_EmailAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail("newuser@test.com")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        
        verify(userRepository, times(1)).existsByEmail("newuser@test.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_RoleNotFound_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLogin_Success() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn("jwt-token");

        // When
        AuthResponse result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertTrue(result.getRoles().contains("ROLE_USER"));
        
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(tokenProvider, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void testLogin_UserNotFound_ThrowsException() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(tokenProvider, never()).generateToken(any());
    }

    @Test
    void testGetCurrentUser_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserResponse result = authService.getCurrentUser("testuser");

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertTrue(result.getEnabled());
        assertTrue(result.getRoles().contains("ROLE_USER"));
        
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testGetCurrentUser_NotFound_ThrowsException() {
        // Given
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.getCurrentUser("nonexistent"));
        
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void testGetCurrentUser_WithMultipleRoles() {
        // Given
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        roles.add(adminRole);
        testUser.setRoles(roles);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // When
        UserResponse result = authService.getCurrentUser("testuser");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getRoles().size());
        assertTrue(result.getRoles().contains("ROLE_USER"));
        assertTrue(result.getRoles().contains("ROLE_ADMIN"));
        
        verify(userRepository, times(1)).findByUsername("testuser");
    }
}
