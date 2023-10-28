package com.cantina.cantina.domain.services;

import com.cantina.cantina.domain.models.dtos.AlimentoDTO;

import java.util.List;

public interface AlimentoService {

    AlimentoDTO createAlimento(AlimentoDTO alimentoDTO);
    void deleteAlimento(Long alimentoId);
    AlimentoDTO getAlimento(Long alimentoId);
    List<AlimentoDTO> listAlimento();
    AlimentoDTO updateAlimento(AlimentoDTO alimentoDTO);

}
