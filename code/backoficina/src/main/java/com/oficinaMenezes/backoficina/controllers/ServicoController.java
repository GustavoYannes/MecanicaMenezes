package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.infra.security.TokenService;
import com.oficinaMenezes.backoficina.models.dtos.servico.CreateServicoDTO;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.services.ServicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/servico")
@Tag(name = "Servico")
public class ServicoController {

    private ServicoService servicoService;
    private TokenService tokenService;

    public ServicoController(ServicoService servicoService, TokenService tokenService) {
        this.servicoService = servicoService;
        this.tokenService = tokenService;
    }

    @PostMapping()
    public ResponseEntity<Servico> novoServico(@RequestBody @Valid CreateServicoDTO data, @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ", "");
        UUID uuid = tokenService.getUuidFromToken(token);
        Servico newServico = servicoService.criarServico(data, uuid);
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
