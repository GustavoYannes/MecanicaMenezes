package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.dtos.auth.RegistrarFuncionarioDTO;
import com.oficinaMenezes.backoficina.models.dtos.funcionario.FuncionarioResponse;
import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.models.entities.Gerente;
import com.oficinaMenezes.backoficina.models.entities.Mecanico;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.models.entities.enums.ERole;
import com.oficinaMenezes.backoficina.models.exceptions.auth.UsuarioJaExisteException;
import com.oficinaMenezes.backoficina.models.specifications.FuncionarioSpec;
import com.oficinaMenezes.backoficina.repositories.FuncionarioRepository;
import com.oficinaMenezes.backoficina.repositories.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FuncionarioService {

    private FuncionarioRepository funcionarioRepository;
    private ServicoRepository servicoRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, ServicoRepository servicoRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.servicoRepository = servicoRepository;
    }

    public Funcionario findByUUID(UUID id) {
        return funcionarioRepository.findById(id).orElse(null);
    }

    public Page<FuncionarioResponse> listarFuncionarios(String nome, int page, ERole cargo) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("nome").ascending());

        Specification<Funcionario> spec = Specification
                .where(FuncionarioSpec.nomeContains(nome)
                        .and(FuncionarioSpec.roleConstains(cargo)));

        Page<Funcionario> listaFuncionario = funcionarioRepository.findAll(spec, pageable);

        LocalDate dataInicio = LocalDate.now().withDayOfMonth(1);
        LocalDate dataFim = LocalDate.now().withDayOfMonth(
                LocalDate.now().lengthOfMonth()
        );

        return listaFuncionario.map(funcionario -> {
            List<Servico> servicosDoMes = servicoRepository
                    .findByFuncionarioAndDataBetween(
                            funcionario,
                            dataInicio,
                            dataFim
                    );

            BigDecimal totalGeradoMensal = servicosDoMes.stream()
                    .map(Servico::valorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Long totalServicoMensal = servicosDoMes.stream()
                    .mapToLong(Servico::getQuantidade)
                    .sum();

            return new FuncionarioResponse(
                    funcionario.getNome(),
                    totalGeradoMensal,
                    totalServicoMensal
            );
        });
    }

    public Mecanico criarMecanico(RegistrarFuncionarioDTO data){

        if (this.funcionarioRepository.findByCpf(data.cpf()) != null) throw new UsuarioJaExisteException();
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        Mecanico mecanico = new Mecanico(
                data.cpf(),
                data.nome(),
                data.telefone(),
                data.email(),
                encryptedPassword
        );

        return this.funcionarioRepository.save(mecanico);

    }

}
