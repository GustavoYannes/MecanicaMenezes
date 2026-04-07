package com.oficinaMenezes.backoficina.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.oficinaMenezes.backoficina.models.dtos.entrada.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.services.EntradaService;

import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/entrada")
public class EntradaController {
    private EntradaService entradaService;

    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }

    @PostMapping
    public ResponseEntity<Entrada> criarEntrada(@RequestBody @Valid CreateEntradaDTO data) {
        Entrada newEntrada = entradaService.criarEntrada(data);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newEntrada.getId())
                .toUri();
        return ResponseEntity.created(location).body(newEntrada);
    }

    @PatchMapping("/liberarVeiculo")
    public ResponseEntity<Entrada> finalizarEntrada(@RequestParam Long idEntrada){
        Entrada entrada = entradaService.finalizarEntrada(idEntrada);
        return ResponseEntity.ok(entrada);
    }

    @GetMapping("/gerarPDF")
    public ResponseEntity<byte[]> gerarPDF(@RequestParam Long idEntrada){
        byte[] pdf = entradaService.gerarPdf(idEntrada);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orcamento-" + idEntrada + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
}
