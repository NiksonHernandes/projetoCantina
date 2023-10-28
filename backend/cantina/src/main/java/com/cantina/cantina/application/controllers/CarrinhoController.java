package com.cantina.cantina.application.controllers;

import com.cantina.cantina.domain.models.dtos.SignUpDTO;
import com.cantina.cantina.domain.services.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService _carrinhoService;

    @PostMapping("/adicionar-alimento")
    public ResponseEntity<Object> adicionarAlimento(@RequestParam(value = "alimentoId") Long alimentoId,
                                                    @RequestParam(value = "quantidadeAlimento") Integer quantidadeAlimento) {
        try {
            _carrinhoService.adicionarAlimentoNoCarrinho(alimentoId, quantidadeAlimento);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/adicionar-bebida")
    public ResponseEntity<Object> adicionarBebida(@RequestParam(value = "bebidaId") Long bebidaId,
                                                    @RequestParam(value = "quantidadeBebida") Integer quantidadeBebida) {
        try {
            _carrinhoService.adicionarBebidaNoCarrinho(bebidaId, quantidadeBebida);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/delete-carrinho")
    public ResponseEntity<Object> deleteCarrinho(@RequestParam(value = "carrinhoId") Long carrinhoId) {
        try {
            _carrinhoService.deleteCarrinho(carrinhoId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/fechar-carrinho")
    public ResponseEntity<Object> fecharCarrinho(@RequestParam(value = "carrinhoId") Long carrinhoId) {
        try {
            _carrinhoService.fechaCarrinho(carrinhoId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-carrinho")
    public ResponseEntity<Object> getCarrinho(@RequestParam(value = "carrinhoId") Long carrinhoId) {
        try {
            return ResponseEntity.ok(_carrinhoService.getCarrinho(carrinhoId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-carrinho-abertos")
    public ResponseEntity<Object> getCarrinhoAbertos() {
        try {
            return ResponseEntity.ok(_carrinhoService.getCarrinhoAbertos());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @GetMapping("/get-carrinho-fechados")
    public ResponseEntity<Object> getCarrinhoFechados() {
        try {
            return ResponseEntity.ok(_carrinhoService.getCarrinhoFechados());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/list-carrinho")
    public ResponseEntity<Object> listCarrinho() {
        try {
            return ResponseEntity.ok(_carrinhoService.listCarrinho());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/remover-alimento")
    public ResponseEntity<Object> removerAlimento(@RequestParam(value = "alimentoId") Long alimentoId,
                                                    @RequestParam(value = "carrinhoId") Long carrinhoId,
                                                        @RequestParam(value = "quantidadeAlimento") Integer quantidadeAlimento) {
        try {
            _carrinhoService.removerAlimentoDoCarrinho(alimentoId, carrinhoId, quantidadeAlimento);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
