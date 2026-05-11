package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.models.dtos.Cliente.ClienteResponse;
import com.oficinaMenezes.backoficina.models.dtos.Cliente.ListClienteDTO;
import com.oficinaMenezes.backoficina.services.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) String cpf,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(
                clienteService.buscarTodosClientes(cpf, nomeCompleto, page)
        );
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteResponse> buscarClienteCpf(@PathVariable String cpf) {
        ClienteResponse data = clienteService.buscarClienteCpf(cpf);
        return ResponseEntity.ok(data);
    }

}
