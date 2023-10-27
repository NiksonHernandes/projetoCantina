package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardapioRepository extends JpaRepository<Cardapio, Long> {


}
