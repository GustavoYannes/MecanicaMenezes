import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EntradaCreateRequest } from '../models/entrada-create-request.model';

@Injectable({
  providedIn: 'root'
})
export class EntradaService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/entrada';

  createEntrada(payload: EntradaCreateRequest): Observable<any> {
    return this.http.post(this.API_URL, payload);
  }
}
