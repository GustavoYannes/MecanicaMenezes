package com.oficinaMenezes.backoficina.models.entities;

import com.oficinaMenezes.backoficina.models.entities.enums.ERole;
import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "funcionario")
@Inheritance(strategy = jakarta.persistence.InheritanceType.JOINED)
public class Funcionario implements UserDetails {
    @Id
    protected String cpf;
    @Column(name = "nome", nullable = false)
    protected String nome;
    @Column(name = "telefone", nullable = false)
    protected String telefone;
    @Column(name = "email", nullable = false)
    protected String email;
    @Column(name = "senha", nullable = false)
    protected String senha;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    protected ERole role;

    public Funcionario() {}

    public Funcionario(String cpf, String nome, String telefone, String email, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == ERole.GERENTE) return List.of(new SimpleGrantedAuthority("ROLE_GERENTE"), new SimpleGrantedAuthority("ROLE_MECANICO"), new SimpleGrantedAuthority("ROLE_ASSISTENTE"));
        if (this.role == ERole.MECANICO) return List.of(new SimpleGrantedAuthority("ROLE_MECANICO"));
        else return List.of(new SimpleGrantedAuthority("ROLE_ASSISTENTE"));
    }

    @Override
    public @Nullable String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return cpf;
    }
}
