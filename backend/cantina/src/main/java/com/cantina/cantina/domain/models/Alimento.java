package com.cantina.cantina.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeAlimento;
    private Float valorAlimento;
    private Integer qntEstoqueAlimento;
    private Boolean alimentoDisponivel;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String descricaoAlimento;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String linkAlimento;

}
