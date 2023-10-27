package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.Alimento;
import com.cantina.cantina.domain.models.Bebida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BebidaRepository extends JpaRepository<Bebida, Long> {

    List<Bebida> findByBebidaDisponivelIsTrue();

}
