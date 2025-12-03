package com.goats.api.security;

import com.goats.api.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Proveedor de tokens JWT
 * Genera, valida y extrae información de tokens JWT
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:goatsFutbolSecretKeyForJWT2025MustBeLongEnoughForHS512Algorithm}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}") // 24 horas en milisegundos
    private long jwtExpiration;

    /**
     * Genera un token JWT para el usuario autenticado
     * @param authentication información de autenticación
     * @return token JWT como String
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Genera un token JWT para un usuario específico
     * @param user usuario para el cual generar el token
     * @return token JWT como String
     */
    public String generateTokenFromUser(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", roles)
                .claim("userId", user.getId())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el username del token JWT
     * @param token token JWT
     * @return username extraído
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Valida el token JWT
     * @param token token JWT a validar
     * @return true si es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty");
        }
        return false;
    }

    /**
     * Obtiene la clave de firma para JWT
     * @return SecretKey para firmar y verificar tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
