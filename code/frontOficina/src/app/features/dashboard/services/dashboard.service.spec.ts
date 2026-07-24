import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DashboardService } from './dashboard.service';
import { environment } from '../../../../environments/environment';
import { DashboardData } from '../models/dashboard.model';

describe('DashboardService', () => {
  let service: DashboardService;
  let httpMock: HttpTestingController;

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
      diaria: [],
      semanal: [],
      mensal: []
    }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [DashboardService]
    });
    service = TestBed.inject(DashboardService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch dashboard data with dates', () => {
    const inicio = '2026-05-01';
    const fim = '2026-05-30';

    service.getDashboardData(inicio, fim).subscribe(data => {
      expect(data).toEqual(mockDashboardData);
    });

    const req = httpMock.expectOne(request => 
      request.url === `${environment.apiUrl}/dashboard` &&
      request.params.get('inicio') === inicio &&
      request.params.get('fim') === fim
    );
    expect(req.request.method).toBe('GET');
    req.flush(mockDashboardData);
  });

  it('should fetch dashboard data without dates', () => {
    service.getDashboardData().subscribe(data => {
      expect(data).toEqual(mockDashboardData);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/dashboard`);
    expect(req.request.method).toBe('GET');
    expect(req.request.params.has('inicio')).toBe(false);
    expect(req.request.params.has('fim')).toBe(false);
    req.flush(mockDashboardData);
  });
});
