import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService } from '../../services/dashboard.service';
import { DashboardData, DashboardEvolucaoItem } from '../../models/dashboard.model';
import { NgApexchartsModule, ChartComponent } from 'ng-apexcharts';
import { 
  ApexAxisChartSeries, 
  ApexChart, 
  ApexXAxis, 
  ApexDataLabels, 
  ApexStroke, 
  ApexYAxis, 
  ApexTitleSubtitle, 
  ApexLegend,
  ApexTooltip,
  ApexGrid,
  ApexTheme,
  ApexFill,
  ApexPlotOptions
} from 'ng-apexcharts';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  yaxis: ApexYAxis | ApexYAxis[];
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
  legend: ApexLegend;
  tooltip: ApexTooltip;
  colors: string[];
  fill: ApexFill;
  plotOptions: ApexPlotOptions;
};

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NgApexchartsModule],
  templateUrl: './dashboard.html',
  styles: [`
    :host {
      display: block;
      height: 100%;
      padding: 1.5rem;
    }
  `],
})
export class Dashboard implements OnInit {
  private dashboardService = inject(DashboardService);

  loading = signal(true);
  error = signal(false);
  dashboardData = signal<DashboardData | null>(null);

  // Filtros de data
  inicio = signal(this.getFirstDayOfMonth());
  fim = signal(this.getLastDayOfMonth());
  evolucaoTipo = signal<'diaria' | 'semanal' | 'mensal'>('diaria');
  metricaTipo = signal<'faturamento' | 'quantidadeServicos'>('faturamento');

  // Configuração do gráfico de evolução
  chartOptions = signal<Partial<ChartOptions> | null>(null);

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.loading.set(true);
    this.error.set(false);

    this.dashboardService.getDashboardData(this.inicio(), this.fim()).subscribe({
      next: (data) => {
        this.dashboardData.set(data);
        this.updateChart();
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      }
    });
  }

  setEvolucaoTipo(tipo: 'diaria' | 'semanal' | 'mensal') {
    this.evolucaoTipo.set(tipo);
    this.updateChart();
  }

  setMetricaTipo(tipo: 'faturamento' | 'quantidadeServicos') {
    this.metricaTipo.set(tipo);
    this.updateChart();
  }

  updateChart() {
    const data = this.dashboardData();
    if (!data) return;

    const items = data.evolucao[this.evolucaoTipo()];
    const metrica = this.metricaTipo();
    const isFaturamento = metrica === 'faturamento';
    
    // Altura responsiva baseada no tamanho da tela (aproximado)
    const isMobile = window.innerWidth < 768;
    
    this.chartOptions.set({
      series: [
        {
          name: isFaturamento ? 'Faturamento' : 'Quantidade de Serviços',
          type: isFaturamento ? 'area' : 'bar',
          data: items.map(item => isFaturamento ? item.faturamento : item.quantidadeServicos)
        }
      ],
      chart: {
        height: isMobile ? 300 : 380,
        type: isFaturamento ? 'area' : 'bar',
        toolbar: { show: false },
        fontFamily: 'inherit',
        sparkline: { enabled: false },
        animations: {
          enabled: true,
          speed: 800,
          animateGradually: { enabled: true, delay: 150 },
          dynamicAnimation: { enabled: true, speed: 350 }
        }
      },
      colors: [isFaturamento ? '#EA580C' : '#0F172A'],
      stroke: {
        width: isFaturamento ? 3 : 0,
        curve: 'smooth'
      },
      dataLabels: {
        enabled: false
      },
      fill: {
        type: isFaturamento ? 'gradient' : 'solid',
        gradient: {
          shadeIntensity: 1,
          opacityFrom: 0.45,
          opacityTo: 0.05,
          stops: [20, 100, 100, 100]
        }
      },
      xaxis: {
        categories: items.map(item => {
          if (this.evolucaoTipo() === 'diaria') {
            const date = new Date(item.periodo);
            return date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
          }
          return item.periodo;
        }),
        axisBorder: { show: false },
        axisTicks: { show: false }
      },
      yaxis: {
        title: { 
          text: isFaturamento ? 'Faturamento (R$)' : 'Qtd Serviços', 
          style: { color: isFaturamento ? '#EA580C' : '#0F172A', fontWeight: 600 } 
        },
        labels: { 
          formatter: (val) => isFaturamento 
            ? `R$ ${val.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`
            : `${val}`,
          style: { colors: isFaturamento ? '#EA580C' : '#0F172A' }
        }
      },
      tooltip: {
        shared: true,
        intersect: false,
        theme: 'light',
        y: {
          formatter: (val) => isFaturamento 
            ? `R$ ${val.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`
            : `${val} serviços`
        }
      },
      grid: {
        borderColor: '#E5E7EB',
        strokeDashArray: 4,
        padding: { left: 20, right: 20 }
      },
      legend: { show: false }
    });
  }

  private getFirstDayOfMonth(): string {
    const now = new Date();
    return new Date(now.getFullYear(), now.getMonth(), 1).toISOString().split('T')[0];
  }

  private getLastDayOfMonth(): string {
    const now = new Date();
    return new Date(now.getFullYear(), now.getMonth() + 1, 0).toISOString().split('T')[0];
  }

  onDateChange(event: Event, type: 'inicio' | 'fim') {
    const val = (event.target as HTMLInputElement).value;
    if (type === 'inicio') this.inicio.set(val);
    else this.fim.set(val);
    this.loadDashboardData();
  }
}
