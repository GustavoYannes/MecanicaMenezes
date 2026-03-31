package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.models.dtos.servico.CreateServicoDTO;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.services.ServicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/servico")
@Tag(name = "Servico")
public class ServicoController {

    private ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping()
    public ResponseEntity<Servico> novoSerico(@RequestBody @Valid CreateServicoDTO data){
        Servico newServico = servicoService.criarServico(data);
        if (newServico == null) {
            return ResponseEntity.badRequest().body(null);
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newServico.getId())
                .toUri();
        return ResponseEntity.created(location).body(newServico);
    }
}
