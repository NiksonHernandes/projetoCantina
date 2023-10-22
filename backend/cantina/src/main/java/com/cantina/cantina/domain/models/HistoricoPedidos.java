package com.cantina.cantina.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPedidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "historicoPedidos", cascade = CascadeType.ALL)
    @OrderBy("dataPedido ASC")
    private List<Carrinho> carrinhoList = new ArrayList<Carrinho>();

}
