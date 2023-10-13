package com.cantina.cantina.application.config.security.jwt.handler;

import com.cantina.cantina.application.config.security.jwt.ServletUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    private static Logger logger = LoggerFactory.getLogger(UnauthorizedHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.warn("UnauthorizedHandler, exception: " + authException);

        // Chamado se token errado ou ausente
        String json = ServletUtil.getJson("error", "Não autorizado.");
        ServletUtil.write(response, HttpStatus.FORBIDDEN, json);
    }

}
