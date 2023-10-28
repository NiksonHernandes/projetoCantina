package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.Alimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

    List<Alimento> findByAlimentoDisponivelIsTrue();

}
