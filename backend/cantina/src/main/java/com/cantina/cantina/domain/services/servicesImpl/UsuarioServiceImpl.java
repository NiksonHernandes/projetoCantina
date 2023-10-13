package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.UsuarioRepository;
import com.cantina.cantina.domain.models.Usuario;
import com.cantina.cantina.domain.models.dtos.SignUpDTO;
import com.cantina.cantina.domain.models.dtos.UpdateUsuarioDTO;
import com.cantina.cantina.domain.models.dtos.UsuarioDTO;
import com.cantina.cantina.domain.services.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.net.Authenticator;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository _usuarioRepository;

    @Override
    @Transactional
    public void deleteUsuario(Long usuarioId) {
        if (usuarioId.toString().isEmpty()) {
            throw new IllegalArgumentException("Id do usuário não informado.");
        }

        Usuario usuario = _usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do usuário não foi encontrado."));

        _usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioDTO getCurrentUsuario() {
        return null;
    }

    @Override
    public List<UsuarioDTO> listaUsuario() {
        List<Usuario> listaUsuario = _usuarioRepository.findAll();

        if (listaUsuario.isEmpty()) {
            throw new IllegalArgumentException("Lista Vazia!");
        }

        return UsuarioDTO.toListDTO(listaUsuario);
    }

    @Override
    @Transactional
    public void signUp(SignUpDTO signUpDTO) {
        if (signUpDTO.getEmail().equals("") || signUpDTO.getUsername().equals("") ||
                signUpDTO.getNomeCompleto().equals("") || signUpDTO.getSenha().equals("")) {
            throw new IllegalArgumentException("Campos vazios.");
        }

        if (_usuarioRepository.existsByEmail(signUpDTO.getEmail())){
            throw new IllegalArgumentException("E-mail já existente!");
        }

        if (_usuarioRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new IllegalArgumentException("Username já existente!");
        }

       if (!signUpDTO.getSenha().equals(signUpDTO.getSenhaConfirmacao())) {
           throw new IllegalArgumentException("Senhas diferentes.");
       }

       //Converte o DTO para salvar no BD
       Usuario usuario = new Usuario();
       usuario.setEmail(signUpDTO.getEmail());
       usuario.setUsername(signUpDTO.getUsername());
       usuario.setNomeCompleto(signUpDTO.getNomeCompleto());

       usuario.setSenha(signUpDTO.getSenha());
        //criptografa a senha
        /*BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String senhaCriptografada = bCryptPasswordEncoder.encode(signUpDTO.getSenha());
        usuario.setSenha(senhaCriptografada);*/

        _usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public UpdateUsuarioDTO updateUsuario(UpdateUsuarioDTO updateUsuarioDTO) {
        Usuario usuarioModel = _usuarioRepository.findById(updateUsuarioDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("O ID do usuário não foi encontrado."));

        if (updateUsuarioDTO.getEmail().equals("") || updateUsuarioDTO.getUsername().equals("") ||
                updateUsuarioDTO.getNomeCompleto().equals("") || updateUsuarioDTO.getSenha().equals("")) {
            throw new IllegalArgumentException("Campos vazios.");
        }

        Optional<Usuario> findUsuarioByUsername = _usuarioRepository.findByUsername(updateUsuarioDTO.getUsername());

        if (findUsuarioByUsername.isPresent() && !findUsuarioByUsername.get().getId().equals(updateUsuarioDTO.getId())) {
            throw new IllegalArgumentException("Username já existente!");
        }

        Optional<Usuario> findUserByEmail = _usuarioRepository.findByEmail(updateUsuarioDTO.getEmail());

        if (findUserByEmail.isPresent() && !findUserByEmail.get().getId().equals(updateUsuarioDTO.getId())) {
            throw new IllegalArgumentException("E-mail já existente!");
        }

        if (!updateUsuarioDTO.getSenha().equals(updateUsuarioDTO.getSenhaConfirmacao())) {
            throw new IllegalArgumentException("Senhas diferentes.");
        }

        usuarioModel.setNomeCompleto(updateUsuarioDTO.getNomeCompleto());
        usuarioModel.setEmail(updateUsuarioDTO.getEmail());
        usuarioModel.setUsername(updateUsuarioDTO.getUsername());
        usuarioModel.setCpf(updateUsuarioDTO.getCpf());
        usuarioModel.setSexo(updateUsuarioDTO.getSexo());
        usuarioModel.setSemestreAtual(updateUsuarioDTO.getSemestreAtual());
        usuarioModel.setCurso(updateUsuarioDTO.getCurso());
        usuarioModel.setRua(updateUsuarioDTO.getRua());
        usuarioModel.setBairro(updateUsuarioDTO.getBairro());
        usuarioModel.setTelefone(updateUsuarioDTO.getTelefone());
        usuarioModel.setCelular(updateUsuarioDTO.getCelular());

        usuarioModel.setSenha(updateUsuarioDTO.getSenha());

        usuarioModel = _usuarioRepository.save(usuarioModel);

        return UpdateUsuarioDTO.toDTO(usuarioModel);
    }

}
