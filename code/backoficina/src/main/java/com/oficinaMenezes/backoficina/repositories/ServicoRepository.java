package com.oficinaMenezes.backoficina.repositories;

import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    boolean existsByEntrada(Entrada entrada);
    List<Servico> findByEntradaId(Long entradaId);

}
