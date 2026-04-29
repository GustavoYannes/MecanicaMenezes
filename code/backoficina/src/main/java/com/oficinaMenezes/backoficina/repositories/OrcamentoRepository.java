package com.oficinaMenezes.backoficina.repositories;

import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, String> {

    @Query("SELECT COALESCE(SUM(o.valorTotal), 0) FROM Orcamento o WHERE o.cpfCliente = :cpf")
    BigDecimal somarValorTotalPorCpf(@Param("cpf") String cpf);

    Orcamento findByEntrada(Entrada entrada);

}
