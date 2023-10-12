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

    private String email;
    private String nomeCompleto;
    private String cpf;
    private String sexo;
    private Integer semestreAtual;
    private String curso;
    private String rua;
    private String bairro;
    private String telefone;
    private String celular;

    private String senha;

}
