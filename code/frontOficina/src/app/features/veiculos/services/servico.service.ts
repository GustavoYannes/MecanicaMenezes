import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servico } from '../models/servico.model';
import { ServicoCreateRequest } from '../models/servico-create-request.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ServicoService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/api/servico`;

  getServicosPorEntrada(entradaId: number): Observable<Servico[]> {
    const params = new HttpParams().set('entradaid', entradaId.toString());
    return this.http.get<Servico[]>(`${this.API_URL}/por-entrada`, { params });
  }

  createServico(payload: ServicoCreateRequest): Observable<any> {
    return this.http.post(this.API_URL, payload);
  }

  editarServico(servicoId: number, data: any): Observable<any> {
    return this.http.put(`${this.API_URL}/${servicoId}`, data);
  }
}
