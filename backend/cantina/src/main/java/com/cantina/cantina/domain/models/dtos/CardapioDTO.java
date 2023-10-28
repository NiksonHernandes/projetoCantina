package com.cantina.cantina.domain.models.dtos;

import com.cantina.cantina.domain.models.Cardapio;
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
public class CardapioDTO {

    private Long id;
    private String promocoes;

    private List<AlimentoDTO> alimentos;
    private List<BebidaDTO> bebidas;

    public static CardapioDTO toDTO(Cardapio obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        CardapioDTO dto = modelMapper.map(obj, CardapioDTO.class); //transforma obj passado para UsuarioDTO

        return dto;
    }

    public static List<CardapioDTO> toListDTO(List<Cardapio> obj) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        List<CardapioDTO> dtos = obj
                .stream()
                .map(x -> modelMapper.map(x, CardapioDTO.class))
                .collect(Collectors.toList());

        return dtos;
    }

}
