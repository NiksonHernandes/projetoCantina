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

    @PostMapping("/abrir-carrinho")
    public ResponseEntity<Object> abrirCarrinho() {
        try {
            _carrinhoService.abrirCarrinho();
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

//    @PostMapping("/adicionar-alimento")
//    public ResponseEntity<Object> adicionarAlimentoNoCarrinho(@RequestBody SignUpDTO signUpDTO) {
//        try {
//            //_usuarioService.signUp(signUpDTO);
//            return ResponseEntity.ok().build();
//        } catch (Exception ex) {
//            return ResponseEntity.badRequest().body(ex.getMessage());
//        }
//    }

}
