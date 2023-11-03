package com.cantina.cantina.domain.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float valorTotal;
    private String descricaoDaCompra;
    private Boolean carrinhoFechado;
    private LocalDateTime dataPedido;
    private Integer opcaoPagamento; //0 - qrcode
    //1 - Cartao
    //2 - Na hora

    //Relacionamento com o histórico de pedidos
    @ManyToOne
    @JoinColumn(name = "historicoPedido_id")
    //@JsonBackReference
    private HistoricoPedidos historicoPedidos;

}
