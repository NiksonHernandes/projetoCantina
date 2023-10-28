package com.cantina.cantina.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email = "";
    private String username = "";
    private String nomeCompleto = "";

    @JsonIgnore
    private String senha;

    private String cpf = "";
    private String sexo = "";
    private Integer semestreAtual = 0;
    private String curso = "";
    private String rua = "";
    private String bairro = "";
    private String telefone = "";
    private String celular = "";

    //lista de roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<Role>();

    //Relacionamento com o histórico de pedidos
//    @OneToOne(cascade = CascadeType.ALL)
//    private HistoricoPedidos historicoPedidos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return this.roles; }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    @Override
    public String getPassword() { return this.senha; }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() { return true; }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() { return true; }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @JsonIgnore
    @Override
    public boolean isEnabled() { return true; }

}
