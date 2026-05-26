import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FuncionarioPage, RegistrarFuncionarioRequest } from '../models/funcionario.model';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/mecanico';

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
}
