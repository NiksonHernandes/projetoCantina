package com.cantina.cantina.domain.services;

import com.cantina.cantina.domain.models.Cardapio;
import com.cantina.cantina.domain.models.dtos.CardapioDTO;

public interface CardapioService {

    void createCardapio(Cardapio cardapio);
    CardapioDTO getCardapio(Long cardapioId);

}
