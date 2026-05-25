package com.oficinaMenezes.backoficina.repositories;

import com.oficinaMenezes.backoficina.models.entities.Veiculo;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusEntrada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.oficinaMenezes.backoficina.models.entities.Entrada;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long>{

    Optional<Entrada> findByVeiculoAndStatus(Veiculo veiculo, EStatusEntrada status);

    Page<Entrada> findByVeiculo(Veiculo veiculo, Pageable pageable);
    
}
