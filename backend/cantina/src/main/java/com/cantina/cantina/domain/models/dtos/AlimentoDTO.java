package com.cantina.cantina.domain.models.dtos;

import com.cantina.cantina.domain.models.Alimento;
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
public class AlimentoDTO {

    private Long alimentoId;
    private String nomeAlimento;
    private Float valorAlimento;
    private Integer qntEstoqueAlimento;
    private String descricaoAlimento;
    private Boolean alimentoDisponivel;

    public static AlimentoDTO toDTO(Alimento obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        AlimentoDTO dto = modelMapper.map(obj, AlimentoDTO.class); //transforma obj passado para UsuarioDTO

        return dto;
    }

    public static List<AlimentoDTO> toListDTO(List<Alimento> obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        List<AlimentoDTO> dtos = obj
                .stream()
                .map(x -> modelMapper.map(x, AlimentoDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

}
