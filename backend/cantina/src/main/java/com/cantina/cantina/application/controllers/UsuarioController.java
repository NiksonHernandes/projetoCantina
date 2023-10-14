package com.cantina.cantina.application.controllers;

import com.cantina.cantina.domain.models.dtos.SignUpDTO;
import com.cantina.cantina.domain.models.dtos.UpdateUsuarioDTO;
import com.cantina.cantina.domain.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService _usuarioService;

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/delete-usuario")
    public ResponseEntity<Object> deleteUsuario(@RequestParam(value = "usuarioId") Long usuarioId) {
        try {
            _usuarioService.deleteUsuario(usuarioId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-current-usuario")
    public ResponseEntity<Object> getCurrentUsuario() {
        try {
            return ResponseEntity.ok(_usuarioService.getCurrentUsuario());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-usuario")
    public ResponseEntity<Object> getUsuario(@RequestParam(value = "usuarioId") Long usuarioId) {
        try {
            return ResponseEntity.ok(_usuarioService.getUsuario(usuarioId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/list-usuario")
    public ResponseEntity<Object> listUsuario() {
        try {
            return ResponseEntity.ok(_usuarioService.listUsuario());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            _usuarioService.signUp(signUpDTO);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/update-usuario")
    public  ResponseEntity<Object> updateUsuario(@RequestBody UpdateUsuarioDTO updateUsuarioDTO) {
        try {
            return ResponseEntity.ok(_usuarioService.updateUsuario(updateUsuarioDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
