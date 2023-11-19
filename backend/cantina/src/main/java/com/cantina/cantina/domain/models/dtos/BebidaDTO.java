package com.cantina.cantina.domain.models.dtos;

import com.cantina.cantina.domain.models.Bebida;
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
public class BebidaDTO {

    private Long bebidaId;
    private String nomeBebida;
    private Float valorBebida;
    private Integer qntEstoqueBebida;
    private String descricaoBebida;
    private Boolean bebidaDisponivel;
    private String linkBebida;

    private Integer quantidadeBebidaCarrinho;
    private Float somaBebidaNoCarrinho;

    public static BebidaDTO toDTO(Bebida obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        BebidaDTO dto = modelMapper.map(obj, BebidaDTO.class); //transforma obj passado para UsuarioDTO

        return dto;
    }

    public static List<BebidaDTO> toListDTO(List<Bebida> obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        List<BebidaDTO> dtos = obj
                .stream()
                .map(x -> modelMapper.map(x, BebidaDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

}
