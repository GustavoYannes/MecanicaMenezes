package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.models.dtos.auth.RegistrarFuncionarioDTO;
import com.oficinaMenezes.backoficina.models.dtos.funcionario.FuncionarioListResponse;
import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.models.entities.enums.ERole;
import com.oficinaMenezes.backoficina.services.FuncionarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/mecanico")
@Tag(name = "Mecanico")
public class MecanicoController {

    private FuncionarioService funcionarioService;

    public MecanicoController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<Funcionario> criarMecanico(@RequestBody @Valid RegistrarFuncionarioDTO data){
        Funcionario funcionario = funcionarioService.criarMecanico(data);
        if(funcionario == null) return ResponseEntity.badRequest().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(funcionario.getUuid())
                .toUri();

        return ResponseEntity.created(location).body(funcionario);
    }

    @GetMapping
    public ResponseEntity<Page<FuncionarioListResponse>> getAllMecanicos(
            @RequestParam(required = false) String nome,
            @RequestParam(defaultValue = "0") int page
    ){
        return ResponseEntity.ok(
                funcionarioService.listarFuncionarios(nome, page, ERole.MECANICO)
        );
    }
}
