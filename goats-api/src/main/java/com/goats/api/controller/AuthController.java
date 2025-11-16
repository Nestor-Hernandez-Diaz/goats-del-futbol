package com.goats.api.controller;

import com.goats.api.dto.AuthResponse;
import com.goats.api.dto.LoginRequest;
import com.goats.api.dto.RegisterRequest;
import com.goats.api.dto.UserResponse;
import com.goats.api.model.User;
import com.goats.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación
 * Gestiona registro, login y obtención de información del usuario actual
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Registra un nuevo usuario
     * @param registerRequest datos de registro
     * @return respuesta con token JWT
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario",
               description = "Crea una nueva cuenta de usuario y devuelve un token JWT")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Autentica un usuario y devuelve un token JWT
     * @param loginRequest credenciales de login
     * @return respuesta con token JWT
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión",
               description = "Autentica al usuario y devuelve un token JWT")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }

    /**
     * Obtiene información del usuario autenticado
     * @return información del usuario actual
     */
    @GetMapping("/me")
    @Operation(summary = "Obtener usuario actual",
               description = "Devuelve información del usuario autenticado")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User is not authenticated");
            }

            String username = authentication.getName();
            UserResponse response = authService.getCurrentUser(username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
