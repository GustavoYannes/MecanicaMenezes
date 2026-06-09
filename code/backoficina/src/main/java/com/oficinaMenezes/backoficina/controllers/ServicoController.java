package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.infra.security.TokenService;
import com.oficinaMenezes.backoficina.models.dtos.servico.CreateServicoDTO;
import com.oficinaMenezes.backoficina.models.dtos.servico.EditarServicoDTO;
import com.oficinaMenezes.backoficina.models.dtos.servico.ServicoResponse;
import com.oficinaMenezes.backoficina.models.dtos.servico.ServicoTotalDataResponse;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Funcionario;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.services.EntradaService;
import com.oficinaMenezes.backoficina.services.FuncionarioService;
import com.oficinaMenezes.backoficina.services.ServicoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/servico")
@Tag(name = "Servico")
public class ServicoController {

    private final ServicoService servicoService;
    private final TokenService tokenService;
    private final FuncionarioService funcionarioService;
    private final EntradaService entradaService;

    public ServicoController(ServicoService servicoService, TokenService tokenService, FuncionarioService funcionarioService, EntradaService entradaService) {
        this.servicoService = servicoService;
        this.tokenService = tokenService;
        this.funcionarioService = funcionarioService;
        this.entradaService = entradaService;
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

    @PutMapping("/{servicoId}")
    public ResponseEntity<ServicoResponse> editarServico(@PathVariable Long servicoId, @RequestBody @Valid EditarServicoDTO data){
        ServicoResponse servico = servicoService.editarServico(data, servicoId);
        return ResponseEntity.ok(servico);
    }

    @DeleteMapping("/{servicoId}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long servicoId) {
        servicoService.deletarServico(servicoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-entrada")
    public ResponseEntity<List<ServicoResponse>> listarServicosPorEntrada(@RequestParam Long entradaid){
        List<Servico> listaServicos = servicoService.servicoPorEntrada(entradaid);
        List<ServicoResponse> response = listaServicos.stream()
                .map(Servico::toServicoResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/relatorioMensal")
    public ResponseEntity<ServicoTotalDataResponse> buscarRelatorioPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam UUID uuidFuncionario

    ) {
        Funcionario funcionario = funcionarioService.getFuncionario(uuidFuncionario);
        ServicoTotalDataResponse response = servicoService.servicoPorDataFuncionario(inicio, fim, funcionario);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ServicoResponse>> getServico(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim,

            @RequestParam(required = false)
            UUID uuidFuncionario,

            @RequestParam(required = false)
            Long idEntrada,

            @RequestParam(defaultValue = "0") int page
    ) {
        Funcionario funcionario = null;
        Entrada entrada = null;

        if (uuidFuncionario != null) {
            funcionario = funcionarioService.getFuncionario(uuidFuncionario);
        }

        if (idEntrada != null) {
            entrada = entradaService.getById(idEntrada);
        }

        Page<ServicoResponse> servicos = servicoService.getServico(
                inicio,
                fim,
                funcionario,
                entrada,
                page
        );

        return ResponseEntity.ok(servicos);
    }
}