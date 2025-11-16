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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio de autenticación y gestión de usuarios
 * Maneja registro, login y operaciones relacionadas con usuarios
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario en el sistema
     * @param registerRequest datos de registro
     * @return respuesta con token JWT
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        // Validar que el username no exista
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // Validar que el email no exista
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(true);

        // Asignar rol USER por defecto
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role ROLE_USER not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        // Guardar usuario
        user = userRepository.save(user);

        // Generar token JWT
        String token = tokenProvider.generateTokenFromUser(user);

        // Extraer nombres de roles
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), roleNames);
    }

    /**
     * Autentica un usuario y genera un token JWT
     * @param loginRequest credenciales de login
     * @return respuesta con token JWT
     */
    public AuthResponse login(LoginRequest loginRequest) {
        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Obtener usuario
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generar token JWT
        String token = tokenProvider.generateToken(authentication);

        // Extraer nombres de roles
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), roleNames);
    }

    /**
     * Obtiene información del usuario autenticado
     * @param username nombre de usuario
     * @return información del usuario
     */
    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getEnabled(),
                user.getCreatedAt(),
                roleNames
        );
    }
}
