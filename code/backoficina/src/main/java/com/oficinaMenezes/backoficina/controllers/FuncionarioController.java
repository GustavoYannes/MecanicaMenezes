package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.infra.security.TokenService;
import com.oficinaMenezes.backoficina.models.dtos.funcionario.FuncionarioResponse;
import com.oficinaMenezes.backoficina.services.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/funcionario")
public class FuncionarioController {

    private FuncionarioService funcionarioService;
    private TokenService tokenService;

    public FuncionarioController(FuncionarioService funcionarioService, TokenService tokenService) {
        this.funcionarioService = funcionarioService;
        this.tokenService = tokenService;
    }

    @GetMapping("/id")
    public ResponseEntity<FuncionarioResponse> getFuncionario(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        UUID uuid = tokenService.getUuidFromToken(token);
        return ResponseEntity.ok(funcionarioService.getFuncionario(uuid).toResponse());
    }

}
