package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.models.dtos.Cliente.ListClienteDTO;
import com.oficinaMenezes.backoficina.services.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente")
@Tag(name = "Servico")
public class ClienteController {


    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<Page<ListClienteDTO>> buscarTodosClientes(
            @RequestParam(required = false) String nomeCompleto,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(
                clienteService.buscarTodosClientes(nomeCompleto, page)
        );
    }

}
