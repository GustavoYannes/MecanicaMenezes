import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClientePageResponse } from '../models/cliente-page.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/cliente';

  getClientes(page: number = 0, nomeCompleto: string = ''): Observable<ClientePageResponse> {
    let params = new HttpParams().set('page', page.toString());
    
    if (nomeCompleto) {
      params = params.set('nomeCompleto', nomeCompleto);
    }

    return this.http.get<ClientePageResponse>(this.API_URL, { params });
  }
}
