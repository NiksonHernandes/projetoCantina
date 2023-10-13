package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);

}
