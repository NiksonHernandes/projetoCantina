package com.cantina.cantina.domain.models;

import com.cantina.cantina.domain.models.dtos.AlimentoDTO;
import com.cantina.cantina.domain.models.dtos.BebidaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class CarrinhoAlimentoEBebida {

    private Long carrinhoId;

    private Float valorTotal;
    private String descricaoDaCompra;
    private Boolean carrinhoFechado;
    private LocalDateTime dataPedido;

    private Integer opcaoPagamento; //0 - qrcode
    //1 - Cartao
    //2 - Na hora

    List<AlimentoDTO> alimentos;
    List<BebidaDTO> bebidas;
}
