package com.cantina.cantina.application.controllers;

import com.cantina.cantina.domain.models.Cardapio;
import com.cantina.cantina.domain.models.dtos.SignUpDTO;
import com.cantina.cantina.domain.services.CardapioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cardapio")
public class CardapioController {

    @Autowired
    private CardapioService _cardapioService;

    @PostMapping("/create-cardapio")
    public ResponseEntity<Object> createCardapio(@RequestBody Cardapio cardapio) {
        try {
            _cardapioService.createCardapio(cardapio);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-cardapio")
    public ResponseEntity<Object> getCardapio(@RequestParam(value = "cardapioId") Long cardapioId) {
        try {
            return ResponseEntity.ok(_cardapioService.getCardapio(cardapioId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
