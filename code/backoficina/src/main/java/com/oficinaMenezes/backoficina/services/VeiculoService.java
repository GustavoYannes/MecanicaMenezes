package com.oficinaMenezes.backoficina.services;

import java.util.List;
import java.util.Optional;

import com.oficinaMenezes.backoficina.models.dtos.veiculo.ListVeiculoResponse;
import com.oficinaMenezes.backoficina.models.dtos.veiculo.VeiculoResponse;
import com.oficinaMenezes.backoficina.models.specifications.ClienteSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.oficinaMenezes.backoficina.models.dtos.entrada.CreateEntradaDTO;
import com.oficinaMenezes.backoficina.models.entities.Cliente;
import com.oficinaMenezes.backoficina.models.entities.Veiculo;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusVeiculo;
import com.oficinaMenezes.backoficina.models.exceptions.entrada.VeiculoEmAtendimentoException;
import com.oficinaMenezes.backoficina.models.specifications.VeiculoSpec;
import com.oficinaMenezes.backoficina.repositories.VeiculoRepository;

@Service
public class VeiculoService {
    private ClienteService clienteService;
    private VeiculoRepository veiculoRepository;
   

    public VeiculoService(VeiculoRepository veiculoRepository,  
            ClienteService clienteService) {
        this.veiculoRepository = veiculoRepository;
        this.clienteService = clienteService;
    }

    public Veiculo verificarVeiculo(Veiculo veiculo) {
        if(veiculo.getStatus() == EStatusVeiculo.EMPROGRESSO){
            throw new VeiculoEmAtendimentoException();
        }
        if(veiculo.getStatus() == EStatusVeiculo.ESPERA){
            throw new VeiculoEmAtendimentoException();
        }
        if(veiculo.getStatus() == EStatusVeiculo.CONCLUIDO){
            veiculo.novaEntrada();
        }

        return veiculoRepository.save(veiculo);
    }

    public Veiculo buscarVeiculo(CreateEntradaDTO data) {
        Optional<Veiculo> veiculo = veiculoRepository.findById(data.placa());
        if (veiculo.isEmpty()) {
            return criarVeiculo(data);
        }
        return verificarVeiculo(veiculo.get());
    }

    public Veiculo criarVeiculo(CreateEntradaDTO data) {
       Cliente cliente = clienteService.buscarCliente(data);
       
        Veiculo veiculo = new Veiculo(
                data.placa(),
                data.marca(),
                data.modelo(),
                data.ano(),
                data.cor(),
                data.km(),
                cliente

        );
        return veiculoRepository.save(veiculo);
    }

    public EStatusVeiculo primeiroServico(Veiculo veiculo){
        veiculo.PrimeiroServico();
        veiculoRepository.save(veiculo);
        return veiculo.getStatus();
    }

    public EStatusVeiculo liberarVeiculo(Veiculo veiculo){
        EStatusVeiculo status = veiculo.liberarVeiculo();
        veiculoRepository.save(veiculo);
        return status;
    }

    public Page<ListVeiculoResponse> findAll(List<EStatusVeiculo> statusVeiculo, String placa, int page){
        Pageable pageable = PageRequest.of(page, 5, Sort.by("placa").ascending());
        Specification<Veiculo> spec = Specification
                .where(VeiculoSpec.statusConstains(statusVeiculo))
                .and(VeiculoSpec.placaConstains(placa));


        Page<Veiculo> veiculosPage = veiculoRepository.findAll(spec, pageable);
        return veiculosPage.map(Veiculo::toListVeiculoResponse);
    }

    public Veiculo findByPlaca(String placa){
        Veiculo veiculo = veiculoRepository.findByPlaca(placa);
        if(veiculo == null){throw new VeiculoEmAtendimentoException();}
        return veiculo;
    }
}
