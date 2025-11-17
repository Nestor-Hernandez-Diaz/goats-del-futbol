package com.goats.api.repository;

import com.goats.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad User
 * Proporciona operaciones CRUD y búsquedas personalizadas
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su username
     * @param username nombre de usuario único
     * @return Optional con el usuario encontrado
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por su email
     * @param email correo electrónico único
     * @return Optional con el usuario encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el username dado
     * @param username nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email dado
     * @param email correo electrónico
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por username o lanza excepción si no existe
     * Método helper para evitar código duplicado
     * @param username nombre de usuario
     * @return User encontrado
     * @throws RuntimeException si el usuario no existe
     */
    default User findByUsernameOrThrow(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
