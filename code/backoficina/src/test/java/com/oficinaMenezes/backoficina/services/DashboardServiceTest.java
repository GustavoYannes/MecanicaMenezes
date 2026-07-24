package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardEvolucaoItemResponse;
import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardResponse;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusEntrada;
import com.oficinaMenezes.backoficina.models.exceptions.dashboard.PeriodoDashboardInvalidoException;
import com.oficinaMenezes.backoficina.repositories.EntradaRepository;
import com.oficinaMenezes.backoficina.repositories.ServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private EntradaRepository entradaRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void deveBuscarDashboardComResumoEntradasTempoMedioEEvolucao() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 10);

        Entrada entradaUm = criarEntradaFechada(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 3)
        );
        Entrada entradaDois = criarEntradaFechada(
                LocalDate.of(2026, 1, 5),
                LocalDate.of(2026, 1, 8)
        );

        Servico servicoUm = criarServico(LocalDate.of(2026, 1, 1), 2, "100.00");
        Servico servicoDois = criarServico(LocalDate.of(2026, 1, 8), 1, "50.00");

        when(entradaRepository.findByStatusAndDataSaidaBetween(EStatusEntrada.FECHADA, inicio, fim))
                .thenReturn(List.of(entradaUm, entradaDois));
        when(servicoRepository.findByDataBetween(inicio, fim))
                .thenReturn(List.of(servicoUm, servicoDois));
        when(entradaRepository.countByStatusAndDataEntradaBetween(EStatusEntrada.ABERTA, inicio, fim))
                .thenReturn(3L);
        when(entradaRepository.countByStatusAndDataSaidaBetween(EStatusEntrada.FECHADA, inicio, fim))
                .thenReturn(2L);

        DashboardResponse response = dashboardService.buscarDashboard(inicio, fim);

        assertEquals(inicio, response.inicio());
        assertEquals(fim, response.fim());
        assertEquals(2L, response.resumo().quantidadeCarrosAtendidos());
        assertEquals(new BigDecimal("2.50"), response.resumo().tempoMedioPermanenciaDias());
        assertEquals(3L, response.entradas().abertas());
        assertEquals(2L, response.entradas().fechadas());

        assertEquals(10, response.evolucao().diaria().size());
        assertEvolucao(response.evolucao().diaria().get(0), "2026-01-01", inicio, inicio, 2, "200.00");
        assertEvolucao(
                response.evolucao().diaria().get(2),
                "2026-01-03",
                LocalDate.of(2026, 1, 3),
                LocalDate.of(2026, 1, 3),
                0,
                "0"
        );
        assertEvolucao(
                response.evolucao().diaria().get(7),
                "2026-01-08",
                LocalDate.of(2026, 1, 8),
                LocalDate.of(2026, 1, 8),
                1,
                "50.00"
        );

        assertEquals(2, response.evolucao().semanal().size());
        assertEvolucao(
                response.evolucao().semanal().get(0),
                "2026-W01",
                LocalDate.of(2025, 12, 29),
                LocalDate.of(2026, 1, 4),
                2,
                "200.00"
        );
        assertEvolucao(
                response.evolucao().semanal().get(1),
                "2026-W02",
                LocalDate.of(2026, 1, 5),
                LocalDate.of(2026, 1, 11),
                1,
                "50.00"
        );

        assertEquals(1, response.evolucao().mensal().size());
        assertEvolucao(
                response.evolucao().mensal().get(0),
                "2026-01",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31),
                3,
                "250.00"
        );
    }

    @Test
    void deveRetornarZerosQuandoNaoExistemDadosNoPeriodo() {
        LocalDate inicio = LocalDate.of(2026, 2, 1);
        LocalDate fim = LocalDate.of(2026, 2, 3);

        when(entradaRepository.findByStatusAndDataSaidaBetween(EStatusEntrada.FECHADA, inicio, fim))
                .thenReturn(List.of());
        when(servicoRepository.findByDataBetween(inicio, fim))
                .thenReturn(List.of());
        when(entradaRepository.countByStatusAndDataEntradaBetween(EStatusEntrada.ABERTA, inicio, fim))
                .thenReturn(0L);
        when(entradaRepository.countByStatusAndDataSaidaBetween(EStatusEntrada.FECHADA, inicio, fim))
                .thenReturn(0L);

        DashboardResponse response = dashboardService.buscarDashboard(inicio, fim);

        assertEquals(0L, response.resumo().quantidadeCarrosAtendidos());
        assertEquals(new BigDecimal("0.00"), response.resumo().tempoMedioPermanenciaDias());
        assertEquals(0L, response.entradas().abertas());
        assertEquals(0L, response.entradas().fechadas());
        assertEquals(3, response.evolucao().diaria().size());
        response.evolucao().diaria().forEach(item -> {
            assertEquals(0, item.quantidadeServicos());
            assertEquals(BigDecimal.ZERO, item.faturamento());
        });
    }

    @Test
    void deveUsarMesAtualQuandoPeriodoNaoForInformado() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);

        when(entradaRepository.findByStatusAndDataSaidaBetween(EStatusEntrada.FECHADA, inicioMes, hoje))
                .thenReturn(List.of());
        when(servicoRepository.findByDataBetween(inicioMes, hoje))
                .thenReturn(List.of());

        DashboardResponse response = dashboardService.buscarDashboard(null, null);

        assertEquals(inicioMes, response.inicio());
        assertEquals(hoje, response.fim());
        verify(entradaRepository).countByStatusAndDataEntradaBetween(EStatusEntrada.ABERTA, inicioMes, hoje);
        verify(entradaRepository).countByStatusAndDataSaidaBetween(EStatusEntrada.FECHADA, inicioMes, hoje);
    }

    @Test
    void deveUsarInicioDoMesDaDataFimQuandoSomenteFimForInformado() {
        LocalDate fim = LocalDate.of(2026, 3, 20);
        LocalDate inicioEsperado = LocalDate.of(2026, 3, 1);

        when(entradaRepository.findByStatusAndDataSaidaBetween(EStatusEntrada.FECHADA, inicioEsperado, fim))
                .thenReturn(List.of());
        when(servicoRepository.findByDataBetween(inicioEsperado, fim))
                .thenReturn(List.of());

        DashboardResponse response = dashboardService.buscarDashboard(null, fim);

        assertEquals(inicioEsperado, response.inicio());
        assertEquals(fim, response.fim());
        verify(entradaRepository).countByStatusAndDataEntradaBetween(EStatusEntrada.ABERTA, inicioEsperado, fim);
        verify(entradaRepository).countByStatusAndDataSaidaBetween(EStatusEntrada.FECHADA, inicioEsperado, fim);
    }

    @Test
    void deveLancarExcecaoQuandoInicioForPosteriorAoFim() {
        LocalDate inicio = LocalDate.of(2026, 4, 10);
        LocalDate fim = LocalDate.of(2026, 4, 1);

        assertThrows(
                PeriodoDashboardInvalidoException.class,
                () -> dashboardService.buscarDashboard(inicio, fim)
        );

        verifyNoInteractions(entradaRepository, servicoRepository);
    }

    private Entrada criarEntradaFechada(LocalDate dataEntrada, LocalDate dataSaida) {
        Entrada entrada = new Entrada(null);
        ReflectionTestUtils.setField(entrada, "status", EStatusEntrada.FECHADA);
        ReflectionTestUtils.setField(entrada, "dataEntrada", dataEntrada);
        ReflectionTestUtils.setField(entrada, "dataSaida", dataSaida);
        return entrada;
    }

    private Servico criarServico(LocalDate data, int quantidade, String valor) {
        Servico servico = new Servico(null, null, "Servico teste", quantidade, new BigDecimal(valor));
        ReflectionTestUtils.setField(servico, "data", data);
        return servico;
    }

    private void assertEvolucao(
            DashboardEvolucaoItemResponse item,
            String periodo,
            LocalDate inicio,
            LocalDate fim,
            int quantidadeServicos,
            String faturamento
    ) {
        assertEquals(periodo, item.periodo());
        assertEquals(inicio, item.inicio());
        assertEquals(fim, item.fim());
        assertEquals(quantidadeServicos, item.quantidadeServicos());
        assertEquals(new BigDecimal(faturamento), item.faturamento());
    }
}
