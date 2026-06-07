import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FuncionarioPage, RegistrarFuncionarioRequest, FuncionarioResponse, FuncionarioPerfilResponse } from '../models/funcionario.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/api/mecanico`;

  getFuncionarios(nome: string, page: number): Observable<FuncionarioPage> {
    let params = new HttpParams().set('page', page.toString());
    if (nome) {
      params = params.set('nome', nome);
    }
    return this.http.get<FuncionarioPage>(this.API_URL, { params });
  }

  registrarFuncionario(data: RegistrarFuncionarioRequest): Observable<void> {
    return this.http.post<void>(this.API_URL, data);
  }

  getFuncionarioLogado(): Observable<FuncionarioPerfilResponse> {
    return this.http.get<FuncionarioPerfilResponse>(`${environment.apiUrl}/api/funcionario/id`);
  }
}
