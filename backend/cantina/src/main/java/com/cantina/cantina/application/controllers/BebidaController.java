package com.cantina.cantina.application.controllers;

import com.cantina.cantina.domain.models.dtos.AlimentoDTO;
import com.cantina.cantina.domain.models.dtos.BebidaDTO;
import com.cantina.cantina.domain.services.BebidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bebida")
public class BebidaController {

    @Autowired
    private BebidaService _bebidaService;

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/create-bebida")
    public ResponseEntity<Object> createBebida(@RequestBody BebidaDTO bebidaDTO) {
        try{
            return ResponseEntity.ok(_bebidaService.createBebida(bebidaDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/delete-bebida")
    public ResponseEntity<Object> deleteBebida(@RequestParam(value = "bebidaId") Long bebidaId) {
        try {
            _bebidaService.deleteBebida(bebidaId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-bebida")
    public ResponseEntity<Object> getBebida(@RequestParam(value = "bebidaId") Long bebidaId) {
        try {
            return ResponseEntity.ok(_bebidaService.getBebida(bebidaId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/list-bebida")
    public ResponseEntity<Object> listBebida() {
        try {
            return ResponseEntity.ok(_bebidaService.listBebida());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/update-bebida")
    public  ResponseEntity<Object> updateBebida(@RequestBody BebidaDTO bebidaDTO) {
        try {
            return ResponseEntity.ok(_bebidaService.updateBebida(bebidaDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
