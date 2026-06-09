import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servico } from '../models/servico.model';
import { ServicoCreateRequest } from '../models/servico-create-request.model';
import { environment } from '../../../../environments/environment';
import { PageResponse, ServicoResponse, RelatorioMensalResponse } from '../../funcionarios/models/funcionario.model';

@Injectable({
  providedIn: 'root'
})
export class ServicoService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/servico`;

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

  deletarServico(servicoId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${servicoId}`);
  }

  getServicosPaginados(inicio: string, fim: string, uuidFuncionario: string, page: number): Observable<PageResponse<ServicoResponse>> {
    let params = new HttpParams()
      .set('inicio', inicio)
      .set('fim', fim)
      .set('uuidFuncionario', uuidFuncionario)
      .set('page', page.toString());
    return this.http.get<PageResponse<ServicoResponse>>(this.API_URL, { params });
  }

  getRelatorioMensal(inicio: string, fim: string, uuidFuncionario: string): Observable<RelatorioMensalResponse> {
    let params = new HttpParams()
      .set('inicio', inicio)
      .set('fim', fim)
      .set('uuidFuncionario', uuidFuncionario);
    return this.http.get<RelatorioMensalResponse>(`${this.API_URL}/relatorioMensal`, { params });
  }
}
