import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EntradaCreateRequest } from '../models/entrada-create-request.model';
import { EntradaAberta } from '../models/entrada-aberta.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EntradaService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/entrada`;

  createEntrada(payload: EntradaCreateRequest): Observable<any> {
    return this.http.post(this.API_URL, payload);
  }

  getEntradaAberta(placaVeiculo: string): Observable<EntradaAberta> {
    const params = new HttpParams().set('placaVeiculo', placaVeiculo);
    return this.http.get<EntradaAberta>(`${this.API_URL}/entrada-aberta`, { params });
  }

  getEntradasPorVeiculo(placaVeiculo: string, page: number = 0): Observable<any> {
    const params = new HttpParams()
      .set('placaVeiculo', placaVeiculo)
      .set('page', page.toString());
    return this.http.get<any>(`${this.API_URL}/entrada-por-veiculo`, { params });
  }

  liberarVeiculo(idEntrada: number): Observable<any> {
    const params = new HttpParams().set('idEntrada', idEntrada.toString());
    return this.http.patch(`${this.API_URL}/liberarVeiculo`, {}, { params });
  }

  gerarPdfEntrada(idEntrada: number): Observable<Blob> {
    const params = new HttpParams().set('idEntrada', idEntrada.toString());
    return this.http.get(`${this.API_URL}/gerarPDF`, { params, responseType: 'blob' });
  }
}
