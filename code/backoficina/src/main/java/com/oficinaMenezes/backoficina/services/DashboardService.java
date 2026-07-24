package com.oficinaMenezes.backoficina.services;

import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardEntradasResponse;
import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardEvolucaoItemResponse;
import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardEvolucaoResponse;
import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardResponse;
import com.oficinaMenezes.backoficina.models.dtos.dashboard.DashboardResumoResponse;
import com.oficinaMenezes.backoficina.models.entities.Entrada;
import com.oficinaMenezes.backoficina.models.entities.Servico;
import com.oficinaMenezes.backoficina.models.entities.enums.EStatusEntrada;
import com.oficinaMenezes.backoficina.models.exceptions.dashboard.PeriodoDashboardInvalidoException;
import com.oficinaMenezes.backoficina.repositories.EntradaRepository;
import com.oficinaMenezes.backoficina.repositories.ServicoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DashboardService {

    private final EntradaRepository entradaRepository;
    private final ServicoRepository servicoRepository;

    public DashboardService(EntradaRepository entradaRepository, ServicoRepository servicoRepository) {
        this.entradaRepository = entradaRepository;
        this.servicoRepository = servicoRepository;
    }

    public DashboardResponse buscarDashboard(LocalDate inicio, LocalDate fim) {
        PeriodoDashboard periodo = normalizarPeriodo(inicio, fim);
        List<Entrada> entradasFechadas = entradaRepository.findByStatusAndDataSaidaBetween(
                EStatusEntrada.FECHADA,
                periodo.inicio(),
                periodo.fim()
        );
        List<Servico> servicos = servicoRepository.findByDataBetween(periodo.inicio(), periodo.fim());

        long entradasAbertas = entradaRepository.countByStatusAndDataEntradaBetween(
                EStatusEntrada.ABERTA,
                periodo.inicio(),
                periodo.fim()
        );
        long quantidadeEntradasFechadas = entradaRepository.countByStatusAndDataSaidaBetween(
                EStatusEntrada.FECHADA,
                periodo.inicio(),
                periodo.fim()
        );

        return new DashboardResponse(
                periodo.inicio(),
                periodo.fim(),
                new DashboardResumoResponse(
                        quantidadeEntradasFechadas,
                        calcularTempoMedioPermanencia(entradasFechadas)
                ),
                new DashboardEntradasResponse(entradasAbertas, quantidadeEntradasFechadas),
                new DashboardEvolucaoResponse(
                        montarEvolucaoDiaria(servicos, periodo),
                        montarEvolucaoSemanal(servicos, periodo),
                        montarEvolucaoMensal(servicos, periodo)
                )
        );
    }

    private PeriodoDashboard normalizarPeriodo(LocalDate inicio, LocalDate fim) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataFim = fim == null ? hoje : fim;
        LocalDate dataInicio = inicio == null ? dataFim.withDayOfMonth(1) : inicio;

        if (dataInicio.isAfter(dataFim)) {
            throw new PeriodoDashboardInvalidoException();
        }

        return new PeriodoDashboard(dataInicio, dataFim);
    }

    private BigDecimal calcularTempoMedioPermanencia(List<Entrada> entradasFechadas) {
        if (entradasFechadas.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        double mediaDias = entradasFechadas.stream()
                .filter(entrada -> entrada.getDataEntrada() != null && entrada.getDataSaida() != null)
                .mapToLong(entrada -> ChronoUnit.DAYS.between(entrada.getDataEntrada(), entrada.getDataSaida()))
                .average()
                .orElse(0);

        return BigDecimal.valueOf(mediaDias).setScale(2, RoundingMode.HALF_UP);
    }

    private List<DashboardEvolucaoItemResponse> montarEvolucaoDiaria(List<Servico> servicos, PeriodoDashboard periodo) {
        List<PeriodoAgrupado> periodos = periodo.inicio()
                .datesUntil(periodo.fim().plusDays(1))
                .map(data -> new PeriodoAgrupado(data.toString(), data, data))
                .toList();

        return montarEvolucao(servicos, this::chaveDiaria, periodos);
    }

    private List<DashboardEvolucaoItemResponse> montarEvolucaoSemanal(List<Servico> servicos, PeriodoDashboard periodo) {
        LocalDate primeiraSemana = periodo.inicio().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<PeriodoAgrupado> periodos = Stream.iterate(primeiraSemana, data -> data.plusWeeks(1))
                .takeWhile(inicioSemana -> !inicioSemana.isAfter(periodo.fim()))
                .map(inicioSemana -> criarPeriodoSemanal(inicioSemana, inicioSemana))
                .toList();

        return montarEvolucao(servicos, this::chaveSemanal, periodos);
    }

    private List<DashboardEvolucaoItemResponse> montarEvolucaoMensal(List<Servico> servicos, PeriodoDashboard periodo) {
        YearMonth primeiroMes = YearMonth.from(periodo.inicio());
        YearMonth ultimoMes = YearMonth.from(periodo.fim());

        List<PeriodoAgrupado> periodos = Stream.iterate(primeiroMes, mes -> mes.plusMonths(1))
                .takeWhile(mes -> !mes.isAfter(ultimoMes))
                .map(this::criarPeriodoMensal)
                .toList();

        return montarEvolucao(servicos, this::chaveMensal, periodos);
    }

    private List<DashboardEvolucaoItemResponse> montarEvolucao(
            List<Servico> servicos,
            Function<Servico, PeriodoAgrupado> agrupador,
            List<PeriodoAgrupado> periodos
    ) {
        Map<PeriodoAgrupado, List<Servico>> servicosPorPeriodo = servicos.stream()
                .collect(Collectors.groupingBy(
                        agrupador,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return periodos.stream()
                .sorted(Comparator.comparing(PeriodoAgrupado::inicio))
                .map(periodo -> criarItemEvolucao(
                        periodo,
                        servicosPorPeriodo.getOrDefault(periodo, List.of())
                ))
                .toList();
    }

    private DashboardEvolucaoItemResponse criarItemEvolucao(PeriodoAgrupado periodo, List<Servico> servicos) {
        int quantidadeServicos = servicos.stream()
                .mapToInt(Servico::getQuantidade)
                .sum();

        BigDecimal faturamento = servicos.stream()
                .map(Servico::valorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardEvolucaoItemResponse(
                periodo.nome(),
                periodo.inicio(),
                periodo.fim(),
                quantidadeServicos,
                faturamento
        );
    }

    private PeriodoAgrupado chaveDiaria(Servico servico) {
        LocalDate data = servico.getData();
        return new PeriodoAgrupado(data.toString(), data, data);
    }

    private PeriodoAgrupado chaveSemanal(Servico servico) {
        LocalDate inicioSemana = servico.getData().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return criarPeriodoSemanal(servico.getData(), inicioSemana);
    }

    private PeriodoAgrupado chaveMensal(Servico servico) {
        return criarPeriodoMensal(YearMonth.from(servico.getData()));
    }

    private PeriodoAgrupado criarPeriodoSemanal(LocalDate dataReferencia, LocalDate inicioSemana) {
        LocalDate fimSemana = inicioSemana.plusDays(6);
        WeekFields weekFields = WeekFields.ISO;
        int semana = dataReferencia.get(weekFields.weekOfWeekBasedYear());
        int ano = dataReferencia.get(weekFields.weekBasedYear());

        return new PeriodoAgrupado(String.format("%d-W%02d", ano, semana), inicioSemana, fimSemana);
    }

    private PeriodoAgrupado criarPeriodoMensal(YearMonth mes) {
        return new PeriodoAgrupado(mes.toString(), mes.atDay(1), mes.atEndOfMonth());
    }

    private record PeriodoDashboard(LocalDate inicio, LocalDate fim) {
    }

    private record PeriodoAgrupado(String nome, LocalDate inicio, LocalDate fim) {
    }
}
