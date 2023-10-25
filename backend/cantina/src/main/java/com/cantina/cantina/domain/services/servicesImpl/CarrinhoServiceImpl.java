package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.*;
import com.cantina.cantina.domain.models.*;
import com.cantina.cantina.domain.services.CarrinhoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    @Autowired
    private AlimentoRepository _alimentoRepository;

    @Autowired
    private CarrinhoAlimentoRepository _carrinhoAlimentoRepository;

    @Transactional
    public Carrinho abrirCarrinho() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        //Adiciona ele no histórico de pedidos do cliente
        Optional<HistoricoPedidos> historicoPedidosOptional = _historicoPedidosRepository.findByUsuario_Id(currentUser.getId());

        if (historicoPedidosOptional.isEmpty()) {
            throw new IllegalArgumentException("Histórico de pedidos não encontrado");
        }

        Carrinho novoCarrinho = new Carrinho();
        novoCarrinho.setCarrinhoFechado(false);
        novoCarrinho.setHistoricoPedidos(historicoPedidosOptional.get());

        novoCarrinho = _carrinhoRepository.save(novoCarrinho);

        return novoCarrinho;
    }

    @Override
    @Transactional
    public void adicionarAlimentoNoCarrinho(Long alimentoId, Integer quantidadeAlimento) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        Alimento alimento = _alimentoRepository.findById(alimentoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do alimento não foi encontrado."));

        Optional<HistoricoPedidos> historicoPedidosOptional = _historicoPedidosRepository.findByUsuario_Id(currentUser.getId());
        Long historicoPedidoId = historicoPedidosOptional.get().getId();

        //Verifica se o existe um carrinho aberto
        Optional<Carrinho> existeCarrinhoAberto = _carrinhoRepository.findByHistoricoPedidos_IdAndCarrinhoFechado(historicoPedidoId, false);

        CarrinhoAlimento carrinhoAlimento = new CarrinhoAlimento();

        if (existeCarrinhoAberto.isPresent()) {
            //throw new IllegalArgumentException("EXISTE UM CARRInhO ABERTO");
            Optional<CarrinhoAlimento> existeAlimentoNoCarrinho = _carrinhoAlimentoRepository.findByAlimento_IdAndCarrinho_Id(alimento.getId(), existeCarrinhoAberto.get().getId());

            if (existeAlimentoNoCarrinho.isPresent()){
                existeAlimentoNoCarrinho.get().setQuantidadeAlimento(existeAlimentoNoCarrinho.get().getQuantidadeAlimento() + quantidadeAlimento);

                _carrinhoAlimentoRepository.save(existeAlimentoNoCarrinho.get());
            } else {
                carrinhoAlimento.setAlimento(alimento);
                carrinhoAlimento.setCarrinho(existeCarrinhoAberto.get());
                carrinhoAlimento.setQuantidadeAlimento(quantidadeAlimento);

                _carrinhoAlimentoRepository.save(carrinhoAlimento);
            }

        } else  {
            Carrinho carrinhoAberto = abrirCarrinho();

            if (carrinhoAberto != null) {
                // O carrinho foi aberto com sucesso
                carrinhoAlimento.setAlimento(alimento);
                carrinhoAlimento.setCarrinho(carrinhoAberto);
                carrinhoAlimento.setQuantidadeAlimento(quantidadeAlimento);

                _carrinhoAlimentoRepository.save(carrinhoAlimento);
                System.out.println("KKKKKKKKKKKKKKKKKKKKKKKK CRIADO COM SUCESSO");
            } else {
                // Trate o caso em que o carrinho não foi aberto com sucess
                throw new IllegalArgumentException("Falha ao tentar abrir um novo carrinho");
            }
        }





        //Se já tiver criado ele só atribui




        //verificar se ele ta disponível, verificar a quantidade no estoque

    }

    @Override
    @Transactional
    public void deleteCarrinho(Long carrinhoId) {
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

        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        if (!usuario.getId().equals(carrinho.getHistoricoPedidos().getId())) {
            throw new IllegalArgumentException("Você não pode fechar o carrinho de outra pessoa.");
        }

        carrinho.setCarrinhoFechado(true);
        carrinho.setDataPedido(LocalDateTime.now());

        _carrinhoRepository.save(carrinho);
    }

}
