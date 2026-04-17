package com.oficinaMenezes.backoficina.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.oficinaMenezes.backoficina.models.entities.Veiculo;
@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, String>, JpaSpecificationExecutor<Veiculo>{
    
}
