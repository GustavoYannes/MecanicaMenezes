package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.models.entities.Cliente;
import com.oficinaMenezes.backoficina.services.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
@Tag(name = "Servico")
public class ClienteController {


    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> findAllClientes(@RequestParam(required = false) String nomeCompleto) {
        List<Cliente> clientes = clienteService.buscarTodosClientes(nomeCompleto);
        return ResponseEntity.ok().body(clientes);
    }

}
