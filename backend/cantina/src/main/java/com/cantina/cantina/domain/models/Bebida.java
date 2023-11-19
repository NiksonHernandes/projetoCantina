package com.cantina.cantina.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bebida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeBebida;
    private Float valorBebida;
    private Integer qntEstoqueBebida;
    private Boolean bebidaDisponivel;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricaoBebida;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String linkBebida;

}
