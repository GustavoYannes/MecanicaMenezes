import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface CepAddress {
  cep: string;
  logradouro: string;
  bairro: string;
  localidade: string;
  uf: string;
  erro?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class CepService {
  private http = inject(HttpClient);

  buscar(cep: string): Observable<CepAddress> {
    const cleanCep = cep.replace(/\D/g, '');
    const protocol = typeof window !== 'undefined' && window.location.protocol === 'https:' ? 'https' : 'http';

    return this.http.get<CepAddress>(`${protocol}://viacep.com.br/ws/${cleanCep}/json/`);
  }
}
