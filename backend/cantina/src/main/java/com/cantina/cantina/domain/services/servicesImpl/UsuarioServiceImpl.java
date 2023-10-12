package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.UsuarioRepository;
import com.cantina.cantina.domain.models.Usuario;
import com.cantina.cantina.domain.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository _usuarioRepository;

    @Override
    public List<Usuario> listaUsuario() {
        List<Usuario> listaUsuario = _usuarioRepository.findAll();

        if (listaUsuario.isEmpty()) {
            throw new IllegalArgumentException("Lista Vazia!");
        }

        return listaUsuario;
    }

}
