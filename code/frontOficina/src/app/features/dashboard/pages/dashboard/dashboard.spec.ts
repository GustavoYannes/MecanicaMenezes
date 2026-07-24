import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Dashboard } from './dashboard';
import { DashboardService } from '../../services/dashboard.service';
import { of, throwError } from 'rxjs';
import { DashboardData } from '../../models/dashboard.model';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('Dashboard Component', () => {
  let component: Dashboard;
  let fixture: ComponentFixture<Dashboard>;
  let dashboardService: DashboardService;

  const mockDashboardData: DashboardData = {
    inicio: '2026-05-01',
    fim: '2026-05-30',
    resumo: {
      quantidadeCarrosAtendidos: 3,
      tempoMedioPermanenciaDias: 0.67
    },
    entradas: {
      abertas: 0,
      fechadas: 3
    },
    evolucao: {
      diaria: [
        { periodo: '2026-05-01', inicio: '2026-05-01', fim: '2026-05-01', quantidadeServicos: 5, faturamento: 100 }
      ],
      semanal: [],
      mensal: []
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        DashboardService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
    dashboardService = TestBed.inject(DashboardService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load dashboard data on init', () => {
    const spy = vi.spyOn(dashboardService, 'getDashboardData').mockReturnValue(of(mockDashboardData));
    
    component.ngOnInit();
    
    expect(spy).toHaveBeenCalled();
    expect(component.dashboardData()).toEqual(mockDashboardData);
    expect(component.loading()).toBe(false);
  });

  it('should handle error when loading data', () => {
    vi.spyOn(dashboardService, 'getDashboardData').mockReturnValue(throwError(() => new Error('API Error')));
    
    component.loadDashboardData();
    
    expect(component.error()).toBe(true);
    expect(component.loading()).toBe(false);
  });

  it('should update chart when data is loaded', () => {
    vi.spyOn(dashboardService, 'getDashboardData').mockReturnValue(of(mockDashboardData));
    const chartSpy = vi.spyOn(component, 'updateChart');
    
    component.loadDashboardData();
    
    expect(chartSpy).toHaveBeenCalled();
    expect(component.chartOptions()).toBeTruthy();
  });

  it('should change evolution type', () => {
    vi.spyOn(dashboardService, 'getDashboardData').mockReturnValue(of(mockDashboardData));
    component.loadDashboardData();
    
    component.setEvolucaoTipo('semanal');
    expect(component.evolucaoTipo()).toBe('semanal');
  });

  it('should change metric type', () => {
    vi.spyOn(dashboardService, 'getDashboardData').mockReturnValue(of(mockDashboardData));
    component.loadDashboardData();
    
    component.setMetricaTipo('quantidadeServicos');
    expect(component.metricaTipo()).toBe('quantidadeServicos');
  });

  it('should calculate first and last day of month', () => {
    // @ts-ignore - accessing private methods for testing
    const firstDay = component['getFirstDayOfMonth']();
    // @ts-ignore
    const lastDay = component['getLastDayOfMonth']();
    
    expect(firstDay).toMatch(/^\d{4}-\d{2}-01$/);
    expect(lastDay).toMatch(/^\d{4}-\d{2}-\d{2}$/);
  });
});
