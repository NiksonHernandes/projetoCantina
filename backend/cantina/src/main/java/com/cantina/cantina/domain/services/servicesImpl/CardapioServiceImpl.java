package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.AlimentoRepository;
import com.cantina.cantina.data.repositories.BebidaRepository;
import com.cantina.cantina.data.repositories.CardapioRepository;
import com.cantina.cantina.domain.models.Alimento;
import com.cantina.cantina.domain.models.Bebida;
import com.cantina.cantina.domain.models.Cardapio;
import com.cantina.cantina.domain.models.dtos.AlimentoDTO;
import com.cantina.cantina.domain.models.dtos.BebidaDTO;
import com.cantina.cantina.domain.models.dtos.CardapioDTO;
import com.cantina.cantina.domain.services.CardapioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardapioServiceImpl implements CardapioService {

    @Autowired
    private CardapioRepository _cardapioRepository;

    @Autowired
    private AlimentoRepository _alimentoRepository;

    @Autowired
    private BebidaRepository _bebidaRepository;

    @Override
    @Transactional
    public void createCardapio(Cardapio cardapio) {
        Cardapio novoCardapio = new Cardapio();
        novoCardapio.setPromocoes(cardapio.getPromocoes());

        _cardapioRepository.save(novoCardapio);
    }

    @Override
    public CardapioDTO getCardapio(Long cardapioId) {
        Cardapio cardapio =  _cardapioRepository.findById(cardapioId)
                .orElseThrow(() -> new IllegalArgumentException("Cardápio não encontrado."));

        List<Alimento> alimentoList = _alimentoRepository.findByAlimentoDisponivelIsTrue();

        List<Bebida> bebidaList = _bebidaRepository.findByBebidaDisponivelIsTrue();

        CardapioDTO cardapioDTO = CardapioDTO.toDTO(cardapio);
        cardapioDTO.setAlimentos(AlimentoDTO.toListDTO(alimentoList));
        cardapioDTO.setBebidas(BebidaDTO.toListDTO(bebidaList));


        return cardapioDTO;
    }

}
