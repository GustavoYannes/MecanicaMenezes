package com.oficinaMenezes.backoficina.repositories;

import org.springframework.stereotype.Repository;

import com.oficinaMenezes.backoficina.models.Entrada;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long>{
    
}
