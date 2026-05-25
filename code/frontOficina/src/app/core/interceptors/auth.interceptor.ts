import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../services/token.service';
import { AuthService } from '../services/auth.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);
  const injector = inject(Injector);
  const token = tokenService.getToken();

  let clonedReq = req;
  if (token) {
    clonedReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
  }

  return next(clonedReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // Ignorar requisições para o próprio endpoint de login
      if (req.url.includes('/api/auth/login')) {
        return throwError(() => error);
      }

      const isUnauthorizedStatus = error.status === 401;
      
      let isTokenInvalidMessage = false;
      if (error.error) {
        if (typeof error.error === 'string') {
          isTokenInvalidMessage = error.error.includes('Token inválido ou expirado');
        } else if (error.error.message) {
          isTokenInvalidMessage = String(error.error.message).includes('Token inválido ou expirado');
        }
      }

      // Se for 401, ou 0 (CORS bloqueado por falta de header de autorização no 401 do Spring), ou a mensagem de erro
      if (isUnauthorizedStatus || isTokenInvalidMessage || error.status === 0) {
        try {
          const authService = injector.get(AuthService);
          authService.logout();
        } catch (e) {
          console.warn('Fallback do logout após erro de Injeção de Dependência:', e);
          tokenService.clearAll();
        }
        
        router.navigate(['/login']);
      }
      
      return throwError(() => error);
    })
  );
};
