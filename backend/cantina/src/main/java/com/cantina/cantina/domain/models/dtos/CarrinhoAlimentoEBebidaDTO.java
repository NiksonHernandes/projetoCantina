package com.cantina.cantina.domain.models.dtos;

import com.cantina.cantina.domain.models.Carrinho;
import com.cantina.cantina.domain.models.CarrinhoAlimentoEBebida;
import com.cantina.cantina.domain.models.HistoricoPedidos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoAlimentoEBebidaDTO {

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

    private Integer tipoCartao; //0 - Credito
    //1 - débito
    private Long numeroCartao;
    private String validadeCartao;
    private Long codigoCartao;

    private String codigoDoPedido;

    List<AlimentoDTO> alimentos;
    List<BebidaDTO> bebidas;

    public static CarrinhoAlimentoEBebidaDTO toDTO(CarrinhoAlimentoEBebida obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        CarrinhoAlimentoEBebidaDTO dto = modelMapper.map(obj, CarrinhoAlimentoEBebidaDTO.class); //transforma obj passado para UsuarioDTO

        dto.setAlimentos(obj.getAlimentos());
        dto.setBebidas(obj.getBebidas());
        return dto;
    }

}
