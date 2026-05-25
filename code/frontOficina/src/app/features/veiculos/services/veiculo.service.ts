import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VeiculoPage } from '../models/veiculo-page.model';
import { VeiculoDetail } from '../models/veiculo-detail.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VeiculoService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/api/veiculos`;

  findAllVeiculos(page: number = 0, placa?: string, statusVeiculo?: string[]): Observable<VeiculoPage> {
    let params = new HttpParams().append('page', page.toString());
      
    if (statusVeiculo && statusVeiculo.length > 0) {
      statusVeiculo.forEach(status => {
        params = params.append('statusVeiculo', status);
      });
    }

    if (placa && placa.trim().length > 0) {
      params = params.append('placa', placa.trim());
    }

    return this.http.get<VeiculoPage>(this.API_URL, { params });
  }

  getVeiculoByPlaca(placa: string): Observable<VeiculoDetail> {
    return this.http.get<VeiculoDetail>(`${this.API_URL}/${placa}`);
  }
}
