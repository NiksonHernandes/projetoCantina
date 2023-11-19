package com.cantina.cantina.domain.models.dtos;

import com.cantina.cantina.domain.models.Bebida;
import com.cantina.cantina.domain.models.Carrinho;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoDTO {

    private Long carrinhoId;
    private Float valorTotal;
    private String descricaoDaCompra;
    private Boolean carrinhoFechado;
    private LocalDateTime dataPedido;

    private Integer opcaoPagamento; //0 - qrcode
    //1 - Cartao
    //2 - Na hora
    private Integer statusPedido;
    //0 - pedido em análise
    //1 - aprovada
    //2 - recusado
    //3 - entregue
    //4- pedido cancelado

    private Integer tipoCartao; //0 - Credito
    //1 - débito
    private Long numeroCartao;
    private String validadeCartao;
    private Long codigoCartao;

    private String codigoDoPedido;
    private String nomeUsuario;
    public static CarrinhoDTO toDTO(Carrinho obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        CarrinhoDTO dto = modelMapper.map(obj, CarrinhoDTO.class); //transforma obj passado para UsuarioDTO

        return dto;
    }

    public static List<CarrinhoDTO> toListDTO(List<Carrinho> obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        List<CarrinhoDTO> dtos = obj
                .stream()
                .map(x -> modelMapper.map(x, CarrinhoDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

}
