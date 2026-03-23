package com.oficinaMenezes.backoficina.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.oficinaMenezes.backoficina.dtos.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.Entrada;
import com.oficinaMenezes.backoficina.services.EntradaService;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/entrada")
public class EntradaController {
    private EntradaService entradaService;

    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }

    @PostMapping()
    public ResponseEntity<Entrada> criarEntrada(@RequestBody CreateEntradaDTO data) {
        Entrada newEntrada = entradaService.criarEntrada(data);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEntrada.getId())
                .toUri();
        return ResponseEntity.created(location).body(newEntrada);
    }
    
}
