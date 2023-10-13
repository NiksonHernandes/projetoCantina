package com.cantina.cantina.application.config.security;

import com.cantina.cantina.application.config.security.jwt.JwtAuthenticationFilter;
import com.cantina.cantina.application.config.security.jwt.JwtAuthorizationFilter;
import com.cantina.cantina.application.config.security.jwt.handler.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl _userDetailsServiceImpl;

    @Autowired
    private UnauthorizedHandler _unauthorizedHandler;

    @Autowired
    private AccessDeniedHandler _accesDeniedHandler;

    @Bean //para dizer pro Spring gerenciar ele e podermos criar ponto de injeção nele
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.GET, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/sign-up").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/questionario/get-questionario").permitAll()

                        //.requestMatchers(HttpMethod.GET, "/user/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated() //todos endpoints precisam ser autenticados
                )
                .cors().and().csrf().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), _userDetailsServiceImpl))
                .exceptionHandling()
                .accessDeniedHandler(_accesDeniedHandler)
                .authenticationEntryPoint(_unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
