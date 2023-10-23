package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.CarrinhoRepository;
import com.cantina.cantina.data.repositories.HistoricoPedidosRepository;
import com.cantina.cantina.data.repositories.UsuarioRepository;
import com.cantina.cantina.domain.models.Carrinho;
import com.cantina.cantina.domain.models.HistoricoPedidos;
import com.cantina.cantina.domain.models.Usuario;
import com.cantina.cantina.domain.services.CarrinhoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {

    @Autowired
    private CarrinhoRepository _carrinhoRepository;

    @Autowired
    private HistoricoPedidosRepository _historicoPedidosRepository;

    @Autowired
    private UsuarioRepository _usuarioRepository;

    @Override
    @Transactional
    public void abrirCarrinho() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        //Adiciona ele no histórico de pedidos do cliente
        HistoricoPedidos novoHistoricoPedidos = new HistoricoPedidos();
        Optional<HistoricoPedidos> historicoPedidosOptional = _historicoPedidosRepository.findByUsuario_Id(currentUser.getId());

        if (historicoPedidosOptional.isEmpty()) {
            throw new IllegalArgumentException("Histórico de pedidos não encontrado");
        }

        Carrinho novoCarrinho = new Carrinho();
        novoCarrinho.setCarrinhoFechado(false);
        //novoCarrinho.setDataPedido(LocalDateTime.now());
        novoCarrinho.setHistoricoPedidos(historicoPedidosOptional.get());

        _carrinhoRepository.save(novoCarrinho);
    }

    @Override
    @Transactional
    public void deleteCarrinho(Long carrinhoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        _carrinhoRepository.delete(carrinho);
    }

    @Override
    public void fechaCarrinho(Long carrinhoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        Usuario usuario = _usuarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("O ID do usuário não foi encontrado."));

        if (!currentUser.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Você não pode remover o carrinho de outra pessoa.");
        }

        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        carrinho.setCarrinhoFechado(true);

        _carrinhoRepository.save(carrinho);
    }

}
