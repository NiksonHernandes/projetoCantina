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
    private LocalDateTime dataPedido;

    //Lista de alimentos
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "carrinho_alimentos",
            joinColumns = @JoinColumn(name = "carrinho_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "alimento_id", referencedColumnName = "id"))
    private List<Alimento> alimentos = new ArrayList<>();

    //Lista de bebidas
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "carrinho_bebidas",
            joinColumns = @JoinColumn(name = "carrinho_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "bebida_id", referencedColumnName = "id"))
    private List<Bebida> bebidas = new ArrayList<>();

    //Relacionamento com o histórico de pedidos
    @ManyToOne
    @JoinColumn(name = "historicoPedido_id")
    //@JsonBackReference
    private HistoricoPedidos historicoPedidos;

}
