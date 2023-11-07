package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.*;
import com.cantina.cantina.domain.models.*;
import com.cantina.cantina.domain.models.dtos.AlimentoDTO;
import com.cantina.cantina.domain.models.dtos.BebidaDTO;
import com.cantina.cantina.domain.models.dtos.CarrinhoAlimentoEBebidaDTO;
import com.cantina.cantina.domain.models.dtos.CarrinhoDTO;
import com.cantina.cantina.domain.services.CarrinhoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        if (existeCarrinhoAberto.isPresent()) {
            if (existeCarrinhoAberto.get().getStatusPedido() != null) {
                if (existeCarrinhoAberto.get().getStatusPedido() == 0) {
                    throw new IllegalArgumentException("Aguardando para adicionar. Pedido em análise");
                }
            }
        }

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
    public List<CarrinhoDTO> getCarrinhoPedidoPendente() {
        List<Carrinho> carrinhoListPendentes = _carrinhoRepository.findByStatusPedido(0);

        if (carrinhoListPendentes.isEmpty()) {
            throw new IllegalArgumentException("Não há pedidos pendentes.");
        }

        return CarrinhoDTO.toListDTO(carrinhoListPendentes);
    }

    @Override
    public List<CarrinhoDTO> getCarrinhoPedidoAprovados() {
        List<Carrinho> carrinhoListPendentes = _carrinhoRepository.findByStatusPedido(1);

        if (carrinhoListPendentes.isEmpty()) {
            throw new IllegalArgumentException("Não há pedidos aprovados. Lista vazia!");
        }

        return CarrinhoDTO.toListDTO(carrinhoListPendentes);
    }

    @Override
    public List<CarrinhoDTO> getCarrinhoPedidoRecusados() {
        List<Carrinho> carrinhoListPendentes = _carrinhoRepository.findByStatusPedido(2);

        if (carrinhoListPendentes.isEmpty()) {
            throw new IllegalArgumentException("Não há pedidos recusados. Lista vazia!");
        }

        return CarrinhoDTO.toListDTO(carrinhoListPendentes);
    }

    @Override
    public List<CarrinhoDTO> getCarrinhoPedidoCancelados() {
        List<Carrinho> carrinhoListPendentes = _carrinhoRepository.findByStatusPedido(4);

        if (carrinhoListPendentes.isEmpty()) {
            throw new IllegalArgumentException("Não há pedidos cancelados. Lista vazia!");
        }

        return CarrinhoDTO.toListDTO(carrinhoListPendentes);
    }

    @Override
    public List<CarrinhoDTO> getCarrinhoPedidoEntregues() {
        List<Carrinho> carrinhoListPendentes = _carrinhoRepository.findByStatusPedido(3);

        if (carrinhoListPendentes.isEmpty()) {
            throw new IllegalArgumentException("Não há pedidos Entregues. Lista vazia!");
        }

        return CarrinhoDTO.toListDTO(carrinhoListPendentes);
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

        carrinho.setCarrinhoFechado(true);
        carrinho.setStatusPedido(3);
        carrinho.setDataPedido(LocalDateTime.now());

        _carrinhoRepository.save(carrinho);
    }

    @Override
    public void finalizarPedido(CarrinhoDTO carrinhoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        Carrinho carrinho = _carrinhoRepository.findById(carrinhoDTO.getCarrinhoId())
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        if (carrinho.getOpcaoPagamento() == 1) {
            carrinho.setTipoCartao(carrinhoDTO.getTipoCartao());
            carrinho.setNumeroCartao(carrinhoDTO.getNumeroCartao());
            carrinho.setValidadeCartao(carrinhoDTO.getValidadeCartao());
            carrinho.setCodigoCartao(carrinhoDTO.getCodigoCartao());
        } else if (carrinho.getOpcaoPagamento() == 2) {
             carrinho.setCodigoDoPedido("SXXIOA-00917-OPALDD");
        }

        carrinho.setStatusPedido(0);
        carrinho.setNomeUsuario(currentUser.getNomeCompleto());

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
    public CarrinhoAlimentoEBebidaDTO getCarrinhoProdutos(Long carrinhoId) {
        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        //Alimento lista
        List<CarrinhoAlimento> carrinhoAlimentoIdList = _carrinhoAlimentoRepository.findByCarrinho_Id(carrinho.getId());
        List<AlimentoDTO> alimentoList = new ArrayList<>();

        for (CarrinhoAlimento carrinhoAlimento : carrinhoAlimentoIdList) {
            Optional<Alimento> alimento = _alimentoRepository.findById(carrinhoAlimento.getAlimento().getId());

            if (alimento.isPresent()) {
                AlimentoDTO alimentoDTO = AlimentoDTO.toDTO(alimento.get());
                alimentoDTO.setQuantidadeAlimentoCarrinho(carrinhoAlimento.getQuantidadeAlimento());

                Float somaProduto = carrinhoAlimento.getQuantidadeAlimento() * alimento.get().getValorAlimento();
                alimentoDTO.setSomaAlimentoNoCarrinho(somaProduto);

                alimentoList.add(alimentoDTO);
            }
        }

        //Bebida lista
        List<CarrinhoBebida> carrinhoBebidaIdList = _carrinhoBebidaRepository.findByCarrinho_Id(carrinho.getId());
        List<BebidaDTO> bebidaList = new ArrayList<>();

        for (CarrinhoBebida carrinhoBebida : carrinhoBebidaIdList) {
            Optional<Bebida> bebida = _bebidaRepository.findById(carrinhoBebida.getBebida().getId());

            if (bebida.isPresent()) {
                BebidaDTO bebidaDTO = BebidaDTO.toDTO(bebida.get());
                bebidaDTO.setQuantidadeBebidaCarrinho(carrinhoBebida.getQuantidadeBebida());

                Float somaProduto = carrinhoBebida.getQuantidadeBebida() * bebida.get().getValorBebida();
                bebidaDTO.setSomaBebidaNoCarrinho(somaProduto);

                bebidaList.add(bebidaDTO);
            }
        }

        CarrinhoAlimentoEBebida newCarrinhoAlimentoEBebida = new CarrinhoAlimentoEBebida();
        newCarrinhoAlimentoEBebida.setValorTotal(carrinho.getValorTotal());
        newCarrinhoAlimentoEBebida.setDescricaoDaCompra(carrinho.getDescricaoDaCompra());
        newCarrinhoAlimentoEBebida.setCarrinhoFechado(carrinho.getCarrinhoFechado());
        newCarrinhoAlimentoEBebida.setDataPedido(carrinho.getDataPedido());
        newCarrinhoAlimentoEBebida.setCarrinhoId(carrinho.getId());
        newCarrinhoAlimentoEBebida.setOpcaoPagamento(carrinho.getOpcaoPagamento());

        newCarrinhoAlimentoEBebida.setStatusPedido(carrinho.getStatusPedido());
        newCarrinhoAlimentoEBebida.setTipoCartao(carrinho.getTipoCartao());
        newCarrinhoAlimentoEBebida.setNumeroCartao(carrinho.getNumeroCartao());
        newCarrinhoAlimentoEBebida.setValidadeCartao(carrinho.getValidadeCartao());
        newCarrinhoAlimentoEBebida.setCodigoCartao(carrinho.getCodigoCartao());
        newCarrinhoAlimentoEBebida.setCodigoDoPedido(carrinho.getCodigoDoPedido());


        newCarrinhoAlimentoEBebida.setAlimentos(alimentoList);
        newCarrinhoAlimentoEBebida.setBebidas(bebidaList);

        return CarrinhoAlimentoEBebidaDTO.toDTO(newCarrinhoAlimentoEBebida);
    }

    @Override
    public List<CarrinhoDTO> getCarrinhoFechados() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        Optional<HistoricoPedidos> historicoPedidosOptional = _historicoPedidosRepository.findByUsuario_Id(currentUser.getId());

        if (historicoPedidosOptional.isEmpty()){
            throw new IllegalArgumentException("Não há histórico de pedidos.");
        }

        List<Carrinho> carrinhoList = _carrinhoRepository.findByHistoricoPedidos_IdAndCarrinhoFechadoIsTrue(historicoPedidosOptional.get().getId());

        if (carrinhoList.isEmpty()) {
            throw new IllegalArgumentException("Ainda não há um histórico de pedidos.");
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
    public void opcaoPagamento(Long carrinhoId, Integer opcao) {
        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        carrinho.setOpcaoPagamento(opcao);
        _carrinhoRepository.save(carrinho);
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

        if (quantidadeAlimento == 0) {
            _carrinhoAlimentoRepository.delete(carrinhoAlimento.get());
        } else {
            carrinhoAlimento.get().setQuantidadeAlimento(quantidadeAlimento);
            _carrinhoAlimentoRepository.save(carrinhoAlimento.get());
        }

        somarCarrinho(carrinho.getId());
    }

    @Override
    @Transactional
    public void removerBebidaDoCarrinho(Long bebidaId, Long carrinhoId, Integer quantidadeBebida) {
        if (quantidadeBebida.describeConstable().isEmpty()) {
            throw new IllegalArgumentException("Informe a quantidade.");
        }

        Bebida bebida  = _bebidaRepository.findById(bebidaId)
                .orElseThrow(() -> new IllegalArgumentException("O ID da bebida não foi encontrado."));

        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        if (carrinho.getCarrinhoFechado()) {
            throw new IllegalArgumentException("Não pode alterar um carrinho fechado");
        }

        Optional<CarrinhoBebida> carrinhoBebida = _carrinhoBebidaRepository.findByBebida_IdAndCarrinho_Id(bebida.getId(), carrinho.getId());

        if (carrinhoBebida.isEmpty()) {
            throw new IllegalArgumentException("Bebida não encontrado.");
        }

        if (quantidadeBebida == 0) { //remover tudo
            _carrinhoBebidaRepository.delete(carrinhoBebida.get());
        } else {
            carrinhoBebida.get().setQuantidadeBebida(quantidadeBebida);
            _carrinhoBebidaRepository.save(carrinhoBebida.get());
        }

        somarCarrinho(carrinho.getId());
    }

    @Override
    public void resetarOpcao(Long carrinhoId) {
        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        carrinho.setOpcaoPagamento(null);
        _carrinhoRepository.save(carrinho);
    }

    @Override
    public CarrinhoDTO verificaIsCarrinhoExiste() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        Optional<HistoricoPedidos> historicoPedidosOptional = _historicoPedidosRepository.findByUsuario_Id(currentUser.getId());

        if (historicoPedidosOptional.isEmpty()) {
            throw new IllegalArgumentException("Histórico de pedidos não encontrado");
        }

        List<Carrinho> carrinhoList = _carrinhoRepository.findByHistoricoPedidos_Id(historicoPedidosOptional.get().getId());

        if (carrinhoList.isEmpty()) {
            throw new IllegalArgumentException("Carrinho vazio! Adicione algum item ao carrinho.");
        }

        Carrinho carrinho = new Carrinho();

        for (Carrinho car : carrinhoList) {
            if (!car.getCarrinhoFechado()) { //Se existir carrinho aberto
                carrinho = car;
            }
        }

        if (carrinho.getId() == null) {
            throw new IllegalArgumentException("Carrinho vazio! Adicione algum item ao carrinho.");
        }

        return CarrinhoDTO.toDTO(carrinho);
    }

    @Override
    public void aceitarPedido(Long carrinhoId) {
        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        carrinho.setStatusPedido(1);
        _carrinhoRepository.save(carrinho);
    }

    @Override
    public void recusarPedido(Long carrinhoId) {
        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        carrinho.setStatusPedido(2);
        _carrinhoRepository.save(carrinho);
    }

    @Override
    public void cancelarPedido(Long carrinhoId) {
        Carrinho carrinho = _carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do carrinho não foi encontrado."));

        carrinho.setStatusPedido(4);
        carrinho.setCarrinhoFechado(true);
        _carrinhoRepository.save(carrinho);
    }

}
