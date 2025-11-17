package com.goats.api.security;

import com.goats.api.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir User entities a Spring Security UserDetails
 * Elimina código duplicado en SecurityConfig y JwtAuthenticationFilter
 */
@Component
public class UserDetailsMapper {
    
    /**
     * Convierte un User de la BD a UserDetails de Spring Security
     * @param user entidad User
     * @return UserDetails para autenticación
     */
    public UserDetails toUserDetails(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPasswordHash(),
            user.getEnabled(),
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            authorities
        );
    }
}
