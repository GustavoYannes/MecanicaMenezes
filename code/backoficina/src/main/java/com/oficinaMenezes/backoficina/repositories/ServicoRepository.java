package com.oficinaMenezes.backoficina.repositories;

import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> , JpaSpecificationExecutor<Servico> {

    boolean existsByEntrada(Entrada entrada);
    List<Servico> findByEntradaId(Long entradaId);
    List<Servico> findByFuncionarioAndDataBetween(
            Funcionario funcionario,
            LocalDate dataInicio,
            LocalDate dataFim
    );

    List<Servico> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

}
