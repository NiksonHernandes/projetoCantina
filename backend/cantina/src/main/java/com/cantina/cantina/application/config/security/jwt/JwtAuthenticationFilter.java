package com.cantina.cantina.application.config.security.jwt;

import com.cantina.cantina.domain.models.Usuario;
import com.cantina.cantina.domain.models.dtos.SsoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTH_URL = "/login";

    private final AuthenticationManager _authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        _authenticationManager = authenticationManager;

        setFilterProcessesUrl(AUTH_URL); //fala que a URL será responsável pelo END-point de login
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        try {
            //transforma um string JSON em JAVA
            JwtLoginInput login = new ObjectMapper().readValue(request.getInputStream(), JwtLoginInput.class);
            String username = login.getUsername();
            String senha = login.getSenha();

            if(StringUtils.isEmpty(username) || StringUtils.isEmpty(senha)) {
                throw new BadCredentialsException("Username/senha inválido!");
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(username, senha);

            return _authenticationManager.authenticate(auth);
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        String jwtToken = JwtUtil.createToken(usuario);

        SsoDTO ssoDTO = new SsoDTO();
        ssoDTO.setCurrent_user(usuario);
        ssoDTO.setAccess_token(jwtToken);

        String json = ssoDTO.toJson();
        ServletUtil.write(response, HttpStatus.OK, json);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException error) throws IOException, ServletException {
        String json = ServletUtil.getJson("error", "Login incorreto");
        ServletUtil.write(response, HttpStatus.UNAUTHORIZED, json);
    }

}

