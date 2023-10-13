package com.cantina.cantina.domain.models.dtos;

import lombok.Data;

@Data
public class SignUpDTO {

    private String nomeCompleto;
    private String email;
    private String username;
    private String senha;
    private String senhaConfirmacao;

    private String cpf;
    private String sexo;
    private Integer semestreAtual;
    private String curso;
    private String rua;
    private String bairro;
    private String telefone;
    private String celular;

}
