import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HistoricoVeiculos } from './historico-veiculos';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

describe('HistoricoVeiculos Component', () => {
  let component: HistoricoVeiculos;
  let fixture: ComponentFixture<HistoricoVeiculos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoricoVeiculos],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HistoricoVeiculos);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
