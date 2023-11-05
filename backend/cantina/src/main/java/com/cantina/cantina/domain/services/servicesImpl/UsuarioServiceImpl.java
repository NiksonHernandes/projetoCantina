package com.cantina.cantina.domain.services.servicesImpl;

import com.cantina.cantina.data.repositories.HistoricoPedidosRepository;
import com.cantina.cantina.data.repositories.RoleRepository;
import com.cantina.cantina.data.repositories.UsuarioRepository;
import com.cantina.cantina.domain.models.HistoricoPedidos;
import com.cantina.cantina.domain.models.Role;
import com.cantina.cantina.domain.models.Usuario;
import com.cantina.cantina.domain.models.dtos.SignUpDTO;
import com.cantina.cantina.domain.models.dtos.UpdateUsuarioDTO;
import com.cantina.cantina.domain.models.dtos.UsuarioDTO;
import com.cantina.cantina.domain.services.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository _usuarioRepository;

    @Autowired
    private HistoricoPedidosRepository _historicoPedidosRepository;

    @Autowired
    private RoleRepository _roleRepository;

    @Override
    @Transactional
    public void deleteUsuario(Long usuarioId) {
        if (usuarioId.toString().isEmpty()) {
            throw new IllegalArgumentException("Id do usuário não informado.");
        }

        Usuario usuario = _usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do usuário não foi encontrado."));

        //testar pra ver se deleta o histórico de pedidos dele
        Optional<HistoricoPedidos> historicoPedidos = _historicoPedidosRepository.findByUsuario_Id(usuario.getId());

        if (historicoPedidos.isPresent()) {
            _historicoPedidosRepository.delete(historicoPedidos.get());
        } else {
            throw new IllegalArgumentException("Histórico de pedidos não encontrado.");
        }

        _usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioDTO getCurrentUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        Usuario usuario = _usuarioRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("O ID do usuário não foi encontrado."));

        return UsuarioDTO.toDTO(usuario);
    }

    @Override
    public UsuarioDTO getUsuario(Long usuarioId) {
        Usuario usuario = _usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("O ID do usuário não foi encontrado."));

        return UsuarioDTO.toDTO(usuario);
    }

    @Override
    public List<UsuarioDTO> listUsuario() {
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

       //Adiciona a role padrão de user
       List<Role> roles = new ArrayList<>();
       roles.add(_roleRepository.findByName("ROLE_USER"));
       usuario.setRoles(roles);

        //criptografa a senha
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String senhaCriptografada = bCryptPasswordEncoder.encode(signUpDTO.getSenha());
        usuario.setSenha(senhaCriptografada);

        Usuario novoUsuario = _usuarioRepository.save(usuario);

        //Gera um novo ID do histórico de pedido para ele
        HistoricoPedidos historicoPedidos = new HistoricoPedidos();
        historicoPedidos.setUsuario(novoUsuario);

        HistoricoPedidos novoHistorico = _historicoPedidosRepository.save(historicoPedidos);
    }

    @Override
    @Transactional
    public UpdateUsuarioDTO updateUsuario(UpdateUsuarioDTO updateUsuarioDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario currentUser = (Usuario) authentication.getPrincipal(); //pega o usuário autenticado

        Usuario usuarioModel = _usuarioRepository.findById(updateUsuarioDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("O ID do usuário não foi encontrado."));

        //Não pode alterar o usuário do amiguinho - Somente o ADM
        if (!(updateUsuarioDTO.getId().equals(currentUser.getId()))
                && currentUser.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("Não permitido! Você só pode alterar o seu usuário.");
        }

        if (updateUsuarioDTO.getEmail().equals("") || updateUsuarioDTO.getUsername().equals("") ||
                updateUsuarioDTO.getNomeCompleto().equals("")) {
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

//        if (!updateUsuarioDTO.getSenha().equals(updateUsuarioDTO.getSenhaConfirmacao())) {
//            throw new IllegalArgumentException("Senhas diferentes.");
//        }

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

//        if (!updateUsuarioDTO.getSenha().isEmpty()) {
//            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//            String senhaCriptografada = bCryptPasswordEncoder.encode(updateUsuarioDTO.getSenha());
//            usuarioModel.setSenha(senhaCriptografada);
//        }

        usuarioModel = _usuarioRepository.save(usuarioModel);

        return UpdateUsuarioDTO.toDTO(usuarioModel);
    }

}
