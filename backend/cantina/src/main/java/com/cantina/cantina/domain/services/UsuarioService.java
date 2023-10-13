package com.cantina.cantina.domain.services;

import com.cantina.cantina.domain.models.dtos.SignUpDTO;
import com.cantina.cantina.domain.models.dtos.UpdateUsuarioDTO;
import com.cantina.cantina.domain.models.dtos.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    void deleteUsuario(Long usuarioId);
    UsuarioDTO getCurrentUsuario();
    UsuarioDTO getUsuario(Long usuarioId);
    List<UsuarioDTO> listaUsuario();
    void signUp(SignUpDTO signUpDTO);
    UpdateUsuarioDTO updateUsuario(UpdateUsuarioDTO updateUsuarioDTO);

}
