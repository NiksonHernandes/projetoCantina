package com.cantina.cantina.domain.services;

import com.cantina.cantina.domain.models.dtos.BebidaDTO;

import java.util.List;

public interface BebidaService {

    BebidaDTO createBebida(BebidaDTO bebidaDTO);
    void deleteBebida(Long bebidaId);
    BebidaDTO getBebida(Long bebidaId);
    List<BebidaDTO> listBebida();
    BebidaDTO updateBebida(BebidaDTO bebidaDTO);

}
