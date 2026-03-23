package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.dtos.auth.RegistrarFuncionarioDTO;
import com.oficinaMenezes.backoficina.models.entities.Gerente;
import com.oficinaMenezes.backoficina.repositories.FuncionarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private FuncionarioRepository funcionarioRepository;

    public AuthenticationService (FuncionarioRepository funcionarioRepository){
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        return funcionarioRepository.findByCpf(cpf);
    }

    public Gerente registrarGerente(RegistrarFuncionarioDTO data) {

        if (this.funcionarioRepository.findByCpf(data.cpf()) != null) return null;
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        Gerente gerente = new Gerente(
                data.cpf(),
                data.nome(),
                data.telefone(),
                data.email(),
                encryptedPassword
        );

        return this.funcionarioRepository.save(gerente);
    }
}
