import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VehicleListView } from './vehicle-list-view';
import { VeiculoService } from '../../services/veiculo.service';
import { VehicleModalService } from '../../services/vehicle-modal.service';
import { of, throwError } from 'rxjs';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

describe('VehicleListView Component', () => {
  let component: VehicleListView;
  let fixture: ComponentFixture<VehicleListView>;
  let veiculoService: VeiculoService;
  let modalService: VehicleModalService;

  const mockVeiculoPage = {
    content: [
      { placa: 'ABC1234', modelo: 'Gol', ano: 2020, cor: 'Prata', status: 'CONCLUIDO', marca: 'VW' }
    ],
    totalElements: 1,
    totalPages: 1,
    first: true,
    last: true,
    empty: false,
    number: 0,
    numberOfElements: 1,
    size: 10
  } as any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehicleListView],
      providers: [
        VeiculoService,
        VehicleModalService,
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(VehicleListView);
    component = fixture.componentInstance;
    veiculoService = TestBed.inject(VeiculoService);
    modalService = TestBed.inject(VehicleModalService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load vehicles on init', () => {
    const spy = vi.spyOn(veiculoService, 'findAllVeiculos').mockReturnValue(of(mockVeiculoPage));
    
    component.ngOnInit();
    
    expect(spy).toHaveBeenCalledWith(0, '', []);
    expect(component.veiculos().length).toBe(1);
    expect(component.loading()).toBe(false);
  });

  it('should handle error when loading vehicles', () => {
    vi.spyOn(veiculoService, 'findAllVeiculos').mockReturnValue(throwError(() => new Error('API Error')));
    
    component.loadVeiculos();
    
    expect(component.error()).toBe(true);
    expect(component.loading()).toBe(false);
  });

  it('should update search query and reload', () => {
    const spy = vi.spyOn(veiculoService, 'findAllVeiculos').mockReturnValue(of(mockVeiculoPage));
    
    component.onSearch('ABC');
    
    expect(component.searchQuery).toBe('ABC');
    expect(component.currentPage()).toBe(0);
    expect(spy).toHaveBeenCalledWith(0, 'ABC', []);
  });

  it('should handle empty state', () => {
    vi.spyOn(veiculoService, 'findAllVeiculos').mockReturnValue(of({ 
      content: [], totalElements: 0, totalPages: 0, first: true, last: true, 
      empty: true, number: 0, numberOfElements: 0, size: 10 
    } as any));
    
    component.loadVeiculos();
    
    expect(component.empty()).toBe(true);
    expect(component.veiculos().length).toBe(0);
  });

  it('should open modal when register button is clicked', () => {
    const modalSpy = vi.spyOn(modalService, 'open');
    
    component.openRegistrarVeiculo();
    
    expect(modalSpy).toHaveBeenCalled();
  });
});
