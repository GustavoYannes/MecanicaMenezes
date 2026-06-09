import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClienteSearchPage } from '../models/cliente-search-result.model';
import { ClienteDetail } from '../models/cliente-detail.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClienteLookupService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/cliente`;

  searchByCpf(cpf: string): Observable<ClienteSearchPage> {
    return this.http.get<ClienteSearchPage>(this.API_URL, {
      params: { cpf }
    });
  }

  getByCpf(cpf: string): Observable<ClienteDetail> {
    return this.http.get<ClienteDetail>(`${this.API_URL}/${cpf}`);
  }
}
