import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VeiculoSearchPage } from '../models/veiculo-search-result.model';
import { VeiculoDetail } from '../models/veiculo-detail.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VeiculoLookupService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/veiculos`;

  searchByPlaca(placa: string): Observable<VeiculoSearchPage> {
    return this.http.get<VeiculoSearchPage>(this.API_URL, {
      params: { placa }
    });
  }

  getByPlaca(placa: string): Observable<VeiculoDetail> {
    return this.http.get<VeiculoDetail>(`${this.API_URL}/${placa}`);
  }
}
