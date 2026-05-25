package com.oficinaMenezes.backoficina.controllers;

import com.oficinaMenezes.backoficina.models.dtos.Cliente.ClienteResponse;
import com.oficinaMenezes.backoficina.models.dtos.veiculo.ListVeiculoResponse;
import com.oficinaMenezes.backoficina.models.dtos.veiculo.VeiculoResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusVeiculo;
import com.oficinaMenezes.backoficina.services.VeiculoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("api/veiculos")
@Tag(name = "Veiculos")
public class VeiculoController {
    
    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public ResponseEntity<Page<ListVeiculoResponse>> findAllVeiculos(
            @RequestParam  (required = false) List<EStatusVeiculo> statusVeiculo,
            @RequestParam  (required = false) String placa,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(
                veiculoService.findAll(statusVeiculo, placa, page)
        );
    }

    @GetMapping("/{placa}")
    public ResponseEntity<VeiculoResponse> findByPlaca(@PathVariable String placa){
        VeiculoResponse veiculo = veiculoService.findByPlaca(placa).toVeiculoResponse();
        return ResponseEntity.ok().body(veiculo);
    }

}
