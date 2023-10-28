package com.cantina.cantina.data.repositories;

import com.cantina.cantina.domain.models.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    List<Carrinho> findByCarrinhoFechadoIsTrue(); //Carrinho feechado
    List<Carrinho> findByCarrinhoFechadoIsFalse(); //Carrinho Aberto
    List<Carrinho> findByHistoricoPedidos_Id(Long historicoPedidoId);
    Optional<Carrinho> findByHistoricoPedidos_IdAndCarrinhoFechado(Long historicoPedidoId, Boolean isCarrinhoFechado);

}
