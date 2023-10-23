package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.HistoricoPedidos;
import com.cantina.cantina.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoricoPedidosRepository extends JpaRepository<HistoricoPedidos, Long> {

    Optional<HistoricoPedidos> findByUsuario_Id(Long usuarioId);

}
