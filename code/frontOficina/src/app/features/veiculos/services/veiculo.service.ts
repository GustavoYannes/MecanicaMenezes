import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VeiculoPage } from '../models/veiculo-page.model';

@Injectable({
  providedIn: 'root'
})
export class VeiculoService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/veiculos';

  findAllVeiculos(page: number = 0, placa?: string): Observable<VeiculoPage> {
    let params = new HttpParams()
      .append('statusVeiculo', 'ESPERA')
      .append('statusVeiculo', 'EMPROGRESSO')
      .append('page', page.toString());
      
    if (placa && placa.trim().length > 0) {
      params = params.append('placa', placa.trim());
    }

    return this.http.get<VeiculoPage>(this.API_URL, { params });
  }
}
