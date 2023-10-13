package com.cantina.cantina.domain.models.dtos;

import com.cantina.cantina.domain.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsuarioDTO {

    private Long id;
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

    public static UpdateUsuarioDTO toDTO(Usuario obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        UpdateUsuarioDTO dto = modelMapper.map(obj, UpdateUsuarioDTO.class);

        return dto;
    }

}
