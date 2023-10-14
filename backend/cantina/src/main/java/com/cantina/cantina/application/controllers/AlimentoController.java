package com.cantina.cantina.application.controllers;

import com.cantina.cantina.domain.models.dtos.AlimentoDTO;
import com.cantina.cantina.domain.services.AlimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alimento")
public class AlimentoController {

    @Autowired
    private AlimentoService _alimentoService;

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/create-alimento")
    public ResponseEntity<Object> createAlimento(@RequestBody AlimentoDTO alimentoDTO) {
        try{
            return ResponseEntity.ok(_alimentoService.createAlimento(alimentoDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/delete-alimento")
    public ResponseEntity<Object> deleteAlimento(@RequestParam(value = "alimentoId") Long alimentoId) {
        try {
            _alimentoService.deleteAlimento(alimentoId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-alimento")
    public ResponseEntity<Object> getAlimento(@RequestParam(value = "alimentoId") Long alimentoId) {
        try {
            return ResponseEntity.ok(_alimentoService.getAlimento(alimentoId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/list-alimento")
    public ResponseEntity<Object> listAlimento() {
        try {
            return ResponseEntity.ok(_alimentoService.listAlimento());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/update-alimento")
    public  ResponseEntity<Object> updateAlimento(@RequestBody AlimentoDTO alimentoDTO) {
        try {
            return ResponseEntity.ok(_alimentoService.updateAlimento(alimentoDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
