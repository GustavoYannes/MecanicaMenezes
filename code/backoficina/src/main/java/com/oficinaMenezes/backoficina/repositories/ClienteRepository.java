package com.oficinaMenezes.backoficina.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oficinaMenezes.backoficina.models.entities.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    
}
