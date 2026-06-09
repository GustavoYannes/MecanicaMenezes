import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { TokenService } from '../services/token.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  const allowedRoles = route.data?.['roles'] as string[];
  const userRole = tokenService.getUserRole();

  if (userRole && allowedRoles && allowedRoles.includes(userRole)) {
    return true;
  }

  // Se não for gerente, redireciona para a página principal permitida (dashboard)
  return router.parseUrl('/dashboard');
};
