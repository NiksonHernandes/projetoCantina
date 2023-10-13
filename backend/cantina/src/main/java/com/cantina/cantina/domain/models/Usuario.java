package com.cantina.cantina.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email = "";
    private String username = "";
    private String nomeCompleto = "";

    private String senha;

    private String cpf = "";
    private String sexo = "";
    private Integer semestreAtual = 0;
    private String curso = "";
    private String rua = "";
    private String bairro = "";
    private String telefone = "";
    private String celular = "";

}
