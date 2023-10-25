package com.cantina.cantina.domain.services;

import com.cantina.cantina.domain.models.Carrinho;

public interface CarrinhoService {

    Carrinho abrirCarrinho();
    void adicionarAlimentoNoCarrinho(Long alimentoId, Integer quantidadeAlimento); //quantidade
    void deleteCarrinho(Long carrinhoId);
    void fechaCarrinho(Long carrinhoId);

}
