package com.cantina.cantina.domain.models.dtos;

import com.cantina.cantina.domain.models.Usuario;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL) //Ignora os valores vazios
public class UsuarioDTO {

    private Long id;

    private String email;
    private String username;
    private String nomeCompleto;
    private String cpf;
    private String sexo;
    private Integer semestreAtual;
    private String curso;
    private String rua;
    private String bairro;
    private String telefone;
    private String celular;

    public static UsuarioDTO toDTO(Usuario obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        UsuarioDTO dto = modelMapper.map(obj, UsuarioDTO.class); //transforma obj passado para UsuarioDTO

        return dto;
    }

    public static List<UsuarioDTO > toListDTO(List<Usuario> obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        List<UsuarioDTO > dtos = obj
                .stream()
                .map(x -> modelMapper.map(x, UsuarioDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

}
