package com.oficinaMenezes.backoficina.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oficinaMenezes.backoficina.models.entities.Veiculo;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusVeiculo;
import com.oficinaMenezes.backoficina.services.VeiculoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<List<Veiculo>> findAllVeiculos(@RequestParam  (required = false) List<EStatusVeiculo> statusVeiculo) {
        List<Veiculo> veiculos = veiculoService.findAll(statusVeiculo);
        return ResponseEntity.ok().body(veiculos);
    
    }
    
    
}
