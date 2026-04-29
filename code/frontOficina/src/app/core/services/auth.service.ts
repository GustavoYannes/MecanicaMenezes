import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable, tap } from 'rxjs';
import { TokenService } from './token.service';

export interface LoginRequest {
  cpf: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private tokenService = inject(TokenService);
  private apiUrl = environment.apiUrl;

  constructor() { }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/api/auth/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          this.tokenService.setToken(response.token);
          this.decodeAndStoreUserInfo(response.token);
        }
      })
    );
  }

  logout(): void {
    this.tokenService.clearAll();
  }

  private decodeAndStoreUserInfo(token: string): void {
    try {
      // JWT is separated by dots. The payload is the second part.
      const payloadBase64 = token.split('.')[1];
      if (payloadBase64) {
        // Base64 decoding string handles URI encoding safely
        const decodedJson = decodeURIComponent(atob(payloadBase64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        const payload = JSON.parse(decodedJson);
        
        if (payload.nome) {
          this.tokenService.setUserName(payload.nome);
        }
      }
    } catch (error) {
      console.error('Error decoding JWT token', error);
    }
  }

  isAuthenticated(): boolean {
    return !!this.tokenService.getToken();
  }
}
