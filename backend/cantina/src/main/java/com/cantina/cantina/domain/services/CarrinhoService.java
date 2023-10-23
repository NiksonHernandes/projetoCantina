package com.cantina.cantina.domain.services;

public interface CarrinhoService {

    void abrirCarrinho();
    //void adicionarAlimentoNoCarrinho(Long alimentoId); //quantidade
    void deleteCarrinho(Long carrinhoId);
    void fechaCarrinho(Long carrinhoId);

}
