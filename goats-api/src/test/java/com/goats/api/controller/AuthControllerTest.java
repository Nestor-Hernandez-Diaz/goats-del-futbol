package com.goats.api.controller;

import com.goats.api.dto.AuthResponse;
import com.goats.api.dto.LoginRequest;
import com.goats.api.dto.RegisterRequest;
import com.goats.api.dto.UserResponse;
import com.goats.api.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        authResponse = new AuthResponse("mock-jwt-token", 1L, "testuser", 
                                       "test@test.com", new HashSet<>());

        userResponse = new UserResponse(1L, "testuser", "test@test.com", 
                                       true, null, new HashSet<>());
    }

    @Test
    void testRegister_Success() {
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        ResponseEntity<?> response = authController.register(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthResponse);
        AuthResponse body = (AuthResponse) response.getBody();
        assertEquals("testuser", body.getUsername());
        assertEquals("mock-jwt-token", body.getToken());
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        when(authService.register(any(RegisterRequest.class)))
            .thenThrow(new RuntimeException("Username already exists"));

        ResponseEntity<?> response = authController.register(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already exists", response.getBody());
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    void testLogin_Success() {
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthResponse);
        AuthResponse body = (AuthResponse) response.getBody();
        assertEquals("testuser", body.getUsername());
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    void testLogin_InvalidCredentials() {
        when(authService.login(any(LoginRequest.class)))
            .thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    void testGetCurrentUser_Success() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("testuser");
        
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(authService.getCurrentUser("testuser")).thenReturn(userResponse);

        ResponseEntity<?> response = authController.getCurrentUser();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserResponse);
        UserResponse body = (UserResponse) response.getBody();
        assertEquals("testuser", body.getUsername());
        verify(authService, times(1)).getCurrentUser("testuser");
        
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUser_NotAuthenticated() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        ResponseEntity<?> response = authController.getCurrentUser();

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not authenticated", response.getBody());
        verify(authService, never()).getCurrentUser(any());
        
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUser_UserNotFound() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("nonexistent");
        
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(authService.getCurrentUser("nonexistent"))
            .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<?> response = authController.getCurrentUser();

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(authService, times(1)).getCurrentUser("nonexistent");
        
        SecurityContextHolder.clearContext();
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        when(authService.register(any(RegisterRequest.class)))
            .thenThrow(new RuntimeException("Email already exists"));

        ResponseEntity<?> response = authController.register(registerRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
    }

    @Test
    void testLogin_DisabledUser() {
        when(authService.login(any(LoginRequest.class)))
            .thenThrow(new RuntimeException("User is disabled"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void testGetCurrentUser_AuthenticationNotAuthenticated() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        ResponseEntity<?> response = authController.getCurrentUser();

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not authenticated", response.getBody());
        
        SecurityContextHolder.clearContext();
    }
}
