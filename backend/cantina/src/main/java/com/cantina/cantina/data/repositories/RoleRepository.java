package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String nome);

}
