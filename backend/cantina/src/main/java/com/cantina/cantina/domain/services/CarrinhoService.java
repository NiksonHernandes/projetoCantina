package com.cantina.cantina.domain.services;

import com.cantina.cantina.domain.models.dtos.CarrinhoAlimentoEBebidaDTO;
import com.cantina.cantina.domain.models.dtos.CarrinhoDTO;

import java.util.List;

public interface CarrinhoService {

    void adicionarAlimentoNoCarrinho(Long alimentoId, Integer quantidadeAlimento);
    void adicionarBebidaNoCarrinho(Long bebidaId, Integer quantidadeBebida);
    void deleteCarrinho(Long carrinhoId);
    void fechaCarrinho(Long carrinhoId); //Quando fechar tem que reduzir a quantidade no estoque
    void finalizarPedido(CarrinhoDTO carrinhoDTO);
    CarrinhoDTO getCarrinho(Long carrinhoId);
    List<CarrinhoDTO> getCarrinhoAbertos();
    CarrinhoAlimentoEBebidaDTO getCarrinhoProdutos(Long carrinhoId);
    List<CarrinhoDTO> getCarrinhoFechados();
    List<CarrinhoDTO> listCarrinho();
    void opcaoPagamento(Long carrinhoId, Integer opcao);
    void removerAlimentoDoCarrinho(Long alimentoId, Long carrinhoId, Integer quantidadeAlimento);
    void removerBebidaDoCarrinho(Long bebidaId, Long carrinhoId, Integer quantidadeBebida);
    void resetarOpcao(Long carrinhoId);
    CarrinhoDTO verificaIsCarrinhoExiste();




    //remover alimento do carrinho
    //remover todos os alimentos do carrinho -> posso usar o deleteCarrinho

}
