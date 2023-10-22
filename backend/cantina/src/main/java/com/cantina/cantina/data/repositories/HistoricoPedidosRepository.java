package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.HistoricoPedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoPedidosRepository extends JpaRepository<HistoricoPedidos, Long> {
}
