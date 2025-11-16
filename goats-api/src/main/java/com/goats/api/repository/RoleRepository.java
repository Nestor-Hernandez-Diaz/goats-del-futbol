package com.goats.api.repository;

import com.goats.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Role
 * Proporciona operaciones CRUD y búsquedas personalizadas
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre
     * @param name nombre del rol (ej: "ROLE_ADMIN")
     * @return Optional con el rol encontrado
     */
    Optional<Role> findByName(String name);

    /**
     * Verifica si existe un rol con el nombre dado
     * @param name nombre del rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}
