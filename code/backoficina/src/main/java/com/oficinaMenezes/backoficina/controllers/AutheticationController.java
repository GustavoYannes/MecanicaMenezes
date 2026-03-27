package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.infra.security.TokenService;
import com.oficinaMenezes.backoficina.models.dtos.auth.AuthenticationDTO;
import com.oficinaMenezes.backoficina.models.dtos.auth.LoginResponseDTO;
import com.oficinaMenezes.backoficina.models.dtos.auth.RegistrarFuncionarioDTO;
import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Auth")
public class AutheticationController {

    private AuthenticationManager authenticationManager;
    private AuthenticationService authenticationService;
    private TokenService tokenService;

    public AutheticationController(AuthenticationManager authenticationManager, AuthenticationService authenticationService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data){

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.cpf(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Funcionario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Funcionario> registrar(@RequestBody @Valid RegistrarFuncionarioDTO data){
        Funcionario funcionario = authenticationService.registrarGerente(data);
        if(funcionario == null) return ResponseEntity.badRequest().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(funcionario.getUuid())
                .toUri();

        return ResponseEntity.created(location).body(funcionario);
    }


}
