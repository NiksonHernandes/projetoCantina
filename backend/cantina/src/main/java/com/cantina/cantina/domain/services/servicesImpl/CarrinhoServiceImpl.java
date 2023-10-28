package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.*;
import com.cantina.cantina.domain.models.*;
import com.cantina.cantina.domain.models.dtos.CarrinhoDTO;
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
    private BebidaRepository _bebidaRepository;

    @Autowired
    private CarrinhoAlimentoRepository _carrinhoAlimentoRepository;

    @Autowired
    private CarrinhoBebidaRepository _carrinhoBebidaRepository;

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

    @Transactional
    public void somarCarrinho(Long carrinhoId) {
        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        List<CarrinhoAlimento> carrinhoAlimentoList = _carrinhoAlimentoRepository.findByCarrinho_Id(carrinho.getId());
        List<CarrinhoBebida> carrinhoBebidaList = _carrinhoBebidaRepository.findByCarrinho_Id(carrinho.getId());

        if (carrinhoAlimentoList.isEmpty() && carrinhoBebidaList.isEmpty()) {
            throw new IllegalArgumentException("Lista vazia!");
        }

        float valorTotal = calcularValorTotalAlimentos(carrinhoAlimentoList);
        valorTotal += calcularValorTotalBebidas(carrinhoBebidaList);

        carrinho.setValorTotal(valorTotal);
        _carrinhoRepository.save(carrinho);
    }

    private float calcularValorTotalAlimentos(List<CarrinhoAlimento> carrinhoAlimentoList) {
        float valorTotalAlimentos = 0f;

        for (CarrinhoAlimento produto : carrinhoAlimentoList) {
            Alimento alimento = _alimentoRepository.findById(produto.getAlimento().getId())
                    .orElseThrow(() -> new IllegalArgumentException("O ID do alimento não foi encontrado."));

            float precoAlimento = alimento.getValorAlimento();
            int quantidadeSelecionada = produto.getQuantidadeAlimento();

            valorTotalAlimentos += precoAlimento * quantidadeSelecionada;
        }

        return valorTotalAlimentos;
    }

    private float calcularValorTotalBebidas(List<CarrinhoBebida> carrinhoBebidaList) {
        float valorTotalBebidas = 0f;

        for (CarrinhoBebida produto : carrinhoBebidaList) {
            Bebida bebida = _bebidaRepository.findById(produto.getBebida().getId())
                    .orElseThrow(() -> new IllegalArgumentException("O ID da bebida não foi encontrado."));

            float precoBebida = bebida.getValorBebida();
            int quantidadeSelecionada = produto.getQuantidadeBebida();

            valorTotalBebidas += precoBebida * quantidadeSelecionada;
        }

        return valorTotalBebidas;
    }

    @Override
    @Transactional
    public void adicionarAlimentoNoCarrinho(Long alimentoId, Integer quantidadeAlimento) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        if (quantidadeAlimento.describeConstable().isEmpty()) {
            throw new IllegalArgumentException("Informe a quantidade.");
        }

        Alimento alimento = _alimentoRepository.findById(alimentoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do alimento não foi encontrado."));

        Optional<HistoricoPedidos> historicoPedidosOptional = _historicoPedidosRepository.findByUsuario_Id(currentUser.getId());
        Long historicoPedidoId = historicoPedidosOptional.get().getId();

        //Verifica se o existe um carrinho aberto
        Optional<Carrinho> existeCarrinhoAberto = _carrinhoRepository.findByHistoricoPedidos_IdAndCarrinhoFechado(historicoPedidoId, false);

        CarrinhoAlimento carrinhoAlimento = new CarrinhoAlimento();

        if (existeCarrinhoAberto.isPresent()) {
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

            somarCarrinho(existeCarrinhoAberto.get().getId());
        } else  {
            Carrinho carrinhoAberto = abrirCarrinho();

            if (carrinhoAberto != null) {
                // O carrinho foi aberto com sucesso
                carrinhoAlimento.setAlimento(alimento);
                carrinhoAlimento.setCarrinho(carrinhoAberto);
                carrinhoAlimento.setQuantidadeAlimento(quantidadeAlimento);

                _carrinhoAlimentoRepository.save(carrinhoAlimento);

                somarCarrinho(carrinhoAberto.getId());
            } else {
                throw new IllegalArgumentException("Falha ao tentar abrir um novo carrinho");
            }
        }
    }

    @Override
    @Transactional
    public void adicionarBebidaNoCarrinho(Long bebidaId, Integer quantidadeBebida) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        if (quantidadeBebida.describeConstable().isEmpty()) {
            throw new IllegalArgumentException("Informe a quantidade.");
        }

        Bebida bebida = _bebidaRepository.findById(bebidaId)
                .orElseThrow(() -> new IllegalArgumentException("O ID dda bebida não foi encontrado."));

        Optional<HistoricoPedidos> historicoPedidosOptional = _historicoPedidosRepository.findByUsuario_Id(currentUser.getId());
        Long historicoPedidoId = historicoPedidosOptional.get().getId();

        //Verifica se o existe um carrinho aberto
        Optional<Carrinho> existeCarrinhoAberto = _carrinhoRepository.findByHistoricoPedidos_IdAndCarrinhoFechado(historicoPedidoId, false);

        CarrinhoBebida carrinhoBebida = new CarrinhoBebida();

        if (existeCarrinhoAberto.isPresent()) {
            Optional<CarrinhoBebida> existeBebidaNoCarrinho = _carrinhoBebidaRepository.findByBebida_IdAndCarrinho_Id(bebida.getId(), existeCarrinhoAberto.get().getId());

            if (existeBebidaNoCarrinho.isPresent()){
                existeBebidaNoCarrinho.get().setQuantidadeBebida(existeBebidaNoCarrinho.get().getQuantidadeBebida() + quantidadeBebida);

                _carrinhoBebidaRepository.save(existeBebidaNoCarrinho.get());
            } else {
                carrinhoBebida.setBebida(bebida);
                carrinhoBebida.setCarrinho(existeCarrinhoAberto.get());
                carrinhoBebida.setQuantidadeBebida(quantidadeBebida);

                _carrinhoBebidaRepository.save(carrinhoBebida);
            }

            somarCarrinho(existeCarrinhoAberto.get().getId());
        } else  {
            Carrinho carrinhoAberto = abrirCarrinho();

            if (carrinhoAberto != null) {
                // O carrinho foi aberto com sucesso
                carrinhoBebida.setBebida(bebida);
                carrinhoBebida.setCarrinho(carrinhoAberto);
                carrinhoBebida.setQuantidadeBebida(quantidadeBebida);

                _carrinhoBebidaRepository.save(carrinhoBebida);

                somarCarrinho(carrinhoAberto.getId());
            } else {
                throw new IllegalArgumentException("Falha ao tentar abrir um novo carrinho");
            }
        }
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

    @Override
    public CarrinhoDTO getCarrinho(Long carrinhoId) {
        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        return CarrinhoDTO.toDTO(carrinho);
    }

    @Override
    public List<CarrinhoDTO> getCarrinhoAbertos() {
        List<Carrinho> carrinhoList = _carrinhoRepository.findByCarrinhoFechadoIsFalse();

        if (carrinhoList.isEmpty()) {
            throw new IllegalArgumentException("Não há carrinhos abertos.");
        }

        return CarrinhoDTO.toListDTO(carrinhoList);
    }

    @Override
    public List<CarrinhoDTO> getCarrinhoFechados() {
        List<Carrinho> carrinhoList = _carrinhoRepository.findByCarrinhoFechadoIsTrue();

        if (carrinhoList.isEmpty()) {
            throw new IllegalArgumentException("Não há carrinhos fechados.");
        }

        return CarrinhoDTO.toListDTO(carrinhoList);
    }

    @Override
    public List<CarrinhoDTO> listCarrinho() {
        List<Carrinho> carrinhoList = _carrinhoRepository.findAll();

        if (carrinhoList.isEmpty()) {
            throw new IllegalArgumentException("Lista de carrinhos vazia.");
        }

        return CarrinhoDTO.toListDTO(carrinhoList);
    }

    @Override
    @Transactional
    public void removerAlimentoDoCarrinho(Long alimentoId, Long carrinhoId, Integer quantidadeAlimento) {
        if (quantidadeAlimento.describeConstable().isEmpty()) {
            throw new IllegalArgumentException("Informe a quantidade.");
        }

        Alimento alimento = _alimentoRepository.findById(alimentoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do alimento não foi encontrado."));

        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        if (carrinho.getCarrinhoFechado()) {
            throw new IllegalArgumentException("Não pode alterar um carrinho fechado");
        }

        Optional<CarrinhoAlimento> carrinhoAlimento = _carrinhoAlimentoRepository.findByAlimento_IdAndCarrinho_Id(alimento.getId(), carrinho.getId());

        if (carrinhoAlimento.isEmpty()) {
            throw new IllegalArgumentException("Alimento não encontrado.");
        }

        if (quantidadeAlimento.equals(carrinhoAlimento.get().getQuantidadeAlimento())) { //remover tudo
            _carrinhoAlimentoRepository.delete(carrinhoAlimento.get());
        } else {
            carrinhoAlimento.get().setQuantidadeAlimento(quantidadeAlimento);
            _carrinhoAlimentoRepository.save(carrinhoAlimento.get());
        }

        somarCarrinho(carrinho.getId());
    }

}
