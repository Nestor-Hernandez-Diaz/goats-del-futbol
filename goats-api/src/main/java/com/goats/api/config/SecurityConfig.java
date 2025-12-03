package com.goats.api.config;

import com.goats.api.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.goats.api.repository.UserRepository;
import com.goats.api.model.User;
import com.goats.api.security.UserDetailsMapper;

import java.util.List;

/**
 * Configuración de seguridad con JWT
 * Define filtros, CORS, autenticación y autorización
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserDetailsMapper userDetailsMapper;

  /**
   * Configura la cadena de filtros de seguridad
   */
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> {})
      .authorizeHttpRequests(auth -> auth
        // Swagger y documentación pública
        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
        // Autenticación pública (login/register)
        .requestMatchers("/api/auth/**").permitAll()
        // GET públicos - consulta de datos
        .requestMatchers(HttpMethod.GET, "/api/players/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/stats/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/achievements/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/subscriptions/player/*/count").permitAll()
        // POST de comentarios - permitir (la autenticación se valida en el controller)
        .requestMatchers(HttpMethod.POST, "/api/comments").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/comments/*/replies").permitAll()
        // Subscriptions y Notifications - permitir con autenticación en controller
        .requestMatchers("/api/subscriptions/**").permitAll()
        .requestMatchers("/api/notifications/**").permitAll()
        // Todo lo demás requiere autenticación
        .anyRequest().authenticated()
      )
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }

  /**
   * Configura CORS para permitir peticiones desde el frontend
   */
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration c = new CorsConfiguration();
    c.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*", "http://localhost", "http://127.0.0.1"));
    c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    c.setAllowedHeaders(List.of("*")); // Permitir todos los headers
    c.setExposedHeaders(List.of("Authorization"));
    c.setAllowCredentials(true);
    c.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
    s.registerCorsConfiguration("/**", c);
    return s;
  }

  /**
   * Bean para encriptar contraseñas
   */
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Bean para el AuthenticationManager
   */
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Bean para cargar detalles de usuario desde la base de datos
   */
  @Bean
  UserDetailsService userDetailsService() {
    return username -> {
      User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
      
      return userDetailsMapper.toUserDetails(user);
    };
  }
}